package matches;

import java.util.List;
import java.util.function.ToDoubleFunction;

public class MatchStatistics {

    private final MatchRepository repository;
    private final MatchRepository duelsRepository;
    private final MatchRepository doublesRepository;
    private final MatchRepository standardRepository;

    enum PlayListName{
        DUELS,
        DOUBLES,
        STANDARD
    }

    public MatchStatistics(MatchRepository repository) {
        this.repository = repository;
        duelsRepository = new MatchRepository(repository.getMatchesByPlaylist(10));
        doublesRepository = new MatchRepository(repository.getMatchesByPlaylist(11));
        standardRepository = new MatchRepository(repository.getMatchesByPlaylist(13));
    }

    // ============================
    // Métodos privados
    // ============================

    private double calculateAverage(List<MatchResult> matches, ToDoubleFunction<MatchResult> parameter) {

        if (matches.isEmpty()) {
            return 0.0;
        }

        double total = 0.0;

        for (MatchResult match : matches) {
            total += parameter.applyAsDouble(match);
        }

        return total / matches.size();
    }

    private double calculateWinPercentage(List<MatchResult> matches) {

        if (matches.isEmpty()) {
            return 0.0;
        }

        int wins = 0;

        for (MatchResult match : matches) {

            if (match.isWon()) {
                wins++;
            }
        }

        return (double) wins / matches.size() * 100.0;
    }

    private PlayListName getPlaylistName(int playListId) {
        return switch (playListId) {
            case 10 -> PlayListName.DUELS;
            case 11 -> PlayListName.DOUBLES;
            case 13 -> PlayListName.STANDARD;

            default -> null;
        };
    }

    public int getPlayedGames(int playListId) {
        return switch(getPlaylistName(playListId)) {
            case DUELS -> duelsRepository.getAllMatches().size();
            case DOUBLES -> doublesRepository.getAllMatches().size();
            case STANDARD -> standardRepository.getAllMatches().size();

            case null -> repository.getAllMatches().size();
        };
    }

// ============================
// Asistencias
// ============================

    public double getAverageAssists() {
        return calculateAverage(repository.getAllMatches(), MatchResult::getAssists);
    }

    public double getAverageAssists(int playlistId) {
        return switch (getPlaylistName(playlistId)) {
            case DUELS -> calculateAverage(duelsRepository.getAllMatches(), MatchResult::getAssists);
            case DOUBLES -> calculateAverage(doublesRepository.getAllMatches(), MatchResult::getAssists);
            case STANDARD -> calculateAverage(standardRepository.getAllMatches(), MatchResult::getAssists);
        };
    }

    public double getLastAverageAssists(int n) {
        return calculateAverage(repository.getLastMatches(n), MatchResult::getAssists);
    }

    public double getLastAverageAssists(int n, int playlistId) {
        return switch (getPlaylistName(playlistId)) {
            case DUELS -> calculateAverage(duelsRepository.getLastMatches(n), MatchResult::getAssists);
            case DOUBLES -> calculateAverage(doublesRepository.getLastMatches(n), MatchResult::getAssists);
            case STANDARD -> calculateAverage(standardRepository.getLastMatches(n), MatchResult::getAssists);
        };
    }

// ============================
// Paradas
// ============================

    public double getAverageSaves() {
        return calculateAverage(repository.getAllMatches(), MatchResult::getSaves);
    }

    public double getAverageSaves(int playlistId) {
        return switch (getPlaylistName(playlistId)) {
            case DUELS -> calculateAverage(duelsRepository.getAllMatches(), MatchResult::getSaves);
            case DOUBLES -> calculateAverage(doublesRepository.getAllMatches(), MatchResult::getSaves);
            case STANDARD -> calculateAverage(standardRepository.getAllMatches(), MatchResult::getSaves);
        };
    }

    public double getLastAverageSaves(int n) {
        return calculateAverage(repository.getLastMatches(n), MatchResult::getSaves);
    }

    public double getLastAverageSaves(int n, int playlistId) {
        return switch (getPlaylistName(playlistId)) {
            case DUELS -> calculateAverage(duelsRepository.getLastMatches(n), MatchResult::getSaves);
            case DOUBLES -> calculateAverage(doublesRepository.getLastMatches(n), MatchResult::getSaves);
            case STANDARD -> calculateAverage(standardRepository.getLastMatches(n), MatchResult::getSaves);
        };
    }

// ============================
// Tiros
// ============================

