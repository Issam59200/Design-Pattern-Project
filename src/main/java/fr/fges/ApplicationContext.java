package fr.fges;

public class ApplicationContext {
    private final GameRepository repository;
    private final GameStorage storage;
    private final GameDisplay display;

    public ApplicationContext(GameRepository repository, GameStorage storage, GameDisplay display) {
        this.repository = repository;
        this.storage = storage;
        this.display = display;
    }

    public GameRepository getRepository() {
        return repository;
    }

    public GameStorage getStorage() {
        return storage;
    }

    public GameDisplay getDisplay() {
        return display;
    }
}