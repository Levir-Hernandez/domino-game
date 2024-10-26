package com.domino.app.controller;

import com.domino.app.model.*;
import com.domino.app.view.DominoView;
import com.domino.app.view.DominoViewImp;

import java.util.Random;
import java.util.Scanner;
import java.util.function.IntPredicate;

/**
 * Manages the flow of the domino game, coordinating between the model and the view.
 * <p>
 * The {@code DominoController} handles user input, game logic, and interaction
 * with the {@code DominoModel} and {@code DominoView}. It is responsible for
 * starting the game, managing player turns, and requesting player actions
 * through console input.
 * </p>
 * <p>
 * The controller defines the main game loop, processes player actions, and
 * updates the view accordingly. It also facilitates game configuration settings
 * and manages the transition between different game states.
 * </p>
 */
public class DominoController
{
    private Scanner input; // Scanner for user input
    private DominoModel model; // The domino game model
    private DominoView view; // The view for displaying game information

    /**
     * Entry point of the Domino application.
     * Initializes the model, view, and starts the game.
     */
    public static void main(String[] args)
    {
        DominoModel dominoModel = new DominoModelImp();
        DominoView dominoView = new DominoViewImp();

        DominoController dominoController = new DominoController();
        dominoController.setModel(dominoModel);
        dominoController.setView(dominoView);

        dominoController.startDominoGame();
    }

    /**
     * Constructs a DominoController and initializes the input scanner.
     */
    public DominoController()
    {
        input = new Scanner(System.in);
        model = null;
        view = null;
    }

    /**
     * Gets the current domino model.
     * @return The current domino model.
     */
    public DominoModel getModel() {return model;}

    /**
     * Sets the domino model for this controller.
     * @param model The domino model to be set.
     */
    public void setModel(DominoModel model) {this.model = model;}

    /**
     * Sets the view for this controller and links the controller to the view.
     * @param view The view to be set.
     */
    public void setView(DominoView view) {
        this.view = view;
        this.view.setController(this);
    }

    /**
     * Starts the domino game by displaying the main menu and processing the selected option.
     */
    private void startDominoGame()
    {
        view.displayMainMenu();

        int option = requestMenuOption();
        view.displaySeparatorEndOfSection();
        switch (option)
        {
            case 0 -> startDominoMatch();
            case 1 -> requestConfigSettings();
            case 2 -> exitDominoGame();
        }
    }

    /**
     * Starts a domino match and processes turns until a winner is found or the match is drawn.
     */
    public void startDominoMatch()
    {
        view.resetViewData();
        model.startMatch();

        view.displayDominoMatchHeader();
        while(!model.hasWinner() && !model.hasDraw())
        {
            view.displayMatchResume();
            if(model.canCurrentPlayerPlayTile())
            {
                view.displayCurrentPlayerPlayableTiles();
                chooseSideToPlayTile(model.currentPlayerPlayTile(requestTileToPlay()));
            }
            else if(!model.isBoneyardEmpty())
            {
                Tile.AttachSide attachSide = model.currentPlayerDrawTile();
                view.displayDrawMessage();
                chooseSideToPlayTile(attachSide);
            }
            else
            {
                view.displayPassAction();
                model.currentPlayerPassTurn();
            }
            view.displayPlayerMoveMade();
            model.advanceTurn();
        }

        view.displayEndMatchSummary();
        view.displayEndMatchMenu();

        int option = requestEndMatchOpt();
        view.displaySeparatorEndOfSection();
        switch (option)
        {
            case 0 -> startDominoGame();
            case 1 -> startDominoMatch();
            case 2 -> exitDominoGame();
        }
    }

    /**
     * Chooses the side to play the tile based on the attach side available.
     * @param attachSide The side where the tile can be attached.
     */
    public void chooseSideToPlayTile(Tile.AttachSide attachSide)
    {
        switch (attachSide)
        {
            case BOTH ->
            {
                view.displaySideToPlayLastTile();
                switch (requestSideToPlay())
                {
                    case 0 -> model.currentPlayerPlayTileLeft();
                    case 1 -> model.currentPlayerPlayTileRight();
                }
            }
        }
    }

    /**
     * Manages the configuration settings for the domino game.
     */
    public void requestConfigSettings()
    {
        boolean configuring = true;
        DominoSettings lastDominoSettings = model.getSettings();
        while(configuring)
        {
            view.displaySettingsMenu();

            int option = requestFeature();
            view.displaySeparatorSection();
            switch (option)
            {
                case 0 -> requestNumOfPlayers();
                case 1 -> requestMinDots();
                case 2 -> requestMaxDots();
                case 3 -> requestOpponentsTilesFlag();
                case 4 -> requestBoneyardTilesFlag();
                case 5 ->
                {
                    configuring = false;
                    view.displayChangesSaved();
                }
                case 6 ->
                {
                    getModel().setSettings(lastDominoSettings);
                    configuring = false;
                    view.displayChangesDiscarded();
                }
            }
        }
        startDominoGame();
    }

    /**
     * Requests the user to select a menu option.
     * @return The selected option (0-2).
     */
    public int requestMenuOption()
    {
        return requestInput(option -> 0 <= option && option <= 2,
                view::displayRequestNumberMessage,
                view::displayInvalidOptionMessage);
    }

