
# Plan for Honor of Kings Information System

## 1. Project Goal
A console-based Java system to manage players, heroes, equipment, teams, and match records for Honor of Kings.
Two types of users: Admin (full control) and Player (view and edit own info).

## 2. Requirement Analysis
All required features from section 5 of the coursework will be implemented:
- Player lookup by name, show team, level, owned heroes and their equipment.
- Team overview by ID/name, show members, average level, total matches, win rate, top player.
- Hero details by name, show type, base stats, compatible equipment, owners.
- Equipment statistics: rank by usage count.
- Match history for player/team: last N matches with opponent, date, result, heroes picked.
- Leaderboard: top players by win rate, level, matches. Ties broken by ID.
- Data management: Admin can add/edit/delete all entities; Player can edit own basic info.
- Authentication: login/logout with Admin and Player roles.

## 3. Java Concepts Used
- Inheritance: Person -> Player, Admin; MatchRecord -> TeamMatchRecord, SoloMatchRecord
- Polymorphism: method overriding (getRoleDisplay, getMatchDescription)
- Collections: List, Map for storing data and relationships
- Exception handling: input validation in InputHelper
- File I/O: not in base version (will be extra credit)
- Enums: HeroType, MatchResult, Role

## 4. Class Design

### 4.1 Enums
- **HeroType**: MAGE, ASSASSIN, TANK, WARRIOR, MARKSMAN, SUPPORT
- **MatchResult**: WIN, LOSS, DRAW
- **Role**: ADMIN, PLAYER

### 4.2 Model Classes
- **Person** (abstract): id, name, username, password, role (Role enum)
  - **Player** extends Person: level, teamId (int, references Team by ID), winCount, totalMatches, heroOwnerships (Map<Integer, List<Integer>> — heroId → list of equipmentIds)
  - **Admin** extends Person: (no extra fields needed; role distinction via Role enum + inheritance for coursework requirement)
- **Hero**: id, name, heroType (HeroType), attack, defense, hp
- **Equipment**: id, name, description, compatibleHeroTypes (Set<HeroType>), statBonus
- **Team**: id, name, memberIds (List<Integer> — references Player by ID), totalMatches, winCount
- **MatchRecord** (abstract): id, date (LocalDate), result (MatchResult)
  - **TeamMatchRecord** extends MatchRecord: teamAId, teamBId, teamAHeroPicks (Map<Integer, Integer> — playerId → heroId), teamBHeroPicks
  - **SoloMatchRecord** extends MatchRecord: playerAId, playerBId, playerAHeroId, playerBHeroId

### 4.3 Service Layer
- **DataManager** (singleton, pure storage facade): owns all collections (List/Map per entity type), exposes simple add/get/remove/getAll/update operations. No business logic. Per-type auto-increment ID counters (nextPlayerId, nextHeroId, etc.).
- **AuthenticationService**: login(username, password), logout(), getCurrentUser(), getCurrentRole()
- **PlayerService**: findPlayerByName, findPlayerById, addPlayer, updatePlayer, deletePlayer, updateOwnProfile, getPlayerHeroes, addHeroToPlayer, removeHeroFromPlayer, equipItemToHero
- **HeroService**: findHeroByName, findHeroById, addHero, updateHero, deleteHero, getCompatibleEquipment, getHeroOwners
- **EquipmentService**: findEquipmentByName, findEquipmentById, addEquipment, updateEquipment, deleteEquipment, getUsageCount, getTopEquipment
- **TeamService**: findTeamByName, findTeamById, addTeam, updateTeam, deleteTeam, getAverageLevel, getWinRate, getTopPlayer, transferPlayer (atomic team move)
- **MatchService**: addMatchRecord, getMatchesForPlayer, getMatchesForTeam, getLastNMatches
- **RankingService**: getTopPlayersByWinRate, getTopPlayersByLevel, getTopPlayersByMatches
- **SearchService** (facade/coordinator): delegates to domain services above for cross-entity queries

### 4.4 Util
- **InputHelper**: safe console input with type validation and range checking
- **DataInitializer**: hardcoded test data (see Section 6)

