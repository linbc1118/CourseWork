package service;

import model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Central in-memory data store for the Honor of Kings system.
 *
 * Owns all entity collections and exposes simple add/get/remove/update
 * operations with no business logic. Designed to be instantiated once
 * in Main and passed to domain services via constructor injection.
 *
 * Per plan.md Section 4.3: pure storage facade -- no validation,
 * no cross-entity operations, no ID generation (that lives in services).
 */
public class DataManager {

    // ========== Data Collections ==========

    private List<Player> players;
    private List<Admin> admins;
    private List<Hero> heroes;
    private List<Equipment> equipmentList;
    private List<Team> teams;
    private List<MatchRecord> matchRecords;

    // ========== Constructor ==========

    /**
     * Creates an empty DataManager with all collections initialized.
     * Call DataInitializer or service methods to populate after construction.
     */
    public DataManager() {
        this.players = new ArrayList<>();
        this.admins = new ArrayList<>();
        this.heroes = new ArrayList<>();
        this.equipmentList = new ArrayList<>();
        this.teams = new ArrayList<>();
        this.matchRecords = new ArrayList<>();
    }

    // ========== List Getters ==========

    /** Returns the full list of players (direct reference for simplicity). */
    public List<Player> getPlayers() {
        return players;
    }

    /** Returns the full list of admins. */
    public List<Admin> getAdmins() {
        return admins;
    }

    /** Returns the full list of heroes. */
    public List<Hero> getHeroes() {
        return heroes;
    }

    /** Returns the full list of equipment. */
    public List<Equipment> getEquipmentList() {
        return equipmentList;
    }

    /** Returns the full list of teams. */
    public List<Team> getTeams() {
        return teams;
    }

    /** Returns the full list of match records. */
    public List<MatchRecord> getMatchRecords() {
        return matchRecords;
    }

    // ========== Player CRUD ==========

    /** Adds a player to the store. */
    public void addPlayer(Player p) {
        players.add(p);
    }

    /**
     * Removes a player by ID. Does nothing if not found.
     * Note: caller must handle cascade (remove from team, etc.) beforehand.
     */
    public void removePlayer(int playerId) {
        Player p = findPlayerById(playerId);
        if (p != null) {
            players.remove(p);
        }
    }

