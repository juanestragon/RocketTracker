package matches;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MatchStorage {

    private final Path matchesDirectory;

    public MatchStorage(Path matchesDirectory) {
        this.matchesDirectory = matchesDirectory;
    }

    public void save(MatchResult result) {

        try {

            Files.createDirectories(matchesDirectory);

            String fileName = LocalDateTime.parse(
                    result.getDate()).format(DateTimeFormatter.ofPattern("yy-MM-dd_H-mm-ss")) + ".json";

            Path file = matchesDirectory.resolve(fileName);
            String json = buildJson(result);

            Files.writeString(
                    file,
                    json,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

            System.out.println("Partida guardada: " + file);

        } catch (IOException e) {
            System.err.println("No se pudo guardar la partida:");
            e.printStackTrace();
        }
    }

    private String buildJson(MatchResult result) {

        StringBuilder json = new StringBuilder();

        json.append("{\n");

        appendString(json, "date", result.getDate(), true);
        appendString(json, "player", result.getPlayerName(), true);
        appendString(json, "matchGuid", result.getMatchGuid(), true);

        appendNumber(json, "playlistId", result.getPlaylistId(), true);

        appendBoolean(json, "won", result.isWon(), true);

        json.append("    \"metrics\": {\n");

        appendNumber(json, "assists", result.getAssists(), true);
        appendNumber(json, "saves", result.getSaves(), true);
        appendNumber(json, "shots", result.getShots(), true);
        appendNumber(json, "goals", result.getGoals(), true);
        appendNumber(json, "demos", result.getDemos(), true);

        appendNumber(json, "airPercentage", result.getAirPercentage(), true);
        appendNumber(json, "supersonicPercentage", result.getSupersonicPercentage(), true);
        appendNumber(json, "boostUsedSupersonic", result.getBoostUsedSupersonic(), true);
        appendNumber(json, "supersonicSessionPercentage", result.getSupersonicSessionPercentage(), true);
        appendNumber(json, "averageBoostToSupersonic", result.getAverageBoostToSupersonic(), true);
        appendNumber(json, "averageSpeed", result.getAverageSpeed(), false);

        json.append("    }\n");
        json.append("}\n");

        return json.toString();
    }

    private void appendString(StringBuilder json, String key, String value, boolean comma) {

        json.append("    \"").append(key).append("\": \"").append(escape(value)).append("\"");

        if (comma) {
            json.append(",");
        }

        json.append("\n");
    }

    private void appendBoolean(StringBuilder json, String key, boolean value, boolean comma) {

        json.append("    \"").append(key).append("\": ").append(value);

        if (comma) {
            json.append(",");
        }

        json.append("\n");
    }

    private void appendNumber(StringBuilder json, String key, double value, boolean comma) {

        json.append("        \"").append(key).append("\": ").append(value);

        if (comma) {
            json.append(",");
        }

        json.append("\n");
    }

    private void appendNumber(StringBuilder json, String key, int value, boolean comma) {

        json.append("        \"").append(key).append("\": ").append(value);

        if (comma) {
            json.append(",");
        }

        json.append("\n");
    }

    private String escape(String value) {

        if (value == null) {
            return "";
        }

        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}