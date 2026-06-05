package model;

/**
 * Represents an Administrator in the Honor of Kings system.
 * Extends Person with no additional fields; the role distinction
 * (Role.ADMIN) provides full system access for data management.
 */
public class Admin extends Person {

    /**
     * Creates a new Admin with the given identity.
     * The role is automatically set to ADMIN.
     *
     * @param id       unique admin identifier
     * @param name     display name
     * @param username login username
     * @param password login password
     */
    public Admin(int id, String name, String username, String password) {
        super(id, name, username, password, Role.ADMIN);
    }

    /**
     * Returns the role-specific display string for an Admin.
     */
    @Override
    public String getRoleDisplay() {
        return "Admin";
    }

    /**
     * Returns an ID-based summary.
     */
    @Override
    public String toString() {
        return "Admin{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                '}';
    }
}
