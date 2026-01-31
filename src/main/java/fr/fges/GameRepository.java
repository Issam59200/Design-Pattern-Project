package fr.fges;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GameRepository {
    private List<BoardGame> games = new ArrayList<>();

    public void addGame(BoardGame game) {
        games.add(game);
    }

    public void removeGame(BoardGame game) {
        games.remove(game);
    }

    public List<BoardGame> getGames() {
        return games;
    }

    // Méthode pour obtenir les jeux triés
    public List<BoardGame> getSortedGames() {
        return games.stream()
                .sorted(Comparator.comparing(BoardGame::title))
                .toList();
    }

    // Vérifie si le titre du jeu existe déjà retourne true si c'est le cas et false sinon
    public boolean exists(String title) {
        for (BoardGame game : games) {
            if (game.title().equalsIgnoreCase(title))
                return true;
        }
        return false;
    }

    public List<BoardGame> getRandomGames (int count) {
        Collections.shuffle(games);
        return games.subList(0, Math.min(count, games.size()));

    }


}
