package fr.fges.tournament;

/**
 * Responsable de la sélection du format de tournoi.
 * Utilise le Strategy pattern : retourne la bonne TournamentStrategy.
 */
public class FormatSelector {
    private final InputHandler inputHandler;

    public FormatSelector(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    /**
     * Affiche les formats disponibles et retourne la stratégie choisie.
     */
    public TournamentStrategy selectFormat() {
        System.out.println("\n1. Championship (tous contre tous)");
        System.out.println("2. King of the Hill");

        int choice = inputHandler.readIntInRange("Format", 1, 2);
        return (choice == 1) ? new ChampionshipStrategy() : new KingOfTheHillStrategy();
    }
}
