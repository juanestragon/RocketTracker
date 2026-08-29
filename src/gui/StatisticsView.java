package gui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lang.StatisticsTrans;

public class StatisticsView {

    private final VBox root;

    private StatisticsTrans statisticsTrans;

    // ============================
    // Filtros
    // ============================

    private final Button allButton;
    private final Button oneVsOneButton;
    private final Button twoVsTwoButton;
    private final Button threeVsThreeButton;
    private final Button todayMatchesButton;

    private final Label matchLimitLabel;
    private final TextField matchLimitField;

    // ============================
    // Estadísticas
    // ============================

    private final Label winPercentageLabel;

    private final Label averageGoalsLabel;
    private final Label averageShotsLabel;
    private final Label averageSavesLabel;
    private final Label averageAssistsLabel;

    private final Label averageDemosLabel;
    private final Label averageAirPercentageLabel;
    private final Label averageSupersonicPercentageLabel;
    private final Label averageSpeedLabel;

    private final Label averageBoostUsedSupersonicLabel;
    private final Label averageSupersonicSessionPercentageLabel;
    private final Label averageBoostToSupersonicLabel;

    public StatisticsView(StatisticsTrans statisticsTrans) {

        root = new VBox();

        this.statisticsTrans = statisticsTrans;

        allButton = new Button(statisticsTrans.getAllButton());
        oneVsOneButton = new Button("1v1");
        twoVsTwoButton = new Button("2v2");
        threeVsThreeButton = new Button("3v3");
        todayMatchesButton = new Button(statisticsTrans.getTodayButton());

        matchLimitLabel = new Label(statisticsTrans.getMatchLimitLabel());

        matchLimitField = new TextField();
        matchLimitField.setPromptText(statisticsTrans.getMatchLimitUnavailable());

        winPercentageLabel = new Label("-");

        averageGoalsLabel = new Label("-");
        averageShotsLabel = new Label("-");
        averageSavesLabel = new Label("-");
        averageAssistsLabel = new Label("-");

        averageDemosLabel = new Label("-");
        averageAirPercentageLabel = new Label("-");
        averageSupersonicPercentageLabel = new Label("-");
        averageSpeedLabel = new Label("-");

        averageBoostUsedSupersonicLabel = new Label("-");
        averageSupersonicSessionPercentageLabel = new Label("-");
        averageBoostToSupersonicLabel = new Label("-");

        build();
    }

    // ============================
    // Construcción
    // ============================

    private void build() {

        root.getStyleClass().add("statistics-view");
        root.getChildren().clear();

        Label title = new Label(statisticsTrans.getTitle());
        title.getStyleClass().add("statistics-title");

        HBox filterBar = createFilterBar();
        VBox winRate = createWinRate();
        GridPane basicStatistics = createBasicStatistics();
        GridPane advancedStatistics = createAdvancedStatistics();
        VBox boostStatistics = createBoostStatistics();

        root.getChildren().addAll(
                title,
                filterBar,
                winRate,
                basicStatistics,
                advancedStatistics,
                boostStatistics);
    }

    // ============================
    // Filtros
    // ============================

    private HBox createFilterBar() {

        HBox filterBar = new HBox();
        filterBar.getChildren().clear();
        filterBar.getStyleClass().add("statistics-filter-bar");

        configureFilterButton(allButton);
        configureFilterButton(oneVsOneButton);
        configureFilterButton(twoVsTwoButton);
        configureFilterButton(threeVsThreeButton);
        configureFilterButton(todayMatchesButton);

        matchLimitLabel.getStyleClass().add("statistics-filter-label");
        matchLimitField.getStyleClass().add("statistics-match-limit");
        matchLimitField.setPrefWidth(120);

        filterBar.getChildren().addAll(
                allButton,
                oneVsOneButton,
                twoVsTwoButton,
                threeVsThreeButton,
                matchLimitLabel,
                matchLimitField,
                todayMatchesButton);

        return filterBar;
    }

    public void updateLang(StatisticsTrans statisticsTrans) {
        this.statisticsTrans = statisticsTrans;

        allButton.setText(statisticsTrans.getAllButton());
        todayMatchesButton.setText(statisticsTrans.getTodayButton());
        matchLimitLabel.setText(statisticsTrans.getMatchLimitLabel());

        build();
    }

    private void configureFilterButton(Button button) {
        button.getStyleClass().add("statistics-filter-button");
    }

    public void selectFilter(Button selectedButton) {

        allButton.getStyleClass().remove("selected");
        oneVsOneButton.getStyleClass().remove("selected");
        twoVsTwoButton.getStyleClass().remove("selected");
        threeVsThreeButton.getStyleClass().remove("selected");

        selectedButton.getStyleClass().add("selected");
    }

    public void selectToday() {
        if (todayMatchesButton.getStyleClass().contains("selected")) {
            todayMatchesButton.getStyleClass().removeAll("selected");
            matchLimitField.setDisable(false);
        } else {
            todayMatchesButton.getStyleClass().add("selected");
            matchLimitField.setText("");
            matchLimitField.setDisable(true);
            matchLimitField.setText(statisticsTrans.getMatchLimitUnavailable());
        }
    }

    // ============================
    // Win rate
    // ============================

