package fr.fges;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests pour GameSelector.
 */
public class GameSelectorTest {
    private GameSelector gameSelector;
    private TournamentService mockService;
    private InputHandler mockInputHandler;

    @BeforeEach
    public void setup() {
        mockService = mock(TournamentService.class);
        mockInputHandler = mock(InputHandler.class);
        gameSelector = new GameSelector(mockService, mockInputHandler);
    }

    @Test
    public void testSelectGameReturnsChosenGame() {
        BoardGame chess = new BoardGame("Chess", 2, 2, "Strategy");
        BoardGame go = new BoardGame("Go", 2, 2, "Strategy");
        when(mockService.getAvailableTwoPlayerGames()).thenReturn(Arrays.asList(chess, go));
        when(mockInputHandler.readIntInRange(anyString(), eq(1), eq(2))).thenReturn(2);

        BoardGame result = gameSelector.selectGame();

        assertEquals(go, result);
    }

    @Test
    public void testSelectGameReturnsNullWhenNoGames() {
        when(mockService.getAvailableTwoPlayerGames()).thenReturn(Collections.emptyList());

        BoardGame result = gameSelector.selectGame();

        assertNull(result);
    }

    @Test
    public void testSelectGameWithSingleGame() {
        BoardGame chess = new BoardGame("Chess", 2, 2, "Strategy");
        when(mockService.getAvailableTwoPlayerGames()).thenReturn(Arrays.asList(chess));
        when(mockInputHandler.readIntInRange(anyString(), eq(1), eq(1))).thenReturn(1);

        BoardGame result = gameSelector.selectGame();

        assertEquals(chess, result);
    }
}
