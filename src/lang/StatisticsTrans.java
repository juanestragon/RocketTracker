package lang;

public class StatisticsTrans {

    private final String title;
    private final String allButton;
    private final String todayButton;
    private final String matchLimitLabel;
    private final String matchLimitUnavailable;
    private final String winRateLabel;
    private final String goalsLabel;
    private final String savesLabel;
    private final String assistsLabel;
    private final String shotsLabel;
    private final String demosLabel;
    private final String airTimeLabel;
    private final String supersonicTimeLabel;
    private final String speedLabel;
    private final String boostInSupersonicLabel;
    private final String boostToSupersonicLabel;
    private final String supersonicSessionPercentageLabel;

    public StatisticsTrans(String title,String allButton, String todayButton, String matchLimitLabel, String matchLimitUnavailable, String winRateLabel, String goalsLabel, String savesLabel, String assistsLabel, String shotsLabel, String demosLabel, String airTimeLabel, String supersonicTimeLabel, String speedLabel, String boostInSupersonicLabel, String boostToSupersonicLabel, String supersonicSessionPercentageLabel) {
        this.title = title;
        this.allButton = allButton;
        this.todayButton = todayButton;
        this.matchLimitLabel = matchLimitLabel;
        this.matchLimitUnavailable = matchLimitUnavailable;
        this.winRateLabel = winRateLabel;
        this.goalsLabel = goalsLabel;
        this.savesLabel = savesLabel;
        this.assistsLabel = assistsLabel;
        this.shotsLabel = shotsLabel;
        this.demosLabel = demosLabel;
        this.airTimeLabel = airTimeLabel;
        this.supersonicTimeLabel = supersonicTimeLabel;
        this.speedLabel = speedLabel;
        this.boostInSupersonicLabel = boostInSupersonicLabel;
        this.boostToSupersonicLabel = boostToSupersonicLabel;
        this.supersonicSessionPercentageLabel = supersonicSessionPercentageLabel;
    }

    public String getTitle() {
        return title;
    }

    public String getAllButton() {
        return allButton;
    }

    public String getTodayButton() {
        return todayButton;
    }

    public String getMatchLimitLabel() {
        return matchLimitLabel;
    }

    public String getMatchLimitUnavailable() {
        return matchLimitUnavailable;
    }

    public String getWinRateLabel() {
        return winRateLabel;
    }

    public String getGoalsLabel() {
        return goalsLabel;
    }

    public String getSavesLabel() {
        return savesLabel;
    }

    public String getAssistsLabel() {
        return assistsLabel;
    }

    public String getShotsLabel() {
        return shotsLabel;
    }

    public String getDemosLabel() {
        return demosLabel;
    }

    public String getAirTimeLabel() {
        return airTimeLabel;
    }

    public String getSupersonicTimeLabel() {
        return supersonicTimeLabel;
    }

    public String getSpeedLabel() {
        return speedLabel;
    }

    public String getBoostInSupersonicLabel() {
        return boostInSupersonicLabel;
    }

    public String getBoostToSupersonicLabel() {
        return boostToSupersonicLabel;
    }

    public String getSupersonicSessionPercentageLabel() {
        return supersonicSessionPercentageLabel;
    }
}
