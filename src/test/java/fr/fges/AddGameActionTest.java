package fr.fges;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests pour AddGameMenuAction.
 */
@ExtendWith(MockitoExtension.class)
class AddGameActionTest {

    @Mock
    private GameController controller;

    @Test
    void shouldHaveCorrectLabel() {
        // Arrange
        MenuAction action = new AddGameMenuAction(controller);

        // Act & Assert
        assertEquals("Add Board Game", action.label());
    }

    @Test
    void shouldCallAddGameOnController() {
        // Arrange
        MenuAction action = new AddGameMenuAction(controller);

        // Act
        action.execute();

        // Assert
        verify(controller).addGame();
    }

    @Test
    void shouldBeAvailableOnWeekday() {
        // Arrange
        MenuAction action = new AddGameMenuAction(controller);

        // Act & Assert
        assertTrue(action.availableOnWeekday());
    }

    @Test
    void shouldBeAvailableOnWeekend() {
        // Arrange
        MenuAction action = new AddGameMenuAction(controller);

        // Act & Assert
        assertTrue(action.availableOnWeekend());
    }
}
