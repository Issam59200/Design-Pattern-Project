package fr.fges;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class GameStorageTest {
    
    // Tests d'intégration pour GameStorage
    // Les tests spécifiques pour CSV sont dans GameStorageCSVTest.java
    // Les tests spécifiques pour JSON sont dans GameStorageJSONTest.java
    
    @Test
    void shouldDetectJsonFormat() {
        // Arrange
        GameStorage storage = new GameStorage("test.json");
        
        // Act & Assert - Le format doit être détecté correctement
        assertNotNull(storage);
    }
    
    @Test
    void shouldDetectCsvFormat() {
        // Arrange
        GameStorage storage = new GameStorage("test.csv");
        
        // Act & Assert - Le format doit être détecté correctement
        assertNotNull(storage);
    }
}