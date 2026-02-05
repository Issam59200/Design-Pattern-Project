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

    public Optional<Integer> readint(String prompt) {
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
}
