package fr.fges;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.fges.ui.GameController;
import fr.fges.ui.MenuAction;
import fr.fges.ui.RemoveGameMenuAction;

/**
 * Tests pour RemoveGameMenuAction.
 */
@ExtendWith(MockitoExtension.class)
class RemoveGameActionTest {

    @Mock
    private GameController controller;

    @Test
    void shouldHaveCorrectLabel() {
        // Arrange
        MenuAction action = new RemoveGameMenuAction(controller);

        // Act & Assert
        assertEquals("Remove Board Game", action.label());
    }

    @Test
    void shouldCallRemoveGameOnController() {
        // Arrange
        MenuAction action = new RemoveGameMenuAction(controller);

        // Act
        action.execute();

        // Assert
        verify(controller).removeGame();
    }

    @Test
    void shouldBeAvailableOnWeekday() {
        // Arrange
        MenuAction action = new RemoveGameMenuAction(controller);

        // Act & Assert
        assertTrue(action.availableOnWeekday());
    }

    @Test
    void shouldBeAvailableOnWeekend() {
        // Arrange
        MenuAction action = new RemoveGameMenuAction(controller);

        // Act & Assert
        assertTrue(action.availableOnWeekend());
    }
}
