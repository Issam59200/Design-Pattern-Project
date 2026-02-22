package fr.fges;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import fr.fges.model.Match;
import fr.fges.model.Player;
import fr.fges.tournament.ChampionshipStrategy;

class ChampionshipStrategyTest {

    @Test
    void shouldGenerateCorrectNumberOfMatches_With3Players() {
        // Arrange
        ChampionshipStrategy strategy = new ChampionshipStrategy();
        List<Player> players = List.of(new Player("Alice"), new Player("Bob"), new Player("Charlie"));

        // Act
        List<Match> matches = strategy.generateMatches(players);

        // Assert - 3 * 2 / 2 = 3 matchs
        assertEquals(3, matches.size());
    }

    @Test
    void shouldGenerateCorrectNumberOfMatches_With4Players() {
        // Arrange
        ChampionshipStrategy strategy = new ChampionshipStrategy();
        List<Player> players = List.of(
                new Player("Alice"), new Player("Bob"),
                new Player("Charlie"), new Player("Diana"));

        // Act
        List<Match> matches = strategy.generateMatches(players);

        // Assert - 4 * 3 / 2 = 6 matchs
        assertEquals(6, matches.size());
    }

    @Test
    void shouldIncludeAllPairings() {
        // Arrange
        ChampionshipStrategy strategy = new ChampionshipStrategy();
        List<Player> players = List.of(new Player("Alice"), new Player("Bob"), new Player("Charlie"));

        // Act
        List<Match> matches = strategy.generateMatches(players);

        // Assert - toutes les combinaisons présentes
        assertTrue(matches.stream().anyMatch(m -> m.player1().equals("Alice") && m.player2().equals("Bob")));
        assertTrue(matches.stream().anyMatch(m -> m.player1().equals("Alice") && m.player2().equals("Charlie")));
        assertTrue(matches.stream().anyMatch(m -> m.player1().equals("Bob") && m.player2().equals("Charlie")));
    }

    @Test
    void shouldNotGenerateDuplicateMatches() {
        // Arrange
        ChampionshipStrategy strategy = new ChampionshipStrategy();
        List<Player> players = List.of(
                new Player("Alice"), new Player("Bob"),
                new Player("Charlie"), new Player("Diana"));

        // Act
        List<Match> matches = strategy.generateMatches(players);

        // Assert - pas de doublons (A vs B et B vs A)
        long uniqueCount = matches.stream()
                .map(m -> m.player1().compareTo(m.player2()) < 0
                        ? m.player1() + "-" + m.player2()
                        : m.player2() + "-" + m.player1())
                .distinct()
                .count();
        assertEquals(matches.size(), uniqueCount);
    }
}