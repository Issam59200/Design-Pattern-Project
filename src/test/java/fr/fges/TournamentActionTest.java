package fr.fges;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests pour TournamentAction.
 * Vérifie les interactions : sélection jeu, saisie joueurs, choix format, appel au service.
 */
public class TournamentActionTest {
    private TournamentAction tournamentAction;
    private TournamentService mockTournamentService;
    private TournamentDisplay mockDisplay;
    private InputHandler mockInputHandler;
    private BoardGame testGame;

    @BeforeEach
    public void setup() {
        mockTournamentService = mock(TournamentService.class);
        mockDisplay = mock(TournamentDisplay.class);
        mockInputHandler = mock(InputHandler.class);
        
        testGame = new BoardGame("Chess", 2, 2, "Strategy");
        tournamentAction = new TournamentAction(mockTournamentService, mockDisplay, mockInputHandler);
    }

    @Test
    public void testExecuteWithChampionshipFormat() {
        // Arrange
        when(mockTournamentService.getAvailableTwoPlayerGames()).thenReturn(Arrays.asList(testGame));
        
        when(mockInputHandler.readIntInRange(anyString(), anyInt(), anyInt())).thenAnswer(invocation -> {
            String prompt = invocation.getArgument(0);
            if (prompt.contains("Choisissez un jeu")) return 1;
            if (prompt.contains("Nombre de joueurs")) return 3;
            if (prompt.contains("Format")) return 1;
            return 0;
        });
        when(mockInputHandler.readString(anyString())).thenReturn("Alice", "Bob", "Charlie");
        
        Tournament mockTournament = mock(Tournament.class);
        when(mockTournamentService.createTournament(eq(testGame), anyList(), any(ChampionshipStrategy.class)))
            .thenReturn(mockTournament);
        when(mockTournament.isFinished()).thenReturn(true);
        when(mockTournament.getGame()).thenReturn(testGame);

        // Act
        tournamentAction.execute();

        // Assert
        verify(mockTournamentService).createTournament(eq(testGame), anyList(), any(ChampionshipStrategy.class));
        verify(mockDisplay).displayFinalResults(mockTournament);
    }

    @Test
    public void testExecuteWithKingOfTheHillFormat() {
        // Arrange
        when(mockTournamentService.getAvailableTwoPlayerGames()).thenReturn(Arrays.asList(testGame));
        
        when(mockInputHandler.readIntInRange(anyString(), anyInt(), anyInt())).thenAnswer(invocation -> {
            String prompt = invocation.getArgument(0);
            if (prompt.contains("Choisissez un jeu")) return 1;
            if (prompt.contains("Nombre de joueurs")) return 4;
            if (prompt.contains("Format")) return 2;
            return 0;
        });
        when(mockInputHandler.readString(anyString())).thenReturn("Alice", "Bob", "Charlie", "David");
        
        Tournament mockTournament = mock(Tournament.class);
        when(mockTournamentService.createTournament(eq(testGame), anyList(), any(KingOfTheHillStrategy.class)))
            .thenReturn(mockTournament);
        when(mockTournament.isFinished()).thenReturn(true);
        when(mockTournament.getGame()).thenReturn(testGame);

        // Act
        tournamentAction.execute();

        // Assert
        verify(mockTournamentService).createTournament(eq(testGame), anyList(), any(KingOfTheHillStrategy.class));
        verify(mockDisplay).displayFinalResults(mockTournament);
    }

    @Test
    public void testExecuteWithNoAvailableGames() {
        // Arrange
        when(mockTournamentService.getAvailableTwoPlayerGames()).thenReturn(Arrays.asList());

        // Act
        tournamentAction.execute();

        // Assert
        verify(mockTournamentService).getAvailableTwoPlayerGames();
        verify(mockDisplay, never()).displayFinalResults(any());
    }

