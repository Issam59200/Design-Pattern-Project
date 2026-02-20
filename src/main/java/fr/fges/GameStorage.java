package fr.fges;

import java.util.List;

public interface  GameStorage {
    List<BoardGame> load(String filename);
    void save(String filename, List<BoardGame> games);
}
