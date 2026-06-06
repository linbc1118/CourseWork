package service;

import model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Handles saving and loading all system data to/from a single text file.
 *
 * <h3>File format</h3>
 * The file uses a header-per-section format. Each section starts with a
 * {@code ==SECTION_NAME==} line, followed by one record per line using
 * the pipe character ({@code |}) as the field delimiter.
 *
 * <pre>
 * ==PLAYERS==
 * id|name|username|password|level|totalMatches|wins|teamId
 *   teamId = "null" if the player is not on a team.
 *
 * ==PLAYER_HERO_EQUIPMENT==
 * playerId|heroId|equipId1,equipId2,...
 *   One line per (player, hero) pair. Equipment list may be empty.
 *
 * ==ADMINS==
 * id|name|username|password
 *
 * ==HEROES==
 * id|name|type|key1=val1,key2=val2
 *   type is a HeroType enum constant (e.g. ASSASSIN, MAGE).
 *   baseStats use "=" for pairs and "," between pairs.
 *
 * ==HERO_COMPATIBLE_EQUIPMENT==
 * heroId|equipId1,equipId2,...
 *   One line per hero that has compatible equipment.
 *
 * ==EQUIPMENT==
 * id|name|usageCount
 *
 * ==TEAMS==
 * id|name|playerId1,playerId2,...
 *
 * ==MATCH_RECORDS==
 * id|teamAId|teamBId|date|result|playerId=heroId,...
 *   result is a MatchResult enum constant (WIN, LOSS, DRAW).
 *   playerHeroPicks use "=" for pairs and "," between pairs.
 * </pre>
 *
 * <h3>Limitations</h3>
 * Field values (names, usernames, passwords) must not contain the pipe
 * character ({@code |}) or leading/trailing whitespace. This is acceptable
 * for the controlled game-data domain this project operates in.
 */
public class FileStorageService {

    // ========== Section Headers ==========

    private static final String SEC_PLAYERS                = "==PLAYERS==";
    private static final String SEC_PLAYER_HERO_EQUIPMENT  = "==PLAYER_HERO_EQUIPMENT==";
    private static final String SEC_ADMINS                 = "==ADMINS==";
    private static final String SEC_HEROES                 = "==HEROES==";
    private static final String SEC_HERO_COMPAT_EQUIP      = "==HERO_COMPATIBLE_EQUIPMENT==";
    private static final String SEC_EQUIPMENT              = "==EQUIPMENT==";
    private static final String SEC_TEAMS                  = "==TEAMS==";
    private static final String SEC_MATCH_RECORDS          = "==MATCH_RECORDS==";

    // ========== Delimiters ==========

    /** Regex for splitting lines on the pipe character. */
    private static final String PIPE_RE = "\\|";

    /** The pipe character used when joining fields. */
    private static final char PIPE = '|';

    /** Comma separator for list/map sub-fields. */
    private static final String COMMA = ",";

    /** Equals separator for key=value pairs. */
    private static final String EQ = "=";

    /** String written for a null team ID. */
    private static final String NULL_TEAM = "null";

    // ========================================================================
    //  Public API
    // ========================================================================

