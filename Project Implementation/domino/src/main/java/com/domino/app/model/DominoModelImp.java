package com.domino.app.model;

import java.util.*;

/**
 * The implementation of the {@link DominoModel} interface.
 * This class manages the game state, including the board, boneyard, players,
 * and game settings. It provides methods to control the flow of the game,
 * handle player actions, and determine game outcomes.
 */
public class DominoModelImp implements DominoModel
{
    // Game components
    private Board board; // The board where the game is played
    private Boneyard boneyard; // The pool of unused tiles
    private List<Player> players; // List of players participating in the game

    // Game state variables
    private int currentTurn; // Index of the player whose turn it is
    private int consecutivePassCount; // Count of consecutive passes made by players

    // Last action variables
    private Tile lastDrawnTile; // The last tile drawn by the current player
    private Tile lastPlayedTile; // The last tile played on the board
    private DominoModel.GameAction lastGameAction; // The last action performed in the game

    private DominoSettings settings; // The settings for the current game

    /**
     * Constructs a new instance of DominoModelImp.
     * Initializes the board, boneyard, and game state variables.
     * Prepares the players list for the specified number of players.
     */
    public DominoModelImp()
    {
        this.board = new Board();
        this.boneyard = new Boneyard();

        this.currentTurn = -1;
        this.consecutivePassCount = 0;

        this.lastDrawnTile = null;
        this.lastPlayedTile = null;
        this.lastGameAction = null;
        this.settings = new DominoSettings();

        this.players = new ArrayList<>(getNumOfPlayers());
    }

    // Settings methods
    public int getNumOfPlayers() {return settings.getNumOfPlayers();}
    public void setNumOfPlayers(int numOfPlayers) {settings.setNumOfPlayers(numOfPlayers);}

    public int getNumOfHumanPlayers() {return settings.getNumOfHumanPlayers();}
    public void setNumOfHumanPlayers(int numOfHumanPlayers) {settings.setNumOfHumanPlayers(numOfHumanPlayers);}

    public int getMinDots() {return settings.getMinDots();}
    public void setMinDots(int minDots) {settings.setMinDots(minDots);}

    public int getMaxDots() {return settings.getMaxDots();}
    public void setMaxDots(int maxDots) {settings.setMaxDots(maxDots);}

    public boolean isHideOpponentTiles() {return settings.isHideOpponentTiles();}
    public void setHideOpponentTiles(boolean hideOpponentTiles) {settings.setHideOpponentTiles(hideOpponentTiles);}

    public boolean isHideBoneyardTiles() {return settings.isHideBoneyardTiles();}
    public void setHideBoneyardTiles(boolean hideBoneyardTiles) {settings.setHideBoneyardTiles(hideBoneyardTiles);}

    public DominoSettings getSettings() {return settings.clone();}
    public void setSettings(DominoSettings settings) {this.settings = settings;}

    public DominoModel.GameAction getLastGameAction() {return lastGameAction;}

    // Control methods
    public void startMatch()
    {
        // Clear the end state of the last match and apply the new settings
        resetGameData();
        applySettings();
        // Re-deal the tiles if no player has a double tile
        boolean anyPlayerHasDoubleTile = false;
        while(!anyPlayerHasDoubleTile)
        {
            dealTiles();
            anyPlayerHasDoubleTile = players.stream().anyMatch(Player::hasAnyDoubleTile);
        }
        pickStartingPlayer();
    }

    public int getWinnerTurn()
    {
        if(hasWinner()) return players.indexOf(getRegularWinner());
        else if(hasDraw()) return players.indexOf(getWinnerByDraw());
        else return -1;
    }

    public boolean hasWinner()
    {
        return players.stream().anyMatch(p -> !p.hasAnyTile());
    }

    public boolean hasDraw()
    {
        return consecutivePassCount == players.size()
                && isBoneyardEmpty()
                && players.stream().allMatch(Player::hasAnyTile);
    }

