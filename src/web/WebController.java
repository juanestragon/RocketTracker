package web;

import config.Config;
import config.ConfigStorage;
import events.WebEventListener;
import javafx.application.Platform;
import network.ConnectionState;
import network.ConnectionStateListener;
import network.RocketLeagueClient;

import java.nio.file.Path;

public class WebController implements WebEventListener, ConnectionStateListener {

    private final WebAPI webAPI;
    private final WebView view;
    private final RocketLeagueClient client;

    private final HomeView homeView;
    private final StatisticsView statisticsView;
    private final ConfigStorage configStorage;

    // ============================
    // Estado del filtro
    // ============================

    /*
     * null = Todas
     *
     * 10 = Ranked 1v1
     * 11 = Ranked 2v2
     * 13 = Ranked 3v3
     */

    /*
     * null = todas las partidas
     * > 0 = últimas N partidas
     */

    public WebController(WebAPI webAPI, WebView view, RocketLeagueClient client, ConfigStorage configStorage) {
        this.configStorage = configStorage;
        this.webAPI = webAPI;
        this.view = view;
        this.client = client;

        this.homeView = new HomeView();
        this.statisticsView = new StatisticsView();

        configureNavigation();
        configureStatisticsFilters();
        configureConnectionButton();
        configureSettings();
        loadSettings();

        /*
         * Sincronizamos el estado actual del cliente
         * con la interfaz.
         */

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
    public void onConnectionStateChanged(
            ConnectionState state
    ) {

        Platform.runLater(() ->
                updateConnectionState(state)
        );
    }

    private void updateConnectionState(
            ConnectionState state
    ) {

        switch (state) {

            case CONNECTED -> {

                view.setConnectionConnected();
            }

            case CONNECTING -> {

                view.setConnectionConnecting();
            }

            case DISCONNECTED -> {

                view.setConnectionDisconnected();
            }
        }
    }

// ============================
// Filtros de estadísticas
// ============================

    private void configureStatisticsFilters() {

        statisticsView.getAllButton().setOnAction(event -> {

            statisticsView.selectFilter(
                    statisticsView.getAllButton()
            );

            updateStatistics();
        });

        statisticsView.getOneVsOneButton().setOnAction(event -> {

            statisticsView.selectFilter(
                    statisticsView.getOneVsOneButton()
            );

            updateStatistics();
        });

        statisticsView.getTwoVsTwoButton().setOnAction(event -> {

            statisticsView.selectFilter(
                    statisticsView.getTwoVsTwoButton()
            );

            updateStatistics();
        });

        statisticsView.getThreeVsThreeButton().setOnAction(event -> {

            statisticsView.selectFilter(
                    statisticsView.getThreeVsThreeButton()
            );

            updateStatistics();
        });

        statisticsView.getMatchLimitField().setOnAction(event ->
                updateStatistics()
        );
    }

// ============================
// Estadísticas
// ============================

    private void updateStatistics() {

        int playlistId = getSelectedPlaylistId();
        Integer limit = getMatchLimit();

        Platform.runLater(() -> {

            if (limit == null) {

                // ============================
                // Sin límite
                // ============================

                if (playlistId == -1) {
                    updateStatisticsAll();
                } else {
                    updateStatisticsPlaylist(playlistId);
                }

            } else {

                // ============================
                // Con límite
                // ============================

                if (playlistId == -1) {
                    updateStatisticsLast(limit);
                } else {
                    updateStatisticsLast(limit, playlistId);
                }
            }
        });
    }

    private void updateStatisticsAll() {

        statisticsView.setWinPercentage(
                webAPI.getWinPercentage()
        );

        statisticsView.setAverageGoals(
                webAPI.getAverageGoals()
        );

        statisticsView.setAverageShots(
                webAPI.getAverageShots()
        );

        statisticsView.setAverageSaves(
                webAPI.getAverageSaves()
        );

        statisticsView.setAverageAssists(
                webAPI.getAverageAssists()
        );

        statisticsView.setAverageDemos(
                webAPI.getAverageDemos()
        );

        statisticsView.setAverageAirPercentage(
                webAPI.getAverageAirPercentage()
        );

        statisticsView.setAverageSupersonicPercentage(
                webAPI.getAverageSupersonicPercentage()
        );

        statisticsView.setAverageSpeed(
                webAPI.getAverageSpeed()
        );

        statisticsView.setAverageBoostUsedSupersonic(
                webAPI.getAverageBoostUsedSupersonic()
        );

        statisticsView.setAverageSupersonicSessionPercentage(
                webAPI.getAverageSupersonicSessionPercentage()
        );

        statisticsView.setAverageBoostToSupersonic(
                webAPI.getAverageBoostToSupersonic()
        );
    }

    private int getSelectedPlaylistId() {

        if (statisticsView.getOneVsOneButton()
                .getStyleClass()
                .contains("selected")) {

            return 10;
        }

        if (statisticsView.getTwoVsTwoButton()
                .getStyleClass()
                .contains("selected")) {

            return 11;
        }

        if (statisticsView.getThreeVsThreeButton()
                .getStyleClass()
                .contains("selected")) {

            return 13;
        }

        return -1;
    }

    private Integer getMatchLimit() {

        String text =
                statisticsView
                        .getMatchLimitField()
                        .getText()
                        .trim();

        if (text.isEmpty()) {
            return null;
        }

        try {

            int value = Integer.parseInt(text);

            if (value <= 0) {
                return null;
            }

            return value;

        } catch (NumberFormatException e) {

            return null;
        }
    }

    private void updateStatisticsPlaylist(
            int playlistId
    ) {

        statisticsView.setWinPercentage(
                webAPI.getWinPercentage(
                        playlistId
                )
        );

        statisticsView.setAverageGoals(
                webAPI.getAverageGoals(
                        playlistId
                )
        );

        statisticsView.setAverageShots(
                webAPI.getAverageShots(
                        playlistId
                )
        );

        statisticsView.setAverageSaves(
                webAPI.getAverageSaves(
                        playlistId
                )
        );

        statisticsView.setAverageAssists(
                webAPI.getAverageAssists(
                        playlistId
                )
        );

        statisticsView.setAverageDemos(
                webAPI.getAverageDemos(
                        playlistId
                )
        );

        statisticsView.setAverageAirPercentage(
                webAPI.getAverageAirPercentage(
                        playlistId
                )
        );

        statisticsView.setAverageSupersonicPercentage(
                webAPI.getAverageSupersonicPercentage(
                        playlistId
                )
        );

        statisticsView.setAverageSpeed(
                webAPI.getAverageSpeed(
                        playlistId
                )
        );

        statisticsView.setAverageBoostUsedSupersonic(
                webAPI.getAverageBoostUsedSupersonic(
                        playlistId
                )
        );

        statisticsView.setAverageSupersonicSessionPercentage(
                webAPI.getAverageSupersonicSessionPercentage(
                        playlistId
                )
        );

        statisticsView.setAverageBoostToSupersonic(
                webAPI.getAverageBoostToSupersonic(
                        playlistId
                )
        );
    }

    private void updateStatisticsLast(
            int n,
            int playlistId
    ) {

        statisticsView.setWinPercentage(
                webAPI.getLastWinPercentage(
                        n,
                        playlistId
                )
        );

        statisticsView.setAverageGoals(
                webAPI.getLastAverageGoals(
                        n,
                        playlistId
                )
        );

        statisticsView.setAverageShots(
                webAPI.getLastAverageShots(
                        n,
                        playlistId
                )
        );

        statisticsView.setAverageSaves(
                webAPI.getLastAverageSaves(
                        n,
                        playlistId
                )
        );

        statisticsView.setAverageAssists(
                webAPI.getLastAverageAssists(
                        n,
                        playlistId
                )
        );

        statisticsView.setAverageDemos(
                webAPI.getLastAverageDemos(
                        n,
                        playlistId
                )
        );

        statisticsView.setAverageAirPercentage(
                webAPI.getLastAverageAirPercentage(
                        n,
                        playlistId
                )
        );

        statisticsView.setAverageSupersonicPercentage(
                webAPI.getLastAverageSupersonicPercentage(
                        n,
                        playlistId
                )
        );

        statisticsView.setAverageSpeed(
                webAPI.getLastAverageSpeed(
                        n,
                        playlistId
                )
        );

        statisticsView.setAverageBoostUsedSupersonic(
                webAPI.getLastAverageBoostUsedSupersonic(
                        n,
                        playlistId
                )
        );

        statisticsView.setAverageSupersonicSessionPercentage(
                webAPI.getLastAverageSupersonicSessionPercentage(
                        n,
                        playlistId
                )
        );

        statisticsView.setAverageBoostToSupersonic(
                webAPI.getLastAverageBoostToSupersonic(
                        n,
                        playlistId
                )
        );
    }

    private void updateStatisticsLast(int n) {

        statisticsView.setWinPercentage(
                webAPI.getLastWinPercentage(n)
        );

        statisticsView.setAverageGoals(
                webAPI.getLastAverageGoals(n)
        );

        statisticsView.setAverageShots(
                webAPI.getLastAverageShots(n)
        );

        statisticsView.setAverageSaves(
                webAPI.getLastAverageSaves(n)
        );

        statisticsView.setAverageAssists(
                webAPI.getLastAverageAssists(n)
        );

        statisticsView.setAverageDemos(
                webAPI.getLastAverageDemos(n)
        );

        statisticsView.setAverageAirPercentage(
                webAPI.getLastAverageAirPercentage(n)
        );

        statisticsView.setAverageSupersonicPercentage(
                webAPI.getLastAverageSupersonicPercentage(n)
        );

        statisticsView.setAverageSpeed(
                webAPI.getLastAverageSpeed(n)
        );

        statisticsView.setAverageBoostUsedSupersonic(
                webAPI.getLastAverageBoostUsedSupersonic(n)
        );

        statisticsView.setAverageSupersonicSessionPercentage(
                webAPI.getLastAverageSupersonicSessionPercentage(n)
        );

        statisticsView.setAverageBoostToSupersonic(
                webAPI.getLastAverageBoostToSupersonic(n)
        );
    }

    // ============================
    // Home
    // ============================

    private void updateHome() {

        Platform.runLater(() -> {

            if (webAPI.isMatchActive()) {

                homeView.setCurrentMatch(
                        webAPI.getCurrentArena(),
                        webAPI.getCurrentPlaylistId()
                );

            } else {

                homeView.clearCurrentMatch();
            }

            updateHomeMatches();
        });
    }

    private void updateHomeMatches() {

        homeView.setLastMatch(
                webAPI.getLastMatch()
        );

        homeView.setRecentMatches(
                webAPI.getLastMatches(10)
        );
    }

    // ============================
    // Settings
    // ============================

    private void showSettings() {

        view.setContent(
                view.getSettingsView().getRoot()
        );

        view.selectNavigationButton(
                view.getSettingsButton()
        );
    }

    private void loadSettings() {

        Config config = configStorage.load();
        view.getSettingsView().setPlayerName(config.getPlayerName());
        view.getSettingsView().setStoragePath(config.getStoragePath().toString());
    }

    private void saveSettings() {

        String playerName = view.getSettingsView().getPlayerName();

        String storagePath = view.getSettingsView().getStoragePath();
        if (playerName.isBlank()) {
            view.getSettingsView().setStatus(
                    "El nombre del jugador no puede estar vacío."
            );
            return;
        }

        if (storagePath.isBlank()) {
            view.getSettingsView().setStatus(
                    "La ruta de almacenamiento no puede estar vacía."
            );

            return;
        }

        Config currentConfig = configStorage.load();

        Config config = new Config(
                        playerName,
                        currentConfig.getRocketLeagueUrl(),
                        Path.of(storagePath));

        configStorage.save(config);

        view.getSettingsView().setStatus(
                "Configuración guardada."
        );
    }

    private void configureSettings() {

        view.getSettingsView()
                .getBrowseButton()
                .setOnAction(event -> browseStoragePath());

        view.getSettingsView()
                .getSaveButton()
                .setOnAction(event -> saveSettings());
    }

    private void browseStoragePath() {

        javafx.stage.Stage stage =
                (javafx.stage.Stage)
                        view.getRoot()
                                .getScene()
                                .getWindow();

        boolean wasMaximized =
                stage.isMaximized();

        javafx.stage.DirectoryChooser chooser =
                new javafx.stage.DirectoryChooser();

        chooser.setTitle(
                "Select storage folder"
        );

        java.io.File selected =
                chooser.showDialog(stage);

        if (selected != null) {

            view.getSettingsView().setStoragePath(
                    selected.toPath().toString()
            );
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

            homeView.setCurrentMatch(
                    webAPI.getCurrentArena(),
                    webAPI.getCurrentPlaylistId()
            );

            /*
             * Si estamos viendo Statistics, las estadísticas
             * se actualizan por si algún dato ha cambiado.
             */
            if (view.getView() ==
                    View.STATISTICS) {

                updateStatistics();
            }
        });
    }

    @Override
    public void onMatchFinished() {

        Platform.runLater(() -> {

            homeView.clearCurrentMatch();

            updateHomeMatches();

            if (view.getView() == View.STATISTICS) {

                updateStatistics();
            }
        });
    }
}