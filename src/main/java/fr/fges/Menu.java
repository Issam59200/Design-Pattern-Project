package fr.fges;


public class Menu {

    // 1. On stocke le scanner en attribut pour ne pas le recréer à chaque fois
    private final GameController controller;
    private final InputHandler inputHandler;

    public Menu(GameController controller, InputHandler inputHandler) {
        this.controller = controller;
        this.inputHandler = inputHandler;
    }

    // Pas de changement ici
    public void displayMainMenu() {
        System.out.println("=== Board Game Collection ===");

        int num = 1;
        System.out.println(num++ + ". Add Board Game");
        System.out.println(num++ + ". Remove Board Game");
        System.out.println(num++ + ". List All Board Games");
        System.out.println(num++ + ". Recommend Game");

        // On vérifie si c'est le week-end
        if (controller.isWeekend()) {
            System.out.println(num++ + ". View Summary (Weekend Special!)");
        }
        System.out.println(num++ + ". Undo Last Action");
        System.out.println(num++ + ". Games for X Players");
        System.out.println(num++ + ". Exit");
        System.out.println("Please select an option:");
    }

    public void run() {
        while (true) {
            displayMainMenu();
            String choice = inputHandler.readMenuChoice();

            if (controller.isWeekend()) {
                handleWeekendMenu(choice);
            } else {
                handleWeekdayMenu(choice);
            }
        }
    }

    // Logique Weekend
    private void handleWeekendMenu(String choice) {
        switch (choice) {
            case "1" -> controller.addGame();
            case "2" -> controller.removeGame();
            case "3" -> controller.listAllGames();
            case "4" -> controller.recommendGame();
            case "5" -> controller.suggestGames();
            case "6" -> controller.undoLastAction();
            case "7" -> controller.gamesForXPlayers();
            case "8" -> controller.exit();
            default -> System.out.println("Invalid choice. Please select a valid option.");
        }
    }

    // Logique semaine
    private void handleWeekdayMenu(String choice) {
        switch (choice) {
            case "1" -> controller.addGame();
            case "2" -> controller.removeGame();
            case "3" -> controller.listAllGames();
            case "4" -> controller.recommendGame();
            case "5" -> controller.undoLastAction();
            case "6" -> controller.gamesForXPlayers();
            case "7" -> controller.exit();
            default -> System.out.println("Invalid choice. Please select a valid option.");
        }
    }
}
