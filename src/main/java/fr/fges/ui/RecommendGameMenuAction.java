package fr.fges.ui;

/**
 * Action: Recommander un jeu en fonction du nombre de joueurs.
 * Disponible en semaine.
 */
public class RecommendGameMenuAction implements MenuAction {
    private final GameController controller;

    public RecommendGameMenuAction(GameController controller) {
        this.controller = controller;
    }

    @Override
    public String label() {
        return "Recommend Game";
    }

    @Override
    public void execute() {
        controller.recommendGame();
    }

    @Override
    public boolean availableOnWeekday() {
        return true;
    }

    @Override
    public boolean availableOnWeekend() {
        return false;
    }
}
