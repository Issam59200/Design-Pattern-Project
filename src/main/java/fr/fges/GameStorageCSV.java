package fr.fges;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestionnaire de stockage CSV.
 * Responsabilité unique : lire et écrire des jeux au format CSV.
 */
public class GameStorageCSV implements GameStorage {
    
    /**
     * Charge les jeux depuis un fichier CSV.
     * @param filename Chemin du fichier CSV
     * @return Liste des jeux chargés
     */
    @Override
    public List<BoardGame> load(String filename) {
        List<BoardGame> loadedGames = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
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
    
    /**
     * Sauvegarde les jeux dans un fichier CSV.
     * @param filename Chemin du fichier CSV
     * @param games Liste des jeux à sauvegarder
     */
    @Override
    public void save(String filename, List<BoardGame> games) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
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
