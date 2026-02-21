package fr.fges;

/**
 * Responsable de l'exécution des matchs d'un tournoi.
 * Respecte le SRP : ne s'occupe que du déroulement des matchs.
 */
public class TournamentRunner {
    private final TournamentDisplay tournamentDisplay;
    private final InputHandler inputHandler;

    public TournamentRunner(TournamentDisplay tournamentDisplay, InputHandler inputHandler) {
        this.tournamentDisplay = tournamentDisplay;
        this.inputHandler = inputHandler;
    }

    /**
     * Exécute tous les matchs du tournoi un par un.
     */
    public void run(Tournament tournament) {
        System.out.println("\n=== Début du tournoi : " + tournament.getGame().title() + " ===\n");

        while (!tournament.isFinished()) {
            Match match = tournament.getNextMatch();
            if (match == null) break;

            tournamentDisplay.displayMatchHeader(
                    tournament.getCurrentMatchNumber(),
                    tournament.getTotalMatches(),
                    match);

            System.out.println("1. " + match.player1() + " gagne");
            System.out.println("2. " + match.player2() + " gagne");

            int winner = inputHandler.readIntInRange("Résultat", 1, 2);
            String winnerName = (winner == 1) ? match.player1() : match.player2();

            tournament.registerResult(match, winnerName);
            System.out.println(winnerName + " remporte le match!\n");
        }
    }
}
