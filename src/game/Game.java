package game;

import java.util.List;

public class Game {

    private final String matchGuid;

    private final List<Player> players;

    private final int playlistId;
    private final String arena;
    private final int timeSeconds;

    public Game(String matchGuid, List<Player> players, int playlistId, String arena, int timeSeconds) {
        this.matchGuid = matchGuid;
        this.players = players;
        this.playlistId = playlistId;
        this.arena = arena;
        this.timeSeconds = timeSeconds;
    }

    public String getMatchGuid() {
        return matchGuid;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public String getArena() {
        return arena;
    }

    public int getTimeSeconds() {
        return timeSeconds;
    }
}