    public double getAverageShots() {
        return calculateAverage(repository.getAllMatches(), MatchResult::getShots);
    }

    public double getAverageShots(int playlistId) {
        return switch (getPlaylistName(playlistId)) {
            case DUELS -> calculateAverage(duelsRepository.getAllMatches(), MatchResult::getShots);
            case DOUBLES -> calculateAverage(doublesRepository.getAllMatches(), MatchResult::getShots);
            case STANDARD -> calculateAverage(standardRepository.getAllMatches(), MatchResult::getShots);
        };
    }

    public double getLastAverageShots(int n) {
        return calculateAverage(repository.getLastMatches(n), MatchResult::getShots);
    }

    public double getLastAverageShots(int n, int playlistId) {
        return switch (getPlaylistName(playlistId)) {
            case DUELS -> calculateAverage(duelsRepository.getLastMatches(n), MatchResult::getShots);
            case DOUBLES -> calculateAverage(doublesRepository.getLastMatches(n), MatchResult::getShots);
            case STANDARD -> calculateAverage(standardRepository.getLastMatches(n), MatchResult::getShots);
            default -> 0.0;
        };
    }

// ============================
// Goles
// ============================

    public double getAverageGoals() {
        return calculateAverage(repository.getAllMatches(), MatchResult::getGoals);
    }

    public double getAverageGoals(int playlistId) {
        return switch (getPlaylistName(playlistId)) {
            case DUELS -> calculateAverage(duelsRepository.getAllMatches(), MatchResult::getGoals);
            case DOUBLES -> calculateAverage(doublesRepository.getAllMatches(), MatchResult::getGoals);
            case STANDARD -> calculateAverage(standardRepository.getAllMatches(), MatchResult::getGoals);
        };
    }

    public double getLastAverageGoals(int n) {
        return calculateAverage(repository.getLastMatches(n), MatchResult::getGoals);
    }

    public double getLastAverageGoals(int n, int playlistId) {
        return switch (getPlaylistName(playlistId)) {
            case DUELS -> calculateAverage(duelsRepository.getLastMatches(n), MatchResult::getGoals);
            case DOUBLES -> calculateAverage(doublesRepository.getLastMatches(n), MatchResult::getGoals);
            case STANDARD -> calculateAverage(standardRepository.getLastMatches(n), MatchResult::getGoals);
        };
    }

// ============================
// Demoliciones
// ============================

    public double getAverageDemos() {
        return calculateAverage(repository.getAllMatches(), MatchResult::getDemos);
    }

    public double getAverageDemos(int playlistId) {
        return switch (getPlaylistName(playlistId)) {
            case DUELS -> calculateAverage(duelsRepository.getAllMatches(), MatchResult::getDemos);
            case DOUBLES -> calculateAverage(doublesRepository.getAllMatches(), MatchResult::getDemos);
            case STANDARD -> calculateAverage(standardRepository.getAllMatches(), MatchResult::getDemos);
        };
    }

    public double getLastAverageDemos(int n) {
        return calculateAverage(repository.getLastMatches(n), MatchResult::getDemos);
    }

    public double getLastAverageDemos(int n, int playlistId) {
        return switch (getPlaylistName(playlistId)) {
            case DUELS -> calculateAverage(duelsRepository.getLastMatches(n), MatchResult::getDemos);
            case DOUBLES -> calculateAverage(doublesRepository.getLastMatches(n), MatchResult::getDemos);
            case STANDARD -> calculateAverage(standardRepository.getLastMatches(n), MatchResult::getDemos);
        };
    }

// ============================
// Tiempo de aire
// ============================

    public double getAverageAirPercentage() {
        return calculateAverage(repository.getAllMatches(), MatchResult::getAirPercentage);
    }

    public double getAverageAirPercentage(int playlistId) {
        return switch (getPlaylistName(playlistId)) {
            case DUELS -> calculateAverage(duelsRepository.getAllMatches(), MatchResult::getAirPercentage);
            case DOUBLES -> calculateAverage(doublesRepository.getAllMatches(), MatchResult::getAirPercentage);
            case STANDARD -> calculateAverage(standardRepository.getAllMatches(), MatchResult::getAirPercentage);
        };
    }

