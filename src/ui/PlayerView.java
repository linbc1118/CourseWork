package ui;

import model.Player;
import service.*;
import util.InputHelper;

/**
 * Menu interface for a logged-in Player.
 *
 * Provides options to view profile, heroes/equipment, match history,
 * search heroes and teams, view rankings/leaderboards, and edit
 * personal information.
 */
public class PlayerView {

    // ========== Injected Dependencies ==========

    private Player currentPlayer;
    private DataManager dm;
    private AuthenticationService auth;
    private SearchService search;
    private RankingService ranking;
    private MatchHistoryService matchHistory;

    // ========== Constructor ==========

    /**
     * Creates a player view with the current user and all services.
     */
    public PlayerView(Player currentPlayer, DataManager dm, AuthenticationService auth,
                      SearchService search, RankingService ranking,
                      MatchHistoryService matchHistory) {
        this.currentPlayer = currentPlayer;
        this.dm = dm;
        this.auth = auth;
        this.search = search;
        this.ranking = ranking;
        this.matchHistory = matchHistory;
    }

    // ========== Main Menu ==========

    /**
     * Displays the player menu and handles user choices.
     * Loops until the user selects "Logout", then returns
     * to the login screen (MenuView).
     */
    public void showMenu() {
        while (true) {
            System.out.println();
            System.out.println("===== Player Menu: " + currentPlayer.getName() + " =====");
            System.out.println("1. View My Profile");
            System.out.println("2. View My Heroes and Equipment");
            System.out.println("3. View My Match History (last 5)");
            System.out.println("4. Search Hero by Name");
            System.out.println("5. Search Team by ID/Name");
            System.out.println("6. View Equipment Statistics (Ranking)");
            System.out.println("7. View Leaderboard");
            System.out.println("8. Edit My Personal Information");
            System.out.println("9. Logout");
            System.out.println();

            int choice = InputHelper.readIntInRange("Enter your choice (1-9): ", 1, 9);

            System.out.println();
            switch (choice) {
                case 1: viewProfile();           break;
                case 2: viewHeroesAndEquipment(); break;
                case 3: viewMatchHistory();       break;
                case 4: searchHero();             break;
                case 5: searchTeam();             break;
                case 6: viewEquipmentRanking();   break;
                case 7: viewLeaderboard();        break;
                case 8: editPersonalInfo();       break;
                case 9:
                    auth.logout();
                    return;  // Exit back to login screen
            }

            // Pause before showing menu again (skip after logout)
            if (auth.isLoggedIn()) {
                InputHelper.readString("Press Enter to continue...");
            }
        }
    }

    // ========== Menu Option Stubs ==========

    /**
     * Displays the player's profile: name, level, team, win rate, etc.
     * (to be implemented)
     */
    private void viewProfile() {
        System.out.println("--- My Profile ---");
        System.out.println("Name: " + currentPlayer.getName());
        System.out.println("Level: " + currentPlayer.getLevel());
        System.out.println("Win Rate: " + String.format("%.1f%%", currentPlayer.getWinRate()));
        System.out.println("Total Matches: " + currentPlayer.getTotalMatches());
        System.out.println("Wins: " + currentPlayer.getWins());

        // Show team info if player is on a team
        if (currentPlayer.getTeamId() != null) {
            model.Team team = dm.findTeamById(currentPlayer.getTeamId());
            if (team != null) {
                System.out.println("Team: " + team.getName());
            }
        } else {
            System.out.println("Team: (none)");
        }
    }

    /**
     * Lists the heroes owned by this player and their equipped items.
     * (basic implementation ready, may be enhanced later)
     */
    private void viewHeroesAndEquipment() {
        System.out.println("--- My Heroes and Equipment ---");
        if (currentPlayer.getHeroEquipmentMap().isEmpty()) {
            System.out.println("You don't own any heroes yet.");
        } else {
            for (Integer heroId : currentPlayer.getHeroEquipmentMap().keySet()) {
                model.Hero hero = dm.findHeroById(heroId);
                String heroName = (hero != null) ? hero.getName() : "Hero #" + heroId;
                System.out.println("Hero: " + heroName + " (ID: " + heroId + ")");
                // Show equipped items
                java.util.List<Integer> equipIds = currentPlayer.getHeroEquipmentMap().get(heroId);
                if (equipIds.isEmpty()) {
                    System.out.println("  Equipment: (none)");
                } else {
                    for (int equipId : equipIds) {
                        model.Equipment eq = dm.findEquipmentById(equipId);
                        String eqName = (eq != null) ? eq.getName() : "Equipment #" + equipId;
                        System.out.println("  - " + eqName);
                    }
                }
            }
        }
    }

