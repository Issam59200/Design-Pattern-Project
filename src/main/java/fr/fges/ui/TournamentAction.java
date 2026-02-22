package fr.fges.ui;

import java.util.List;
import java.util.Objects;

import fr.fges.model.BoardGame;
import fr.fges.service.TournamentService;
import fr.fges.tournament.FormatSelector;
import fr.fges.tournament.GameSelector;
import fr.fges.tournament.PlayerCollector;
import fr.fges.tournament.Tournament;
import fr.fges.tournament.TournamentDisplay;
import fr.fges.tournament.TournamentRunner;
import fr.fges.tournament.TournamentStrategy;

/**
 * Action de menu pour lancer un tournoi.
 * Orchestre les étapes via des classes dédiées (SRP).
 */
public class TournamentAction implements MenuAction {
    private final GameSelector gameSelector;
    private final PlayerCollector playerCollector;
    private final FormatSelector formatSelector;
    private final TournamentRunner tournamentRunner;
    private final TournamentService tournamentService;
    private final TournamentDisplay tournamentDisplay;

    public TournamentAction(TournamentService tournamentService,
                            TournamentDisplay tournamentDisplay,
                            InputHandler inputHandler) {
        Objects.requireNonNull(tournamentService);
        Objects.requireNonNull(tournamentDisplay);
        Objects.requireNonNull(inputHandler);

        this.tournamentService = tournamentService;
        this.tournamentDisplay = tournamentDisplay;
        this.gameSelector = new GameSelector(tournamentService, inputHandler);
        this.playerCollector = new PlayerCollector(inputHandler);
        this.formatSelector = new FormatSelector(inputHandler);
        this.tournamentRunner = new TournamentRunner(tournamentDisplay, inputHandler);
    }

    @Override
    public String label() {
        return "Lancer un tournoi";
    }

    @Override
    public void execute() {
        BoardGame game = gameSelector.selectGame();
        if (game == null) return;

        List<String> playerNames = playerCollector.collectPlayerNames();
        TournamentStrategy strategy = formatSelector.selectFormat();

        Tournament tournament = tournamentService.createTournament(game, playerNames, strategy);
        tournamentRunner.run(tournament);
        tournamentDisplay.displayFinalResults(tournament);
    }

    @Override
    public boolean availableOnWeekday() {
        return true;
    }

    @Override
    public boolean availableOnWeekend() {
        return true;
    }
}