    public double getLastAverageAirPercentage(int n) {
        return calculateAverage(repository.getLastMatches(n), MatchResult::getAirPercentage);
    }

    public double getLastAverageAirPercentage(int n, int playlistId) {
        return switch (getPlaylistName(playlistId)) {
            case DUELS -> calculateAverage(duelsRepository.getLastMatches(n), MatchResult::getAirPercentage);
            case DOUBLES -> calculateAverage(doublesRepository.getLastMatches(n), MatchResult::getAirPercentage);
            case STANDARD -> calculateAverage(standardRepository.getLastMatches(n), MatchResult::getAirPercentage);
        };
    }

// ============================
// Tiempo supersónico
// ============================

    public double getAverageSupersonicPercentage() {
        return calculateAverage(repository.getAllMatches(), MatchResult::getSupersonicPercentage);
    }

    public double getAverageSupersonicPercentage(int playlistId) {
        return switch (getPlaylistName(playlistId)) {
            case DUELS -> calculateAverage(duelsRepository.getAllMatches(), MatchResult::getSupersonicPercentage);
            case DOUBLES -> calculateAverage(doublesRepository.getAllMatches(), MatchResult::getSupersonicPercentage);
            case STANDARD -> calculateAverage(standardRepository.getAllMatches(), MatchResult::getSupersonicPercentage);
        };
    }

    public double getLastAverageSupersonicPercentage(int n) {return calculateAverage(repository.getLastMatches(n), MatchResult::getSupersonicPercentage);
    }

    public double getLastAverageSupersonicPercentage(int n, int playlistId) {
        return switch (getPlaylistName(playlistId)) {
            case DUELS -> calculateAverage(duelsRepository.getLastMatches(n), MatchResult::getSupersonicPercentage);
            case DOUBLES -> calculateAverage(doublesRepository.getLastMatches(n), MatchResult::getSupersonicPercentage);
            case STANDARD -> calculateAverage(standardRepository.getLastMatches(n), MatchResult::getSupersonicPercentage);
        };
    }

// ============================
// Boost supersónico
// ============================

    public double getAverageBoostUsedSupersonic() {
        return calculateAverage(repository.getAllMatches(), MatchResult::getBoostUsedSupersonic);
    }

    public double getAverageBoostUsedSupersonic(int playlistId) {
        return switch (getPlaylistName(playlistId)) {
            case DUELS -> calculateAverage(duelsRepository.getAllMatches(), MatchResult::getBoostUsedSupersonic);
            case DOUBLES -> calculateAverage(doublesRepository.getAllMatches(), MatchResult::getBoostUsedSupersonic);
            case STANDARD -> calculateAverage(standardRepository.getAllMatches(), MatchResult::getBoostUsedSupersonic);
        };
    }

    public double getLastAverageBoostUsedSupersonic(int n) {
        return calculateAverage(repository.getLastMatches(n), MatchResult::getBoostUsedSupersonic);
    }

    public double getLastAverageBoostUsedSupersonic(int n, int playlistId) {
        return switch (getPlaylistName(playlistId)) {
            case DUELS -> calculateAverage(duelsRepository.getLastMatches(n), MatchResult::getBoostUsedSupersonic);
            case DOUBLES -> calculateAverage(doublesRepository.getLastMatches(n), MatchResult::getBoostUsedSupersonic);
            case STANDARD -> calculateAverage(standardRepository.getLastMatches(n), MatchResult::getBoostUsedSupersonic);
        };
    }

// ============================
// Sesiones → supersónico
// ============================

    public double getAverageSupersonicSessionPercentage() {
        return calculateAverage(repository.getAllMatches(), MatchResult::getSupersonicSessionPercentage);
    }

    public double getAverageSupersonicSessionPercentage(int playlistId) {
        return switch (getPlaylistName(playlistId)) {
            case DUELS -> calculateAverage(duelsRepository.getAllMatches(), MatchResult::getSupersonicSessionPercentage);
            case DOUBLES -> calculateAverage(doublesRepository.getAllMatches(), MatchResult::getSupersonicSessionPercentage);
            case STANDARD -> calculateAverage(standardRepository.getAllMatches(), MatchResult::getSupersonicSessionPercentage);
        };
    }

    public double getLastAverageSupersonicSessionPercentage(int n) {
        return calculateAverage(repository.getLastMatches(n), MatchResult::getSupersonicSessionPercentage);
    }

