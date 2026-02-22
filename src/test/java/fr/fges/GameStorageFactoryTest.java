package fr.fges;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import fr.fges.storage.GameStorage;
import fr.fges.storage.GameStorageCSV;
import fr.fges.storage.GameStorageFactory;
import fr.fges.storage.GameStorageJSON;

class GameStorageFactoryTest {

    @Test
    void shouldReturnJsonStorageForJsonFile() {
        // Act
        GameStorage storage = GameStorageFactory.create("games.json");

        // Assert
        assertInstanceOf(GameStorageJSON.class, storage);
    }

    @Test
    void shouldReturnCsvStorageForCsvFile() {
        // Act
        GameStorage storage = GameStorageFactory.create("games.csv");

        // Assert
        assertInstanceOf(GameStorageCSV.class, storage);
    }

    @Test
    void shouldThrowExceptionForUnsupportedFormat() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            GameStorageFactory.create("games.txt");
        });
    }

    @Test
    void shouldThrowExceptionForXmlFormat() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            GameStorageFactory.create("games.xml");
        });
    }

    @Test
    void shouldHandlePathWithDirectories() {
        // Act
        GameStorage jsonStorage = GameStorageFactory.create("/path/to/data/games.json");
        GameStorage csvStorage = GameStorageFactory.create("/path/to/data/games.csv");

        // Assert
        assertInstanceOf(GameStorageJSON.class, jsonStorage);
        assertInstanceOf(GameStorageCSV.class, csvStorage);
    }
}