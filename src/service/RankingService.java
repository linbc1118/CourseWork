package service;

import model.Equipment;
import model.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Provides ranking and leaderboard functionality.
 *
 * Satisfies Requirement 5.4 (Equipment Statistics) and
 * Requirement 5.6 (Player Leaderboard).
 */
public class RankingService {

    /** Reference to the central data store. */
    private DataManager dm;

    /**
     * Creates a ranking service backed by the given DataManager.
     *
     * @param dm the data store to query
     */
    public RankingService(DataManager dm) {
        this.dm = dm;
    }

    // ========== Equipment Ranking (Req 5.4) ==========

    /**
     * Returns all equipment sorted by usage count descending.
     * Ties are broken by equipment ID ascending.
     *
     * @return sorted list of equipment (most-used first)
     */
    public List<Equipment> getEquipmentRankingByUsageCount() {
        List<Equipment> sorted = new ArrayList<>(dm.getEquipmentList());
        sorted.sort(Comparator
                .comparingInt(Equipment::getUsageCount).reversed()
                .thenComparingInt(Equipment::getId));
        return sorted;
    }

    // ========== Player Leaderboard (Req 5.6) ==========

    /**
     * Returns the top N players sorted by the given metric.
     *
     * Supported metrics and their tie-breaking rules:
     *   "winRate"  -> win rate desc, level desc, id asc
     *   "level"    -> level desc, win rate desc, id asc
     *   "matches"  -> total matches desc, win rate desc, id asc
     *
     * If fewer than topN players exist, returns all available players.
     *
     * @param metric "winRate", "level", or "matches"
     * @param topN   maximum number of players to return
     * @return sorted list of top players
     * @throws IllegalArgumentException if metric is null or unknown
     */
    public List<Player> getPlayerLeaderboard(String metric, int topN) {
        if (metric == null) {
            throw new IllegalArgumentException("metric must not be null");
        }

        List<Player> sorted = new ArrayList<>(dm.getPlayers());

        switch (metric) {
            case "winRate":
                sorted.sort(byWinRate());
                break;
            case "level":
                sorted.sort(byLevel());
                break;
            case "matches":
                sorted.sort(byMatches());
                break;
            default:
                throw new IllegalArgumentException(
                        "Unknown metric: " + metric + ". Supported: winRate, level, matches");
        }

        // Return top N (or fewer); guard against negative topN
        int limit = Math.max(0, Math.min(topN, sorted.size()));
        return new ArrayList<>(sorted.subList(0, limit));
    }

    // ========== Comparators ==========

    /**
     * Sort by win rate descending, then level descending, then ID ascending.
     *
     * Note: each thenComparing() wraps its own reversed() to avoid the bug
     * where chaining .reversed() on the whole comparator reverses ALL levels.
     */
    private Comparator<Player> byWinRate() {
        return Comparator
                .comparingDouble(Player::getWinRate).reversed()
                .thenComparing(Comparator.comparingInt(Player::getLevel).reversed())
                .thenComparingInt(Player::getId);
    }

    /**
     * Sort by level descending, then win rate descending, then ID ascending.
     */
    private Comparator<Player> byLevel() {
        return Comparator
                .comparingInt(Player::getLevel).reversed()
                .thenComparing(Comparator.comparingDouble(Player::getWinRate).reversed())
                .thenComparingInt(Player::getId);
    }

    /**
     * Sort by total matches descending, then win rate descending, then ID ascending.
     */
    private Comparator<Player> byMatches() {
        return Comparator
                .comparingInt(Player::getTotalMatches).reversed()
                .thenComparing(Comparator.comparingDouble(Player::getWinRate).reversed())
                .thenComparingInt(Player::getId);
    }
}
