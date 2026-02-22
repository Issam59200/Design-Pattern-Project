package fr.fges;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import fr.fges.tournament.PlayerCollector;
import fr.fges.ui.InputHandler;

/**
 * Tests pour PlayerCollector.
 */
public class PlayerCollectorTest {
    private PlayerCollector playerCollector;
    private InputHandler mockInputHandler;

    @BeforeEach
    public void setup() {
        mockInputHandler = mock(InputHandler.class);
        playerCollector = new PlayerCollector(mockInputHandler);
    }

    @Test
    public void testCollectThreePlayerNames() {
        when(mockInputHandler.readIntInRange(anyString(), eq(3), eq(8))).thenReturn(3);
        when(mockInputHandler.readString(anyString())).thenReturn("Alice", "Bob", "Charlie");

        List<String> names = playerCollector.collectPlayerNames();

        assertEquals(3, names.size());
        assertEquals("Alice", names.get(0));
        assertEquals("Bob", names.get(1));
        assertEquals("Charlie", names.get(2));
    }

    @Test
    public void testCollectMaxPlayerNames() {
        when(mockInputHandler.readIntInRange(anyString(), eq(3), eq(8))).thenReturn(8);
        when(mockInputHandler.readString(anyString())).thenReturn("P1", "P2", "P3", "P4", "P5", "P6", "P7", "P8");

        List<String> names = playerCollector.collectPlayerNames();

        assertEquals(8, names.size());
    }

    @Test
    public void testCollectCallsReadStringForEachPlayer() {
        when(mockInputHandler.readIntInRange(anyString(), eq(3), eq(8))).thenReturn(4);
        when(mockInputHandler.readString(anyString())).thenReturn("A", "B", "C", "D");

        playerCollector.collectPlayerNames();

        verify(mockInputHandler, times(4)).readString(anyString());
    }
}