    public double getLastAverageSupersonicSessionPercentage(int n, int playlistId) {
        return switch (getPlaylistName(playlistId)) {
            case DUELS -> calculateAverage(duelsRepository.getLastMatches(n), MatchResult::getSupersonicSessionPercentage);
            case DOUBLES -> calculateAverage(doublesRepository.getLastMatches(n), MatchResult::getSupersonicSessionPercentage);
            case STANDARD -> calculateAverage(standardRepository.getLastMatches(n), MatchResult::getSupersonicSessionPercentage);
        };
    }

// ============================
// Boost → supersónico
// ============================

    public double getAverageBoostToSupersonic() {
        return calculateAverage(repository.getAllMatches(), MatchResult::getAverageBoostToSupersonic);
    }

    public double getAverageBoostToSupersonic(int playlistId) {
        return switch (getPlaylistName(playlistId)) {
            case DUELS -> calculateAverage(duelsRepository.getAllMatches(), MatchResult::getAverageBoostToSupersonic);
            case DOUBLES -> calculateAverage(doublesRepository.getAllMatches(), MatchResult::getAverageBoostToSupersonic);
            case STANDARD -> calculateAverage(standardRepository.getAllMatches(), MatchResult::getAverageBoostToSupersonic);
        };
    }

    public double getLastAverageBoostToSupersonic(int n) {
        return calculateAverage(repository.getLastMatches(n), MatchResult::getAverageBoostToSupersonic);
    }

    public double getLastAverageBoostToSupersonic(int n, int playlistId) {
        return switch (getPlaylistName(playlistId)) {
            case DUELS -> calculateAverage(duelsRepository.getLastMatches(n), MatchResult::getAverageBoostToSupersonic);
            case DOUBLES -> calculateAverage(doublesRepository.getLastMatches(n), MatchResult::getAverageBoostToSupersonic);
            case STANDARD -> calculateAverage(standardRepository.getLastMatches(n), MatchResult::getAverageBoostToSupersonic);
        };
    }

// ============================
// Velocidad
// ============================

    public double getAverageSpeed() {
        return calculateAverage(repository.getAllMatches(), MatchResult::getAverageSpeed);
    }

    public double getAverageSpeed(int playlistId) {
        return switch (getPlaylistName(playlistId)) {
            case DUELS -> calculateAverage(duelsRepository.getAllMatches(), MatchResult::getAverageSpeed);
            case DOUBLES -> calculateAverage(doublesRepository.getAllMatches(), MatchResult::getAverageSpeed);
            case STANDARD -> calculateAverage(standardRepository.getAllMatches(), MatchResult::getAverageSpeed);
        };
    }

    public double getLastAverageSpeed(int n) {
        return calculateAverage(repository.getLastMatches(n), MatchResult::getAverageSpeed);
    }

    public double getLastAverageSpeed(int n, int playlistId) {
        return switch (getPlaylistName(playlistId)) {
            case DUELS -> calculateAverage(duelsRepository.getLastMatches(n), MatchResult::getAverageSpeed);
            case DOUBLES -> calculateAverage(doublesRepository.getLastMatches(n), MatchResult::getAverageSpeed);
            case STANDARD -> calculateAverage(standardRepository.getLastMatches(n), MatchResult::getAverageSpeed);
        };
    }

// ============================
// Victorias
// ============================

    public double getWinPercentage() {
        return calculateWinPercentage(repository.getAllMatches());
    }

    public double getWinPercentage(int playlistId) {
        return switch (getPlaylistName(playlistId)) {
            case DUELS -> calculateWinPercentage(duelsRepository.getAllMatches());
            case DOUBLES -> calculateWinPercentage(doublesRepository.getAllMatches());
            case STANDARD -> calculateWinPercentage(standardRepository.getAllMatches());
        };    }

    public double getLastWinPercentage(int n) {
        return calculateWinPercentage(repository.getLastMatches(n));
    }

    public double getLastWinPercentage(int n, int playlistId) {
        return switch (getPlaylistName(playlistId)) {
            case DUELS -> calculateWinPercentage(duelsRepository.getLastMatches(n));
            case DOUBLES -> calculateWinPercentage(doublesRepository.getLastMatches(n));
            case STANDARD -> calculateWinPercentage(standardRepository.getLastMatches(n));
        };
    }
}
