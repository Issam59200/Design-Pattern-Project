package fr.fges;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import fr.fges.model.Match;
import fr.fges.model.Player;
import fr.fges.tournament.KingOfTheHillStrategy;

class KingOfTheHillStrategyTest {

    @Test
    void shouldGenerateFirstMatch() {
        // Arrange
        KingOfTheHillStrategy strategy = new KingOfTheHillStrategy();
        List<Player> players = List.of(new Player("Alice"), new Player("Bob"), new Player("Charlie"));

        // Act
        List<Match> matches = strategy.generateMatches(players);

        // Assert - seul le premier match est généré
        assertEquals(1, matches.size());
        assertEquals("Alice", matches.get(0).player1());
        assertEquals("Bob", matches.get(0).player2());
    }

    @Test
    void shouldReturnEmptyListWithLessThan2Players() {
        // Arrange
        KingOfTheHillStrategy strategy = new KingOfTheHillStrategy();
        List<Player> players = List.of(new Player("Alice"));

        // Act
        List<Match> matches = strategy.generateMatches(players);

        // Assert
        assertTrue(matches.isEmpty());
    }
}