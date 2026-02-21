package fr.fges;

import java.util.List;

/**
 * Responsable de la sélection d'un jeu pour le tournoi.
 * Respecte le SRP : une seule responsabilité (choisir un jeu).
 */
public class GameSelector {
    private final TournamentService tournamentService;
    private final InputHandler inputHandler;

    public GameSelector(TournamentService tournamentService, InputHandler inputHandler) {
        this.tournamentService = tournamentService;
        this.inputHandler = inputHandler;
    }

    /**
     * Affiche les jeux disponibles et demande à l'utilisateur d'en choisir un.
     * @return le jeu sélectionné, ou null si aucun jeu disponible
     */
    public BoardGame selectGame() {
        List<BoardGame> games = tournamentService.getAvailableTwoPlayerGames();
        if (games.isEmpty()) {
            System.out.println("Aucun jeu disponible pour un tournoi.");
            return null;
        }

        System.out.println("\n=== Sélection du jeu ===");
        for (int i = 0; i < games.size(); i++) {
            System.out.println((i + 1) + ". " + games.get(i).title());
        }

        int choice = inputHandler.readIntInRange("Choisissez un jeu", 1, games.size());
        return games.get(choice - 1);
    }
}
