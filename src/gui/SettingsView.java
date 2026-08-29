package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lang.SettingsTrans;

public class SettingsView {

    private final VBox root;

    private SettingsTrans settingsTrans;

    private final TextField playerNameField;
    private final TextField storagePathField;
    private final TextField packetSendRateField;

    private final Button browseButton;
    private final Button saveButton;

    private final ComboBox languageSelector;

    private final Label statusLabel;

    public SettingsView(SettingsTrans settingsTrans) {

        root = new VBox(16);

        this.settingsTrans = settingsTrans;

        playerNameField = new TextField();
        storagePathField = new TextField();
        packetSendRateField = new TextField();

        browseButton = new Button();
        saveButton = new Button();

        languageSelector = new ComboBox<>();

        statusLabel = new Label();

        build();
    }

    private void build() {

        root.getStyleClass().add("settings-view");
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_LEFT);
        root.getChildren().clear();

        // ============================
        // Title
        // ============================

        Label title = new Label(settingsTrans.getTitle());
        title.getStyleClass().add("settings-title");

        // ============================
        // Player name
        // ============================

        Label playerNameLabel = new Label(settingsTrans.getPlayerLabel());
        playerNameLabel.getStyleClass().add("settings-label");
        playerNameField.setPromptText(settingsTrans.getPlayerTextField());
        playerNameField.setMaxWidth(Double.MAX_VALUE);

        // ============================
        // Storage path
        // ============================

        Label storagePathLabel = new Label(settingsTrans.getStoragePathLabel());
        storagePathLabel.getStyleClass().add("settings-label");
        storagePathField.setMaxWidth(Double.MAX_VALUE);
        browseButton.getStyleClass().add("settings-browse-button");
        browseButton.setText(settingsTrans.getBrowseButton());

        // ============================
        // Packet Send Rate And Language
        // ============================

        Label packetSendRateLabel = new Label(settingsTrans.getPacketSendRateLabel());
        packetSendRateLabel.getStyleClass().add("settings-label");
        packetSendRateField.setMaxWidth(Double.MAX_VALUE);


        Label languageSelectorLabel = new Label(settingsTrans.getLanguageLabel());
        languageSelectorLabel.getStyleClass().add("settings-label");
        languageSelector.getItems().clear();
        languageSelector.getItems().addAll("English", "Español");
        languageSelector.getStyleClass().addAll("settings-language");

        HBox.setHgrow(storagePathField, javafx.scene.layout.Priority.ALWAYS);
        HBox storageBox = new HBox(8, storagePathField, browseButton, languageSelectorLabel, languageSelector);
        storageBox.setAlignment(Pos.CENTER_LEFT);

        // ============================
        // Save
        // ============================

        saveButton.setText(settingsTrans.getSaveButton());
        saveButton.getStyleClass().add("settings-save-button");
        statusLabel.getStyleClass().add("settings-status");

        // ============================
        // Layout
        // ============================

        root.getChildren().addAll(
                title,
                playerNameLabel,
                playerNameField,
                storagePathLabel,
                storageBox,
                packetSendRateLabel,
                packetSendRateField,
                saveButton,
                statusLabel);
    }

    public void updateLang(SettingsTrans settingsTrans) {
        this.settingsTrans = settingsTrans;
        build();
    }

    // ============================
    // Root
    // ============================

    public VBox getRoot() {
        return root;
    }

    // ============================
    // Buttons
    // ============================

    public Button getBrowseButton() {
        return browseButton;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public ComboBox getLanguageSelector() {
        return languageSelector;
    }

    // ============================
    // Values
    // ============================

    public void setPlayerName(String playerName) {
        playerNameField.setText(playerName == null ? "" : playerName );
    }

    public String getPlayerName() {
        return playerNameField.getText().trim();
    }

    public void setStoragePath(String storagePath) {
        storagePathField.setText(storagePath == null ? "" : storagePath);
    }

    public String getStoragePath() {
        return storagePathField.getText().trim();
    }

    public void setStatus(String message) {
        statusLabel.setText(message == null ? "" : message);
    }

    public String getPacketSendRate() {
        return packetSendRateField.getText().trim();
    }

    public void setPacketSendRateField(String packetSendRate) {
        packetSendRateField.setText(packetSendRate == null ? "" : packetSendRate);
    }

    public void setLanguageSelector(String selection) {
        languageSelector.setValue(selection);
    }
}