    /**
     * Writes all data from the given {@link DataManager} to a text file.
     *
     * @param dm       the central data store to read from
     * @param filePath the destination file path (will be overwritten)
     * @throws IOException if an I/O error occurs during writing
     */
    public void saveAllData(DataManager dm, String filePath) throws IOException {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(filePath))) {
            writePlayers(dm, w);
            writePlayerHeroEquipment(dm, w);
            writeAdmins(dm, w);
            writeHeroes(dm, w);
            writeHeroCompatibleEquipment(dm, w);
            writeEquipment(dm, w);
            writeTeams(dm, w);
            writeMatchRecords(dm, w);
        }
    }

    /**
     * Reads all data from a text file and populates the given {@link DataManager}.
     *
     * <p>Existing data in the DataManager is <b>cleared</b> before loading.
     * If the file does not exist, this method returns silently (the caller
     * is expected to fall back to seed data via {@code DataInitializer}).
     *
     * @param dm       the central data store to populate
     * @param filePath the source file path
     * @throws IOException if an I/O error occurs during reading
     */
    public void loadAllData(DataManager dm, String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        // Start fresh before loading
        dm.clearAllData();

        // Relationship lines are collected during the read pass and processed
        // after all entities exist, so that cross-references resolve correctly.
        List<String> playerHeroEquipLines = new ArrayList<>();
        List<String> heroCompatEquipLines  = new ArrayList<>();

        try (BufferedReader r = new BufferedReader(new FileReader(file))) {
            String line;
            String section = null;

            while ((line = r.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                // Detect section headers
                if (line.startsWith("==") && line.endsWith("==")) {
                    section = line;
                    continue;
                }

                // Dispatch based on current section
                switch (section) {
                    case SEC_PLAYERS:
                        parsePlayer(line, dm);
                        break;
                    case SEC_PLAYER_HERO_EQUIPMENT:
                        playerHeroEquipLines.add(line);
                        break;
                    case SEC_ADMINS:
                        parseAdmin(line, dm);
                        break;
                    case SEC_HEROES:
                        parseHero(line, dm);
                        break;
                    case SEC_HERO_COMPAT_EQUIP:
                        heroCompatEquipLines.add(line);
                        break;
                    case SEC_EQUIPMENT:
                        parseEquipment(line, dm);
                        break;
                    case SEC_TEAMS:
                        parseTeam(line, dm);
                        break;
                    case SEC_MATCH_RECORDS:
                        parseMatchRecord(line, dm);
                        break;
                    default:
                        // Unknown section - skip line silently
                        break;
                }
            }
        }

        // Second pass: resolve relationships now that all entities exist
        for (String relLine : playerHeroEquipLines) {
            parsePlayerHeroEquipment(relLine, dm);
        }
        for (String relLine : heroCompatEquipLines) {
            parseHeroCompatibleEquipment(relLine, dm);
        }
    }

    // ========================================================================
    //  Save helpers - one per section
    // ========================================================================

    /** Writes the ==PLAYERS== section. */
    private void writePlayers(DataManager dm, BufferedWriter w) throws IOException {
        w.write(SEC_PLAYERS);
        w.newLine();
        for (Player p : dm.getPlayers()) {
            w.write(join(PIPE,
                    p.getId(),
                    p.getName(),
                    p.getUsername(),
                    p.getPassword(),
                    p.getLevel(),
                    p.getTotalMatches(),
                    p.getWins(),
                    p.getTeamId() != null ? p.getTeamId() : NULL_TEAM));
            w.newLine();
        }
        w.newLine();
    }

    /** Writes the ==PLAYER_HERO_EQUIPMENT== section. */
    private void writePlayerHeroEquipment(DataManager dm, BufferedWriter w) throws IOException {
        w.write(SEC_PLAYER_HERO_EQUIPMENT);
        w.newLine();
        for (Player p : dm.getPlayers()) {
            for (Map.Entry<Integer, List<Integer>> entry : p.getHeroEquipmentMap().entrySet()) {
                int heroId = entry.getKey();
                List<Integer> equipIds = entry.getValue();
                String equipStr = joinComma(equipIds);
                w.write(join(PIPE, p.getId(), heroId, equipStr));
                w.newLine();
            }
        }
        w.newLine();
    }

    /** Writes the ==ADMINS== section. */
    private void writeAdmins(DataManager dm, BufferedWriter w) throws IOException {
        w.write(SEC_ADMINS);
        w.newLine();
        for (Admin a : dm.getAdmins()) {
            w.write(join(PIPE,
                    a.getId(),
                    a.getName(),
                    a.getUsername(),
                    a.getPassword()));
            w.newLine();
        }
        w.newLine();
    }

    /** Writes the ==HEROES== section. */
    private void writeHeroes(DataManager dm, BufferedWriter w) throws IOException {
        w.write(SEC_HEROES);
        w.newLine();
        for (Hero h : dm.getHeroes()) {
            // Encode baseStats as "key1=val1,key2=val2,..."
            String statsStr = joinMap(h.getBaseStats());
            w.write(join(PIPE,
                    h.getId(),
                    h.getName(),
                    h.getType().name(),
                    statsStr));
            w.newLine();
        }
        w.newLine();
    }

    /** Writes the ==HERO_COMPATIBLE_EQUIPMENT== section. */
    private void writeHeroCompatibleEquipment(DataManager dm, BufferedWriter w) throws IOException {
        w.write(SEC_HERO_COMPAT_EQUIP);
        w.newLine();
        for (Hero h : dm.getHeroes()) {
            List<Integer> compatIds = h.getCompatibleEquipmentIds();
            if (compatIds.isEmpty()) {
                continue; // nothing to save
            }
            w.write(join(PIPE, h.getId(), joinComma(compatIds)));
            w.newLine();
        }
        w.newLine();
    }

    /** Writes the ==EQUIPMENT== section. */
    private void writeEquipment(DataManager dm, BufferedWriter w) throws IOException {
        w.write(SEC_EQUIPMENT);
        w.newLine();
        for (Equipment e : dm.getEquipmentList()) {
            w.write(join(PIPE,
                    e.getId(),
                    e.getName(),
                    e.getUsageCount()));
            w.newLine();
        }
        w.newLine();
    }

    /** Writes the ==TEAMS== section. */
    private void writeTeams(DataManager dm, BufferedWriter w) throws IOException {
        w.write(SEC_TEAMS);
        w.newLine();
        for (Team t : dm.getTeams()) {
            w.write(join(PIPE,
                    t.getId(),
                    t.getName(),
                    joinComma(t.getPlayerIds())));
            w.newLine();
        }
        w.newLine();
    }

    /** Writes the ==MATCH_RECORDS== section. */
    private void writeMatchRecords(DataManager dm, BufferedWriter w) throws IOException {
        w.write(SEC_MATCH_RECORDS);
        w.newLine();
        for (MatchRecord m : dm.getMatchRecords()) {
            // Encode playerHeroPicks as "pid1=hid1,pid2=hid2,..."
            String picksStr = joinMap(m.getPlayerHeroPicks());
            w.write(join(PIPE,
                    m.getId(),
                    m.getTeamAId(),
                    m.getTeamBId(),
                    m.getDate().toString(),
                    m.getResult().name(),
                    picksStr));
            w.newLine();
        }
        w.newLine();
    }

    // ========================================================================
    //  Load helpers - one per entity / relationship type
    // ========================================================================

    /**
     * Parses a player line.
     * Format: {@code id|name|username|password|level|totalMatches|wins|teamId}
     * where {@code teamId} may be the literal string {@code "null"}.
     */
    private void parsePlayer(String line, DataManager dm) {
        String[] parts = line.split(PIPE_RE);
        // parts: 0=id, 1=name, 2=username, 3=password, 4=level, 5=totalMatches, 6=wins, 7=teamId
        int id           = Integer.parseInt(parts[0]);
        String name      = parts[1];
        String username  = parts[2];
        String password  = parts[3];
        int level        = Integer.parseInt(parts[4]);
        int totalMatches = Integer.parseInt(parts[5]);
        int wins         = Integer.parseInt(parts[6]);

        Player player = new Player(id, name, username, password, level, totalMatches, wins);

        if (parts.length > 7 && !parts[7].equals(NULL_TEAM)) {
            player.setTeamId(Integer.parseInt(parts[7]));
        }

        dm.addPlayer(player);
    }

    /**
     * Parses a player-hero-equipment relationship line.
     * Format: {@code playerId|heroId|equipId1,equipId2,...}
     */
    private void parsePlayerHeroEquipment(String line, DataManager dm) {
        String[] parts = line.split(PIPE_RE);
        int playerId = Integer.parseInt(parts[0]);
        int heroId   = Integer.parseInt(parts[1]);

        Player player = dm.findPlayerById(playerId);
        if (player == null) {
            return; // orphaned relationship - skip
        }

        List<Integer> equipIds = new ArrayList<>();
        if (parts.length > 2 && !parts[2].isEmpty()) {
            for (String token : parts[2].split(COMMA)) {
                equipIds.add(Integer.parseInt(token.trim()));
            }
        }

        // Put directly into the player's map (getHeroEquipmentMap returns the raw map)
        player.getHeroEquipmentMap().put(heroId, equipIds);
    }

    /**
     * Parses an admin line.
     * Format: {@code id|name|username|password}
     */
    private void parseAdmin(String line, DataManager dm) {
        String[] parts = line.split(PIPE_RE);
        int id          = Integer.parseInt(parts[0]);
        String name     = parts[1];
        String username = parts[2];
        String password = parts[3];

        Admin admin = new Admin(id, name, username, password);
        dm.addAdmin(admin);
    }

    /**
     * Parses a hero line.
     * Format: {@code id|name|type|key1=val1,key2=val2}
     * The stats sub-field may be empty if the hero has no base stats.
     */
    private void parseHero(String line, DataManager dm) {
        String[] parts = line.split(PIPE_RE);
        // parts: 0=id, 1=name, 2=type, 3=baseStats (optional)
        int id         = Integer.parseInt(parts[0]);
        String name    = parts[1];
        HeroType type  = HeroType.valueOf(parts[2]);

        Map<String, Integer> baseStats = new HashMap<>();
        if (parts.length > 3 && !parts[3].isEmpty()) {
            for (String pair : parts[3].split(COMMA)) {
                String[] kv = pair.split(EQ);
                baseStats.put(kv[0], Integer.parseInt(kv[1]));
            }
        }

        Hero hero = new Hero(id, name, type, baseStats);
        dm.addHero(hero);
    }

    /**
     * Parses a hero-compatible-equipment relationship line.
     * Format: {@code heroId|equipId1,equipId2,...}
     */
    private void parseHeroCompatibleEquipment(String line, DataManager dm) {
        String[] parts = line.split(PIPE_RE);
        int heroId = Integer.parseInt(parts[0]);

        Hero hero = dm.findHeroById(heroId);
        if (hero == null) {
            return; // orphaned relationship - skip
        }

        if (parts.length > 1 && !parts[1].isEmpty()) {
            for (String token : parts[1].split(COMMA)) {
                hero.addCompatibleEquipment(Integer.parseInt(token.trim()));
            }
        }
    }

    /**
     * Parses an equipment line.
     * Format: {@code id|name|usageCount}
     */
    private void parseEquipment(String line, DataManager dm) {
        String[] parts = line.split(PIPE_RE);
        int id         = Integer.parseInt(parts[0]);
        String name    = parts[1];
        int usageCount = Integer.parseInt(parts[2]);

        Equipment equipment = new Equipment(id, name);
        equipment.setUsageCount(usageCount);
        dm.addEquipment(equipment);
    }

    /**
     * Parses a team line.
     * Format: {@code id|name|playerId1,playerId2,...}
     * The player list sub-field may be empty.
     */
    private void parseTeam(String line, DataManager dm) {
        String[] parts = line.split(PIPE_RE);
        int id      = Integer.parseInt(parts[0]);
        String name = parts[1];

        Team team = new Team(id, name);

        if (parts.length > 2 && !parts[2].isEmpty()) {
            for (String token : parts[2].split(COMMA)) {
                team.addPlayer(Integer.parseInt(token.trim()));
            }
        }

        dm.addTeam(team);
    }

    /**
     * Parses a match-record line.
     * Format: {@code id|teamAId|teamBId|date|result|playerId=heroId,...}
     * The player-hero-picks sub-field may be empty.
     */
    private void parseMatchRecord(String line, DataManager dm) {
        String[] parts = line.split(PIPE_RE);
        // parts: 0=id, 1=teamAId, 2=teamBId, 3=date, 4=result, 5=playerHeroPicks (optional)
        int id          = Integer.parseInt(parts[0]);
        int teamAId     = Integer.parseInt(parts[1]);
        int teamBId     = Integer.parseInt(parts[2]);
        LocalDate date  = LocalDate.parse(parts[3]);
        MatchResult result = MatchResult.valueOf(parts[4]);

        Map<Integer, Integer> playerHeroPicks = new HashMap<>();
        if (parts.length > 5 && !parts[5].isEmpty()) {
            for (String pair : parts[5].split(COMMA)) {
                String[] kv = pair.split(EQ);
                playerHeroPicks.put(Integer.parseInt(kv[0]), Integer.parseInt(kv[1]));
            }
        }

        MatchRecord record = new MatchRecord(id, teamAId, teamBId, date, result, playerHeroPicks);
        dm.addMatchRecord(record);
    }

    // ========================================================================
    //  Mini string-building utilities
    // ========================================================================

    /**
     * Joins an array of objects with the given delimiter character.
     * Each object is converted via {@code String.valueOf()}.
     */
    private static String join(char delimiter, Object... parts) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) sb.append(delimiter);
            sb.append(parts[i]);
        }
        return sb.toString();
    }

    /**
     * Joins a list of integers with commas.
     * Returns an empty string if the list is null or empty.
     */
    private static String joinComma(List<Integer> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(COMMA);
            sb.append(list.get(i));
        }
        return sb.toString();
    }

    /**
     * Joins a map of K→Integer into {@code "k1=v1,k2=v2,..."}.
     * Works with both {@code Map<String, Integer>} (hero baseStats)
     * and {@code Map<Integer, Integer>} (matchRecord playerHeroPicks).
     * Returns an empty string if the map is null or empty.
     */
    private static <K> String joinMap(Map<K, Integer> map) {
        if (map == null || map.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Map.Entry<K, Integer> entry : map.entrySet()) {
            if (!first) sb.append(COMMA);
            sb.append(entry.getKey()).append(EQ).append(entry.getValue());
            first = false;
        }
        return sb.toString();
    }
}
