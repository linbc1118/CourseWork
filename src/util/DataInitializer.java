package util;

import model.*;
import service.DataManager;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates hardcoded test data for the Honor of Kings system.
 *
 * Populates a DataManager with at least:
 * - 1 admin
 * - 10 players
 * - 15 heroes
 * - 20 equipment items
 * - 3 teams
 * - 10 match records
 *
 * Also establishes relationships: player hero ownership, hero equipment
 * compatibility, team membership, and match hero picks.
 *
 * Usage: DataInitializer.initialize(dm);
 * Called once from Main before starting the menu loop.
 */
public class DataInitializer {

    /**
     * Populates the given DataManager with all seed data.
     */
    public static void initialize(DataManager dm) {
        createAdmins(dm);
        createPlayers(dm);
        createEquipment(dm);
        createHeroes(dm);
        createTeams(dm);
        createMatchRecords(dm);
        linkPlayersToHeroes(dm);
        linkHeroesToEquipment(dm);
    }

    // ========== Admins ==========

    private static void createAdmins(DataManager dm) {
        dm.addAdmin(new Admin(1, "System Admin", "admin", "admin123"));
    }

    // ========== Players (10) ==========

    private static void createPlayers(DataManager dm) {
        dm.addPlayer(new Player(1,  "Zhang Fei",     "player1",  "pass1",  45, 120, 75));
        dm.addPlayer(new Player(2,  "Guan Yu",       "player2",  "pass2",  42, 105, 60));
        dm.addPlayer(new Player(3,  "Liu Bei",        "player3",  "pass3",  38,  90, 50));
        dm.addPlayer(new Player(4,  "Cao Cao",        "player4",  "pass4",  50, 150, 100));
        dm.addPlayer(new Player(5,  "Sun Quan",       "player5",  "pass5",  30,  70, 35));
        dm.addPlayer(new Player(6,  "Lu Bu",          "player6",  "pass6",  48, 130, 90));
        dm.addPlayer(new Player(7,  "Diao Chan",      "player7",  "pass7",  35,  80, 45));
        dm.addPlayer(new Player(8,  "Zhao Yun",       "player8",  "pass8",  40, 100, 55));
        dm.addPlayer(new Player(9,  "Zhou Yu",        "player9",  "pass9",  25,  50, 20));
        dm.addPlayer(new Player(10, "Sima Yi",        "player10", "pass10", 33,  60, 30));
    }

    // ========== Equipment (20) ==========

    private static void createEquipment(DataManager dm) {
        dm.addEquipment(new Equipment(1,  "Shadow Blade"));
        dm.addEquipment(new Equipment(2,  "Endless Battle"));
        dm.addEquipment(new Equipment(3,  "Hex Core"));
        dm.addEquipment(new Equipment(4,  "Guardian Angel"));
        dm.addEquipment(new Equipment(5,  "Blade of Despair"));
        dm.addEquipment(new Equipment(6,  "Frost Cape"));
        dm.addEquipment(new Equipment(7,  "Lightning Trident"));
        dm.addEquipment(new Equipment(8,  "Sword of Rupture"));
        dm.addEquipment(new Equipment(9,  "Armor of Faith"));
        dm.addEquipment(new Equipment(10, "Shoes of Tranquility"));
        dm.addEquipment(new Equipment(11, "Tiger Gauntlets"));
        dm.addEquipment(new Equipment(12, "Sage's Stone"));
        dm.addEquipment(new Equipment(13, "Eye of Phoenix"));
        dm.addEquipment(new Equipment(14, "Dragon Scale"));
        dm.addEquipment(new Equipment(15, "Moonlight Sword"));
        dm.addEquipment(new Equipment(16, "Crimson Gem"));
        dm.addEquipment(new Equipment(17, "Silver Shield"));
        dm.addEquipment(new Equipment(18, "Golden Armor"));
        dm.addEquipment(new Equipment(19, "Iron Helmet"));
        dm.addEquipment(new Equipment(20, "Warrior's Axe"));
    }

    // ========== Heroes (15) ==========

