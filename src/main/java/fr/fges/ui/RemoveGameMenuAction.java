package fr.fges.ui;

/**
 * Action: Retirer un jeu de la collection.
 * Disponible en semaine et le weekend.
 */
public class RemoveGameMenuAction implements MenuAction {
    private final GameController controller;

    public RemoveGameMenuAction(GameController controller) {
        this.controller = controller;
    }

    @Override
    public String label() {
        return "Remove Board Game";
    }

    @Override
    public void execute() {
        controller.removeGame();
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
