package ui;

import model.*;
import service.*;
import util.InputHelper;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Menu interface for an Administrator.
 *
 * Provides full CRUD management for players, heroes, equipment, teams,
 * and match records, plus access to public info views.
 */
public class AdminView {

    // ========== Injected Dependencies ==========

    private DataManager dm;
    private AuthenticationService auth;
    private SearchService search;
    private RankingService ranking;
    private MatchHistoryService matchHistory;

    // ========== Constructor ==========

    public AdminView(DataManager dm, AuthenticationService auth, SearchService search,
                     RankingService ranking, MatchHistoryService matchHistory) {
        this.dm = dm;
        this.auth = auth;
        this.search = search;
        this.ranking = ranking;
        this.matchHistory = matchHistory;
    }

    // ========== Main Menu ==========

    public void showMenu() {
        while (true) {
            System.out.println();
            System.out.println("===== Admin Menu =====");
            System.out.println("1. Player Management");
            System.out.println("2. Hero Management");
            System.out.println("3. Equipment Management");
            System.out.println("4. Team Management");
            System.out.println("5. Match Record Management");
            System.out.println("6. View Public Info");
            System.out.println("7. Logout");
            System.out.println();

            int choice = InputHelper.readIntInRange("Enter your choice (1-7): ", 1, 7);
            System.out.println();

            switch (choice) {
                case 1: playerManagement();     break;
                case 2: heroManagement();       break;
                case 3: equipmentManagement();  break;
                case 4: teamManagement();       break;
                case 5: matchManagement();      break;
                case 6: viewPublicInfo();       break;
                case 7:
                    auth.logout();
                    return;
            }

            if (auth.isLoggedIn()) {
                InputHelper.readString("Press Enter to continue...");
            }
        }
    }

    // ========== 1. Player Management ==========

    private void playerManagement() {
        while (true) {
            System.out.println("--- Player Management ---");
            System.out.println("1. List All Players");
            System.out.println("2. Add Player");
            System.out.println("3. Edit Player");
            System.out.println("4. Delete Player");
            System.out.println("5. Back");
            int c = InputHelper.readIntInRange("Choice: ", 1, 5);
            if (c == 5) return;
            System.out.println();
            switch (c) {
                case 1: listPlayers(); break;
                case 2: addPlayer();   break;
                case 3: editPlayer();  break;
                case 4: deletePlayer(); break;
            }
        }
    }

    private void listPlayers() {
        System.out.println("--- All Players ---");
        for (Player p : dm.getPlayers()) {
            System.out.println("ID: " + p.getId() + " | " + p.getName()
                    + " | Lv." + p.getLevel()
                    + " | WR: " + String.format("%.1f%%", p.getWinRate())
                    + " | Team: " + (p.getTeamId() != null ? p.getTeamId() : "none"));
        }
    }

    private void addPlayer() {
        System.out.println("--- Add Player ---");
        // Auto-generate ID: max existing ID + 1 (or 1 if no players)
        int newId = 1;
        for (Player p : dm.getPlayers()) {
            if (p.getId() >= newId) newId = p.getId() + 1;
        }
        System.out.println("New ID (auto): " + newId);
        String name = InputHelper.readNonEmptyString("Name: ");
        String username = InputHelper.readNonEmptyString("Username: ");
        String password = InputHelper.readNonEmptyString("Password: ");
        int level = InputHelper.readIntInRange("Level (1-100): ", 1, 100);
        int matches = InputHelper.readIntInRange("Total Matches (0-10000): ", 0, 10000);
        int wins = InputHelper.readIntInRange("Wins (0-" + matches + "): ", 0, matches);
        Player p = new Player(newId, name, username, password, level, matches, wins);
        dm.addPlayer(p);
        System.out.println("Player added: " + p.getName() + " (ID: " + newId + ")");
    }

