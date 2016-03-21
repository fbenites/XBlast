package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;

/**
 * Class representing a Bomb for the game XBlast.
 * 
 * @author Lorenz Rasch (249937)
 */
public final class Bomb {

    private final PlayerID owner;
    private final Cell pos;
    private final Sq<Integer> fuse;
    private final int range;

    /**
     * Creates a new Bomb with the parameters given. (primary constructor)
     * 
     * @param ownerId
     *            PlayerID of the owner
     * @param position
     *            cell where the bomb is placed
     * @param fuseLengths
     *            "count-down" sequence of the fuse
     * @param range
     *            the range of the bombs explosion particles
     * @throws NullPointerException
     *             if any of the first 3 parameters is null
     * @throws IllegalArgumentException
     *             if the fuse sequence is empty or the range is negative
     */
    public Bomb(PlayerID ownerId, Cell position, Sq<Integer> fuseLengths,
            int range) throws NullPointerException, IllegalArgumentException {
        this.owner = Objects.requireNonNull(ownerId, "Given PlayerID is null.");
        this.pos = Objects.requireNonNull(position, "Given position is null.");
        if (Objects.requireNonNull(fuseLengths, "Given fuse length is null.")
                .isEmpty()) {
            throw new IllegalArgumentException(
                    "The given fuse sequence is empty.");
        } else {
            this.fuse = fuseLengths;
        }
        this.range = ArgumentChecker.requireNonNegative(range);
    }

    /**
     * Creates a new Bomb with the parameters given. (calls primary constructor)
     * 
     * @param ownerId
     *            PlayerID of the owner
     * @param position
     *            cell where the bomb is placed
     * @param fuseLength
     *            length of the "count-down" sequence for the fuse
     * @param range
     *            the range of the bombs explosion particles
     * @throws NullPointerException
     *             if any of the first 2 arguments are null
     * @throws IllegalArgumentException
     *             if the fuse sequence is empty or the range is negative
     */
    public Bomb(PlayerID ownerId, Cell position, int fuseLength, int range)
            throws NullPointerException, IllegalArgumentException {
        this(ownerId, position, Sq.iterate(
                ArgumentChecker.requireNonNegative(fuseLength), u -> u - 1)
                .limit(fuseLength), range);
    }

    /**
     * Gives the player id of this bombs owner.
     * 
     * @return the owners PlayerID
     */
    public PlayerID ownerId() {
        return this.owner;
    }

    /**
     * Gives the position/cell of this bomb.
     * 
     * @return this bombs position
     */
    public Cell position() {
        return this.pos;
    }

    /**
     * Gives the fuse sequence (count-down) of this bomb.
     * 
     * @return this bombs fuse sequence
     */
    public Sq<Integer> fuseLengths() {
        return this.fuse;
    }

    /**
     * Gives the length of the fuse of this bomb.
     * 
     * @return this bombs fuse length
     */
    public int fuseLength() {
        return this.fuse.head();
    }

    /**
     * Gives the range of this bombs explosion particles.
     * 
     * @return this bombs range
     */
    public int range() {
        return this.range;
    }

    /**
     * Gives all explosion particle sequences of this bomb.
     * 
     * @return this bombs explosion particle sequences
     */
    public List<Sq<Sq<Cell>>> explosion() {
        List<Sq<Sq<Cell>>> l = new ArrayList<Sq<Sq<Cell>>>();
        // create an "explosion-arm" for all 4 directions
        for (Direction d : Direction.values()) {
            l.add(this.explosionArmTowards(d));
        }
        return l;
    }

    /**
     * Gives the explosion particle sequence for the given direction.
     * 
     * @param dir
     *            the direction of the "explosion-arm"
     * @return the sequences of explosion particles of this arm
     */
    private Sq<Sq<Cell>> explosionArmTowards(Direction dir) {
        // for every tick the explosion is active, create a sequence of
        // particles as long as the range of the bomb
        return Sq.repeat(
                Ticks.EXPLOSION_TICKS,
                Sq.iterate(this.position(), u -> u.neighbor(dir)).limit(
                        this.range()));
    }
}
