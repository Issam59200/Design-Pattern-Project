package fr.fges;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {

    @Mock
    private GameRepository repository;
    @Mock
    private GameStorage storage;
    @Mock
    private GameDisplay display;
    @Mock
    private GameRecommender recommender;
    @Mock
    private InputHandler inputHandler;

    @InjectMocks
    private GameController controller;


    @Test
    void shouldAddGame_WhenInputIsValid_AndGameDoesNotExist() {
        // ARRANGE (Préparation)
        when(inputHandler.readString("Title")).thenReturn("Catan");
        when(repository.exists("Catan")).thenReturn(false);
        when(inputHandler.readInt("Minimum Players")).thenReturn(Optional.of(3));
        when(inputHandler.readInt("Maximum Players")).thenReturn(Optional.of(4));
        when(inputHandler.readString("Category")).thenReturn("Strategy");

        // ACT (Action)
        controller.addGame();

        // ASSERT (Vérification)
        verify(repository).addGame(any(BoardGame.class)); // Vérifie que le jeu est ajouté
        verify(storage).saveToFile(any());                // Vérifie que la sauvegarde est déclenchée
    }

    @Test
    void shouldNotAddGame_WhenGameAlreadyExists() {
        // ARRANGE
        when(inputHandler.readString("Title")).thenReturn("Catan");
        when(repository.exists("Catan")).thenReturn(true); // Le jeu est déjà là

        // ACT
        controller.addGame();

        // ASSERT
        verify(repository, never()).addGame(any(BoardGame.class)); // On ne doit PAS ajouter
        verify(storage, never()).saveToFile(any());                // On ne doit PAS sauvegarder
    }


    @Test
    void shouldRemoveGame_WhenGameIsFound() {
        // ARRANGE
        String titleToRemove = "Monopoly";
        BoardGame game = new BoardGame("Monopoly", 2, 6, "Family");
        List<BoardGame> existingGames = new ArrayList<>();
        existingGames.add(game);

        when(inputHandler.readString("Title of game to remove")).thenReturn(titleToRemove);
        when(repository.getGames()).thenReturn(existingGames);

        // ACT
        controller.removeGame();

        // ASSERT
        verify(repository).removeGame(game); // Vérifie qu'on supprime le bon objet
        verify(storage).saveToFile(existingGames);
    }

    @Test
    void shouldDoNothing_WhenGameToRemoveIsNotFound() {
        // ARRANGE
        when(inputHandler.readString("Title of game to remove")).thenReturn("Unknown Game");
        when(repository.getGames()).thenReturn(new ArrayList<>()); // Liste vide

        // ACT
        controller.removeGame();

        // ASSERT
        verify(repository, never()).removeGame(any());
        verify(storage, never()).saveToFile(any());
    }


    @Test
    void suggestGames_ShouldExitEarly_WhenRepositoryIsEmpty() {
        // ARRANGE
        when(repository.isEmpty()).thenReturn(true);

        // ACT
        controller.suggestGames();

        // ASSERT
        // On vérifie qu'on n'a jamais demandé le nombre de jeux à l'utilisateur
        // Cela prouve qu'on s'est arrêté au "return" du début
        verify(inputHandler, never()).readInt(anyString());
    }

    @Test
    void suggestGames_ShouldDisplaySelection_WhenRepositoryHasContent() {
        // ARRANGE
        int requestedCount = 3;
        BoardGame g1 = new BoardGame("G1", 1, 2, "C");
        List<BoardGame> suggestions = List.of(g1);

        when(repository.isEmpty()).thenReturn(false);
        when(inputHandler.readInt(anyString())).thenReturn(Optional.of(requestedCount));
        when(repository.getRandomGames(requestedCount)).thenReturn(suggestions);

        // ACT
        controller.suggestGames();

        // ASSERT
        verify(repository).getRandomGames(requestedCount); // On a bien demandé 3 jeux au repo
        verify(display).formatGameDisplay(g1);             // On a bien demandé l'affichage du jeu
    }
}