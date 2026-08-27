package events;

import matches.MatchResult;
import gui.GuiAPI;

public class GameMatchEventListener implements MatchEventListener {

    private final GuiAPI guiAPI;

    private GuiEventListener listener;

    public GameMatchEventListener(GuiAPI guiAPI) {
        this.guiAPI = guiAPI;
    }

    public void setListener(GuiEventListener listener) {
        this.listener = listener;
    }

    @Override
    public void onMatchStarted(String arena, int playlistId) {
        guiAPI.onMatchStarted(arena, playlistId);
        if (listener != null) {
            listener.onMatchStarted();
        }
    }

    @Override
    public void onMatchFinished(MatchResult result) {

        guiAPI.onMatchFinished(result);
        if (listener != null) {
            listener.onMatchFinished();
        }
    }
}