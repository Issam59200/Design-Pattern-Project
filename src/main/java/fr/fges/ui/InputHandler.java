package fr.fges;

import java.util.Optional;
import java.util.Scanner;

public class InputHandler {

    private final Scanner scanner;

    public InputHandler(Scanner scanner) {
        this .scanner = scanner;
    }

    public String readString(String prompt) {
        System.out.printf("%s: ", prompt);
        return scanner.nextLine();
    }

    public String readLine() {
        return scanner.nextLine();
    }

    public Optional<Integer> readInt(String prompt) {
        System.out.printf("%s: ", prompt);
        String input = scanner.nextLine();

        try {
            return Optional.of(Integer.parseInt(input));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public String readMenuChoice() {
        return scanner.nextLine();
    }

    /**
     * Lit un entier dans une plage donnée avec validation.
     * Boucle jusqu'à obtenir une valeur valide.
     */
    public int readIntInRange(String prompt, int min, int max) {
        while (true) {
            Optional<Integer> result = readInt(prompt + " (" + min + "-" + max + ")");
            if (result.isPresent() && result.get() >= min && result.get() <= max) {
                return result.get();
            }
            System.out.println("Entrez un nombre entre " + min + " et " + max);
        }
    }
}
