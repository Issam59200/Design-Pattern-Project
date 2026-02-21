package fr.fges;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests pour FormatSelector.
 */
public class FormatSelectorTest {
    private FormatSelector formatSelector;
    private InputHandler mockInputHandler;

    @BeforeEach
    public void setup() {
        mockInputHandler = mock(InputHandler.class);
        formatSelector = new FormatSelector(mockInputHandler);
    }

    @Test
    public void testSelectChampionshipFormat() {
        when(mockInputHandler.readIntInRange(anyString(), eq(1), eq(2))).thenReturn(1);

        TournamentStrategy strategy = formatSelector.selectFormat();

        assertInstanceOf(ChampionshipStrategy.class, strategy);
    }

    @Test
    public void testSelectKingOfTheHillFormat() {
        when(mockInputHandler.readIntInRange(anyString(), eq(1), eq(2))).thenReturn(2);

        TournamentStrategy strategy = formatSelector.selectFormat();

        assertInstanceOf(KingOfTheHillStrategy.class, strategy);
    }
}
