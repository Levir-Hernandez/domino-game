package com.domino.app.view;

import com.domino.app.controller.DominoController;
import com.domino.app.model.DominoModel;
import com.domino.app.model.Tile;

import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.ToIntFunction;

/**
 * The implementation of the {@link DominoView} interface.
 * This class handles the presentation layer of the Domino game,
 * providing methods to display game information, player actions,
 * and game status updates to the console.
 */
public class DominoViewImp implements DominoView {

    private DominoController controller; // The controller that manages game logic and interactions
    private StringJoiner summary; // A joiner to format and store the game summary
    private String lastMove; // The last move made in the game

    /**
     * Creates a new instance of {@code DominoViewImp}.
     * This constructor sets up the initial state of the view, including
     * the last move and controller references, and initializes the summary
     * joiner for output formatting.
     */
    public DominoViewImp()
    {
        lastMove = null;
        controller = null;
        summary = new StringJoiner("\n");
    }

    @Override
    public void setController(DominoController dominoController)
    {
        this.controller = dominoController;
    }

    @Override
    public void resetViewData()
    {
        lastMove = null;
        summary = new StringJoiner("\n");
    }

    @Override
    public void displayMainMenu() {
        displaySeparatorSection();
        System.out.println(" DOMINO GAME");
        displaySeparatorSection();
        System.out.printf(
                " (0) PLAY (%d HUMAN vs %d CPU)\n (1) SETTINGS\n (2) EXIT\n",
                getModel().getNumOfHumanPlayers(),
                getModel().getNumOfPlayers()-getModel().getNumOfHumanPlayers());
        displaySeparatorSection();
    }

    @Override
    public void displaySettingsMenu()
    {
        displaySeparatorSection();
        System.out.println(" DOMINO SETTINGS");
        displaySeparatorSection();

        System.out.printf(
                " (0) PLAYERS: %d HUMAN vs %d CPU\n" +
                        " (1) MIN PIPS: %d\n" +
                        " (2) MAX PIPS: %d\n" +
                        " (3) OPPONENTS TILES: %s\n" +
                        " (4) BONEYARD TILES: %s\n",
                getModel().getNumOfHumanPlayers(),
                getModel().getNumOfPlayers()-getModel().getNumOfHumanPlayers(),
                getModel().getMinDots(),
                getModel().getMaxDots(),
                booleanToOption(getModel().isHideOpponentTiles()),
                booleanToOption(getModel().isHideBoneyardTiles())
        );
        displaySeparatorSection();
        System.out.println(
                " (5) SAVE CHANGES\n" +
                        " (6) DISCARD CHANGES");
        displaySeparatorSection();
    }

    @Override
    public void displayExitMessage()
    {
        displaySeparatorSection();
        System.out.println(" EXITING GAME");
        displaySeparatorSection();
    }

    @Override
    public void displayDominoMatchHeader()
    {
        displaySeparatorSection();
        System.out.printf(
                " DOMINO MATCH: %d HUMAN vs %d CPU\n",
                getModel().getNumOfHumanPlayers(),
                getModel().getNumOfPlayers()-getModel().getNumOfHumanPlayers()
        );
    }

    @Override
    public void displayCurrentPlayerPlayableTiles()
    {
        System.out.println(" TILES TO PLAY");
        displaySeparatorSection();
        System.out.println(playableTilesRepr(getModel().getCurrentPlayerPlayableTiles()));
        displaySeparatorSection();
    }

    @Override
    public void displayRequestNumberMessage()
    {
        System.out.print(" SELECT: ");
    }

    @Override
    public void displaySideToPlayLastTile()
    {
        displaySeparatorSection();
        System.out.printf(" SIDE TO PLAY %s\n", regularTileRepr(getModel().getLastPlayedTile()));
        displaySeparatorSection();
        System.out.println(" (0) LEFT\n (1) RIGHT");
        displaySeparatorSection();
    }

    @Override
    public void displayMatchResume()
    {
        displaySeparatorSection();
        System.out.printf(" PLAYER'S TURN (J%d)%n", getModel().getCurrentTurn());
        displaySeparatorSection();
        displayHiddenPlayerTiles();
        displayHiddenBoneyardTiles();
        displaySeparatorSection();
        displayBoardLastMove();
    }

    @Override
    public void displayPlayerMoveMade()
    {
        displaySeparatorSection();
        System.out.println(" MOVE MADE");
        displaySeparatorSection();
        System.out.println(lastMoveRepr());
        displaySeparatorEndOfSection();
    }

