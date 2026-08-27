package gui;

import config.Config;
import config.ConfigStorage;
import events.GuiEventListener;
import network.ConnectionState;
import network.ConnectionStateListener;
import network.RocketLeagueClient;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.util.Duration;

import java.nio.file.Path;

public class GuiController implements GuiEventListener, ConnectionStateListener {

    private final GuiAPI guiAPI;
    private final GuiView view;
    private final RocketLeagueClient client;

    private final HomeView homeView;
    private final StatisticsView statisticsView;
    private final ConfigStorage configStorage;

    public GuiController(GuiAPI guiAPI, GuiView view, RocketLeagueClient client, ConfigStorage configStorage) {
        this.configStorage = configStorage;
        this.guiAPI = guiAPI;
        this.view = view;
        this.client = client;

        this.homeView = new HomeView();
        this.statisticsView = new StatisticsView();

        configureNavigation();
        configureStatisticsFilters();
        configureConnectionButton();
        configureSettings();

        updateConnectionState(client.getConnectionState());
        showHome();
    }

    // ============================
    // Navegación
    // ============================

    private void configureNavigation() {

        view.getHomeButton().setOnAction(event -> showHome());
        view.getStatisticsButton().setOnAction(event -> showStatistics());
        view.getSettingsButton().setOnAction(event -> showSettings());
    }

    private void showHome() {

        view.setContent(homeView.getRoot());
        view.selectNavigationButton(view.getHomeButton());

        updateHome();
    }

    private void showStatistics() {

        view.setContent(statisticsView.getRoot());
        view.selectNavigationButton(view.getStatisticsButton());

        updateStatistics();
    }

    // ============================
    // Conexión
    // ============================

    private void configureConnectionButton() {

        view.getConnectionButton().setOnAction(event -> {

            if (client.isRunning()) {
                client.stop();
            } else {
                client.start();
            }
        });
    }

    @Override
    public void onConnectionStateChanged(ConnectionState state) {
        Platform.runLater(() -> updateConnectionState(state));
    }

    private void updateConnectionState(ConnectionState state) {

        switch (state) {
            case CONNECTED -> view.setConnectionConnected();
            case CONNECTING -> view.setConnectionConnecting();
            case DISCONNECTED -> view.setConnectionDisconnected();

        }
    }

// ============================
// Filtros de estadísticas
// ============================

    private void configureStatisticsFilters() {

        statisticsView.getAllButton().setOnAction(event -> {
            statisticsView.selectFilter(statisticsView.getAllButton());
            updateStatistics();
        });

        statisticsView.getOneVsOneButton().setOnAction(event -> {
            statisticsView.selectFilter(statisticsView.getOneVsOneButton());
            updateStatistics();
        });

        statisticsView.getTwoVsTwoButton().setOnAction(event -> {
            statisticsView.selectFilter(statisticsView.getTwoVsTwoButton());
            updateStatistics();
        });

        statisticsView.getThreeVsThreeButton().setOnAction(event -> {
            statisticsView.selectFilter(statisticsView.getThreeVsThreeButton());
            updateStatistics();
        });

        statisticsView.getMatchLimitField().setOnAction(event -> updateStatistics());
    }

// ============================
// Estadísticas
// ============================

    private void updateStatistics() {

        int playlistId = getSelectedPlaylistId();
        Integer limit = getMatchLimit();

        Platform.runLater(() -> {

            if (limit == null) {

                if (playlistId == -1) {
                    updateStatisticsAll();
                } else {
                    updateStatisticsPlaylist(playlistId);
                }

            } else {

                if (playlistId == -1) {
                    updateStatisticsLast(limit);
                } else {
                    updateStatisticsLast(limit, playlistId);
                }
            }
        });
    }

    private void updateStatisticsAll() {

        statisticsView.setWinPercentage(guiAPI.getWinPercentage());
        statisticsView.setAverageGoals(guiAPI.getAverageGoals());
        statisticsView.setAverageShots(guiAPI.getAverageShots());
        statisticsView.setAverageSaves(guiAPI.getAverageSaves());
        statisticsView.setAverageAssists(guiAPI.getAverageAssists());
        statisticsView.setAverageDemos(guiAPI.getAverageDemos());
        statisticsView.setAverageAirPercentage(guiAPI.getAverageAirPercentage());
        statisticsView.setAverageSupersonicPercentage(guiAPI.getAverageSupersonicPercentage());
        statisticsView.setAverageSpeed(guiAPI.getAverageSpeed());
        statisticsView.setAverageBoostUsedSupersonic(guiAPI.getAverageBoostUsedSupersonic());
        statisticsView.setAverageSupersonicSessionPercentage(guiAPI.getAverageSupersonicSessionPercentage());
        statisticsView.setAverageBoostToSupersonic(guiAPI.getAverageBoostToSupersonic());
    }

