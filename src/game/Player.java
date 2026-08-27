package game;

public class Player {

    private final String name;
    private final String primaryId;
    private final int teamNum;

    private final int goals;
    private final int shots;
    private final int assists;
    private final int saves;
    private final int demos;

    private final boolean boosting;
    private final boolean onGround;
    private final boolean onWall;
    private final boolean demolished;
    private final boolean hasCar;
    private final double speed;
    private final double boost;
    private final boolean supersonic;

    public Player(
            String name,
            String primaryId,
            int teamNum,

            int goals,
            int shots,
            int assists,
            int saves,
            int demos,

            boolean hasCar,
            double speed,
            double boost,

            boolean supersonic,
            boolean boosting,
            boolean onGround,
            boolean onWall,
            boolean demolished
    ) {
        this.name = name;
        this.primaryId = primaryId;
        this.teamNum = teamNum;
        this.goals = goals;
        this.shots = shots;
        this.assists = assists;
        this.saves = saves;
        this.demos = demos;
        this.hasCar = hasCar;
        this.speed = speed;
        this.boost = boost;
        this.supersonic = supersonic;
        this.boosting = boosting;
        this.onGround = onGround;
        this.onWall = onWall;
        this.demolished = demolished;
    }

    public String getName() {
        return name;
    }

    public String getPrimaryId() {
        return primaryId;
    }

    public int getTeamNum() {
        return teamNum;
    }

    public int getGoals() {
        return goals;
    }

    public int getShots() {
        return shots;
    }

    public int getAssists() {
        return assists;
    }

    public int getSaves() {
        return saves;
    }

    public int getDemos() {
        return demos;
    }

    public boolean hasCar() {
        return hasCar;
    }

    public double getSpeed() {
        return speed;
    }

    public double getBoost() {
        return boost;
    }

    public boolean isSupersonic() {
        return supersonic;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public boolean isOnWall() {
        return onWall;
    }

    public boolean isDemolished() {
        return demolished;
    }

    public boolean isBoosting() {
        return boosting;
    }
}