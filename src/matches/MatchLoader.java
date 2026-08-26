package matches;

import util.JsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MatchLoader {

    private final Path matchesDirectory;

    public MatchLoader(Path matchesDirectory) {
        this.matchesDirectory = matchesDirectory;
    }

    public List<MatchResult> loadAll() {

        List<MatchResult> matches = new ArrayList<>();

        if (!Files.exists(matchesDirectory)) {
            return matches;
        }

        try {

            try (var files = Files.list(matchesDirectory)) {

                files.filter(Files::isRegularFile)
                        .filter(path -> path.toString().endsWith(".json"))
                        .forEach(path -> matches.add(load(path)));
            }

        } catch (IOException e) {

            throw new RuntimeException("No se pudieron leer las partidas", e);
        }

        return matches;
    }

    private MatchResult load(Path file) {

        try {

            String json = Files.readString(file);

            Object parsed = JsonParser.parse(json);

            if (!(parsed instanceof Map<?, ?>)) {
                throw new IllegalArgumentException("El JSON de la partida no es un objeto");
            }

            Map<?, ?> root = (Map<?, ?>) parsed;

            Map<?, ?> metrics = getObject(root, "metrics");

            return new MatchResult(
                    getString(root, "date"),
                    getString(root, "player"),
                    getString(root, "matchGuid"),
                    getInt(root, "playlistId"),
                    getBoolean(root, "won"),

                    getInt(metrics, "assists"),
                    getInt(metrics, "saves"),
                    getInt(metrics, "shots"),
                    getInt(metrics, "goals"),
                    getInt(metrics, "demos"),

                    getDouble(metrics, "airPercentage"),
                    getDouble(metrics, "supersonicPercentage"),
                    getDouble(metrics, "boostUsedSupersonic"),
                    getDouble(metrics, "supersonicSessionPercentage"),
                    getDouble(metrics, "averageBoostToSupersonic"),
                    getDouble(metrics, "averageSpeed"));

        } catch (IOException e) {
            throw new RuntimeException("No se pudo leer la partida: " + file, e);
        }
    }

    private String getString(Map<?, ?> object, String key) {

        Object value = object.get(key);

        if (!(value instanceof String)) {
            throw new IllegalArgumentException("El campo '" + key + "' no es un String");
        }

        return (String) value;
    }

    private boolean getBoolean(Map<?, ?> object, String key) {

        Object value = object.get(key);

        if (!(value instanceof Boolean)) {
            throw new IllegalArgumentException("El campo '" + key + "' no es un boolean");
        }

        return (Boolean) value;
    }

    private int getInt(Map<?, ?> object, String key) {

        Object value = object.get(key);

        if (!(value instanceof Number)) {
            throw new IllegalArgumentException("El campo '" + key + "' no es un número");
        }

        return ((Number) value).intValue();
    }

    private double getDouble(Map<?, ?> object, String key) {

        Object value = object.get(key);

        if (!(value instanceof Number)) {
            throw new IllegalArgumentException("El campo '" + key + "' no es un número");
        }

        return ((Number) value).doubleValue();
    }

    private Map<?, ?> getObject(Map<?, ?> object, String key) {

        Object value = object.get(key);

        if (!(value instanceof Map<?, ?>)) {
            throw new IllegalArgumentException("El campo '" + key + "' no es un objeto");
        }

        return (Map<?, ?>) value;
    }
}