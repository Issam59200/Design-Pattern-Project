package fr.fges.ui;

/**
 * Action: Annuler la dernière action.
 * Disponible en semaine et le weekend.
 */
public class UndoMenuAction implements MenuAction {
    private final GameController controller;

    public UndoMenuAction(GameController controller) {
        this.controller = controller;
    }

    @Override
    public String label() {
        return "Undo Last Action";
    }

    @Override
    public void execute() {
        controller.undoLastAction();
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