    private int getSelectedPlaylistId() {

        if (statisticsView.getOneVsOneButton().getStyleClass().contains("selected")) {
            return 10;
        }

        if (statisticsView.getTwoVsTwoButton().getStyleClass().contains("selected")) {
            return 11;
        }

        if (statisticsView.getThreeVsThreeButton().getStyleClass().contains("selected")) {
            return 13;
        }

        return -1;
    }

    private Integer getMatchLimit() {

        String text = statisticsView.getMatchLimitField().getText().trim();

        if (text.isEmpty()) {
            return null;
        }

        try {

            int value = Integer.parseInt(text);

            return value <= 0 ? 0 : value;

        } catch (NumberFormatException e) {

            return null;
        }
    }

    private void updateStatisticsPlaylist(int playlistId) {

        statisticsView.setWinPercentage(guiAPI.getWinPercentage(playlistId));
        statisticsView.setAverageGoals(guiAPI.getAverageGoals(playlistId));
        statisticsView.setAverageShots(guiAPI.getAverageShots(playlistId));
        statisticsView.setAverageSaves(guiAPI.getAverageSaves(playlistId));
        statisticsView.setAverageAssists(guiAPI.getAverageAssists(playlistId));
        statisticsView.setAverageDemos(guiAPI.getAverageDemos(playlistId));
        statisticsView.setAverageAirPercentage(guiAPI.getAverageAirPercentage(playlistId));
        statisticsView.setAverageSupersonicPercentage(guiAPI.getAverageSupersonicPercentage(playlistId));
        statisticsView.setAverageSpeed(guiAPI.getAverageSpeed(playlistId));
        statisticsView.setAverageBoostUsedSupersonic(guiAPI.getAverageBoostUsedSupersonic(playlistId));
        statisticsView.setAverageSupersonicSessionPercentage(guiAPI.getAverageSupersonicSessionPercentage(playlistId));
        statisticsView.setAverageBoostToSupersonic(guiAPI.getAverageBoostToSupersonic(playlistId));
    }

    private void updateStatisticsLast(int n, int playlistId) {

        statisticsView.setWinPercentage(guiAPI.getLastWinPercentage(n, playlistId));
        statisticsView.setAverageGoals(guiAPI.getLastAverageGoals(n, playlistId));
        statisticsView.setAverageShots(guiAPI.getLastAverageShots(n, playlistId));
        statisticsView.setAverageSaves(guiAPI.getLastAverageSaves(n, playlistId));
        statisticsView.setAverageAssists(guiAPI.getLastAverageAssists(n, playlistId));
        statisticsView.setAverageDemos(guiAPI.getLastAverageDemos(n, playlistId));
        statisticsView.setAverageAirPercentage(guiAPI.getLastAverageAirPercentage(n, playlistId));
        statisticsView.setAverageSupersonicPercentage(guiAPI.getLastAverageSupersonicPercentage(n, playlistId));
        statisticsView.setAverageSpeed(guiAPI.getLastAverageSpeed(n, playlistId));
        statisticsView.setAverageBoostUsedSupersonic(guiAPI.getLastAverageBoostUsedSupersonic(n, playlistId));
        statisticsView.setAverageSupersonicSessionPercentage(guiAPI.getLastAverageSupersonicSessionPercentage(n, playlistId));
        statisticsView.setAverageBoostToSupersonic(guiAPI.getLastAverageBoostToSupersonic(n, playlistId));
    }

