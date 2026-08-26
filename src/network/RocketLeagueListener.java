package network;

import events.MatchEventListener;
import game.Game;
import game.GameParser;
import game.MatchAnalyzer;
import game.Player;
import matches.MatchResult;
import matches.MatchStorage;

import java.net.http.WebSocket;
import java.time.LocalDateTime;
import java.util.concurrent.CompletionStage;

public class RocketLeagueListener implements WebSocket.Listener {

    private final StringBuilder messageBuffer = new StringBuilder();
    private boolean connectionClosed = false;

    private final String playerName;
    private final MatchStorage storage;
    private final MatchEventListener listener;

    /*
     * Se ejecuta cuando el WebSocket se cierra.
     * RocketLeagueClient lo utilizará para volver
     * al bucle de conexión.
     */
    private final Runnable connectionClosedCallback;

    private MatchAnalyzer analyzer;
    private Thread connectionWatchdog;
    private static final int PLAYLIST_1V1 = 10;
    private static final int PLAYLIST_2V2 = 11;
    private static final int PLAYLIST_3V3 = 13;
    private static final long CONNECTION_TIMEOUT = 3000;
    private static final int MAX_PLAYER_TIME = 20;

    private Game game = null;

    private boolean isCompetitive = false;
    private String matchGuid;

    private int playerTeam = 1;
    private int playerTime = 0;
    private volatile long lastMessageTime;
    private boolean playerFound = false;
    private boolean matchActive = false;

    public RocketLeagueListener(
            String playerName,
            MatchStorage storage,
            MatchEventListener listener,
            Runnable connectionClosedCallback
    ) {
        this.playerName = playerName;
        this.storage = storage;
        this.listener = listener;
        this.connectionClosedCallback = connectionClosedCallback;
    }

    @Override
    public void onOpen(WebSocket webSocket) {

        connectionClosed = false;
        lastMessageTime = System.currentTimeMillis();

        connectionWatchdog =
                new Thread(
                        () -> watchConnection(webSocket),
                        "RocketLeagueWatchdog"
                );

        connectionWatchdog.setDaemon(true);
        connectionWatchdog.start();

        System.out.println(
                "Conectado a Rocket League."
        );

        webSocket.request(1);
    }

    @Override
    public CompletionStage<?> onText(
            WebSocket webSocket,
            CharSequence data,
            boolean last
    ) {

        messageBuffer.append(data);
        lastMessageTime = System.currentTimeMillis();
        if (last) {

            String rawMessage =
                    messageBuffer.toString();

            StatsMessage message =
                    StatsMessageParser.parse(rawMessage);

            handleMessage(message);

            messageBuffer.setLength(0);
        }

        webSocket.request(1);

        return null;
    }

    private void handleMessage(
            StatsMessage message
    ) {

        switch (message.getEvent()) {

            case "MatchCreated" ->
                    handleMatchCreated();

            case "UpdateState" ->
                    handleGameUpdate(message);

            case "MatchEnded" ->
                    handleMatchEnded(message);

            case "MatchDestroyed" ->
                    handleMatchDestroyed();

            default -> {
            }
        }
    }

    private void handleMatchCreated() {

        if (game != null) {
            return;
        }

        System.out.println(
                "Partida iniciada."
        );

        matchActive = true;
        playerFound = false;

        analyzer = null;
        matchGuid = null;

        playerTeam = -1;
        playerTime = 0;

        isCompetitive = false;
    }

    private void handleGameUpdate(
            StatsMessage message
    ) {

        if (!matchActive) {
            return;
        }

        game =
                GameParser.parse(
                        message.getData()
                );

        /*
         * Buscamos al jugador únicamente
         * durante los primeros estados.
         */
        if (
                playerTime < MAX_PLAYER_TIME
                        && !playerFound
        ) {

            playerTime++;

            isCompetitive =
                    game.getPlaylistId() == PLAYLIST_1V1
                            || game.getPlaylistId() == PLAYLIST_2V2
                            || game.getPlaylistId() == PLAYLIST_3V3;

            Player player =
                    findPlayer(game);

            if (player == null) {

                playerFound = false;

                if (playerTime == MAX_PLAYER_TIME) {

                    System.out.println(
                            "No se pudo encontrar al jugador: "
                                    + playerName
                    );
                }

                return;
            }

            System.out.println(
                    "Jugador "
                            + playerName
                            + " encontrado."
            );

            playerFound = true;

            listener.onMatchStarted(
                    game.getArena(),
                    game.getPlaylistId()
            );

            if (!isCompetitive) {

                System.out.println(
                        "La partida no es competitiva, "
                                + "no se guardaran datos"
                );
            }

            playerTeam =
                    player.getTeamNum();

            matchGuid =
                    game.getMatchGuid();

            analyzer =
                    new MatchAnalyzer(
                            player.getPrimaryId()
                    );
        }

        /*
         * Si no encontramos al jugador o la
         * partida no es competitiva,
         * ignoramos los siguientes estados.
         */
        if (
                !playerFound
                        || !isCompetitive
        ) {
            return;
        }

        analyzer.update(game);
    }

