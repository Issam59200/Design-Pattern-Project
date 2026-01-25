package fr.fges;

public class Main {
    // Méthode pour valider la présence des arguments - facilite les tests
    private static void validateArguments(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java -jar boardgamecollection.jar <storage-file>");
            System.out.println("Storage file must be .json or .csv");
            System.exit(1);
        }
    }

    // Méthode pour valider l'extension du fichier - facilite les tests
    private static boolean isValidFileExtension(String filename) {
        return filename.endsWith(".json") || filename.endsWith(".csv");
    }

    // Méthode pour gérer une extension invalide - facilite les tests
    private static void handleInvalidExtension() {
        System.out.println("Error: Storage file must have .json or .csv extension");
        System.exit(1);
    }

    // Méthode pour initialiser l'application - facilite les tests
    private static void initializeApplication(String storageFile) {
        GameCollection.setStorageFile(storageFile);
        GameCollection.loadFromFile();
        System.out.println("Using storage file: " + storageFile);
    }

    // Méthode pour exécuter la boucle principale - facilite les tests (peut être arrêtée)
    private static void runApplication() {
        while (true) {
            Menu.handleMenu();
        }
    }

    // Méthode principale simplifiée qui orchestre les autres méthodes
    public static void main(String[] args) {
        validateArguments(args);
        
        String storageFile = args[0];
        
        if (!isValidFileExtension(storageFile)) {
            handleInvalidExtension();
        }

        initializeApplication(storageFile);
        runApplication();
    }
}
