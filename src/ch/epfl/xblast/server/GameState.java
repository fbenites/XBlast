package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;

/**
 * Class representing the state of the game XBlast at a given time/tick.
 * 
 * @author Lorenz Rasch (249937)
 */
public final class GameState {

    private final int ticks;
    private final Board board;
    private final List<Player> players;
    private final List<Bomb> bombs;
    private final List<Sq<Sq<Cell>>> explosions;
    private final List<Sq<Cell>> blasts;

    /**
     * Creates the GameState for a given time/tick according to the parameters
     * given. (primary constructor)
     * 
     * @param ticks
     *            the current time/tick
     * @param board
     *            the current board
     * @param players
     *            the players
     * @param bombs
     *            all bombs currently active
     * @param explosions
     *            all explosions currently active
     * @param blasts
     *            all explosion particles currently active
     * @throws IllegalArgumentException
     *             if the current time/tick is negative or if there are not 4
     *             players
     * @throws NullPointerException
     *             if any of the later 5 parameters is null
     */
    public GameState(int ticks, Board board, List<Player> players,
            List<Bomb> bombs, List<Sq<Sq<Cell>>> explosions,
            List<Sq<Cell>> blasts) throws IllegalArgumentException,
            NullPointerException {
        this.ticks = ArgumentChecker.requireNonNegative(ticks);
        this.board = Objects.requireNonNull(board, "Given board is null.");
        this.players = Collections.unmodifiableList(Objects.requireNonNull(
                players, "Given player list is null."));
        this.bombs = Collections.unmodifiableList(Objects.requireNonNull(bombs,
                "Given bomb list is null."));
        this.explosions = Collections.unmodifiableList(Objects.requireNonNull(
                explosions, "Given explosions list is null."));
        this.blasts = Collections.unmodifiableList(Objects.requireNonNull(
                blasts, "Given blasts list is null."));
        if (this.players.size() != 4) {
            throw new IllegalArgumentException("Game must have 4 players. ("
                    + this.players.size() + ")");
        }
    }

    /**
     * Gives the GameState at the beginning of a game. (calls primary
     * constructor)
     * 
     * @param board
     *            the "starting" board
     * @param players
     *            the players
     * @throws IllegalArgumentException
     *             if there are not 4 players
     * @throws NullPointerException
     *             if any of the two parameters is null
     */
    public GameState(Board board, List<Player> players)
            throws IllegalArgumentException, NullPointerException {
        this(0, board, players, new ArrayList<Bomb>(),
                new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
    }

    /**
     * Gives the current tick/time of the game.
     * 
     * @return the current tick
     */
    public int ticks() {
        return this.ticks;
    }

    /**
     * Checks if this game is over. (time over, one or less players alive)
     * 
     * @return true, if the total number of ticks has elapsed or, one or less
     *         players are alive
     */
    public boolean isGameOver() {
        return this.ticks > Ticks.TOTAL_TICKS
                || this.alivePlayers().size() <= 1;
    }

    /**
     * Gives the remaining time of this game in seconds.
     * 
     * @return the remaining time
     */
    public double remainingTime() {
        return ((double) Ticks.TOTAL_TICKS - this.ticks())
                / Ticks.TICKS_PER_SECOND;
    }

    /**
     * Gives the player id of the winner of this game, if there is one. (last
     * player alive when the game is over)
     * 
     * @return the player id of the winner, or an empty Optional
     */
    public Optional<PlayerID> winner() {
        if (this.isGameOver() && (this.alivePlayers().size() == 1)) {
            return Optional.of(this.alivePlayers().get(0).id());
        } else {
            return Optional.empty();
        }
    }

    /**
     * Gives the current board.
     * 
     * @return the current board
     */
    public Board board() {
        return this.board;
    }

    /**
     * Gives the players in this game.
     * 
     * @return the players
     */
    public List<Player> players() {
        return this.players;
    }

    /**
     * Gives the currently alive players.
     * 
     * @return the currently alive players
     */
    public List<Player> alivePlayers() {
        List<Player> list = new ArrayList<Player>();
        for (Player p : this.players()) {
            if (p.isAlive()) {
                list.add(p);
            }
        }
        // FIXME shorter with lambda expression?
        return Collections.unmodifiableList(list);
    }

    // TODO add comments

    public Map<Cell, Bomb> bombedCells() {
        // TODO
        return null;
    }

    // TODO add comments

    public Set<Cell> blastedCells() {
        // TODO
        return null;
    }

    // TODO add comments

    public GameState next(Map<PlayerID, Optional<Direction>> speedChangeEvents,
            Set<PlayerID> bombDropEvents) {
        // TODO
        return null;
    }

    /**
     * Gives the list of the explosion particle sequences active on the next
     * tick.
     * 
     * @param blasts0
     *            the currently active explosion particle sequences
     * @param board0
     *            the current board
     * @param explosions0
     *            the currently active explosions
     * @return the next explosion particle sequences
     */
    private static List<Sq<Cell>> nextBlasts(List<Sq<Cell>> blasts0,
            Board board0, List<Sq<Sq<Cell>>> explosions0) {
        List<Sq<Cell>> blasts1 = new ArrayList<Sq<Cell>>();
        for (Sq<Cell> blast0 : blasts0) {
            // add all not completed explosion particle sequences to the "next"
            // list, remove first entry
            // ignore any explosion particle sequences that reached a non-free
            // block
            if (!blast0.tail().isEmpty()
                    && board0.blockAt(blast0.head()).isFree()) {
                blasts1.add(blast0.tail());
            }
        }
        // add the next sequence of every active explosion to the "next"
        // explosion particle sequences list
        for (Sq<Sq<Cell>> explosion0 : explosions0) {
            blasts1.add(explosion0.head());
        }
        return Collections.unmodifiableList(blasts1);
    }

}
