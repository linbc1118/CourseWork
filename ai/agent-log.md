## Architect Agent

**Main contribution:** Reviewed the class design and suggested improvements. Confirmed the separation of model/service/ui. Recommended adding a method to remove hero references when deleting a hero.

**Human decision:** Accepted the advice; will implement removeHeroFromAllPlayers() in DataManager.

**Related commits:** (f227ee1)

## Implementation Agent - Person class

**Main contribution:** Wrote the complete Person abstract class with proper encapsulation and abstract method.

**Human decision:** Accepted all code. Verified that it compiles.

**Related commits:** (1213e94)

## Implementation Agent - Player class

**Main contribution:** Wrote the Player class extending Person. Flagged a design discrepancy (object-based Map vs ID-based Map) and resolved it via AskUserQuestion. Used Map<Integer, List<Integer>> for hero-equipment mapping per plan.md design rules. Implemented getWinRate() with division-by-zero guard, equipHero() with usage count tracking, and getRoleDisplay().

**Human decision:** Accepted ID-based Map design and Integer teamId. Noted that Hero.java and Equipment.java are needed before compilation.

**Related commits:** (2025141)

## Implementation Agent - Admin class

**Main contribution:** Wrote the Admin class extending Person. Minimal implementation — constructor hardcodes Role.ADMIN, overrides getRoleDisplay() returning "Admin". No extra fields needed.

**Human decision:** Accepted all code. Verified that it compiles.

**Related commits:** (5ee23bc)

## Implementation Agent - Hero class

**Main contribution:** Wrote the Hero class and HeroType enum. Flagged String type vs HeroType enum discrepancy and resolved via AskUserQuestion. Hero class includes defensive copies for baseStats map, duplicate prevention in addCompatibleEquipment(), and ID-based equals/hashCode/toString.

**Human decision:** Accepted HeroType enum and all Hero class design. Both files compile successfully.

**Related commits:** (34c7b60)

## Implementation Agent - Equipment class

**Main contribution:** Wrote the Equipment class with usageCount tracking. Simple design — constructor initializes count to 0, incrementUsageCount() for Player.equipHero() integration, ID-based equals/hashCode/toString.

**Human decision:** Accepted all code. Compiled successfully.

**Related commits:** (f54e03c)

## Implementation Agent - Team class

**Main contribution:** Wrote the Team class with ID-based player references to avoid circular references with Player. addPlayer/removePlayer using integer IDs, ID-based equals/hashCode/toString.

**Human decision:** Accepted all code. Compiled successfully.

**Related commits:** (bfb0c05)

## Implementation Agent - MatchRecord class

**Main contribution:** Wrote the MatchRecord class and MatchResult enum. Flagged int winnerTeamId vs MatchResult enum discrepancy and resolved via AskUserQuestion. Team-match-only design with result from team A's perspective. Defensive copies for playerHeroPicks map, ID-based equals/hashCode/toString.

**Human decision:** Accepted MatchResult enum and all MatchRecord design. Both files compile successfully.

**Related commits:** (311ec16)

## Implementation Agent - DataManager class

**Main contribution:** Wrote the DataManager class (central storage facade). 6 entity collections, 18 CRUD methods (add/remove/update per type), 8 finders (byId for all, byName for Player/Hero/Team). Pure storage — no business logic. Explained Main wiring via constructor injection pattern.

**Human decision:** Accepted all code. Compiled successfully.

**Related commits:** (aa4cf4a)

## Implementation Agent - DataInitializer class

**Main contribution:** Wrote the DataInitializer class with hardcoded seed data meeting all coursework minimums. 1 admin, 10 players, 15 heroes, 20 equipment, 3 teams, 10 match records. Established bidirectional relationships: player hero ownership, hero equipment compatibility, team membership, and match hero picks. File later corrected from src/service/ to src/util/ per plan.md.

**Human decision:** Accepted all seed data and relationships. File location corrected.