    private static void createHeroes(DataManager dm) {
        dm.addHero(new Hero(1,  "Li Bai",          HeroType.ASSASSIN,  makeStats(90, 40, 1200)));
        dm.addHero(new Hero(2,  "Diao Chan",       HeroType.MAGE,      makeStats(75, 30, 1000)));
        dm.addHero(new Hero(3,  "Luban No.7",      HeroType.MARKSMAN,  makeStats(85, 35, 950)));
        dm.addHero(new Hero(4,  "Kai",             HeroType.WARRIOR,   makeStats(80, 70, 1400)));
        dm.addHero(new Hero(5,  "Sun Wukong",      HeroType.ASSASSIN,  makeStats(95, 45, 1150)));
        dm.addHero(new Hero(6,  "Zhao Yun",        HeroType.WARRIOR,   makeStats(78, 65, 1350)));
        dm.addHero(new Hero(7,  "Hua Mulan",       HeroType.WARRIOR,   makeStats(82, 60, 1300)));
        dm.addHero(new Hero(8,  "Cheng Yaojin",    HeroType.TANK,      makeStats(55, 95, 1800)));
        dm.addHero(new Hero(9,  "Zhuang Zhou",     HeroType.SUPPORT,   makeStats(50, 80, 1600)));
        dm.addHero(new Hero(10, "Cai Wenji",       HeroType.SUPPORT,   makeStats(40, 60, 1100)));
        dm.addHero(new Hero(11, "Sun Shangxiang",  HeroType.MARKSMAN,  makeStats(88, 30, 900)));
        dm.addHero(new Hero(12, "Hou Yi",          HeroType.MARKSMAN,  makeStats(92, 28, 880)));
        dm.addHero(new Hero(13, "Daji",            HeroType.MAGE,      makeStats(80, 25, 980)));
        dm.addHero(new Hero(14, "Zhen Ji",         HeroType.MAGE,      makeStats(72, 32, 1020)));
        dm.addHero(new Hero(15, "Mo Xie",          HeroType.MAGE,      makeStats(78, 28, 960)));
    }

    // ========== Teams (3) ==========

    private static void createTeams(DataManager dm) {
        Team team1 = new Team(1, "Team Dragon");
        team1.addPlayer(1);   // Zhang Fei
        team1.addPlayer(2);   // Guan Yu
        team1.addPlayer(3);   // Liu Bei
        team1.addPlayer(4);   // Cao Cao

        Team team2 = new Team(2, "Team Tiger");
        team2.addPlayer(5);   // Sun Quan
        team2.addPlayer(6);   // Lu Bu
        team2.addPlayer(7);   // Diao Chan (player)

        Team team3 = new Team(3, "Team Eagle");
        team3.addPlayer(8);   // Zhao Yun
        team3.addPlayer(9);   // Zhou Yu
        team3.addPlayer(10);  // Sima Yi

        dm.addTeam(team1);
        dm.addTeam(team2);
        dm.addTeam(team3);

        // Set teamId on each player to keep both sides in sync
        setPlayerTeam(dm, 1,  1);
        setPlayerTeam(dm, 2,  1);
        setPlayerTeam(dm, 3,  1);
        setPlayerTeam(dm, 4,  1);
        setPlayerTeam(dm, 5,  2);
        setPlayerTeam(dm, 6,  2);
        setPlayerTeam(dm, 7,  2);
        setPlayerTeam(dm, 8,  3);
        setPlayerTeam(dm, 9,  3);
        setPlayerTeam(dm, 10, 3);
    }

    // ========== Match Records (10) ==========

