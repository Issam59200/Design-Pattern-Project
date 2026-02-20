package fr.fges;

/**
 * Action: Afficher les jeux compatibles avec un nombre de joueurs.
 * Disponible en semaine et le weekend.
 */
public class GamesForXPlayersMenuAction implements MenuAction {
    private final GameController controller;

    public GamesForXPlayersMenuAction(GameController controller) {
        this.controller = controller;
    }

    @Override
    public String label() {
        return "Games for X Players";
    }

    @Override
    public void execute() {
        controller.gamesForXPlayers();
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