**Related commits:** (37ca0e4)

## Implementation Agent - AuthenticationService class

**Main contribution:** Wrote the AuthenticationService class for login/logout session management. Searches admins first then players for credential matching. Provides isAdmin() via instanceof check and isLoggedIn() for permission gating. Uses actual DataManager method names.

**Human decision:** Accepted all code. Compiled successfully.

**Related commits:** (29b1027)

## Implementation Agent - SearchService class

**Main contribution:** Wrote the SearchService class with case-insensitive entity lookup. searchTeamByIdOrName() uses try/catch on Integer.parseInt() for ID-or-name disambiguation. Private normalize() helper for lowercase comparison. Null-safe throughout.

**Human decision:** Accepted all code. Compiled successfully.

**Related commits:** (da0cabd)

## Implementation Agent - RankingService class

**Main contribution:** Wrote the RankingService class with equipment ranking by usage count and player leaderboards by winRate/level/matches. Uses Comparator chain with two-level tie-breaking per plan.md design rules. Returns topN results via subList.

**Human decision:** Accepted all code. Compiled successfully.

**Related commits:** (0a0df4d)

## Implementation Agent - MatchHistoryService class

**Main contribution:** Wrote the MatchHistoryService class with player/team match history lookup and formatting. Filters by playerHeroPicks key presence for players, teamAId/teamBId for teams. Sorts by date descending with subList limiting. formatMatchRecord() resolves IDs to names for readable output.

**Human decision:** Accepted all code. Compiled successfully.

**Related commits:** (7364f04)

## Testing/Reviewer Agent - RankingService

**Main contribution:** Reviewed RankingService sorting and tie-breaking logic. Found a critical bug: `.reversed()` at the end of a comparator chain reverses the entire chain, not just the last `thenComparing`. In `byWinRate()`, this causes winRate to sort ASC instead of DESC when tie-breaking on level. Same issue in `byLevel()` and `byMatches()`. Also noted: unused `java.util.stream.Collectors` import, default switch branch silently returning unsorted results (should throw or log), and missing null/negative guard for topN.

Recommended fix: use `thenComparing(Comparator.comparingInt(...).reversed())` instead of chaining `.reversed()` on the whole chain.

**Human decision:** Accepted all fixes. Applied: fixed .reversed() chaining bug in all 3 comparators, removed unused Collectors import, added null guard + IllegalArgumentException for unknown metric, added negative topN guard, wrapped subList result in new ArrayList to prevent serialization issues.

**Related commits:** (39d4f97)

## Implementation Agent - InputHelper class

**Main contribution:** Wrote the InputHelper utility class with safe console input. Static Scanner reuse, loop-until-valid pattern for readInt/readIntInRange/readNonEmptyString, NumberFormatException handling, range validation.

**Human decision:** Accepted all code. Compiled successfully.

**Related commits:** (7e21de4)

## Implementation Agent - MenuView class

**Main contribution:** Wrote the MenuView class (main UI entry point). Login loop with exit condition, role-based dispatch to AdminView/PlayerView, logout returns to login screen. Uses InputHelper for input and constructor injection for all 5 services.

**Human decision:** Accepted all code. Will compile once AdminView and PlayerView are implemented.

**Related commits:** (eec4606)

## Implementation Agent - PlayerView class

**Main contribution:** Wrote the PlayerView class with 9 fully implemented menu options. Profile display, hero/equipment listing with ID-to-name resolution, match history with formatting, hero/team search, equipment ranking, leaderboard with metric selection, and password editing. Looping menu with InputHelper and logout.

**Human decision:** Accepted all code. Compiled successfully.

**Related commits:** (ae63331)

## Implementation Agent - PlayerView.searchHero() enhancement

**Main contribution:** Added owners list to hero search. Iterates all players checking heroEquipmentMap for the searched hero ID.

**Human decision:** Accepted.

**Related commits:** (稍后填写)