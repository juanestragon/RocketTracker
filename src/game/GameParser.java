package game;

import util.JsonParser;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameParser {

    private GameParser() {
    }

    public static Game parse(String json) {

        Object rootObject = JsonParser.parse(json);

        if (!(rootObject instanceof Map<?, ?> root)) {
            throw new IllegalArgumentException(
                    "El JSON de GameState debe ser un objeto"
            );
        }

        String matchGuid = getString(root, "MatchGuid");

        List<Player> players = parsePlayers(root);

        Object gameObject = root.get("Game");

        if (!(gameObject instanceof Map<?, ?> game)) {
            throw new IllegalArgumentException(
                    "GameState no contiene un objeto Game válido"
            );
        }

        int playlistId = getInt(game, "PlaylistId");
        int timeSeconds = getInt(game, "TimeSeconds");

        boolean overtime = getBoolean(game, "bOvertime");
        boolean replay = getBoolean(game, "bReplay");
        boolean hasWinner = getBoolean(game, "bHasWinner");

        String winner = getString(game, "Winner");
        String arena = getString(game, "Arena");

        return new Game(
                matchGuid,
                players,
                playlistId,
                timeSeconds,
                overtime,
                replay,
                hasWinner,
                winner,
                arena
        );
    }

    private static List<Player> parsePlayers(
            Map<?, ?> root
    ) {
        Object playersObject = root.get("Players");

        if (!(playersObject instanceof List<?> playersList)) {
            throw new IllegalArgumentException(
                    "Players no es un array válido"
            );
        }

        List<Player> players = new ArrayList<>();

        for (Object playerObject : playersList) {

            if (!(playerObject instanceof Map<?, ?> player)) {
                continue;
            }

            Player state = new Player(
                    getString(player, "Name"),
                    getString(player, "PrimaryId"),
                    getInt(player, "TeamNum"),

                    getInt(player, "Goals"),
                    getInt(player, "Shots"),
                    getInt(player, "Assists"),
                    getInt(player, "Saves"),
                    getInt(player, "Touches"),
                    getInt(player, "CarTouches"),
                    getInt(player, "Demos"),

                    getBoolean(player, "bHasCar"),
                    getDouble(player, "Speed"),
                    getDouble(player, "Boost"),
                    getBoolean(player, "bSupersonic"),

                    getBoolean(player, "bBoosting"),
                    getBoolean(player, "bOnGround"),
                    getBoolean(player, "bOnWall"),
                    getBoolean(player, "bDemolished")

            );

            players.add(state);
        }

        return players;
    }

    private static String getString(
            Map<?, ?> object,
            String key
    ) {
        Object value = object.get(key);

        if (value == null) {
            return null;
        }

        return String.valueOf(value);
    }

    private static int getInt(
            Map<?, ?> object,
            String key
    ) {
        Object value = object.get(key);

        if (value instanceof Number number) {
            return number.intValue();
        }

        return 0;
    }

    private static double getDouble(
            Map<?, ?> object,
            String key
    ) {
        Object value = object.get(key);

        if (value instanceof Number number) {
            return number.doubleValue();
        }

        return 0.0;
    }

    private static boolean getBoolean(
            Map<?, ?> object,
            String key
    ) {
        Object value = object.get(key);

        if (value instanceof Boolean booleanValue) {
            return booleanValue;
        }

        return false;
    }
}