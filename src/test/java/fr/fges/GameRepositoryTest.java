package fr.fges;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import fr.fges.model.BoardGame;

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

    @Test
    public void testGameExists() {
        // Arrange
        GameRepository repository = new GameRepository();
        BoardGame catan = new BoardGame("Catan", 2, 4, "Stategy");
        repository.addGame(catan);

        // Act
        boolean estPresent = repository.exists(catan.title());

        // Assert
        assertTrue(estPresent);
    }

    @Test
    void testGetRandomGames() {
        // On met 3 jeux dans le repository (Arrange)
        GameRepository repository = new GameRepository();
        repository.addGame(new BoardGame("Jeu A", 1, 1, "Cat1"));
        repository.addGame(new BoardGame("Jeu B", 1, 1, "Cat1"));
        repository.addGame(new BoardGame("Jeu C", 1, 1, "Cat1"));

        // On en demande 2 au hasard (Act)
        List<BoardGame> result = repository.getRandomGames(2);

        // Vérification (Assert)
        assertEquals(2, result.size());
    }
}