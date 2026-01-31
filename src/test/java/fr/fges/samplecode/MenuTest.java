package fr.fges.samplecode;

import fr.fges.*;
import fr.fges.ApplicationContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class MenuTest {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        // 1. On redirige la sortie console pour vérifier ce qui est affiché
        System.setOut(new PrintStream(outContent));
        GameRepository repository = new GameRepository();

        // 2. IMPORTANT : On nettoie la collection statique avant chaque test
        // pour partir d'une feuille blanche (sinon les tests s'influencent)
        repository.getGames().clear();
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

        GameRepository repository = new GameRepository();
        GameStorage storage = new GameStorage("test.json");
        GameDisplay display = new GameDisplay();

        // On crée le contexte (le sac à dos de l'application)
        ApplicationContext context = new ApplicationContext(repository, storage, display);

        // On crée le scanner (qui va lire ta simulation "System.in")
        Scanner scanner = new Scanner(System.in);

        // On instancie le menu APRÈS avoir défini l'entrée (provideInput)
        Menu menu = new Menu(context, scanner);
        menu.addGame();

        // Vérification 1 : Le jeu est-il bien dans la collection ? (cette fois on intérroge le repository)
        List<BoardGame> games = repository.getGames();

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

        GameRepository repository = new GameRepository();
        GameStorage storage = new GameStorage("test.json");
        GameDisplay display = new GameDisplay();

        // On crée le contexte (le sac à dos de l'application)
        ApplicationContext context = new ApplicationContext(repository, storage, display);

        // On crée le scanner (qui va lire ta simulation "System.in")
        Scanner scanner = new Scanner(System.in);

        Menu menu = new Menu(context, scanner);
        menu.addGame();

        // Vérification : Rien ne doit être ajouté
        assertEquals(0, repository.getGames().size());
        assertTrue(outContent.toString().contains("Error: Please enter valid numbers"));
    }

    @Test
    void testRemoveGame() {
        // On initialise un repository
        GameRepository repository = new GameRepository();

        // On crée un jeu de test et on l'injecte directement dans ce repository local
        BoardGame monopoly = new BoardGame("Monopoly", 2, 6, "Strategy");
        repository.addGame(monopoly);

        // Scénario : L'utilisateur veut supprimer "Monopoly"
        String simulation = "Monopoly\n";
        provideInput(simulation);


        GameStorage storage = new GameStorage("test.json");
        GameDisplay display = new GameDisplay();

        // On crée le contexte (le sac à dos de l'application)
        ApplicationContext context = new ApplicationContext(repository, storage, display);

        // On crée le scanner (qui va lire ta simulation "System.in")
        Scanner scanner = new Scanner(System.in);

        Menu menu = new Menu(context, scanner);
        menu.removeGame();

        // Vérification
        assertEquals(0, repository.getGames().size(), "La collection doit être vide");
        assertTrue(outContent.toString().contains("Board game removed successfully"));
    }

    @Test
    void testRemoveGameNotFound() {
        GameRepository repository = new GameRepository();
        BoardGame monopoly = new BoardGame("Monopoly", 2, 8, "Family");
        repository.addGame(monopoly);

        // Scénario : Erreur de frappe
        String simulation = "Trivial Pursuit\n";
        provideInput(simulation);

        GameStorage storage = new GameStorage("test.json");
        GameDisplay display = new GameDisplay();

        // On crée le contexte (le sac à dos de l'application)
        ApplicationContext context = new ApplicationContext(repository, storage, display);

        // On crée le scanner (qui va lire ta simulation "System.in")
        Scanner scanner = new Scanner(System.in);

        Menu menu = new Menu(context, scanner);
        menu.removeGame();

        assertEquals(1, repository.getGames().size(), "Le jeu ne doit pas être supprimé");
        assertTrue(outContent.toString().contains("No board game found"));
    }

    @Test
    void testListAllGamesEmpty() {
        GameRepository repository = new GameRepository();
        BoardGame monopoly = new BoardGame("Monopoly", 2, 8, "Family");
        repository.addGame(monopoly);

        GameStorage storage = new GameStorage("test.json");
        GameDisplay display = new GameDisplay();

        // On crée le contexte (le sac à dos de l'application)
        ApplicationContext context = new ApplicationContext(repository, storage, display);

        // On crée le scanner (qui va lire ta simulation "System.in")
        Scanner scanner = new Scanner(System.in);

        // Scénario : Liste vide
        Menu menu = new Menu(context, scanner); // Pas besoin d'input ici
        menu.listAllGames();

        assertTrue(outContent.toString().contains("No board games in collection"));
    }

    @Test
    void testListAllGamesWithContent() {
        GameRepository repository = new GameRepository();
        BoardGame monopoly = new BoardGame("Monopoly", 2, 8, "Family");
        repository.addGame(monopoly);

        GameStorage storage = new GameStorage("test.json");
        GameDisplay display = new GameDisplay();

        // On crée le contexte (le sac à dos de l'application)
        ApplicationContext context = new ApplicationContext(repository, storage, display);

        // On crée le scanner (qui va lire ta simulation "System.in")
        Scanner scanner = new Scanner(System.in);

        Menu menu = new Menu(context, scanner);
        menu.listAllGames();

        // On vérifie que le titre apparaît dans la sortie console
        assertTrue(outContent.toString().contains("Catan"));
        assertTrue(outContent.toString().contains("(3-4 players)"));
    }
}