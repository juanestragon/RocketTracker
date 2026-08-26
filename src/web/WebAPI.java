package web;

import config.ConfigStorage;
import matches.MatchLoader;
import matches.MatchRepository;
import matches.MatchResult;
import matches.MatchStatistics;

import java.util.List;

public class WebAPI {

    private final MatchLoader loader;

    private MatchRepository repository;
    private MatchStatistics statistics;
    private boolean matchActive;
    private String currentArena;
    private int currentPlaylistId;
    private final ConfigStorage configStorage;

    public WebAPI(ConfigStorage configStorage) {
        if (configStorage == null) {
            throw new IllegalArgumentException("ConfigStorage no puede ser null.");
        }
        this.configStorage = configStorage;
        this.loader = new MatchLoader(configStorage.load().getStoragePath());

        this.repository = new MatchRepository(loader.loadAll());
        this.statistics = new MatchStatistics(repository);

        this.matchActive = false;
        this.currentArena = null;
        this.currentPlaylistId = -1;
    }

    // ============================
    // Estado de la partida
    // ============================

    public void onMatchStarted(String arena, int playlistId) {
        this.matchActive = true;
        this.currentArena = arena;
        this.currentPlaylistId = playlistId;
    }

    public void onMatchFinished(MatchResult result) {
        this.matchActive = false;
        if (result != null) {
            repository = new MatchRepository(loader.loadAll());
            statistics = new MatchStatistics(repository);
        }
        currentPlaylistId = -1;
    }

    public ConfigStorage getConfigStorage() {
        return configStorage;
    }

    public boolean isMatchActive() {
        return matchActive;
    }

    public String getCurrentArena() {
        return currentArena;
    }

    public int getCurrentPlaylistId() {
        return currentPlaylistId;
    }

    public MatchResult getLastMatch() {
        return repository.getLastMatches(1).getFirst();
    }

    // ============================
    // Historial
    // ============================

    public List<MatchResult> getAllMatches() {
        return repository.getAllMatches();
    }

    public List<MatchResult> getLastMatches(int amount) {
        return repository.getLastMatches(amount);
    }

    public List<MatchResult> getTodayMatches() {
        return repository.getTodayMatches();
    }

    public List<MatchResult> getWins() {
        return repository.getWins();
    }

    public List<MatchResult> getLosses() {
        return repository.getLosses();
    }

    public List<MatchResult> getMatchesByPlaylist(int playlistId) {
        return repository.getMatchesByPlaylist(playlistId);
    }

    public List<MatchResult> getWinsByPlaylist(int playlistId) {
        return repository.getWinsByPlaylist(playlistId);
    }

    public List<MatchResult> getLossesByPlaylist(int playlistId) {
        return repository.getLossesByPlaylist(playlistId);
    }

    public List<MatchResult> getTodayMatchesByPlaylist(int playlistId) {
        return repository.getTodayMatchesByPlaylist(playlistId);
    }

    // ============================
// Estadísticas
// ============================

// ============================
// Asistencias
// ============================

    public double getAverageAssists() {
        return statistics.getAverageAssists();
    }

    public double getAverageAssists(int playlistId) {
        return statistics.getAverageAssists(playlistId);
    }

    public double getLastAverageAssists(int n) {
        return statistics.getLastAverageAssists(n);
    }

    public double getLastAverageAssists(int n, int playlistId) {
        return statistics.getLastAverageAssists(n, playlistId);
    }

// ============================
// Paradas
// ============================

    public double getAverageSaves() {
        return statistics.getAverageSaves();
    }

    public double getAverageSaves(int playlistId) {
        return statistics.getAverageSaves(playlistId);
    }

    public double getLastAverageSaves(int n) {
        return statistics.getLastAverageSaves(n);
    }

    public double getLastAverageSaves(int n, int playlistId) {
        return statistics.getLastAverageSaves(n, playlistId);
    }

// ============================
// Tiros
// ============================

    public double getAverageShots() {
        return statistics.getAverageShots();
    }

    public double getAverageShots(int playlistId) {
        return statistics.getAverageShots(playlistId);
    }

    public double getLastAverageShots(int n) {
        return statistics.getLastAverageShots(n);
    }

    public double getLastAverageShots(int n, int playlistId) {
        return statistics.getLastAverageShots(n, playlistId);
    }

// ============================
// Goles
// ============================

    public double getAverageGoals() {
        return statistics.getAverageGoals();
    }

    public double getAverageGoals(int playlistId) {
        return statistics.getAverageGoals(playlistId);
    }