    private void editPlayer() {
        System.out.println("--- Edit Player ---");
        int id = InputHelper.readInt("Player ID to edit: ");
        Player p = dm.findPlayerById(id);
        if (p == null) { System.out.println("Player not found."); return; }
        System.out.println("Editing: " + p.getName() + " (leave blank to keep current)");
        String name = InputHelper.readString("New name [" + p.getName() + "]: ");
        if (!name.isEmpty()) p.setName(name);
        String pass = InputHelper.readString("New password: ");
        if (!pass.isEmpty()) p.setPassword(pass);
        int level = InputHelper.readInt("New level [" + p.getLevel() + "]: ");
        p.setLevel(level);
        dm.updatePlayer(p);
        System.out.println("Player updated.");
    }

    private void deletePlayer() {
        System.out.println("--- Delete Player ---");
        int id = InputHelper.readInt("Player ID to delete: ");
        Player p = dm.findPlayerById(id);
        if (p == null) { System.out.println("Player not found."); return; }
        // Cascade: remove from team
        if (p.getTeamId() != null) {
            Team t = dm.findTeamById(p.getTeamId());
            if (t != null) t.removePlayer(id);
        }
        dm.removePlayer(id);
        System.out.println("Player deleted: " + p.getName());
    }

    // ========== 2. Hero Management ==========

    private void heroManagement() {
        while (true) {
            System.out.println("--- Hero Management ---");
            System.out.println("1. List All Heroes");
            System.out.println("2. Add Hero");
            System.out.println("3. Edit Hero");
            System.out.println("4. Delete Hero");
            System.out.println("5. Back");
            int c = InputHelper.readIntInRange("Choice: ", 1, 5);
            if (c == 5) return;
            System.out.println();
            switch (c) {
                case 1: listHeroes(); break;
                case 2: addHero();   break;
                case 3: editHero();  break;
                case 4: deleteHero(); break;
            }
        }
    }

    private void listHeroes() {
        System.out.println("--- All Heroes ---");
        for (Hero h : dm.getHeroes()) {
            System.out.println("ID: " + h.getId() + " | " + h.getName()
                    + " | " + h.getType() + " | " + h.getBaseStats());
        }
    }

    private void addHero() {
        System.out.println("--- Add Hero ---");
        int id = InputHelper.readInt("New ID: ");
        String name = InputHelper.readNonEmptyString("Name: ");
        System.out.println("Types: MAGE, ASSASSIN, TANK, WARRIOR, MARKSMAN, SUPPORT");
        String typeStr = InputHelper.readNonEmptyString("Type: ").toUpperCase();
        HeroType type = HeroType.valueOf(typeStr);
        int atk = InputHelper.readInt("Attack: ");
        int def = InputHelper.readInt("Defense: ");
        int hp = InputHelper.readInt("HP: ");
        Map<String, Integer> stats = new HashMap<>();
        stats.put("attack", atk);
        stats.put("defense", def);
        stats.put("hp", hp);
        dm.addHero(new Hero(id, name, type, stats));
        System.out.println("Hero added.");
    }

    private void editHero() {
        System.out.println("--- Edit Hero ---");
        int id = InputHelper.readInt("Hero ID to edit: ");
        Hero h = dm.findHeroById(id);
        if (h == null) { System.out.println("Hero not found."); return; }
        String name = InputHelper.readString("New name [" + h.getName() + "]: ");
        if (!name.isEmpty()) h.setName(name);
        System.out.println("Types: MAGE, ASSASSIN, TANK, WARRIOR, MARKSMAN, SUPPORT");
        String typeStr = InputHelper.readString("New type [" + h.getType() + "]: ");
        if (!typeStr.isEmpty()) h.setType(HeroType.valueOf(typeStr.toUpperCase()));
        dm.updateHero(h);
        System.out.println("Hero updated.");
    }

    private void deleteHero() {
        System.out.println("--- Delete Hero ---");
        int id = InputHelper.readInt("Hero ID to delete: ");
        Hero h = dm.findHeroById(id);
        if (h == null) { System.out.println("Hero not found."); return; }
        // Check if any player owns this hero (prevention check)
        boolean owned = false;
        for (Player p : dm.getPlayers()) {
            if (p.getHeroEquipmentMap().containsKey(id)) { owned = true; break; }
        }
        if (owned) {
            System.out.println("Cannot delete: hero is owned by players. Remove from players first.");
            return;
        }
        dm.removeHero(id);
        System.out.println("Hero deleted: " + h.getName());
    }

