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
import fr.fges.ui.RecommendGameMenuAction;

/**
 * Tests pour RecommendGameMenuAction.
 */
@ExtendWith(MockitoExtension.class)
class RecommendGameActionTest {

    @Mock
    private GameController controller;

    @Test
    void shouldHaveCorrectLabel() {
        // Arrange
        MenuAction action = new RecommendGameMenuAction(controller);

        // Act & Assert
        assertEquals("Recommend Game", action.label());
    }

    @Test
    void shouldCallRecommendGameOnController() {
        // Arrange
        MenuAction action = new RecommendGameMenuAction(controller);

        // Act
        action.execute();

        // Assert
        verify(controller).recommendGame();
    }

    @Test
    void shouldBeAvailableOnWeekday() {
        // Arrange
        MenuAction action = new RecommendGameMenuAction(controller);

        // Act & Assert
        assertTrue(action.availableOnWeekday());
    }

    @Test
    void shouldNotBeAvailableOnWeekend() {
        // Arrange
        MenuAction action = new RecommendGameMenuAction(controller);

        // Act & Assert
        assertFalse(action.availableOnWeekend());
    }
}
