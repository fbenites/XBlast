package ch.epfl.xblast.server;

import java.util.Objects;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;

/**
 * Class representing a Player in the game XBlast.
 * 
 * @author Lorenz Rasch (249937)
 */
public final class Player {

    private final PlayerID id;
    private final Sq<LifeState> ls;
    private final Sq<DirectedPosition> dPos;
    private final int maxB, bRange;

    /**
     * Creates a new Player with the parameters given. (primary constructor)
     * 
     * @param id
     *            this Players id
     * @param lifeStates
     *            this Players life-state
     * @param directedPos
     *            this Players directed position
     * @param maxBombs
     *            this Players amount of bombs
     * @param bombRange
     *            this Players bomb range
     * @throws IllegalArgumentException
     *             if the amount of bombs or the bomb range is negative
     * @throws NullPointerException
     *             if any of the first 3 parameters is null
     */
    public Player(PlayerID id, Sq<LifeState> lifeStates,
            Sq<DirectedPosition> directedPos, int maxBombs, int bombRange)
            throws IllegalArgumentException, NullPointerException {
        this.id = Objects.requireNonNull(id, "Given PlayerID is null.");
        this.ls = Objects
                .requireNonNull(lifeStates, "Given LifeState is null.");
        this.dPos = Objects.requireNonNull(directedPos,
                "Given DirectedPosition is null.");
        // if maxBombs/bombRange is over 9 leave it as it is, the player will
        // not receive any bonuses
        this.maxB = ArgumentChecker.requireNonNegative(maxBombs);
        this.bRange = ArgumentChecker.requireNonNegative(bombRange);
    }

    /**
     * Creates a new Player with the parameters given, facing South. (calls
     * primary constructor)
     * 
     * @param id
     *            this Players id
     * @param lives
     *            this Players number of lives
     * @param position
     *            this Players position
     * @param maxBombs
     *            this Players amount of bombs
     * @param bombRange
     *            this Players bomb range
     * @throws IllegalArgumentException
     *             if the number of lives, the amount of bombs or the bomb range
     *             is negative
     * @throws NullPointerException
     *             if the player id or the position is null
     */
    public Player(PlayerID id, int lives, Cell position, int maxBombs,
            int bombRange) throws IllegalArgumentException,
            NullPointerException {
        this(id, Sq.repeat(Ticks.PLAYER_INVULNERABLE_TICKS,
                new LifeState(lives, LifeState.State.INVULNERABLE)).concat(
                Sq.constant(new LifeState(lives, LifeState.State.VULNERABLE))),
                Sq.constant(new DirectedPosition(SubCell
                        .centralSubCellOf(Objects.requireNonNull(position)),
                        Direction.S)), maxBombs, bombRange);
        // TODO create method to create LiveStates, used here and in
        // statesForNextLife
    }

    /**
     * Gives this Players ID.
     * 
     * @return the Player ID
     */
    public PlayerID id() {
        return id;
    }

    /**
     * Gives this Players sequence of LifeStates
     * 
     * @return the LifeStates
     */
    public Sq<LifeState> lifeStates() {
        return ls;
    }

    /**
     * Gives this Players current LifeState
     * 
     * @return the LifeState
     */
    public LifeState lifeState() {
        return this.lifeStates().head();
    }

    /**
     * Gives the sequence of LifeStates for this Players next life, if the
     * Player has any lives left.
     * 
     * @return
     */
    public Sq<LifeState> statesForNextLife() {
        // if current amount of lives is one, player will permanently die
        if (this.lives() == 1) {
            // sequence of player dying, then permanent death
            return Sq.repeat(Ticks.PLAYER_DYING_TICKS,
                    new LifeState(1, LifeState.State.DYING)).concat(
                    Sq.constant(new LifeState(0, LifeState.State.DEAD)));
        } else {
            // sequence of player dying, being invulnerable at the start of his
            // next life (now one life less), being vulnerable
            return Sq
                    .repeat(Ticks.PLAYER_DYING_TICKS,
                            new LifeState(this.lives(), LifeState.State.DYING))
                    .concat(Sq.repeat(Ticks.PLAYER_INVULNERABLE_TICKS,
                            new LifeState(this.lives() - 1,
                                    LifeState.State.INVULNERABLE)))
                    .concat(Sq.constant(new LifeState(this.lives() - 1,
                            LifeState.State.VULNERABLE)));
        }
    }

    /**
     * Gives this Players current number of lives.
     * 
     * @return the number of lives
     */
    public int lives() {
        return this.lifeState().lives();
    }

    /**
     * Checks if this Player is alive, if this Player has any lives left.
     * 
     * @return true, if this Player is alive/if the Player has any lives left
     */
    public boolean isAlive() {
        return (this.lives() > 0);
    }

    /**
     * Gives this Players directed position.
     * 
     * @return the directed position
     */
    public Sq<DirectedPosition> directedPositions() {
        return this.dPos;
    }

    /**
     * Gives this Players position.
     * 
     * @return the position
     */
    public SubCell position() {
        return this.directedPositions().head().position();
    }

    /**
     * Gives the direction this Player is facing.
     * 
     * @return the Players direction
     */
    public Direction direction() {
        return this.directedPositions().head().direction();
    }

