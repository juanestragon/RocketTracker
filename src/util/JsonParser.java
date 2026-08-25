package util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsonParser {

    private final String json;
    private int position;

    private JsonParser(String json) {
        this.json = json;
        this.position = 0;
    }

    public static Object parse(String json) {
        JsonParser parser = new JsonParser(json);

        Object result = parser.parseValue();

        parser.skipWhitespace();

        if (parser.position != parser.json.length()) {
            throw new IllegalArgumentException(
                    "JSON inesperado en posición " + parser.position
            );
        }

        return result;
    }

    private Object parseValue() {
        skipWhitespace();

        if (position >= json.length()) {
            throw error("Se esperaba un valor");
        }

        char c = json.charAt(position);

        return switch (c) {
            case '{' -> parseObject();
            case '[' -> parseArray();
            case '"' -> parseString();
            case 't' -> parseTrue();
            case 'f' -> parseFalse();
            case 'n' -> parseNull();
            default -> {
                if (c == '-' || Character.isDigit(c)) {
                    yield parseNumber();
                }

                throw error("Carácter inesperado: '" + c + "'");
            }
        };
    }

    private Map<String, Object> parseObject() {
        Map<String, Object> object = new LinkedHashMap<>();

        position++; // {

        skipWhitespace();

        if (consume('}')) {
            return object;
        }

        while (true) {

            skipWhitespace();

            if (position >= json.length() || json.charAt(position) != '"') {
                throw error("Se esperaba una clave");
            }

            String key = parseString();

            skipWhitespace();

            if (!consume(':')) {
                throw error("Se esperaba ':' después de la clave");
            }

            Object value = parseValue();

            object.put(key, value);

            skipWhitespace();

            if (consume('}')) {
                break;
            }

            if (!consume(',')) {
                throw error("Se esperaba ',' o '}'");
            }
        }

        return object;
    }

    private List<Object> parseArray() {
        List<Object> array = new ArrayList<>();

        position++; // [

        skipWhitespace();

        if (consume(']')) {
            return array;
        }

        while (true) {

            Object value = parseValue();

            array.add(value);

            skipWhitespace();

            if (consume(']')) {
                break;
            }

            if (!consume(',')) {
                throw error("Se esperaba ',' o ']'");
            }
        }

        return array;
    }

    private String parseString() {
        if (!consume('"')) {
            throw error("Se esperaba una cadena");
        }

        StringBuilder result = new StringBuilder();

        while (position < json.length()) {

            char c = json.charAt(position++);

            if (c == '"') {
                return result.toString();
            }

            if (c != '\\') {
                result.append(c);
                continue;
            }

            if (position >= json.length()) {
                throw error("Escape incompleto");
            }

            char escaped = json.charAt(position++);

            switch (escaped) {

                case '"':
                    result.append('"');
                    break;

                case '\\':
                    result.append('\\');
                    break;

                case '/':
                    result.append('/');
                    break;

                case 'b':
                    result.append('\b');
                    break;

                case 'f':
                    result.append('\f');
                    break;

                case 'n':
                    result.append('\n');
                    break;

                case 'r':
                    result.append('\r');
                    break;

                case 't':
                    result.append('\t');
                    break;

                case 'u':
                    result.append(parseUnicodeEscape());
                    break;

                default:
                    throw error(
                            "Escape desconocido: \\" + escaped
                    );
            }
        }

        throw error("Cadena sin cerrar");
    }

    private char parseUnicodeEscape() {
        if (position + 4 > json.length()) {
            throw error("Escape Unicode incompleto");
        }

        String hex = json.substring(position, position + 4);

        try {
            char character = (char) Integer.parseInt(hex, 16);
            position += 4;
            return character;
        } catch (NumberFormatException e) {
            throw error("Escape Unicode inválido: \\u" + hex);
        }
    }

    private Boolean parseTrue() {
        expect("true");
        return true;
    }

    private Boolean parseFalse() {
        expect("false");
        return false;
    }

    private Object parseNull() {
        expect("null");
        return null;
    }

    private Number parseNumber() {
        int start = position;

        if (json.charAt(position) == '-') {
            position++;
        }

        if (position >= json.length()
                || !Character.isDigit(json.charAt(position))) {
            throw error("Número inválido");
        }

        if (json.charAt(position) == '0') {
            position++;
        } else {
            while (position < json.length()
                    && Character.isDigit(json.charAt(position))) {
                position++;
            }
        }

        boolean decimal = false;

        if (position < json.length()
                && json.charAt(position) == '.') {

            decimal = true;
            position++;

            if (position >= json.length()
                    || !Character.isDigit(json.charAt(position))) {
                throw error("Parte decimal inválida");
            }

            while (position < json.length()
                    && Character.isDigit(json.charAt(position))) {
                position++;
            }
        }

        if (position < json.length()
                && (json.charAt(position) == 'e'
                || json.charAt(position) == 'E')) {

            decimal = true;
            position++;

            if (position < json.length()
                    && (json.charAt(position) == '+'
                    || json.charAt(position) == '-')) {
                position++;
            }

            if (position >= json.length()
                    || !Character.isDigit(json.charAt(position))) {
                throw error("Exponente inválido");
            }

            while (position < json.length()
                    && Character.isDigit(json.charAt(position))) {
                position++;
            }
        }

        String number = json.substring(start, position);

        try {
            if (decimal) {
                return Double.parseDouble(number);
            }

            return Long.parseLong(number);

        } catch (NumberFormatException e) {
            throw error("Número inválido: " + number);
        }
    }

    private void skipWhitespace() {
        while (position < json.length()
                && Character.isWhitespace(json.charAt(position))) {
            position++;
        }
    }

    private boolean consume(char expected) {
        if (position < json.length()
                && json.charAt(position) == expected) {

            position++;
            return true;
        }

        return false;
    }

    private void expect(String expected) {
        if (!json.startsWith(expected, position)) {
            throw error("Se esperaba '" + expected + "'");
        }

        position += expected.length();
    }

    private IllegalArgumentException error(String message) {
        return new IllegalArgumentException(
                message + " en posición " + position
        );
    }
}