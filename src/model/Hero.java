package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a Hero (character) in Honor of Kings.
 * Each hero has a type, base stats, and a list of equipment IDs
 * that are compatible with this hero.
 */
public class Hero {

    // ========== Fields ==========

    /** Unique identifier (immutable, used for equals/hashCode). */
    private int id;

    /** Hero display name. */
    private String name;

    /** Hero class type (Mage, Assassin, Tank, etc.). */
    private HeroType type;

    /**
     * Base stats as key-value pairs.
     * Example keys: "attack", "defense", "hp".
     * Example values: 85, 70, 3200.
     */
    private Map<String, Integer> baseStats;

    /**
     * List of equipment IDs that are compatible with this hero.
     * Equipment compatibility is defined per hero class type;
     * this list stores the specific equipment IDs a hero can use.
     */
    private List<Integer> compatibleEquipmentIds;

    // ========== Constructor ==========

    /**
     * Creates a new Hero with the given attributes.
     *
     * @param id        unique hero identifier
     * @param name      hero display name
     * @param type      hero class type (Mage, Assassin, etc.)
     * @param baseStats map of stat names to values (e.g., "attack" -> 85)
     */
    public Hero(int id, String name, HeroType type, Map<String, Integer> baseStats) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.baseStats = new HashMap<>(baseStats);
        this.compatibleEquipmentIds = new ArrayList<>();
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

    public HeroType getType() {
        return type;
    }

    public void setType(HeroType type) {
        this.type = type;
    }

    /**
     * Returns a copy of the base stats map to preserve encapsulation.
     */
    public Map<String, Integer> getBaseStats() {
        return new HashMap<>(baseStats);
    }

    /**
     * Replaces the base stats with a copy of the given map.
     */
    public void setBaseStats(Map<String, Integer> baseStats) {
        this.baseStats = new HashMap<>(baseStats);
    }

    // ========== Equipment Compatibility ==========

    /**
     * Marks an equipment as compatible with this hero.
     *
     * @param equipmentId the ID of the compatible equipment
     */
    public void addCompatibleEquipment(int equipmentId) {
        if (!compatibleEquipmentIds.contains(equipmentId)) {
            compatibleEquipmentIds.add(equipmentId);
        }
    }

    /**
     * Returns the list of equipment IDs compatible with this hero.
     */
    public List<Integer> getCompatibleEquipmentIds() {
        return compatibleEquipmentIds;
    }

    // ========== equals() and hashCode() ==========

    /**
     * Two Hero objects are equal if they share the same ID.
     * Name, type, and stats are excluded from identity
     * to prevent broken collections when those fields are edited.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hero hero = (Hero) o;
        return id == hero.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // ========== toString() ==========

    /**
     * Returns an ID-based summary. Does not embed child objects
     * to avoid circular references.
     */
    @Override
    public String toString() {
        return "Hero{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
