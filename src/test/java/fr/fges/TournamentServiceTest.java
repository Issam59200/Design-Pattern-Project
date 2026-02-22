package fr.fges;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.fges.model.BoardGame;
import fr.fges.service.TournamentService;
import fr.fges.tournament.ChampionshipStrategy;
import fr.fges.tournament.KingOfTheHillStrategy;
import fr.fges.tournament.Tournament;

class TournamentServiceTest {

    private GameRepository repository;
    private TournamentService service;

    @BeforeEach
    void setUp() {
        repository = new GameRepository();
        service = new TournamentService(repository);
    }

    @Test
    void shouldReturnOnly2PlayerCompatibleGames() {
        // Arrange
        repository.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        repository.addGame(new BoardGame("Patchwork", 2, 2, "Family"));
        repository.addGame(new BoardGame("Catan", 3, 4, "Strategy"));
        repository.addGame(new BoardGame("Uno", 2, 10, "Card"));

        // Act
        List<BoardGame> twoPlayerGames = service.getAvailableTwoPlayerGames();

        // Assert - Chess, Patchwork et Uno sont compatibles 2 joueurs
        assertEquals(3, twoPlayerGames.size());
        assertTrue(twoPlayerGames.stream().anyMatch(g -> g.title().equals("Chess")));
        assertTrue(twoPlayerGames.stream().anyMatch(g -> g.title().equals("Patchwork")));
        assertTrue(twoPlayerGames.stream().anyMatch(g -> g.title().equals("Uno")));
        // Catan (3-4) n'est pas compatible 2 joueurs
        assertFalse(twoPlayerGames.stream().anyMatch(g -> g.title().equals("Catan")));
    }

    @Test
    void shouldReturnEmptyListWhenNoTwoPlayerGames() {
        // Arrange
        repository.addGame(new BoardGame("Catan", 3, 4, "Strategy"));
        repository.addGame(new BoardGame("Mysterium", 3, 7, "Cooperative"));

        // Act
        List<BoardGame> twoPlayerGames = service.getAvailableTwoPlayerGames();

        // Assert
        assertTrue(twoPlayerGames.isEmpty());
    }

    @Test
    void shouldValidatePlayerCount_TooFew() {
        // Act & Assert
        assertNotNull(service.validatePlayerCount(2));
        assertNotNull(service.validatePlayerCount(1));
    }

    @Test
    void shouldValidatePlayerCount_TooMany() {
        // Act & Assert
        assertNotNull(service.validatePlayerCount(9));
        assertNotNull(service.validatePlayerCount(20));
    }

    @Test
    void shouldValidatePlayerCount_ValidRange() {
        // Act & Assert
        assertNull(service.validatePlayerCount(3));
        assertNull(service.validatePlayerCount(5));
        assertNull(service.validatePlayerCount(8));
    }

    @Test
    void shouldCreateTournamentWithChampionship() {
        // Arrange
        BoardGame chess = new BoardGame("Chess", 2, 2, "Strategy");
        List<String> playerNames = List.of("Alice", "Bob", "Charlie");

        // Act
        Tournament tournament = service.createTournament(chess, playerNames, new ChampionshipStrategy());

        // Assert
        assertNotNull(tournament);
        assertEquals(3, tournament.getPlayers().size());
        assertEquals(3, tournament.getTotalMatches());
        assertEquals("Chess", tournament.getGame().title());
    }

    @Test
    void shouldCreateTournamentWithKingOfTheHill() {
        // Arrange
        BoardGame chess = new BoardGame("Chess", 2, 2, "Strategy");
        List<String> playerNames = List.of("Alice", "Bob", "Charlie");

        // Act
        Tournament tournament = service.createTournament(chess, playerNames, new KingOfTheHillStrategy());

        // Assert
        assertNotNull(tournament);
        assertEquals(3, tournament.getPlayers().size());
        assertNotNull(tournament.getNextMatch());
    }
}