package fr.fges;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests pour UndoMenuAction.
 */
@ExtendWith(MockitoExtension.class)
class UndoActionTest {

    @Mock
    private GameController controller;

    @Test
    void shouldHaveCorrectLabel() {
        // Arrange
        MenuAction action = new UndoMenuAction(controller);

        // Act & Assert
        assertEquals("Undo Last Action", action.label());
    }

    @Test
    void shouldCallUndoLastActionOnController() {
        // Arrange
        MenuAction action = new UndoMenuAction(controller);

        // Act
        action.execute();

        // Assert
        verify(controller).undoLastAction();
    }

    @Test
    void shouldBeAvailableOnWeekday() {
        // Arrange
        MenuAction action = new UndoMenuAction(controller);

        // Act & Assert
        assertTrue(action.availableOnWeekday());
    }

    @Test
    void shouldBeAvailableOnWeekend() {
        // Arrange
        MenuAction action = new UndoMenuAction(controller);

        // Act & Assert
        assertTrue(action.availableOnWeekend());
    }
}