    private static void createMatchRecords(DataManager dm) {
        // Match 1: Team Dragon (A) vs Team Tiger (B) -- Dragon wins
        dm.addMatchRecord(new MatchRecord(1, 1, 2,
                LocalDate.of(2025, 1, 15), MatchResult.WIN,
                makePicks(new int[][]{{1,1},{2,2},{3,6},{4,4},{5,3},{6,5},{7,10}})));

        // Match 2: Team Eagle (A) vs Team Dragon (B) -- Dragon wins (team B)
        dm.addMatchRecord(new MatchRecord(2, 3, 1,
                LocalDate.of(2025, 2, 10), MatchResult.LOSS,
                makePicks(new int[][]{{8,7},{9,9},{10,15},{1,1},{2,4},{3,8},{4,11}})));

        // Match 3: Team Tiger (A) vs Team Eagle (B) -- Tiger wins
        dm.addMatchRecord(new MatchRecord(3, 2, 3,
                LocalDate.of(2025, 3, 5), MatchResult.WIN,
                makePicks(new int[][]{{5,12},{6,5},{7,13},{8,8},{9,14},{10,3}})));

        // Match 4: Team Dragon (A) vs Team Eagle (B) -- Draw
        dm.addMatchRecord(new MatchRecord(4, 1, 3,
                LocalDate.of(2025, 3, 20), MatchResult.DRAW,
                makePicks(new int[][]{{1,6},{2,4},{3,7},{4,1},{8,11},{9,10},{10,2}})));

        // Match 5: Team Dragon (A) vs Team Tiger (B) -- Tiger wins (team B)
        dm.addMatchRecord(new MatchRecord(5, 1, 2,
                LocalDate.of(2025, 4, 8), MatchResult.LOSS,
                makePicks(new int[][]{{1,2},{2,7},{3,4},{4,5},{5,8},{6,6},{7,3}})));

        // Match 6: Team Tiger (A) vs Team Dragon (B) -- Tiger wins
        dm.addMatchRecord(new MatchRecord(6, 2, 1,
                LocalDate.of(2025, 5, 12), MatchResult.WIN,
                makePicks(new int[][]{{5,1},{6,5},{7,14},{1,6},{2,7},{3,4},{4,2}})));

        // Match 7: Team Eagle (A) vs Team Tiger (B) -- Eagle wins
        dm.addMatchRecord(new MatchRecord(7, 3, 2,
                LocalDate.of(2025, 6, 1), MatchResult.WIN,
                makePicks(new int[][]{{8,12},{9,13},{10,15},{5,3},{6,8},{7,9}})));

        // Match 8: Team Dragon (A) vs Team Eagle (B) -- Dragon wins
        dm.addMatchRecord(new MatchRecord(8, 1, 3,
                LocalDate.of(2025, 7, 18), MatchResult.WIN,
                makePicks(new int[][]{{1,1},{2,6},{3,7},{4,8},{8,10},{9,11},{10,13}})));

        // Match 9: Team Tiger (A) vs Team Eagle (B) -- Tiger wins
        dm.addMatchRecord(new MatchRecord(9, 2, 3,
                LocalDate.of(2025, 8, 22), MatchResult.WIN,
                makePicks(new int[][]{{5,5},{6,1},{7,2},{8,4},{9,11},{10,12}})));

        // Match 10: Team Dragon (A) vs Team Tiger (B) -- Dragon wins
        dm.addMatchRecord(new MatchRecord(10, 1, 2,
                LocalDate.of(2025, 9, 30), MatchResult.WIN,
                makePicks(new int[][]{{1,4},{2,8},{3,6},{4,5},{5,2},{6,7},{7,10}})));
    }

    // ========== Relationships ==========

    /**
     * Gives each player 2-3 heroes with an empty equipment list.
     * The equipment can be filled in later via Player.equipHero().
     */
    private static void linkPlayersToHeroes(DataManager dm) {
        addHeroToPlayer(dm, 1,  new int[]{1, 4, 8});    // Li Bai, Kai, Cheng Yaojin
        addHeroToPlayer(dm, 2,  new int[]{2, 6, 7});    // Diao Chan, Zhao Yun, Hua Mulan
        addHeroToPlayer(dm, 3,  new int[]{3, 9, 12});   // Luban, Zhuang Zhou, Hou Yi
        addHeroToPlayer(dm, 4,  new int[]{5, 4, 14});   // Sun Wukong, Kai, Zhen Ji
        addHeroToPlayer(dm, 5,  new int[]{6, 10});      // Zhao Yun, Cai Wenji
        addHeroToPlayer(dm, 6,  new int[]{1, 5, 11});   // Li Bai, Sun Wukong, Sun Shangxiang
        addHeroToPlayer(dm, 7,  new int[]{13, 15});     // Daji, Mo Xie
        addHeroToPlayer(dm, 8,  new int[]{3, 7, 8});    // Luban, Hua Mulan, Cheng Yaojin
        addHeroToPlayer(dm, 9,  new int[]{2, 11, 14});  // Diao Chan, Sun Shangxiang, Zhen Ji
        addHeroToPlayer(dm, 10, new int[]{4, 6, 12});   // Kai, Zhao Yun, Hou Yi
    }

