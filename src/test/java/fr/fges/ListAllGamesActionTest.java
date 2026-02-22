package fr.fges;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.fges.ui.GameController;
import fr.fges.ui.ListAllGamesMenuAction;
import fr.fges.ui.MenuAction;

/**
 * Tests pour ListAllGamesMenuAction.
 */
@ExtendWith(MockitoExtension.class)
class ListAllGamesActionTest {

    @Mock
    private GameController controller;

    @Test
    void shouldHaveCorrectLabel() {
        // Arrange
        MenuAction action = new ListAllGamesMenuAction(controller);

        // Act & Assert
        assertEquals("List All Board Games", action.label());
    }

    @Test
    void shouldCallListAllGamesOnController() {
        // Arrange
        MenuAction action = new ListAllGamesMenuAction(controller);

        // Act
        action.execute();

        // Assert
        verify(controller).listAllGames();
    }

    @Test
    void shouldBeAvailableOnWeekday() {
        // Arrange
        MenuAction action = new ListAllGamesMenuAction(controller);

        // Act & Assert
        assertTrue(action.availableOnWeekday());
    }

    @Test
    void shouldNotBeAvailableOnWeekend() {
        // Arrange
        MenuAction action = new ListAllGamesMenuAction(controller);

        // Act & Assert
        assertFalse(action.availableOnWeekend());
    }
}
