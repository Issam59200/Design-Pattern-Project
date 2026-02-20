package fr.fges;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for MenuRegistry pattern.
 * Vérifie que les actions sont bien enregistrées, affichées et exécutées.
 */
@ExtendWith(MockitoExtension.class)
class MenuRegistryTest {

    @Mock
    private GameController controller;

    private MenuRegistry registryWeekday;
    private MenuRegistry registryWeekend;

    @BeforeEach
    void setUp() {
        registryWeekday = new MenuRegistry(false);
        registryWeekend = new MenuRegistry(true);
    }

    @Test
    void shouldRegisterAndExecuteActionByChoice() {
        // Arrange
        MenuAction addAction = new AddGameMenuAction(controller);
        registryWeekday.register("1", addAction);

        // Act
        boolean executed = registryWeekday.execute(1);

        // Assert
        assertTrue(executed);
        verify(controller).addGame();
    }

    @Test
    void shouldReturnFalseForInvalidChoice() {
        // Arrange
        MenuAction addAction = new AddGameMenuAction(controller);
        registryWeekday.register("1", addAction);

        // Act
        boolean executed = registryWeekday.execute(99);

        // Assert
        assertFalse(executed);
        verify(controller, never()).addGame();
    }

    @Test
    void shouldOnlyDisplayWeekdayActionsOnWeekday() {
        // Arrange
        registryWeekday.register("1", new ListAllGamesMenuAction(controller));
        registryWeekday.register("2", new SuggestGamesMenuAction(controller));

        // Act - L'action "List" est disponible en semaine
        boolean executed = registryWeekday.execute(1);

        // Assert
        assertTrue(executed);
        verify(controller).listAllGames();
    }

    @Test
    void shouldOnlyDisplayWeekendActionsOnWeekend() {
        // Arrange
        registryWeekend.register("1", new SuggestGamesMenuAction(controller));
        registryWeekend.register("2", new AddGameMenuAction(controller));

        // Act - SuggestGames est disponible le weekend, pas List
        boolean executedSuggest = registryWeekend.execute(1);

        // Assert
        assertTrue(executedSuggest);
        verify(controller).suggestGames();
    }

    @Test
    void shouldSkipUnavailableActionsBasedOnDay() {
        // Arrange - ListAllGames n'est pas disponible le weekend
        registryWeekend.register("1", new ListAllGamesMenuAction(controller));
        registryWeekend.register("2", new SuggestGamesMenuAction(controller));

        // Act - Essayer d'exécuter la position 1 (la première action DISPONIBLE le weekend)
        // ListAllGames est skippée car non disponible weekend
        // Donc position 1 = SuggestGames (1ère action disponible)
        boolean executed = registryWeekend.execute(1);

        // Assert - Devrait exécuter SuggestGames, pas ListAllGames
        assertTrue(executed);
        verify(controller).suggestGames();
        verify(controller, never()).listAllGames();
    }

    @Test
    void shouldCountAvailableActionsCorrectly() {
        // Arrange - Ajouter des actions avec disponibilités différentes
        registryWeekday.register("1", new ListAllGamesMenuAction(controller));
        registryWeekday.register("2", new SuggestGamesMenuAction(controller));
        registryWeekday.register("3", new AddGameMenuAction(controller));

        // Act - En semaine : List(oui) + Suggest(non) + Add(oui) = 2
        int available = registryWeekday.getAvailableActionCount();

        // Assert
        assertEquals(2, available);
    }

    @Test
    void shouldExecuteCorrectActionWhenMultipleRegistered() {
        // Arrange
        registryWeekday.register("1", new AddGameMenuAction(controller));
        registryWeekday.register("2", new RemoveGameMenuAction(controller));
        registryWeekday.register("3", new ListAllGamesMenuAction(controller));

        // Act - Exécuter la 2e action
        boolean executed = registryWeekday.execute(2);

        // Assert
        assertTrue(executed);
        verify(controller).removeGame();
        verify(controller, never()).addGame();
        verify(controller, never()).listAllGames();
    }
}
