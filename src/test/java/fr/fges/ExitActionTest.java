package fr.fges;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.fges.ui.ExitMenuAction;
import fr.fges.ui.GameController;
import fr.fges.ui.MenuAction;

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
