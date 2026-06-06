import service.*;
import ui.MenuView;
import util.DataInitializer;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {

    /** Save file name for persisting data between sessions. */
    private static final String SAVE_FILE = "data.txt";

    public static void main(String[] args) {
        // 1. Create core instances
        DataManager dm = new DataManager();
        FileStorageService fileStorage = new FileStorageService();

        // 2. Load persisted data, or seed + save on first run
        File saveFile = new File(SAVE_FILE);
        if (!saveFile.exists()) {
            // First run: seed hardcoded data, then persist immediately
            System.out.println("No save file found. Initializing with default data...");
            DataInitializer.initialize(dm);
            try {
                fileStorage.saveAllData(dm, SAVE_FILE);
                System.out.println("Default data saved to " + SAVE_FILE);
            } catch (IOException e) {
                System.out.println("Warning: Could not save initial data: " + e.getMessage());
            }
        } else {
            // Restore previous session data
            try {
                fileStorage.loadAllData(dm, SAVE_FILE);
                System.out.println("Loaded saved data from " + SAVE_FILE
                        + " (" + dm.getPlayers().size() + " players, "
                        + dm.getHeroes().size() + " heroes, "
                        + dm.getMatchRecords().size() + " matches)");
            } catch (IOException e) {
                System.out.println("Failed to load save file: " + e.getMessage());
                System.out.println("Falling back to default data...");
                DataInitializer.initialize(dm);
            }
        }

        // 3. Create services (all share the same DataManager)
        AuthenticationService auth = new AuthenticationService(dm);
        SearchService search = new SearchService(dm);
        RankingService ranking = new RankingService(dm);
        MatchHistoryService matchHistory = new MatchHistoryService(dm);

        // 4. Register a shutdown hook to save data even if the program is
        //    terminated unexpectedly (Ctrl+C, IDE stop button, etc.).
        //    The 'saved' flag prevents double-saving when exit is clean.
        AtomicBoolean saved = new AtomicBoolean(false);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!saved.get()) {
                try {
                    fileStorage.saveAllData(dm, SAVE_FILE);
                    System.out.println("[shutdown hook] Data saved to " + SAVE_FILE);
                } catch (IOException e) {
                    System.out.println("[shutdown hook] Warning: Failed to save data: " + e.getMessage());
                }
            }
        }, "FileStorageShutdownHook"));

        // 5. Start the login / menu loop (blocks until user exits)
        MenuView menu = new MenuView(dm, auth, search, ranking, matchHistory);
        menu.start();

        // 6. Persist all data on clean exit
        try {
            fileStorage.saveAllData(dm, SAVE_FILE);
            saved.set(true);
            System.out.println("Data saved to " + SAVE_FILE);
        } catch (IOException e) {
            System.out.println("Warning: Failed to save data: " + e.getMessage());
        }
    }
}
