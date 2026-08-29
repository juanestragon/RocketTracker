package gui;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import lang.GuiTrans;

public class GuiView {

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

    private View currentView;
    private GuiTrans guiTrans;

    public GuiView(GuiTrans guiTrans) {

        this.guiTrans = guiTrans;

        root = new BorderPane();
        currentView = View.HOME;
        sidebar = new VBox();
        content = new VBox();

        applicationName = new Label("Rocket Tracker");

        homeButton = createNavigationButton(guiTrans.getHomeButton());
        statisticsButton = createNavigationButton(guiTrans.getStatisticsButton());
        settingsButton = createNavigationButton(guiTrans.getSettingsButton());
        connectionStatus = new Label(guiTrans.getConnectionStatusDisconnected());
        connectionButton = new Button("⏻");
        connectionButton.getStyleClass().add("connection-button");
        connectionBox = new HBox(8, connectionButton, connectionStatus);
        connectionBox.setAlignment(Pos.CENTER_RIGHT);

        build();
    }

    public void updateLang(GuiTrans guiTrans) {
        this.guiTrans = guiTrans;

        homeButton.setText(guiTrans.getHomeButton());
        statisticsButton.setText(guiTrans.getStatisticsButton());
        settingsButton.setText(guiTrans.getSettingsButton());
        connectionStatus.setText(guiTrans.getSettingsButton());

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

        VBox navigation = new VBox(homeButton, statisticsButton, settingsButton);
        navigation.getStyleClass().add("navigation");

        connectionBox.getStyleClass().add("connection-box");
        connectionStatus.getStyleClass().add("connection-status");

        sidebar.getStyleClass().add("sidebar");
        sidebar.getChildren().addAll(applicationName, navigation);
        sidebar.getChildren().add(connectionBox);

        VBox.setVgrow(navigation, javafx.scene.layout.Priority.ALWAYS);
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
        content.getStyleClass().add("content");
    }

    private Button createNavigationButton(String text) {

        Button button = new Button(text);
        button.getStyleClass().add("navigation-button");
        button.setMaxWidth(Double.MAX_VALUE);

        return button;
    }

    // ============================
    // Connection
    // ============================

    public void setConnectionConnected() {

        connectionStatus.setText(guiTrans.getConnectionStatusConnected());
        connectionStatus.getStyleClass().removeAll( "connecting", "disconnected");
        connectionStatus.getStyleClass().add("connected");

        connectionButton.getStyleClass().removeAll( "connection-connecting", "connection-disconnected");
        connectionButton.getStyleClass().add("connection-connected");
    }

    public void setConnectionConnecting() {

        connectionStatus.setText(guiTrans.getConnectionStatusConnecting());
        connectionStatus.getStyleClass().removeAll("connected", "disconnected");
        connectionStatus.getStyleClass().add("connecting");

        connectionButton.getStyleClass().removeAll("connection-connected", "connection-disconnected");
        connectionButton.getStyleClass().add("connection-connecting");
    }

    public void setConnectionDisconnected() {

        connectionStatus.setText(guiTrans.getConnectionStatusDisconnected());
        connectionStatus.getStyleClass().removeAll("connected", "connecting");
        connectionStatus.getStyleClass().add("disconnected");

        connectionButton.getStyleClass().removeAll("connection-connected", "connection-connecting");
        connectionButton.getStyleClass().add("connection-disconnected");
    }

    // ============================
    // Getters
    // ============================

    public BorderPane getRoot() {
        return root;
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

    public void setContent(javafx.scene.Node node) {

        content.getChildren().clear();
        if (node != null) {
            content.getChildren().add(node);
        }
    }
}