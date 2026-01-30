package fr.fges;

import java.util.List;
import java.util.Scanner;

public class Menu {

    // 1. On stocke le scanner en attribut pour ne pas le recréer à chaque fois
    private final Scanner scanner;
    
    private final GameRepository repository;
    private final GameStorage storage;
    private final GameDisplay display;

    public Menu(ApplicationContext context, Scanner scanner) {
        this.scanner = scanner;
        this.repository = context.getRepository();
        this.storage = context.getStorage();
        this.display = context.getDisplay();
    }

    // Pas de changement ici
    public String getUserInput(String prompt) {
        // No new line for this one
        System.out.printf("%s: ", prompt);
        // Read input for the keyboard
        return scanner.nextLine();
    }

    // Pas de changement ici
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

        repository.addGame(game);
        
        storage.saveToFile(repository.getGames());
        
        System.out.println("Board game added successfully.");
    }

    public void removeGame() {
        System.out.println("--- Remove a Game ---");
        String title = getUserInput("Title of game to remove");

        List<BoardGame> games = repository.getGames();

        // Recherche améliorée (plus lisible)
        BoardGame gameToRemove = games.stream()
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
