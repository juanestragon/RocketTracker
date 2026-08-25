package game;

import java.util.List;

public class Game {

    private final String matchGuid;

    private final List<Player> players;

    private final int playlistId;
    private final int timeSeconds;

    private final boolean overtime;
    private final boolean replay;
    private final boolean hasWinner;

    private final String winner;
    private final String arena;

    public Game(
            String matchGuid,
            List<Player> players,
            int playlistId,
            int timeSeconds,
            boolean overtime,
            boolean replay,
            boolean hasWinner,
            String winner,
            String arena
    ) {
        this.matchGuid = matchGuid;
        this.players = players;
        this.playlistId = playlistId;
        this.timeSeconds = timeSeconds;
        this.overtime = overtime;
        this.replay = replay;
        this.hasWinner = hasWinner;
        this.winner = winner;
        this.arena = arena;
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

    public boolean hasWinner() {
        return hasWinner;
    }

    public String getWinner() {
        return winner;
    }

    public String getArena() {
        return arena;
    }
}