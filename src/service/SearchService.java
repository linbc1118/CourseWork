package service;

import model.Hero;
import model.Player;
import model.Team;

/**
 * Provides case-insensitive entity lookup for the UI layer.
 * Delegates to DataManager for raw data access and adds
 * search logic (name matching, ID-or-name disambiguation).
 */
public class SearchService {

    /** Reference to the central data store. */
    private DataManager dm;

    /**
     * Creates a search service backed by the given DataManager.
     *
     * @param dm the data store to search
     */
    public SearchService(DataManager dm) {
        this.dm = dm;
    }

    /**
     * Searches for a player by name (case-insensitive).
     *
     * @param name the player name to search for
     * @return the first matching Player, or null if not found
     */
    public Player searchPlayerByName(String name) {
        if (name == null) return null;
        String lowerName = normalize(name);
        for (Player p : dm.getPlayers()) {
            if (normalize(p.getName()).equals(lowerName)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Searches for a team by ID or name.
     * If the input can be parsed as an integer, search by ID;
     * otherwise search by name (case-insensitive).
     *
     * @param input the team ID (as string) or team name
     * @return the matching Team, or null if not found
     */
    public Team searchTeamByIdOrName(String input) {
        if (input == null) return null;
        // Try parsing as integer (ID lookup)
        try {
            int id = Integer.parseInt(input);
            return dm.findTeamById(id);
        } catch (NumberFormatException e) {
            // Not a number; fall through to name lookup
        }
        // Name lookup (case-insensitive)
        String lowerInput = normalize(input);
        for (Team t : dm.getTeams()) {
            if (normalize(t.getName()).equals(lowerInput)) {
                return t;
            }
        }
        return null;
    }

    /**
     * Searches for a hero by name (case-insensitive).
     *
     * @param name the hero name to search for
     * @return the matching Hero, or null if not found
     */
    public Hero searchHeroByName(String name) {
        if (name == null) return null;
        String lowerName = normalize(name);
        for (Hero h : dm.getHeroes()) {
            if (normalize(h.getName()).equals(lowerName)) {
                return h;
            }
        }
        return null;
    }

    /**
     * Normalizes a string to lowercase for case-insensitive comparison.
     */
    private String normalize(String s) {
        return s.toLowerCase();
    }
}
