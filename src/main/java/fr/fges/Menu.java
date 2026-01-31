package fr.fges;

import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.DayOfWeek;

public class Menu {

    // 1. On stocke le scanner en attribut pour ne pas le recréer à chaque fois
    private final Scanner scanner;
    
    private final GameRepository repository;
    private final GameStorage storage;
    private final GameDisplay display;
    private final GameRecommender recommender;

    private void suggestGames() {

        // On vérifie d'abord s'il y a des jeux
        if (repository.isEmpty()) {
            System.out.println("Aucun jeu disponible pour le moment !");
            return; // On s'arrête là, on ne descend pas plus bas
        }
        System.out.println("Combien de jeux voulez-vous pour votre week-end ?");

        // 1. On récupère le nombre saisi par l'utilisateur
        try {
            int count = Integer.parseInt(scanner.nextLine());

            // 2. On demande au repository de nous trouver 'count' jeux au hasard
            List<BoardGame> selection = repository.getRandomGames(count);

            // 3. On affiche le résultat
            System.out.println("Voici votre sélection :");
            for (BoardGame game : selection) {
                System.out.println(game);
            }

        } catch (NumberFormatException e) {
            System.out.println("Merci de saisir un nombre valide.");
        }
    }

    // Méthode pour savoir si on est le week-end
    private boolean isWeekend() {
        LocalDate today = LocalDate.now();
        DayOfWeek day = today.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }

    public Menu(ApplicationContext context, Scanner scanner) {
        this.scanner = scanner;
        this.repository = context.getRepository();
        this.storage = context.getStorage();
        this.display = context.getDisplay();
        this.recommender = context.getRecommender();
    }

    // Pas de changement ici
    public String getUserInput(String prompt) {
        // No new line for this one
        System.out.printf("%s: ", prompt);
        // Read input for the keyboard
        return scanner.nextLine();
    }

    // Pas de changement ici
    public void displayMainMenu() {
        System.out.println("=== Board Game Collection ===");

        int num = 1;
        System.out.println(num++ + ". Add Board Game");
        System.out.println(num++ + ". Remove Board Game");

        // On vérifie si c'est le week-end
        if (isWeekend()) {
            System.out.println(num++ + ". Suggest a weekend selection");
        }
        System.out.println(num++ + ". List All Board Games");
        System.out.println(num++ + ". Recommend Game");
        System.out.println(num++ + ". Exit");
        System.out.println("Please select an option (1-6):");
    }

    public void handleMenu() {
        while (true) {
            displayMainMenu();
            String input = scanner.nextLine();

            // On sépare la logique selon si c'est le week-end ou non
            // pour que les numéros correspondent toujours à l'affichage.
            if (isWeekend()) {
                handleWeekendMenu(input);
            } else {
                handleWeekdayMenu(input);
            }
        }
    }

    // Logique Weekend
    private void handleWeekendMenu(String choice) {
        switch (choice) {
            case "1" -> addGame();
            case "2" -> removeGame();
            case "3" -> suggestGames();
            case "4" -> listAllGames();
            case "5" -> recommendGame();
            case "6" -> exit();
            default -> System.out.println("Invalid choice. Please select a valid option.");
        }
    }

    // Logique semaine
    private void handleWeekdayMenu(String choice) {
        switch (choice) {
            case "1" -> addGame();
            case "2" -> removeGame();
            case "3" -> listAllGames();
            case "4" -> recommendGame();
            case "5" -> exit();
            default -> System.out.println("Invalid choice. Please select a valid option.");
        }
    }

    public void addGame() {
        System.out.println("--- Add a new Game ---");
        String title = getUserInput("Title");

        // On vérifie si le jeu existe avant de l'ajouter
        if (repository.exists(title)) {
            System.out.println("Ce jeu existe déjà");
            return;
        }

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
                // On délègue l'affichage à l'objet 'display' pour respecter le SRP (le Menu ne doit pas savoir comment formater)
                System.out.println(display.formatGameDisplay(game));
            }
        }
    }

    public void recommendGame() {
        System.out.println("--- Recommend a Game ---");
        
        int playerCount = 0;
        try {
            playerCount = Integer.parseInt(getUserInput("How many players?"));
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid number.");
            return;
        }

        BoardGame recommended = recommender.recommendGame(playerCount);
        
        if (recommended != null) {
            System.out.printf("Recommended game: \"%s\" (%d-%d players, %s)%n",
                    recommended.title(),
                    recommended.minPlayers(),
                    recommended.maxPlayers(),
                    recommended.category());
        } else {
            System.out.printf("No games available for %d players.%n", playerCount);
        }
    }

    public void exit() {
        System.out.println("Exiting the application. Goodbye!");
        System.exit(0);
    }

//    public void handleMenu() {
//        while (true) {
//            displayMainMenu();
//            String choice = scanner.nextLine();
//
//            switch (choice) {
//                case "1" -> addGame();
//                case "2" -> removeGame();
//                case "3" ->suggestGames();
//                case "4" -> listAllGames();
//                case "5" -> recommendGame();
//                case "6" -> exit();
//                default -> System.out.println("Invalid choice. Please select a valid option.");
//            }
//        }
//    }
}
