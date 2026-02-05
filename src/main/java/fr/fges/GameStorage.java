package fr.fges;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GameStorage {
    private String storageFile;
    private GameStorageCSV csvStorage;
    private GameStorageJSON jsonStorage;

    public GameStorage(String storageFile) {
        this.storageFile = storageFile;
        this.csvStorage = new GameStorageCSV();
        this.jsonStorage = new GameStorageJSON();
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
            return jsonStorage.loadFromJson(storageFile);
        } else if ("csv".equals(format)) {
            return csvStorage.loadFromCsv(storageFile);
        }
        return new ArrayList<>();
    }

    public void saveToFile(List<BoardGame> games) {
        String format = getFileFormat(storageFile);
        if ("json".equals(format)) {
            jsonStorage.saveToJson(storageFile, games);
        } else if ("csv".equals(format)) {
            csvStorage.saveToCsv(storageFile, games);
        }
    }
}
