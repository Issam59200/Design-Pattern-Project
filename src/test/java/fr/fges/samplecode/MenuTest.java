package fr.fges.samplecode;

import fr.fges.BoardGame;
import fr.fges.GameCollection;
import fr.fges.Menu;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MenuTest {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        // 1. On redirige la sortie console pour vérifier ce qui est affiché
        System.setOut(new PrintStream(outContent));

        // 2. IMPORTANT : On nettoie la collection statique avant chaque test
        // pour partir d'une feuille blanche (sinon les tests s'influencent)
        GameCollection.getGames().clear();
    }

    @AfterEach
    void restoreStreams() {
        // On remet la console normale après chaque test
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    // Petite méthode utilitaire pour simuler la frappe au clavier
    private void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    @Test
    void testAddGame() {
        // Scénario : L'utilisateur tape le titre, min, max, catégorie
        // "Uno" -> Entrée -> "2" -> Entrée -> "10" -> Entrée -> "Card" -> Entrée
        String simulation = "Uno\n2\n10\nCard\n";
        provideInput(simulation);

        // On instancie le menu APRÈS avoir défini l'entrée (provideInput)
        Menu menu = new Menu();
        menu.addGame();

        // Vérification 1 : Le jeu est-il bien dans la collection ?
        List<BoardGame> games = GameCollection.getGames();
        assertEquals(1, games.size(), "Il devrait y avoir 1 jeu ajouté");
        assertEquals("Uno", games.get(0).title());

        // Vérification 2 : A-t-on le message de succès ?
        // On vérifie si la sortie console contient le message attendu
        assertTrue(outContent.toString().contains("Board game added successfully"));
    }

    @Test
    void testAddGameWithInvalidInput() {
        // Scénario : L'utilisateur tape "deux" au lieu de 2
        String simulation = "BadInputGame\ndeux\n3\nStrategy\n";
        provideInput(simulation);

        Menu menu = new Menu();
        menu.addGame();

        // Vérification : Rien ne doit être ajouté
        assertEquals(0, GameCollection.getGames().size());
        assertTrue(outContent.toString().contains("Error: Please enter valid numbers"));
    }

    @Test
    void testRemoveGame() {
        // Préparation : On ajoute manuellement un jeu pour pouvoir le supprimer
        GameCollection.addGame(new BoardGame("Monopoly", 2, 8, "Family"));

        // Scénario : L'utilisateur veut supprimer "Monopoly"
        String simulation = "Monopoly\n";
        provideInput(simulation);

        Menu menu = new Menu();
        menu.removeGame();

        // Vérification
        assertEquals(0, GameCollection.getGames().size(), "La collection doit être vide");
        assertTrue(outContent.toString().contains("Board game removed successfully"));
    }

    @Test
    void testRemoveGameNotFound() {
        GameCollection.addGame(new BoardGame("Monopoly", 2, 8, "Family"));

        // Scénario : Erreur de frappe
        String simulation = "Trivial Pursuit\n";
        provideInput(simulation);

        Menu menu = new Menu();
        menu.removeGame();

        assertEquals(1, GameCollection.getGames().size(), "Le jeu ne doit pas être supprimé");
        assertTrue(outContent.toString().contains("No board game found"));
    }

    @Test
    void testListAllGamesEmpty() {
        // Scénario : Liste vide
        Menu menu = new Menu(); // Pas besoin d'input ici
        menu.listAllGames();

        assertTrue(outContent.toString().contains("No board games in collection"));
    }

    @Test
    void testListAllGamesWithContent() {
        GameCollection.addGame(new BoardGame("Catan", 3, 4, "Strategy"));

        Menu menu = new Menu();
        menu.listAllGames();

        // On vérifie que le titre apparaît dans la sortie console
        assertTrue(outContent.toString().contains("Catan"));
        assertTrue(outContent.toString().contains("(3-4 players)"));
    }
}