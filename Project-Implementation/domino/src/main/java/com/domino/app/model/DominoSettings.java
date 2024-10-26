package com.domino.app.model;

/**
 * Represents the settings for a game of domino, including the number of players,
 * the number of human players, and the range of pips (dots) on the tiles.
 * <p>
 * This class also provides options to hide opponent tiles and boneyard tiles during gameplay.
 * It implements the {@code Cloneable} interface to allow creating a copy of the settings.
 * </p>
 */
public class DominoSettings implements Cloneable
{
    private int numOfPlayers;
    private int numOfHumanPlayers;
    private int minDots;
    private int maxDots;
    private boolean hideOpponentTiles;
    private boolean hideBoneyardTiles;

    /**
     * Constructs a new instance of {@code DominoSettings} with default values:
     * <ul>
     *   <li>Number of players: 4</li>
     *   <li>Number of human players: 1</li>
     *   <li>Minimum dots: 0</li>
     *   <li>Maximum dots: 6</li>
     *   <li>Opponent tiles: hidden</li>
     *   <li>Boneyard tiles: hidden</li>
     * </ul>
     */
    public DominoSettings()
    {
        this.numOfPlayers = 4;
        this.numOfHumanPlayers = 1;
        this.minDots = 0;
        this.maxDots = 6;
        this.hideOpponentTiles = true;
        this.hideBoneyardTiles = true;
    }

    /**
     * Returns the number of players in the game.
     *
     * @return the number of players
     */
    public int getNumOfPlayers() {return numOfPlayers;}

    /**
     * Sets the number of players for the game.
     *
     * @param numOfPlayers the number of players to set
     */
    public void setNumOfPlayers(int numOfPlayers) {this.numOfPlayers = numOfPlayers;}

    /**
     * Returns the number of human players in the game.
     *
     * @return the number of human players
     */
    public int getNumOfHumanPlayers() {return numOfHumanPlayers;}

    /**
     * Sets the number of human players for the game.
     *
     * @param numOfHumanPlayers the number of human players to set
     */
    public void setNumOfHumanPlayers(int numOfHumanPlayers) {this.numOfHumanPlayers = numOfHumanPlayers;}

    /**
     * Returns the minimum number of dots on the tiles.
     *
     * @return the minimum dots
     */
    public int getMinDots() {return minDots;}

    /**
     * Sets the minimum number of dots on the tiles.
     *
     * @param minDots the minimum dots to set
     */
    public void setMinDots(int minDots) {this.minDots = minDots;}

    /**
     * Returns the maximum number of dots on the tiles.
     *
     * @return the maximum dots
     */
    public int getMaxDots() {return maxDots;}

    /**
     * Sets the maximum number of dots on the tiles.
     *
     * @param maxDots the maximum dots to set
     */
    public void setMaxDots(int maxDots) {this.maxDots = maxDots;}

    /**
     * Checks if opponent tiles should be hidden.
     *
     * @return true if opponent tiles are hidden, false otherwise
     */
    public boolean isHideOpponentTiles() {return hideOpponentTiles;}

    /**
     * Sets whether to hide opponent tiles.
     *
     * @param hideOpponentTiles true to hide opponent tiles, false to show them
     */
    public void setHideOpponentTiles(boolean hideOpponentTiles) {this.hideOpponentTiles = hideOpponentTiles;}

    /**
     * Checks if boneyard tiles should be hidden.
     *
     * @return true if boneyard tiles are hidden, false otherwise
     */
    public boolean isHideBoneyardTiles() {return hideBoneyardTiles;}

    /**
     * Sets whether to hide boneyard tiles.
     *
     * @param hideBoneyardTiles true to hide boneyard tiles, false to show them
     */
    public void setHideBoneyardTiles(boolean hideBoneyardTiles) {this.hideBoneyardTiles = hideBoneyardTiles;}

    /**
     * Creates and returns a copy of this {@code DominoSettings} object.
     *
     * @return a clone of this instance
     */
    @Override
    public DominoSettings clone()
    {
        try {return (DominoSettings) super.clone();}
        catch (CloneNotSupportedException e) {throw new AssertionError();}
    }
}
