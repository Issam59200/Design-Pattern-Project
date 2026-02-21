package fr.fges;

import java.util.List;

/**
 * Facade pour l'affichage du tournoi.
 * Délègue à MatchDisplay et RankingDisplay (Facade pattern).
 */
public class TournamentDisplay {
    private final MatchDisplay matchDisplay;
    private final RankingDisplay rankingDisplay;

    public TournamentDisplay() {
        this.matchDisplay = new MatchDisplay();
        this.rankingDisplay = new RankingDisplay();
    }

    public TournamentDisplay(MatchDisplay matchDisplay, RankingDisplay rankingDisplay) {
        this.matchDisplay = matchDisplay;
        this.rankingDisplay = rankingDisplay;
    }

    public void displayMatchHeader(int currentMatchNumber, int totalMatches, Match match) {
        matchDisplay.displayMatchHeader(currentMatchNumber, totalMatches, match);
    }

    public void displayFinalResults(Tournament tournament) {
        System.out.println("\n=== RÉSULTATS FINAUX DU TOURNOI ===");
        System.out.println("Jeu: " + tournament.getGame().title());
        rankingDisplay.displayRanking(tournament.getRanking());
        rankingDisplay.displayWinner(tournament);
    }

    public void displayRanking(List<Player> ranking) {
        rankingDisplay.displayRanking(ranking);
    }

    public void displayWinner(Tournament tournament) {
        rankingDisplay.displayWinner(tournament);
    }

    public void displayTop3(List<Player> ranking) {
        rankingDisplay.displayTop3(ranking);
    }

    public void displayPlayerStats(Player player) {
        rankingDisplay.displayPlayerStats(player);
    }

    public void displayTournamentSummary(Tournament tournament) {
        System.out.println("\nRÉSUMÉ DU TOURNOI:");
        System.out.println("─────────────────────────────────────────────");
        System.out.println("Jeu: " + tournament.getGame().title());
        System.out.println("Nombre de joueurs: " + tournament.getPlayers().size());
        System.out.println("Total de matchs: " + tournament.getTotalMatches());
        System.out.println("Statut: " + (tournament.isFinished() ? "Terminé" : "En cours"));
    }
}
