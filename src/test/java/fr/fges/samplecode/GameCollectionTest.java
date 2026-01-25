package fr.fges.samplecode;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.fges.BoardGame;
import fr.fges.GameCollection;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameCollectionTest {

    private static final String TEST_JSON_FILE = "test-games.json";
    private static final String TEST_CSV_FILE = "test-games.csv";

    @BeforeEach
    public void setUp() {
        // Nettoyer la collection avant chaque test
        GameCollection.getGames().clear();
    }

    @AfterEach
    public void tearDown() {
        // Supprimer les fichiers de test après chaque test
        new File(TEST_JSON_FILE).delete();
        new File(TEST_CSV_FILE).delete();
        GameCollection.getGames().clear();
    }

    @Test
    public void testAddGame() {
        // Créer un jeu simple
        BoardGame game = new BoardGame("Monopoly", 2, 6, "Strategy");
        
        // Ajouter le jeu
        GameCollection.setStorageFile(TEST_JSON_FILE);
        GameCollection.addGame(game);
        
        // Vérifier que le jeu est dans la collection
        assertEquals(1, GameCollection.getGames().size());
        assertTrue(GameCollection.getGames().contains(game));
    }

    @Test
    public void testRemoveGame() {
        // Ajouter un jeu
        BoardGame game = new BoardGame("Chess", 2, 2, "Strategy");
        GameCollection.setStorageFile(TEST_JSON_FILE);
        GameCollection.addGame(game);
        
        // Retirer le jeu
        GameCollection.removeGame(game);
        
        // Vérifier que la collection est vide
        assertEquals(0, GameCollection.getGames().size());
    }

    @Test
    public void testGetSortedGames() {
        // Ajouter plusieurs jeux dans le désordre
        GameCollection.setStorageFile(TEST_JSON_FILE);
        GameCollection.addGame(new BoardGame("Uno", 2, 10, "Cards"));
        GameCollection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        GameCollection.addGame(new BoardGame("Monopoly", 2, 6, "Strategy"));
        
        // Récupérer la liste triée
        List<BoardGame> sorted = GameCollection.getSortedGames();
        
        // Vérifier que les jeux sont triés par titre
        assertEquals("Chess", sorted.get(0).title());
        assertEquals("Monopoly", sorted.get(1).title());
        assertEquals("Uno", sorted.get(2).title());
    }

    @Test
    public void testSaveAndLoadJson() {
        // Créer et ajouter des jeux
        GameCollection.setStorageFile(TEST_JSON_FILE);
        GameCollection.addGame(new BoardGame("Risk", 2, 6, "War"));
        GameCollection.addGame(new BoardGame("Scrabble", 2, 4, "Word"));
        
        // Nettoyer la collection en mémoire
        GameCollection.getGames().clear();
        
        // Recharger depuis le fichier
        GameCollection.loadFromFile();
        
        // Vérifier que les jeux ont été rechargés
        assertEquals(2, GameCollection.getGames().size());
    }

    @Test
    public void testSaveAndLoadCsv() {
        // Créer et ajouter des jeux
        GameCollection.setStorageFile(TEST_CSV_FILE);
        GameCollection.addGame(new BoardGame("Catan", 3, 4, "Strategy"));
        GameCollection.addGame(new BoardGame("Ticket to Ride", 2, 5, "Family"));
        
        // Nettoyer la collection en mémoire
        GameCollection.getGames().clear();
        
        // Recharger depuis le fichier
        GameCollection.loadFromFile();
        
        // Vérifier que les jeux ont été rechargés
        assertEquals(2, GameCollection.getGames().size());
    }

    @Test
    public void testLoadFromNonExistentFile() {
        // Configurer un fichier qui n'existe pas
        GameCollection.setStorageFile("fichier-inexistant.json");
        
        // Charger depuis le fichier
        GameCollection.loadFromFile();
        
        // Vérifier que la collection reste vide (pas d'erreur)
        assertEquals(0, GameCollection.getGames().size());
    }

    @Test
    public void testViewAllGamesWithEmptyCollection() {
        // Tester viewAllGames() avec collection vide
        // Pas d'exception attendue
        assertDoesNotThrow(() -> GameCollection.viewAllGames());
    }

    @Test
    public void testViewAllGamesWithGames() {
        // Ajouter des jeux
        GameCollection.setStorageFile(TEST_JSON_FILE);
        GameCollection.addGame(new BoardGame("Pandemic", 2, 4, "Cooperative"));
        
        // Tester viewAllGames() ne lance pas d'exception
        assertDoesNotThrow(() -> GameCollection.viewAllGames());
    }
}
