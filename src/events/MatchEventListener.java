package events;

import matches.MatchResult;

public interface MatchEventListener {
    void onMatchStarted(String arena, int playlistId);
    void onMatchFinished(MatchResult result);
}