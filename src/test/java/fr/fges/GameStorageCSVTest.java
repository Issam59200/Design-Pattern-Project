package fr.fges;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class GameStorageCSVTest {
    
    private GameStorageCSV csvStorage;
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
        
        // Act - Sauvegarder
        csvStorage.saveToCsv(tempCsvFile.toString(), gamesToSave);
        
        // Assert - Le fichier doit exister
        assertTrue(Files.exists(tempCsvFile));
        
        // Act - Charger
        List<BoardGame> loadedGames = csvStorage.loadFromCsv(tempCsvFile.toString());
        
        // Assert - Les jeux chargés doivent être identiques
        assertEquals(2, loadedGames.size());
        assertEquals("Uno", loadedGames.get(0).title());
        assertEquals(2, loadedGames.get(0).minPlayers());
        assertEquals(10, loadedGames.get(0).maxPlayers());
        assertEquals("Card", loadedGames.get(0).category());
        assertEquals("Catan", loadedGames.get(1).title());
        assertEquals(3, loadedGames.get(1).minPlayers());
        assertEquals(4, loadedGames.get(1).maxPlayers());
        assertEquals("Strategy", loadedGames.get(1).category());
    }
    
    @Test
    void shouldReturnEmptyListWhenCsvFileDoesNotExist() throws IOException {
        // Arrange - Supprimer le fichier temporaire
        Files.deleteIfExists(tempCsvFile);
        
        // Act
        List<BoardGame> loadedGames = csvStorage.loadFromCsv(tempCsvFile.toString());
        
        // Assert
        assertTrue(loadedGames.isEmpty());
    }
    
    @Test
    void shouldHandleEmptyGameList() throws IOException {
        // Arrange
        List<BoardGame> emptyList = List.of();
        
        // Act
        csvStorage.saveToCsv(tempCsvFile.toString(), emptyList);
        List<BoardGame> loadedGames = csvStorage.loadFromCsv(tempCsvFile.toString());
        
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
        csvStorage.saveToCsv(tempCsvFile.toString(), games);
        
        // Assert - Vérifier le format CSV
        String content = Files.readString(tempCsvFile);
        assertTrue(content.contains("title,minPlayers,maxPlayers,category"));
        assertTrue(content.contains("Test Game,1,5,Test"));
    }
}
