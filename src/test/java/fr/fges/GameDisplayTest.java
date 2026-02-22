package fr.fges;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import fr.fges.model.BoardGame;
import fr.fges.ui.GameDisplay;

class GameDisplayTest {

    @Test
    void shouldFormatSingleGame() {
        // Arrange
        GameDisplay display = new GameDisplay();
        BoardGame monopoly = new BoardGame("Monopoly", 2, 6, "Strategy");
        
        // Act
        String result = display.formatGameDisplay(monopoly);
        
        // Assert
        assertEquals("- Monopoly (2-6 players) [Strategy]", result);
    }

    @Test
    void shouldFormatAllGamesWhenEmpty() {
        // Arrange
        GameDisplay display = new GameDisplay();
        List<BoardGame> emptyList = new ArrayList<>();
        
        // Act
        String result = display.formatAllGames(emptyList);
        
        // Assert
        assertEquals("No board games in collection.\n", result);   
    }

    @Test
    void shouldFormatAllGamesInAlphabeticalOrder() {
        // Arrange
        GameDisplay display = new GameDisplay();
        List<BoardGame> games = List.of(
            new BoardGame("Uno", 2, 10, "Card"),
            new BoardGame("Chess", 2, 2, "Strategy"),
            new BoardGame("Monopoly", 2, 6, "Strategy")
        );
        
        // Act
        String result = display.formatAllGames(games);
        
        // Assert
        assertTrue(result.contains("Chess"));
        assertTrue(result.contains("Monopoly"));
        assertTrue(result.contains("Uno"));
        
        // Vérifier l'ordre : Chess doit apparaître avant Monopoly
        int chessIndex = result.indexOf("Chess");
        int monopolyIndex = result.indexOf("Monopoly");
        int unoIndex = result.indexOf("Uno");
        
        assertTrue(chessIndex < monopolyIndex);
        assertTrue(monopolyIndex < unoIndex);
    }
}