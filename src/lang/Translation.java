package lang;

public class Translation {

    private final GuiTrans guiTrans;
    private final HomeTrans homeTrans;
    private final StatisticsTrans statisticsTrans;
    private final SettingsTrans settingsTrans;

    public Translation(GuiTrans guiTrans, HomeTrans homeTrans, StatisticsTrans statisticsTrans, SettingsTrans settingsTrans) {
        this.guiTrans = guiTrans;
        this.homeTrans = homeTrans;
        this.statisticsTrans = statisticsTrans;
        this.settingsTrans = settingsTrans;
    }

    public SettingsTrans getSettingsTrans() {
        return settingsTrans;
    }

    public StatisticsTrans getStatisticsTrans() {
        return statisticsTrans;
    }

    public HomeTrans getHomeTrans() {
        return homeTrans;
    }

    public GuiTrans getGuiTrans() {
        return guiTrans;
    }
}
