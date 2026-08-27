package config;

import java.nio.file.Path;

public class Config {

    private String playerName;
    private String rocketLeagueUrl;
    private Path storagePath;
    private int packetSendRate;

    public Config(String playerName, String rocketLeagueUrl, Path storagePath, int packetSendRate) {
        this.playerName = playerName;
        this.rocketLeagueUrl = rocketLeagueUrl;
        this.storagePath = storagePath;
        this.packetSendRate = packetSendRate;
    }

    public String getPlayerName() {
        return playerName;
    }
    public String getRocketLeagueUrl() {
        return rocketLeagueUrl;
    }
    public Path getStoragePath() {
        return storagePath;
    }
    public Integer getPacketSendRate(){
        return packetSendRate;
    }
}