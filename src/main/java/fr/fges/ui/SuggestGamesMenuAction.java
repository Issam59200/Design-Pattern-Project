package fr.fges.ui;

/**
 * Action: Suggérer une sélection de jeux pour le weekend.
 * Disponible UNIQUEMENT le weekend.
 */
public class SuggestGamesMenuAction implements MenuAction {
    private final GameController controller;

    public SuggestGamesMenuAction(GameController controller) {
        this.controller = controller;
    }

    @Override
    public String label() {
        return "Suggest Games for Weekend";
    }

    @Override
    public void execute() {
        controller.suggestGames();
    }

    @Override
    public boolean availableOnWeekday() {
        return false;
    }

    @Override
    public boolean availableOnWeekend() {
        return true;
    }
}
