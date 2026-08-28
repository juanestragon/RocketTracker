package lang;

import util.JsonParser;

import java.util.Map;

public class TranslationParser {
    public static Translation parse(String json, String lang) {
        Object rootObject = JsonParser.parse(json);
        if (!(rootObject instanceof Map<?, ?> root)) {
            throw new IllegalArgumentException("El JSON de la traduccion debe ser un objeto");}

        Object objectLang = root.get(lang);
        if (!(objectLang instanceof Map<?, ?> langO)) {
            throw new IllegalArgumentException("objectLang no contiene un objeto lang válido");
        }

        Object guiObject = langO.get("gui");
        Object homeObject = langO.get("home");
        Object statisticsObject = langO.get("statistics");
        Object settingsObject = langO.get("settings");

        return new Translation(getGuiTrans(guiObject), getHomeTrans(homeObject), getStatisticsTrans(statisticsObject), getSettingsTrans(settingsObject));
    }

    private static String getString(Map<?, ?> object, String key) {

        Object value = object.get(key);
        if (value == null) {
            return null;
        }

        return String.valueOf(value);
    }

    private static GuiTrans getGuiTrans(Object guiObject) {

        if (!(guiObject instanceof Map<?, ?> gui)) {
            throw new IllegalArgumentException("guiObject no contiene un objeto gui válido");
        }

        String homeButton = getString(gui, "homeButton");
        String statisticsButton = getString(gui, "statisticsButton");
        String settingsButton = getString(gui, "settingsButton");
        String connectionStatusConnected = getString(gui, "connectionStatus-Connected");
        String connectionStatusConnecting = getString(gui, "connectionStatus-Connecting");
        String connectionStatusDisconnected = getString(gui, "connectionStatus-Disconnected");

        return new GuiTrans(homeButton, statisticsButton, settingsButton, connectionStatusConnected, connectionStatusConnecting,connectionStatusDisconnected);
    }

    private static HomeTrans getHomeTrans(Object homeObject) {
        if (!(homeObject instanceof Map<?,?> home)) {
            throw new IllegalArgumentException("homeObject no contiene un objeto home válido");
        }

        String currentMatchLabel = getString(home, "currentMatchLabel");
        String playerLabel = getString(home, "playerLabel");
        String dateLabel = getString(home, "dateLabel");
        String resultLabel = getString(home, "resultLabel");
        String resultLabelWin = getString(home, "resultLabelWin");
        String resultLabelLoose = getString(home, "resultLabelLoose");
        String lastMatchesLabel = getString(home, "lastMatchesLabel");
        String lastMatchLabel = getString(home, "lastMatchLabel");

        return new HomeTrans(currentMatchLabel, playerLabel, dateLabel, resultLabel, resultLabelWin, resultLabelLoose, lastMatchesLabel, lastMatchLabel);
    }

    private static StatisticsTrans getStatisticsTrans(Object statisticsObject) {
        if (!(statisticsObject instanceof Map<?,?> statistics)) {
            throw new IllegalArgumentException("statisticsObject no contiene un objeto statistics válido");
        }

        String title = getString(statistics, "title");
        String allButton = getString(statistics, "allButton");
        String todayButton = getString(statistics, "todayButton");
        String matchLimitLabel = getString(statistics, "matchLimitLabel");
        String matchLimitUnavailable = getString(statistics, "matchLimitUnavailable");
        String winRateLabel = getString(statistics, "winRateLabel");
        String goalsLabel = getString(statistics, "goalsLabel");
        String savesLabel = getString(statistics, "savesLabel");
        String assistsLabel = getString(statistics, "assistsLabel");
        String shotsLabel = getString(statistics, "shotsLabel");
        String demosLabel = getString(statistics, "demosLabel");
        String airTimeLabel = getString(statistics, "airTimeLabel");
        String supersonicTimeLabel = getString(statistics, "supersonicTimeLabel");
        String speedLabel = getString(statistics, "speedLabel");
        String boostInSupersonicLabel = getString(statistics, "boostInSupersonicLabel");
        String boostToSupersonicLabel = getString(statistics, "boostToSupersonicLabel");
        String supersonicSessionPercentageLabel = getString(statistics, "supersonicSessionPercentageLabel");

        return new StatisticsTrans(title, allButton, todayButton, matchLimitLabel, matchLimitUnavailable, winRateLabel, goalsLabel, savesLabel,
                assistsLabel, shotsLabel, demosLabel, airTimeLabel, supersonicTimeLabel, speedLabel, boostInSupersonicLabel, boostToSupersonicLabel, supersonicSessionPercentageLabel);

    }

    private static SettingsTrans getSettingsTrans(Object settingsObject) {
        if (!(settingsObject instanceof Map<?,?> settings)) {
            throw new IllegalArgumentException("settingsObject no contiene un objeto settings válido");
        }

        String emptyPlayerName = getString(settings, "emptyPlayerName");
        String emptyStoragePath = getString(settings, "emptyStoragePath");
        String invalidPacketSendRate = getString(settings, "invalidPacketSendRate");
        String saved = getString(settings, "saved");
        String browseButton = getString(settings, "browseButton");
        String saveButton = getString(settings, "saveButton");
        String title = getString(settings, "title");
        String playerLabel = getString(settings, "playerLabel");
        String playerTextField = getString(settings, "playerTextField");
        String storagePathLabel = getString(settings, "storagePathLabel");
        String packetSendRateLabel = getString(settings, "packetSendRateLabel");
        String languageLabel = getString(settings, "languageLabel");

        return new SettingsTrans(emptyPlayerName, emptyStoragePath, invalidPacketSendRate, saved, browseButton, saveButton,
                title, playerLabel, playerTextField, storagePathLabel, packetSendRateLabel, languageLabel);
    }
}
