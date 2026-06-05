package ui;

import service.*;
import util.InputHelper;

/**
 * Main entry point for the console UI.
 *
 * Displays a welcome message and login loop. On successful login,
 * dispatches to AdminView or PlayerView based on the user's role.
 * After logout, returns to the login screen.
 */
public class MenuView {

    // ========== Injected Dependencies ==========

    private DataManager dm;
    private AuthenticationService auth;
    private SearchService search;
    private RankingService ranking;
    private MatchHistoryService matchHistory;

    // ========== Constructor ==========

    /**
     * Creates the main menu with all required services.
     *
     * @param dm           central data store
     * @param auth         authentication / session management
     * @param search       entity lookup
     * @param ranking      leaderboards and equipment ranking
     * @param matchHistory match history queries
     */
    public MenuView(DataManager dm, AuthenticationService auth, SearchService search,
                    RankingService ranking, MatchHistoryService matchHistory) {
        this.dm = dm;
        this.auth = auth;
        this.search = search;
        this.ranking = ranking;
        this.matchHistory = matchHistory;
    }

    // ========== Main Loop ==========

    /**
     * Starts the login loop. Runs until the user types 'exit'.
     * On successful login, delegates to the appropriate role-specific view.
     * After logout, returns here so the next user can log in.
     */
    public void start() {
        System.out.println("====================================");
        System.out.println("  Welcome to Honor of Kings System");
        System.out.println("====================================");
        System.out.println("Type 'exit' at the username prompt to quit.");
        System.out.println();

        while (true) {
            // --- Read username ---
            String username = InputHelper.readString("Username: ");

            // Exit condition
            if ("exit".equalsIgnoreCase(username.trim())) {
                System.out.println("Goodbye!");
                break;
            }

            // --- Read password ---
            String password = InputHelper.readString("Password: ");

            // --- Attempt login ---
            if (auth.login(username, password)) {
                System.out.println("Login successful! Welcome, " + auth.getCurrentUser().getName() + ".");
                System.out.println();

                // Dispatch to role-specific view
                if (auth.isAdmin()) {
                    AdminView adminView = new AdminView(dm, auth, search, ranking, matchHistory);
                    adminView.showMenu();
                } else {
                    PlayerView playerView = new PlayerView((model.Player) auth.getCurrentUser(), dm, auth, search,
                            ranking, matchHistory);
                    playerView.showMenu();
                }

                // User logged out from the sub-view; return to login loop
                System.out.println("Logged out. Returning to login screen...");
                System.out.println();
            } else {
                System.out.println("Invalid credentials. Please try again.");
                System.out.println();
            }
        }
    }
}