    /**
     * Assigns each hero 2-3 compatible equipment IDs.
     */
    private static void linkHeroesToEquipment(DataManager dm) {
        addEquipToHero(dm, 1,  new int[]{1, 5, 7});       // Li Bai: Shadow Blade, Blade of Despair, Lightning Trident
        addEquipToHero(dm, 2,  new int[]{3, 12, 13});     // Diao Chan: Hex Core, Sage's Stone, Eye of Phoenix
        addEquipToHero(dm, 3,  new int[]{1, 11, 15});     // Luban: Shadow Blade, Tiger Gauntlets, Moonlight Sword
        addEquipToHero(dm, 4,  new int[]{4, 6, 17});      // Kai: Guardian Angel, Frost Cape, Silver Shield
        addEquipToHero(dm, 5,  new int[]{2, 8, 20});      // Sun Wukong: Endless Battle, Sword of Rupture, Warrior's Axe
        addEquipToHero(dm, 6,  new int[]{4, 9, 18});      // Zhao Yun: Guardian Angel, Armor of Faith, Golden Armor
        addEquipToHero(dm, 7,  new int[]{6, 10, 19});     // Hua Mulan: Frost Cape, Shoes of Tranquility, Iron Helmet
        addEquipToHero(dm, 8,  new int[]{4, 14, 17});     // Cheng Yaojin: Guardian Angel, Dragon Scale, Silver Shield
        addEquipToHero(dm, 9,  new int[]{12, 13, 16});    // Zhuang Zhou: Sage's Stone, Eye of Phoenix, Crimson Gem
        addEquipToHero(dm, 10, new int[]{9, 10, 13});     // Cai Wenji: Armor of Faith, Shoes of Tranquility, Eye of Phoenix
        addEquipToHero(dm, 11, new int[]{1, 11, 15});     // Sun Shangxiang: Shadow Blade, Tiger Gauntlets, Moonlight Sword
        addEquipToHero(dm, 12, new int[]{2, 7, 15});      // Hou Yi: Endless Battle, Lightning Trident, Moonlight Sword
        addEquipToHero(dm, 13, new int[]{3, 12, 14});     // Daji: Hex Core, Sage's Stone, Dragon Scale
        addEquipToHero(dm, 14, new int[]{3, 5, 13});      // Zhen Ji: Hex Core, Blade of Despair, Eye of Phoenix
        addEquipToHero(dm, 15, new int[]{2, 3, 7});       // Mo Xie: Endless Battle, Hex Core, Lightning Trident
    }

    // ========== Helper Methods ==========

    /** Creates a stats map with attack, defense, and hp values. */
    private static Map<String, Integer> makeStats(int attack, int defense, int hp) {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("attack", attack);
        stats.put("defense", defense);
        stats.put("hp", hp);
        return stats;
    }

    /** Builds a playerHeroPicks map from an array of [playerId, heroId] pairs. */
    private static Map<Integer, Integer> makePicks(int[][] pairs) {
        Map<Integer, Integer> picks = new HashMap<>();
        for (int[] pair : pairs) {
            picks.put(pair[0], pair[1]);
        }
        return picks;
    }

    /** Sets a player's teamId. */
    private static void setPlayerTeam(DataManager dm, int playerId, int teamId) {
        Player p = dm.findPlayerById(playerId);
        if (p != null) {
            p.setTeamId(teamId);
        }
    }

    /** Adds heroes with empty equipment lists to a player. */
    private static void addHeroToPlayer(DataManager dm, int playerId, int[] heroIds) {
        Player p = dm.findPlayerById(playerId);
        if (p != null) {
            for (int heroId : heroIds) {
                p.getHeroEquipmentMap().put(heroId, new java.util.ArrayList<>());
            }
        }
    }

    /** Adds compatible equipment IDs to a hero. */
    private static void addEquipToHero(DataManager dm, int heroId, int[] equipIds) {
        Hero h = dm.findHeroById(heroId);
        if (h != null) {
            for (int equipId : equipIds) {
                h.addCompatibleEquipment(equipId);
            }
        }
    }
}
