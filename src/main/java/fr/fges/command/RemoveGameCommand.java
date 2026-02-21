package fr.fges.command;

/**
 * Commande pour retirer un jeu de la collection.
 * Execute: retire le jeu | Undo: rajoute le jeu.
 */
public class RemoveGameCommand implements Command {
    private final GameRepository repository;
    private final GameStorage storage;
    private final String storageFile;
    private final BoardGame game;

    public RemoveGameCommand(GameRepository repository, GameStorage storage, String storageFile, BoardGame game) {
        this.repository = repository;
        this.storage = storage;
        this.storageFile = storageFile;
        this.game = game;
    }

    @Override
    public void execute() {
        repository.removeGame(game);
        storage.save(storageFile, repository.getGames());
    }

    @Override
    public void undo() {
        repository.addGame(game);
        storage.save(storageFile, repository.getGames());
        System.out.println("Undone: Added \"" + game.title() + "\" back to collection");
    }

    @Override
    public String getDescription() {
        return "Remove game: " + game.title();
    }
}
