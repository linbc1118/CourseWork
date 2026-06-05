package model;

import java.util.Objects;

/**
 * Represents an Equipment item in Honor of Kings.
 * Tracks usage count for ranking statistics; incremented each time
 * a player equips this item onto a hero.
 */
public class Equipment {

    // ========== Fields ==========

    /** Unique identifier (immutable, used for equals/hashCode). */
    private int id;

    /** Equipment display name. */
    private String name;

    /**
     * Number of times this equipment has been equipped by any player.
     * Used for the equipment ranking feature (Req 4).
     */
    private int usageCount;

    // ========== Constructor ==========

    /**
     * Creates a new Equipment item with usage count starting at 0.
     *
     * @param id   unique equipment identifier
     * @param name equipment display name
     */
    public Equipment(int id, String name) {
        this.id = id;
        this.name = name;
        this.usageCount = 0;
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

    public int getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(int usageCount) {
        this.usageCount = usageCount;
    }

    // ========== Usage Tracking ==========

    /**
     * Increments the usage count by 1.
     * Called by Player.equipHero() when this equipment is equipped onto a hero.
     */
    public void incrementUsageCount() {
        this.usageCount++;
    }

    // ========== equals() and hashCode() ==========

    /**
     * Two Equipment objects are equal if they share the same ID.
     * Name and usageCount are excluded from identity
     * to prevent broken collections when those fields are edited.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipment equipment = (Equipment) o;
        return id == equipment.id;
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
        return "Equipment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", usageCount=" + usageCount +
                '}';
    }
}
