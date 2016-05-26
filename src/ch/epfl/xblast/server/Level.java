package ch.epfl.xblast.server;

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

    private static final List<Player> PLAYERS = Arrays.asList(new Player(
            PlayerID.PLAYER_1, 3, new Cell(1, 1), 2, 3), new Player(
            PlayerID.PLAYER_2, 3, new Cell(13, 1), 2, 3), new Player(
            PlayerID.PLAYER_3, 3, new Cell(13, 11), 2, 3), new Player(
            PlayerID.PLAYER_4, 3, new Cell(1, 11), 2, 3));

    private static final Block FF = Block.FREE;
    private static final Block IW = Block.INDESTRUCTIBLE_WALL;
    private static final Block DW = Block.DESTRUCTIBLE_WALL;

    private static final Board BOARD = Board.ofQuadrantNWBlocksWalled(Arrays
            .asList(Arrays.asList(FF, FF, FF, FF, FF, DW, FF),
                    Arrays.asList(FF, IW, DW, IW, DW, IW, DW),
                    Arrays.asList(FF, DW, FF, FF, FF, DW, FF),
                    Arrays.asList(DW, IW, FF, IW, IW, IW, IW),
                    Arrays.asList(FF, DW, FF, DW, FF, FF, FF),
                    Arrays.asList(DW, IW, DW, IW, DW, IW, FF)));

    private static final BlockImage SHADED_BLOCK = BlockImage.IRON_FLOOR_S;

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
        // add free block/image
        palete.put(Block.FREE, BlockImage.IRON_FLOOR);
        // skip second entry in BlockImage(shaded block), add all
        // Block/BlockImage pairs
        for (int i = 1; i < Block.values().length; i++) {
            palete.put(Block.values()[i], BlockImage.values()[i + 1]);
        }
        // create BoardPainter
        BoardPainter painter = new BoardPainter(palete, SHADED_BLOCK);
        // create gamestate
        GameState state = new GameState(BOARD, PLAYERS);
        return new Level(painter, state);
    }

}
