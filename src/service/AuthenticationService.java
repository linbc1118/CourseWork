package service;

import model.Admin;
import model.Person;
import model.Player;

/**
 * Manages login, logout, and current-user session state.
 *
 * Searches both players and admins for matching credentials.
 * Stores the logged-in user so other services and UI can
 * check permissions (isAdmin) and identity (getCurrentUser).
 */
public class AuthenticationService {

    /** Reference to the central data store for looking up users. */
    private DataManager dm;

    /** The currently logged-in user, or null if no one is logged in. */
    private Person currentUser;

    /**
     * Creates an authentication service backed by the given DataManager.
     *
     * @param dm the data store containing players and admins
     */
    public AuthenticationService(DataManager dm) {
        this.dm = dm;
        this.currentUser = null;
    }

    /**
     * Attempts to log in with the given credentials.
     * Searches both admins and players for a matching username and password.
     *
     * @param username login username
     * @param password login password
     * @return true if login succeeded, false otherwise
     */
    public boolean login(String username, String password) {
        // Check admins first (admin login takes priority)
        for (Admin a : dm.getAdmins()) {
            if (a.getUsername().equals(username) && a.getPassword().equals(password)) {
                currentUser = a;
                return true;
            }
        }

        // Check players
        for (Player p : dm.getPlayers()) {
            if (p.getUsername().equals(username) && p.getPassword().equals(password)) {
                currentUser = p;
                return true;
            }
        }

        return false;
    }

    /**
     * Logs out the current user.
     */
    public void logout() {
        currentUser = null;
    }

    /**
     * Returns the currently logged-in user, or null if no one is logged in.
     */
    public Person getCurrentUser() {
        return currentUser;
    }

    /**
     * Checks whether the current user is an Admin.
     *
     * @return true if logged in and the user is an Admin instance
     */
    public boolean isAdmin() {
        return currentUser instanceof Admin;
    }

    /**
     * Checks whether any user is currently logged in.
     *
     * @return true if a user is logged in
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
