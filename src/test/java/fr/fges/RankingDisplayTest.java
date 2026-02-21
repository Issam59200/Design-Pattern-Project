package fr.fges;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests pour RankingDisplay.
 */
public class RankingDisplayTest {
    private RankingDisplay rankingDisplay;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    public void setup() {
        rankingDisplay = new RankingDisplay();
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void testDisplayRanking() {
        Player p1 = new Player("Alice");
        Player p2 = new Player("Bob");
        p1.addVictory();
        p2.addDefeat();

        rankingDisplay.displayRanking(Arrays.asList(p1, p2));

        String output = outputStream.toString();
        assertTrue(output.contains("CLASSEMENT FINAL"));
        assertTrue(output.contains("Alice"));
        assertTrue(output.contains("Bob"));
        assertTrue(output.contains("Points"));
    }

    @Test
    public void testDisplayRankingEmpty() {
        rankingDisplay.displayRanking(new ArrayList<>());

        String output = outputStream.toString();
        assertTrue(output.contains("Aucun joueur"));
    }

    @Test
    public void testDisplayWinner() {
        Player winner = new Player("Alice");
        winner.addVictory();
        winner.addVictory();

        Tournament mockTournament = mock(Tournament.class);
        when(mockTournament.getRanking()).thenReturn(Arrays.asList(winner));

        rankingDisplay.displayWinner(mockTournament);

        String output = outputStream.toString();
        assertTrue(output.contains("VAINQUEUR"));
        assertTrue(output.contains("ALICE"));
    }

    @Test
    public void testDisplayWinnerEmptyRanking() {
        Tournament mockTournament = mock(Tournament.class);
        when(mockTournament.getRanking()).thenReturn(new ArrayList<>());

        rankingDisplay.displayWinner(mockTournament);

        String output = outputStream.toString();
        assertEquals("", output);
    }

    @Test
    public void testDisplayTop3() {
        Player p1 = new Player("Alice");
        Player p2 = new Player("Bob");
        Player p3 = new Player("Charlie");
        p1.addVictory();
        p1.addVictory();
        p2.addVictory();
        p3.addDefeat();

        rankingDisplay.displayTop3(Arrays.asList(p1, p2, p3));

        String output = outputStream.toString();
        assertTrue(output.contains("PODIUM"));
        assertTrue(output.contains("OR"));
        assertTrue(output.contains("ARGENT"));
        assertTrue(output.contains("BRONZE"));
    }

    @Test
    public void testDisplayTop3WithLessThan3Players() {
        Player p1 = new Player("Alice");
        p1.addVictory();

        rankingDisplay.displayTop3(Arrays.asList(p1));

        String output = outputStream.toString();
        assertTrue(output.contains("OR"));
        assertFalse(output.contains("ARGENT"));
    }

    @Test
    public void testDisplayPlayerStats() {
        Player player = new Player("Alice");
        player.addVictory();
        player.addVictory();
        player.addDefeat();

        rankingDisplay.displayPlayerStats(player);

        String output = outputStream.toString();
        assertTrue(output.contains("Statistiques de Alice"));
        assertTrue(output.contains("Points totaux"));
        assertTrue(output.contains("Victoires"));
        assertTrue(output.contains("Défaites"));
        assertTrue(output.contains("Taux de victoire"));
    }

    @Test
    public void testDisplayPlayerStatsNoMatches() {
        Player player = new Player("Bob");

        rankingDisplay.displayPlayerStats(player);

        String output = outputStream.toString();
        assertTrue(output.contains("Statistiques de Bob"));
        assertTrue(output.contains("Points totaux: 0"));
    }
}
