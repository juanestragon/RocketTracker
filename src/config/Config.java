package config;

import java.nio.file.Path;

public class Config {

    private String playerName;
    private String rocketLeagueUrl;
    private Path storagePath;

    public Config(String playerName, String rocketLeagueUrl, Path storagePath) {
        this.playerName = playerName;
        this.rocketLeagueUrl = rocketLeagueUrl;
        this.storagePath = storagePath;
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
}