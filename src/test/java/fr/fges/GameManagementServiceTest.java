package fr.fges;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour GameManagementService.
 * Vérifie les opérations d'ajout et de suppression de jeux.
 */
@ExtendWith(MockitoExtension.class)
class GameManagementServiceTest {

    @Mock
    private GameRepository repository;
    
    @Mock
    private GameStorage storage;
    
    @Mock
    private InputHandler inputHandler;
    
    @Mock
    private CommandManager commandManager;

    private GameManagementService service;

    private static final String STORAGE_FILE = "test.json";

    @BeforeEach
    void setUp() {
        service = new GameManagementService(repository, storage, STORAGE_FILE, inputHandler, commandManager);
    }

    @Test
    void shouldAddGame_WhenInputIsValid_AndGameDoesNotExist() {
        // Arrange
        when(inputHandler.readString("Title")).thenReturn("Catan");
        when(repository.exists("Catan")).thenReturn(false);
        when(inputHandler.readInt("Minimum Players")).thenReturn(Optional.of(3));
        when(inputHandler.readInt("Maximum Players")).thenReturn(Optional.of(4));
        when(inputHandler.readString("Category")).thenReturn("Strategy");

        // Act
        service.addGame();

        // Assert
        verify(commandManager).executeCommand(any(AddGameCommand.class));
    }

    @Test
    void shouldNotAddGame_WhenGameAlreadyExists() {
        // Arrange
        when(inputHandler.readString("Title")).thenReturn("Catan");
        when(repository.exists("Catan")).thenReturn(true);

        // Act
        service.addGame();

        // Assert
        verify(commandManager, never()).executeCommand(any());
    }

    @Test
    void shouldNotAddGame_WhenMinPlayersIsInvalid() {
        // Arrange
        when(inputHandler.readString("Title")).thenReturn("Catan");
        when(repository.exists("Catan")).thenReturn(false);
        when(inputHandler.readInt("Minimum Players")).thenReturn(Optional.empty());

        // Act
        service.addGame();

        // Assert
        verify(commandManager, never()).executeCommand(any());
    }

    @Test
    void shouldNotAddGame_WhenMaxPlayersIsInvalid() {
        // Arrange
        when(inputHandler.readString("Title")).thenReturn("Catan");
        when(repository.exists("Catan")).thenReturn(false);
        when(inputHandler.readInt("Minimum Players")).thenReturn(Optional.of(3));
        when(inputHandler.readInt("Maximum Players")).thenReturn(Optional.empty());

        // Act
        service.addGame();

        // Assert
        verify(commandManager, never()).executeCommand(any());
    }

    @Test
    void shouldRemoveGame_WhenGameIsFound() {
        // Arrange
        String titleToRemove = "Monopoly";
        BoardGame game = new BoardGame("Monopoly", 2, 6, "Family");
        List<BoardGame> existingGames = new ArrayList<>();
        existingGames.add(game);

        when(inputHandler.readString("Title of game to remove")).thenReturn(titleToRemove);
        when(repository.getGames()).thenReturn(existingGames);

        // Act
        service.removeGame();

        // Assert
        verify(commandManager).executeCommand(any(RemoveGameCommand.class));
    }

    @Test
    void shouldDoNothing_WhenGameToRemoveIsNotFound() {
        // Arrange
        when(inputHandler.readString("Title of game to remove")).thenReturn("Unknown Game");
        when(repository.getGames()).thenReturn(new ArrayList<>());

        // Act
        service.removeGame();

        // Assert
        verify(commandManager, never()).executeCommand(any());
    }

    @Test
    void shouldRemoveGame_CaseInsensitive() {
        // Arrange
        BoardGame game = new BoardGame("Monopoly", 2, 6, "Family");
        when(inputHandler.readString("Title of game to remove")).thenReturn("monopoly");
        when(repository.getGames()).thenReturn(List.of(game));

        // Act
        service.removeGame();

        // Assert
        verify(commandManager).executeCommand(any(RemoveGameCommand.class));
    }
}
