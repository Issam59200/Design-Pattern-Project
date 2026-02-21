package fr.fges;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsable de la collecte des joueurs pour le tournoi.
 * Respecte le SRP : une seule responsabilité (collecter les noms).
 */
public class PlayerCollector {
    private final InputHandler inputHandler;

    public PlayerCollector(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    /**
     * Demande le nombre de joueurs et leurs noms.
     * @return la liste des noms de joueurs
     */
    public List<String> collectPlayerNames() {
        int count = inputHandler.readIntInRange("Nombre de joueurs", 3, 8);

        List<String> names = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            names.add(inputHandler.readString("Nom du joueur " + i));
        }
        return names;
    }
}
