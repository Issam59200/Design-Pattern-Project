package fr.fges;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.fges.model.Match;
import fr.fges.tournament.MatchDisplay;

/**
 * Tests pour MatchDisplay.
 */
public class MatchDisplayTest {
    private MatchDisplay matchDisplay;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    public void setup() {
        matchDisplay = new MatchDisplay();
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void testDisplayMatchHeader() {
        Match match = new Match("Alice", "Bob");

        matchDisplay.displayMatchHeader(1, 3, match);

        String output = outputStream.toString();
        assertTrue(output.contains("MATCH 1/3"));
        assertTrue(output.contains("Alice vs Bob"));
    }

    @Test
    public void testDisplayMatchHeaderWithDifferentNumbers() {
        Match match = new Match("PlayerOne", "PlayerTwo");

        matchDisplay.displayMatchHeader(5, 10, match);

        String output = outputStream.toString();
        assertTrue(output.contains("5/10"));
        assertTrue(output.contains("PlayerOne"));
        assertTrue(output.contains("PlayerTwo"));
    }
}
