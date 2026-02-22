package fr.fges;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.fges.ui.AddGameMenuAction;
import fr.fges.ui.GameController;
import fr.fges.ui.MenuAction;

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
