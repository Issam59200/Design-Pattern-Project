package fr.fges;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class MenuTest {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    
    private GameRepository repository;
    private GameStorage storage;
    private GameDisplay display;
    private Menu menu;
    private Path tempFile;

    @BeforeEach
    void setUp() throws IOException {
        // Rediriger la sortie console
        System.setOut(new PrintStream(outContent));

        tempFile = Files.createTempFile("test-menu", ".json");
        
        repository = new GameRepository();
        storage = new GameStorage(tempFile.toString());
        display = new GameDisplay();
    }

    @AfterEach
    void restoreStreams() throws IOException {
        // Remettre la console normale
        System.setIn(originalIn);
        System.setOut(originalOut);
        
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testAddGame() {
        String simulation = "Uno\n2\n10\nCard\n";
        Scanner testScanner = new Scanner(new ByteArrayInputStream(simulation.getBytes()));
        ApplicationContext context = new ApplicationContext(repository, storage, display);
        menu = new Menu(context, testScanner);

        menu.addGame();

        List<BoardGame> games = repository.getGames();
        assertEquals(1, games.size(), "Il devrait y avoir 1 jeu ajouté");
        assertEquals("Uno", games.get(0).title());

        assertTrue(outContent.toString().contains("Board game added successfully"));
        testScanner.close();
    }

    @Test
    void testAddGameWithInvalidInput() {
        String simulation = "BadInputGame\ndeux\n3\nStrategy\n";
        Scanner testScanner = new Scanner(new ByteArrayInputStream(simulation.getBytes()));
        ApplicationContext context = new ApplicationContext(repository, storage, display);
        menu = new Menu(context, testScanner);

        menu.addGame();

        assertEquals(0, repository.getGames().size());
        assertTrue(outContent.toString().contains("Error: Please enter valid numbers"));
        testScanner.close();
    }

    @Test
    void testRemoveGame() {
        repository.addGame(new BoardGame("Monopoly", 2, 8, "Family"));

        String simulation = "Monopoly\n";
        Scanner testScanner = new Scanner(new ByteArrayInputStream(simulation.getBytes()));
        ApplicationContext context = new ApplicationContext(repository, storage, display);
        menu = new Menu(context, testScanner);

        menu.removeGame();

        assertEquals(0, repository.getGames().size(), "La collection doit être vide");
        assertTrue(outContent.toString().contains("Board game removed successfully"));
        testScanner.close();
    }

    @Test
    void testRemoveGameNotFound() {
        repository.addGame(new BoardGame("Monopoly", 2, 8, "Family"));

        String simulation = "Trivial Pursuit\n";
        Scanner testScanner = new Scanner(new ByteArrayInputStream(simulation.getBytes()));
        ApplicationContext context = new ApplicationContext(repository, storage, display);
        menu = new Menu(context, testScanner);

        menu.removeGame();

        assertEquals(1, repository.getGames().size(), "Le jeu ne doit pas être supprimé");
        assertTrue(outContent.toString().contains("No board game found"));
        testScanner.close();
    }

    @Test
    void testListAllGamesEmpty() {
        Scanner testScanner = new Scanner(new ByteArrayInputStream("".getBytes()));
        ApplicationContext context = new ApplicationContext(repository, storage, display);
        menu = new Menu(context, testScanner);
        
        menu.listAllGames();

        assertTrue(outContent.toString().contains("No board games in collection"));
        testScanner.close();
    }

    @Test
    void testListAllGamesWithContent() {
        repository.addGame(new BoardGame("Catan", 3, 4, "Strategy"));

        Scanner testScanner = new Scanner(new ByteArrayInputStream("".getBytes()));
        ApplicationContext context = new ApplicationContext(repository, storage, display);
        menu = new Menu(context, testScanner);
        
        menu.listAllGames();

        assertTrue(outContent.toString().contains("Catan"));
        assertTrue(outContent.toString().contains("(3-4 players)"));
        testScanner.close();
    }
}