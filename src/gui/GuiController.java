package gui;

import config.Config;
import config.ConfigStorage;
import events.GuiEventListener;
import lang.Translation;
import lang.TranslationParser;
import network.ConnectionState;
import network.ConnectionStateListener;
import network.RocketLeagueClient;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.util.Duration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GuiController implements GuiEventListener, ConnectionStateListener {

    private final GuiAPI guiAPI;
    private final GuiView view;
    private final RocketLeagueClient client;

    private final HomeView homeView;
    private final StatisticsView statisticsView;
    private final ConfigStorage configStorage;
    private final SettingsView settingsView;

    private Translation translation;
    private boolean max = true;

    public GuiController(GuiAPI guiAPI, GuiView view, RocketLeagueClient client, ConfigStorage configStorage) throws IOException {
        this.configStorage = configStorage;
        this.translation = TranslationParser.parse(Files.readString(Path.of("res", "lang", "lang.json")), configStorage.load().getLang());
        this.guiAPI = guiAPI;
        this.view = view;
        this.client = client;

        this.homeView = new HomeView(translation.getHomeTrans(), translation.getStatisticsTrans());
        this.statisticsView = new StatisticsView(translation.getStatisticsTrans());
        this.settingsView = new SettingsView(translation.getSettingsTrans());

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
            max = true;
            updateStatistics();
        });

        statisticsView.getOneVsOneButton().setOnAction(event -> {
            statisticsView.selectFilter(statisticsView.getOneVsOneButton());
            max = true;
            updateStatistics();
        });

        statisticsView.getTwoVsTwoButton().setOnAction(event -> {
            statisticsView.selectFilter(statisticsView.getTwoVsTwoButton());
            max = true;
            updateStatistics();
        });

        statisticsView.getThreeVsThreeButton().setOnAction(event -> {
            statisticsView.selectFilter(statisticsView.getThreeVsThreeButton());
            max = true;
            updateStatistics();
        });

        statisticsView.getTodayMatchesButton().setOnAction(event -> {
            statisticsView.selectToday();
            updateStatistics();
        });

        statisticsView.getMatchLimitField().setOnAction(event -> {
            updateStatistics();
            max = getMatchLimit() == null;

        });
    }

// ============================
// Estadísticas
// ============================

    private void updateStatistics() {

        int playlistId = getSelectedPlaylistId();
        Integer limit = getMatchLimit();

        Platform.runLater(() -> {

            if (max && !statisticsView.getTodayMatchesButton().getStyleClass().contains("selected")) {

                statisticsView.getMatchLimitField().setText("" + guiAPI.getPlayedGames(playlistId));

                if (playlistId == -1) {
                    updateStatisticsAll();
                } else {
                    updateStatisticsPlaylist(playlistId);
                }

            } else if (!statisticsView.getTodayMatchesButton().getStyleClass().contains("selected")){

                if (playlistId == -1) {
                    updateStatisticsLast(limit);
                } else {
                    updateStatisticsLast(limit, playlistId);
                }
            } else {
                updateStatisticsToday(playlistId);
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

    private void updateStatisticsToday(int playlistId) {
        statisticsView.setWinPercentage(guiAPI.getTodayWinPercentage(playlistId));
        statisticsView.setAverageGoals(guiAPI.getTodayAverageGoals(playlistId));
        statisticsView.setAverageShots(guiAPI.getTodayAverageShots(playlistId));
        statisticsView.setAverageSaves(guiAPI.getTodayAverageSaves(playlistId));
        statisticsView.setAverageAssists(guiAPI.getTodayAverageAssists(playlistId));
        statisticsView.setAverageDemos(guiAPI.getTodayAverageDemos(playlistId));
        statisticsView.setAverageAirPercentage(guiAPI.getTodayAverageAirPercentage(playlistId));
        statisticsView.setAverageSupersonicPercentage(guiAPI.getTodayAverageSupersonicPercentage(playlistId));
        statisticsView.setAverageSpeed(guiAPI.getTodayAverageSpeed(playlistId));
        statisticsView.setAverageBoostUsedSupersonic(guiAPI.getTodayAverageBoostUsedSupersonic(playlistId));
        statisticsView.setAverageSupersonicSessionPercentage(guiAPI.getTodayAverageSupersonicSessionPercentage(playlistId));
        statisticsView.setAverageBoostToSupersonic(guiAPI.getTodayAverageBoostToSupersonic(playlistId));
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
        homeView.setRecentMatches(guiAPI.getLastMatches(5));
    }

    // ============================
    // Settings
    // ============================

    private void showSettings() {

        view.setContent(settingsView
                .getRoot());
        view.selectNavigationButton(view.getSettingsButton());
        loadSettings();
    }

    private void loadSettings() {

        Config config = configStorage.load();
        settingsView
                .setPlayerName(config.getPlayerName());
        settingsView
                .setStoragePath(config.getStoragePath().toString());
        settingsView
                .setPacketSendRateField(config.getPacketSendRate().toString());
    }

    private void saveSettings() {

        String playerName = settingsView
                .getPlayerName();

        String storagePath = settingsView
                .getStoragePath();
        int packetSendRate;
        try {
            packetSendRate = Integer.parseInt(settingsView
                    .getPacketSendRate());

            if (playerName.isBlank()) {
                settingsView
                        .setStatus(translation.getSettingsTrans().getEmptyPlayerName());
                return;
            }

            if (storagePath.isBlank()) {
                settingsView
                        .setStatus(translation.getSettingsTrans().getEmptyStoragePath());

                return;
            }

            if (packetSendRate <= 0) {
                settingsView
                        .setStatus(translation.getSettingsTrans().getInvalidPacketSendRate());

                return;
            }

            Config currentConfig = configStorage.load();
            Config config = new Config(playerName, currentConfig.getRocketLeagueUrl(), Path.of(storagePath), packetSendRate, "");
            configStorage.save(config);

            settingsView
                    .setStatus(translation.getSettingsTrans().getSaved());

            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            if(client.isRunning()) {
                client.stop();
                pause.setOnFinished(event -> {
                    settingsView
                            .setStatus("");
                    client.start();
                });
            } else{
                pause.setOnFinished(event -> settingsView
                        .setStatus(""));
            }
            pause.play();


        } catch (NumberFormatException e) {
            settingsView
                    .setStatus(translation.getSettingsTrans().getInvalidPacketSendRate());
        }
    }

    private void configureSettings() {

        settingsView
                .getBrowseButton().setOnAction(event -> browseStoragePath());
        settingsView
                .getSaveButton().setOnAction(event -> saveSettings());
    }

    private void browseStoragePath() {

        javafx.stage.Stage stage = (javafx.stage.Stage) view.getRoot().getScene().getWindow();
        boolean wasMaximized = stage.isMaximized();
        javafx.stage.DirectoryChooser chooser = new javafx.stage.DirectoryChooser();

        chooser.setTitle("Select storage folder");
        java.io.File selected = chooser.showDialog(stage);

        if (selected != null) {
            settingsView
                    .setStoragePath(selected.toPath().toString());
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