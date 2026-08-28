package lang;

public class HomeTrans {
    private final String currentMatchLabel;
    private final String playerLabel;
    private final String dateLabel;
    private final String resultLabel;
    private final String resultLabelWin;
    private final String resultLabelLoose;
    private final String lastMatchesLabel;
    private final String lastMatchLabel;

    public HomeTrans(String currentMatchLabel, String playerLabel, String dateLabel, String resultLabel, String resultLabelWin, String resultLabelLoose, String lastMatchesLabel, String lastMatchLabel) {
        this.currentMatchLabel = currentMatchLabel;
        this.playerLabel = playerLabel;
        this.dateLabel = dateLabel;
        this.resultLabel = resultLabel;
        this.resultLabelWin = resultLabelWin;
        this.resultLabelLoose = resultLabelLoose;
        this.lastMatchesLabel = lastMatchesLabel;
        this.lastMatchLabel = lastMatchLabel;
    }

    public String getCurrentMatchLabel() {
        return currentMatchLabel;
    }

    public String getPlayerLabel() {
        return playerLabel;
    }

    public String getDateLabel() {
        return dateLabel;
    }

    public String getResultLabel() {
        return resultLabel;
    }

    public String getResultLabelWin() {
        return resultLabelWin;
    }

    public String getResultLabelLoose() {
        return resultLabelLoose;
    }

    public String getLastMatchesLabel() {
        return lastMatchesLabel;
    }

    public String getLastMatchLabel() {
        return lastMatchLabel;
    }
}
