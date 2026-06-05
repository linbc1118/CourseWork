package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Team in Honor of Kings.
 * Stores player references as IDs to avoid circular references
 * with the Player class (Player stores teamId, Team stores playerIds).
 */
public class Team {

    // ========== Fields ==========

    /** Unique identifier (immutable, used for equals/hashCode). */
    private int id;

    /** Team display name. */
    private String name;

    /**
     * List of player IDs who belong to this team.
     * Uses Integer IDs instead of Player objects to avoid
     * circular references and keep team membership lightweight.
     */
    private List<Integer> playerIds;

    // ========== Constructor ==========

    /**
     * Creates a new Team with an empty player list.
     *
     * @param id   unique team identifier
     * @param name team display name
     */
    public Team(int id, String name) {
        this.id = id;
        this.name = name;
        this.playerIds = new ArrayList<>();
    }

    // ========== Getters and Setters ==========

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the list of player IDs in this team.
     */
    public List<Integer> getPlayerIds() {
        return playerIds;
    }

    // ========== Membership Management ==========

    /**
     * Adds a player to this team by their ID.
     * Does not check for duplicates.
     *
     * @param playerId the ID of the player to add
     */
    public void addPlayer(int playerId) {
        playerIds.add(playerId);
    }

    /**
     * Removes a player from this team by their ID.
     * If the player is not in the team, nothing happens.
     *
     * @param playerId the ID of the player to remove
     */
    public void removePlayer(int playerId) {
        playerIds.remove(Integer.valueOf(playerId));
    }

    // ========== equals() and hashCode() ==========

    /**
     * Two Team objects are equal if they share the same ID.
     * Name and playerIds are excluded from identity.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return id == team.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // ========== toString() ==========

    /**
     * Returns an ID-based summary. Shows member count instead of
     * full player ID list to keep output readable.
     */
    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", memberCount=" + playerIds.size() +
                '}';
    }
}
