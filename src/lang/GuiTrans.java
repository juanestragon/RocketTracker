package lang;

public class GuiTrans {

    private final String homeButton;
    private final String statisticsButton;
    private final String settingsButton;
    private final String connectionStatusConnected;
    private final String connectionStatusConnecting;
    private final String connectionStatusDisconnected;

    public GuiTrans(String homeButton, String statisticsButton, String settingsButton, String connectionStatusConnected, String connectionStatusConnecting, String connectionStatusDisconnected) {
        this.homeButton = homeButton;
        this.statisticsButton = statisticsButton;
        this.settingsButton = settingsButton;
        this.connectionStatusConnected = connectionStatusConnected;
        this.connectionStatusConnecting = connectionStatusConnecting;
        this.connectionStatusDisconnected = connectionStatusDisconnected;
    }

    public String getHomeButton() {
        return homeButton;
    }

    public String getStatisticsButton() {
        return statisticsButton;
    }

    public String getSettingsButton() {
        return settingsButton;
    }

    public String getConnectionStatusConnected() {
        return connectionStatusConnected;
    }

    public String getConnectionStatusConnecting() {
        return connectionStatusConnecting;
    }

    public String getConnectionStatusDisconnected() {
        return connectionStatusDisconnected;
    }
}
