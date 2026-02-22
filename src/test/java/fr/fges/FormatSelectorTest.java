package fr.fges;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import fr.fges.tournament.ChampionshipStrategy;
import fr.fges.tournament.FormatSelector;
import fr.fges.tournament.KingOfTheHillStrategy;
import fr.fges.tournament.TournamentStrategy;
import fr.fges.ui.InputHandler;

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
