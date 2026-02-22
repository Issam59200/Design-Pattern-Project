package fr.fges.tournament;

import java.util.ArrayList;
import java.util.List;

import fr.fges.model.Match;
import fr.fges.model.Player;

/**
 * Format King of the Hill.
 * Le gagnant reste en place, le perdant est remplacé par le joueur suivant.
 * On génère tous les matchs à l'avance : chaque joueur en attente
 * affronte le "roi" actuel (le premier joueur de la liste au départ).
 *
 * Note : contrairement au Championship, l'ordre des matchs dépend des résultats.
 * On génère donc un match initial, puis les matchs suivants sont déterminés
 * dynamiquement par le Tournament lors de l'exécution.
 *
 * Cette stratégie génère la séquence initiale :
 * - Match 1 : joueur 1 vs joueur 2
 * - Les matchs suivants seront : gagnant vs joueur 3, gagnant vs joueur 4, etc.
 * - On fait plusieurs tours jusqu'à ce que chaque joueur ait joué un nombre équitable de matchs.
 */
public class KingOfTheHillStrategy implements TournamentStrategy {

    @Override
    public List<Match> generateMatches(List<Player> players) {
        // En King of the Hill, les matchs sont dynamiques (dépendent du gagnant).
        // On retourne seulement le premier match ; le Tournament gère la suite.
        List<Match> matches = new ArrayList<>();
        if (players.size() >= 2) {
            matches.add(new Match(players.get(0).getName(), players.get(1).getName()));
        }
        return matches;
    }
}