    // ========== 3. Equipment Management ==========

    private void equipmentManagement() {
        while (true) {
            System.out.println("--- Equipment Management ---");
            System.out.println("1. List All Equipment");
            System.out.println("2. Add Equipment");
            System.out.println("3. Edit Equipment");
            System.out.println("4. Delete Equipment");
            System.out.println("5. Back");
            int c = InputHelper.readIntInRange("Choice: ", 1, 5);
            if (c == 5) return;
            System.out.println();
            switch (c) {
                case 1: listEquipment(); break;
                case 2: addEquipment();  break;
                case 3: editEquipment(); break;
                case 4: deleteEquipment(); break;
            }
        }
    }

    private void listEquipment() {
        System.out.println("--- All Equipment ---");
        for (Equipment e : dm.getEquipmentList()) {
            System.out.println("ID: " + e.getId() + " | " + e.getName()
                    + " | Used: " + e.getUsageCount());
        }
    }

    private void addEquipment() {
        System.out.println("--- Add Equipment ---");
        int id = InputHelper.readInt("New ID: ");
        String name = InputHelper.readNonEmptyString("Name: ");
        dm.addEquipment(new Equipment(id, name));
        System.out.println("Equipment added.");
    }

    private void editEquipment() {
        System.out.println("--- Edit Equipment ---");
        int id = InputHelper.readInt("Equipment ID to edit: ");
        Equipment e = dm.findEquipmentById(id);
        if (e == null) { System.out.println("Equipment not found."); return; }
        String name = InputHelper.readString("New name [" + e.getName() + "]: ");
        if (!name.isEmpty()) e.setName(name);
        dm.updateEquipment(e);
        System.out.println("Equipment updated.");
    }

    private void deleteEquipment() {
        System.out.println("--- Delete Equipment ---");
        int id = InputHelper.readInt("Equipment ID to delete: ");
        Equipment e = dm.findEquipmentById(id);
        if (e == null) { System.out.println("Equipment not found."); return; }
        dm.removeEquipment(id);
        System.out.println("Equipment deleted: " + e.getName());
    }

    // ========== 4. Team Management ==========

    private void teamManagement() {
        while (true) {
            System.out.println("--- Team Management ---");
            System.out.println("1. List All Teams");
            System.out.println("2. Add Team");
            System.out.println("3. Edit Team");
            System.out.println("4. Delete Team");
            System.out.println("5. Add Player to Team");
            System.out.println("6. Remove Player from Team");
            System.out.println("7. Back");
            int c = InputHelper.readIntInRange("Choice: ", 1, 7);
            if (c == 7) return;
            System.out.println();
            switch (c) {
                case 1: listTeams();        break;
                case 2: addTeam();          break;
                case 3: editTeam();         break;
                case 4: deleteTeam();       break;
                case 5: addPlayerToTeam();  break;
                case 6: removePlayerFromTeam(); break;
            }
        }
    }

    private void listTeams() {
        System.out.println("--- All Teams ---");
        for (Team t : dm.getTeams()) {
            System.out.println("ID: " + t.getId() + " | " + t.getName()
                    + " | Members: " + t.getPlayerIds().size());
        }
    }

    private void addTeam() {
        System.out.println("--- Add Team ---");
        int id = InputHelper.readInt("New ID: ");
        String name = InputHelper.readNonEmptyString("Name: ");
        dm.addTeam(new Team(id, name));
        System.out.println("Team added.");
    }

    private void editTeam() {
        System.out.println("--- Edit Team ---");
        int id = InputHelper.readInt("Team ID to edit: ");
        Team t = dm.findTeamById(id);
        if (t == null) { System.out.println("Team not found."); return; }
        String name = InputHelper.readString("New name [" + t.getName() + "]: ");
        if (!name.isEmpty()) t.setName(name);
        dm.updateTeam(t);
        System.out.println("Team updated.");
    }

