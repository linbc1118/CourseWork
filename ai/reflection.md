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
(To be filled at the end of the project)