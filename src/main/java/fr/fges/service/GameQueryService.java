package fr.fges.service;

import java.util.List;
import java.util.Optional;

/**
 * Service de consultation des jeux.
 * Responsable des opérations de recherche, recommandation et affichage.
 */
public class GameQueryService {
    private final GameRepository repository;
    private final GameDisplay display;
    private final GameRecommender recommender;
    private final InputHandler inputHandler;

    public GameQueryService(GameRepository repository, GameDisplay display, 
                           GameRecommender recommender, InputHandler inputHandler) {
        this.repository = repository;
        this.display = display;
        this.recommender = recommender;
        this.inputHandler = inputHandler;
    }

    /**
     * Affiche tous les jeux de la collection triés par ordre alphabétique.
     */
    public void listAllGames() {
        System.out.println("--- List of Games ---");

        List<BoardGame> sortedGames = repository.getSortedGames();

        if (sortedGames.isEmpty()) {
            System.out.println("No board games in collection.");
        } else {
            for (BoardGame game : sortedGames) {
                System.out.println(display.formatGameDisplay(game));
            }
        }
    }

    /**
     * Recommande un jeu en fonction du nombre de joueurs.
     */
    public void recommendGame() {
        System.out.println("--- Recommend a Game ---");

        Optional<Integer> playerCount = inputHandler.readInt("How many players?");
        if (playerCount.isEmpty()) {
            System.out.println("Error: Please enter a valid number.");
            return;
        }

        BoardGame recommended = recommender.recommendGame(playerCount.get());

        if (recommended != null) {
            System.out.printf("Recommended game: \"%s\" (%d-%d players, %s)%n",
                    recommended.title(),
                    recommended.minPlayers(),
                    recommended.maxPlayers(),
                    recommended.category());
        } else {
            System.out.printf("No games available for %d players.%n", playerCount.get());
        }
    }

    /**
     * Suggère une sélection aléatoire de jeux pour le week-end.
     */
    public void suggestGames() {
        // On vérifie d'abord s'il y a des jeux
        if (repository.isEmpty()) {
            System.out.println("Aucun jeu disponible pour le moment !");
            return;
        }
        System.out.println("Combien de jeux voulez-vous pour votre week-end ?");

        Optional<Integer> count = inputHandler.readInt("");
        if (count.isEmpty()) {
            System.out.println("Merci de saisir un nombre valide.");
            return;
        }

        List<BoardGame> selection = repository.getRandomGames(count.get());

        System.out.println("Voici votre sélection :");
        for (BoardGame game : selection) {
            System.out.println(display.formatGameDisplay(game));
        }
    }

    /**
     * Affiche tous les jeux compatibles avec un nombre de joueurs donné.
     */
    public void gamesForXPlayers() {
        System.out.println("--- Games for X Players ---");

        Optional<Integer> playerCount = inputHandler.readInt("Number of players");
        if (playerCount.isEmpty()) {
            System.out.println("Error: Please enter a valid number.");
            return;
        }

        List<BoardGame> compatibleGames = repository.getGamesForPlayerCount(playerCount.get());

        if (compatibleGames.isEmpty()) {
            System.out.printf("No games found for %d players.%n", playerCount.get());
        } else {
            System.out.printf("Games for %d players:%n", playerCount.get());
            for (BoardGame game : compatibleGames) {
                System.out.println(display.formatGameDisplay(game));
            }
        }
    }
}
