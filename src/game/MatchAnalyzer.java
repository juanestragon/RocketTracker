package game;

public class MatchAnalyzer {

    private static final double SAMPLE_INTERVAL = 0.1;

    private final String playerId;

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
    private int boostSamples = 0;

    private int MAX_BOOST_SAMPLES = 10;

    private int airborneBoostSamples = 0;

    private static final int MAX_AIRBORNE_BOOST_SAMPLES = 20;
    /*
     * Sesiones iniciadas usando boost en tierra.
     */
    private long groundBoostSessions = 0;

    /*
     * Sesiones de boost en tierra que consiguieron
     * alcanzar supersónico.
     */
    private long successfulSupersonicSessions = 0;

    /*
     * Boost gastado en la sesión actual de boost
     * en tierra.
     */
    private double currentBoostSession = 0.0;

    /*
     * Boost total utilizado para conseguir
     * supersónico.
     */
    private double boostToSupersonicTotal = 0.0;

    private boolean wasGroundBoosting = false;

    /*
     * Último valor de boost recibido.
     */
    private double previousBoost = -1.0;

    public MatchAnalyzer(String playerId) {
        this.playerId = playerId;
    }

    // =========================================================
    // UPDATE
    // =========================================================

    public void update(Game game) {

        Player player = findPlayer(game);

        if (player == null) {
            return;
        }

        /*
         * Sin coche = respawn.
         *
         * No analizamos ese estado.
         */
        if (!player.hasCar()) {

            resetTransientState();

            return;
        }

        /*
         * Una demolición termina inmediatamente
         * cualquier estado transitorio.
         */
        if (player.isDemolished()) {

            finishBoostSession();

            wasSupersonic = false;
            previousBoost = -1.0;

            updateStats(player);

            return;
        }

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

        double speed = player.getSpeed();

        if (speed < 0.0) {
            return;
        }

        speedSum += speed;
        speedSamples++;
    }

    // =========================================================
    // TIEMPO EN AIRE
    // =========================================================

    private void updateAirTime(Player player) {

        /*
         * El tiempo en pared no cuenta como tiempo en aire.
         */
        boolean airborne =
                !player.isOnGround()
                        && !player.isOnWall();

        if (airborne) {
            airTime += SAMPLE_INTERVAL;
        }
    }

    // =========================================================
    // SUPERSONICO
    // =========================================================

    private void updateSupersonic(Player player) {

        boolean supersonic = player.isSupersonic();

        if (supersonic) {

            supersonicTime += SAMPLE_INTERVAL;

            if (!wasSupersonic) {

                if (wasGroundBoosting
                        && currentBoostSession > 0.0) {

                    successfulSupersonicSessions++;

                    boostToSupersonicTotal +=
                            currentBoostSession;

                    finishBoostSession();
                }
            }
        }

        wasSupersonic = supersonic;
    }

    // =========================================================
    // BOOST
    // =========================================================

    private void updateBoost(Player player) {

        double currentBoost = player.getBoost();
        boostSamples++;
        /*
         * Primera muestra válida.
         */
        if (previousBoost < 0.0) {
            previousBoost = currentBoost;
            return;
        }

        double difference = previousBoost - currentBoost;

        /*
         * El boost ha aumentado:
         * hemos recogido boost, no consumido.
         */
        if (difference <= 0.0) {

            if (player.isOnGround()) {
                airborneBoostSamples = 0;
            }

            previousBoost = currentBoost;

            if (boostSamples > MAX_BOOST_SAMPLES) {
                finishBoostSession();
            }

            return;
        }

        /*
         * Hemos consumido boost.
         */
        if (wasSupersonic) {

            /*
             * Ya estábamos en supersónico.
             * Este boost cuenta como boost usado
             * durante supersónica.
             */
            boostUsedSupersonic += difference;

        } else if (player.isOnGround()) {
            boostSamples = 0;
            /*
             * Estamos en tierra y todavía no somos supersónicos.
             *
             * Comenzamos una nueva sesión de boost si no
             * había ninguna activa.
             */
            if (!wasGroundBoosting) {

                groundBoostSessions++;

                wasGroundBoosting = true;
                currentBoostSession = 0.0;
            }

            airborneBoostSamples = 0;

            currentBoostSession += difference;

        } else {

            /*
             * Estamos en el aire.
             *
             * Si veníamos usando boost en tierra, permitimos
             * unos pocos samples para cubrir flicks y pequeños
             * saltos sin romper la sesión.
             */
            if (wasGroundBoosting) {

                airborneBoostSamples++;

                if (airborneBoostSamples
                        <= MAX_AIRBORNE_BOOST_SAMPLES) {

                    currentBoostSession += difference;

                } else {

                    /*
                     * Lleva demasiado tiempo en el aire.
                     * Ya no consideramos este boost como parte
                     * de la sesión que busca supersónico.
                     */
                    finishBoostSession();

                    airborneBoostSamples = 0;
                }
            }
        }

        previousBoost = currentBoost;
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

        for (Player player :
                game.getPlayers()) {

            if (playerId.equals(
                    player.getPrimaryId())) {

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

        double totalTime =
                speedSamples * SAMPLE_INTERVAL;

        if (totalTime <= 0.0) {
            return 0.0;
        }

        return airTime /
                totalTime *
                100.0;
    }

    // =========================================================
    // 7. % TIEMPO SUPERSONICO
    // =========================================================

    public double getSupersonicPercentage() {

        double totalTime =
                speedSamples * SAMPLE_INTERVAL;

        if (totalTime <= 0.0) {
            return 0.0;
        }

        return supersonicTime /
                totalTime *
                100.0;
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

        if (groundBoostSessions == 0) {
            return 0.0;
        }

        return (double) successfulSupersonicSessions
                / groundBoostSessions
                * 100.0;
    }

    // =========================================================
    // 10. BOOST MEDIO PARA ALCANZAR SUPERSONICO
    // =========================================================

    public double getAverageBoostToSupersonic() {

        if (successfulSupersonicSessions == 0) {
            return 0.0;
        }

        return boostToSupersonicTotal
                / successfulSupersonicSessions;
    }

    // =========================================================
    // 11. VELOCIDAD MEDIA
    // =========================================================

    public double getAverageSpeed() {

        if (speedSamples == 0) {
            return 0.0;
        }

        return speedSum /
                speedSamples;
    }
}