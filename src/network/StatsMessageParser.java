package network;

import util.JsonParser;

import java.util.Map;

public class StatsMessageParser {

    private StatsMessageParser() {
    }

    public static StatsMessage parse(String json) {

        Object parsed = JsonParser.parse(json);

        if (!(parsed instanceof Map<?, ?> object)) {
            throw new IllegalArgumentException(
                    "El mensaje de Stats API debe ser un objeto JSON"
            );
        }

        Object event = object.get("Event");
        Object data = object.get("Data");

        if (!(event instanceof String)) {
            throw new IllegalArgumentException(
                    "El campo Event no es válido"
            );
        }

        if (!(data instanceof String)) {
            throw new IllegalArgumentException(
                    "El campo Data no es válido"
            );
        }

        return new StatsMessage(
                (String) event,
                (String) data
        );
    }
}