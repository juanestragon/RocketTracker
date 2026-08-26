package web;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SettingsView {

    private final VBox root;

    private final TextField playerNameField;
    private final TextField storagePathField;

    private final Button browseButton;
    private final Button saveButton;

    private final Label statusLabel;

    public SettingsView() {

        root = new VBox(16);

        playerNameField = new TextField();
        storagePathField = new TextField();

        browseButton = new Button("Browse...");
        saveButton = new Button("Save");

        statusLabel = new Label();

        build();
    }

    private void build() {

        root.getStyleClass().add("settings-view");
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_LEFT);

        Label title = new Label("Settings");
        title.getStyleClass().add("settings-title");

        // ============================
        // Player name
        // ============================

        Label playerNameLabel = new Label("Player name");
        playerNameLabel.getStyleClass().add("settings-label");

        playerNameField.setPromptText("Rocket League player name");
        playerNameField.setMaxWidth(Double.MAX_VALUE);

        // ============================
        // Storage path
        // ============================

        Label storagePathLabel = new Label("Storage path");
        storagePathLabel.getStyleClass().add("settings-label");
        storagePathField.setMaxWidth(Double.MAX_VALUE);

        browseButton.getStyleClass().add("settings-browse-button");

        HBox storageBox = new HBox(8, storagePathField, browseButton);
        storageBox.setAlignment(Pos.CENTER_LEFT);

        HBox.setHgrow(storagePathField, javafx.scene.layout.Priority.ALWAYS);

        // ============================
        // Save
        // ============================

        saveButton.getStyleClass().add("settings-save-button");
        statusLabel.getStyleClass().add("settings-status");

        root.getChildren().addAll(
                title,

                playerNameLabel,
                playerNameField,

                storagePathLabel,
                storageBox,

                saveButton,
                statusLabel);
    }

    // ============================
    // Root
    // ============================

    public VBox getRoot() {
        return root;
    }

    // ============================
    // Player name
    // ============================

    public String getPlayerName() {
        return playerNameField.getText().trim();
    }

    public void setPlayerName(String playerName) {
        playerNameField.setText(playerName != null ? playerName : "");
    }

    // ============================
    // Storage path
    // ============================

    public String getStoragePath() {
        return storagePathField.getText().trim();
    }

    public void setStoragePath(String storagePath) {
        storagePathField.setText(storagePath != null ? storagePath : "");
    }

    // ============================
    // Status
    // ============================

    public void setStatus(String status) {
        statusLabel.setText(status != null ? status : "");
    }

    // ============================
    // Controls
    // ============================

    public Button getBrowseButton() {
        return browseButton;
    }

    public Button getSaveButton() {
        return saveButton;
    }
}