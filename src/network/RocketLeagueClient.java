package network;

import events.MatchEventListener;
import matches.MatchStorage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;

public class RocketLeagueClient {

    private final String rocketLeagueUrl;
    private final String playerName;
    private final MatchStorage storage;
    private final MatchEventListener eventListener;

    private final HttpClient httpClient;

    private volatile WebSocket webSocket;

    private volatile ConnectionState connectionState =
            ConnectionState.DISCONNECTED;

    private volatile boolean running;

    private Thread connectionThread;

    private volatile ConnectionStateListener connectionStateListener;

    public RocketLeagueClient(
            String rocketLeagueUrl,
            String playerName,
            MatchStorage storage,
            MatchEventListener eventListener
    ) {

        this.rocketLeagueUrl = rocketLeagueUrl;
        this.playerName = playerName;
        this.storage = storage;
        this.eventListener = eventListener;

        this.httpClient =
                HttpClient.newHttpClient();
    }

    // ============================
    // Listener
    // ============================

    public void setConnectionStateListener(
            ConnectionStateListener listener
    ) {

        this.connectionStateListener = listener;

        if (listener != null) {

            listener.onConnectionStateChanged(
                    connectionState
            );
        }
    }

    // ============================
    // Control
    // ============================

    public synchronized void start() {

        if (running) {
            return;
        }

        running = true;

        setConnectionState(
                ConnectionState.CONNECTING
        );

        connectionThread =
                new Thread(
                        this::connectionLoop,
                        "RocketLeagueConnection"
                );

        connectionThread.setDaemon(true);
        connectionThread.start();
    }

    public synchronized void stop() {

        running = false;

        if (connectionThread != null) {

            connectionThread.interrupt();
            connectionThread = null;
        }

        WebSocket socket = webSocket;

        webSocket = null;

        if (socket != null) {

            try {

                socket.sendClose(
                        WebSocket.NORMAL_CLOSURE,
                        "Cliente detenido"
                );

            } catch (Exception ignored) {
            }
        }

        setConnectionState(
                ConnectionState.DISCONNECTED
        );
    }

    // ============================
    // Conexión
    // ============================

    private void connectionLoop() {

        while (
                running &&
                        !Thread.currentThread().isInterrupted()
        ) {

            try {

                System.out.println(
                        "Intentando conectar con Rocket League..."
                );

                setConnectionState(
                        ConnectionState.CONNECTING
                );

                webSocket =
                        httpClient.newWebSocketBuilder()
                                .buildAsync(
                                        URI.create(
                                                rocketLeagueUrl
                                        ),
                                        new RocketLeagueListener(
                                                playerName,
                                                storage,
                                                eventListener,
                                                this::handleConnectionClosed
                                        )
                                )
                                .join();

                /*
                 * Si buildAsync().join() ha terminado
                 * correctamente, el WebSocket está abierto.
                 */
                setConnectionState(
                        ConnectionState.CONNECTED
                );

                /*
                 * Esperamos mientras el socket siga
                 * existiendo y el cliente siga activo.
                 */
                while (
                        running &&
                                webSocket != null
                ) {

                    Thread.sleep(500);
                }

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
                return;

            } catch (Exception e) {

                webSocket = null;

                if (!running) {
                    return;
                }

                System.out.println(
                        "Rocket League no está disponible. Reintentando en 2 segundos..."
                );

                setConnectionState(
                        ConnectionState.CONNECTING
                );

                try {

                    Thread.sleep(2000);

                } catch (InterruptedException interruptedException) {

                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    /*
     * Callback llamado por RocketLeagueListener cuando
     * el WebSocket se cierra.
     */
    private void handleConnectionClosed() {

        running = false;

        webSocket = null;

        setConnectionState(
                ConnectionState.DISCONNECTED
        );
    }

    // ============================
    // Estado
    // ============================

    public ConnectionState getConnectionState() {
        return connectionState;
    }

    public boolean isRunning() {
        return running;
    }

    private void setConnectionState(
            ConnectionState state
    ) {

        if (connectionState == state) {
            return;
        }

        connectionState = state;

        ConnectionStateListener listener =
                connectionStateListener;

        if (listener != null) {

            listener.onConnectionStateChanged(
                    state
            );
        }
    }
}
