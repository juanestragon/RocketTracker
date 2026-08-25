package network;

public class StatsMessage {

    private final String event;
    private final String data;

    public StatsMessage(String event, String data) {
        this.event = event;
        this.data = data;
    }

    public String getEvent() {
        return event;
    }

    public String getData() {
        return data;
    }
}