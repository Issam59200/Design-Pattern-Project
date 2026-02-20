package fr.fges;

/**
 * Factory pour créer le bon DAO de stockage selon l'extension du fichier.
 * C'est le seul endroit où la logique de sélection du format existe.
 */
public class GameStorageFactory {

    public static GameStorage create(String filename) {
        if (filename.endsWith(".json")) {
            return new GameStorageJSON();
        } else if (filename.endsWith(".csv")) {
            return new GameStorageCSV();
        }
        throw new IllegalArgumentException("Unsupported file format: " + filename);
    }
}