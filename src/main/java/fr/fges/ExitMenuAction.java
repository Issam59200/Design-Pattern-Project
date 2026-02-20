package fr.fges;

/**
 * Action: Quitter l'application.
 * Disponible en semaine et le weekend.
 */
public class ExitMenuAction implements MenuAction {
    private final GameController controller;

    public ExitMenuAction(GameController controller) {
        this.controller = controller;
    }

    @Override
    public String label() {
        return "Exit";
    }

    @Override
    public void execute() {
        controller.exit();
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
