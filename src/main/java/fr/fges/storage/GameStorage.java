package fr.fges.storage;

import java.util.List;

/**
 * Interface DAO pour la persistence des jeux.
 * Contrat que chaque implémentation (JSON, CSV) doit respecter.
 */
public interface  GameStorage {
    /**
     * Charge les jeux depuis le fichier de stockage.
     * @param filename Chemin du fichier
     * @return Liste des jeux chargés
     */
    List<BoardGame> load(String filename);

    /**
     * Sauvegarde les jeux dans le fichier de stockage.
     * @param filename Chemin du fichier
     * @param games Liste des jeux à sauvegarder
     */
    void save(String filename, List<BoardGame> games);
}
