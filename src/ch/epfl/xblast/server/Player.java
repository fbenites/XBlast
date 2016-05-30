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
    public Player(PlayerID playerID, Sq<LifeState> lifeStates,
            Sq<DirectedPosition> directedPos, int maxBombs, int bombRange) {
        id = Objects.requireNonNull(playerID, "Given PlayerID is null.");
        ls = Objects.requireNonNull(lifeStates, "Given LifeState is null.");
        dPos = Objects.requireNonNull(directedPos,
                "Given DirectedPosition is null.");
        // if maxBombs/bombRange is over 9 leave it as it is, the player
        // will not receive any bonuses
        maxB = ArgumentChecker.requireNonNegative(maxBombs);
        bRange = ArgumentChecker.requireNonNegative(bombRange);
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
            int bombRange) {
        this(id, Player.createLifeStates(lives), Sq
                .constant(new DirectedPosition(SubCell.centralSubCellOf(Objects
                        .requireNonNull(position)), Direction.S)), maxBombs,
                bombRange);
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
        return lifeStates().head();
    }

    /**
     * Gives the sequence of LifeStates for this Players next life, if the
     * Player has any lives left.
     * 
     * @return LifeStates for Players next life
     */
    public Sq<LifeState> statesForNextLife() {
        // Player is dying, then states with one life less are added
        return Sq.repeat(Ticks.PLAYER_DYING_TICKS,
                new LifeState(lives(), LifeState.State.DYING)).concat(
                Player.createLifeStates(lives() - 1));
    }

    /**
     * Gives this Players current number of lives.
     * 
     * @return the number of lives
     */
    public int lives() {
        return lifeState().lives();
    }

    /**
     * Checks if this Player is alive, if this Player has any lives left.
     * 
     * @return true, if this Player is alive/if the Player has any lives left
     */
    public boolean isAlive() {
        return (lives() > 0);
    }

    /**
     * Gives this Players directed position.
     * 
     * @return the directed position
     */
    public Sq<DirectedPosition> directedPositions() {
        return dPos;
    }

    /**
     * Gives this Players position.
     * 
     * @return the position
     */
    public SubCell position() {
        return directedPositions().head().position();
    }

    /**
     * Gives the direction this Player is facing.
     * 
     * @return the Players direction
     */
    public Direction direction() {
        return directedPositions().head().direction();
    }

    /**
     * Gives the Players amount of bombs.
     * 
     * @return the amount of bombs
     */
    public int maxBombs() {
        return maxB;
    }

    /**
     * Gives a new Player with a new amount of bombs given.
     * 
     * @param newMaxBombs
     *            the new amount of bombs
     * @return a Player with the amount of bombs given
     * @throws IllegalArgumentException
     *             if the new amount of bombs is negative
     */
    public Player withMaxBombs(int newMaxBombs) {
        return new Player(id(), lifeStates(), directedPositions(), newMaxBombs,
                bombRange());
    }

    /**
     * Gives the Players bombs explosion particle range.
     * 
     * @return the bombs explosion particle range
     */
    public int bombRange() {
        return bRange;
    }

    /**
     * Gives a new Player with a new bomb range.
     * 
     * @param newBombRange
     *            the new bomb range
     * @return a Player with the bomb range given
     * @throws IllegalArgumentException
     *             if the new bomb range is negative
     */
    public Player withBombRange(int newBombRange) {
        return new Player(id(), lifeStates(), directedPositions(), maxBombs(),
                newBombRange);
    }

    /**
     * Gives a bomb at this Players position. (let's Player drop a bomb)
     * 
     * @return a bomb at this Players position and with this Players bomb range
     */
    public Bomb newBomb() {
        return new Bomb(id(), position().containingCell(),
                Ticks.BOMB_FUSE_TICKS, bombRange());
    }

    /**
     * Creates LifeStates according to the number of lives. First a short state
     * of invulnerability, then constant vulnerability. If number of lives is
     * zero, constant state of death is returned.
     * 
     * @param lives
     *            the number of lives
     * @return LifeStates according to number of lives
     * @throws IllegalArgumentException
     *             if the number of lives is negative
     */
    private static Sq<LifeState> createLifeStates(int lives) {
        if (lives <= 0) {
            return Sq.constant(new LifeState(lives, LifeState.State.DEAD));
        } else {
            return Sq.repeat(Ticks.PLAYER_INVULNERABLE_TICKS,
                    new LifeState(lives, LifeState.State.INVULNERABLE))
                    .concat(Sq.constant(new LifeState(lives,
                            LifeState.State.VULNERABLE)));
        }
    }

    /**
     * Class grouping the number of lives and the state of a Player.
     * 
     * @author Lorenz Rasch (249937)
     */
    public final static class LifeState {

        private final int l;
        private final State s;

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
        public LifeState(int lives, State state) {
            l = ArgumentChecker.requireNonNegative(lives);
            s = Objects.requireNonNull(state, "Given State is null.");
        }

        /**
         * Gives the number of lives in this LifeState.
         * 
         * @return the number of lives
         */
        public int lives() {
            return l;
        }

        /**
         * Gives the state in this LifeState.
         * 
         * @return the state
         */
        public State state() {
            return s;
        }

        /**
         * Checks if this LifeState allows a player to move.
         * 
         * @return true, if the state is VULNERABLE or INVULNERABLE
         */
        public boolean canMove() {
            return (state() == State.VULNERABLE)
                    || (state() == State.INVULNERABLE);
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
        public DirectedPosition(SubCell position, Direction direction) {
            pos = Objects.requireNonNull(position, "Given position is null.");
            d = Objects.requireNonNull(direction, "Given direction is null.");
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
            return new DirectedPosition(newPosition, direction());
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
            return new DirectedPosition(position(), newDirection);
        }
    }

}