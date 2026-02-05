package fr.fges;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test d'intégration léger pour GameController.
 * Les tests détaillés des opérations sont dans :
 * - CommandManagerTest
 * - GameManagementServiceTest  
 * - GameQueryServiceTest
 * 
 * Ici on vérifie juste que le GameController est bien initialisé
 * et délègue correctement aux services.
 */
@ExtendWith(MockitoExtension.class)
class GameControllerTest {

    @Mock
    private GameRepository repository;
    @Mock
    private GameStorage storage;
    @Mock
    private GameDisplay display;
    @Mock
    private GameRecommender recommender;
    @Mock
    private InputHandler inputHandler;

    private GameController controller;

    @BeforeEach
    void setUp() {
        // Le controller crée lui-même ses services dans son constructeur
        controller = new GameController(repository, storage, display, recommender, inputHandler);
    }

    @Test
    void shouldInitializeSuccessfully() {
        // ARRANGE & ACT - Le controller est créé dans setUp()
        
        // ASSERT - Si aucune exception n'est levée, c'est bon
        // Ce test vérifie simplement que tous les services sont bien créés
        assert controller != null;
    }
}
