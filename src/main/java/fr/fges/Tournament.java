package fr.fges;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Encapsule l'état d'un tournoi.
 * Gère les joueurs, les matchs, l'enregistrement des résultats et le classement.
 */
public class Tournament {
    private final BoardGame game;
    private final List<Player> players;
    private final TournamentStrategy strategy;
    private final Queue<Match> remainingMatches;
    private final List<Match> completedMatches;
    private int totalMatches;

    public Tournament(BoardGame game, List<Player> players, TournamentStrategy strategy) {
        this.game = game;
        this.players = players;
        this.strategy = strategy;
        this.remainingMatches = new LinkedList<>(strategy.generateMatches(players));
        this.completedMatches = new ArrayList<>();
        this.totalMatches = calculateTotalMatches();
    }

    private int calculateTotalMatches() {
        if (strategy instanceof ChampionshipStrategy) {
            // Round-robin : n * (n-1) / 2
            return players.size() * (players.size() - 1) / 2;
        } else {
            // King of the Hill : (n - 1) * 2 tours pour que ce soit équitable
            return (players.size() - 1) * 2;
        }
    }

    /**
     * Retourne le prochain match à jouer, ou null si le tournoi est terminé.
     */
    public Match getNextMatch() {
        if (remainingMatches.isEmpty()) {
            return null;
        }
        return remainingMatches.peek();
    }

    /**
     * Enregistre le résultat d'un match.
     * @param match le match joué
     * @param winnerName le nom du gagnant
     */
    public void registerResult(Match match, String winnerName) {
        remainingMatches.poll();
        completedMatches.add(match);

        String loserName = match.player1().equals(winnerName) ? match.player2() : match.player1();

        Player winner = findPlayer(winnerName);
        Player loser = findPlayer(loserName);

        winner.addVictory();
        loser.addDefeat();

        // En King of the Hill, on génère le prochain match dynamiquement
        if (strategy instanceof KingOfTheHillStrategy && completedMatches.size() < totalMatches) {
            // Le gagnant reste, on prend le prochain joueur qui attend
            String nextChallenger = getNextChallenger(winnerName, loserName);
            if (nextChallenger != null) {
                remainingMatches.add(new Match(winnerName, nextChallenger));
            }
        }
    }

    /**
     * Trouve le prochain challenger en King of the Hill.
     * Parcourt les joueurs dans l'ordre et prend le premier qui n'est ni le gagnant ni le perdant actuel.
     */
    private String getNextChallenger(String currentKing, String lastLoser) {
        // Trouver l'index du dernier perdant et prendre le suivant dans la rotation
        int loserIndex = -1;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getName().equals(lastLoser)) {
                loserIndex = i;
                break;
            }
        }

        // Parcourir les joueurs à partir du suivant du perdant
        for (int offset = 1; offset < players.size(); offset++) {
            int candidateIndex = (loserIndex + offset) % players.size();
            String candidate = players.get(candidateIndex).getName();
            if (!candidate.equals(currentKing)) {
                return candidate;
            }
        }
        return null;
    }

    private Player findPlayer(String name) {
        return players.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Player not found: " + name));
    }

    /**
     * Vérifie si le tournoi est terminé.
     */
    public boolean isFinished() {
        return completedMatches.size() >= totalMatches;
    }

    /**
     * Retourne le classement trié : points desc > wins desc > nom asc.
     */
    public List<Player> getRanking() {
        return players.stream()
                .sorted(Comparator
                        .comparingInt(Player::getPoints).reversed()
                        .thenComparingInt(Player::getWins).reversed()
                        .thenComparing(Player::getName))
                .toList();
    }

    /**
     * Retourne le numéro du match actuel (1-indexed).
     */
    public int getCurrentMatchNumber() {
        return completedMatches.size() + 1;
    }

    public int getTotalMatches() {
        return totalMatches;
    }

    public BoardGame getGame() {
        return game;
    }

    public List<Player> getPlayers() {
        return players;
    }
}