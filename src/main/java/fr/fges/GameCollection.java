package fr.fges;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GameCollection {
    private static final List<BoardGame> games = new ArrayList<>();
    private static String storageFile = "";

    public static void setStorageFile(String file) {
        storageFile = file;
    }

    public static List<BoardGame> getGames() {
        return games;
    }

    public static void addGame(BoardGame game) {
        games.add(game);
        saveToFile();
    }

    public static void removeGame(BoardGame game) {
        games.remove(game);
        saveToFile();
    }

    // Méthode pour obtenir les jeux triés
    public static List<BoardGame> getSortedGames() {
        return games.stream()
                .sorted(Comparator.comparing(BoardGame::title))
                .toList();
    }

    // Méthode pour formater l'affichage d'un jeu
    private static String formatGameDisplay(BoardGame game) {
        return "Game: " + game.title() + " (" + game.minPlayers() + "-" + game.maxPlayers() + " players) - " + game.category();
    }

    // Méthode simplifiée qui utilise les méthodes ci-dessus
    public static void viewAllGames() {
        if (games.isEmpty()) {
            System.out.println("No board games in collection.");
            return;
        }

        List<BoardGame> sortedGames = getSortedGames();
        for (BoardGame game : sortedGames) {
            System.out.println(formatGameDisplay(game));
        }
    }

    // Méthode pour détecter le format du fichier
    private static String getFileFormat(String filename) {
        if (filename.endsWith(".json")) {
            return "json";
        } else if (filename.endsWith(".csv")) {
            return "csv";
        }
        return "unknown";
    }

    // Méthode pour vérifier si le fichier existe
    private static boolean fileExists() {
        return new File(storageFile).exists();
    }

    // Méthode simplifiée qui utilise les méthodes ci-dessus
    public static void loadFromFile() {
        if (!fileExists()) {
            return;
        }

        String format = getFileFormat(storageFile);
        if ("json".equals(format)) {
            loadFromJson();
        } else if ("csv".equals(format)) {
            loadFromCsv();
        }
    }

    private static void loadFromJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File file = new File(storageFile);
            List<BoardGame> loadedGames = mapper.readValue(file, new TypeReference<List<BoardGame>>() {});
            games.clear();
            games.addAll(loadedGames);
        } catch (IOException e) {
            System.out.println("Error loading from JSON: " + e.getMessage());
        }
    }

    private static void loadFromCsv() {
        try (BufferedReader reader = new BufferedReader(new FileReader(storageFile))) {
            games.clear();
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // skip header
                }
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    BoardGame game = new BoardGame(
                            parts[0],
                            Integer.parseInt(parts[1]),
                            Integer.parseInt(parts[2]),
                            parts[3]
                    );
                    games.add(game);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading from CSV: " + e.getMessage());
        }
    }

    // Méthode simplifiée qui utilise getFileFormat() déjà défini
    public static void saveToFile() {
        String format = getFileFormat(storageFile);
        if ("json".equals(format)) {
            saveToJson();
        } else if ("csv".equals(format)) {
            saveToCsv();
        }
    }

    private static void saveToJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(storageFile), games);
        } catch (IOException e) {
            System.out.println("Error saving to JSON: " + e.getMessage());
        }
    }

    private static void saveToCsv() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(storageFile))) {
            writer.write("title,minPlayers,maxPlayers,category");
            writer.newLine();
            for (BoardGame game : games) {
                writer.write(game.title() + "," + game.minPlayers() + "," + game.maxPlayers() + "," + game.category());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving to CSV: " + e.getMessage());
        }
    }
}
