package web;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import matches.MatchResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HomeView {

    private final VBox root;

    // ============================
    // Partida actual
    // ============================

    private final Label currentMatchLabel;

    // ============================
    // Última partida
    // ============================

    private final VBox lastMatchCard;

    private final Label lastPlayerLabel;
    private final Label lastDateLabel;
    private final Label lastResultLabel;
    private final Label lastPlaylistLabel;

    private final Label assistsLabel;
    private final Label savesLabel;
    private final Label shotsLabel;
    private final Label goalsLabel;
    private final Label demosLabel;

    private final Label airPercentageLabel;
    private final Label supersonicPercentageLabel;
    private final Label boostUsedSupersonicLabel;
    private final Label supersonicSessionPercentageLabel;
    private final Label averageBoostToSupersonicLabel;
    private final Label averageSpeedLabel;

    // ============================
    // Últimas partidas
    // ============================

    private final GridPane recentMatchesBox;

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public HomeView() {

        root = new VBox();

        currentMatchLabel = new Label("No hay ninguna partida en curso");
        lastMatchCard = new VBox();
        lastPlayerLabel = new Label("-");
        lastDateLabel = new Label("-");
        lastResultLabel = new Label("-");
        lastPlaylistLabel = new Label("-");

        assistsLabel = new Label("-");
        savesLabel = new Label("-");
        shotsLabel = new Label("-");
        goalsLabel = new Label("-");
        demosLabel = new Label("-");
        airPercentageLabel = new Label("-");
        supersonicPercentageLabel = new Label("-");
        boostUsedSupersonicLabel = new Label("-");
        supersonicSessionPercentageLabel = new Label("-");
        averageBoostToSupersonicLabel = new Label("-");
        averageSpeedLabel = new Label("-");
        recentMatchesBox = new GridPane();
        root.setMaxWidth(Double.MAX_VALUE);
        root.setMaxHeight(Double.MAX_VALUE);

        build();
    }

    private void build() {

        root.getStyleClass().add("home-view");

        currentMatchLabel.getStyleClass().add(
                "current-match"
        );

        VBox lastMatchSection =
                createLastMatchSection();

        VBox recentMatchesSection =
                createRecentMatchesSection();

        root.getChildren().addAll(
                currentMatchLabel,
                lastMatchSection,
                recentMatchesSection
        );
    }

    // ============================
    // Última partida
    // ============================

    private VBox createLastMatchSection() {

        VBox section =
                new VBox();

        section.getStyleClass().add(
                "home-section"
        );

        Label title =
                new Label("Última partida");

        title.getStyleClass().add(
                "section-title"
        );

        lastMatchCard.getStyleClass().add(
                "last-match-card"
        );

        GridPane informationGrid =
                new GridPane();

        informationGrid.getStyleClass().add(
                "match-information-grid"
        );

        informationGrid.setHgap(40);
        informationGrid.setVgap(18);

        informationGrid.add(
                createInfoBlock(
                        "Jugador",
                        lastPlayerLabel
                ),
                0,
                0
        );

        informationGrid.add(
                createInfoBlock(
                        "Fecha",
                        lastDateLabel
                ),
                1,
                0
        );

        informationGrid.add(
                createInfoBlock(
                        "Resultado",
                        lastResultLabel
                ),
                0,
                1
        );

        informationGrid.add(
                createInfoBlock(
                        "Playlist",
                        lastPlaylistLabel
                ),
                1,
                1
        );

        GridPane.setColumnSpan(
                informationGrid,
                2
        );

        VBox metrics =
                createMetrics();

        lastMatchCard.getChildren().addAll(
                informationGrid,
                metrics
        );

        section.getChildren().addAll(
                title,
                lastMatchCard
        );

        return section;
    }

    private VBox createInfoBlock(
            String title,
            Label value
    ) {

        VBox block =
                new VBox();

        block.getStyleClass().add(
                "match-info"
        );

        Label titleLabel =
                new Label(title);

        titleLabel.getStyleClass().add(
                "metric-title"
        );

        value.getStyleClass().add(
                "metric-value"
        );

        block.getChildren().addAll(
                titleLabel,
                value
        );

        return block;
    }

    private VBox createMetrics() {

        VBox metrics =
                new VBox();

        metrics.getStyleClass().add(
                "metrics-container"
        );

        HBox basicMetrics =
                new HBox();

        basicMetrics.getStyleClass().add(
                "metrics-row"
        );

        basicMetrics.getChildren().addAll(
                createMetric(
                        "Goles",
                        goalsLabel
                ),
                createMetric(
                        "Salvadas",
                        savesLabel
                ),
                createMetric(
                        "Tiros",
                        shotsLabel
                ),
                createMetric(
                        "Asistencias",
                        assistsLabel
                ),
                createMetric(
                        "Demos",
                        demosLabel
                )
        );

        HBox advancedMetrics =
                new HBox();

        advancedMetrics.getStyleClass().add(
                "metrics-row"
        );

        advancedMetrics.getChildren().addAll(
                createMetric(
                        "Tiempo en aire",
                        airPercentageLabel
                ),
                createMetric(
                        "Tiempo supersónico",
                        supersonicPercentageLabel
                ),
                createMetric(
                        "Boost hasta supersónico",
                        averageBoostToSupersonicLabel
                ),
                createMetric(
                        "Boost supersónico",
                        boostUsedSupersonicLabel
                ),
                createMetric(
                        "Boost que alcanzó supersónicas",
                        supersonicSessionPercentageLabel
                ),
                createMetric(
                        "Velocidad media",
                        averageSpeedLabel
                )
        );

        metrics.getChildren().addAll(
                basicMetrics,
                advancedMetrics
        );

        return metrics;
    }

    private VBox createMetric(
            String title,
            Label value
    ) {

        VBox metric =
                new VBox();

        metric.getStyleClass().add(
                "metric"
        );

        Label titleLabel =
                new Label(title);

        titleLabel.getStyleClass().add(
                "metric-title"
        );

        value.getStyleClass().add(
                "metric-value"
        );

        metric.getChildren().addAll(
                titleLabel,
                value
        );

        return metric;
    }

    // ============================
    // Últimas partidas
    // ============================

    private VBox createRecentMatchesSection() {

        VBox section =
                new VBox();

        section.getStyleClass().add(
                "home-section"
        );

        Label title =
                new Label("Últimas partidas");

        title.getStyleClass().add(
                "section-title"
        );

        recentMatchesBox.getStyleClass().add(
                "recent-matches"
        );

        ColumnConstraints column =
                new ColumnConstraints();

        column.setPercentWidth(100);

        recentMatchesBox
                .getColumnConstraints()
                .add(column);

        recentMatchesBox.setMaxWidth(
                Double.MAX_VALUE
        );

        VBox.setVgrow(
                recentMatchesBox,
                javafx.scene.layout.Priority.ALWAYS
        );

        section.getChildren().addAll(
                title,
                recentMatchesBox
        );

        return section;
    }

    // ============================
    // API pública
    // ============================

    public VBox getRoot() {
        return root;
    }

    public void setCurrentMatch(String arena, int playlistId) {

        currentMatchLabel.setText(
                getPlaylistName(playlistId)
                        + " - "
                        + getHumanArenaName(arena)
        );
    }

    public void clearCurrentMatch() {

        currentMatchLabel.setText("No hay ninguna partida en curso");
    }

    public void setLastMatch(MatchResult match) {

        if (match == null) {
            clearLastMatch();
            return;
        }

        lastPlayerLabel.setText(match.getPlayerName());
        lastDateLabel.setText(formatDate(match.getDate()));
        lastResultLabel.setText(match.isWon() ? "Victoria" : "Derrota");
        lastResultLabel.getStyleClass().removeAll("match-win", "match-loss");
        lastResultLabel.getStyleClass().add(match.isWon() ? "match-win" : "match-loss");
        lastPlaylistLabel.setText(getPlaylistName(match.getPlaylistId()));

        assistsLabel.setText(String.valueOf(match.getAssists()));
        savesLabel.setText(String.valueOf(match.getSaves()));
        shotsLabel.setText(String.valueOf(match.getShots()));
        goalsLabel.setText(String.valueOf(match.getGoals()));
        demosLabel.setText(String.valueOf(match.getDemos()));
        airPercentageLabel.setText(formatPercentage(match.getAirPercentage()));
        supersonicPercentageLabel.setText(formatPercentage(match.getSupersonicPercentage()));
        boostUsedSupersonicLabel.setText(formatNumber(match.getBoostUsedSupersonic()));
        supersonicSessionPercentageLabel.setText(formatPercentage(match.getSupersonicSessionPercentage()));
        averageBoostToSupersonicLabel.setText(formatNumber(match.getAverageBoostToSupersonic()));
        averageSpeedLabel.setText(formatNumber(match.getAverageSpeed()));
    }

    public void clearLastMatch() {

        lastPlayerLabel.setText("-");
        lastDateLabel.setText("-");
        lastResultLabel.setText("-");
        lastPlaylistLabel.setText("-");

        assistsLabel.setText("-");
        savesLabel.setText("-");
        shotsLabel.setText("-");
        goalsLabel.setText("-");
        demosLabel.setText("-");

        airPercentageLabel.setText("-");
        supersonicPercentageLabel.setText("-");
        boostUsedSupersonicLabel.setText("-");
        supersonicSessionPercentageLabel.setText("-");
        averageBoostToSupersonicLabel.setText("-");
        averageSpeedLabel.setText("-");
    }

    public void setRecentMatches(
            List<MatchResult> matches
    ) {

        recentMatchesBox.getChildren().clear();

        for (int i = 0; i < matches.size(); i++) {

            GridPane row =
                    createRecentMatch(
                            matches.get(i)
                    );

            recentMatchesBox.add(
                    row,
                    0,
                    i
            );

            GridPane.setHgrow(
                    row,
                    javafx.scene.layout.Priority.ALWAYS
            );

            GridPane.setFillWidth(
                    row,
                    true
            );
        }
    }

    private GridPane createRecentMatch(
            MatchResult match
    ) {

        GridPane row =
                new GridPane();

        row.getStyleClass().add(
                "recent-match"
        );

        row.setMaxWidth(
                Double.MAX_VALUE
        );

        ColumnConstraints playlistColumn =
                new ColumnConstraints();

        playlistColumn.setPercentWidth(30);

        ColumnConstraints resultColumn =
                new ColumnConstraints();

        resultColumn.setPercentWidth(30);

        ColumnConstraints dateColumn =
                new ColumnConstraints();

        dateColumn.setPercentWidth(40);

        row.getColumnConstraints().addAll(
                playlistColumn,
                resultColumn,
                dateColumn
        );

        Label playlist =
                new Label(
                        getPlaylistName(
                                match.getPlaylistId()
                        )
                );

        playlist.getStyleClass().add(
                "recent-match-playlist"
        );

        playlist.setMaxWidth(
                Double.MAX_VALUE
        );

        playlist.setAlignment(
                Pos.CENTER_LEFT
        );

        Label result =
                new Label(
                        match.isWon()
                                ? "Victoria"
                                : "Derrota"
                );

        result.getStyleClass().add(
                match.isWon()
                        ? "match-win"
                        : "match-loss"
        );

        result.setMaxWidth(
                Double.MAX_VALUE
        );

        result.setAlignment(
                Pos.CENTER
        );

        Label date =
                new Label(
                        formatDate(
                                match.getDate()
                        )
                );

        date.getStyleClass().add(
                "recent-match-date"
        );

        date.setMaxWidth(
                Double.MAX_VALUE
        );

        date.setAlignment(
                Pos.CENTER_RIGHT
        );

        row.add(
                playlist,
                0,
                0
        );

        row.add(
                result,
                1,
                0
        );

        row.add(
                date,
                2,
                0
        );

        return row;
    }

    // ============================
    // Utilidades
    // ============================

    private String getPlaylistName(int playListId) {
        return switch (playListId) {
            case -1 -> "Main Menu";

            case 0 -> "Casual";
            case 1 -> "1v1";
            case 2 -> "2v2";
            case 3 -> "3v3";
            case 4 -> "4v4";

            case 10 -> "Ranked 1v1";
            case 11 -> "Ranked 2v2";
            case 13 -> "Ranked 3v3";

            case 15 -> "Snow Day Casual";
            case 17 -> "Hoops Casual";
            case 18 -> "Rumble Casual";
            case 23 -> "Dropshot Casual";

            case 27 -> "Ranked Hoops";
            case 28 -> "Ranked Rumble";
            case 29 -> "Ranked Dropshot";
            case 30 -> "Ranked Snow Day";

            case 31 -> "Haunted Ball";
            case 32 -> "Beach Ball";
            case 33 -> "Gridiron";
            case 35 -> "Rocket Labs";
            case 37 -> "RumShot";
            case 38 -> "GodBall";
            case 41 -> "Boomer";
            case 43 -> "GodBall 2v2";
            case 44 -> "Special Snow Day";
            case 46 -> "Football";
            case 47 -> "Cubic";
            case 48 -> "Tactical Rumble";
            case 49 -> "Spring Loaded";
            case 50 -> "Speed Demon";
            case 52 -> "Rumble BM";
            case 54 -> "Knockout";
            case 55 -> "Thirdwheel";
            case 62 -> "Magnus Futball";

            case 22 -> "Custom Tournament";
            case 34 -> "Tournament";

            case 6 -> "Private Match";
            case 7 -> "Season";
            case 8 -> "Exhibition";
            case 9 -> "Free Training";
            case 16 -> "Rocket Labs";
            case 19 -> "Workshop";
            case 20 -> "Training Editor";
            case 21 -> "Custom Training";
            case 24 -> "LAN";
            case 26 -> "External Match";
            case 40 -> "Coops Vs AI";

            default -> "Desconocida (" + playListId + ")";
        };
    }

    private String getHumanArenaName(String arenaName) {
        return switch (arenaName) {
            //STANDARD

            case "Stadium_P", "Stadium_Race_Day_P", "Stadium_Day_P",
                 "Stadium_Winter_P", "Stadium_Foggy_P", "Stadium_10A_P" -> "DFH Stadium";
            case "Underwater_P", "Underwater_GRS_P" -> "Aquadome";
            case "Park_P", "Park_Bman_P", "Park_Night_P", "Park_Snowy_P", "Park_Rainy_P" -> "Beckwith Park";
            case "CS_P", "CS_Day_P", "CS_HW_P", "BB_P", "Swoosh_P" -> "Champions Field";
            case "Outlaw_P", "Outlaw_Oasis_P" -> "Deadeye Canyon";
            case "Woods_P", "Woods_Night_P", "Woods_Forest_P" -> "Drift Woods";
            case "FF_Dusk_P" -> "Estadio Vida";
            case "Farm_P", "Farm_Night_P", "Farm_HW_P", "Farm_GRS_P" -> "Farmstead";
            case "CHN_Stadium_P", "CHN_Stadium_Day_P", "FNI_Stadium_P" -> "Forbidden Temple";
            case "UF_Day_P" -> "Futura Garden";
            case "EuroStadium_P", "EuroStadium_Dusk_P", "EuroStadium_Night_P",
                 "EuroStadium_SnowNight_P", "EuroStadium_Rainy_P" -> "Mannfield";
            case "NeoTokyo_Standard_P", "NeoTokyo_Toon_P", "NeoTokyo_Hax_P", "NeoTokyo_Arcade_P",
                 "NeoTokyo_Hax_Signs_P", "NeoTokyo_Hax_Signs_Off_P" -> "Neo Tokyo";
            case "Music_P" -> "Neon Fields";
            case "Beach_P", "Beach_Night_P", "Beach_Night_GRS_P" -> "Salty Shores";
            case "Street_P" -> "Sovereign Heights";
            case "ARC_Standard_P", "ARC_Darc_P" -> "Starbase Arc";
            case "TrainStation_P", "TrainStation_Dawn_P", "Haunted_TrainStation_P",
                 "TrainStation_Night_P" -> "Urban Central";
            case "UtopiaStadium_P", "UtopiaStadium_Dusk_P", "UtopiaStadium_Lux_P", "UtopiaStadium_Snow_P" -> "Utopia Coliseum";
            case "Wasteland_S_P", "Wasteland_Night_S_P", "Wasteland_GRS_P" -> "Wasteland";
            case "Paname_Dusk_P" -> "Parc de Paris";

            // NON STANDARD

            case "ARC_P" -> "Arctagon";
            case "Wasteland_P", "Wasteland_Night_P" -> "Badlands";
            case "Labs_Holyfield_Space_P" -> "Force Field (Star Wars)";
            case "NeoTokyo_P" -> "Tokyo Underpass";
            case "ThrowbackStadium_P", "ThrowbackHockey_P" -> "Throwback Stadium";

            // LABS MAPS

            case "Labs_PillarHeat_P" -> "Barricade";
            case "Labs_Basin_P" -> "Basin";
            case "Labs_PillarWings_P" -> "Colossus";
            case "Labs_Corridor_P" -> "Corridor";
            case "Labs_Cosmic_P", "Labs_Cosmic_V4_P" -> "Cosmic";
            case "Labs_DoubleGoal_P" -> "Double Goal";
            case "Labs_Galleon_P", "Labs_Galleon_Mast_P" -> "Galleon";
            case "Labs_PillarGlass_P" -> "Hourglass";
            case "Labs_Holyfield_P" -> "Loophole";
            case "Labs_Octagon_P", "Labs_Octagon_02_P" -> "Octagon";
            case "Labs_CirclePillars_P" -> "Pillars";
            case "Labs_Octagon_B2B_02_P" -> "Roadblock";
            case "Labs_Underpass_P", "Labs_Underpass_v0_p" -> "Underpass";
            case "Labs_Utopia_P" -> "Utopia Retro";

            // EXTRA MODES

            case "HoopsStreet_P", "HoopsStreet_Art_P" -> "The Block";
            case "HoopsStadium_P" -> "Dunk House";

            case "ShatterShot_P" -> "Core 707";

            case "KO_Calavera_P" -> "Calavera";
            case "KO_Carbon_P" -> "Carbon";
            case "KO_Quadron_P" -> "Quadron";

            default -> "Unknown: " + arenaName;
        };
    }


    private String formatDate(
            String date
    ) {

        try {

            LocalDateTime dateTime =
                    LocalDateTime.parse(date);

            return DATE_FORMATTER.format(
                    dateTime
            );

        } catch (Exception e) {

            return date;
        }
    }

    private String formatPercentage(
            double value
    ) {

        return String.format(
                "%.1f%%",
                value
        );
    }

    private String formatNumber(
            double value
    ) {

        return String.format(
                "%.2f",
                value
        );
    }
}