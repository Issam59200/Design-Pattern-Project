package fr.fges;

/**
 * Représente un joueur dans un tournoi avec ses statistiques.
 * Classe mutable : les points et victoires sont mis à jour au fil des matchs.
 */
public class Player {
    private final String name;
    private int points;
    private int wins;

    public Player(String name) {
        this.name = name;
        this.points = 0;
        this.wins = 0;
    }

    public void addVictory() {
        this.points += 3;
        this.wins++;
    }

    public void addDefeat() {
        this.points += 1;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public int getWins() {
        return wins;
    }
}