package fr.fges;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class GameRepositoryTest {

    @Test
    void shouldAddGameToRepository() {
        // Arrange
        GameRepository repository = new GameRepository();
        BoardGame monopoly = new BoardGame("Monopoly", 2, 6, "Strategy");
        
        // Act
        repository.addGame(monopoly);
        
        // Assert
        List<BoardGame> games = repository.getGames();
        assertEquals(1, games.size());
        assertTrue(games.contains(monopoly));
    }

    @Test
    void shouldRemoveGameFromRepository() {
        // Arrange
        GameRepository repository = new GameRepository();
        BoardGame monopoly = new BoardGame("Monopoly", 2, 6, "Strategy");
        repository.addGame(monopoly);
    
        // Act
        repository.removeGame(monopoly);
    
        // Assert
        List<BoardGame> games = repository.getGames();
        assertFalse(games.contains(monopoly));
        assertEquals(0, games.size());
    }

    @Test
    void shouldReturnSortedGames() {
        // Arrange
        GameRepository repository = new GameRepository();
        BoardGame uno = new BoardGame("UNO", 3, 4, "Strategy");
        BoardGame chess = new BoardGame("Chess", 2, 2, "Strategy");
        BoardGame monopoly = new BoardGame("Monopoly", 2, 6, "Strategy");
        
        repository.addGame(monopoly);
        repository.addGame(uno);
        repository.addGame(chess);
        
        // Act
        List<BoardGame> sortedGames = repository.getSortedGames();
        
        // Assert
        assertEquals(3, sortedGames.size());
        assertEquals("Chess", sortedGames.get(0).title());
        assertEquals("Monopoly", sortedGames.get(1).title());
        assertEquals("UNO", sortedGames.get(2).title());
    }
}