package fr.fges;

import java.util.List;
import java.util.Scanner;

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

    private static ApplicationContext initializeApplication(String storageFile) {
        // Créer les instances de nos nouvelles classes
        GameRepository repository = new GameRepository();
        GameStorage storage = new GameStorage(storageFile);
        GameDisplay display = new GameDisplay();
        
        // Charger les jeux depuis le fichier
        List<BoardGame> loadedGames = storage.loadFromFile();
        
        // Ajouter les jeux chargés dans le repository
        for (BoardGame game : loadedGames) {
            repository.addGame(game);
        }
        
        System.out.println("Using storage file: " + storageFile);
        
        // Retourner un objet qui contient tout (pour passer au Menu)
        return new ApplicationContext(repository, storage, display);
    }

    private static void runApplication(ApplicationContext context) {
        Scanner scanner = new Scanner(System.in);
        Menu menu = new Menu(context, scanner);
        menu.handleMenu();
    }

    // Méthode principale simplifiée qui orchestre les autres méthodes
    public static void main(String[] args) {
        validateArguments(args);
        
        String storageFile = args[0];
        
        if (!isValidFileExtension(storageFile)) {
            handleInvalidExtension();
        }

        ApplicationContext context = initializeApplication(storageFile);
        runApplication(context);
    }
}

class ApplicationContext {
    private final GameRepository repository;
    private final GameStorage storage;
    private final GameDisplay display;
    
    public ApplicationContext(GameRepository repository, GameStorage storage, GameDisplay display) {
        this.repository = repository;
        this.storage = storage;
        this.display = display;
    }
    
    public GameRepository getRepository() {
        return repository;
    }
    
    public GameStorage getStorage() {
        return storage;
    }
    
    public GameDisplay getDisplay() {
        return display;
    }
}