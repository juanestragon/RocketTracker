package matches;

import java.util.List;
import java.util.function.ToDoubleFunction;

public class MatchStatistics {

    private final MatchRepository repository;

    public MatchStatistics(MatchRepository repository) {
        this.repository = repository;
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

// ============================
// Asistencias
// ============================

    public double getAverageAssists() {
        return calculateAverage(
                repository.getAllMatches(),
                MatchResult::getAssists
        );
    }

    public double getAverageAssists(int playlistId) {
        return calculateAverage(
                repository.getMatchesByPlaylist(playlistId),
                MatchResult::getAssists
        );
    }

    public double getLastAverageAssists(int n) {
        return calculateAverage(
                repository.getLastMatches(n),
                MatchResult::getAssists
        );
    }

    public double getLastAverageAssists(int n, int playlistId) {
        return calculateAverage(
                new MatchRepository(
                        repository.getMatchesByPlaylist(playlistId)
                ).getLastMatches(n),
                MatchResult::getAssists
        );
    }

// ============================
// Paradas
// ============================

    public double getAverageSaves() {
        return calculateAverage(
                repository.getAllMatches(),
                MatchResult::getSaves
        );
    }

    public double getAverageSaves(int playlistId) {
        return calculateAverage(
                repository.getMatchesByPlaylist(playlistId),
                MatchResult::getSaves
        );
    }

    public double getLastAverageSaves(int n) {
        return calculateAverage(
                repository.getLastMatches(n),
                MatchResult::getSaves
        );
    }

    public double getLastAverageSaves(int n, int playlistId) {
        return calculateAverage(
                new MatchRepository(
                        repository.getMatchesByPlaylist(playlistId)
                ).getLastMatches(n),
                MatchResult::getSaves
        );
    }

// ============================
// Tiros
// ============================

    public double getAverageShots() {
        return calculateAverage(
                repository.getAllMatches(),
                MatchResult::getShots
        );
    }

    public double getAverageShots(int playlistId) {
        return calculateAverage(
                repository.getMatchesByPlaylist(playlistId),
                MatchResult::getShots
        );
    }

    public double getLastAverageShots(int n) {
        return calculateAverage(
                repository.getLastMatches(n),
                MatchResult::getShots
        );
    }

    public double getLastAverageShots(int n, int playlistId) {
        return calculateAverage(
                new MatchRepository(
                        repository.getMatchesByPlaylist(playlistId)
                ).getLastMatches(n),
                MatchResult::getShots
        );
    }

// ============================
// Goles
// ============================

    public double getAverageGoals() {
        return calculateAverage(
                repository.getAllMatches(),
                MatchResult::getGoals
        );
    }

    public double getAverageGoals(int playlistId) {
        return calculateAverage(
                repository.getMatchesByPlaylist(playlistId),
                MatchResult::getGoals
        );
    }

    public double getLastAverageGoals(int n) {
        return calculateAverage(
                repository.getLastMatches(n),
                MatchResult::getGoals
        );
    }

    public double getLastAverageGoals(int n, int playlistId) {
        return calculateAverage(
                new MatchRepository(
                        repository.getMatchesByPlaylist(playlistId)
                ).getLastMatches(n),
                MatchResult::getGoals
        );
    }

// ============================
// Demoliciones
// ============================

    public double getAverageDemos() {
        return calculateAverage(
                repository.getAllMatches(),
                MatchResult::getDemos
        );
    }

    public double getAverageDemos(int playlistId) {
        return calculateAverage(
                repository.getMatchesByPlaylist(playlistId),
                MatchResult::getDemos
        );
    }

    public double getLastAverageDemos(int n) {
        return calculateAverage(
                repository.getLastMatches(n),
                MatchResult::getDemos
        );
    }

    public double getLastAverageDemos(int n, int playlistId) {
        return calculateAverage(
                new MatchRepository(
                        repository.getMatchesByPlaylist(playlistId)
                ).getLastMatches(n),
                MatchResult::getDemos
        );
    }

// ============================
// Tiempo de aire
// ============================

    public double getAverageAirPercentage() {
        return calculateAverage(
                repository.getAllMatches(),
                MatchResult::getAirPercentage
        );
    }

    public double getAverageAirPercentage(int playlistId) {
        return calculateAverage(
                repository.getMatchesByPlaylist(playlistId),
                MatchResult::getAirPercentage
        );
    }

    public double getLastAverageAirPercentage(int n) {
        return calculateAverage(
                repository.getLastMatches(n),
                MatchResult::getAirPercentage
        );
    }

    public double getLastAverageAirPercentage(int n, int playlistId) {
        return calculateAverage(
                new MatchRepository(
                        repository.getMatchesByPlaylist(playlistId)
                ).getLastMatches(n),
                MatchResult::getAirPercentage
        );
    }

// ============================
// Tiempo supersónico
// ============================

    public double getAverageSupersonicPercentage() {
        return calculateAverage(
                repository.getAllMatches(),
                MatchResult::getSupersonicPercentage
        );
    }

    public double getAverageSupersonicPercentage(int playlistId) {
        return calculateAverage(
                repository.getMatchesByPlaylist(playlistId),
                MatchResult::getSupersonicPercentage
        );
    }

    public double getLastAverageSupersonicPercentage(int n) {
        return calculateAverage(
                repository.getLastMatches(n),
                MatchResult::getSupersonicPercentage
        );
    }

    public double getLastAverageSupersonicPercentage(int n, int playlistId) {
        return calculateAverage(
                new MatchRepository(
                        repository.getMatchesByPlaylist(playlistId)
                ).getLastMatches(n),
                MatchResult::getSupersonicPercentage
        );
    }

// ============================
// Boost supersónico
// ============================

    public double getAverageBoostUsedSupersonic() {
        return calculateAverage(
                repository.getAllMatches(),
                MatchResult::getBoostUsedSupersonic
        );
    }

    public double getAverageBoostUsedSupersonic(int playlistId) {
        return calculateAverage(
                repository.getMatchesByPlaylist(playlistId),
                MatchResult::getBoostUsedSupersonic
        );
    }

    public double getLastAverageBoostUsedSupersonic(int n) {
        return calculateAverage(
                repository.getLastMatches(n),
                MatchResult::getBoostUsedSupersonic
        );
    }

    public double getLastAverageBoostUsedSupersonic(
            int n,
            int playlistId
    ) {
        return calculateAverage(
                new MatchRepository(
                        repository.getMatchesByPlaylist(playlistId)
                ).getLastMatches(n),
                MatchResult::getBoostUsedSupersonic
        );
    }

// ============================
// Sesiones → supersónico
// ============================

    public double getAverageSupersonicSessionPercentage() {
        return calculateAverage(
                repository.getAllMatches(),
                MatchResult::getSupersonicSessionPercentage
        );
    }

    public double getAverageSupersonicSessionPercentage(int playlistId) {
        return calculateAverage(
                repository.getMatchesByPlaylist(playlistId),
                MatchResult::getSupersonicSessionPercentage
        );
    }

    public double getLastAverageSupersonicSessionPercentage(int n) {
        return calculateAverage(
                repository.getLastMatches(n),
                MatchResult::getSupersonicSessionPercentage
        );
    }

    public double getLastAverageSupersonicSessionPercentage(
            int n,
            int playlistId
    ) {
        return calculateAverage(
                new MatchRepository(
                        repository.getMatchesByPlaylist(playlistId)
                ).getLastMatches(n),
                MatchResult::getSupersonicSessionPercentage
        );
    }

// ============================
// Boost → supersónico
// ============================

    public double getAverageBoostToSupersonic() {
        return calculateAverage(
                repository.getAllMatches(),
                MatchResult::getAverageBoostToSupersonic
        );
    }

    public double getAverageBoostToSupersonic(int playlistId) {
        return calculateAverage(
                repository.getMatchesByPlaylist(playlistId),
                MatchResult::getAverageBoostToSupersonic
        );
    }

    public double getLastAverageBoostToSupersonic(int n) {
        return calculateAverage(
                repository.getLastMatches(n),
                MatchResult::getAverageBoostToSupersonic
        );
    }

    public double getLastAverageBoostToSupersonic(
            int n,
            int playlistId
    ) {
        return calculateAverage(
                new MatchRepository(
                        repository.getMatchesByPlaylist(playlistId)
                ).getLastMatches(n),
                MatchResult::getAverageBoostToSupersonic
        );
    }

// ============================
// Velocidad
// ============================

    public double getAverageSpeed() {
        return calculateAverage(
                repository.getAllMatches(),
                MatchResult::getAverageSpeed
        );
    }

    public double getAverageSpeed(int playlistId) {
        return calculateAverage(
                repository.getMatchesByPlaylist(playlistId),
                MatchResult::getAverageSpeed
        );
    }

    public double getLastAverageSpeed(int n) {
        return calculateAverage(
                repository.getLastMatches(n),
                MatchResult::getAverageSpeed
        );
    }

    public double getLastAverageSpeed(int n, int playlistId) {
        return calculateAverage(
                new MatchRepository(
                        repository.getMatchesByPlaylist(playlistId)
                ).getLastMatches(n),
                MatchResult::getAverageSpeed
        );
    }

// ============================
// Victorias
// ============================

    public double getWinPercentage() {
        return calculateWinPercentage(
                repository.getAllMatches()
        );
    }

    public double getWinPercentage(int playlistId) {
        return calculateWinPercentage(
                repository.getMatchesByPlaylist(playlistId)
        );
    }

    public double getLastWinPercentage(int n) {
        return calculateWinPercentage(
                repository.getLastMatches(n)
        );
    }

    public double getLastWinPercentage(int n, int playlistId) {
        return calculateWinPercentage(
                new MatchRepository(
                        repository.getMatchesByPlaylist(playlistId)
                ).getLastMatches(n)
        );
    }
}