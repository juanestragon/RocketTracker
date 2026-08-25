package config;

import util.JsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;

public class ConfigStorage {

    private final Path configFile;

    public ConfigStorage(Path configFile) {
        this.configFile = configFile;
    }

    public Config load() {

        if (!Files.exists(configFile)) {

            Config config = createDefaultConfig();

            save(config);

            return config;
        }

        try {

            String json = Files.readString(configFile);

            Object parsed = JsonParser.parse(json);

            if (!(parsed instanceof Map<?, ?> map)) {
                throw new IllegalArgumentException("La configuración no es un objeto JSON");
            }

            String playerName = getString(map, "playerName");

            String rocketLeagueUrl = getString(map, "rocketLeagueUrl");

            String storagePath = getString(map, "storagePath");

            return new Config(
                    playerName,
                    rocketLeagueUrl,
                    Path.of(storagePath)
            );

        } catch (IOException e) {

            throw new RuntimeException("No se pudo leer la configuración", e);
        }
    }

    public void save(Config config) {

        try {

            Path parent = configFile.getParent();

            if (parent != null) {
                Files.createDirectories(parent);
            }

            String json = buildJson(config);

            Files.writeString(
                    configFile,
                    json,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "No se pudo guardar la configuración",
                    e
            );
        }
    }

    private Config createDefaultConfig() {

        return new Config(
                "",
                "ws://127.0.0.1:49124",
                Path.of("data", "matches")
        );
    }

    private String buildJson(Config config) {

        StringBuilder json =
                new StringBuilder();

        json.append("{\n");

        appendString(
                json,
                "playerName",
                config.getPlayerName(),
                true
        );

        appendString(
                json,
                "rocketLeagueUrl",
                config.getRocketLeagueUrl(),
                true
        );

        appendString(
                json,
                "storagePath",
                config.getStoragePath().toString(),
                false
        );

        json.append("}\n");

        return json.toString();
    }

    private void appendString(
            StringBuilder json,
            String key,
            String value,
            boolean comma
    ) {

        json.append("    \"")
                .append(key)
                .append("\": \"")
                .append(escape(value))
                .append("\"");

        if (comma) {
            json.append(",");
        }

        json.append("\n");
    }

    private String getString(
            Map<?, ?> map,
            String key
    ) {

        Object value = map.get(key);

        if (!(value instanceof String string)) {

            throw new IllegalArgumentException(
                    "La configuración no contiene un valor válido para: "
                            + key
            );
        }

        return string;
    }

    private String getOptionalString(
            Map<?, ?> map,
            String key
    ) {

        Object value = map.get(key);

        if (value == null) {
            return null;
        }

        if (!(value instanceof String string)) {

            throw new IllegalArgumentException(
                    "La configuración no contiene un valor válido para: "
                            + key
            );
        }

        return string.isBlank()
                ? null
                : string;
    }

    private String escape(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}