    public void advanceTurn()
    {
        currentTurn = (currentTurn+1) % players.size();
    }

    // Access methods
    public boolean isBoardEmpty() {return board.isEmpty();}
    public boolean isBoneyardEmpty() {return boneyard.isEmpty();}

    public List<Tile> getBoneyardTiles(){return boneyard.getTiles();}
    public List<Tile> getBoardTiles(){return board.getTiles();}
    public List<Tile> getBoardTilesExcludingLastAdded(){return board.getTilesExcludingLastAdded();}

    public Player getPlayer(int index){return players.get(index);}
    public Player getCurrentPlayer(){return getPlayer(currentTurn);}
    public int getCurrentTurn(){return currentTurn;}

    public List<Tile> getCurrentPlayerTiles(){return getCurrentPlayer().getTiles();}
    public List<Tile> getCurrentPlayerPlayableTiles()
    {
        Tile endsBoardTile = getEndsBoardTile();
        if(endsBoardTile == null) return getCurrentPlayerTiles();
        else
        {
            return getCurrentPlayerTiles().stream()
                    .filter(tile -> endsBoardTile.canAttach(tile).ordinal() > Tile.AttachSide.NONE.ordinal())
                    .toList();
        }
    }

    public boolean canCurrentPlayerPlayTile(){return !getCurrentPlayerPlayableTiles().isEmpty();}

    public Tile getEndsBoardTile(){return board.getEndsBoardTile();}
    public Tile getLastDrawnTile() {return lastDrawnTile;}
    public Tile getLastPlayedTile() {return lastPlayedTile;}

    // Play methods
    public Tile.AttachSide currentPlayerPlayTile(int indexFromPlayableTiles)
    {
        int index = getCurrentPlayerTiles().indexOf(getCurrentPlayerPlayableTiles().get(indexFromPlayableTiles));
        Tile tile = getCurrentPlayer().grabTile(index);
        return currentPlayerPlayTile(tile);
    }

    public boolean currentPlayerPlayTileLeft()
    {
        return currentPlayerPlayTileLeft(lastPlayedTile);
    }

    public boolean currentPlayerPlayTileRight()
    {
        return currentPlayerPlayTileRight(lastPlayedTile);
    }

    public Tile.AttachSide currentPlayerDrawTile()
    {
        Tile lastDrawnTile = boneyard.releaseTile();

        // Save the stolen tile from the override in the play method.
        Tile.AttachSide attachSide = currentPlayerPlayTile(lastDrawnTile);

        this.lastDrawnTile = lastDrawnTile;

        switch (lastGameAction)
        {
            case PASS -> lastGameAction = GameAction.DRAW_AND_PASS;
            case PLAY_LEFT -> lastGameAction = GameAction.DRAW_AND_PLAY_LEFT;
            case PLAY_RIGHT -> lastGameAction = GameAction.DRAW_AND_PLAY_RIGHT;
        }

        return attachSide;
    }

    public void currentPlayerPassTurn()
    {
        lastDrawnTile = null;
        lastPlayedTile = null;
        lastGameAction = GameAction.PASS;
        increasePassCount();
    }

    // Private control methods

    /**
     * Applies the current game settings by initializing the player list.
     * Adds the specified number of human players and CPU players to the game.
     */
    private void applySettings()
    {
        for(int i = 0; i < getNumOfHumanPlayers(); i++)
        {
            players.add(new HumanPlayer());
        }

        for(int i = 0; i < getNumOfPlayers()-getNumOfHumanPlayers(); i++)
        {
            players.add(new CPUPlayer());
        }
    }

    /**
     * Resets the game data to prepare for a new match.
     * Initializes the board, boneyard, player list, and game state variables.
     */
    private void resetGameData()
    {
        this.board = new Board();
        this.boneyard = new Boneyard();
        this.players = new ArrayList<>(getNumOfPlayers());

        this.currentTurn = -1;
        this.consecutivePassCount = 0;

        this.lastDrawnTile = null;
        this.lastPlayedTile = null;
        this.lastGameAction = null;
    }

