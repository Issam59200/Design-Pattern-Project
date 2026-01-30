package fr.fges;

import java.util.ArrayList;
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
}
