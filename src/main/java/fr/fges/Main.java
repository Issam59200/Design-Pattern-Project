package fr.fges;

import java.util.List;
import java.util.Scanner;

import fr.fges.model.BoardGame;
import fr.fges.service.GameRecommender;
import fr.fges.service.TournamentService;
import fr.fges.storage.GameStorage;
import fr.fges.storage.GameStorageFactory;
import fr.fges.tournament.TournamentDisplay;
import fr.fges.ui.GameController;
import fr.fges.ui.GameDisplay;
import fr.fges.ui.InputHandler;
import fr.fges.ui.Menu;
import fr.fges.ui.TournamentAction;

public class Main {
    // Méthode pour valider la présence des arguments - facilite les tests
    private static void validateArguments(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java -jar boardgamecollection.jar <storage-file>");
            System.out.println("Storage file must be .json or .csv");
            System.exit(1);
        }
    }

    // Méthode pour valider l'extension du fichier - facilite les tests
    private static boolean isValidFileExtension(String filename) {
        return filename.endsWith(".json") || filename.endsWith(".csv");
    }

    // Méthode pour gérer une extension invalide - facilite les tests
    private static void handleInvalidExtension() {
        System.out.println("Error: Storage file must have .json or .csv extension");
        System.exit(1);
    }

    // Méthode principale simplifiée qui orchestre les autres méthodes
    public static void main(String[] args) {
        validateArguments(args);
        
        String storageFile = args[0];
        
        if (!isValidFileExtension(storageFile)) {
            handleInvalidExtension();
        }

        // 1. Couche Data : Factory crée le bon DAO selon l'extension
        GameRepository repository = new GameRepository();
        GameStorage storage = GameStorageFactory.create(storageFile);
        GameDisplay display = new GameDisplay();
        GameRecommender recommender = new GameRecommender(repository);

        // 2. Charger les jeux existants
        List<BoardGame> loadedGames = storage.load(storageFile);
        for (BoardGame game : loadedGames) {
            repository.addGame(game);
        }

        System.out.println("Using storage file: " + storageFile);

        // 3. Créer les classes "haut niveau" (interaction utilisateur)
        Scanner scanner = new Scanner(System.in);
        InputHandler inputHandler = new InputHandler(scanner);
        GameController controller = new GameController(repository, storage, storageFile, display, recommender, inputHandler);

        // 4. Créer les services de tournoi
        TournamentService tournamentService = new TournamentService(repository);
        TournamentDisplay tournamentDisplay = new TournamentDisplay();

        // 5. Créer et lancer le menu avec le MenuRegistry
        boolean isWeekend = controller.isWeekend();
        Menu menu = new Menu(controller, inputHandler, isWeekend);
        
        // 6. Enregistrer l'action de tournoi dynamiquement
        TournamentAction tournamentAction = new TournamentAction(tournamentService, tournamentDisplay, inputHandler);
        menu.registerAction("9", tournamentAction);
        
        menu.run();
    }
}

