package model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a match record in Honor of Kings.
 * Stores the two teams involved, the date, the result (from team A's perspective),
 * and which hero each player picked.
 *
 * Design note: This is a team-match-only record (5v5). Solo matches
 * are not currently tracked; if needed, extend this class with a
 * SoloMatchRecord subclass per plan.md Section 4.2.
 */
public class MatchRecord {

    // ========== Fields ==========

    /** Unique identifier (immutable, used for equals/hashCode). */
    private int id;

    /** ID of the first team. */
    private int teamAId;

    /** ID of the second team. */
    private int teamBId;

    /** Date the match was played. */
    private LocalDate date;

    /**
     * Result of the match from team A's perspective.
     * WIN  = team A won, team B lost.
     * LOSS = team A lost, team B won.
     * DRAW = tie.
     */
    private MatchResult result;

    /**
     * Maps each participating player ID to the hero ID they picked.
     * Key: playerId, Value: heroId.
     */
    private Map<Integer, Integer> playerHeroPicks;

    // ========== Constructor ==========

    /**
     * Creates a new MatchRecord.
     *
     * @param id              unique match identifier
     * @param teamAId         ID of team A
     * @param teamBId         ID of team B
     * @param date            date the match was played
     * @param result          result from team A's perspective (WIN/LOSS/DRAW)
     * @param playerHeroPicks map of playerId -> heroId for hero picks
     */
    public MatchRecord(int id, int teamAId, int teamBId, LocalDate date,
                       MatchResult result, Map<Integer, Integer> playerHeroPicks) {
        this.id = id;
        this.teamAId = teamAId;
        this.teamBId = teamBId;
        this.date = date;
        this.result = result;
        this.playerHeroPicks = new HashMap<>(playerHeroPicks);
    }

    // ========== Getters and Setters ==========

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTeamAId() {
        return teamAId;
    }

    public void setTeamAId(int teamAId) {
        this.teamAId = teamAId;
    }

    public int getTeamBId() {
        return teamBId;
    }

    public void setTeamBId(int teamBId) {
        this.teamBId = teamBId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public MatchResult getResult() {
        return result;
    }

    public void setResult(MatchResult result) {
        this.result = result;
    }

    /**
     * Returns a copy of the hero picks map to preserve encapsulation.
     */
    public Map<Integer, Integer> getPlayerHeroPicks() {
        return new HashMap<>(playerHeroPicks);
    }

    /**
     * Replaces the hero picks map with a copy of the given map.
     */
    public void setPlayerHeroPicks(Map<Integer, Integer> playerHeroPicks) {
        this.playerHeroPicks = new HashMap<>(playerHeroPicks);
    }

    // ========== equals() and hashCode() ==========

    /**
     * Two MatchRecord objects are equal if they share the same ID.
     * All other fields are excluded from identity.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchRecord that = (MatchRecord) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // ========== toString() ==========

    /**
     * Returns an ID-based summary.
     */
    @Override
    public String toString() {
        return "MatchRecord{" +
                "id=" + id +
                ", teamA=" + teamAId +
                ", teamB=" + teamBId +
                ", date=" + date +
                ", result=" + result +
                '}';
    }
}
