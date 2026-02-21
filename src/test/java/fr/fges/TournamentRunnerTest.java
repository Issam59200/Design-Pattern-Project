package fr.fges;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests pour TournamentRunner.
 */
public class TournamentRunnerTest {
    private TournamentRunner tournamentRunner;
    private TournamentDisplay mockDisplay;
    private InputHandler mockInputHandler;

    @BeforeEach
    public void setup() {
        mockDisplay = mock(TournamentDisplay.class);
        mockInputHandler = mock(InputHandler.class);
        tournamentRunner = new TournamentRunner(mockDisplay, mockInputHandler);
    }

    @Test
    public void testRunExecutesAllMatches() {
        Match match1 = new Match("Alice", "Bob");
        Match match2 = new Match("Alice", "Charlie");

        Tournament mockTournament = mock(Tournament.class);
        when(mockTournament.getGame()).thenReturn(new BoardGame("Chess", 2, 2, "Strategy"));
        when(mockTournament.isFinished()).thenReturn(false, false, true);
        when(mockTournament.getNextMatch()).thenReturn(match1, match2);
        when(mockTournament.getCurrentMatchNumber()).thenReturn(1, 2);
        when(mockTournament.getTotalMatches()).thenReturn(2);
        when(mockInputHandler.readIntInRange(anyString(), eq(1), eq(2))).thenReturn(1, 2);

        tournamentRunner.run(mockTournament);

        verify(mockTournament).registerResult(match1, "Alice");
        verify(mockTournament).registerResult(match2, "Charlie");
        verify(mockDisplay, times(2)).displayMatchHeader(anyInt(), anyInt(), any());
    }

    @Test
    public void testRunWithAlreadyFinishedTournament() {
        Tournament mockTournament = mock(Tournament.class);
        when(mockTournament.getGame()).thenReturn(new BoardGame("Chess", 2, 2, "Strategy"));
        when(mockTournament.isFinished()).thenReturn(true);

        tournamentRunner.run(mockTournament);

        verify(mockTournament, never()).getNextMatch();
        verify(mockDisplay, never()).displayMatchHeader(anyInt(), anyInt(), any());
    }

    @Test
    public void testRunPlayer2WinsMatch() {
        Match match = new Match("Alice", "Bob");

        Tournament mockTournament = mock(Tournament.class);
        when(mockTournament.getGame()).thenReturn(new BoardGame("Chess", 2, 2, "Strategy"));
        when(mockTournament.isFinished()).thenReturn(false, true);
        when(mockTournament.getNextMatch()).thenReturn(match);
        when(mockTournament.getCurrentMatchNumber()).thenReturn(1);
        when(mockTournament.getTotalMatches()).thenReturn(1);
        when(mockInputHandler.readIntInRange(anyString(), eq(1), eq(2))).thenReturn(2);

        tournamentRunner.run(mockTournament);

        verify(mockTournament).registerResult(match, "Bob");
    }
}
