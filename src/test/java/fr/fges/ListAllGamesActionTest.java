package fr.fges;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
