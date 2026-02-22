package fr.fges.ui;

/**
 * Interface pour les actions du menu.
 * Chaque option du menu (Add, Remove, List, etc.) implémente cette interface.
 */
public interface MenuAction {
    /**
     * Retourne le libellé de l'action pour l'affichage.
     * @return Texte à afficher dans le menu
     */
    String label();

    /**
     * Exécute l'action.
     */
    void execute();

    /**
     * Retourne vrai si cette action est applicable en semaine.
     * @return true si l'action s'affiche en semaine
     */
    boolean availableOnWeekday();

    /**
     * Retourne vrai si cette action est applicable le weekend.
     * @return true si l'action s'affiche le weekend
     */
    boolean availableOnWeekend();
}