    @Override
    public void displayDrawMessage()
    {
        System.out.println(" YOU CANNOT PLAY ANY TILE (DRAW)");
        displaySeparatorSection();
        System.out.printf(" TILE OBTAINED FROM THE BONEYARD: %s\n", regularTileRepr((getModel().getLastDrawnTile())));
    }

    @Override
    public void displayPassAction()
    {
        System.out.println(" YOU CANNOT PLAY ANY TILE");
    }

    @Override
    public void displayEndMatchSummary()
    {
        displaySeparatorSection();
        System.out.println(" END OF MATCH");

        displaySeparatorSection();
        System.out.println(" SUMMARY");
        displaySeparatorSection();
        System.out.println(summary);

        displaySeparatorSection();
        System.out.println(" REMAINING TILES");
        displaySeparatorSection();
        displayVisiblePlayerTiles();
        displayVisibleBoneyardTiles();
        displaySeparatorSection();

        if(getModel().hasDraw())
        {
            System.out.println(" TIEBREAKER");
            displaySeparatorSection();
            displayPlayersPoints();
            displaySeparatorSection();
        }

        System.out.printf(" WINNER (J%d)\n", getModel().getWinnerTurn());
        displaySeparatorEndOfSection();
    }

    @Override
    public void displayEndMatchMenu()
    {
        displaySeparatorSection();
        System.out.println(" END OF MATCH MENU");
        displaySeparatorSection();
        System.out.println(
                " (0) GO BACK TO MAIN MENU\n" +
                        " (1) PLAY AGAIN\n" +
                        " (2) QUIT"
        );
        displaySeparatorSection();
    }

    @Override
    public void displayFeatureMessage()
    {
        System.out.print(" SELECT FEATURE: ");
    }

    @Override
    public void displayNumOfPlayersMessage()
    {
        System.out.print(" 2 <= NUMBER OF PLAYERS <= 4: ");
    }

    @Override
    public void displayNumOfHumanPlayersMessage()
    {
        System.out.printf(" 0 <= HUMAN PLAYERS <= %s: ", getModel().getNumOfPlayers());
    }

    @Override
    public void displayMinDotsMessage()
    {
        System.out.printf(
                " 0 <= MIN DOTS < %d: "
                , getModel().getMaxDots()
        );
    }

    @Override
    public void displayMaxDotsMessage()
    {
        System.out.printf(
                " MAX DOTS > %d: "
                , getModel().getMinDots()
        );
    }

    @Override
    public void displayOpponentsTilesMessage()
    {
        System.out.print(" OTHER PLAYERS TILES (HIDDEN|VISIBLE): ");
    }

    @Override
    public void displayBoneyardTilesMessage()
    {
        System.out.print(" BONEYARD TILES (HIDDEN|VISIBLE): ");
    }

    @Override
    public void displayChangesSaved()
    {
        System.out.println(" CHANGES SAVED");
        displaySeparatorEndOfSection();
    }

    @Override
    public void displayChangesDiscarded()
    {
        System.out.println(" CHANGES DISCARDED");
        displaySeparatorEndOfSection();
    }

    @Override
    public void displayInvalidOptionMessage()
    {
        System.out.println(" INVALID OPTION - TRY AGAIN");
    }

    @Override
    public void displayInvalidInputMessage()
    {
        System.out.println(" INVALID INPUT - TRY AGAIN");
    }

    @Override
    public void displaySeparatorSection()
    {
        System.out.println("----------------------------------------------------");
    }

    @Override
    public void displaySeparatorEndOfSection()
    {
        displaySeparatorSection();
        System.out.println();
    }

    // implementation methods

    /**
     * Converts a boolean option to its string representation.
     *
     * @param option the boolean value to convert
     * @return "HIDDEN" if option is true, otherwise "VISIBLE"
     */
    public String booleanToOption(boolean option)
    {
        return option?"HIDDEN":"VISIBLE";
    }

    /**
     * Retrieves the associated DominoModel from the controller.
     *
     * @return the DominoModel
     */
    private DominoModel getModel() {return controller.getModel();}

    /**
     * Provides a string representation of a regular tile in the format [left|right].
     *
     * @param tile the Tile object to represent
     * @return the string representation of the tile
     */
    private String regularTileRepr(Tile tile)
    {
        return String.format("[%d|%d]", tile.getLeft(), tile.getRight());
    }

