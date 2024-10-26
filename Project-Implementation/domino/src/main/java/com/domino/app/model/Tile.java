package com.domino.app.model;

/**
 * Represents a domino tile that has two numbered ends.
 */
public class Tile
{
    private int left;  // Number on the left end of the tile
    private int right; // Number on the right end of the tile

    /**
     * Enum for indicating which side of a tile can attach.
     */
    public enum AttachSide
    {
        NONE, LEFT, RIGHT, BOTH
    }

    /**
     * Creates a new domino tile with the specified numbers on each end.
     *
     * @param left  Number on the left end of the tile
     * @param right Number on the right end of the tile
     */
    public Tile(int left, int right)
    {
        this.left = left;
        this.right = right;
    }

    /**
     * Gets the number on the left end of the tile.
     *
     * @return The number on the left end
     */
    public int getLeft() {return left;}

    /**
     * Gets the number on the right end of the tile.
     *
     * @return The number on the right end
     */
    public int getRight() {return right;}

    /**
     * Calculates the sum of the numbers on both ends of the tile.
     *
     * @return The sum of the numbers on the ends
     */
    public int getSum() {return left + right;}

    /**
     * Checks if the tile is a double (both ends have the same number).
     *
     * @return true if the tile is a double, false otherwise
     */
    public boolean isDouble() {return left == right;}

    /**
     * Rotates the tile, swapping the numbers on the ends.
     */
    public void rotate()
    {
        // Exchange sides
        int left = this.left;
        this.left = right;
        right = left;
    }

    /**
     * Checks if an external tile can be attached to this tile.
     *
     * @param tile The external tile to be attached
     * @return An AttachSide enum value indicating attachability: NONE, LEFT, RIGHT, BOTH
     */
    public AttachSide canAttach(Tile tile)
    {
        boolean attachLeft = (tile.right == left || tile.left == left);
        boolean attachRight = (right == tile.left || right == tile.right);

        AttachSide attachSide;
        if(attachLeft && attachRight) attachSide = AttachSide.BOTH;
        else if(attachLeft)           attachSide = AttachSide.LEFT;
        else if(attachRight)          attachSide = AttachSide.RIGHT;
        else                          attachSide = AttachSide.NONE;

        return attachSide;
    }
}