    @Test
    public void testExecuteRunsMatchesUntilCompletion() {
        // Arrange
        Match match1 = new Match("Alice", "Bob");
        Match match2 = new Match("Bob", "Charlie");
        
        when(mockTournamentService.getAvailableTwoPlayerGames()).thenReturn(Arrays.asList(testGame));
        
        final int[] resultCallCount = {0};
        when(mockInputHandler.readIntInRange(anyString(), anyInt(), anyInt())).thenAnswer(invocation -> {
            String prompt = invocation.getArgument(0);
            if (prompt.contains("Choisissez un jeu")) return 1;
            if (prompt.contains("Nombre de joueurs")) return 3;
            if (prompt.contains("Format")) return 1;
            if (prompt.contains("Résultat")) {
                resultCallCount[0]++;
                return resultCallCount[0]; // 1er match: joueur 1, 2ème match: joueur 2
            }
            return 0;
        });
        when(mockInputHandler.readString(anyString())).thenReturn("Alice", "Bob", "Charlie");
        
        Tournament mockTournament = mock(Tournament.class);
        when(mockTournamentService.createTournament(eq(testGame), anyList(), any(ChampionshipStrategy.class)))
            .thenReturn(mockTournament);
        when(mockTournament.isFinished()).thenReturn(false, false, true);
        when(mockTournament.getNextMatch()).thenReturn(match1, match2, null);
        when(mockTournament.getCurrentMatchNumber()).thenReturn(1, 2);
        when(mockTournament.getTotalMatches()).thenReturn(3);
        when(mockTournament.getGame()).thenReturn(testGame);

        // Act
        tournamentAction.execute();

        // Assert
        verify(mockTournament, times(2)).registerResult(any(), anyString());
        verify(mockDisplay).displayFinalResults(mockTournament);
    }

    @Test
    public void testLabelReturnsCorrectText() {
        assertEquals("Lancer un tournoi", tournamentAction.label());
    }

    @Test
    public void testAvailableOnWeekdayReturnsTrue() {
        assertTrue(tournamentAction.availableOnWeekday());
    }

    @Test
    public void testAvailableOnWeekendReturnsTrue() {
        assertTrue(tournamentAction.availableOnWeekend());
    }

    @Test
    public void testConstructorRequiresNonNullService() {
        assertThrows(NullPointerException.class, () -> 
            new TournamentAction(null, mockDisplay, mockInputHandler));
    }

    @Test
    public void testConstructorRequiresNonNullDisplay() {
        assertThrows(NullPointerException.class, () -> 
            new TournamentAction(mockTournamentService, null, mockInputHandler));
    }

    @Test
    public void testConstructorRequiresNonNullInputHandler() {
        assertThrows(NullPointerException.class, () -> 
            new TournamentAction(mockTournamentService, mockDisplay, null));
    }

    @Test
    public void testPlayerNameCollection() {
        // Arrange
        when(mockTournamentService.getAvailableTwoPlayerGames()).thenReturn(Arrays.asList(testGame));
        
        when(mockInputHandler.readIntInRange(anyString(), anyInt(), anyInt())).thenAnswer(invocation -> {
            String prompt = invocation.getArgument(0);
            if (prompt.contains("Choisissez un jeu")) return 1;
            if (prompt.contains("Nombre de joueurs")) return 3;
            if (prompt.contains("Format")) return 1;
            return 0;
        });
        when(mockInputHandler.readString(anyString())).thenReturn("Alice", "Bob", "Charlie");
        
        Tournament mockTournament = mock(Tournament.class);
        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<String>> playerNamesCaptor = (ArgumentCaptor<List<String>>) (ArgumentCaptor<?>) ArgumentCaptor.forClass(List.class);
        when(mockTournamentService.createTournament(eq(testGame), playerNamesCaptor.capture(), any()))
            .thenReturn(mockTournament);
        when(mockTournament.isFinished()).thenReturn(true);
        when(mockTournament.getGame()).thenReturn(testGame);

        // Act
        tournamentAction.execute();

        // Assert
        List<String> capturedNames = playerNamesCaptor.getValue();
        assertEquals(3, capturedNames.size());
        assertEquals("Alice", capturedNames.get(0));
        assertEquals("Bob", capturedNames.get(1));
        assertEquals("Charlie", capturedNames.get(2));
    }
}