    private void updateStatisticsLast(int n) {

        statisticsView.setWinPercentage(guiAPI.getLastWinPercentage(n));
        statisticsView.setAverageGoals(guiAPI.getLastAverageGoals(n));
        statisticsView.setAverageShots(guiAPI.getLastAverageShots(n));
        statisticsView.setAverageSaves(guiAPI.getLastAverageSaves(n));
        statisticsView.setAverageAssists(guiAPI.getLastAverageAssists(n));
        statisticsView.setAverageDemos(guiAPI.getLastAverageDemos(n));
        statisticsView.setAverageAirPercentage(guiAPI.getLastAverageAirPercentage(n));
        statisticsView.setAverageSupersonicPercentage(guiAPI.getLastAverageSupersonicPercentage(n));
        statisticsView.setAverageSpeed(guiAPI.getLastAverageSpeed(n));
        statisticsView.setAverageBoostUsedSupersonic(guiAPI.getLastAverageBoostUsedSupersonic(n));
        statisticsView.setAverageSupersonicSessionPercentage(guiAPI.getLastAverageSupersonicSessionPercentage(n));
        statisticsView.setAverageBoostToSupersonic(guiAPI.getLastAverageBoostToSupersonic(n));
    }

    // ============================
    // Home
    // ============================

    private void updateHome() {

        Platform.runLater(() -> {

            if (guiAPI.isMatchActive()) {
                homeView.setCurrentMatch(guiAPI.getCurrentArena(), guiAPI.getCurrentPlaylistId());
            } else {
                homeView.clearCurrentMatch();
            }

            updateHomeMatches();
        });
    }

    private void updateHomeMatches() {

        homeView.setLastMatch(guiAPI.getLastMatch());
        homeView.setRecentMatches(guiAPI.getLastMatches(10));
    }

    // ============================
    // Settings
    // ============================

    private void showSettings() {

        view.setContent(view.getSettingsView().getRoot());
        view.selectNavigationButton(view.getSettingsButton());
        loadSettings();
    }

    private void loadSettings() {

        Config config = configStorage.load();
        view.getSettingsView().setPlayerName(config.getPlayerName());
        view.getSettingsView().setStoragePath(config.getStoragePath().toString());
        view.getSettingsView().setPacketSendRateField(config.getPacketSendRate().toString());
    }

    private void saveSettings() {

        String playerName = view.getSettingsView().getPlayerName();

        String storagePath = view.getSettingsView().getStoragePath();
        int packetSendRate;
        try {
            packetSendRate = Integer.parseInt(view.getSettingsView().getPacketSendRate());

            if (playerName.isBlank()) {
                view.getSettingsView().setStatus("El nombre del jugador no puede estar vacío.");
                return;
            }

            if (storagePath.isBlank()) {
                view.getSettingsView().setStatus("La ruta de almacenamiento no puede estar vacía.");

                return;
            }

            if (packetSendRate <= 0) {
                view.getSettingsView().setStatus("El ratio de envio de paquetes debe ser mayor que 0");

                return;
            }

            Config currentConfig = configStorage.load();
            Config config = new Config(playerName, currentConfig.getRocketLeagueUrl(), Path.of(storagePath), packetSendRate);
            configStorage.save(config);

            view.getSettingsView().setStatus("Configuración guardada.");

            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            if(client.isRunning()) {
                client.stop();
                pause.setOnFinished(event -> {
                    view.getSettingsView().setStatus("");
                    client.start();
                });
            } else{
                pause.setOnFinished(event -> view.getSettingsView().setStatus(""));
            }
            pause.play();


        } catch (NumberFormatException e) {
            view.getSettingsView().setStatus("El ratio de paquetes debe ser un entero");
        }
    }

    private void configureSettings() {

        view.getSettingsView().getBrowseButton().setOnAction(event -> browseStoragePath());
        view.getSettingsView().getSaveButton().setOnAction(event -> saveSettings());
    }

    private void browseStoragePath() {

        javafx.stage.Stage stage = (javafx.stage.Stage) view.getRoot().getScene().getWindow();
        boolean wasMaximized = stage.isMaximized();
        javafx.stage.DirectoryChooser chooser = new javafx.stage.DirectoryChooser();

        chooser.setTitle("Select storage folder");
        java.io.File selected = chooser.showDialog(stage);

        if (selected != null) {
            view.getSettingsView().setStoragePath(selected.toPath().toString());
        }

        if (wasMaximized) {
            Platform.runLater(() -> {
                stage.setMaximized(false);
                stage.setMaximized(true);
            });
        }
    }

    // ============================
    // Eventos de partida
    // ============================

    @Override
    public void onMatchStarted() {

        Platform.runLater(() -> {
            homeView.setCurrentMatch(guiAPI.getCurrentArena(), guiAPI.getCurrentPlaylistId());
        });
    }

    @Override
    public void onMatchFinished() {

        Platform.runLater(() -> {

            homeView.clearCurrentMatch();
            updateHomeMatches();
            updateStatistics();
        });
    }
}