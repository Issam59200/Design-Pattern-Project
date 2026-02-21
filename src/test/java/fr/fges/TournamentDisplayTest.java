package fr.fges;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests pour TournamentDisplay (facade).
 * Vérifie que la facade délègue correctement à MatchDisplay et RankingDisplay.
 */
public class TournamentDisplayTest {
    private TournamentDisplay display;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    public void setup() {
        display = new TournamentDisplay();
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @org.junit.jupiter.api.AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void testDisplayMatchHeader() {
        Match match = new Match("Alice", "Bob");
        display.displayMatchHeader(1, 3, match);

        String output = outputStream.toString();
        assertTrue(output.contains("MATCH 1/3"));
        assertTrue(output.contains("Alice vs Bob"));
    }

    @Test
    public void testDisplayFinalResults() {
        BoardGame game = new BoardGame("Chess", 2, 2, "Strategy");
        Player player1 = new Player("Alice");
        Player player2 = new Player("Bob");
        player1.addVictory();
        player2.addDefeat();

        List<Player> players = Arrays.asList(player1, player2);
        Tournament mockTournament = mock(Tournament.class);
        when(mockTournament.getGame()).thenReturn(game);
        when(mockTournament.getRanking()).thenReturn(players);

        display.displayFinalResults(mockTournament);

        String output = outputStream.toString();
        assertTrue(output.contains("RÉSULTATS FINAUX"));
        assertTrue(output.contains("Chess"));
        assertTrue(output.contains("Alice"));
    }

    @Test
    public void testDisplayRankingWithMedals() {
        Player player1 = new Player("Alice");
        Player player2 = new Player("Bob");
        Player player3 = new Player("Charlie");

        player1.addVictory();
        player1.addVictory();
        player2.addVictory();
        player3.addDefeat();

        List<Player> ranking = Arrays.asList(player1, player2, player3);
        display.displayRanking(ranking);

        String output = outputStream.toString();
        assertTrue(output.contains("CLASSEMENT FINAL"));
        assertTrue(output.contains("Alice"));
        assertTrue(output.contains("Bob"));
        assertTrue(output.contains("Charlie"));
        assertTrue(output.contains("Points"));
        assertTrue(output.contains("Victoires"));
    }

    @Test
    public void testDisplayRankingEmptyList() {
        List<Player> emptyRanking = new ArrayList<>();
        display.displayRanking(emptyRanking);

        String output = outputStream.toString();
        assertTrue(output.contains("Aucun joueur"));
    }

    @Test
    public void testDisplayWinner() {
        BoardGame game = new BoardGame("Chess", 2, 2, "Strategy");
        Player winner = new Player("Alice");
        winner.addVictory();
        winner.addVictory();

        Tournament mockTournament = mock(Tournament.class);
        when(mockTournament.getRanking()).thenReturn(Arrays.asList(winner));
        when(mockTournament.getGame()).thenReturn(game);

        display.displayWinner(mockTournament);

        String output = outputStream.toString();
        assertTrue(output.contains("VAINQUEUR"));
        assertTrue(output.contains("ALICE"));
    }

    @Test
    public void testDisplayTop3() {
        Player player1 = new Player("Alice");
        Player player2 = new Player("Bob");
        Player player3 = new Player("Charlie");

        player1.addVictory();
        player1.addVictory();
        player1.addVictory();
        player2.addVictory();
        player2.addVictory();
        player3.addVictory();

        List<Player> ranking = Arrays.asList(player1, player2, player3);
        display.displayTop3(ranking);

        String output = outputStream.toString();
        assertTrue(output.contains("PODIUM"));
        assertTrue(output.contains("OR"));
        assertTrue(output.contains("ARGENT"));
        assertTrue(output.contains("BRONZE"));
        assertTrue(output.contains("Alice"));
        assertTrue(output.contains("Bob"));
        assertTrue(output.contains("Charlie"));
    }

    @Test
    public void testDisplayPlayerStats() {
        Player player = new Player("Alice");
        player.addVictory();
        player.addVictory();
        player.addDefeat();

        display.displayPlayerStats(player);

        String output = outputStream.toString();
        assertTrue(output.contains("Statistiques de Alice"));
        assertTrue(output.contains("Points totaux"));
        assertTrue(output.contains("Victoires"));
        assertTrue(output.contains("Défaites"));
        assertTrue(output.contains("Taux de victoire"));
    }

    @Test
    public void testDisplayPlayerStatsWithoutMatches() {
        Player player = new Player("Bob");
        display.displayPlayerStats(player);

        String output = outputStream.toString();
        assertTrue(output.contains("Statistiques de Bob"));
        assertTrue(output.contains("Points totaux: 0"));
    }

    @Test
    public void testDisplayTournamentSummary() {
        BoardGame game = new BoardGame("Chess", 2, 2, "Strategy");
        Player player1 = new Player("Alice");
        Player player2 = new Player("Bob");

        Tournament mockTournament = mock(Tournament.class);
        when(mockTournament.getGame()).thenReturn(game);
        when(mockTournament.getPlayers()).thenReturn(Arrays.asList(player1, player2));
        when(mockTournament.getTotalMatches()).thenReturn(3);
        when(mockTournament.isFinished()).thenReturn(true);

        display.displayTournamentSummary(mockTournament);

        String output = outputStream.toString();
        assertTrue(output.contains("RÉSUMÉ DU TOURNOI"));
        assertTrue(output.contains("Chess"));
        assertTrue(output.contains("Nombre de joueurs: 2"));
        assertTrue(output.contains("Total de matchs: 3"));
        assertTrue(output.contains("Terminé"));
    }

    @Test
    public void testDisplayTournamentSummaryInProgress() {
        BoardGame game = new BoardGame("Chess", 2, 2, "Strategy");

        Tournament mockTournament = mock(Tournament.class);
        when(mockTournament.getGame()).thenReturn(game);
        when(mockTournament.getPlayers()).thenReturn(Arrays.asList(new Player("Alice")));
        when(mockTournament.getTotalMatches()).thenReturn(5);
        when(mockTournament.isFinished()).thenReturn(false);

        display.displayTournamentSummary(mockTournament);

        String output = outputStream.toString();
        assertTrue(output.contains("En cours"));
    }

    @Test
    public void testDisplayTop3WithLessThan3Players() {
        Player player1 = new Player("Alice");
        Player player2 = new Player("Bob");

        player1.addVictory();
        player2.addDefeat();

        List<Player> ranking = Arrays.asList(player1, player2);
        display.displayTop3(ranking);

        String output = outputStream.toString();
        assertTrue(output.contains("PODIUM"));
        assertTrue(output.contains("OR"));
        assertTrue(output.contains("ARGENT"));
        assertFalse(output.contains("BRONZE"));
    }

    @Test
    public void testDisplayMatchHeaderShowsCorrectFormat() {
        Match match = new Match("PlayerOne", "PlayerTwo");
        display.displayMatchHeader(2, 5, match);

        String output = outputStream.toString();
        assertTrue(output.contains("2/5"));
        assertTrue(output.contains("PlayerOne"));
        assertTrue(output.contains("PlayerTwo"));
    }
}
