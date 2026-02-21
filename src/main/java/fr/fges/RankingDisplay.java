package fr.fges;

import java.util.List;

/**
 * Affiche les classements, podiums et statistiques des joueurs.
 * Classe extraite de TournamentDisplay (SRP).
 */
public class RankingDisplay {

    /**
     * Affiche le classement complet avec points et victoires.
     */
    public void displayRanking(List<Player> ranking) {
        System.out.println("\nCLASSEMENT FINAL:");
        System.out.println("─────────────────────────────────────────────");

        if (ranking.isEmpty()) {
            System.out.println("Aucun joueur dans le tournoi.");
            return;
        }

        String[] medals = {"1er", "2ème", "3ème"};

        for (int i = 0; i < ranking.size(); i++) {
            Player player = ranking.get(i);
            String medal = i < 3 ? medals[i] : (i + 1) + "ème";
            String statsStr = String.format("Points: %d  Victoires: %d",
                    player.getPoints(), player.getWins());
            System.out.println(medal + ". " + player.getName() + " - " + statsStr);
        }
    }

    /**
     * Affiche le gagnant du tournoi.
     */
    public void displayWinner(Tournament tournament) {
        List<Player> ranking = tournament.getRanking();
        if (ranking.isEmpty()) return;

        Player winner = ranking.get(0);
        System.out.println("\n=== VAINQUEUR: " + winner.getName().toUpperCase() + " ===");
        System.out.println("Points: " + winner.getPoints() + "  |  Victoires: " + winner.getWins());
    }

    /**
     * Affiche le top 3 (podium).
     */
    public void displayTop3(List<Player> ranking) {
        System.out.println("\nPODIUM:");
        System.out.println("─────────────────────────────────────────────");

        String[] medals = {"OR", "ARGENT", "BRONZE"};

        for (int i = 0; i < Math.min(ranking.size(), 3); i++) {
            Player player = ranking.get(i);
            System.out.println(String.format("%s: %s (%d points, %d victoires)",
                    medals[i], player.getName(), player.getPoints(), player.getWins()));
        }
    }

    /**
     * Affiche les statistiques d'un joueur.
     */
    public void displayPlayerStats(Player player) {
        System.out.println("\nStatistiques de " + player.getName() + ":");
        System.out.println("─────────────────────────────────────────────");
        System.out.println("Points totaux: " + player.getPoints());
        System.out.println("Victoires: " + player.getWins());

        int defeats = player.getPoints() - (player.getWins() * 3);
        System.out.println("Défaites: " + defeats);

        int totalMatches = player.getWins() + defeats;
        if (totalMatches > 0) {
            double winRate = (double) player.getWins() / totalMatches * 100;
            System.out.println("Taux de victoire: " + String.format("%.1f%%", winRate));
        }
    }
}
