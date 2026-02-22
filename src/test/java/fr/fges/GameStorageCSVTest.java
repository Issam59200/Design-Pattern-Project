package fr.fges;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.fges.model.BoardGame;
import fr.fges.storage.GameStorage;
import fr.fges.storage.GameStorageCSV;

class GameStorageCSVTest {

    private GameStorage csvStorage;
    private Path tempCsvFile;

    @BeforeEach
    void setUp() throws IOException {
        csvStorage = new GameStorageCSV();
        tempCsvFile = Files.createTempFile("test-games", ".csv");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempCsvFile);
    }

    @Test
    void shouldSaveAndLoadGamesFromCsv() throws IOException {
        // Arrange
        List<BoardGame> gamesToSave = List.of(
            new BoardGame("Uno", 2, 10, "Card"),
            new BoardGame("Catan", 3, 4, "Strategy")
        );

        // Act
        csvStorage.save(tempCsvFile.toString(), gamesToSave);
        List<BoardGame> loadedGames = csvStorage.load(tempCsvFile.toString());

        // Assert
        assertEquals(2, loadedGames.size());
        assertEquals("Uno", loadedGames.get(0).title());
        assertEquals(2, loadedGames.get(0).minPlayers());
        assertEquals(10, loadedGames.get(0).maxPlayers());
        assertEquals("Card", loadedGames.get(0).category());
        assertEquals("Catan", loadedGames.get(1).title());
    }

    @Test
    void shouldReturnEmptyListWhenCsvFileDoesNotExist() throws IOException {
        // Arrange
        Files.deleteIfExists(tempCsvFile);

        // Act
        List<BoardGame> loadedGames = csvStorage.load(tempCsvFile.toString());

        // Assert
        assertTrue(loadedGames.isEmpty());
    }

    @Test
    void shouldHandleEmptyGameList() throws IOException {
        // Arrange
        List<BoardGame> emptyList = List.of();

        // Act
        csvStorage.save(tempCsvFile.toString(), emptyList);
        List<BoardGame> loadedGames = csvStorage.load(tempCsvFile.toString());

        // Assert
        assertTrue(loadedGames.isEmpty());
    }

    @Test
    void shouldSaveWithCorrectCsvFormat() throws IOException {
        // Arrange
        List<BoardGame> games = List.of(
            new BoardGame("Test Game", 1, 5, "Test")
        );

        // Act
        csvStorage.save(tempCsvFile.toString(), games);

        // Assert
        String content = Files.readString(tempCsvFile);
        assertTrue(content.contains("title,minPlayers,maxPlayers,category"));
        assertTrue(content.contains("Test Game,1,5,Test"));
    }

    @Test
    void shouldImplementGameStorage() {
        // Assert - vérifie que c'est bien une implémentation du DAO
        assertInstanceOf(GameStorage.class, csvStorage);
    }
}