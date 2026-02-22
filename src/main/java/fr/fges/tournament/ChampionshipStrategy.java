package fr.fges.tournament;

import java.util.ArrayList;
import java.util.List;

import fr.fges.model.Match;
import fr.fges.model.Player;

/**
 * Format Championship (round-robin).
 * Chaque joueur affronte tous les autres exactement une fois.
 * Nombre de matchs = n * (n - 1) / 2
 */
public class ChampionshipStrategy implements TournamentStrategy {

    @Override
    public List<Match> generateMatches(List<Player> players) {
        List<Match> matches = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            for (int j = i + 1; j < players.size(); j++) {
                matches.add(new Match(players.get(i).getName(), players.get(j).getName()));
            }
        }
        return matches;
    }
}