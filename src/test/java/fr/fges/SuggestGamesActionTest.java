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
import fr.fges.ui.MenuAction;
import fr.fges.ui.SuggestGamesMenuAction;

/**
 * Tests pour SuggestGamesMenuAction.
 */
@ExtendWith(MockitoExtension.class)
class SuggestGamesActionTest {

    @Mock
    private GameController controller;

    @Test
    void shouldHaveCorrectLabel() {
        // Arrange
        MenuAction action = new SuggestGamesMenuAction(controller);

        // Act & Assert
        assertEquals("Suggest Games for Weekend", action.label());
    }

    @Test
    void shouldCallSuggestGamesOnController() {
        // Arrange
        MenuAction action = new SuggestGamesMenuAction(controller);

        // Act
        action.execute();

        // Assert
        verify(controller).suggestGames();
    }

    @Test
    void shouldNotBeAvailableOnWeekday() {
        // Arrange
        MenuAction action = new SuggestGamesMenuAction(controller);

        // Act & Assert
        assertFalse(action.availableOnWeekday());
    }

    @Test
    void shouldBeAvailableOnWeekend() {
        // Arrange
        MenuAction action = new SuggestGamesMenuAction(controller);

        // Act & Assert
        assertTrue(action.availableOnWeekend());
    }
}
