package fr.fges;

public class ApplicationContext {
    private final GameRepository repository;
    private final GameStorage storage;
    private final GameDisplay display;
    private final GameRecommender recommender;

    public ApplicationContext(GameRepository repository, GameStorage storage, GameDisplay display, GameRecommender recommender) {
        this.repository = repository;
        this.storage = storage;
        this.display = display;
        this.recommender = recommender;
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

    public GameRecommender getRecommender() {
        return recommender;
    }
}