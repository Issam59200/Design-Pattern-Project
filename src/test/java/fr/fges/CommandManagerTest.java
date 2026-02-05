package fr.fges;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour CommandManager.
 * Vérifie la gestion de l'historique et des opérations undo.
 */
@ExtendWith(MockitoExtension.class)
class CommandManagerTest {

    @Mock
    private GameRepository repository;
    
    @Mock
    private GameStorage storage;

    private CommandManager commandManager;

    @BeforeEach
    void setUp() {
        commandManager = new CommandManager();
    }

    @Test
    void shouldExecuteCommandAndAddToHistory() {
        // Arrange
        BoardGame game = new BoardGame("Catan", 3, 4, "Strategy");
        Command command = new AddGameCommand(repository, storage, game);

        // Act
        commandManager.executeCommand(command);

        // Assert
        verify(repository).addGame(game);
        verify(storage).saveToFile(any());
        assertFalse(commandManager.isEmpty());
    }

    @Test
    void shouldUndoLastCommand() {
        // Arrange
        BoardGame game = new BoardGame("Catan", 3, 4, "Strategy");
        Command command = new AddGameCommand(repository, storage, game);
        commandManager.executeCommand(command);

        // Act
        boolean result = commandManager.undoLastCommand();

        // Assert
        assertTrue(result);
        verify(repository).removeGame(game);
        assertTrue(commandManager.isEmpty());
    }

    @Test
    void shouldReturnFalse_WhenUndoWithEmptyHistory() {
        // Act
        boolean result = commandManager.undoLastCommand();

        // Assert
        assertFalse(result);
        verify(repository, never()).addGame(any());
        verify(repository, never()).removeGame(any());
    }

    @Test
    void shouldUndoMultipleCommands() {
        // Arrange
        BoardGame game1 = new BoardGame("Catan", 3, 4, "Strategy");
        BoardGame game2 = new BoardGame("Pandemic", 2, 4, "Cooperative");
        Command command1 = new AddGameCommand(repository, storage, game1);
        Command command2 = new AddGameCommand(repository, storage, game2);
        
        commandManager.executeCommand(command1);
        commandManager.executeCommand(command2);

        // Act
        commandManager.undoLastCommand();
        commandManager.undoLastCommand();

        // Assert
        verify(repository).removeGame(game2);
        verify(repository).removeGame(game1);
        assertTrue(commandManager.isEmpty());
    }

    @Test
    void shouldUndoRemoveCommand() {
        // Arrange
        BoardGame game = new BoardGame("Catan", 3, 4, "Strategy");
        Command command = new RemoveGameCommand(repository, storage, game);
        commandManager.executeCommand(command);

        // Act
        commandManager.undoLastCommand();

        // Assert
        verify(repository).removeGame(game);
        verify(repository).addGame(game);
    }
}
