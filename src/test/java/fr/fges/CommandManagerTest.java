package fr.fges;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.fges.command.AddGameCommand;
import fr.fges.command.Command;
import fr.fges.command.CommandManager;
import fr.fges.command.RemoveGameCommand;
import fr.fges.model.BoardGame;
import fr.fges.storage.GameStorage;

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

    private static final String STORAGE_FILE = "test.json";

    private CommandManager commandManager;

    @BeforeEach
    void setUp() {
        commandManager = new CommandManager();
    }

    @Test
    void shouldExecuteCommandAndAddToHistory() {
        // Arrange
        BoardGame game = new BoardGame("Catan", 3, 4, "Strategy");
        Command command = new AddGameCommand(repository, storage, STORAGE_FILE, game);

        // Act
        commandManager.executeCommand(command);

        // Assert
        verify(repository).addGame(game);
        verify(storage).save(anyString(), any());
        assertFalse(commandManager.isEmpty());
    }

    @Test
    void shouldUndoLastCommand() {
        // Arrange
        BoardGame game = new BoardGame("Catan", 3, 4, "Strategy");
        Command command = new AddGameCommand(repository, storage, STORAGE_FILE, game);
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
        Command command1 = new AddGameCommand(repository, storage, STORAGE_FILE, game1);
        Command command2 = new AddGameCommand(repository, storage, STORAGE_FILE, game2);
        
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
        Command command = new RemoveGameCommand(repository, storage, STORAGE_FILE, game);
        commandManager.executeCommand(command);

        // Act
        commandManager.undoLastCommand();

        // Assert
        verify(repository).removeGame(game);
        verify(repository).addGame(game);
    }
}
