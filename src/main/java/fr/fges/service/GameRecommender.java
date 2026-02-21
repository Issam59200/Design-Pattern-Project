package fr.fges.service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GameRecommender {
    private final GameRepository repository;
    private final Random random;

    public GameRecommender(GameRepository repository) {
        this.repository = repository;
        this.random = new Random();
    }

    // Constructeur pour les tests avec un seed fixe
    public GameRecommender(GameRepository repository, long seed) {
        this.repository = repository;
        this.random = new Random(seed);
    }

    // Recommande un jeu aléatoire compatible avec le nombre de joueurs donné.
    public BoardGame recommendGame(int playerCount) {
        // Filtrer les jeux compatibles avec le nombre de joueurs
        List<BoardGame> compatibleGames = repository.getGames().stream()
                .filter(game -> game.minPlayers() <= playerCount && game.maxPlayers() >= playerCount)
                .collect(Collectors.toList());

        // Si aucun jeu n'est compatible, retourner null
        if (compatibleGames.isEmpty()) {
            return null;
        }

        // Retourner un jeu aléatoire parmi les compatibles
        int randomIndex = random.nextInt(compatibleGames.size());
        return compatibleGames.get(randomIndex);
    }

    // Retourne le nombre de jeux compatibles avec le nombre de joueurs donné.
    
    public int countCompatibleGames(int playerCount) {
        return (int) repository.getGames().stream()
                .filter(game -> game.minPlayers() <= playerCount && game.maxPlayers() >= playerCount)
                .count();
    }
}