    private void deleteTeam() {
        System.out.println("--- Delete Team ---");
        int id = InputHelper.readInt("Team ID to delete: ");
        Team t = dm.findTeamById(id);
        if (t == null) { System.out.println("Team not found."); return; }
        // Cascade: clear teamId on all members
        for (int pid : t.getPlayerIds()) {
            Player p = dm.findPlayerById(pid);
            if (p != null) p.setTeamId(null);
        }
        dm.removeTeam(id);
        System.out.println("Team deleted: " + t.getName());
    }

    private void addPlayerToTeam() {
        int playerId = InputHelper.readInt("Player ID: ");
        Player p = dm.findPlayerById(playerId);
        if (p == null) { System.out.println("Player not found."); return; }
        int teamId = InputHelper.readInt("Team ID: ");
        Team t = dm.findTeamById(teamId);
        if (t == null) { System.out.println("Team not found."); return; }
        t.addPlayer(playerId);
        p.setTeamId(teamId);
        System.out.println(p.getName() + " added to " + t.getName());
    }

    private void removePlayerFromTeam() {
        int playerId = InputHelper.readInt("Player ID: ");
        Player p = dm.findPlayerById(playerId);
        if (p == null) { System.out.println("Player not found."); return; }
        if (p.getTeamId() == null) { System.out.println("Player is not on a team."); return; }
        Team t = dm.findTeamById(p.getTeamId());
        if (t != null) t.removePlayer(playerId);
        p.setTeamId(null);
        System.out.println(p.getName() + " removed from team.");
    }

    // ========== 5. Match Record Management ==========

    private void matchManagement() {
        while (true) {
            System.out.println("--- Match Record Management ---");
            System.out.println("1. List All Matches");
            System.out.println("2. Add Match");
            System.out.println("3. Delete Match");
            System.out.println("4. Back");
            int c = InputHelper.readIntInRange("Choice: ", 1, 4);
            if (c == 4) return;
            System.out.println();
            switch (c) {
                case 1: listMatches(); break;
                case 2: addMatch();   break;
                case 3: deleteMatch(); break;
            }
        }
    }

    private void listMatches() {
        System.out.println("--- All Matches ---");
        for (MatchRecord m : dm.getMatchRecords()) {
            System.out.println(matchHistory.formatMatchRecord(m));
        }
    }