    /**
     * Provides a string representation of a hidden tile.
     *
     * @param tile the Tile object to represent
     * @return the string representation of the hidden tile
     */
    private String hiddenTileRepr(Tile tile)
    {
        return "[?|?]";
    }

    /**
     * Provides a string representation of a list of tiles indicating the number of tiles.
     *
     * @param tiles the list of Tile objects
     * @return the string representation of the tile count
     */
    private String tilesRepr(List<Tile> tiles)
    {
        return String.format("<%d>", tiles.size());
    }

    /**
     * Provides a string representation of the player's turn.
     *
     * @param playerTurn the index of the current player turn
     * @return the string representation of the player's turn
     */
    private String playerTurnRepr(int playerTurn)
    {
        return String.format(" (J%d)", playerTurn);
    }

    /**
     * Provides a string representation of the player's points.
     *
     * @param playerTurn the index of the player
     * @return the string representation of the player's points
     */
    private String playerPointsRepr(int playerTurn)
    {
        return String.format(" (J%d) {%d}", playerTurn, getModel().getPlayer(playerTurn).getTilesSum());
    }

    /**
     * Displays the points of all players in the console.
     */
    public void displayPlayersPoints()
    {
        StringJoiner joiner = new StringJoiner("\n");

        for (int turn=0; turn < getModel().getNumOfPlayers(); turn++)
        {
            joiner.add(playerPointsRepr(turn));
        }
        System.out.println(joiner);
    }

    /**
     * Provides a string representation of a player's move, including the current turn, action, board representation, and end tile representation.
     *
     * @param action the action taken by the player
     * @param boardRepr the string representation of the board
     * @param endTileRepr the string representation of the end tile
     * @return the string representation of the move
     */
    private String moveRepr(String action, String boardRepr, String endTileRepr)
    {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add(playerTurnRepr(getModel().getCurrentTurn()));
        joiner.add(action);
        joiner.add(boardRepr);
        joiner.add(action);
        joiner.add(endTileRepr);
        return joiner.toString();
    }

    /**
     * Provides a string representation of the last move made in the game.
     *
     * @return the string representation of the last move
     */
    private String lastMoveRepr()
    {
        StringJoiner joiner = new StringJoiner("\n");
        switch (getModel().getLastGameAction())
        {
            case PASS :
            {
                joiner.add(moveRepr("---", currentBoardRepr(), currentEndsBoardTileRepr()));
                break;
            }

            case DRAW_AND_PASS :
            {
                joiner.add(moveRepr("@@@", currentBoardRepr(), currentEndsBoardTileRepr()));
                joiner.add(moveRepr("+++", currentBoardRepr(), currentEndsBoardTileRepr()));
                break;
            }

            case DRAW_AND_PLAY_LEFT :
            {
                joiner.add(moveRepr("@@@", lastBoardRepr(), lastEndsBoardTileRepr()));
            }

            case PLAY_LEFT :
            {
                String currentBoardRepr = currentPlayedBoardRepr(boardRepr -> boardRepr.indexOf("-"));
                joiner.add(moveRepr("...", currentBoardRepr, currentEndsBoardTileRepr()));
                break;
            }

            case DRAW_AND_PLAY_RIGHT :
            {
                joiner.add(moveRepr("@@@", lastBoardRepr(), lastEndsBoardTileRepr()));
            }

            case PLAY_RIGHT :
            {
                String currentBoardRepr = currentPlayedBoardRepr(boardRepr -> boardRepr.lastIndexOf("-"));
                joiner.add(moveRepr("...", currentBoardRepr, currentEndsBoardTileRepr()));
                break;
            }
        }

        lastMove = joiner.toString();
        summary.add(lastMove);

        return joiner.toString();
    }

    /**
     * Provides a string representation of the current end tile of the board.
     *
     * @return the string representation of the current end tile
     */
    private String currentEndsBoardTileRepr()
    {
        Tile endsBoardTile = getModel().getEndsBoardTile();
        return String.format("<%d|%d>", endsBoardTile.getLeft(), endsBoardTile.getRight());
    }

