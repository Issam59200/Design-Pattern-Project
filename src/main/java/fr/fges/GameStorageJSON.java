package fr.fges;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Gestionnaire de stockage JSON.
 * Gère le chargement et la sauvegarde des jeux au format JSON.
 */
public class GameStorageJSON {
    
    /**
     * Charge les jeux depuis un fichier JSON.
     * @param filename Chemin du fichier JSON
     * @return Liste des jeux chargés
     */
    public List<BoardGame> loadFromJson(String filename) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File file = new File(filename);
            return mapper.readValue(file, new TypeReference<List<BoardGame>>() {});
        } catch (IOException e) {
            System.out.println("Error loading from JSON: " + e.getMessage());
        }
        return new ArrayList<>();
    }
    
    /**
     * Sauvegarde les jeux dans un fichier JSON.
     * @param filename Chemin du fichier JSON
     * @param games Liste des jeux à sauvegarder
     */
    public void saveToJson(String filename, List<BoardGame> games) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filename), games);
        } catch (IOException e) {
            System.out.println("Error saving to JSON: " + e.getMessage());
        }
    }
}
