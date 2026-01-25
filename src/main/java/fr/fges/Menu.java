package fr.fges;

import java.util.List;
import java.util.Scanner;

public class Menu {

    // 1. On stocke le scanner en attribut pour ne pas le recréer à chaque fois
    private final Scanner scanner;

    // 2. Constructeur : on initialise le scanner une seule fois
    public Menu() {
        this.scanner = new Scanner(System.in);
    }

    // 3. Méthode utilitaire interne (plus de static)
    public static String getUserInput(String prompt) {
        // No new line for this one
        System.out.printf("%s: ", prompt);
        // Read input for the keyboard
        return scanner.nextLine();
    }

    public static void displayMainMenu() {
        String menuText = """
                === Board Game Collection ===
                1. Add Board Game
                2. Remove Board Game
                3. List All Board Games
                4. Exit
                Please select an option (1-4):
                """;

        System.out.println(menuText);
    }

    public void addGame() {
        System.out.println("--- Add a new Game ---");
        String title = getUserInput("Title");

        // Petit nettoyage : gestion d'erreur simple si l'utilisateur ne rentre pas un nombre
        int minPlayers = 0;
        int maxPlayers = 0;
        try {
            minPlayers = Integer.parseInt(getUserInput("Minimum Players"));
            maxPlayers = Integer.parseInt(getUserInput("Maximum Players"));
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter valid numbers for players.");
            return;
        }

        String category = getUserInput("Category");

        BoardGame game = new BoardGame(title, minPlayers, maxPlayers, category);

        GameCollection.addGame(game);
        System.out.println("Board game added successfully.");
    }

    public void removeGame() {
        System.out.println("--- Remove a Game ---");
        String title = getUserInput("Title of game to remove");

        // Utilisation de getGames() du binôme
        List<BoardGame> games = GameCollection.getGames();

        // Recherche améliorée (plus lisible)
        BoardGame gameToRemove = games.stream()
                .filter(g -> g.title().equalsIgnoreCase(title)) // ignoreCase est plus sympa pour l'utilisateur
                .findFirst()
                .orElse(null);
            if (gameToRemove != null) {
                GameCollection.removeGame(gameToRemove);
                System.out.println("Board game removed successfully.");
            } else {
                System.out.println("No board game found with that title.");
            }
    }

    public void listAllGames() {
        System.out.println("--- List of Games ---");

        // IMPORTANT : C'est ici que tu reprends la responsabilité de l'affichage !
        // Au lieu d'appeler GameCollection.viewAllGames(), tu récupères les données brutes
        List<BoardGame> sortedGames = GameCollection.getSortedGames();

        if (sortedGames.isEmpty()) {
            System.out.println("No board games in collection.");
        } else {
            for (BoardGame game : sortedGames) {
                // Tu gères le formatage ici (Vue), laissant GameCollection gérer les données (Modèle)
                System.out.printf("- %s (%d-%d players) [%s]%n",
                        game.title(), game.minPlayers(), game.maxPlayers(), game.category());
            }
        }
    }

    public void exit() {
        System.out.println("Exiting the application. Goodbye!");
        System.exit(0);
    }

    public void handleMenu() {
        while (true) {
            displayMainMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> addGame();
                case "2" -> removeGame();
                case "3" -> listAllGames();
                case "4" -> exit();
                default -> System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }
}
