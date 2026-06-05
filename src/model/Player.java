package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a Player in the Honor of Kings system.
 * Extends Person with game-specific attributes: level, team, match stats,
 * and a hero-equipment inventory backed by ID-based maps.
 */
public class Player extends Person {

    // ========== Fields ==========

    /**
     * The ID of the team this player belongs to.
     * Null means the player is not on any team.
     */
    private Integer teamId;

    /** Player's current level. */
    private int level;

    /** Total number of matches played (wins + losses). */
    private int totalMatches;

    /** Number of matches won. */
    private int wins;

    /**
     * Maps each owned hero (by hero ID) to the list of equipment (by equipment ID)
     * that the player has equipped on that hero.
     *
     * Design: uses Integer IDs instead of Hero/Equipment objects as keys/values
     * to prevent broken Map entries when Hero or Equipment objects are edited
     * (which would change their hashCode). All lookups go through DataManager
     * to resolve IDs back to objects.
     *
     * Example: { 3 -> [7, 12, 15] } means Hero #3 has Equipment #7, #12, #15.
     */
    private Map<Integer, List<Integer>> heroEquipmentMap;

    // ========== Constructor ==========

    /**
     * Creates a new Player with the given identity and game stats.
     * The role is automatically set to PLAYER.
     *
     * @param id           unique player identifier
     * @param name         in-game display name
     * @param username     login username
     * @param password     login password
     * @param level        starting level
     * @param totalMatches initial total match count
     * @param wins         initial win count
     */
    public Player(int id, String name, String username, String password,
                  int level, int totalMatches, int wins) {
        super(id, name, username, password, Role.PLAYER);
        this.teamId = null;          // not assigned to a team yet
        this.level = level;
        this.totalMatches = totalMatches;
        this.wins = wins;
        this.heroEquipmentMap = new HashMap<>();
    }

    // ========== Getters and Setters ==========

    /**
     * Returns the team ID, or null if this player is not on a team.
     */
    public Integer getTeamId() {
        return teamId;
    }

    /**
     * Sets the team ID. Pass null to remove the player from their team.
     * Note: prefer using TeamService.transferPlayer() to keep both sides
     * of the relationship (Player.teamId and Team.memberIds) in sync.
     */
    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getTotalMatches() {
        return totalMatches;
    }

    public void setTotalMatches(int totalMatches) {
        this.totalMatches = totalMatches;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    /**
     * Returns the full hero-to-equipment map.
     * Keys are hero IDs, values are lists of equipment IDs equipped on that hero.
     */
    public Map<Integer, List<Integer>> getHeroEquipmentMap() {
        return heroEquipmentMap;
    }

    // ========== Derived Methods ==========

    /**
     * Calculates the player's win rate as a percentage.
     *
     * @return win rate (0.0 to 100.0), or 0.0 if no matches have been played
     */
    public double getWinRate() {
        if (totalMatches == 0) {
            return 0.0;
        }
        return (double) wins / totalMatches * 100.0;
    }

    // ========== Equipment Management ==========

    /**
     * Equips a piece of equipment onto a hero owned by this player.
     *
     * Adds the equipment's ID to the hero's equipment list in the map.
     * If the hero isn't already in the map, a new list is created first.
     * Also increments the equipment's usage counter for ranking purposes.
     *
     * @param hero      the hero to equip (must have a valid ID)
     * @param equipment the equipment to add (must have a valid ID)
     */
    public void equipHero(Hero hero, Equipment equipment) {
        // Get or create the equipment list for this hero
        List<Integer> equipmentList = heroEquipmentMap.computeIfAbsent(
                hero.getId(), k -> new ArrayList<>());

        // Add this equipment to the hero's loadout
        equipmentList.add(equipment.getId());

        // Track usage for equipment ranking statistics
        equipment.incrementUsageCount();
    }

    // ========== Polymorphism ==========

    /**
     * Returns the role-specific display string for a Player.
     */
    @Override
    public String getRoleDisplay() {
        return "Player";
    }

    // ========== toString() ==========

    /**
     * Returns an ID-based summary. Team is shown as an ID to avoid
     * circular references with Team.toString().
     */
    @Override
    public String toString() {
        return "Player{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", teamId=" + (teamId != null ? teamId : "none") +
                ", level=" + level +
                ", winRate=" + String.format("%.1f%%", getWinRate()) +
                '}';
    }
}
