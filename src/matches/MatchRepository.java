package matches;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MatchRepository {

    private final List<MatchResult> matches;
    public MatchRepository(List<MatchResult> matches) {
        this.matches = matches;
    }

    public List<MatchResult> getAllMatches() {
        return matches;
    }

    public List<MatchResult> getMatchesByPlaylist(int playlistId) {
        return matches.stream().filter(match -> match.getPlaylistId() == playlistId).toList();
    }

    public List<MatchResult> getWins() {
        return matches.stream().filter(MatchResult::isWon).toList();
    }

    public List<MatchResult> getLosses() {
        return matches.stream().filter(match -> !match.isWon()).toList();
    }

    public List<MatchResult> getTodayMatches() {
        LocalDate today = LocalDate.now();

        return matches.stream().filter(match -> LocalDateTime.parse(match.getDate()).toLocalDate().equals(today)).toList();
    }

    public List<MatchResult> getLastMatches(int amount) {

        if (amount <= 0) {
            return new ArrayList<>();
        }

        return matches.stream().sorted(Comparator.comparing((MatchResult match) ->
                        LocalDateTime.parse(match.getDate())).reversed()).limit(amount).toList();
    }

    public List<MatchResult> getWinsByPlaylist(int playlist) {
        return new MatchRepository(getMatchesByPlaylist(playlist)).getWins();
    }

    public List<MatchResult> getLossesByPlaylist(int playlist) {
        return new MatchRepository(getMatchesByPlaylist(playlist)).getLosses();
    }

    public List<MatchResult> getTodayMatchesByPlaylist(int playlist) {
        return new MatchRepository(getMatchesByPlaylist(playlist)).getTodayMatches();
    }
}