package fr.fges.tournament;

import fr.fges.model.Match;

/**
 * Affiche les informations d'un match.
 * Classe extraite de TournamentDisplay (SRP).
 */
public class MatchDisplay {

    /**
     * Affiche l'en-tête d'un match en cours.
     */
    public void displayMatchHeader(int currentMatchNumber, int totalMatches, Match match) {
        System.out.println("─────────────────────────────────────────────");
        System.out.println("MATCH " + currentMatchNumber + "/" + totalMatches);
        System.out.println("─────────────────────────────────────────────");
        System.out.println("   " + match.player1() + " vs " + match.player2());
        System.out.println("─────────────────────────────────────────────");
    }
}
