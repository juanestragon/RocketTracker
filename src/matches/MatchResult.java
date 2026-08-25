package matches;

public class MatchResult {

    private final String date;
    private final String playerName;
    private final String matchGuid;
    private final int playlistId;

    private final boolean won;

    private final int assists;
    private final int saves;
    private final int shots;
    private final int goals;
    private final int demos;

    private final double airPercentage;
    private final double supersonicPercentage;
    private final double boostUsedSupersonic;
    private final double supersonicSessionPercentage;
    private final double averageBoostToSupersonic;
    private final double averageSpeed;

    public MatchResult(
            String date,
            String playerName,
            String matchGuid,
            int playlistId,
            boolean won,
            int assists,
            int saves,
            int shots,
            int goals,
            int demos,
            double airPercentage,
            double supersonicPercentage,
            double boostUsedSupersonic,
            double supersonicSessionPercentage,
            double averageBoostToSupersonic,
            double averageSpeed
    ) {
        this.date = date;
        this.playerName = playerName;
        this.matchGuid = matchGuid;
        this.playlistId = playlistId;
        this.won = won;
        this.assists = assists;
        this.saves = saves;
        this.shots = shots;
        this.goals = goals;
        this.demos = demos;
        this.airPercentage = airPercentage;
        this.supersonicPercentage = supersonicPercentage;
        this.boostUsedSupersonic = boostUsedSupersonic;
        this.supersonicSessionPercentage = supersonicSessionPercentage;
        this.averageBoostToSupersonic = averageBoostToSupersonic;
        this.averageSpeed = averageSpeed;
    }

    public String getDate() {
        return date;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getMatchGuid() {
        return matchGuid;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public boolean isWon() {
        return won;
    }

    public int getAssists() {
        return assists;
    }

    public int getSaves() {
        return saves;
    }

    public int getShots() {
        return shots;
    }

    public int getGoals() {
        return goals;
    }

    public int getDemos() {
        return demos;
    }

    public double getAirPercentage() {
        return airPercentage;
    }

    public double getSupersonicPercentage() {
        return supersonicPercentage;
    }

    public double getBoostUsedSupersonic() {
        return boostUsedSupersonic;
    }

    public double getSupersonicSessionPercentage() {
        return supersonicSessionPercentage;
    }

    public double getAverageBoostToSupersonic() {
        return averageBoostToSupersonic;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }
}