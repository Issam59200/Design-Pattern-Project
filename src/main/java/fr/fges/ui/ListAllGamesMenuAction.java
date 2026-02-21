package fr.fges;

/**
 * Action: Afficher la liste de tous les jeux.
 * Disponible en semaine.
 */
public class ListAllGamesMenuAction implements MenuAction {
    private final GameController controller;

    public ListAllGamesMenuAction(GameController controller) {
        this.controller = controller;
    }

    @Override
    public String label() {
        return "List All Board Games";
    }

    @Override
    public void execute() {
        controller.listAllGames();
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
