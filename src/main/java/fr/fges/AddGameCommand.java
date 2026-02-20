package fr.fges;

/**
 * Commande pour ajouter un jeu à la collection.
 * Execute: ajoute le jeu | Undo: retire le jeu.
 */
public class AddGameCommand implements Command {
    private final GameRepository repository;
    private final GameStorage storage;
    private final String storageFile;
    private final BoardGame game;

    public AddGameCommand(GameRepository repository, GameStorage storage, String storageFile, BoardGame game) {
        this.repository = repository;
        this.storage = storage;
        this.storageFile = storageFile;
        this.game = game;
    }

    @Override
    public void execute() {
        repository.addGame(game);
        storage.save(storageFile, repository.getGames());
    }

    @Override
    public void undo() {
        repository.removeGame(game);
        storage.save(storageFile, repository.getGames());
        System.out.println("Undone: Removed \"" + game.title() + "\" from collection");
    }

    @Override
    public String getDescription() {
        return "Add game: " + game.title();
    }
}
