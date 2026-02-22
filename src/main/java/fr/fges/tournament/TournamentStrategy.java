package fr.fges.tournament;

import java.util.List;

import fr.fges.model.Match;
import fr.fges.model.Player;

/**
 * Interface Strategy pour les formats de tournoi.
 * Chaque implémentation génère une liste de matchs selon ses propres règles.
 */
public interface TournamentStrategy {
    List<Match> generateMatches(List<Player> players);
}