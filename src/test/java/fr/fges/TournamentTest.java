package fr.fges;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import fr.fges.model.BoardGame;
import fr.fges.model.Match;
import fr.fges.model.Player;
import fr.fges.tournament.ChampionshipStrategy;
import fr.fges.tournament.KingOfTheHillStrategy;
import fr.fges.tournament.Tournament;

class TournamentTest {

    private final BoardGame chess = new BoardGame("Chess", 2, 2, "Strategy");

    @Test
    void shouldCalculatePointsCorrectly_Championship() {
        // Arrange - 3 joueurs, round-robin (3 matchs)
        List<Player> players = List.of(new Player("Alice"), new Player("Bob"), new Player("Charlie"));
        Tournament tournament = new Tournament(chess, players, new ChampionshipStrategy());

        // Act - Alice gagne tout, Bob bat Charlie
        Match m1 = tournament.getNextMatch(); // Alice vs Bob
        tournament.registerResult(m1, "Alice");

        Match m2 = tournament.getNextMatch(); // Alice vs Charlie
        tournament.registerResult(m2, "Alice");

        Match m3 = tournament.getNextMatch(); // Bob vs Charlie
        tournament.registerResult(m3, "Bob");

        // Assert
        assertTrue(tournament.isFinished());

        List<Player> ranking = tournament.getRanking();
        // Alice : 2 victoires = 6pts, Bob : 1 victoire + 1 défaite = 4pts, Charlie : 2 défaites = 2pts
        assertEquals("Alice", ranking.get(0).getName());
        assertEquals(6, ranking.get(0).getPoints());
        assertEquals(2, ranking.get(0).getWins());

        assertEquals("Bob", ranking.get(1).getName());
        assertEquals(4, ranking.get(1).getPoints());
        assertEquals(1, ranking.get(1).getWins());

        assertEquals("Charlie", ranking.get(2).getName());
        assertEquals(2, ranking.get(2).getPoints());
        assertEquals(0, ranking.get(2).getWins());
    }

    @Test
    void shouldRankByPointsThenWinsThenName() {
        // Arrange - 3 joueurs
        List<Player> players = List.of(new Player("Charlie"), new Player("Alice"), new Player("Bob"));
        Tournament tournament = new Tournament(chess, players, new ChampionshipStrategy());

        // Act - Tout le monde gagne 1 match (chacun 3+1 = 4pts, 1 win)
        Match m1 = tournament.getNextMatch(); // Charlie vs Alice
        tournament.registerResult(m1, "Charlie");

        Match m2 = tournament.getNextMatch(); // Charlie vs Bob
        tournament.registerResult(m2, "Bob");

        Match m3 = tournament.getNextMatch(); // Alice vs Bob
        tournament.registerResult(m3, "Alice");

        // Assert - tous à 4pts, 1 win chacun → tri par nom
        List<Player> ranking = tournament.getRanking();
        assertEquals("Alice", ranking.get(0).getName());
        assertEquals("Bob", ranking.get(1).getName());
        assertEquals("Charlie", ranking.get(2).getName());
    }

    @Test
    void shouldTrackMatchNumbers() {
        // Arrange
        List<Player> players = List.of(new Player("Alice"), new Player("Bob"), new Player("Charlie"));
        Tournament tournament = new Tournament(chess, players, new ChampionshipStrategy());

        // Assert
        assertEquals(1, tournament.getCurrentMatchNumber());
        assertEquals(3, tournament.getTotalMatches());

        // Act
        Match m1 = tournament.getNextMatch();
        tournament.registerResult(m1, "Alice");

        // Assert
        assertEquals(2, tournament.getCurrentMatchNumber());
    }

    @Test
    void shouldHandleKingOfTheHillDynamicMatches() {
        // Arrange - 3 joueurs en King of the Hill
        List<Player> players = List.of(new Player("Alice"), new Player("Bob"), new Player("Charlie"));
        Tournament tournament = new Tournament(chess, players, new KingOfTheHillStrategy());

        // Act - Match 1 : Alice vs Bob → Alice gagne
        Match m1 = tournament.getNextMatch();
        assertEquals("Alice", m1.player1());
        assertEquals("Bob", m1.player2());
        tournament.registerResult(m1, "Alice");

        // Match 2 : Alice (roi) vs Charlie (suivant)
        Match m2 = tournament.getNextMatch();
        assertEquals("Alice", m2.player1());
        assertEquals("Charlie", m2.player2());
        tournament.registerResult(m2, "Charlie");

        // Match 3 : Charlie (nouveau roi) vs Bob (revient)
        Match m3 = tournament.getNextMatch();
        assertEquals("Charlie", m3.player1());
        assertEquals("Bob", m3.player2());
        tournament.registerResult(m3, "Charlie");

        // Match 4 : Charlie vs Alice
        Match m4 = tournament.getNextMatch();
        assertEquals("Charlie", m4.player1());
        assertEquals("Alice", m4.player2());
        tournament.registerResult(m4, "Alice");

        // Assert
        assertTrue(tournament.isFinished());
    }

    @Test
    void shouldReturnNullWhenTournamentIsFinished() {
        // Arrange - 3 joueurs championship = 3 matchs
        List<Player> players = List.of(new Player("A"), new Player("B"), new Player("C"));
        Tournament tournament = new Tournament(chess, players, new ChampionshipStrategy());

        // Act - jouer tous les matchs
        for (int i = 0; i < 3; i++) {
            Match m = tournament.getNextMatch();
            tournament.registerResult(m, m.player1());
        }

        // Assert
        assertTrue(tournament.isFinished());
        assertNull(tournament.getNextMatch());
    }
}