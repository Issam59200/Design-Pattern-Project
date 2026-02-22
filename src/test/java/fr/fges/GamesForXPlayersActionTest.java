package fr.fges;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.fges.ui.GameController;
import fr.fges.ui.GamesForXPlayersMenuAction;
import fr.fges.ui.MenuAction;

/**
 * Tests pour GamesForXPlayersMenuAction.
 */
@ExtendWith(MockitoExtension.class)
class GamesForXPlayersActionTest {

    @Mock
    private GameController controller;

    @Test
    void shouldHaveCorrectLabel() {
        // Arrange
        MenuAction action = new GamesForXPlayersMenuAction(controller);

        // Act & Assert
        assertEquals("Games for X Players", action.label());
    }

    @Test
    void shouldCallGamesForXPlayersOnController() {
        // Arrange
        MenuAction action = new GamesForXPlayersMenuAction(controller);

        // Act
        action.execute();

        // Assert
        verify(controller).gamesForXPlayers();
    }

    @Test
    void shouldBeAvailableOnWeekday() {
        // Arrange
        MenuAction action = new GamesForXPlayersMenuAction(controller);

        // Act & Assert
        assertTrue(action.availableOnWeekday());
    }

    @Test
    void shouldBeAvailableOnWeekend() {
        // Arrange
        MenuAction action = new GamesForXPlayersMenuAction(controller);

        // Act & Assert
        assertTrue(action.availableOnWeekend());
    }
}
