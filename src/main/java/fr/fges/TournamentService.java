package fr.fges;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service métier pour les tournois.
 * Responsabilité : filtrage des jeux, validation, création du tournoi.
 * Pas d'affichage ni de lecture d'input (SRP).
 */
public class TournamentService {
    private final GameRepository repository;

    public TournamentService(GameRepository repository) {
        this.repository = repository;
    }

    /**
     * Retourne les jeux compatibles avec un tournoi (exactement 2 joueurs min et max).
     */
    public List<BoardGame> getAvailableTwoPlayerGames() {
        return repository.getGames().stream()
                .filter(game -> game.minPlayers() <= 2 && game.maxPlayers() >= 2)
                .collect(Collectors.toList());
    }

    /**
     * Valide le nombre de participants (entre 3 et 8).
     * @return un message d'erreur si invalide, null si valide
     */
    public String validatePlayerCount(int count) {
        if (count < 3) {
            return "A tournament needs at least 3 players.";
        }
        if (count > 8) {
            return "A tournament can have at most 8 players.";
        }
        return null;
    }

    /**
     * Crée un tournoi avec les paramètres donnés.
     */
    public Tournament createTournament(BoardGame game, List<String> playerNames, TournamentStrategy strategy) {
        List<Player> players = playerNames.stream()
                .map(Player::new)
                .collect(Collectors.toList());

        return new Tournament(game, players, strategy);
    }
}