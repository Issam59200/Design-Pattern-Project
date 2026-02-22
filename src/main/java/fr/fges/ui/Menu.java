package fr.fges.ui;

/**
 * Menu principal de l'application.
 * Utilise MenuRegistry pour afficher et dispatcher les actions.
 * Pas de switch cases — chaque action a sa propre classe.
 */
public class Menu {
    private final InputHandler inputHandler;
    private final MenuRegistry registry;

    public Menu(GameController controller, InputHandler inputHandler, boolean isWeekend) {
        this.inputHandler = inputHandler;
        this.registry = new MenuRegistry(isWeekend);

        // Enregistrer toutes les actions (dans un ordre fixe)
        registry.register("1", new AddGameMenuAction(controller));
        registry.register("2", new RemoveGameMenuAction(controller));
        registry.register("3", new ListAllGamesMenuAction(controller));
        registry.register("4", new RecommendGameMenuAction(controller));
        registry.register("5", new SuggestGamesMenuAction(controller));
        registry.register("6", new UndoMenuAction(controller));
        registry.register("7", new GamesForXPlayersMenuAction(controller));
        registry.register("8", new ExitMenuAction(controller));
    }

    public void run() {
        while (true) {
            registry.displayMenu();
            String choice = inputHandler.readMenuChoice();

            try {
                int choiceInt = Integer.parseInt(choice);
                if (!registry.execute(choiceInt)) {
                    System.out.println("Invalid option. Please try again.\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Please try again.\n");
            }
        }
    }

    /**
     * Enregistre dynamiquement une nouvelle action de menu.
     * Utilisé pour ajouter des actions optionnelles comme le tournoi.
     */
    public void registerAction(String key, MenuAction action) {
        registry.register(key, action);
    }
}