    /**
     * Requests the user to select an option at the end of the match.
     * @return The selected option (0-2).
     */
    public int requestEndMatchOpt()
    {
        return requestInput(option -> 0 <= option && option <= 2,
                view::displayRequestNumberMessage,
                view::displayInvalidOptionMessage);
    }

    /**
     * Requests the user to select a side to play the tile.
     * @return The selected side (0 or 1).
     */
    public int requestSideToPlay()
    {
        if(model.getCurrentPlayer() instanceof CPUPlayer)
        {
            view.displayRequestNumberMessage();
            int opt = new Random().nextInt(2);
            System.out.println(opt);
            return opt;
        }
        else
        {
            return requestInput(option -> 0 <= option && option < 2,
                    view::displayRequestNumberMessage,
                    view::displayInvalidOptionMessage);
        }
    }

    /**
     * Requests the user to select a tile to play.
     * @return The index of the tile to play.
     */
    public int requestTileToPlay()
    {
        if(model.getCurrentPlayer() instanceof CPUPlayer)
        {
            view.displayRequestNumberMessage();
            int opt = new Random().nextInt(model.getCurrentPlayerPlayableTiles().size());
            System.out.println(opt);
            return opt;
        }
        else
        {
            return requestInput(option -> 0 <= option && option < model.getCurrentPlayerPlayableTiles().size(),
                    view::displayRequestNumberMessage,
                    view::displayInvalidOptionMessage);
        }
    }

    /**
     * Requests the user to select a feature in the settings menu.
     * @return The selected option (0-6).
     */
    public int requestFeature()
    {
        return requestInput(option -> 0 <= option && option <= 6,
                view::displayFeatureMessage,
                view::displayInvalidOptionMessage);
    }

    /**
     * Requests input from the user, validating it against the specified criteria.
     * @param isInputValid A predicate that defines the valid input range.
     * @param displayRequestMessage A runnable to display the request message.
     * @param displayErrorMessage A runnable to display an error message.
     * @return The valid input from the user.
     */
    public int requestInput(IntPredicate isInputValid,
                            Runnable displayRequestMessage,
                            Runnable displayErrorMessage)
    {
        // Display the initial request message to the user
        displayRequestMessage.run();

        // Loop until the input meets the acceptable condition
        int option;
        while (!input.hasNextInt() || !isInputValid.test(option = input.nextInt()))
        {
            view.displaySeparatorSection();
            displayErrorMessage.run();    // Display an error message
            view.displaySeparatorSection();
            displayRequestMessage.run();  // Ask for input again
        }

        return option;
    }

    /**
     * Requests text input from the user, specifically for visibility options.
     * @param displayRequestMessage A runnable to display the request message.
     * @return The user-selected visibility option (VISIBLE or HIDDEN).
     */
    public String requestTextInput(Runnable displayRequestMessage)
    {
        // Display the initial request message to the user
        displayRequestMessage.run();

        // Loop until the input meets the acceptable condition
        String option;
        while (!input.hasNext("(?i)VISIBLE|HIDDEN")) // case-insensitivity
        {
            option = input.next();
            view.displaySeparatorSection();
            view.displayInvalidInputMessage(); // Display an error message
            view.displaySeparatorSection();
            displayRequestMessage.run();  // Ask for input again
        }
        option = input.next();

        return option;
    }

    /**
     * Requests the number of players for the game.
     */
    public void requestNumOfPlayers()
    {
        getModel().setNumOfPlayers(
                requestInput(input -> 2 <= input && input <= 4,
                        view::displayNumOfPlayersMessage,
                        view::displayInvalidInputMessage)
        );
        view.displaySeparatorSection();

        getModel().setNumOfHumanPlayers(
                requestInput(input -> 0 <= input && input < getModel().getNumOfPlayers(),
                        view::displayNumOfHumanPlayersMessage,
                        view::displayInvalidInputMessage)
        );
        view.displaySeparatorEndOfSection();
    }

    /**
     * Requests the minimum number of dots allowed for the tiles in the game.
     */
    public void requestMinDots()
    {
        getModel().setMinDots(
                requestInput(input -> 0 <= input && input < getModel().getMaxDots(),
                view::displayMinDotsMessage,
                view::displayInvalidInputMessage)
        );
        view.displaySeparatorEndOfSection();
    }

    /**
     * Requests the maximum number of dots allowed for the tiles in the game.
     */
    public void requestMaxDots()
    {
        getModel().setMaxDots(
                requestInput(input -> input > 0,
                        view::displayMaxDotsMessage,
                        view::displayInvalidInputMessage)
        );
        view.displaySeparatorEndOfSection();
    }

    /**
     * Requests the user's preference for displaying opponent's tiles (visible or hidden).
     */
    public void requestOpponentsTilesFlag()
    {
        String option = requestTextInput(view::displayOpponentsTilesMessage);
        getModel().setHideOpponentTiles(option.equals("HIDDEN"));
        view.displaySeparatorEndOfSection();
    }

    /**
     * Requests the user's preference for displaying boneyard tiles (visible or hidden).
     */
    public void requestBoneyardTilesFlag()
    {
        String option = requestTextInput(view::displayBoneyardTilesMessage);
        getModel().setHideBoneyardTiles(option.equals("HIDDEN"));
        view.displaySeparatorEndOfSection();
    }

    /**
     * Exits the domino game, displaying a farewell message to the user.
     */
    public void exitDominoGame()
    {
        view.displayExitMessage();
    }
}