    /**
     * Picks the starting player for the match based on the highest double tile.
     * If no player has a double tile, the starting player is set to null.
     */
    private void pickStartingPlayer()
    {
        Player startingPlayer = players.stream()
                .filter(Player::hasAnyDoubleTile)
                .max(Comparator.comparing(p -> p.getBiggestDouble().getLeft()))
                .orElse(null);
        currentTurn = players.indexOf(startingPlayer);
    }

    /**
     * Determines the winner by draw, which is the player with the lowest sum of tiles.
     * @return The player with the lowest tile sum, or null if no players exist.
     */
    private Player getWinnerByDraw()
    {
        return players.stream().min(Comparator.comparing(Player::getTilesSum)).orElse(null);
    }

    /**
     * Determines the regular winner, which is the first player with no tiles left.
     * @return The player with no tiles, or null if no such player exists.
     */
    private Player getRegularWinner()
    {
        return players.stream().filter(p -> !p.hasAnyTile()).findFirst().orElse(null);
    }

    /**
     * Deals tiles to each player from the boneyard.
     * Creates all possible tiles based on the min and max dots,
     * shuffles them, and distributes an equal number of tiles to each player.
     */
    private void dealTiles()
    {
        for (int left = getMinDots(); left <= getMaxDots(); left++)
        {
            for (int right = left; right <= getMaxDots(); right++)
            {
                boneyard.addTile(new Tile(left, right));
            }
        }

        boneyard.shuffleTiles();

        int nTiles = boneyard.getTileCount();
        for(Player player: players)
        {
            for(int i=0; i < nTiles/4; i++)
            {
                player.addTile(boneyard.releaseTile());
            }
        }
    }

    // Game methods

    /**
     * Resets the consecutive pass count to zero.
     */
    private void clearPassCount(){consecutivePassCount = 0;}

    /**
     * Increments the consecutive pass count by one.
     */
    private void increasePassCount(){consecutivePassCount++;}

    /**
     * Handles the current player's attempt to play a tile.
     * Determines if the tile can be attached to the board's ends and processes accordingly.
     *
     * @param tile The tile the current player wants to play.
     * @return The side of the board the tile is attached to.
     */
    private Tile.AttachSide currentPlayerPlayTile(Tile tile)
    {
        Tile.AttachSide attachSide;
        Tile endsBoardTile = getEndsBoardTile();
        if(endsBoardTile == null) attachSide = Tile.AttachSide.LEFT;
        else attachSide = endsBoardTile.canAttach(tile);

        switch(attachSide)
        {
            case NONE -> currentPlayerPassTurn();
            case LEFT -> currentPlayerPlayTileLeft(tile);
            case RIGHT -> currentPlayerPlayTileRight(tile);
            case BOTH -> {/*manage this in the controller*/}
        }

        lastDrawnTile = null;
        if(attachSide.ordinal() > Tile.AttachSide.NONE.ordinal())
        {
            lastPlayedTile = tile;
        }

        return attachSide;
    }

    /**
     * Processes the current player's action to play a tile on the left end of the board.
     *
     * @param tile The tile to be played.
     * @return True if the tile was successfully added, otherwise false.
     */
    private boolean currentPlayerPlayTileLeft(Tile tile)
    {
        clearPassCount();
        lastGameAction = GameAction.PLAY_LEFT;
        return board.addTileAtLeftEnd(tile);
    }

    /**
     * Processes the current player's action to play a tile on the right end of the board.
     *
     * @param tile The tile to be played.
     * @return True if the tile was successfully added, otherwise false.
     */
    private boolean currentPlayerPlayTileRight(Tile tile)
    {
        clearPassCount();
        lastGameAction = GameAction.PLAY_RIGHT;
        return board.addTileAtRightEnd(tile);
    }
}
