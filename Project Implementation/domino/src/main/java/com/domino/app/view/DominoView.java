package com.domino.app.view;

import com.domino.app.controller.DominoController;

/**
 * The {@code DominoView} interface defines the methods for displaying various
 * messages and menus in a Domino game console application, as well as
 * managing user interactions with the view. This interface serves as the
 * primary view representation, allowing users to access game options,
 * configure settings, and view match updates.
 */
public interface DominoView
{
    /**
     * Sets the controller for this view.
     *
     * @param dominoController the {@code DominoController} that manages game actions
     */
    void setController(DominoController dominoController);

    /**
     * Resets the view's internal data to the initial state.
     */
    void resetViewData();

    /**
     * Displays the main menu for the Domino game.
     */
    void displayMainMenu();

    /**
     * Shows the settings menu where players can configure game options.
     */
    void displaySettingsMenu();

    /**
     * Displays the exit message when the user chooses to leave the game.
     */
    void displayExitMessage();

    /**
     * Displays the header information at the start of a new match.
     */
    void displayDominoMatchHeader();

    /**
     * Shows the playable tiles of the current player in the match.
     */
    void displayCurrentPlayerPlayableTiles();

    /**
     * Prompts the user to enter a specific number, typically for settings.
     */
    void displayRequestNumberMessage();

    /**
     * Displays which side the last tile was played on.
     */
    void displaySideToPlayLastTile();

    /**
     * Shows a summary of the ongoing match.
     */
    void displayMatchResume();

    /**
     * Indicates a player's move has been completed.
     */
    void displayPlayerMoveMade();

    /**
     * Shows a message when a player draws a tile from the boneyard.
     */
    void displayDrawMessage();

    /**
     * Shows a message when a player passes their turn.
     */
    void displayPassAction();

    /**
     * Displays a summary at the end of the match, including final scores.
     */
    void displayEndMatchSummary();

    /**
     * Shows the end-of-match menu with options for the next steps.
     */
    void displayEndMatchMenu();

    /**
     * Displays messages for different configurable features.
     */
    void displayFeatureMessage();

    /**
     * Prompts the user to select the number of players in the game.
     */
    void displayNumOfPlayersMessage();

    /**
     * Prompts the user to select the number of human players.
     */
    void displayNumOfHumanPlayersMessage();

    /**
     * Prompts the user to select the minimum number of dots on tiles.
     */
    void displayMinDotsMessage();

    /**
     * Prompts the user to select the maximum number of dots on tiles.
     */
    void displayMaxDotsMessage();

    /**
     * Displays the current visibility status of other players' tiles.
     */
    void displayOpponentsTilesMessage();

    /**
     * Displays the current visibility status of the boneyard tiles.
     */
    void displayBoneyardTilesMessage();

    /**
     * Shows a message confirming that changes have been saved.
     */
    void displayChangesSaved();

    /**
     * Shows a message indicating that changes have been discarded.
     */
    void displayChangesDiscarded();

    /**
     * Shows an error message for invalid menu option selection.
     */
    void displayInvalidOptionMessage();

    /**
     * Shows an error message when an invalid input is provided.
     */
    void displayInvalidInputMessage();

    /**
     * Displays a section separator for readability in the console output.
     */
    void displaySeparatorSection();

    /**
     * Displays an end-of-section separator for readability in the console output.
     */
    void displaySeparatorEndOfSection();
}
