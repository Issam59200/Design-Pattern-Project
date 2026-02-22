package fr.fges;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.fges.ui.GameController;
import fr.fges.ui.InputHandler;
import fr.fges.ui.Menu;

/**
 * Tests pour le Menu utilisant MenuRegistry.
 * Vérifie que les actions sont correctement enregistrées et exécutées.
 */
@ExtendWith(MockitoExtension.class)
class MenuTest {

    @Mock
    private GameController controller;

    @Mock
    private InputHandler inputHandler;

    private void simulateUserInput(String choice) {
        when(inputHandler.readMenuChoice())
                .thenReturn(choice)
                .thenThrow(new RuntimeException("Stop Loop"));
    }

    private void runMenuSafe(Menu menu) {
        try {
            menu.run();
        } catch (RuntimeException e) {
            // On ignore l'exception "Stop Loop", c'est juste pour sortir du while(true)
        }
    }

    @Test
    void shouldExecuteAddGameAction_WhenChoiceIs1() {
        // ARRANGE - Menu en semaine
        Menu menu = new Menu(controller, inputHandler, false);
        simulateUserInput("1");

        // ACT
        runMenuSafe(menu);

        // ASSERT
        verify(controller).addGame();
    }

    @Test
    void shouldExecuteListGames_WhenChoiceIs3_OnWeekday() {
        // ARRANGE - Menu en semaine, l'action 3 doit être List
        Menu menu = new Menu(controller, inputHandler, false);
        simulateUserInput("3");

        // ACT
        runMenuSafe(menu);

        // ASSERT
        verify(controller).listAllGames();
        verify(controller, never()).suggestGames();
    }

    @Test
    void shouldExecuteSuggestGames_WhenChoiceIs3_OnWeekend() {
        // ARRANGE - Menu le weekend
        // Positions disponibles: 1=Add, 2=Remove, 3=Suggest (List/Recommend skipped)
        Menu menu = new Menu(controller, inputHandler, true);
        simulateUserInput("3");

        // ACT
        runMenuSafe(menu);

        // ASSERT
        verify(controller).suggestGames();
        verify(controller, never()).listAllGames();
    }

    @Test
    void shouldNotExecuteListGames_OnWeekend() {
        // ARRANGE - Le weekend
        // Positions: 1=Add, 2=Remove, 3=Suggest (pas List!)
        // Essayer de taper 3 exécute Suggest, pas List
        Menu menu = new Menu(controller, inputHandler, true);
        simulateUserInput("3");

        // ACT
        runMenuSafe(menu);

        // ASSERT
        // List ne doit pas être exécuté, c'est Suggest qui l'est à la position 3
        verify(controller, never()).listAllGames();
        verify(controller).suggestGames();
    }

    @Test
    void shouldExecuteRemoveGame_WhenChoiceIs2() {
        // ARRANGE
        Menu menu = new Menu(controller, inputHandler, false);
        simulateUserInput("2");

        // ACT
        runMenuSafe(menu);

        // ASSERT
        verify(controller).removeGame();
    }

    @Test
    void shouldExecuteUndo_WhenChoiceIs5() {
        // ARRANGE - En semaine
        // Positions: 1=Add, 2=Remove, 3=List, 4=Recommend, 5=Undo (Suggest skipped)
        Menu menu = new Menu(controller, inputHandler, false);
        simulateUserInput("5");

        // ACT
        runMenuSafe(menu);

        // ASSERT
        verify(controller).undoLastAction();
    }

    @Test
    void shouldHandleInvalidInput_WhenChoiceIsNotNumeric() {
        // ARRANGE
        Menu menu = new Menu(controller, inputHandler, false);
        simulateUserInput("abc");

        // ACT
        runMenuSafe(menu);

        // ASSERT
        // Aucune action ne devrait être exécutée
        verify(controller, never()).addGame();
        verify(controller, never()).removeGame();
        verify(controller, never()).listAllGames();
    }

    @Test
    void shouldHandleInvalidChoice_WhenChoiceOutOfRange() {
        // ARRANGE
        Menu menu = new Menu(controller, inputHandler, false);
        simulateUserInput("99");

        // ACT
        runMenuSafe(menu);

        // ASSERT
        // Aucune action ne devrait être exécutée
        verify(controller, never()).addGame();
        verify(controller, never()).removeGame();
    }
}