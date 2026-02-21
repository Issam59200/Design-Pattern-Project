package fr.fges;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test d'intégration pour vérifier que les composants de tournoi
 * sont correctement assemblés dans Main.java.
 */
public class MainIntegrationTest {
    private String storageFile;
    private GameRepository repository;
    private GameStorage storage;
    private GameDisplay display;
    private GameRecommender recommender;
    private InputHandler inputHandler;

    @BeforeEach
    public void setup() {
        storageFile = "test-collection.json";
        repository = new GameRepository();
        storage = mock(GameStorage.class);
        display = new GameDisplay();
        recommender = new GameRecommender(repository);
        inputHandler = mock(InputHandler.class);
    }

    @Test
    public void testGameControllerInitialization() {
        // Arrange
        when(storage.load(storageFile)).thenReturn(new ArrayList<>());

        // Act
        GameController controller = new GameController(repository, storage, storageFile, 
                                                        display, recommender, inputHandler);

        // Assert
        assertNotNull(controller.getRepository());
        assertEquals(repository, controller.getRepository());
    }

    @Test
    public void testTournamentServiceInitialization() {
        // Arrange
        when(storage.load(storageFile)).thenReturn(new ArrayList<>());

        // Act
        TournamentService service = new TournamentService(repository);

        // Assert
        assertNotNull(service);
    }

    @Test
    public void testTournamentServiceCanGetAvailableGames() {
        // Arrange
        BoardGame twoPlayerGame = new BoardGame("Chess", 2, 2, "Strategy");
        BoardGame multiPlayerGame = new BoardGame("Monopoly", 2, 6, "Family");
        repository.addGame(twoPlayerGame);
        repository.addGame(multiPlayerGame);
        
        TournamentService service = new TournamentService(repository);

        // Act
        List<BoardGame> availableGames = service.getAvailableTwoPlayerGames();

        // Assert
        assertEquals(2, availableGames.size());
        assertTrue(availableGames.contains(twoPlayerGame));
        assertTrue(availableGames.contains(multiPlayerGame));
    }

    @Test
    public void testTournamentServiceValidatesPlayerCount() {
        // Arrange
        TournamentService service = new TournamentService(repository);

        // Act & Assert
        assertNotNull(service.validatePlayerCount(2));  // Trop peu → message d'erreur
        assertNull(service.validatePlayerCount(3));      // OK → null
        assertNull(service.validatePlayerCount(8));      // OK → null
        assertNotNull(service.validatePlayerCount(9));   // Trop → message d'erreur
    }

    @Test
    public void testTournamentServiceCanCreateTournament() {
        // Arrange
        BoardGame game = new BoardGame("Chess", 2, 2, "Strategy");
        repository.addGame(game);
        TournamentService service = new TournamentService(repository);
        List<String> playerNames = Arrays.asList("Alice", "Bob", "Charlie");
        TournamentStrategy strategy = new ChampionshipStrategy();

        // Act
        Tournament tournament = service.createTournament(game, playerNames, strategy);

        // Assert
        assertNotNull(tournament);
        assertEquals(game, tournament.getGame());
        assertEquals(3, tournament.getPlayers().size());
    }

    @Test
    public void testTournamentActionCreation() {
        // Arrange
        TournamentService service = new TournamentService(repository);
        TournamentDisplay tournamentDisplay = new TournamentDisplay();

        // Act
        TournamentAction action = new TournamentAction(service, tournamentDisplay, inputHandler);

        // Assert
        assertNotNull(action);
        assertTrue(action.label().contains("tournoi"));
        assertTrue(action.availableOnWeekday());
        assertTrue(action.availableOnWeekend());
    }

    @Test
    public void testMenuCanRegisterTournamentAction() {
        // Arrange
        when(storage.load(storageFile)).thenReturn(new ArrayList<>());
        GameController controller = new GameController(repository, storage, storageFile, 
                                                        display, recommender, inputHandler);
        Menu menu = new Menu(controller, inputHandler, false);
        TournamentAction action = new TournamentAction(
            new TournamentService(repository),
            new TournamentDisplay(),
            inputHandler
        );

        // Act - should not throw exception
        menu.registerAction("9", action);

        // Assert
        assertNotNull(action);
    }

