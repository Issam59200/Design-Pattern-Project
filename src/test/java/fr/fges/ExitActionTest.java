package fr.fges;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests pour ExitMenuAction.
 */
@ExtendWith(MockitoExtension.class)
class ExitActionTest {

    @Mock
    private GameController controller;

    @Test
    void shouldHaveCorrectLabel() {
        // Arrange
        MenuAction action = new ExitMenuAction(controller);

        // Act & Assert
        assertEquals("Exit", action.label());
    }

    @Test
    void shouldCallExitOnController() {
        // Arrange
        MenuAction action = new ExitMenuAction(controller);

        // Act
        action.execute();

        // Assert
        verify(controller).exit();
    }

    @Test
    void shouldBeAvailableOnWeekday() {
        // Arrange
        MenuAction action = new ExitMenuAction(controller);

        // Act & Assert
        assertTrue(action.availableOnWeekday());
    }

    @Test
    void shouldBeAvailableOnWeekend() {
        // Arrange
        MenuAction action = new ExitMenuAction(controller);

        // Act & Assert
        assertTrue(action.availableOnWeekend());
    }
}
