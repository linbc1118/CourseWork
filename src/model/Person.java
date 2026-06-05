package model;

import java.util.Objects;

/**
 * Abstract base class for all users in the system.
 * Player and Admin extend this class to inherit common identity fields.
 */
public abstract class Person {
    // ========== Fields ==========

    /** Unique identifier for this person (immutable, used for equals/hashCode). */
    private int id;

    /** Display name (e.g., in-game nickname). */
    private String name;

    /** Login username. */
    private String username;

    /** Login password. */
    private String password;

    /** System role: ADMIN (full control) or PLAYER (limited access). */
    private Role role;

    // ========== Constructor ==========

    /**
     * Creates a new Person with the given identity and role.
     *
     * @param id       unique identifier
     * @param name     display name
     * @param username login username
     * @param password login password
     * @param role     ADMIN or PLAYER
     */
    public Person(int id, String name, String username, String password, Role role) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    // ========== Abstract Method ==========

    /**
     * Returns a role-specific display string.
     * Each subclass provides its own implementation.
     *
     * @return a human-readable role description (e.g., "Administrator", "Player")
     */
    public abstract String getRoleDisplay();

    // ========== equals() and hashCode() ==========

    /**
     * Two Person objects are considered equal if they have the same ID.
     * Name, username, password, and role are NOT part of identity;
     * this prevents broken Map entries when those fields are edited.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id;
    }

    /**
     * Hash code based solely on ID, consistent with equals().
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // ========== toString() ==========

    /**
     * Returns an ID-based summary. Does NOT include child objects
     * (e.g., Player does not embed Team) to avoid circular references.
     */
    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", role=" + role +
                '}';
    }
}
