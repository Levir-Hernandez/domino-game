package com.domino.app.model;

/**
 * Represents a human-controlled player in the domino game.
 * <p>
 * This class extends {@code Player} and serves as a marker for human players.
 * The {@code DominoController} utilizes this class to identify human players
 * and will prompt for tile selections and the sides to which they should
 * be attached during gameplay through console input.
 * </p>
 */
public class HumanPlayer extends Player {}
