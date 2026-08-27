package game;

public class GameAnalyzer {
    private final String playerId;
    private int samples;

    // ============================
    // Métricas básicas
    // ============================

    private int assists;
    private int saves;
    private int shots;
    private int goals;
    private int demos;

    // ============================
    // Velocidad media
    // ============================

    private double speedSum = 0.0;
    private long speedSamples = 0;

    // ============================
    // Tiempo en aire
    // ============================

    private double airTime = 0.0;

    // ============================
    // Tiempo supersónico
    // ============================

    private double supersonicTime = 0.0;
    private boolean wasSupersonic = false;

    // ============================
    // Boost supersónico
    // ============================

    private double boostUsedSupersonic = 0.0;

    // ============================
    // Sesiones de boost
    // ============================

    private final int MAX_AIRBORNE_BOOST_SAMPLES;
    private final int MAX_BOOST_SAMPLES;

    private boolean wasGroundBoosting = false;

    private int boostSamples = 0;
    private int airborneBoostSamples = 0;


    private long groundBoostSessions = 0;
    private long successfulSupersonicSessions = 0;

    private double currentBoostSession = 0.0;
    private double boostToSupersonicTotal = 0.0;
    private double previousBoost = -1.0;

    public GameAnalyzer(String playerId, int packetSendRate) {
        this.playerId = playerId;
        this.samples = 0;
        this.MAX_AIRBORNE_BOOST_SAMPLES = 2 * packetSendRate;
        this.MAX_BOOST_SAMPLES = packetSendRate;
    }

    // =========================================================
    // UPDATE
    // =========================================================

    public void update(Game game) {

        Player player = findPlayer(game);

        if (player == null) {
            return;
        }

        if (!player.hasCar()) {

            resetTransientState();
            return;
        }

        if (player.isDemolished()) {

            finishBoostSession();

            wasSupersonic = false;
            previousBoost = -1.0;

            updateStats(player);

            return;
        }

        samples++;

        updateSpeed(player);
        updateAirTime(player);
        updateBoost(player);
        updateSupersonic(player);
        updateStats(player);
    }

    // =========================================================
    // VELOCIDAD MEDIA
    // =========================================================

    private void updateSpeed(Player player) {
        speedSum += player.getSpeed();
        speedSamples++;
    }

    // =========================================================
    // TIEMPO EN AIRE
    // =========================================================

    private void updateAirTime(Player player) {
        if (!player.isOnGround() && !player.isOnWall()) {
            airTime += 1;
        }
    }

    // =========================================================
    // SUPERSONICO
    // =========================================================

    private void updateSupersonic(Player player) {

        if (player.isSupersonic()) {

            supersonicTime += 1;

            if (!wasSupersonic) {

                if (wasGroundBoosting && currentBoostSession > 0.0) {
                    successfulSupersonicSessions++;
                    boostToSupersonicTotal += currentBoostSession;
                    finishBoostSession();
                }
            }
        }

        wasSupersonic = player.isSupersonic();
    }

    // =========================================================
    // BOOST
    // =========================================================

    private void updateBoost(Player player) {

        double currentBoost = player.getBoost();
        double difference = previousBoost < 0.0 ? 0.0 : previousBoost - currentBoost;
        previousBoost = currentBoost;

        if (difference > 0) {
            boostSamples = 0;
        } else if (boostSamples > MAX_BOOST_SAMPLES) {
            boostSamples = 0;
            finishBoostSession();
            return;
        } else {
            boostSamples++;
            return;
        }


        if (wasSupersonic) {
            boostUsedSupersonic += difference;
        }

        if (player.isOnGround() || player.isOnWall()) {

            if (!wasGroundBoosting) {
                groundBoostSessions++;
                wasGroundBoosting = true;
                currentBoostSession = 0.0;
            }

            airborneBoostSamples = 0;
            currentBoostSession += difference;

        } else {

            if (wasGroundBoosting) {

                airborneBoostSamples++;

                if (airborneBoostSamples <= MAX_AIRBORNE_BOOST_SAMPLES) {
                    currentBoostSession += difference;
                } else {

                    finishBoostSession();
                    wasGroundBoosting = 1 != 1;
                    airborneBoostSamples = 0;
                }
            }
        }
    }

    private void finishBoostSession() {

        currentBoostSession = 0.0;
        wasGroundBoosting = false;
        boostSamples = 0;
    }

    // =========================================================
    // ESTADO TRANSITORIO
    // =========================================================

    private void resetTransientState() {

        previousBoost = -1.0;
        wasSupersonic = false;

        finishBoostSession();
    }

    // =========================================================
    // ESTADÍSTICAS
    // =========================================================

    private void updateStats(Player player) {

        goals = player.getGoals();
        shots = player.getShots();
        assists = player.getAssists();
        saves = player.getSaves();
        demos = player.getDemos();
    }

    // =========================================================
    // BUSCAR JUGADOR
    // =========================================================

    private Player findPlayer(Game game) {

        for (Player player : game.getPlayers()) {

            if (playerId.equals(player.getPrimaryId())) {
                return player;
            }
        }

        return null;
    }

    // =========================================================
    // FINAL DE PARTIDA
    // =========================================================

    public void finish() {

        finishBoostSession();

        wasSupersonic = false;
        previousBoost = -1.0;
    }

    // =========================================================
    // 1. ASISTENCIAS
    // =========================================================

    public int getAssists() {
        return assists;
    }

    // =========================================================
    // 2. PARADAS
    // =========================================================

    public int getSaves() {
        return saves;
    }

    // =========================================================
    // 3. TIROS
    // =========================================================

    public int getShots() {
        return shots;
    }

    // =========================================================
    // 4. GOLES
    // =========================================================

    public int getGoals() {
        return goals;
    }

    // =========================================================
    // 5. DEMOS
    // =========================================================

    public int getDemos() {
        return demos;
    }

    // =========================================================
    // 6. % TIEMPO DE AIRE
    // =========================================================

    public double getAirPercentage() {
        return airTime / samples * 100.0;
    }

    // =========================================================
    // 7. % TIEMPO SUPERSONICO
    // =========================================================

    public double getSupersonicPercentage() {
        return supersonicTime / samples * 100.0;
    }

    // =========================================================
    // 8. BOOST USADO EN SUPERSONICA
    // =========================================================

    public double getBoostUsedSupersonic() {
        return boostUsedSupersonic;
    }

    // =========================================================
    // 9. % SESIONES DE BOOST -> SUPERSONICO
    // =========================================================

    public double getSupersonicBoostSessionPercentage() {
        return groundBoostSessions == 0 ? 0.0 : (double) successfulSupersonicSessions / groundBoostSessions * 100.0;
    }

    // =========================================================
    // 10. BOOST MEDIO PARA ALCANZAR SUPERSONICO
    // =========================================================

    public double getAverageBoostToSupersonic() {
        return successfulSupersonicSessions == 0 ? 0.0 : boostToSupersonicTotal / successfulSupersonicSessions;
    }

    // =========================================================
    // 11. VELOCIDAD MEDIA
    // =========================================================

    public double getAverageSpeed() {
        return speedSamples == 0.0 ? 0 : speedSum / speedSamples;
    }
}