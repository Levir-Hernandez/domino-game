package com.domino.app.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents the boneyard in a domino game, which holds a collection of domino tiles.
 * The boneyard typically stores the unused tiles from which players can draw during the game.
 */
public class Boneyard {
    private List<Tile> tiles; // List of tiles in the boneyard

    /**
     * Initializes a new boneyard with an empty list of tiles.
     */
    public Boneyard() {tiles = new LinkedList<>();}

    /**
     * Checks if the boneyard is empty.
     *
     * @return {@code true} if the boneyard has no tiles, {@code false} otherwise.
     */
    public boolean isEmpty() {return tiles.isEmpty();}

    /**
     * Gets an unmodifiable view of the tiles in the boneyard.
     *
     * @return A read-only list of tiles in the boneyard.
     */
    public List<Tile> getTiles() {return Collections.unmodifiableList(tiles);}

    /**
     * Gets the count of tiles currently in the boneyard.
     *
     * @return The number of tiles in the boneyard.
     */
    public int getTileCount() {return tiles.size();}

    /**
     * Adds a new tile to the boneyard.
     *
     * @param tile The tile to be added.
     * @return {@code true} if the tile was successfully added, {@code false} otherwise.
     */
    public boolean addTile(Tile tile) {return tiles.add(tile);}

    /**
     * Removes and returns the first tile from the boneyard.
     * If the boneyard is empty, it returns {@code null}.
     *
     * @return The first tile from the boneyard, or {@code null} if it is empty.
     */
    public Tile releaseTile() {return isEmpty()? null : tiles.removeFirst();}

    /**
     * Randomly shuffles the tiles in the boneyard.
     * This method rearranges the order of the tiles in the boneyard randomly.
     * It uses the {@link Collections#shuffle(List)} method for randomization.
     */
    public void shuffleTiles() {Collections.shuffle(tiles);}
}
