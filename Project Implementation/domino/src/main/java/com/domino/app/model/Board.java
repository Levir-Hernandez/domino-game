package com.domino.app.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

/**
 * Represents the game board in a domino game, maintaining the sequence of placed tiles.
 */
public class Board
{
    private List<Tile> tiles; // List of tiles on the board
    private Tile lastAddedTile; // Tracks the last tile added to the board

    /**
     * Initializes a new Board with no tiles.
     */
    public Board()
    {
        lastAddedTile = null;
        tiles = new LinkedList<>();
    }

    /**
     * Checks if the board is empty.
     *
     * @return {@code true} if the board has no tiles; {@code false} otherwise.
     */
    public boolean isEmpty() {return tiles.isEmpty();}

    /**
     * Retrieves an unmodifiable view of all tiles on the board.
     *
     * @return A list of tiles on the board.
     */
    public List<Tile> getTiles() {return Collections.unmodifiableList(tiles);}

    /**
     * Gets the tile on the left end of the board.
     *
     * @return The tile on the leftmost end.
     * @throws NoSuchElementException if the board is empty.
     */
    public Tile getLeftEndTile() {return tiles.getFirst();}

    /**
     * Gets the tile on the right end of the board.
     *
     * @return The tile on the rightmost end.
     * @throws NoSuchElementException if the board is empty.
     */
    public Tile getRightEndTile() {return tiles.getLast();}

    /**
     * Creates a new tile representing the values on both ends of the board.
     *
     * @return A tile with the left value from the left end and the right value from the right end;
     *         {@code null} if the board is empty.
     */
    public Tile getEndsBoardTile()
    {
        return isEmpty()? null : new Tile(getLeftEndTile().getLeft(), getRightEndTile().getRight());
    }

    /**
     * Retrieves the last tile that was added to the board.
     *
     * @return The last tile added to the board.
     */
    public Tile getLastAddedTile() {return lastAddedTile;}

    /**
     * Returns a list of tiles on the board, excluding the last added tile.
     *
     * @return A list of tiles, excluding the most recently added tile.
     */
    public List<Tile> getTilesExcludingLastAdded()
    {
        return tiles.stream()
                .filter(tile -> !tile.equals(lastAddedTile))
                .toList();
    }

    /**
     * Adds a tile to the left end of the board.
     *
     * @param tile The tile to be added.
     * @return {@code true} if the tile was successfully added; {@code false} otherwise.
     */
    public boolean addTileAtLeftEnd(Tile tile)
    {
        return addTileAtEnd(
                tile,
                (endTile, newTile) -> newTile.getRight() == endTile.getLeft(),
                newTile -> tiles.addFirst(newTile)
        );
    }

    /**
     * Adds a tile to the right end of the board.
     *
     * @param tile The tile to be added.
     * @return {@code true} if the tile was successfully added; {@code false} otherwise.
     */
    public boolean addTileAtRightEnd(Tile tile)
    {
        return addTileAtEnd(
                tile,
                (endTile, newTile) -> endTile.getRight() == newTile.getLeft(),
                newTile -> tiles.addLast(newTile)
        );
    }

    /**
     * Attempts to add a tile to either end of the board.
     * Rotates the tile if it cannot be attached in its current orientation.
     *
     * @param tile           The tile to be added.
     * @param canAttach      Predicate to check if the tile can attach to the end.
     * @param addTileAction  Action to perform if the tile can be added.
     * @return {@code true} if the tile was successfully added; {@code false} otherwise.
     */
    private boolean addTileAtEnd(Tile tile, BiPredicate<Tile, Tile> canAttach, Consumer<Tile> addTileAction)
    {
        boolean tileCanBeAdded;
        Tile endTile = getEndsBoardTile(); // Store current end tile
        if(endTile == null)
        {
            lastAddedTile = tile; // Update last added tile
            tiles.addFirst(tile);
            return true;
        }

        // Check if tile needs to be rotated
        tileCanBeAdded = canAttach.test(endTile, tile);
        if(!tileCanBeAdded){tile.rotate();}

        // Verify after rotation if the tile can be added
        tileCanBeAdded = canAttach.test(endTile, tile);
        if(tileCanBeAdded)
        {
            addTileAction.accept(tile); // Add the tile
            lastAddedTile = tile; // Update last added tile
        }

        return tileCanBeAdded;
    }
}
