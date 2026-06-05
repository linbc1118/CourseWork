import service.*;
import ui.MenuView;
import util.DataInitializer;

public class Main {
    public static void main(String[] args) {
        // 1. Create DataManager and initialize with test data
        DataManager dm = new DataManager();
        DataInitializer.initialize(dm);

        // 2. Create services (they all depend on dm)
        AuthenticationService auth = new AuthenticationService(dm);
        SearchService search = new SearchService(dm);
        RankingService ranking = new RankingService(dm);
        MatchHistoryService matchHistory = new MatchHistoryService(dm);

        // 3. Start menu
        MenuView menu = new MenuView(dm, auth, search, ranking, matchHistory);
        menu.start();
    }
}
