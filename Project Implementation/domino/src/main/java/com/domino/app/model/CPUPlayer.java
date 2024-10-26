package com.domino.app.model;

/**
 * Represents a computer-controlled player in the domino game.
 * <p>
 * This class extends {@code Player} and acts as a marker for CPU players.
 * The {@code DominoController} utilizes this class to identify CPU players
 * and to determine which tiles to play and the sides to which they should
 * be attached during gameplay.
 * </p>
 */
public class CPUPlayer extends Player {}
