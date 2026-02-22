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
import fr.fges.storage.GameStorageJSON;

class GameStorageJSONTest {

    private GameStorage jsonStorage;
    private Path tempJsonFile;

    @BeforeEach
    void setUp() throws IOException {
        jsonStorage = new GameStorageJSON();
        tempJsonFile = Files.createTempFile("test-games", ".json");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempJsonFile);
    }

    @Test
    void shouldSaveAndLoadGamesFromJson() throws IOException {
        // Arrange
        List<BoardGame> gamesToSave = List.of(
            new BoardGame("Monopoly", 2, 6, "Strategy"),
            new BoardGame("Chess", 2, 2, "Strategy")
        );

        // Act
        jsonStorage.save(tempJsonFile.toString(), gamesToSave);
        List<BoardGame> loadedGames = jsonStorage.load(tempJsonFile.toString());

        // Assert
        assertEquals(2, loadedGames.size());
        assertEquals("Monopoly", loadedGames.get(0).title());
        assertEquals(2, loadedGames.get(0).minPlayers());
        assertEquals(6, loadedGames.get(0).maxPlayers());
        assertEquals("Strategy", loadedGames.get(0).category());
        assertEquals("Chess", loadedGames.get(1).title());
    }

    @Test
    void shouldReturnEmptyListWhenJsonFileDoesNotExist() throws IOException {
        // Arrange
        Files.deleteIfExists(tempJsonFile);

        // Act
        List<BoardGame> loadedGames = jsonStorage.load(tempJsonFile.toString());

        // Assert
        assertTrue(loadedGames.isEmpty());
    }

    @Test
    void shouldHandleEmptyGameList() throws IOException {
        // Arrange
        List<BoardGame> emptyList = List.of();

        // Act
        jsonStorage.save(tempJsonFile.toString(), emptyList);
        List<BoardGame> loadedGames = jsonStorage.load(tempJsonFile.toString());

        // Assert
        assertTrue(loadedGames.isEmpty());
    }

    @Test
    void shouldSaveInPrettyPrintFormat() throws IOException {
        // Arrange
        List<BoardGame> games = List.of(
            new BoardGame("Test Game", 1, 5, "Test")
        );

        // Act
        jsonStorage.save(tempJsonFile.toString(), games);

        // Assert
        String content = Files.readString(tempJsonFile);
        assertTrue(content.contains("\"title\""));
        assertTrue(content.contains("\"minPlayers\""));
        assertTrue(content.contains("\n"));
    }

    @Test
    void shouldImplementGameStorage() {
        // Assert
        assertInstanceOf(GameStorage.class, jsonStorage);
    }
}