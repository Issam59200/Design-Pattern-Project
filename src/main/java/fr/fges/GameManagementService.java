package fr.fges;

import java.util.Optional;

/**
 * Service de gestion des jeux (ajout/suppression).
 * Responsable de la création et suppression des jeux dans la collection.
 */
public class GameManagementService {
    private final GameRepository repository;
    private final GameStorage storage;
    private final String storageFile;
    private final InputHandler inputHandler;
    private final CommandManager commandManager;

    public GameManagementService(GameRepository repository, GameStorage storage, String storageFile,
                                 InputHandler inputHandler, CommandManager commandManager) {
        this.repository = repository;
        this.storage = storage;
        this.storageFile = storageFile;
        this.inputHandler = inputHandler;
        this.commandManager = commandManager;
    }

    /**
     * Ajoute un nouveau jeu à la collection.
     * Demande les informations à l'utilisateur et crée le jeu.
     */
    public void addGame() {
        System.out.println("--- Add a new Game ---");
        String title = inputHandler.readString("Title");

        // On vérifie si le jeu existe avant de l'ajouter
        if (repository.exists(title)) {
            System.out.println("Ce jeu existe déjà");
            return;
        }

        // Lecture des nombres avec gestion d'erreur propre
        Optional<Integer> minPlayers = inputHandler.readInt("Minimum Players");
        if (minPlayers.isEmpty()) {
            System.out.println("Error: Please enter a valid number for minimum players.");
            return;
        }

        Optional<Integer> maxPlayers = inputHandler.readInt("Maximum Players");
        if (maxPlayers.isEmpty()) {
            System.out.println("Error: Please enter a valid number for maximum players.");
            return;
        }

        String category = inputHandler.readString("Category");

        BoardGame game = new BoardGame(title, minPlayers.get(), maxPlayers.get(), category);
        Command command = new AddGameCommand(repository, storage, storageFile, game);
        commandManager.executeCommand(command);

        System.out.println("Board game added successfully.");
    }

    /**
     * Retire un jeu de la collection.
     * Demande le titre du jeu à supprimer.
     */
    public void removeGame() {
        System.out.println("--- Remove a Game ---");
        String title = inputHandler.readString("Title of game to remove");

        // Recherche du jeu (insensible à la casse)
        BoardGame gameToRemove = repository.getGames().stream()
                .filter(g -> g.title().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);

        if (gameToRemove != null) {
            Command command = new RemoveGameCommand(repository, storage, storageFile, gameToRemove);
            commandManager.executeCommand(command);
            System.out.println("Board game removed successfully.");
        } else {
            System.out.println("No board game found with that title.");
        }
    }
}