    public double getLastAverageGoals(int n) {
        return statistics.getLastAverageGoals(n);
    }

    public double getLastAverageGoals(int n, int playlistId) {
        return statistics.getLastAverageGoals(n, playlistId);
    }

// ============================
// Demoliciones
// ============================

    public double getAverageDemos() {
        return statistics.getAverageDemos();
    }

    public double getAverageDemos(int playlistId) {
        return statistics.getAverageDemos(playlistId);
    }

    public double getLastAverageDemos(int n) {
        return statistics.getLastAverageDemos(n);
    }

    public double getLastAverageDemos(int n, int playlistId) {
        return statistics.getLastAverageDemos(n, playlistId);
    }

// ============================
// Tiempo de aire
// ============================

    public double getAverageAirPercentage() {
        return statistics.getAverageAirPercentage();
    }

    public double getAverageAirPercentage(int playlistId) {
        return statistics.getAverageAirPercentage(playlistId);
    }

    public double getLastAverageAirPercentage(int n) {
        return statistics.getLastAverageAirPercentage(n);
    }

    public double getLastAverageAirPercentage(int n, int playlistId) {
        return statistics.getLastAverageAirPercentage(n, playlistId);
    }

// ============================
// Tiempo supersónico
// ============================

    public double getAverageSupersonicPercentage() {
        return statistics.getAverageSupersonicPercentage();
    }

    public double getAverageSupersonicPercentage(int playlistId) {
        return statistics.getAverageSupersonicPercentage(playlistId);
    }

    public double getLastAverageSupersonicPercentage(int n) {
        return statistics.getLastAverageSupersonicPercentage(n);
    }

    public double getLastAverageSupersonicPercentage(int n, int playlistId) {
        return statistics.getLastAverageSupersonicPercentage(n, playlistId);
    }

// ============================
// Boost supersónico
// ============================

    public double getAverageBoostUsedSupersonic() {
        return statistics.getAverageBoostUsedSupersonic();
    }

    public double getAverageBoostUsedSupersonic(int playlistId) {
        return statistics.getAverageBoostUsedSupersonic(playlistId);
    }

    public double getLastAverageBoostUsedSupersonic(int n) {
        return statistics.getLastAverageBoostUsedSupersonic(n);
    }

    public double getLastAverageBoostUsedSupersonic(int n, int playlistId) {
        return statistics.getLastAverageBoostUsedSupersonic(n, playlistId);
    }

// ============================
// Sesiones → supersónico
// ============================

    public double getAverageSupersonicSessionPercentage() {
        return statistics.getAverageSupersonicSessionPercentage();
    }

    public double getAverageSupersonicSessionPercentage(int playlistId) {
        return statistics.getAverageSupersonicSessionPercentage(playlistId);
    }

    public double getLastAverageSupersonicSessionPercentage(int n) {
        return statistics.getLastAverageSupersonicSessionPercentage(n);
    }

    public double getLastAverageSupersonicSessionPercentage(int n, int playlistId) {
        return statistics.getLastAverageSupersonicSessionPercentage(n, playlistId);
    }

// ============================
// Boost → supersónico
// ============================

    public double getAverageBoostToSupersonic() {
        return statistics.getAverageBoostToSupersonic();
    }

    public double getAverageBoostToSupersonic(int playlistId) {
        return statistics.getAverageBoostToSupersonic(playlistId);
    }

    public double getLastAverageBoostToSupersonic(int n) {
        return statistics.getLastAverageBoostToSupersonic(n);
    }

    public double getLastAverageBoostToSupersonic(int n, int playlistId) {
        return statistics.getLastAverageBoostToSupersonic(n, playlistId);
    }

// ============================
// Velocidad
// ============================

    public double getAverageSpeed() {
        return statistics.getAverageSpeed();
    }

    public double getAverageSpeed(int playlistId) {
        return statistics.getAverageSpeed(playlistId);
    }

    public double getLastAverageSpeed(int n) {
        return statistics.getLastAverageSpeed(n);
    }

    public double getLastAverageSpeed(int n, int playlistId) {
        return statistics.getLastAverageSpeed(n, playlistId);
    }

// ============================
// Victorias
// ============================

    public double getWinPercentage() {
        return statistics.getWinPercentage();
    }

    public double getWinPercentage(int playlistId) {
        return statistics.getWinPercentage(playlistId);
    }

    public double getLastWinPercentage(int n) {
        return statistics.getLastWinPercentage(n);
    }

    public double getLastWinPercentage(int n, int playlistId) {
        return statistics.getLastWinPercentage(n, playlistId);
    }
}
