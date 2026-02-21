package fr.fges;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * Contrôleur principal de l'application.
 * Coordonne les services et gère les opérations de haut niveau.
 */
public class GameController {
    private final GameManagementService managementService;
    private final GameQueryService queryService;
    private final CommandManager commandManager;
    private final GameRepository repository;

    public GameController(GameRepository repository, GameStorage storage, String storageFile,
        GameDisplay display, GameRecommender recommender, InputHandler inputHandler) {
        this.repository = repository;
        this.commandManager = new CommandManager();
        this.managementService = new GameManagementService(repository, storage, storageFile, inputHandler, commandManager);
        this.queryService = new GameQueryService(repository, display, recommender, inputHandler);
    }

    // Méthode pour savoir si on est le week-end
    public boolean isWeekend() {
        LocalDate today = LocalDate.now();
        DayOfWeek day = today.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }

    /** Délègue l'ajout de jeu au service de gestion */
    public void addGame() {
        managementService.addGame();
    }

    /** Délègue la suppression de jeu au service de gestion */
    public void removeGame() {
        managementService.removeGame();
    }

    /** Délègue l'affichage de la liste au service de consultation */
    public void listAllGames() {
        queryService.listAllGames();
    }

    /** Délègue la recommandation au service de consultation */
    public void recommendGame() {
        queryService.recommendGame();
    }

    /** Délègue la suggestion au service de consultation */
    public void suggestGames() {
        queryService.suggestGames();
    }

    /** Délègue la recherche par nombre de joueurs au service de consultation */
    public void gamesForXPlayers() {
        queryService.gamesForXPlayers();
    }

    /** Annule la dernière action via le gestionnaire de commandes */
    public void undoLastAction() {
        if (!commandManager.undoLastCommand()) {
            System.out.println("Nothing to undo.");
        }
    }

    public void exit() {
        System.out.println("Exiting the application. Goodbye!");
        System.exit(0);
    }

    /**
     * Accesseur pour le référentiel de jeux.
     * Utilisé pour les fonctionnalités comme les tournois.
     */
    public GameRepository getRepository() {
        return repository;
    }
}
