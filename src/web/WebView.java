package web;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class WebView {

    private final BorderPane root;

    private final VBox sidebar;
    private final VBox content;

    private final Label applicationName;

    private final Button homeButton;
    private final Button statisticsButton;
    private final Button settingsButton;

    private final HBox connectionBox;
    private final Label connectionStatus;
    private final Button connectionButton;

    private final SettingsView settingsView;

    private View currentView;


    public WebView() {

        settingsView = new SettingsView();

        root = new BorderPane();
        currentView = View.HOME;
        sidebar = new VBox();
        content = new VBox();

        applicationName =
                new Label("Rocket Tracker");

        homeButton =
                createNavigationButton("Home");

        statisticsButton =
                createNavigationButton("Statistics");

        settingsButton =
                createNavigationButton("Settings");

        connectionStatus =
                new Label("● Desconectado");

        connectionButton =
                new Button("⏻");

        connectionButton.getStyleClass().add(
                "connection-button"
        );

        connectionBox =
                new HBox(
                        8,
                        connectionButton,
                        connectionStatus
                );

        connectionBox.setAlignment(
                Pos.CENTER_RIGHT
        );

        build();
    }

    private void build() {

        buildSidebar();
        buildContent();

        root.setLeft(sidebar);
        root.setCenter(content);

        root.getStyleClass().add("root");
    }

    public View getView() {
        return currentView;
    }

    private void buildSidebar() {

        sidebar.getStyleClass().add("sidebar");

        VBox navigation =
                new VBox(
                        homeButton,
                        statisticsButton,
                        settingsButton
                );

        navigation.getStyleClass().add(
                "navigation"
        );

        connectionBox.getStyleClass().add(
                "connection-box"
        );

        connectionStatus.getStyleClass().add(
                "connection-status"
        );

        sidebar.getChildren().addAll(
                applicationName,
                navigation
        );

        sidebar.getChildren().add(
                connectionBox
        );

        VBox.setVgrow(
                navigation,
                javafx.scene.layout.Priority.ALWAYS
        );
    }

    public void selectNavigationButton(Button selectedButton) {

        getHomeButton().getStyleClass().remove("selected");
        getStatisticsButton().getStyleClass().remove("selected");
        getSettingsButton().getStyleClass().remove("selected");
        selectedButton.getStyleClass().add("selected");

        if (selectedButton == homeButton) {
            currentView = View.HOME;
        } else if (selectedButton == statisticsButton) {
            currentView = View.STATISTICS;
        } else if (selectedButton == settingsButton) {
            currentView = View.SETTINGS;
        }
    }

    private void buildContent() {

        content.getStyleClass().add(
                "content"
        );
    }

    private Button createNavigationButton(
            String text
    ) {

        Button button =
                new Button(text);

        button.getStyleClass().add(
                "navigation-button"
        );

        button.setMaxWidth(
                Double.MAX_VALUE
        );

        return button;
    }

    // ============================
    // Connection
    // ============================

    public void setConnectionConnected() {

        connectionStatus.setText(
                "● Conectado"
        );

        connectionStatus.getStyleClass().removeAll(
                "connected",
                "connecting",
                "disconnected"
        );

        connectionStatus.getStyleClass().add(
                "connected"
        );

        connectionButton.getStyleClass().removeAll(
                "connection-connected",
                "connection-connecting",
                "connection-disconnected",
                "connection-on",
                "connection-off"
        );

        connectionButton.getStyleClass().add(
                "connection-on"
        );
    }

    public void setConnectionConnecting() {

        connectionStatus.setText(
                "● Conectando"
        );

        connectionStatus.getStyleClass().removeAll(
                "connected",
                "connecting",
                "disconnected"
        );

        connectionStatus.getStyleClass().add(
                "connecting"
        );

        connectionButton.getStyleClass().removeAll(
                "connection-connected",
                "connection-connecting",
                "connection-disconnected",
                "connection-on",
                "connection-off"
        );

        connectionButton.getStyleClass().add(
                "connection-connecting"
        );
    }

    public void setConnectionDisconnected() {

        connectionStatus.setText(
                "● Desconectado"
        );

        connectionStatus.getStyleClass().removeAll(
                "connected",
                "connecting",
                "disconnected"
        );

        connectionStatus.getStyleClass().add(
                "disconnected"
        );

        connectionButton.getStyleClass().removeAll(
                "connection-connected",
                "connection-connecting",
                "connection-disconnected",
                "connection-on",
                "connection-off"
        );

        connectionButton.getStyleClass().add(
                "connection-off"
        );
    }

    // ============================
    // Getters
    // ============================

    public BorderPane getRoot() {
        return root;
    }

    public VBox getContent() {
        return content;
    }

    public Button getHomeButton() {
        return homeButton;
    }

    public Button getStatisticsButton() {
        return statisticsButton;
    }

    public Button getSettingsButton() {
        return settingsButton;
    }

    public Button getConnectionButton() {
        return connectionButton;
    }

    public Label getConnectionStatus() {
        return connectionStatus;
    }

    public SettingsView getSettingsView() {
        return settingsView;
    }

    public void setContent(
            javafx.scene.Node node
    ) {

        content.getChildren().clear();

        if (node != null) {
            content.getChildren().add(node);
        }
    }

    public void setConnectionStatus(
            boolean connected
    ) {

        if (connected) {
            setConnectionConnected();
        } else {
            setConnectionDisconnected();
        }
    }
}