    /**
     * Provides a string representation of the last end tile based on the last game action.
     *
     * @return the string representation of the last end tile
     */
    private String lastEndsBoardTileRepr()
    {
        Tile endsBoardTile = getModel().getEndsBoardTile();

        switch (getModel().getLastGameAction())
        {
            case PLAY_LEFT, DRAW_AND_PLAY_LEFT ->
            {
                Tile lastPlayedTile = getModel().getLastPlayedTile();
                endsBoardTile = new Tile(lastPlayedTile.getLeft(), endsBoardTile.getRight());
            }
            case PLAY_RIGHT, DRAW_AND_PLAY_RIGHT ->
            {
                Tile lastPlayedTile = getModel().getLastPlayedTile();
                endsBoardTile = new Tile(endsBoardTile.getLeft(), lastPlayedTile.getRight());
            }
        }

        return String.format("<%d|%d>", endsBoardTile.getLeft(), endsBoardTile.getRight());
    }

    /**
     * Provides a string representation of the current board state.
     *
     * @return the string representation of the current board
     */
    private String currentBoardRepr()
    {
        return boardTilesRepr(getModel().getBoardTiles());
    }

    /**
     * Provides a string representation of the last board state excluding the last added tile.
     *
     * @return the string representation of the last board
     */
    private String lastBoardRepr()
    {
        return boardTilesRepr(getModel().getBoardTilesExcludingLastAdded());
    }

    /**
     * Provides a string representation of the tiles on the board.
     *
     * @param boardTiles the list of tiles on the board
     * @return the string representation of the board tiles
     */
    private String boardTilesRepr(List<Tile> boardTiles)
    {
        StringJoiner joiner = new StringJoiner("-");
        if(boardTiles.size() < 4)
        {
            boardTiles.forEach(tile -> joiner.add(regularTileRepr(tile)));
        }
        else
        {
            joiner.add(regularTileRepr(boardTiles.getFirst()));
            joiner.add(String.format("[.%d.]", boardTiles.size()-2));
            joiner.add(regularTileRepr(boardTiles.getLast()));
        }

        return joiner.toString();
    }

    /**
     * Provides a string representation of the current played board based on the last game action.
     *
     * @param indexSearcher a function to search for the index of a specific tile representation
     * @return the string representation of the current played board
     */
    private String currentPlayedBoardRepr(ToIntFunction<StringBuilder> indexSearcher)
    {
        StringBuilder currentBoardRepr = new StringBuilder(lastBoardRepr());
        switch (getModel().getLastGameAction())
        {
            case PLAY_LEFT, DRAW_AND_PLAY_LEFT ->
            {
                StringJoiner joiner = new StringJoiner("-");
                joiner.add(regularTileRepr(getModel().getLastPlayedTile()));
                if(!currentBoardRepr.isEmpty()) joiner.add(currentBoardRepr);
                currentBoardRepr = new StringBuilder(joiner.toString());
            }
            case PLAY_RIGHT, DRAW_AND_PLAY_RIGHT ->
            {
                StringJoiner joiner = new StringJoiner("-");
                joiner.add(currentBoardRepr).add(regularTileRepr(getModel().getLastPlayedTile()));
                currentBoardRepr = new StringBuilder(joiner.toString());
            }
        }

        int index = indexSearcher.applyAsInt(currentBoardRepr);
        if(index >= 0)
        {
            currentBoardRepr.replace(index, index+1, "+");
        }
        return currentBoardRepr.toString();
    }

    /**
     * Displays the tiles in the boneyard. If the option to hide boneyard tiles is enabled,
     * the representation will be hidden; otherwise, it will show the regular representation.
     */
    public void displayHiddenBoneyardTiles()
    {
        List<Tile> boneyardTiles = getModel().getBoneyardTiles();
        String boneyardTilesRepr = getModel().isHideBoneyardTiles()?
                hiddenBoneyardRepr(boneyardTiles) : regularBoneyardRepr(boneyardTiles);
        System.out.println(boneyardTilesRepr);
    }

    /**
     * Displays the tiles in the boneyard without any restrictions.
     * This method shows the regular representation of the boneyard tiles,
     * regardless of the visibility options set by the user.
     */
    public void displayVisibleBoneyardTiles()
    {
        System.out.println(regularBoneyardRepr(getModel().getBoneyardTiles()));
    }

    /**
     * Creates a regular string representation of the boneyard tiles.
     *
     * @param boneyardTiles the list of tiles in the boneyard.
     * @return a string representation of the boneyard tiles.
     */
    private String regularBoneyardRepr(List<Tile> boneyardTiles)
    {
        return boneyardRepr(boneyardTiles, this::regularTileRepr);
    }

