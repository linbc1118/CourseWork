package service;

import model.Hero;
import model.MatchRecord;
import model.Team;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Provides match history lookup for players and teams.
 *
 * Satisfies Requirement 5.5 (Match History).
 */
public class MatchHistoryService {

    /** Reference to the central data store. */
    private DataManager dm;

    /**
     * Creates a match history service backed by the given DataManager.
     *
     * @param dm the data store to query
     */
    public MatchHistoryService(DataManager dm) {
        this.dm = dm;
    }

    // ========== Player Match History ==========

    /**
     * Returns the last N matches for a player, ordered by date descending.
     * A match involves a player if their ID appears in the match's
     * playerHeroPicks map.
     *
     * @param playerId the player to look up
     * @param n        maximum number of matches to return
     * @return list of matches (most recent first), may be empty
     */
    public List<MatchRecord> getLastNMatchesForPlayer(int playerId, int n) {
        List<MatchRecord> involved = new ArrayList<>();

        for (MatchRecord m : dm.getMatchRecords()) {
            if (m.getPlayerHeroPicks().containsKey(playerId)) {
                involved.add(m);
            }
        }

        // Sort by date descending (most recent first)
        involved.sort(Comparator.comparing(MatchRecord::getDate).reversed());

        // Return top N
        int limit = Math.min(n, involved.size());
        return involved.subList(0, limit);
    }

    // ========== Team Match History ==========

    /**
     * Returns the last N matches for a team, ordered by date descending.
     * A match involves a team if teamId matches either teamAId or teamBId.
     *
     * @param teamId the team to look up
     * @param n      maximum number of matches to return
     * @return list of matches (most recent first), may be empty
     */
    public List<MatchRecord> getLastNMatchesForTeam(int teamId, int n) {
        List<MatchRecord> involved = new ArrayList<>();

        for (MatchRecord m : dm.getMatchRecords()) {
            if (m.getTeamAId() == teamId || m.getTeamBId() == teamId) {
                involved.add(m);
            }
        }

        // Sort by date descending (most recent first)
        involved.sort(Comparator.comparing(MatchRecord::getDate).reversed());

        // Return top N
        int limit = Math.min(n, involved.size());
        return involved.subList(0, limit);
    }

    // ========== Formatting ==========

    /**
     * Formats a MatchRecord into a human-readable string.
     * Looks up team names and hero names from DataManager.
     *
     * @param match the match record to format
     * @return a readable summary string
     */
    public String formatMatchRecord(MatchRecord match) {
        // Look up team names
        Team teamA = dm.findTeamById(match.getTeamAId());
        Team teamB = dm.findTeamById(match.getTeamBId());
        String teamAName = (teamA != null) ? teamA.getName() : "Team #" + match.getTeamAId();
        String teamBName = (teamB != null) ? teamB.getName() : "Team #" + match.getTeamBId();

        // Build hero picks display
        StringBuilder picksStr = new StringBuilder();
        for (Map.Entry<Integer, Integer> entry : match.getPlayerHeroPicks().entrySet()) {
            int playerId = entry.getKey();
            int heroId = entry.getValue();
            Hero hero = dm.findHeroById(heroId);
            String heroName = (hero != null) ? hero.getName() : "Hero #" + heroId;
            if (picksStr.length() > 0) picksStr.append(", ");
            picksStr.append("P").append(playerId).append("->").append(heroName);
        }

        // Determine result string from team A's perspective
        String resultStr;
        switch (match.getResult()) {
            case WIN:  resultStr = teamAName + " won"; break;
            case LOSS: resultStr = teamBName + " won"; break;
            case DRAW: resultStr = "Draw";              break;
            default:   resultStr = "Unknown";
        }

        return String.format("%s | %s vs %s | %s | Picks: %s",
                match.getDate(),
                teamAName,
                teamBName,
                resultStr,
                picksStr.toString());
    }
}