    /**
     * Updates an existing player. Finds the player by ID and replaces it.
     * Does nothing if the player ID is not found.
     */
    public void updatePlayer(Player updated) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getId() == updated.getId()) {
                players.set(i, updated);
                return;
            }
        }
    }

    // ========== Admin CRUD ==========

    /** Adds an admin to the store. */
    public void addAdmin(Admin a) {
        admins.add(a);
    }

    /** Removes an admin by ID. Does nothing if not found. */
    public void removeAdmin(int adminId) {
        Admin a = findAdminById(adminId);
        if (a != null) {
            admins.remove(a);
        }
    }

    /** Updates an existing admin by ID. Does nothing if not found. */
    public void updateAdmin(Admin updated) {
        for (int i = 0; i < admins.size(); i++) {
            if (admins.get(i).getId() == updated.getId()) {
                admins.set(i, updated);
                return;
            }
        }
    }

    // ========== Hero CRUD ==========

    /** Adds a hero to the store. */
    public void addHero(Hero h) {
        heroes.add(h);
    }

    /** Removes a hero by ID. Does nothing if not found. */
    public void removeHero(int heroId) {
        Hero h = findHeroById(heroId);
        if (h != null) {
            heroes.remove(h);
        }
    }

    /** Updates an existing hero by ID. Does nothing if not found. */
    public void updateHero(Hero updated) {
        for (int i = 0; i < heroes.size(); i++) {
            if (heroes.get(i).getId() == updated.getId()) {
                heroes.set(i, updated);
                return;
            }
        }
    }

    // ========== Equipment CRUD ==========

    /** Adds an equipment to the store. */
    public void addEquipment(Equipment e) {
        equipmentList.add(e);
    }

    /** Removes an equipment by ID. Does nothing if not found. */
    public void removeEquipment(int equipmentId) {
        Equipment e = findEquipmentById(equipmentId);
        if (e != null) {
            equipmentList.remove(e);
        }
    }

    /** Updates an existing equipment by ID. Does nothing if not found. */
    public void updateEquipment(Equipment updated) {
        for (int i = 0; i < equipmentList.size(); i++) {
            if (equipmentList.get(i).getId() == updated.getId()) {
                equipmentList.set(i, updated);
                return;
            }
        }
    }

    // ========== Team CRUD ==========

    /** Adds a team to the store. */
    public void addTeam(Team t) {
        teams.add(t);
    }

    /** Removes a team by ID. Does nothing if not found. */
    public void removeTeam(int teamId) {
        Team t = findTeamById(teamId);
        if (t != null) {
            teams.remove(t);
        }
    }

    /** Updates an existing team by ID. Does nothing if not found. */
    public void updateTeam(Team updated) {
        for (int i = 0; i < teams.size(); i++) {
            if (teams.get(i).getId() == updated.getId()) {
                teams.set(i, updated);
                return;
            }
        }
    }

    // ========== MatchRecord CRUD ==========

    /** Adds a match record to the store. */
    public void addMatchRecord(MatchRecord m) {
        matchRecords.add(m);
    }

    /** Removes a match record by ID. Does nothing if not found. */
    public void removeMatchRecord(int matchId) {
        MatchRecord m = findMatchRecordById(matchId);
        if (m != null) {
            matchRecords.remove(m);
        }
    }

    /** Updates an existing match record by ID. Does nothing if not found. */
    public void updateMatchRecord(MatchRecord updated) {
        for (int i = 0; i < matchRecords.size(); i++) {
            if (matchRecords.get(i).getId() == updated.getId()) {
                matchRecords.set(i, updated);
                return;
            }
        }
    }

    // ========== Finders -- Player ==========

    /** Finds a player by ID, or null if not found. */
    public Player findPlayerById(int id) {
        for (Player p : players) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    /**
     * Finds a player by name. Names may not be unique;
     * returns the first match found, or null if no match.
     */
    public Player findPlayerByName(String name) {
        if (name == null) return null;
        for (Player p : players) {
            if (name.equals(p.getName())) {
                return p;
            }
        }
        return null;
    }

    // ========== Finders -- Admin ==========

    /** Finds an admin by ID, or null if not found. */
    public Admin findAdminById(int id) {
        for (Admin a : admins) {
            if (a.getId() == id) {
                return a;
            }
        }
        return null;
    }

    // ========== Finders -- Hero ==========

    /** Finds a hero by ID, or null if not found. */
    public Hero findHeroById(int id) {
        for (Hero h : heroes) {
            if (h.getId() == id) {
                return h;
            }
        }
        return null;
    }

    /** Finds a hero by name, or null if not found. */
    public Hero findHeroByName(String name) {
        if (name == null) return null;
        for (Hero h : heroes) {
            if (name.equals(h.getName())) {
                return h;
            }
        }
        return null;
    }

    // ========== Finders -- Equipment ==========

    /** Finds an equipment by ID, or null if not found. */
    public Equipment findEquipmentById(int id) {
        for (Equipment e : equipmentList) {
            if (e.getId() == id) {
                return e;
            }
        }
        return null;
    }

    // ========== Finders -- Team ==========

    /** Finds a team by ID, or null if not found. */
    public Team findTeamById(int id) {
        for (Team t : teams) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }

    /** Finds a team by name, or null if not found. */
    public Team findTeamByName(String name) {
        if (name == null) return null;
        for (Team t : teams) {
            if (name.equals(t.getName())) {
                return t;
            }
        }
        return null;
    }

    // ========== Finders -- MatchRecord ==========

    /** Finds a match record by ID, or null if not found. */
    public MatchRecord findMatchRecordById(int id) {
        for (MatchRecord m : matchRecords) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }
}
