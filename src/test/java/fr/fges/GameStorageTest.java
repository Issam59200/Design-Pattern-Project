package fr.fges;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class GameStorageTest {
    
    private Path tempJsonFile;
    private Path tempCsvFile;
    
    // Cette méthode s'exécute AVANT chaque test
    @BeforeEach
    void setUp() throws IOException {
        // Créer des fichiers temporaires
        tempJsonFile = Files.createTempFile("test-games", ".json");
        tempCsvFile = Files.createTempFile("test-games", ".csv");
    }
    
    // Cette méthode s'exécute APRÈS chaque test pour nettoyer
    @AfterEach
    void tearDown() throws IOException {
        // Supprimer les fichiers temporaires
        Files.deleteIfExists(tempJsonFile);
        Files.deleteIfExists(tempCsvFile);
    }
    
    // ========== TESTS JSON ==========
    
    @Test
    void shouldSaveAndLoadGamesFromJson() throws IOException {
        // Arrange
        GameStorage storage = new GameStorage(tempJsonFile.toString());
        List<BoardGame> gamesToSave = List.of(
            new BoardGame("Monopoly", 2, 6, "Strategy"),
            new BoardGame("Chess", 2, 2, "Strategy")
        );
        
        // Act - Sauvegarder
        storage.saveToFile(gamesToSave);
        
        // Assert - Le fichier doit exister
        assertTrue(Files.exists(tempJsonFile));
        
        // Act - Charger
        List<BoardGame> loadedGames = storage.loadFromFile();
        
        // Assert - Les jeux chargés doivent être identiques
        assertEquals(2, loadedGames.size());
        assertEquals("Monopoly", loadedGames.get(0).title());
        assertEquals("Chess", loadedGames.get(1).title());
    }
    
    @Test
    void shouldReturnEmptyListWhenJsonFileDoesNotExist() throws IOException {
        // Arrange - Supprimer le fichier temporaire
        Files.deleteIfExists(tempJsonFile);
        GameStorage storage = new GameStorage(tempJsonFile.toString());
        
        // Act
        List<BoardGame> loadedGames = storage.loadFromFile();
        
        // Assert
        assertTrue(loadedGames.isEmpty());
    }
    
    // ========== TESTS CSV ==========
    
    @Test
    void shouldSaveAndLoadGamesFromCsv() throws IOException {
        // Arrange
        GameStorage storage = new GameStorage(tempCsvFile.toString());
        List<BoardGame> gamesToSave = List.of(
            new BoardGame("Uno", 2, 10, "Card"),
            new BoardGame("Catan", 3, 4, "Strategy")
        );
        
        // Act - Sauvegarder
        storage.saveToFile(gamesToSave);
        
        // Assert - Le fichier doit exister
        assertTrue(Files.exists(tempCsvFile));
        
        // Act - Charger
        List<BoardGame> loadedGames = storage.loadFromFile();
        
        // Assert - Les jeux chargés doivent être identiques
        assertEquals(2, loadedGames.size());
        assertEquals("Uno", loadedGames.get(0).title());
        assertEquals("Catan", loadedGames.get(1).title());
    }
    
    @Test
    void shouldReturnEmptyListWhenCsvFileDoesNotExist() throws IOException {
        // Arrange - Supprimer le fichier temporaire
        Files.deleteIfExists(tempCsvFile);
        GameStorage storage = new GameStorage(tempCsvFile.toString());
        
        // Act
        List<BoardGame> loadedGames = storage.loadFromFile();
        
        // Assert
        assertTrue(loadedGames.isEmpty());
    }
    
    @Test
    void shouldHandleEmptyGameList() throws IOException {
        // Arrange
        GameStorage storage = new GameStorage(tempJsonFile.toString());
        List<BoardGame> emptyList = List.of();
        
        // Act
        storage.saveToFile(emptyList);
        List<BoardGame> loadedGames = storage.loadFromFile();
        
        // Assert
        assertTrue(loadedGames.isEmpty());
    }
}