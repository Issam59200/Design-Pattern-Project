package fr.fges;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommandTest {

    @Mock
    private GameRepository repository;
    
    @Mock
    private GameStorage storage;

    private static final String STORAGE_FILE = "test.json";

    @Test
    void shouldExecuteAddGameCommand() {
        // Arrange
        BoardGame game = new BoardGame("Catan", 3, 4, "Strategy");
        Command command = new AddGameCommand(repository, storage, STORAGE_FILE, game);

        // Act
        command.execute();

        // Assert
        verify(repository).addGame(game);
        verify(storage).save(anyString(), any());
    }

    @Test
    void shouldUndoAddGameCommand() {
        // Arrange
        BoardGame game = new BoardGame("Catan", 3, 4, "Strategy");
        Command command = new AddGameCommand(repository, storage, STORAGE_FILE, game);

        // Act
        command.undo();

        // Assert
        verify(repository).removeGame(game);
        verify(storage).save(anyString(), any());
    }

    @Test
    void shouldExecuteRemoveGameCommand() {
        // Arrange
        BoardGame game = new BoardGame("Catan", 3, 4, "Strategy");
        Command command = new RemoveGameCommand(repository, storage, STORAGE_FILE, game);

        // Act
        command.execute();

        // Assert
        verify(repository).removeGame(game);
        verify(storage).save(anyString(), any());
    }

    @Test
    void shouldUndoRemoveGameCommand() {
        // Arrange
        BoardGame game = new BoardGame("Catan", 3, 4, "Strategy");
        Command command = new RemoveGameCommand(repository, storage, STORAGE_FILE, game);

        // Act
        command.undo();

        // Assert
        verify(repository).addGame(game);
        verify(storage).save(anyString(), any());
    }
}
