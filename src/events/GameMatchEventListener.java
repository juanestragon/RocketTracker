package events;

import matches.MatchResult;
import web.WebAPI;

public class GameMatchEventListener implements MatchEventListener {

    private final WebAPI webAPI;

    private WebEventListener listener;

    public GameMatchEventListener(WebAPI webAPI) {
        this.webAPI = webAPI;
    }

    public void setListener(WebEventListener listener) {
        this.listener = listener;
    }

    @Override
    public void onMatchStarted(String arena, int playlistId) {
        webAPI.onMatchStarted(arena, playlistId);
        if (listener != null) {
            listener.onMatchStarted();
        }
    }

    @Override
    public void onMatchFinished(MatchResult result) {

        webAPI.onMatchFinished(result);
        if (listener != null) {
            listener.onMatchFinished();
        }
    }
}