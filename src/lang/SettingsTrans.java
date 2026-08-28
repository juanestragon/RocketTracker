package lang;

public class SettingsTrans {

    private final String emptyPlayerName;
    private final String emptyStoragePath;
    private final String invalidPacketSendRate;
    private final String saved;
    private final String browseButton;
    private final String saveButton;
    private final String title;
    private final String playerLabel;
    private final String playerTextField;
    private final String storagePathLabel;
    private final String packetSendRateLabel;
    private final String languageLabel;

    public SettingsTrans(String emptyPlayerName, String emptyStoragePath, String invalidPacketSendRate, String saved, String browseButton, String saveButton, String title, String playerLabel, String playerTextField, String storagePathLabel, String packetSendRateLabel, String languageLabel) {
        this.emptyPlayerName = emptyPlayerName;
        this.emptyStoragePath = emptyStoragePath;
        this.invalidPacketSendRate = invalidPacketSendRate;
        this.saved = saved;
        this.browseButton = browseButton;
        this.saveButton = saveButton;
        this.title = title;
        this.playerLabel = playerLabel;
        this.playerTextField = playerTextField;
        this.storagePathLabel = storagePathLabel;
        this.packetSendRateLabel = packetSendRateLabel;
        this.languageLabel = languageLabel;
    }

    public String getEmptyPlayerName() {
        return emptyPlayerName;
    }

    public String getEmptyStoragePath() {
        return emptyStoragePath;
    }

    public String getInvalidPacketSendRate() {
        return invalidPacketSendRate;
    }

    public String getSaved() {
        return saved;
    }

    public String getBrowseButton() {
        return browseButton;
    }

    public String getSaveButton() {
        return saveButton;
    }

    public String getTitle() {
        return title;
    }

    public String getPlayerLabel() {
        return playerLabel;
    }

    public String getPlayerTextField() {
        return playerTextField;
    }

    public String getStoragePathLabel() {
        return storagePathLabel;
    }

    public String getPacketSendRateLabel() {
        return packetSendRateLabel;
    }

    public String getLanguageLabel() {
        return languageLabel;
    }
}