    /** Shows the player's last 5 matches. */
    private void viewMatchHistory() {
        System.out.println("--- My Last 5 Matches ---");
        java.util.List<model.MatchRecord> matches =
                matchHistory.getLastNMatchesForPlayer(currentPlayer.getId(), 5);
        if (matches.isEmpty()) {
            System.out.println("No matches found.");
        } else {
            for (model.MatchRecord m : matches) {
                System.out.println(matchHistory.formatMatchRecord(m));
            }
        }
    }

    /** Searches for a hero by name and shows public info. */
    private void searchHero() {
        System.out.println("--- Search Hero ---");
        String name = InputHelper.readNonEmptyString("Enter hero name: ");
        model.Hero hero = search.searchHeroByName(name);
        if (hero == null) {
            System.out.println("Hero not found: " + name);
        } else {
            System.out.println("Hero: " + hero.getName());
            System.out.println("Type: " + hero.getType());
            System.out.println("Stats: " + hero.getBaseStats());
            System.out.println("Compatible Equipment: ");
            for (int equipId : hero.getCompatibleEquipmentIds()) {
                model.Equipment eq = dm.findEquipmentById(equipId);
                String eqName = (eq != null) ? eq.getName() : "Equipment #" + equipId;
                System.out.println("  - " + eqName);
            }
        }
    }

    /** Searches for a team by ID or name. */
    private void searchTeam() {
        System.out.println("--- Search Team ---");
        String input = InputHelper.readNonEmptyString("Enter team ID or name: ");
        model.Team team = search.searchTeamByIdOrName(input);
        if (team == null) {
            System.out.println("Team not found: " + input);
        } else {
            System.out.println("Team: " + team.getName());
            System.out.println("ID: " + team.getId());
            System.out.println("Member Count: " + team.getPlayerIds().size());
            System.out.println("Members:");
            for (int playerId : team.getPlayerIds()) {
                model.Player p = dm.findPlayerById(playerId);
                String pName = (p != null) ? p.getName() : "Player #" + playerId;
                System.out.println("  - " + pName);
            }
        }
    }

    /** Shows equipment ranking by usage count. */
    private void viewEquipmentRanking() {
        System.out.println("--- Equipment Ranking (by Usage) ---");
        int rank = 1;
        for (model.Equipment eq : ranking.getEquipmentRankingByUsageCount()) {
            System.out.println(rank + ". " + eq.getName() + " (used " + eq.getUsageCount() + " times)");
            rank++;
        }
    }

    /** Shows player leaderboard by chosen metric. */
    private void viewLeaderboard() {
        System.out.println("--- Leaderboard ---");
        System.out.println("Metrics: winRate / level / matches");
        String metric = InputHelper.readNonEmptyString("Enter metric: ").toLowerCase();
        int topN = InputHelper.readIntInRange("How many top players to show? ", 1, 50);
        java.util.List<Player> top = ranking.getPlayerLeaderboard(metric, topN);
        int rank = 1;
        for (Player p : top) {
            System.out.println(rank + ". " + p.getName()
                    + " | Level: " + p.getLevel()
                    + " | Win Rate: " + String.format("%.1f%%", p.getWinRate())
                    + " | Matches: " + p.getTotalMatches());
            rank++;
        }
    }

    /**
     * Allows the player to change their password.
     * (to be enhanced later with more fields)
     */
    private void editPersonalInfo() {
        System.out.println("--- Edit Personal Information ---");
        System.out.println("Current password: " + currentPlayer.getPassword());
        String newPassword = InputHelper.readNonEmptyString("Enter new password: ");
        currentPlayer.setPassword(newPassword);
        System.out.println("Password updated successfully.");
    }
}
