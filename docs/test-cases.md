# Test Cases for Honor of Kings System

## TC01: Player View My Profile
- **Function Tested**: Player views own profile
- **Input**: Login as Zhang Fei (player1/pass1) → menu 1
- **Expected Output**: Display name, level, win rate, matches, team.
- **Actual Output**:
- **Result**: Pass
- **Bug Found**: None

## TC02: View My Heroes and Equipment
- **Function Tested**: Player views owned heroes and equipment
- **Input**: Player menu → option 2 (Zhang Fei)
- **Expected Output**: List heroes, each with equipment (may be empty).
- **Actual Output**:
- **Result**: Pass
- **Bug Found**: None

## TC03: Team Overview by ID
- **Function Tested**: Search team by ID
- **Input**: Player menu → option 5 → enter "1" (Team Dragon)
- **Expected Output**: Team details: members, average level, total matches, win rate, top player.
- **Actual Output**:
- **Result**: Pass
- **Bug Found**: None

## TC03: Team Overview by ID
- **Function Tested**: Search team by ID
- **Input**: Player menu → option 5 → enter "1" (Team Dragon)
- **Expected Output**: Team details: members, average level, total matches, win rate, top player.
- **Actual Output**:- **Result**: Pass
- **Bug Found**: None

## TC04: Hero Details by Name
- **Function Tested**: Search hero by name
- **Input**: Player menu → option 4 → enter "Kai"
- **Expected Output**: Hero type, stats, compatible equipment, owners.
- **Actual Output**:- **Result**: Pass
- **Bug Found**: None

## TC05: Equipment Statistics (Ranking)
- **Function Tested**: Equipment ranking by usage count
- **Input**: Player menu → option 6
- **Expected Output**: Equipment sorted descending by usage count.
- **Actual Output**:- **Result**: Pass
- **Bug Found**: None

## TC06: Match History for a Player
- **Function Tested**: View last 5 matches of logged‑in player
- **Input**: Player menu → option 3 (Guan Yu)
- **Expected Output**: 5 recent matches with opponent, date, result, hero picks.
- **Actual Output** (first line):- **Result**: Pass
- **Bug Found**: None

## TC07: Leaderboard by Level
- **Function Tested**: Show top N players sorted by level
- **Input**: Player menu → option 7 → metric "level" → top 3
- **Expected Output**: Top 3 players by level.
- **Actual Output**:- **Result**: Pass
- **Bug Found**: None

## TC08: Player Edit Own Information (Change Password)
- **Function Tested**: Player changes password
- **Input**: Player1 menu → option 8 → current "pass1" → new "pass2"
- **Expected Output**: Success message. Logout and login with new password works.
- **Actual Output**:- **Result**: Pass
- **Bug Found**: None

## TC09: Logout and Re‑login with Different Player
- **Function Tested**: Logout then login as another player
- **Input**: Player1 logs out → login as player2/pass2
- **Expected Output**: Logout message, return to login screen; second login succeeds.
- **Actual Output**:- **Result**: Pass
- **Bug Found**: None

## TC10: Invalid Menu Input Handling
- **Function Tested**: Handle non‑integer menu input
- **Input**: Enter non‑integer (e.g., "abc") at menu prompt
- **Expected Output**: Error message and re‑prompt.
- **Actual Output**:- **Result**: Pass
- **Bug Found**: None

## TC11: Admin Add New Player
- **Function Tested**: Administrator adds a new player
- **Input**: Login as admin (admin/admin123) → Player Management → Add Player → Name="TestUser", username="test123", password="pass123", level=10, matches=0, wins=0
- **Expected Output**: Success message, player appears in list.
- **Actual Output**:- **Result**: Pass
- **Bug Found**: None

## TC12: Admin Delete Player
- **Function Tested**: Administrator deletes a player
- **Input**: Admin menu → Player Management → Delete Player → Player ID = 11
- **Expected Output**: Success message, player removed from list.
- **Actual Output**:- **Result**: Pass
- **Bug Found**: None