    private VBox createWinRate() {

        winPercentageLabel.getStyleClass().add("statistics-win-rate-value");

        Label title = new Label(statisticsTrans.getWinRateLabel());
        title.getStyleClass().add("statistics-win-rate-title");

        VBox box = new VBox();
        box.getStyleClass().add("statistics-win-rate");
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(winPercentageLabel, title);

        return box;
    }

    // ============================
    // Estadísticas básicas
    // ============================

    private GridPane createBasicStatistics() {

        GridPane grid = new GridPane();
        grid.getStyleClass().add("statistics-grid");
        grid.setHgap(12);
        grid.setVgap(12);

        grid.add(createStatistic(statisticsTrans.getGoalsLabel(), averageGoalsLabel), 0, 0);
        grid.add(createStatistic(statisticsTrans.getShotsLabel(), averageShotsLabel), 1, 0);
        grid.add(createStatistic(statisticsTrans.getSavesLabel(), averageSavesLabel), 2, 0);
        grid.add(createStatistic(statisticsTrans.getAssistsLabel(), averageAssistsLabel), 3, 0);

        return grid;
    }

    // ============================
    // Estadísticas avanzadas
    // ============================

    private GridPane createAdvancedStatistics() {

        GridPane grid = new GridPane();
        grid.getStyleClass().add("statistics-grid");
        grid.setHgap(12);
        grid.setVgap(12);

        grid.add(createStatistic(statisticsTrans.getDemosLabel(), averageDemosLabel), 0, 0);
        grid.add(createStatistic(statisticsTrans.getAirTimeLabel(), averageAirPercentageLabel), 1, 0);
        grid.add(createStatistic(statisticsTrans.getSupersonicTimeLabel(), averageSupersonicPercentageLabel), 2, 0);
        grid.add(createStatistic(statisticsTrans.getSpeedLabel(), averageSpeedLabel), 3, 0);

        return grid;
    }

    // ============================
    // Estadísticas de boost
    // ============================

    private VBox createBoostStatistics() {

        VBox box = new VBox();
        box.getStyleClass().add("statistics-boost");
        box.getChildren().addAll(
                createStatisticRow(statisticsTrans.getBoostToSupersonicLabel(), averageBoostToSupersonicLabel),
                createStatisticRow(statisticsTrans.getBoostInSupersonicLabel(), averageBoostUsedSupersonicLabel),
                createStatisticRow(statisticsTrans.getSupersonicSessionPercentageLabel(), averageSupersonicSessionPercentageLabel));

        return box;
    }

    private HBox createStatisticRow(String title, Label value) {

        value.getStyleClass().add("statistics-boost-value");

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("statistics-boost-title");

        HBox row = new HBox();
        row.getStyleClass().add("statistics-boost-row");
        row.getChildren().addAll(titleLabel, value);

        return row;
    }

    private VBox createStatistic(String title, Label value) {

        value.getStyleClass().add("metric-value");

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("metric-title");

        VBox box = new VBox();
        box.getStyleClass().add("statistics-card");
        box.getChildren().addAll(titleLabel, value);

        return box;
    }

    // ============================
    // Getters
    // ============================

    public VBox getRoot() {
        return root;
    }

    public Button getAllButton() {
        return allButton;
    }

    public Button getOneVsOneButton() {
        return oneVsOneButton;
    }

    public Button getTwoVsTwoButton() {
        return twoVsTwoButton;
    }

    public Button getThreeVsThreeButton() {
        return threeVsThreeButton;
    }

    public Button getTodayMatchesButton(){
        return todayMatchesButton;
    }

    public TextField getMatchLimitField() {
        return matchLimitField;
    }

    // ============================
    // Setters de estadísticas
    // ============================

    public void setWinPercentage(double value) {
        winPercentageLabel.setText(formatPercentage(value));
    }

    public void setAverageGoals(double value) {
        averageGoalsLabel.setText(formatNumber(value));
    }

    public void setAverageShots(double value) {
        averageShotsLabel.setText(formatNumber(value));
    }

    public void setAverageSaves(double value) {
        averageSavesLabel.setText(formatNumber(value));
    }

    public void setAverageAssists(double value) {
        averageAssistsLabel.setText(formatNumber(value));
    }

    public void setAverageDemos(double value) {
        averageDemosLabel.setText(formatNumber(value));
    }

    public void setAverageAirPercentage(double value) {
        averageAirPercentageLabel.setText(formatPercentage(value));
    }

    public void setAverageSupersonicPercentage(double value) {
        averageSupersonicPercentageLabel.setText(formatPercentage(value));
    }

    public void setAverageSpeed(double value) {
        averageSpeedLabel.setText(formatNumber(value));
    }

    public void setAverageBoostUsedSupersonic(double value) {
        averageBoostUsedSupersonicLabel.setText(formatNumber(value));
    }

    public void setAverageSupersonicSessionPercentage(double value) {
        averageSupersonicSessionPercentageLabel.setText(formatPercentage(value));
    }

    public void setAverageBoostToSupersonic(double value) {
        averageBoostToSupersonicLabel.setText(formatNumber(value));
    }

    // ============================
    // Formato
    // ============================

    private String formatPercentage(double value) {
        return String.format("%.1f%%", value);
    }

    private String formatNumber(double value) {
        return String.format("%.2f", value);
    }
}