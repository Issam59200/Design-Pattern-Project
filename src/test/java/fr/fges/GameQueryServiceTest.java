package fr.fges;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour GameQueryService.
 * Vérifie les opérations de consultation et recherche de jeux.
 */
@ExtendWith(MockitoExtension.class)
class GameQueryServiceTest {

    @Mock
    private GameRepository repository;
    
    @Mock
    private GameDisplay display;
    
    @Mock
    private GameRecommender recommender;
    
    @Mock
    private InputHandler inputHandler;

    private GameQueryService service;

    @BeforeEach
    void setUp() {
        service = new GameQueryService(repository, display, recommender, inputHandler);
    }

    @Test
    void shouldListAllGames_WhenGamesExist() {
        // Arrange
        BoardGame game1 = new BoardGame("Catan", 3, 4, "Strategy");
        BoardGame game2 = new BoardGame("Pandemic", 2, 4, "Cooperative");
        when(repository.getSortedGames()).thenReturn(List.of(game1, game2));

        // Act
        service.listAllGames();

        // Assert
        verify(repository).getSortedGames();
        verify(display).formatGameDisplay(game1);
        verify(display).formatGameDisplay(game2);
    }

    @Test
    void shouldListAllGames_WhenNoGamesExist() {
        // Arrange
        when(repository.getSortedGames()).thenReturn(List.of());

        // Act
        service.listAllGames();

        // Assert
        verify(repository).getSortedGames();
        verify(display, never()).formatGameDisplay(any());
    }

    @Test
    void shouldRecommendGame_WhenValidPlayerCount() {
        // Arrange
        BoardGame recommended = new BoardGame("Catan", 3, 4, "Strategy");
        when(inputHandler.readInt("How many players?")).thenReturn(Optional.of(4));
        when(recommender.recommendGame(4)).thenReturn(recommended);

        // Act
        service.recommendGame();

        // Assert
        verify(recommender).recommendGame(4);
    }

    @Test
    void shouldNotRecommend_WhenInvalidPlayerCount() {
        // Arrange
        when(inputHandler.readInt("How many players?")).thenReturn(Optional.empty());

        // Act
        service.recommendGame();

        // Assert
        verify(recommender, never()).recommendGame(anyInt());
    }

    @Test
    void shouldRecommendGame_WhenNoGameFound() {
        // Arrange
        when(inputHandler.readInt("How many players?")).thenReturn(Optional.of(10));
        when(recommender.recommendGame(10)).thenReturn(null);

        // Act
        service.recommendGame();

        // Assert
        verify(recommender).recommendGame(10);
    }

    @Test
    void shouldSuggestGames_ExitEarly_WhenRepositoryIsEmpty() {
        // Arrange
        when(repository.isEmpty()).thenReturn(true);

        // Act
        service.suggestGames();

        // Assert
        verify(repository).isEmpty();
        verify(inputHandler, never()).readInt(anyString());
    }

    @Test
    void shouldSuggestGames_DisplaySelection_WhenRepositoryHasContent() {
        // Arrange
        int requestedCount = 3;
        BoardGame game1 = new BoardGame("G1", 1, 2, "C");
        List<BoardGame> suggestions = List.of(game1);

        when(repository.isEmpty()).thenReturn(false);
        when(inputHandler.readInt(anyString())).thenReturn(Optional.of(requestedCount));
        when(repository.getRandomGames(requestedCount)).thenReturn(suggestions);

        // Act
        service.suggestGames();

        // Assert
        verify(repository).getRandomGames(requestedCount);
        verify(display).formatGameDisplay(game1);
    }

    @Test
    void shouldSuggestGames_NotProceed_WhenInvalidCount() {
        // Arrange
        when(repository.isEmpty()).thenReturn(false);
        when(inputHandler.readInt(anyString())).thenReturn(Optional.empty());

        // Act
        service.suggestGames();

        // Assert
        verify(repository, never()).getRandomGames(anyInt());
        verify(display, never()).formatGameDisplay(any());
    }

    @Test
    void shouldFindGamesForXPlayers_WhenCompatibleGamesExist() {
        // Arrange
        int playerCount = 4;
        BoardGame catan = new BoardGame("Catan", 3, 4, "Strategy");
        BoardGame pandemic = new BoardGame("Pandemic", 2, 4, "Cooperative");
        List<BoardGame> compatibleGames = List.of(catan, pandemic);

        when(inputHandler.readInt("Number of players")).thenReturn(Optional.of(playerCount));
        when(repository.getGamesForPlayerCount(playerCount)).thenReturn(compatibleGames);

        // Act
        service.gamesForXPlayers();

        // Assert
        verify(repository).getGamesForPlayerCount(playerCount);
        verify(display).formatGameDisplay(catan);
        verify(display).formatGameDisplay(pandemic);
    }

    @Test
    void shouldFindGamesForXPlayers_DisplayMessage_WhenNoCompatibleGames() {
        // Arrange
        int playerCount = 10;
        when(inputHandler.readInt("Number of players")).thenReturn(Optional.of(playerCount));
        when(repository.getGamesForPlayerCount(playerCount)).thenReturn(List.of());

        // Act
        service.gamesForXPlayers();

        // Assert
        verify(repository).getGamesForPlayerCount(playerCount);
        verify(display, never()).formatGameDisplay(any());
    }

    @Test
    void shouldFindGamesForXPlayers_NotProceed_WhenInputIsInvalid() {
        // Arrange
        when(inputHandler.readInt("Number of players")).thenReturn(Optional.empty());

        // Act
        service.gamesForXPlayers();

        // Assert
        verify(repository, never()).getGamesForPlayerCount(anyInt());
        verify(display, never()).formatGameDisplay(any());
    }
}
