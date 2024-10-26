package com.domino.app.model;

import java.util.*;

/**
 * Represents a player in the domino game, holding a set of tiles.
 */
public class Player
{
    private List<Tile> tiles; // List of tiles held by the player

    /**
     * Constructs a new Player with an empty set of tiles.
     */
    public Player()
    {
        tiles = new ArrayList<>(7); // Initializes the list with a capacity of 7 tiles
    }

    /**
     * Checks if the player has any tiles left.
     *
     * @return {@code true} if the player has tiles; {@code false} if the player's hand is empty.
     */
    public boolean hasAnyTile() {return !tiles.isEmpty();}

    /**
     * Checks if the player has any double tiles (tiles with the same number on both sides).
     *
     * @return {@code true} if the player has double tiles; {@code false} otherwise.
     */
    public boolean hasAnyDoubleTile() {return !getDoubleTiles().isEmpty();}

    /**
     * Retrieves an unmodifiable list of the tiles held by the player.
     *
     * @return An unmodifiable list of the player's tiles.
     */
    public List<Tile> getTiles() {return Collections.unmodifiableList(tiles);}

    /**
     * Gets a list of all double tiles held by the player.
     *
     * @return A list of the player's double tiles; an empty list if none are present.
     */
    public List<Tile> getDoubleTiles()
    {
        return tiles.stream()
                .filter(Tile::isDouble)
                .toList();
    }

    /**
     * Gets the count of tiles currently held by the player.
     *
     * @return The number of tiles in the player's hand.
     */
    public int getTileCount() {return tiles.size();}

    /**
     * Calculates the total sum of the values of all tiles held by the player.
     *
     * @return The sum of the values of the player's tiles.
     */
    public int getTilesSum()
    {
        return tiles.stream()
                .map(Tile::getSum)
                .reduce(0, Integer::sum);
    }

    /**
     * Retrieves the double tile with the highest pip value from the player's hand.
     * A double tile has the same number of pips on both sides.
     *
     * @return The double tile with the highest pip value; {@code null} if the player has no double tiles.
     */
    public Tile getBiggestDouble()
    {
        return getDoubleTiles().stream()
                .max(Comparator.comparing(Tile::getLeft))
                .orElse(null);
    }

    /**
     * Adds a tile to the player's hand.
     *
     * @param tile The tile to be added.
     * @return {@code true} if the tile was successfully added; {@code false} otherwise.
     */
    public boolean addTile(Tile tile) {return tiles.add(tile);}

    /**
     * Removes and returns the tile at the specified index from the player's hand.
     *
     * @param index The index of the tile to be removed.
     * @return The tile that was removed.
     * @throws IndexOutOfBoundsException if the index is out of range.
     */
    public Tile grabTile(int index) {return tiles.remove(index);}
}