    @Test
    public void testTournamentWithChampionshipStrategy() {
        // Arrange
        BoardGame game = new BoardGame("Chess", 2, 2, "Strategy");
        List<String> playerNames = Arrays.asList("Alice", "Bob", "Charlie");
        TournamentService service = new TournamentService(repository);
        ChampionshipStrategy strategy = new ChampionshipStrategy();

        // Act
        Tournament tournament = service.createTournament(game, playerNames, strategy);

        // Assert
        assertEquals(3, tournament.getTotalMatches()); // 3 joueurs = 3 matchs (tous contre tous)
        assertNotNull(tournament.getNextMatch());
        assertFalse(tournament.isFinished());
    }

    @Test
    public void testTournamentWithKingOfTheHillStrategy() {
        // Arrange
        BoardGame game = new BoardGame("Chess", 2, 2, "Strategy");
        List<String> playerNames = Arrays.asList("Alice", "Bob", "Charlie", "David");
        TournamentService service = new TournamentService(repository);
        KingOfTheHillStrategy strategy = new KingOfTheHillStrategy();

        // Act
        Tournament tournament = service.createTournament(game, playerNames, strategy);

        // Assert
        assertEquals(6, tournament.getTotalMatches()); // (4-1) * 2 = 6
        assertNotNull(tournament.getNextMatch());
        assertFalse(tournament.isFinished());
    }

    @Test
    public void testTournamentMatchRegistration() {
        // Arrange
        BoardGame game = new BoardGame("Chess", 2, 2, "Strategy");
        List<String> playerNames = Arrays.asList("Alice", "Bob", "Charlie");
        TournamentService service = new TournamentService(repository);
        ChampionshipStrategy strategy = new ChampionshipStrategy();
        Tournament tournament = service.createTournament(game, playerNames, strategy);

        // Act
        Match firstMatch = tournament.getNextMatch();
        tournament.registerResult(firstMatch, firstMatch.player1());

        // Assert
        assertEquals(2, tournament.getCurrentMatchNumber());
        assertNotNull(tournament.getNextMatch());
    }

    @Test
    public void testTournamentCompletionAndRanking() {
        // Arrange
        BoardGame game = new BoardGame("Chess", 2, 2, "Strategy");
        List<String> playerNames = Arrays.asList("Alice", "Bob", "Charlie");
        TournamentService service = new TournamentService(repository);
        ChampionshipStrategy strategy = new ChampionshipStrategy();
        Tournament tournament = service.createTournament(game, playerNames, strategy);

        // Act - Jouer tous les matchs
        while (!tournament.isFinished()) {
            Match match = tournament.getNextMatch();
            if (match != null) {
                tournament.registerResult(match, match.player1()); // Le premier joueur gagne toujours
            }
        }

        // Assert
        assertTrue(tournament.isFinished());
        List<Player> ranking = tournament.getRanking();
        assertEquals(3, ranking.size());
        assertTrue(ranking.get(0).getPoints() > 0);
    }

    @Test
    public void testAllComponentsInstantiable() {
        // Arrange & Act
        Player player = new Player("Test");
        Match match = new Match("A", "B");
        ChampionshipStrategy championshipStrategy = new ChampionshipStrategy();
        KingOfTheHillStrategy kingOfTheHillStrategy = new KingOfTheHillStrategy();

        // Assert
        assertNotNull(player);
        assertNotNull(match);
        assertNotNull(championshipStrategy);
        assertNotNull(kingOfTheHillStrategy);
    }

    @Test
    public void testPlayerStatisticsTracking() {
        // Arrange
        Player player = new Player("Alice");

        // Act
        player.addVictory();
        player.addVictory();
        player.addDefeat();

        // Assert
        assertEquals(7, player.getPoints()); // 2 victoires (6 points) + 1 défaite (1 point)
        assertEquals(2, player.getWins());
    }

    @Test
    public void testGameControllerHasRepositoryMethod() {
        // Arrange
        when(storage.load(storageFile)).thenReturn(new ArrayList<>());

        // Act
        GameController controller = new GameController(repository, storage, storageFile, 
                                                        display, recommender, inputHandler);

        // Assert
        assertDoesNotThrow(() -> {
            GameRepository retrievedRepository = controller.getRepository();
            assertEquals(repository, retrievedRepository);
        });
    }
}
