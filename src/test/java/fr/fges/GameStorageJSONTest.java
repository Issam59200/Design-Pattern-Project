package fr.fges;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class GameStorageJSONTest {
    
    private GameStorageJSON jsonStorage;
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
        
        // Act - Sauvegarder
        jsonStorage.save(tempJsonFile.toString(), gamesToSave);
        
        // Assert - Le fichier doit exister
        assertTrue(Files.exists(tempJsonFile));
        
        // Act - Charger
        List<BoardGame> loadedGames = jsonStorage.load(tempJsonFile.toString());
        
        // Assert - Les jeux chargés doivent être identiques
        assertEquals(2, loadedGames.size());
        assertEquals("Monopoly", loadedGames.get(0).title());
        assertEquals(2, loadedGames.get(0).minPlayers());
        assertEquals(6, loadedGames.get(0).maxPlayers());
        assertEquals("Strategy", loadedGames.get(0).category());
        assertEquals("Chess", loadedGames.get(1).title());
        assertEquals(2, loadedGames.get(1).minPlayers());
        assertEquals(2, loadedGames.get(1).maxPlayers());
        assertEquals("Strategy", loadedGames.get(1).category());
    }
    
    @Test
    void shouldReturnEmptyListWhenJsonFileDoesNotExist() throws IOException {
        // Arrange - Supprimer le fichier temporaire
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
        
        // Assert - Vérifier que le JSON est formaté (pretty print)
        String content = Files.readString(tempJsonFile);
        assertTrue(content.contains("\"title\""));
        assertTrue(content.contains("\"minPlayers\""));
        assertTrue(content.contains("\"maxPlayers\""));
        assertTrue(content.contains("\"category\""));
        // Le pretty print devrait avoir des retours à la ligne
        assertTrue(content.contains("\n"));
    }
}