    /**
     * Creates a hidden string representation of the boneyard tiles.
     *
     * @param boneyardTiles the list of tiles in the boneyard.
     * @return a string representation of the hidden boneyard tiles.
     */
    private String hiddenBoneyardRepr(List<Tile> boneyardTiles)
    {
        return boneyardRepr(boneyardTiles, this::hiddenTileRepr);
    }

    /**
     * Creates a string representation of the boneyard tiles using a specified tile representation function.
     *
     * @param boneyardTiles the list of tiles in the boneyard.
     * @param tileRepr a function that converts a tile to its string representation.
     * @return a string representation of the boneyard tiles.
     */
    private String boneyardRepr(List<Tile> boneyardTiles, Function<Tile, String> tileRepr)
    {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add(" (@@@)");
        boneyardTiles.forEach(tile -> joiner.add(tileRepr.apply(tile)));
        joiner.add(tilesRepr(boneyardTiles));
        return joiner.toString();
    }

    /**
     * Displays the last move made on the board. If the board is not empty,
     * it will print the last move along with separators for clarity.
     */
    public void displayBoardLastMove()
    {
        if(!getModel().isBoardEmpty())
        {
            System.out.println(" BOARD - LAST MOVE");
            displaySeparatorSection();
            System.out.println(lastMove); // *
            displaySeparatorSection();
        }
    }

    /**
     * Displays the tiles of all players, hiding opponent tiles if the option is enabled.
     * Only shows the tiles of the current player.
     */
    public void displayHiddenPlayerTiles() {
        displayPlayerTiles(turn -> !getModel().isHideOpponentTiles() || turn == getModel().getCurrentTurn());
    }

    /**
     * Displays the tiles of all players, showing all tiles regardless of the player's turn.
     */
    public void displayVisiblePlayerTiles() {
        displayPlayerTiles(turn -> true);
    }

    /**
     * Displays the tiles of players while considering visibility rules.
     * The method chooses to display the tiles of players based on the provided condition.
     *
     * @param showTilesCondition A predicate that determines whether to show the tiles of a player.
     */
    public void displayPlayerTiles(IntPredicate showTilesCondition)
    {
        StringJoiner joiner = new StringJoiner("\n");
        for(int turn=0; turn < getModel().getNumOfPlayers(); turn++)
        {
            if(showTilesCondition.test(turn))
            {
                joiner.add(regularPlayerRepr(turn, getModel().getPlayer(turn).getTiles()));
            }
            else
            {
                joiner.add(hiddenPlayerRepr(turn, getModel().getPlayer(turn).getTiles()));
            }
        }

        System.out.println(" TILES");
        displaySeparatorSection();
        System.out.println(joiner);
    }

    /**
     * Creates a regular string representation of a player's tiles.
     *
     * @param playerTurn the index of the player.
     * @param playerTiles the list of tiles the player holds.
     * @return a string representation of the player's tiles.
     */
    private String regularPlayerRepr(int playerTurn, List<Tile> playerTiles)
    {
        return playerRepr(playerTurn, playerTiles, this::regularTileRepr);
    }

    /**
     * Creates a hidden string representation of a player's tiles.
     *
     * @param playerTurn the index of the player.
     * @param playerTiles the list of tiles the player holds.
     * @return a string representation of the hidden player's tiles.
     */
    private String hiddenPlayerRepr(int playerTurn, List<Tile> playerTiles)
    {
        return playerRepr(playerTurn, playerTiles, this::hiddenTileRepr);
    }

    /**
     * Creates a string representation of a player's tiles using a specified tile representation function.
     *
     * @param playerTurn the index of the player.
     * @param playerTiles the list of tiles the player holds.
     * @param tileRepr a function that converts a tile to its string representation.
     * @return a string representation of the player's tiles.
     */
    private String playerRepr(int playerTurn, List<Tile> playerTiles, Function<Tile, String> tileRepr)
    {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add(playerTurnRepr(playerTurn));
        playerTiles.forEach(tile -> joiner.add(tileRepr.apply(tile)));
        joiner.add(tilesRepr(playerTiles));
        return joiner.toString();
    }

    /**
     * Creates a string representation of the playable tiles.
     *
     * @param playableTiles the list of playable tiles.
     * @return a string representation of the playable tiles.
     */
    private String playableTilesRepr(List<Tile> playableTiles)
    {
        StringJoiner joiner = new StringJoiner("\n");
        for(int option=0; option < playableTiles.size(); option++)
        {
            joiner.add(String.format(" (%d) %s", option, regularTileRepr(playableTiles.get(option))));
        }
        return joiner.toString();
    }
}
