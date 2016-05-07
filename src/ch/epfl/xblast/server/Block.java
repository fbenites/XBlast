package ch.epfl.xblast.server;

import java.util.NoSuchElementException;

/**
 * Enumeration representing the different kinds of blocks on the board.
 * 
 * @author Lorenz Rasch (249937)
 */
public enum Block {

    FREE, INDESTRUCTIBLE_WALL, DESTRUCTIBLE_WALL, CRUMBLING_WALL, BONUS_BOMB(
            Bonus.INC_BOMB), BONUS_RANGE(Bonus.INC_RANGE);

    private final Bonus bonus;

    /**
     * Creates a new Block with the provided bonus.
     * 
     * @param b
     *            the provided bonus
     */
    private Block(Bonus b) {
        bonus = b;
    }

    /**
     * Creates a new Block without any bonus.
     */
    private Block() {
        bonus = null;
    }

    /**
     * Checks if this Block represents a free space on the board.
     * 
     * @return true, if this block is FREE
     */
    public boolean isFree() {
        return this == FREE;
    }

    /**
     * Checks if a player can stand/walk on this Block.
     * 
     * @return true, if this block is FREE or a bonus.
     */
    public boolean canHostPlayer() {
        return isFree() || isBonus();
    }

    /**
     * Checks if this Block casts a shadow.
     * 
     * @return true, if this Block is a WALL of some sort
     */
    public boolean castsShadow() {
        return this == INDESTRUCTIBLE_WALL || this == DESTRUCTIBLE_WALL
                || this == CRUMBLING_WALL;
    }

    /**
     * Checks if this Block is a bonus block.
     * 
     * @return true, if this block is a BONUS of some sort
     */
    public boolean isBonus() {
        return this == BONUS_BOMB || this == BONUS_RANGE;
    }

    /**
     * Gives the bonus associated with this Block, if it exists
     * 
     * @return the bonus associated with this Block
     * @throws NoSuchElementException
     *             if there is no Bonus associated with this Block
     */
    public Bonus associatedBonus() {
        if (!this.isBonus()) {
            throw new NoSuchElementException(
                    "There is no Bonus associated with Block: "
                            + this.toString());
        } else {
            return bonus;
        }
    }
}