package fr.fges;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GameStorage {
    private String storageFile;

    public GameStorage(String storageFile) {
        this.storageFile = storageFile;
    }

    private String getFileFormat(String filename) {
        if (filename.endsWith(".json")) {
            return "json";
        } else if (filename.endsWith(".csv")) {
            return "csv";
        }
        return "unknown";
    }

    private boolean fileExists() {
        return new File(storageFile).exists();
    }

    public List<BoardGame> loadFromFile() {
        if (!fileExists()) {
            return new ArrayList<>();
        }

        String format = getFileFormat(storageFile);
        if ("json".equals(format)) {
            return loadFromJson();
        } else if ("csv".equals(format)) {
            return loadFromCsv();
        }
        return new ArrayList<>();
    }

    private List<BoardGame> loadFromJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File file = new File(storageFile);
            return mapper.readValue(file, new TypeReference<List<BoardGame>>() {});
        } catch (IOException e) {
            System.out.println("Error loading from JSON: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    private List<BoardGame> loadFromCsv() {
        List<BoardGame> loadedGames = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(storageFile))) {
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
                    loadedGames.add(game);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading from CSV: " + e.getMessage());
        }
        return loadedGames;
    }

    // Méthode simplifiée qui utilise getFileFormat() déjà défini
    public void saveToFile(List<BoardGame> games) {
        String format = getFileFormat(storageFile);
        if ("json".equals(format)) {
            saveToJson(games);
        } else if ("csv".equals(format)) {
            saveToCsv(games);
        }
    }

    private void saveToJson(List<BoardGame> games) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(storageFile), games);
        } catch (IOException e) {
            System.out.println("Error saving to JSON: " + e.getMessage());
        }
    }

    private void saveToCsv(List<BoardGame> games) {
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