    private Player findPlayer(Game game) {

        for (Player player : game.getPlayers()) {

            if (
                    playerName.equals(
                            player.getName()
                    )
            ) {
                return player;
            }
        }

        return null;
    }

    private void handleMatchEnded(StatsMessage message) {

        if (!matchActive) {
            return;
        }

        System.out.println("Partida finalizada");

        matchActive = false;
        boolean won = GameParser.parseTeamNum(message.getData()) == playerTeam;
        System.out.println(won);
        if (
                !playerFound
                        || analyzer == null
                        || game == null
                        || !isCompetitive
        ) {

            resetMatch();
            return;
        }


        analyzer.finish();

        MatchResult result =
                new MatchResult(
                        LocalDateTime.now().toString(),
                        playerName,
                        matchGuid,
                        game.getPlaylistId(),
                        won,

                        analyzer.getAssists(),
                        analyzer.getSaves(),
                        analyzer.getShots(),
                        analyzer.getGoals(),
                        analyzer.getDemos(),

                        analyzer.getAirPercentage(),
                        analyzer.getSupersonicPercentage(),
                        analyzer.getBoostUsedSupersonic(),
                        analyzer.getSupersonicBoostSessionPercentage(),
                        analyzer.getAverageBoostToSupersonic(),
                        analyzer.getAverageSpeed()
                );

        storage.save(result);

        listener.onMatchFinished(result);

        resetMatch();
    }

    private void handleMatchDestroyed() {

        if (!matchActive) {
            return;
        }

        System.out.println(
                "La partida ha sido destruida, "
                        + "no se guardaran datos"
        );

        listener.onMatchFinished(null);

        resetMatch();
    }

    private void resetMatch() {

        if (analyzer != null) {
            analyzer.finish();
        }

        analyzer = null;

        game = null;
        matchGuid = null;

        playerTeam = -1;
        playerTime = 0;

        playerFound = false;
        matchActive = false;
        isCompetitive = false;
    }

    @Override
    public void onError(
            WebSocket webSocket,
            Throwable error
    ) {

        System.err.println(
                "Conexión con Rocket League perdida:"
        );

        error.printStackTrace();

        notifyConnectionClosed();
    }

    @Override
    public CompletionStage<?> onClose(
            WebSocket webSocket,
            int statusCode,
            String reason
    ) {

        System.out.println(
                "Conexión con Rocket League cerrada: "
                        + reason
        );

        notifyConnectionClosed();

        return null;
    }

    private void notifyConnectionClosed() {

        if (connectionClosed) {
            return;
        }

        connectionClosed = true;

        if (connectionClosedCallback != null) {
            connectionClosedCallback.run();
        }
    }

    private void watchConnection(WebSocket webSocket) {

        while (!connectionClosed) {

            try {

                Thread.sleep(2000);

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
                return;
            }

            if (!isRocketLeagueRunning()) {

                System.out.println(
                        "Rocket League se ha cerrado. "
                                + "Cerrando conexión."
                );

                try {

                    webSocket.sendClose(
                            WebSocket.NORMAL_CLOSURE,
                            "Rocket League cerrado"
                    );

                } catch (Exception ignored) {
                }

                return;
            }
        }
    }

    private boolean isRocketLeagueRunning() {

        String os =
                System.getProperty("os.name")
                        .toLowerCase();

        try {

            Process process;

            if (os.contains("win")) {

                process =
                        new ProcessBuilder(
                                "tasklist",
                                "/FI",
                                "IMAGENAME eq RocketLeague.exe"
                        )
                                .redirectErrorStream(true)
                                .start();

            } else {

                process =
                        new ProcessBuilder(
                                "pgrep",
                                "-f",
                                "RocketLeague"
                        )
                                .redirectErrorStream(true)
                                .start();
            }

            String output =
                    new String(
                            process.getInputStream()
                                    .readAllBytes()
                    );

            int exitCode =
                    process.waitFor();

            if (os.contains("win")) {

                return exitCode == 0
                        && output.contains(
                        "RocketLeague.exe"
                );
            }

            return exitCode == 0;

        } catch (Exception e) {

            return false;
        }
    }
}