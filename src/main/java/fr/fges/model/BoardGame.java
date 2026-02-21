package fr.fges.model;

public record BoardGame(
        String title,
        int minPlayers,
        int maxPlayers,
        String category
) {
}
