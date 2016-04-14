package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.Lists;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.server.Player.DirectedPosition;
import ch.epfl.xblast.server.Player.LifeState;

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
    private static final List<List<PlayerID>> PERMUTATIONS = Lists
            .permutations(Arrays.asList(PlayerID.values()));
    private static final Random RANDOM = new Random(2016);

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
        this.players = Collections.unmodifiableList(new ArrayList<Player>(
                Objects.requireNonNull(players, "Given player list is null.")));
        this.bombs = Collections.unmodifiableList(new ArrayList<Bomb>(Objects
                .requireNonNull(bombs, "Given bomb list is null.")));
        this.explosions = Collections
                .unmodifiableList(new ArrayList<Sq<Sq<Cell>>>(Objects
                        .requireNonNull(explosions,
                                "Given explosions list is null.")));
        this.blasts = Collections.unmodifiableList(new ArrayList<Sq<Cell>>(
                Objects.requireNonNull(blasts, "Given blasts list is null.")));
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
        return Collections.unmodifiableList(list);
    }

    /**
     * Gives a Map of Cell(position)/Bomb pairs.
     * 
     * @return map of Cell/Bomb pairs
     */
    public Map<Cell, Bomb> bombedCells() {
        return GameState.bombedCells(this.bombs);
    }

    /**
     * Gives a Set of every Cell with at least on explosion particle on it.
     * 
     * @return a Set of Cells with explosion particles
     */
    public Set<Cell> blastedCells() {
        return GameState.blastedCells(this.blasts);
    }

    /**
     * Gives the next State of the game, based on current player inputs. Order
     * of calculation: 1. Explosion particles, 2. Board, 3. Explosions, 4.
     * Bombs, 5. Players
     * 
     * @param speedChangeEvents
     *            input events concerning player movement
     * @param bombDropEvents
     *            input events concerning players wishing to drop bombs
     * @return the state of game
     */
    public GameState next(Map<PlayerID, Optional<Direction>> speedChangeEvents,
            Set<PlayerID> bombDropEvents) {
        /* 1 explosion particles */
        List<Sq<Cell>> blasts1 = GameState.nextBlasts(this.blasts,
                this.board(), this.explosions);

        /* 2 board */
        Set<Cell> consumedBonuses = new HashSet<Cell>();
        Map<PlayerID, Bonus> playerBonuses = new HashMap<PlayerID, Bonus>();
        List<Player> players0 = new ArrayList<Player>(this.players());
        // sort players according to active permutation
        List<PlayerID> perm = PERMUTATIONS.get(this.ticks()
                % PERMUTATIONS.size());
        Collections.sort(
                players0,
                (x, y) -> Integer.compare(perm.indexOf(x.id()),
                        perm.indexOf(y.id())));
        // if a player is in the center of a "bonus-cell" the bonus is consumed
        for (Player p : players0) {
            if (p.position().isCentral()
                    && this.board().blockAt(p.position().containingCell())
                            .isBonus()) {
                // adds player/bonus pair to map if the bonus position isn't in
                // the set yet to make sure only one player gets bonus
                if (consumedBonuses.add(p.position().containingCell())) {
                    playerBonuses.put(p.id(),
                            this.board().blockAt(p.position().containingCell())
                                    .associatedBonus());
                }
            }
        }
        // get cells with at least one explosion particle on it
        Set<Cell> blastedCells1 = GameState.blastedCells(blasts1);
        // calculate board from active board, consumed bonuses and blastedCells
        Board board1 = GameState.nextBoard(this.board(), consumedBonuses,
                blastedCells1);

        /* 3 explosions */
        List<Sq<Sq<Cell>>> explosions1 = GameState
                .nextExplosions(this.explosions);

        /* 4 bombs */
        List<Bomb> bombs1 = new ArrayList<Bomb>();
        // check active bombs
        for (Bomb b : this.bombs) {
            if (blastedCells1.contains(b.position())) {
                // bombs on blasts explode immediately
                explosions1.addAll(b.explosion());
            } else if (b.fuseLengths().tail().isEmpty()) {
                // bombs whose fuse has expired explode
                explosions1.addAll(b.explosion());
            } else {
                // all other bombs fuses decrease by 1
                bombs1.add(new Bomb(b.ownerId(), b.position(), b.fuseLengths()
                        .tail(), b.range()));
            }
        }
        // check bombs newly dropped
        for (Bomb b : GameState.newlyDroppedBombs(players0, bombDropEvents,
                this.bombs)) {
            if (blastedCells1.contains(b.position())) {
                // bombs on blasts explode immediately
                explosions1.addAll(b.explosion());
            } else if (b.fuseLengths().tail().isEmpty()) {
                // bombs whose fuse has expired explode
                explosions1.addAll(b.explosion());
            } else {
                // all other bombs fuses decrease by 1
                bombs1.add(new Bomb(b.ownerId(), b.position(), b.fuseLengths()
                        .tail(), b.range()));
            }
        }

        /* 5 players */
        Map<Cell, Bomb> bombedCells1 = GameState.bombedCells(bombs1);
        List<Player> players1 = GameState.nextPlayers(this.players(),
                playerBonuses, bombedCells1.keySet(), board1, blastedCells1,
                speedChangeEvents);

        return new GameState(this.ticks() + 1, board1, players1, bombs1,
                explosions1, blasts1);
    }

    /**
     * Gives a Map of Cell(position)/Bomb pairs from a list given.
     * 
     * @param the
     *            list of bombs
     * @return map of Cell/Bomb pairs
     */
    private static Map<Cell, Bomb> bombedCells(List<Bomb> bombs0) {
        Map<Cell, Bomb> bombs1 = new HashMap<Cell, Bomb>();
        for (Bomb b : bombs0) {
            bombs1.put(b.position(), b);
        }
        return Collections.unmodifiableMap(bombs1);
    }

    /**
     * Gives a Set of every Cell with at least on explosion particle on it from
     * a list of sequences of explosion particles.
     * 
     * @param list
     *            of explosion particles
     * @return a Set of Cells with explosion particles
     */

    private static Set<Cell> blastedCells(List<Sq<Cell>> blasts0) {
        Set<Cell> blasts1 = new HashSet<Cell>();
        for (Sq<Cell> blast : blasts0) {
            blasts1.add(blast.head());
        }
        return Collections.unmodifiableSet(blasts1);
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

    /**
     * Gives the next State of the Board based on the current Board, the bonuses
     * that got consumed and a set of cells with at least one explosion particle
     * on them.
     * 
     * @param board0
     *            the current board
     * @param consumedBonuses
     *            the positions of consumed bonuses
     * @param blastedCells1
     *            the cells with explosion particles
     * @return the next Board
     */
    private static Board nextBoard(Board board0, Set<Cell> consumedBonuses,
            Set<Cell> blastedCells1) {
        List<Sq<Block>> board1 = new ArrayList<Sq<Block>>();
        for (Cell c : Cell.ROW_MAJOR_ORDER) {
            // convert consumed bonuses in Free blocks
            if (consumedBonuses.contains(c)) {
                board1.add(Sq.constant(Block.FREE));
            } else if (blastedCells1.contains(c)) {
                // convert blasted destructible walls into Bonuses or a free
                // space. chance 1/3.
                if (board0.blockAt(c) == Block.DESTRUCTIBLE_WALL) {
                    switch (RANDOM.nextInt(3)) {
                    case 0:
                        board1.add(Sq.repeat(Ticks.WALL_CRUMBLING_TICKS,
                                Block.CRUMBLING_WALL).concat(
                                Sq.constant(Block.BONUS_BOMB)));
                        break;
                    case 1:
                        board1.add(Sq.repeat(Ticks.WALL_CRUMBLING_TICKS,
                                Block.CRUMBLING_WALL).concat(
                                Sq.constant(Block.BONUS_RANGE)));
                        break;
                    case 2:
                        board1.add(Sq.repeat(Ticks.WALL_CRUMBLING_TICKS,
                                Block.CRUMBLING_WALL).concat(
                                Sq.constant(Block.FREE)));
                        break;
                    }
                    // make blasted bonuses disappear after a short time
                } else if (board0.blockAt(c).isBonus()) {
                    if (!board0.blocksAt(c)
                            .limit(Ticks.BONUS_DISAPPEARING_TICKS)
                            .dropWhile(b -> b.isBonus()).isEmpty()) {
                        // leave block as it is if it already will disappear
                        board1.add(board0.blocksAt(c).tail());
                    } else {
                        // add disappearing time to block before making it a
                        // free block
                        board1.add(Sq.repeat(Ticks.BONUS_DISAPPEARING_TICKS,
                                board0.blockAt(c)).concat(
                                Sq.constant(Block.FREE)));
                    }
                } else {
                    // leave block as it is, blast will have no effect (on
                    // IndestructibleWall/CrumblingWall/Free)
                    board1.add(board0.blocksAt(c).tail());
                }
            } else {
                // leave block as it is, it is neither a consumed bonus or
                // blasted by an explosion
                board1.add(board0.blocksAt(c).tail());
            }
        }
        return new Board(board1);
    }

    /**
     * Gives the next state of the Players(Movement, State) based on consumed
     * bonuses, bombs, the board, blasted cells and speed change events.
     * 
     * @param players0
     *            The current Players
     * @param playerBonuses
     *            a Map associating every consumed bonus to a PlayerID
     * @param bombedCells1
     *            a set of cells containing bombs
     * @param board1
     *            the next board
     * @param blastedCells1
     *            a set of cells with an explosion blast on them
     * @param speedChangeEvents
     *            a map associating given commands to a PlayerID
     * @return the next Players
     */
    private static List<Player> nextPlayers(List<Player> players0,
            Map<PlayerID, Bonus> playerBonuses, Set<Cell> bombedCells1,
            Board board1, Set<Cell> blastedCells1,
            Map<PlayerID, Optional<Direction>> speedChangeEvents) {
        List<Player> players1 = new ArrayList<Player>();
        for (Player p : players0) {
            Sq<Player.DirectedPosition> dp1 = p.directedPositions();
            /* 1 directed position, speed change event */
            if (speedChangeEvents.containsKey(p.id())) {
                // the sc event for the player has no value, he stopps at the
                // next central subcell
                if (!speedChangeEvents.get(p.id()).isPresent()) {
                    // position sequence to next subcell
                    dp1 = p.directedPositions().takeWhile(
                            u -> !u.position().isCentral());
                    // stopped position sequence
                    dp1 = dp1.concat(Player.DirectedPosition.stopped(p
                            .directedPositions().findFirst(
                                    u -> u.position().isCentral())));
                    // the sc event for the player is paralell to the previous
                    // direction, change direction immediately
                } else if (speedChangeEvents.get(p.id()).get()
                        .isParallelTo(p.direction())) {
                    dp1 = Player.DirectedPosition.moving(new DirectedPosition(p
                            .position(), speedChangeEvents.get(p.id()).get()));
                    // the player want to change direction perpendicular, change
                    // direction at next central subcell
                } else {
                    // position sequence to next subcell
                    dp1 = p.directedPositions().takeWhile(
                            u -> !u.position().isCentral());
                    // position at next subcell with new direction
                    DirectedPosition central = new DirectedPosition(p
                            .directedPositions()
                            .findFirst(u -> u.position().isCentral())
                            .position(), speedChangeEvents.get(p.id()).get());
                    // moving sequence with new direction, from next central
                    // subcell
                    dp1 = dp1.concat(DirectedPosition.moving(central));
                }
            }
            /* 2 directed position, blocked or not */
            // no movement if player is dying or dead
            if (p.isAlive() && p.lifeState().state() != LifeState.State.DYING) {
                Cell nextCell = dp1.head().position().containingCell()
                        .neighbor(dp1.head().direction());
                // no movement only if, next cell is blocked AND player is on
                // central subcell
                // is equal to, movement if, next cell isn't blocked OR player
                // isn't
                // on central subcell
                if (board1.blockAt(nextCell).canHostPlayer()
                        || !dp1.head().position().isCentral()) {
                    Cell thisCell = dp1.head().position().containingCell();
                    // check possibility of moving when a bomb is involved
                    if (bombedCells1.contains(thisCell)) {
                        SubCell thisSubCell = dp1.head().position();
                        SubCell nextSubCell = dp1.tail().head().position();
                        // movement on cell with bomb only possible, if distance
                        // to
                        // central subcell increases OR player is further away
                        // from
                        // central subcell than distance 6
                        if ((thisSubCell.distanceToCentral() < nextSubCell
                                .distanceToCentral())
                                || thisSubCell.distanceToCentral() != 6) {
                            dp1 = dp1.tail();
                        } else {
                            // no movement
                        }
                    } else {
                        // no bomb on this cell, normal movement
                        dp1 = dp1.tail();
                    }
                } else {
                    // no movement
                }
            }

            /* 3 life state */
            Sq<Player.LifeState> ls1 = p.lifeStates();
            ls1 = ls1.tail();
            // player hit by explosion particle loses a life
            if (blastedCells1.contains(dp1.head().position().containingCell())
                    && p.lifeState().state() == Player.LifeState.State.VULNERABLE) {
                ls1 = p.statesForNextLife();
            }

            // create new player
            Player player1 = new Player(p.id(), ls1, dp1, p.maxBombs(),
                    p.bombRange());
            /* 4 bonus */
            if (playerBonuses.containsKey(p.id())) {
                // collected bonus is applied to corresponding player
                player1 = playerBonuses.get(p.id()).applyTo(player1);
            }
            players1.add(player1);
        }
        return Collections.unmodifiableList(players1);
    }

    /**
     * Gives the next State of all active explosions. Removes particles that
     * become active from explosions and removes finished explosions completely.
     * 
     * @param explosions0
     *            the current active explosions
     * @return the next Explosions
     */
    private static List<Sq<Sq<Cell>>> nextExplosions(
            List<Sq<Sq<Cell>>> explosions0) {
        List<Sq<Sq<Cell>>> explosions1 = new ArrayList<Sq<Sq<Cell>>>();
        // advance all explosion arms to the next sequence of particles
        for (Sq<Sq<Cell>> explosion : explosions0) {
            if (!explosion.tail().isEmpty()) {
                explosions1.add(explosion.tail());
            }
        }
        return explosions1;
    }

    /**
     * Gives a list of bombs, dropped by the players based on which players want
     * to drop a bomb, their bomb capacity and the already dropped bombs.
     * 
     * @param players0
     *            the current players
     * @param bombDropEvents
     *            set of playerIDs wishing to drop a bomb
     * @param bombs0
     *            all currently active bombs
     * @return a list of bombs dropped
     */
    private static List<Bomb> newlyDroppedBombs(List<Player> players0,
            Set<PlayerID> bombDropEvents, List<Bomb> bombs0) {
        List<Bomb> bombs1 = new ArrayList<Bomb>();
        // check for every player
        for (Player p : players0) {
            // player wants to drop a bomb and is alive, drops a bomb
            if (bombDropEvents.contains(p.id()) && p.isAlive()) {
                int counter = 0;
                boolean spotTaken = false;
                for (Bomb b : bombs0) {
                    // count bombs dropped by this player
                    counter = b.ownerId() == p.id() ? counter + 1 : counter;
                    // check if players position is already taken by a bomb
                    if (b.position().equals(p.position().containingCell()))
                        spotTaken = true;
                }
                // player not at max bomb capacity and on free spot, drops new
                // bomb
                if (counter < p.maxBombs() && !spotTaken) {
                    bombs1.add(p.newBomb());
                }
            }
        }
        return Collections.unmodifiableList(bombs1);
    }

}
