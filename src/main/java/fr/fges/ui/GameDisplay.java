package fr.fges.ui;

import java.util.Comparator;
import java.util.List;

import fr.fges.model.BoardGame;

public class GameDisplay {
    // Méthode pour formater l'affichage d'un jeu
    public String formatGameDisplay(BoardGame game) {
    // Format pour le Menu : "- Title (min-max players) [category]"
    return "- " + game.title() + " (" + game.minPlayers() + 
           "-" + game.maxPlayers() + " players) [" + game.category() + "]";
}

    public String formatAllGames(List<BoardGame> games) {
        if (games.isEmpty()) {
            return "No board games in collection.\n";
        }

        StringBuilder result = new StringBuilder();

        List<BoardGame> sortedGames = games.stream()
                .sorted(Comparator.comparing(BoardGame::title))
                .toList();
        for (BoardGame game : sortedGames) {
            result.append(formatGameDisplay(game)).append("\n");
        }
        return result.toString();
    }

    // Méthode simplifiée qui utilise les méthodes ci-dessus
    public void viewAllGames(List<BoardGame> games) {
        System.out.println(formatAllGames(games));
    }
}
