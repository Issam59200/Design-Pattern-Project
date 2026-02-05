package fr.fges;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuTest {

    @Mock
    private GameController controller;

    @Mock
    private InputHandler inputHandler;

    @InjectMocks
    private Menu menu;

    private void simulateUserInput(String choice) {
        when(inputHandler.readMenuChoice())
                .thenReturn(choice)
                .thenThrow(new RuntimeException("Stop Loop"));
    }

    private void runMenuSafe() {
        try {
            menu.run();
        } catch (RuntimeException e) {
            // On ignore l'exception "Stop Loop", c'est juste pour sortir du while(true)
        }
    }

    @Test
    void shouldCallListGames_WhenChoiceIs3_InWeekday() {
        // ARRANGE
        when(controller.isWeekend()).thenReturn(false); // On est en SEMAINE
        simulateUserInput("3"); // L'utilisateur tape 3

        // ACT
        runMenuSafe();

        // ASSERT
        // En semaine, 3 = List All Games
        verify(controller).listAllGames();
        verify(controller, never()).suggestGames(); // On vérifie qu'on n'a pas appelé la méthode du weekend
    }

    @Test
    void shouldCallRecommend_WhenChoiceIs4_InWeekday() {
        // ARRANGE
        when(controller.isWeekend()).thenReturn(false);
        simulateUserInput("4");

        // ACT
        runMenuSafe();

        // ASSERT
        // En semaine, 4 = Recommend
        verify(controller).recommendGame();
    }

    @Test
    void shouldCallSuggest_WhenChoiceIs3_InWeekend() {
        // ARRANGE
        when(controller.isWeekend()).thenReturn(true); // On est le WEEK-END
        simulateUserInput("3");

        // ACT
        runMenuSafe();

        // ASSERT
        // Le week-end, 3 = Suggest weekend selection
        verify(controller).suggestGames();
        verify(controller, never()).listAllGames(); // Ce n'est pas "Lister" sur le bouton 3 le week-end
    }

    @Test
    void shouldCallListGames_WhenChoiceIs4_InWeekend() {
        // ARRANGE
        when(controller.isWeekend()).thenReturn(true);
        simulateUserInput("4");

        // ACT
        runMenuSafe();

        // ASSERT
        // Le week-end, 4 = List All Games (tout est décalé de 1)
        verify(controller).listAllGames();
    }

    @Test
    void shouldDisplayError_WhenChoiceIsInvalid() {
        // ARRANGE
        when(controller.isWeekend()).thenReturn(false);
        simulateUserInput("99"); // Choix qui n'existe pas

        // ACT
        runMenuSafe();

        // ASSERT
        verify(controller, never()).addGame();
        verify(controller, never()).removeGame();
        verify(controller, never()).listAllGames();
        verify(controller, never()).recommendGame();
        verify(controller, never()).suggestGames();
        verify(controller, never()).exit();
    }
}