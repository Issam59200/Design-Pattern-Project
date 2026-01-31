package fr.fges;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameRecommenderTest {

    private GameRepository repository;
    private GameRecommender recommender;

    @BeforeEach
    void setUp() {
        repository = new GameRepository();
        // Utiliser un seed fixe pour des tests déterministes
        recommender = new GameRecommender(repository, 42);
    }

    @Test
    void shouldRecommendGameForValidPlayerCount() {
        // Arrange
        repository.addGame(new BoardGame("Catan", 3, 4, "Strategy"));
        repository.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        repository.addGame(new BoardGame("Monopoly", 2, 6, "Family"));
        
        // Act
        BoardGame recommended = recommender.recommendGame(4);
        
        // Assert
        assertNotNull(recommended, "Un jeu devrait être recommandé");
        assertTrue(recommended.minPlayers() <= 4 && recommended.maxPlayers() >= 4,
                "Le jeu recommandé doit être compatible avec 4 joueurs");
    }

    @Test
    void shouldReturnNullWhenNoCompatibleGames() {
        // Arrange
        repository.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        repository.addGame(new BoardGame("Catan", 3, 4, "Strategy"));
        
        // Act
        BoardGame recommended = recommender.recommendGame(10);
        
        // Assert
        assertNull(recommended, "Aucun jeu ne devrait être recommandé pour 10 joueurs");
    }

    @Test
    void shouldReturnNullWhenRepositoryIsEmpty() {
        // Arrange - repository vide
        
        // Act
        BoardGame recommended = recommender.recommendGame(4);
        
        // Assert
        assertNull(recommended, "Aucun jeu ne devrait être recommandé si la collection est vide");
    }

    @Test
    void shouldRecommendGameForMinimumPlayers() {
        // Arrange
        repository.addGame(new BoardGame("Uno", 2, 10, "Card"));
        
        // Act
        BoardGame recommended = recommender.recommendGame(2);
        
        // Assert
        assertNotNull(recommended);
        assertEquals("Uno", recommended.title());
    }

    @Test
    void shouldRecommendGameForMaximumPlayers() {
        // Arrange
        repository.addGame(new BoardGame("Uno", 2, 10, "Card"));
        
        // Act
        BoardGame recommended = recommender.recommendGame(10);
        
        // Assert
        assertNotNull(recommended);
        assertEquals("Uno", recommended.title());
    }

    @Test
    void shouldCountCompatibleGames() {
        // Arrange
        repository.addGame(new BoardGame("Catan", 3, 4, "Strategy"));
        repository.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        repository.addGame(new BoardGame("Monopoly", 2, 6, "Family"));
        repository.addGame(new BoardGame("Uno", 2, 10, "Card"));
        
        // Act
        int count = recommender.countCompatibleGames(4);
        
        // Assert
        assertEquals(3, count, "Il devrait y avoir 3 jeux compatibles avec 4 joueurs");
    }

    @Test
    void shouldReturnZeroWhenNoCompatibleGamesForCount() {
        // Arrange
        repository.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        
        // Act
        int count = recommender.countCompatibleGames(5);
        
        // Assert
        assertEquals(0, count, "Il ne devrait y avoir aucun jeu compatible avec 5 joueurs");
    }

    @Test
    void shouldRecommendDifferentGamesWithDifferentSeeds() {
        // Arrange - Ajouter plusieurs jeux compatibles
        repository.addGame(new BoardGame("Catan", 3, 4, "Strategy"));
        repository.addGame(new BoardGame("7 Wonders", 3, 7, "Strategy"));
        repository.addGame(new BoardGame("Ticket to Ride", 2, 5, "Family"));
        
        // Act - Recommander avec différents seeds
        GameRecommender recommender1 = new GameRecommender(repository, 1);
        GameRecommender recommender2 = new GameRecommender(repository, 2);
        BoardGame game1 = recommender1.recommendGame(4);
        BoardGame game2 = recommender2.recommendGame(4);
        
        // Assert - Tous les jeux recommandés doivent être compatibles
        assertNotNull(game1);
        assertNotNull(game2);
        assertTrue(game1.minPlayers() <= 4 && game1.maxPlayers() >= 4);
        assertTrue(game2.minPlayers() <= 4 && game2.maxPlayers() >= 4);
    }

    @Test
    void shouldHandleEdgeCaseWithOnePlayer() {
        // Arrange
        repository.addGame(new BoardGame("Solitaire", 1, 1, "Card"));
        repository.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        
        // Act
        BoardGame recommended = recommender.recommendGame(1);
        
        // Assert
        assertNotNull(recommended);
        assertEquals("Solitaire", recommended.title());
    }

    @Test
    void shouldHandleLargePlayerCount() {
        // Arrange
        repository.addGame(new BoardGame("Werewolf", 5, 20, "Party"));
        repository.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        
        // Act
        BoardGame recommended = recommender.recommendGame(15);
        
        // Assert
        assertNotNull(recommended);
        assertEquals("Werewolf", recommended.title());
    }
}