    /**
     * Gives the Players amount of bombs.
     * 
     * @return the amount of bombs
     */
    public int maxBombs() {
        return this.maxB;
    }

    /**
     * Gives a new Player with a new amount of bombs given.
     * 
     * @param newMaxBombs
     *            the new amount of bombs
     * @return a Player with the amount of bombs given
     */
    public Player withMaxBombs(int newMaxBombs) {
        return new Player(this.id, this.ls, this.dPos, newMaxBombs, this.bRange);
    }

    /**
     * Gives the Players bombs explosion particle range.
     * 
     * @return the bombs explosion particle range
     */
    public int bombRange() {
        return this.bRange;
    }

    /**
     * Gives a new Player with a new bomb range.
     * 
     * @param newBombRange
     *            the new bomb range
     * @return a Player with the bomb range given
     */
    public Player withBombRange(int newBombRange) {
        return new Player(this.id, this.ls, this.dPos, this.maxB, newBombRange);
    }

    /**
     * Gives a bomb at this Players position. (let's Player drop a bomb)
     * 
     * @return a bomb at this Players position and with this Players bomb range
     */
    public Bomb newBomb() {
        return new Bomb(this.id, this.position().containingCell(),
                Ticks.BOMB_FUSE_TICKS, this.bRange);
    }

    /**
     * Class grouping the number of lives and the state of a Player.
     * 
     * @author Lorenz Rasch (249937)
     */
    public final static class LifeState {

        private final int lives;
        private final State state;

        /**
         * Creates a new LifeState with the number of lives and the state given.
         * Is the number of lives zero, the state of death is given.
         * 
         * @param lives
         *            the number of lives
         * @param state
         *            the state
         * @throws IllegalArgumentException
         *             if the number of lives is negative
         * @throws NullPointerException
         *             if the state is null
         */
        public LifeState(int lives, State state)
                throws IllegalArgumentException, NullPointerException {
            this.lives = ArgumentChecker.requireNonNegative(lives);
            // the player has no lives left, his state is death
            if (lives == 0) {
                this.state = State.DEAD;
            } else {
                this.state = Objects.requireNonNull(state,
                        "Given State is null.");
            }
        }

        /**
         * Gives the number of lives in this LifeState.
         * 
         * @return the number of lives
         */
        public int lives() {
            return this.lives;
        }

        /**
         * Gives the state in this LifeState.
         * 
         * @return the state
         */
        public State state() {
            return this.state;
        }

        /**
         * Checks if this LifeState allows a player to move.
         * 
         * @return true, if the state is VULNERABLE or INVULNERABLE
         */
        public boolean canMove() {
            return (this.state() == State.VULNERABLE)
                    || (this.state() == State.INVULNERABLE);
        }

        /**
         * Enumeration representing the different states in a LifeState.
         * 
         * @author Lorenz Rasch (249937)
         */
        public enum State {
            INVULNERABLE, VULNERABLE, DYING, DEAD;
        }

    }

    /**
     * Class grouping the direction a Player is facing and the position of a
     * Player.
     * 
     * @author Lorenz Rasch (249937)
     */
    public final static class DirectedPosition {

        private final SubCell pos;
        private final Direction d;

        /**
         * Creates a new DirectedPosition with the position and direction given.
         * 
         * @param position
         *            the position
         * @param direction
         *            the direction
         * @throws NullPointerException
         *             if any of the parameters is null
         */
        public DirectedPosition(SubCell position, Direction direction)
                throws NullPointerException {
            this.pos = Objects.requireNonNull(position,
                    "Given position is null.");
            this.d = Objects.requireNonNull(direction,
                    "Given direction is null.");
        }

        /**
         * Gives a sequence of DirectedPositions where a Player has stopped.
         * 
         * @param p
         *            the DirectedPosition
         * @return infinite sequence of DirectedPositions where the Player
         *         stopped
         */
        public static Sq<DirectedPosition> stopped(DirectedPosition p) {
            return Sq.constant(p);
        }

        /**
         * Gives a sequence of DirectedPositions for a Player moving in the
         * given direction.
         * 
         * @param p
         *            the DirectedPosition
         * @return infinite sequence of DirectedPositions in the direction of
         *         the DirectedPosition
         */
        public static Sq<DirectedPosition> moving(DirectedPosition p) {
            return Sq.iterate(p,
                    u -> u.withPosition(u.position().neighbor(u.direction())));
        }

        /**
         * Gives the position.
         * 
         * @return the position
         */
        public SubCell position() {
            return pos;
        }

        /**
         * Gives a new DirectedPosition with the position given.
         * 
         * @param newPosition
         *            the position
         * @return DirectedPosition with the new position
         */
        public DirectedPosition withPosition(SubCell newPosition) {
            return new DirectedPosition(newPosition, this.direction());
        }

        /**
         * Gives the direction.
         * 
         * @return the direction
         */
        public Direction direction() {
            return d;
        }

        /**
         * Gives a new DirectedPosition with the direction given.
         * 
         * @param newDirection
         *            the direction
         * @return DirectedPosition with the new direction
         */
        public DirectedPosition withDirection(Direction newDirection) {
            return new DirectedPosition(this.position(), newDirection);
        }
    }

}