package fr.fges.ui;

/**
 * Action: Ajouter un jeu à la collection.
 * Disponible en semaine et le weekend.
 */
public class AddGameMenuAction implements MenuAction {
    private final GameController controller;

    public AddGameMenuAction(GameController controller) {
        this.controller = controller;
    }

    @Override
    public String label() {
        return "Add Board Game";
    }

    @Override
    public void execute() {
        controller.addGame();
    }

    @Override
    public boolean availableOnWeekday() {
        return true;
    }

    @Override
    public boolean availableOnWeekend() {
        return true;
    }
}
