package fr.fges.ui;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Registre des actions du menu.
 * Encapsule toutes les actions disponibles et les affiche dynamiquement.
 * Remplace les switch cases du Menu et permet d'ajouter/retirer des actions facilement.
 */
public class MenuRegistry {
    private final Map<String, MenuAction> actions = new LinkedHashMap<>();
    private boolean isWeekend;

    public MenuRegistry(boolean isWeekend) {
        this.isWeekend = isWeekend;
    }

    /**
     * Enregistre une action dans le registre.
     * @param key Identifiant unique (exemple: "1", "2", etc.)
     * @param action L'action à enregistrer
     */
    public void register(String key, MenuAction action) {
        actions.put(key, action);
    }

    /**
     * Affiche les actions disponibles selon le jour.
     */
    public void displayMenu() {
        System.out.println("=== Board Game Collection ===");
        int num = 1;
        for (MenuAction action : actions.values()) {
            if (isWeekend && action.availableOnWeekend()) {
                System.out.println(num++ + ". " + action.label());
            } else if (!isWeekend && action.availableOnWeekday()) {
                System.out.println(num++ + ". " + action.label());
            }
        }
        System.out.println("Please select an option:");
    }

    /**
     * Exécute l'action correspondant au choix utilisateur.
     * @param choice L'indice de l'action (1-basé)
     * @return true si l'action a été exécutée, false sinon
     */
    public boolean execute(int choice) {
        int count = 0;
        for (MenuAction action : actions.values()) {
            if (isWeekend && action.availableOnWeekend()) {
                count++;
                if (count == choice) {
                    action.execute();
                    return true;
                }
            } else if (!isWeekend && action.availableOnWeekday()) {
                count++;
                if (count == choice) {
                    action.execute();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Exécute l'action correspondant à la clé spécifiée.
     * @param key L'identifiant de l'action
     * @return true si l'action a été exécutée, false sinon (action non trouvée ou non disponible)
     */
    public boolean executeByKey(String key) {
        Optional<MenuAction> action = Optional.ofNullable(actions.get(key));
        if (action.isEmpty()) {
            return false;
        }

        MenuAction menuAction = action.get();
        if (isWeekend && !menuAction.availableOnWeekend()) {
            return false;
        }
        if (!isWeekend && !menuAction.availableOnWeekday()) {
            return false;
        }

        menuAction.execute();
        return true;
    }

    /**
     * Met à jour le statut du jour.
     * @param isWeekend true si on est le weekend
     */
    public void setWeekend(boolean isWeekend) {
        this.isWeekend = isWeekend;
    }

    /**
     * Retourne le nombre d'actions disponibles pour la journée actuelle.
     */
    public int getAvailableActionCount() {
        return (int) actions.values().stream()
                .filter(a -> (isWeekend && a.availableOnWeekend()) || (!isWeekend && a.availableOnWeekday()))
                .count();
    }
}
