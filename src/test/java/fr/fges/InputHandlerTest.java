package fr.fges;

import java.util.Optional;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import fr.fges.ui.InputHandler;
public class InputHandlerTest {

    @Test
    void readInt_withValidNumber_returnsValue() {
        // Arrange
        Scanner scanner = new Scanner("42");
        InputHandler inputHandler = new InputHandler(scanner);

        // Act
        Optional<Integer> result = inputHandler.readInt("Enter number");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(42, result.get());
    }

    @Test
    void readInt_withInvalidInput_returnsEmpty() {
        // Arrange
        Scanner scanner = new Scanner("not a number");
        InputHandler inputHandler = new InputHandler(scanner);

        // Act
        Optional<Integer> result = inputHandler.readInt("Enter number");

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void readInt_withEmptyInput_returnsEmpty() {
        // Arrange
        Scanner scanner = new Scanner("\n");
        InputHandler inputHandler = new InputHandler(scanner);

        // Act
        Optional<Integer> result = inputHandler.readInt("Enter number");

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void readInt_withNegativeNumber_returnsValue() {
        // Arrange
        Scanner scanner = new Scanner("-5");
        InputHandler inputHandler = new InputHandler(scanner);

        // Act
        Optional<Integer> result = inputHandler.readInt("Enter number");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(-5, result.get());
    }

    @Test
    void readString_returnsUserInput() {
        // Arrange
        Scanner scanner = new Scanner("Catan");
        InputHandler inputHandler = new InputHandler(scanner);

        // Act
        String result = inputHandler.readString("Game title");

        // Assert
        assertEquals("Catan", result);
    }

    @Test
    void readString_withSpaces_returnsFullString() {
        // Arrange
        Scanner scanner = new Scanner("Ticket to Ride");
        InputHandler inputHandler = new InputHandler(scanner);

        // Act
        String result = inputHandler.readString("Game title");

        // Assert
        assertEquals("Ticket to Ride", result);
    }

    @Test
    void readLine_returnsUserInput() {
        // Arrange
        Scanner scanner = new Scanner("test input");
        InputHandler inputHandler = new InputHandler(scanner);

        // Act
        String result = inputHandler.readLine();

        // Assert
        assertEquals("test input", result);
    }

    @Test
    void readMenuChoice_returnsChoice() {
        // Arrange
        Scanner scanner = new Scanner("3");
        InputHandler inputHandler = new InputHandler(scanner);

        // Act
        String result = inputHandler.readMenuChoice();

        // Assert
        assertEquals("3", result);
    }

}
