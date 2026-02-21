package fr.fges.command;

/**
 * Interface Command - Pattern Command pour implémenter Undo/Redo.
 * Permet d'encapsuler une action et son inverse.
 */
public interface Command {
    /** Exécute l'action */
    void execute();
    
    /** Annule l'action */
    void undo();
    
    /** Retourne une description de l'action */
    String getDescription();
}