    private void addMatch() {
        System.out.println("--- Add Match ---");
        int id = InputHelper.readInt("New ID: ");
        int teamA = InputHelper.readInt("Team A ID: ");
        int teamB = InputHelper.readInt("Team B ID: ");
        System.out.print("Date (YYYY-MM-DD): ");
        String dateStr = InputHelper.readNonEmptyString("");
        LocalDate date = LocalDate.parse(dateStr);
        System.out.println("Result (from Team A perspective): WIN / LOSS / DRAW");
        String resStr = InputHelper.readNonEmptyString("Result: ").toUpperCase();
        MatchResult result = MatchResult.valueOf(resStr);
        // Build playerHeroPicks map
        Map<Integer, Integer> picks = new HashMap<>();
        System.out.println("Enter hero picks (playerId heroId per line, blank to finish):");
        while (true) {
            String line = InputHelper.readString("  Pick: ");
            if (line.isEmpty()) break;
            String[] parts = line.trim().split("\\s+");
            if (parts.length == 2) {
                picks.put(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            }
        }
        dm.addMatchRecord(new MatchRecord(id, teamA, teamB, date, result, picks));
        System.out.println("Match added.");
    }

    private void deleteMatch() {
        System.out.println("--- Delete Match ---");
        int id = InputHelper.readInt("Match ID to delete: ");
        MatchRecord m = dm.findMatchRecordById(id);
        if (m == null) { System.out.println("Match not found."); return; }
        dm.removeMatchRecord(id);
        System.out.println("Match deleted.");
    }

    // ========== 6. View Public Info ==========

    private void viewPublicInfo() {
        while (true) {
            System.out.println("--- Public Info ---");
            System.out.println("1. Search Hero by Name");
            System.out.println("2. Search Team by ID/Name");
            System.out.println("3. View Equipment Ranking");
            System.out.println("4. View Leaderboard");
            System.out.println("5. View Player Profile");
            System.out.println("6. Back");
            int c = InputHelper.readIntInRange("Choice: ", 1, 6);
            if (c == 6) return;
            System.out.println();
            switch (c) {
                case 1: searchHero();          break;
                case 2: searchTeam();          break;
                case 3: viewEquipmentRanking(); break;
                case 4: viewLeaderboard();     break;
                case 5: viewPlayerProfile();   break;
            }
            InputHelper.readString("Press Enter to continue...");
        }
    }

    // --- Reuses PlayerView logic for public queries ---

    private void searchHero() {
        String name = InputHelper.readNonEmptyString("Enter hero name: ");
        Hero hero = search.searchHeroByName(name);
        if (hero == null) {
            System.out.println("Hero not found.");
        } else {
            System.out.println("Hero: " + hero.getName() + " | Type: " + hero.getType());
            System.out.println("Stats: " + hero.getBaseStats());
            System.out.println("Compatible Equipment:");
            for (int eid : hero.getCompatibleEquipmentIds()) {
                Equipment eq = dm.findEquipmentById(eid);
                System.out.println("  - " + (eq != null ? eq.getName() : "Eq #" + eid));
            }
            System.out.println("Owners:");
            for (Player p : dm.getPlayers()) {
                if (p.getHeroEquipmentMap().containsKey(hero.getId())) {
                    System.out.println("  - " + p.getName());
                }
            }
        }
    }

    private void searchTeam() {
        String input = InputHelper.readNonEmptyString("Enter team ID or name: ");
        Team team = search.searchTeamByIdOrName(input);
        if (team == null) {
            System.out.println("Team not found.");
        } else {
            System.out.println("Team: " + team.getName() + " | Members: " + team.getPlayerIds().size());
            for (int pid : team.getPlayerIds()) {
                Player p = dm.findPlayerById(pid);
                if (p != null) {
                    System.out.println("  - " + p.getName() + " | Lv." + p.getLevel()
                            + " | WR: " + String.format("%.1f%%", p.getWinRate()));
                }
            }
        }
    }

    private void viewEquipmentRanking() {
        System.out.println("--- Top 10 Equipment ---");
        List<Equipment> all = ranking.getEquipmentRankingByUsageCount();
        for (int i = 0; i < Math.min(10, all.size()); i++) {
            Equipment e = all.get(i);
            System.out.println((i + 1) + ". " + e.getName() + " (used " + e.getUsageCount() + ")");
        }
    }

    private void viewLeaderboard() {
        System.out.println("Metrics: winRate / level / matches");
        String metric = InputHelper.readNonEmptyString("Metric: ").toLowerCase();
        int topN = InputHelper.readIntInRange("Top N: ", 1, 50);
        for (Player p : ranking.getPlayerLeaderboard(metric, topN)) {
            System.out.println(p.getName() + " | Lv." + p.getLevel()
                    + " | WR: " + String.format("%.1f%%", p.getWinRate())
                    + " | Matches: " + p.getTotalMatches());
        }
    }

    private void viewPlayerProfile() {
        int id = InputHelper.readInt("Player ID: ");
        Player p = dm.findPlayerById(id);
        if (p == null) {
            System.out.println("Player not found.");
        } else {
            System.out.println("Name: " + p.getName() + " | Lv." + p.getLevel());
            System.out.println("Win Rate: " + String.format("%.1f%%", p.getWinRate()));
            System.out.println("Team: " + (p.getTeamId() != null ? dm.findTeamById(p.getTeamId()).getName() : "none"));
            System.out.println("Heroes:");
            for (int hid : p.getHeroEquipmentMap().keySet()) {
                Hero h = dm.findHeroById(hid);
                System.out.println("  - " + (h != null ? h.getName() : "Hero #" + hid));
            }
        }
    }
}
