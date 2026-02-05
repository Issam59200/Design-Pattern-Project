package fr.fges;

import java.util.Stack;

/**
 * Gestionnaire d'historique des commandes.
 * Permet d'enregistrer et d'annuler les dernières actions.
 */
public class CommandManager {
    private final Stack<Command> commandHistory;

    public CommandManager() {
        this.commandHistory = new Stack<>();
    }

    /**
     * Exécute une commande et l'ajoute à l'historique.
     * @param command La commande à exécuter
     */
    public void executeCommand(Command command) {
        command.execute();
        commandHistory.push(command);
    }

    /**
     * Annule la dernière commande exécutée.
     * @return true si une commande a été annulée, false si l'historique est vide
     */
    public boolean undoLastCommand() {
        if (commandHistory.isEmpty()) {
            return false;
        }
        Command lastCommand = commandHistory.pop();
        lastCommand.undo();
        return true;
    }

    /**
     * Vérifie si l'historique est vide.
     * @return true si aucune commande n'est disponible pour undo
     */
    public boolean isEmpty() {
        return commandHistory.isEmpty();
    }
}
