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
- Inheritance: Person -> Player, Admin
- Polymorphism: method overriding (getRoleDisplay)
- Collections: List, Map for storing data and relationships
- Exception handling: input validation in InputHelper
- File I/O: not in base version (will be extra credit)
- Enums: for HeroType (optional)

## 4. Class Design
- model: Person (abstract), Player, Admin, Hero, Equipment, Team, MatchRecord
- service: DataManager (stores all data), AuthenticationService (login/current user), SearchService, RankingService, MatchHistoryService
- util: InputHelper (safe console input), DataInitializer (hardcoded test data)
- ui: MenuView (main loop), PlayerView, AdminView

## 5. UML Draft
(I will add a text-based diagram later)

## 6. Data Design
All data stored in memory using Java collections. Initial data will be created in DataInitializer with at least:
- 10 players
- 15 heroes
- 20 equipment items
- 3 teams
- 10 match records

## 7. AI Usage Plan
I will use Claude Code with three agent roles:
- Architect Agent: for class design and planning
- Implementation Agent: for writing specific methods
- Testing/Reviewer Agent: for code review and bug finding

I will record all prompts in ai/prompts.md and agent logs in ai/agent-log.md.

## 8. Prompt Strategy
I will write specific, small prompts (not "write my whole project"). I will ask for one class or method at a time. I will always verify AI output before using.

## 9. Development Timeline
- Stage 1: model classes + DataInitializer
- Stage 2: services (Authentication, Search, Ranking, MatchHistory)
- Stage 3: UI (menus, player/admin views)
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
Risk: Player-Hero-Equipment association may be complex. Mitigation: Store a Map<Hero, List<Equipment>> inside Player class.

## 12. Final Reflection Placeholder
# AI Reflection

## 1. Which AI tools or models did you use?
I used Claude Code (Deepseek V4 Pro) as my main AI assistant. I used the same model in three roles: Architect, Implementation, and Reviewer.

## 2. Which prompt was the most useful? Why?
The most useful prompt was the one for implementing DataInitializer. It clearly specified the required data quantities (10 players, 15 heroes, etc.) and relationships. The AI generated a large set of usable test data at once, saving me from manual entry. This prompt taught me how to "guide AI with concrete requirements".

## 3. Which AI-generated suggestion was wrong, incomplete, or misleading?
My original design used `Map<Hero, List<Equipment>>` in the Player class, but the Architect Agent flagged this as fragile (mutable-key hashCode problems) and recommended switching to ID-based keys (`Map<Integer, List<Integer>>`). This was a case where the AI corrected my design rather than the other way around. Also, the AI-generated `FileStorageService` initially had GBK encoding errors from em-dash characters in comments, which were fixed by replacing them with standard ASCII characters.

## 4. How did you check whether AI-generated code was correct?
I used the following methods:
- Checked for compilation errors (red underlines) in IntelliJ IDEA.
- Ran the program and manually tested relevant features.
- For complex logic (e.g., leaderboard sorting), I printed intermediate results and verified them manually.
- Asked the AI to act as a Reviewer Agent to re‑examine the code.

## 5. What bugs did you fix yourself instead of asking AI to fix?
- The shutdown hook: after discovering that data was lost when the program was terminated unexpectedly (e.g., clicking the IDE Stop button instead of typing "exit"), I added a `Runtime.addShutdownHook` to save data on any termination.
- The AI's `RankingService` had a `.reversed()` chaining bug in the comparators — `.reversed()` at the end of the chain reversed ALL tie-breaking levels, not just the last one. This was actually caught by the Reviewer Agent (not me directly), and the AI applied the fix.
- File path issues (relative vs. absolute) – I chose to use `data.txt` in the project root directory for simplicity.

## 6. What Java concept did you understand better after using AI?
I gained a better understanding of:
- How to use Map and List together in the collections framework to express relationships (Player → Hero → Equipment).
- Inheritance and polymorphism: `Person` as an abstract class, with `Player` and `Admin` overriding `getRoleDisplay()`.
- Exception handling: using try‑catch for file I/O and input conversion.

## 7. What Java concept are you still unsure about?
- Advanced generics (e.g., `? extends Person`).
- Multithreading and concurrency (not covered in this project).
- Complex operations with lambda expressions and the Stream API – some AI‑generated code runs but I don’t fully understand it.

## 8. Did AI make the project easier, harder, or both? Explain.
Both. It was easier because I didn’t have to look up syntax from scratch; I could ask the AI to generate boilerplate code and then modify it. It was harder because sometimes the AI’s code contained hidden bugs that required time to understand and fix. Also, recording prompts and agent logs added extra work, but it helped me keep track of what I had asked.

## 9. Which parts of the final project were mainly written by you?
- The first draft of `plan.md` (though I incorporated AI suggestions, the structure and content were my own).
- Some manual test cases (`test-cases.md`).
- The logic that wires services together in the UI menu (the AI generated the framework, but I adjusted the call order).
- Git commit messages and branch management.

## 10. Which parts were mainly generated or heavily assisted by AI?
- Initial versions of all model classes (`Person`, `Player`, `Admin`, `Hero`, `Equipment`, `Team`, `MatchRecord`).
- Most of `DataManager` and `DataInitializer`.
- The four service classes (`AuthenticationService`, `SearchService`, `RankingService`, `MatchHistoryService`).
- The initial structure and many methods of the UI layer (`MenuView`, `PlayerView`, `AdminView`).
- The complete implementation of `FileStorageService` (extra credit).
- Most of the prompt records and agent log format templates.