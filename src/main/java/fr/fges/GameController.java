package fr.fges;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class GameController {

    private final GameRepository repository;
    private final GameStorage storage;
    private final GameDisplay display;
    private final GameRecommender recommender;
    private final InputHandler inputHandler;

    public GameController(GameRepository repository, GameStorage storage, GameDisplay display, GameRecommender recommender, InputHandler inputHandler) {
        this.repository = repository;
        this.storage = storage;
        this.display = display;
        this.recommender = recommender;
        this.inputHandler = inputHandler;

    }

    // Méthode pour savoir si on est le week-end
    private boolean isWeekend() {
        LocalDate today = LocalDate.now();
        DayOfWeek day = today.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }

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

        repository.addGame(game);

        storage.saveToFile(repository.getGames());

        System.out.println("Board game added successfully.");
    }

    public void removeGame() {
        System.out.println("--- Remove a Game ---");
        String title = inputHandler.readString("Title of game to remove");

        // Recherche du jeu (instensible à la classe)
        BoardGame gameToRemove = repository.getGames().stream()
                .filter(g -> g.title().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);

        if (gameToRemove != null) {
            repository.removeGame(gameToRemove);
            storage.saveToFile(repository.getGames());
            System.out.println("Board game removed successfully.");
        } else {
            System.out.println("No board game found with that title.");
        }
    }

    public void listAllGames() {
        System.out.println("--- List of Games ---");

        List<BoardGame> sortedGames = repository.getSortedGames();

        if (sortedGames.isEmpty()) {
            System.out.println("No board games in collection.");
        } else {
            for (BoardGame game : sortedGames) {
                // On délègue l'affichage à l'objet 'display' pour respecter le SRP (le Menu ne doit pas savoir comment formater)
                System.out.println(display.formatGameDisplay(game));
            }
        }
    }

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

    public void suggestGames() {

        // On vérifie d'abord s'il y a des jeux
        if (repository.isEmpty()) {
            System.out.println("Aucun jeu disponible pour le moment !");
            return; // On s'arrête là, on ne descend pas plus bas
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

    public void exit() {
        System.out.println("Exiting the application. Goodbye!");
        System.exit(0);
    }
}