### 4.5 UI
- **MenuView**: main loop, login menu, role-based routing
- **AdminView**: admin-specific menus (CRUD operations for all entities)
- **PlayerView**: player-specific menus (view info, edit own profile, view heroes/equipment)

## 5. UML Draft
(I will add a text-based diagram later)

## 6. Data Design
All data stored in memory using Java collections inside DataManager. Initial data will be created in DataInitializer with at least:
- 10 players (mix of Admin and Player roles)
- 15 heroes (spanning all HeroType values)
- 20 equipment items (each with compatibleHeroTypes defined)
- 3 teams
- 10 match records (mix of TeamMatchRecord and SoloMatchRecord)

### 6.1 Key Design Rules
- **equals()/hashCode()**: ALL model classes override these using ONLY the immutable `int id` field. Name, stats, and other mutable fields must NOT participate in equality or hashing. This prevents broken Map entries when entities are edited.
- **toString()**: Never include full child objects. Use IDs instead (e.g., `"Player{id=1, name='Zhang Fei', teamId=3}"`). This prevents StackOverflowError from circular references (Team ↔ Player).
- **ID generation**: Per-type auto-increment counters in DataManager. IDs are unique within each entity type (Player #1 and Hero #1 can coexist — lookup methods know the target type).
- **"Top Player" metric**: Defined as highest win rate, tie-broken by highest level, then by lowest ID. Computed on-the-fly (not stored) since it changes with each match.

### 6.2 Referential Integrity (Delete Cascade Rules)
- **Delete Hero**: Block if owned by any Player. Admin must unassign first.
- **Delete Equipment**: Block if used in any Player's equipment list. Admin must unequip first.
- **Delete Player**: Remove from Team's memberIds list. Their MatchRecords are preserved (marked with deleted player ID — historical data).
- **Delete Team**: Set teamId to 0 (no team) for all members. TeamMatchRecords involving this team are preserved.
- **Move Player between Teams**: Must go through TeamService.transferPlayer() which updates both teams' memberIds AND the player's teamId atomically. Never set these fields directly from outside.

### 6.3 Equipment: Compatibility vs. Ownership
These are two SEPARATE relationships:
- **Compatibility** (static, defined once): Equipment.compatibleHeroTypes (Set<HeroType>) — which hero classes can use this equipment.
- **Ownership** (dynamic, per player): Player.heroOwnerships (Map<Integer, List<Integer>>) — which equipment a player has on which hero.

## 7. AI Usage Plan
I will use Claude Code with three agent roles:
- Architect Agent: for class design and planning
- Implementation Agent: for writing specific methods
- Testing/Reviewer Agent: for code review and bug finding

I will record all prompts in ai/prompts.md and agent logs in ai/agent-log.md.

## 8. Prompt Strategy
I will write specific, small prompts (not "write my whole project"). I will ask for one class or method at a time. I will always verify AI output before using.

## 9. Development Timeline
- Stage 1: enums + model classes + DataInitializer
- Stage 2: DataManager + domain services (Authentication, Player, Hero, Equipment, Team, Match, Ranking)
- Stage 3: UI (MenuView, PlayerView, AdminView) + InputHelper
- Stage 4: testing and documentation

## 10. Testing Plan
I will write at least 10 manual test cases in docs/test-cases.md covering:
- player lookup (existing and non-existing)
- team overview
- hero details
- equipment ranking
- match history
- leaderboard
- admin add/delete
- login with wrong password

## 11. Risk Analysis
Risk: Player-Hero-Equipment association may be complex.
Mitigation: Use ID-based Map<Integer, List<Integer>> (heroId → equipmentIds) instead of object-keyed Map. All model classes use ID-based equals/hashCode, preventing broken map entries when entities are edited. Equipment compatibility (Set<HeroType>) is stored on Equipment separately from ownership, avoiding conflation of the two relationships.

Risk: Referential integrity with object references.
Mitigation: Adopted prevention-on-delete strategy (block deletion if entity is in use). All relationship mutations go through service methods that update both sides atomically (e.g., TeamService.transferPlayer).

## 12. Final Reflection Placeholder
(To be filled at the end of the project)
