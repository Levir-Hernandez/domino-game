package com.domino.app.model;

import java.util.List;

/**
 * The {@code DominoModel} interface defines the core functionalities and behaviors
 * of the Domino game model. It provides methods to manage the game state, including
 * player actions, tile management, and game settings. Implementing this interface
 * allows for the creation of different game strategies and behaviors.
 */
public interface DominoModel
{
    /**
     * Represents the various actions a player can take during their turn.
     */
    enum GameAction
    {
        PASS, // No tile drawn or played
        DRAW_AND_PASS, // A tile drawn but not played
        DRAW_AND_PLAY_LEFT, // A tile drawn and played to the left
        DRAW_AND_PLAY_RIGHT, // A tile drawn and played to the right
        PLAY_LEFT, // A tile played to the left without drawing
        PLAY_RIGHT // A tile played to the right without drawing
    }

    /**
     * Starts a new match of Domino.
     */
    void startMatch();

    /**
     * Checks if there is a winner in the current match.
     *
     * @return {@code true} if there is a winner; {@code false} otherwise.
     */
    boolean hasWinner();

    /**
     * Checks if the match has ended in a draw.
     *
     * @return {@code true} if the match is a draw; {@code false} otherwise.
     */
    boolean hasDraw();

    /**
     * Determines if the current player can play a tile.
     *
     * @return {@code true} if the current player can play a tile; {@code false} otherwise.
     */
    boolean canCurrentPlayerPlayTile();

    /**
     * Allows the current player to play a tile by specifying the index of the tile.
     *
     * @param i The index of the tile to be played.
     * @return The side of the tile being played.
     */
    Tile.AttachSide currentPlayerPlayTile(int i);

    /**
     * Checks if the boneyard (the pile of remaining tiles) is empty.
     *
     * @return {@code true} if the boneyard is empty; {@code false} otherwise.
     */
    boolean isBoneyardEmpty();

    /**
     * Allows the current player to draw a tile from the boneyard.
     *
     * @return The side of the drawn tile.
     */
    Tile.AttachSide currentPlayerDrawTile();

    /**
     * Allows the current player to pass their turn.
     */
    void currentPlayerPassTurn();

    /**
     * Advances the turn to the next player.
     */
    void advanceTurn();

    /**
     * Determines if the current player has played a tile to the left.
     *
     * @return {@code true} if a tile was played to the left; {@code false} otherwise.
     */
    boolean currentPlayerPlayTileLeft();

    /**
     * Determines if the current player has played a tile to the right.
     *
     * @return {@code true} if a tile was played to the right; {@code false} otherwise.
     */
    boolean currentPlayerPlayTileRight();

    /**
     * Gets the current player in the match.
     *
     * @return The current player.
     */
    Player getCurrentPlayer();

    /**
     * Retrieves a list of tiles that the current player can play.
     *
     * @return A list of playable tiles for the current player.
     */
    List<Tile> getCurrentPlayerPlayableTiles();

    /**
     * Gets the settings for the current game.
     *
     * @return The current game settings.
     */
    DominoSettings getSettings();

    /**
     * Sets the settings for the current game.
     *
     * @param lastDominoSettings The settings to be applied.
     */
    void setSettings(DominoSettings lastDominoSettings);

    /**
     * Sets the number of players in the match.
     *
     * @param i The number of players.
     */
    void setNumOfPlayers(int i);

    /**
     * Sets the number of human players in the match.
     *
     * @param i The number of human players.
     */
    void setNumOfHumanPlayers(int i);

    /**
     * Sets the minimum number of dots on the tiles.
     *
     * @param i The minimum dots.
     */
    void setMinDots(int i);

    /**
     * Gets the maximum number of dots on the tiles.
     *
     * @return The maximum dots.
     */
    int getMaxDots();

    /**
     * Sets the maximum number of dots on the tiles.
     *
     * @param i The maximum dots.
     */
    void setMaxDots(int i);

    /**
     * Sets whether to hide the opponent's tiles from view.
     *
     * @param hidden {@code true} to hide opponent tiles; {@code false} to show them.
     */
    void setHideOpponentTiles(boolean hidden);

    /**
     * Sets whether to hide the boneyard tiles from view.
     *
     * @param hidden {@code true} to hide boneyard tiles; {@code false} to show them.
     */
    void setHideBoneyardTiles(boolean hidden);

    /**
     * Gets the number of human players in the match.
     *
     * @return The number of human players.
     */
    int getNumOfHumanPlayers();

    /**
     * Gets the total number of players in the match.
     *
     * @return The total number of players.
     */
    int getNumOfPlayers();

    /**
     * Gets the minimum number of dots on the tiles.
     *
     * @return The minimum dots.
     */
    int getMinDots();

    /**
     * Checks if opponent tiles are hidden from view.
     *
     * @return {@code true} if opponent tiles are hidden; {@code false} otherwise.
     */
    boolean isHideOpponentTiles();

    /**
     * Checks if boneyard tiles are hidden from view.
     *
     * @return {@code true} if boneyard tiles are hidden; {@code false} otherwise.
     */
    boolean isHideBoneyardTiles();

    /**
     * Gets the last played tile on the board.
     *
     * @return The last played tile.
     */
    Tile getLastPlayedTile();

    /**
     * Gets the turn index of the current player.
     *
     * @return The current turn index.
     */
    int getCurrentTurn();

    /**
     * Gets the last drawn tile by the current player.
     *
     * @return The last drawn tile.
     */
    Tile getLastDrawnTile();

    /**
     * Gets the turn index of the winner.
     *
     * @return The turn index of the winner.
     */
    int getWinnerTurn();

    /**
     * Gets the player at the specified turn index.
     *
     * @param playerTurn The turn index of the player.
     * @return The player at the specified turn index.
     */
    Player getPlayer(int playerTurn);

    /**
     * Gets the last game action performed.
     *
     * @return The last game action.
     */
    GameAction getLastGameAction();

    /**
     * Gets the tile at the ends of the board.
     *
     * @return The tile at the ends of the board.
     */
    Tile getEndsBoardTile();

    /**
     * Retrieves a list of tiles currently on the board.
     *
     * @return A list of tiles on the board.
     */
    List<Tile> getBoardTiles();

    /**
     * Retrieves a list of tiles on the board, excluding the last added tile.
     *
     * @return A list of tiles on the board excluding the last added tile.
     */
    List<Tile> getBoardTilesExcludingLastAdded();

    /**
     * Retrieves a list of tiles in the boneyard.
     *
     * @return A list of tiles in the boneyard.
     */
    List<Tile> getBoneyardTiles();

    /**
     * Checks if the board is empty.
     *
     * @return {@code true} if the board is empty; {@code false} otherwise.
     */
    boolean isBoardEmpty();
}
