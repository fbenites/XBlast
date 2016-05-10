package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;

/**
 * Class grouping a Boardpainter and a Gamestate. Would allow to introduce
 * additional images for the board.
 * 
 * @author Lorenz Rasch (249937)
 */
public final class Level {

    private final BoardPainter bp;
    private final GameState gs;

    /**
     * Constant for the start of the game. Board layout as provided, players in
     * corners (clockwise, Player 1 top left) with 3 lives, carrying 2 bombs
     * with range 3. BoardPainter with provided images.
     */
    public static final Level DEFAULT_LEVEL = getDefaultLevel();

    /**
     * Creates a new Level with the Boardpainter and Gamestate given.
     * 
     * @param painter
     *            the painter/images
     * @param state
     *            the current gamestate
     * @throws NullPointerException
     *             if any of the provided arguments is null
     */
    public Level(BoardPainter painter, GameState state) {
        bp = Objects.requireNonNull(painter);
        gs = Objects.requireNonNull(state);
    }

    /**
     * Gives the Boardpainter of this Level.
     * 
     * @return the Boardpainter
     */
    public BoardPainter boardPainter() {
        return bp;
    }

    /**
     * Gives the current Gamestate.
     * 
     * @return the gs
     */
    public GameState gameState() {
        return gs;
    }

    /**
     * Gives the default Level. Board layout as provided, players in corners
     * (clockwise, Player 1 top left) with 3 lives, carrying 2 bombs with range
     * 3. BoardPainter with provided images.
     * 
     * @return the default Level
     */
    private static Level getDefaultLevel() {
        // build palate
        Map<Block, BlockImage> palete = new HashMap<Block, BlockImage>();
        palete.put(Block.FREE, BlockImage.IRON_FLOOR);
        for (int i = 1; i < Block.values().length; i++) {
            palete.put(Block.values()[i], BlockImage.values()[i + 1]);
        }
        // create BoardPainter
        BoardPainter painter = new BoardPainter(palete, BlockImage.IRON_FLOOR_S);
        // build list of players
        List<Player> players = new ArrayList<Player>();
        players.add(new Player(PlayerID.PLAYER_1, 3, new Cell(1, 1), 2, 3));
        players.add(new Player(PlayerID.PLAYER_2, 3, new Cell(13, 1), 2, 3));
        players.add(new Player(PlayerID.PLAYER_3, 3, new Cell(13, 11), 2, 3));
        players.add(new Player(PlayerID.PLAYER_4, 3, new Cell(1, 11), 2, 3));
        // create board
        Block ff = Block.FREE;
        Block iw = Block.INDESTRUCTIBLE_WALL;
        Block dw = Block.DESTRUCTIBLE_WALL;
        Board board = Board.ofQuadrantNWBlocksWalled(Arrays.asList(
                Arrays.asList(ff, ff, ff, ff, ff, dw, ff),
                Arrays.asList(ff, iw, dw, iw, dw, iw, dw),
                Arrays.asList(ff, dw, ff, ff, ff, dw, ff),
                Arrays.asList(dw, iw, ff, iw, iw, iw, iw),
                Arrays.asList(ff, dw, ff, dw, ff, ff, ff),
                Arrays.asList(dw, iw, dw, iw, dw, iw, ff)));
        // create gamestate
        GameState state = new GameState(board, players);
        return new Level(painter, state);
    }

}
