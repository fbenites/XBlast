package ch.epfl.xblast.server;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;

public class LevelTest {

    private static Block __ = Block.FREE;
    private static Block XX = Block.INDESTRUCTIBLE_WALL;
    private static Block xx = Block.DESTRUCTIBLE_WALL;
    Block aa = Block.CRUMBLING_WALL;
    Block bb = Block.BONUS_BOMB;
    Block rr = Block.BONUS_RANGE;


    Board BOARD = Board.ofQuadrantNWBlocksWalled(Arrays.asList(
            Arrays.asList(__, __, __, __, __, xx, __),
            Arrays.asList(__, XX, xx, XX, xx, XX, xx),
            Arrays.asList(aa, xx, bb, __, __, xx, __),
            Arrays.asList(xx, XX, __, XX, XX, XX, XX),
            Arrays.asList(__, xx, rr, xx, __, __, __),
            Arrays.asList(xx, XX, xx, XX, xx, XX, __)));

    private static List<Player> PLAYERS = new ArrayList<Player>(
            Arrays.asList(
            new Player(PlayerID.PLAYER_1, 2, new Cell(1, 1), 5, 5),
            new Player(PlayerID.PLAYER_2, 3, new Cell(1, 11), 6, 6),
            new Player(PlayerID.PLAYER_3, 4, new Cell(13, 1), 3, 3),
            new Player(PlayerID.PLAYER_4, 5, new Cell(13, 11), 2, 2)));

    @Test(expected = NullPointerException.class)
    public void exceptionTest01() {
        new Level(null, new GameState(BOARD, PLAYERS));
    }

    @Test(expected = NullPointerException.class)
    public void exceptionTest02() {
        Map<Block, BlockImage> m = new HashMap<Block, BlockImage>();
        m.put(Block.FREE, BlockImage.IRON_FLOOR);
        m.put(Block.INDESTRUCTIBLE_WALL, BlockImage.DARK_BLOCK);
        m.put(Block.DESTRUCTIBLE_WALL, BlockImage.EXTRA);
        m.put(Block.CRUMBLING_WALL, BlockImage.EXTRA_O);
        m.put(Block.BONUS_BOMB, BlockImage.BONUS_BOMB);
        m.put(Block.BONUS_RANGE, BlockImage.BONUS_RANGE);
        new Level(new BoardPainter(m, BlockImage.IRON_FLOOR_S), null);
    }
    
    @Test
    public void getterTest(){
        Map<Block, BlockImage> m = new HashMap<Block, BlockImage>();
        m.put(Block.FREE, BlockImage.IRON_FLOOR);
        m.put(Block.INDESTRUCTIBLE_WALL, BlockImage.DARK_BLOCK);
        m.put(Block.DESTRUCTIBLE_WALL, BlockImage.EXTRA);
        m.put(Block.CRUMBLING_WALL, BlockImage.EXTRA_O);
        m.put(Block.BONUS_BOMB, BlockImage.BONUS_BOMB);
        m.put(Block.BONUS_RANGE, BlockImage.BONUS_RANGE);
        BoardPainter bp = new BoardPainter(m, BlockImage.IRON_FLOOR_S);
        GameState gs = new GameState(BOARD, PLAYERS);
        Level l = new Level(bp, gs);
        assertEquals(bp, l.boardPainter());
        assertEquals(gs, l.gameState());
        assertEquals(0, l.gameState().ticks());
    }
    
    @Test
    public void defaultLevelTest(){
        
        
        Level l = Level.DEFAULT_LEVEL;

        assertEquals(0, l.boardPainter().byteForCell(BOARD, new Cell(2, 1)));
        assertEquals(1, l.boardPainter().byteForCell(BOARD, new Cell(1, 1)));
        assertEquals(2, l.boardPainter().byteForCell(BOARD, new Cell(0, 0)));
        assertEquals(3, l.boardPainter().byteForCell(BOARD, new Cell(3, 2)));
        assertEquals(4, l.boardPainter().byteForCell(BOARD, new Cell(1, 3)));
        assertEquals(5, l.boardPainter().byteForCell(BOARD, new Cell(3, 3)));
        assertEquals(6, l.boardPainter().byteForCell(BOARD, new Cell(3, 5)));
        
        assertEquals(0, l.gameState().ticks());
        assertEquals(0, l.gameState().blastedCells().size());
        assertEquals(0, l.gameState().bombedCells().size());
        
        assertEquals(3, l.gameState().players().get(3).id().ordinal());
        
        assertFalse(l.gameState().isGameOver());
    }

}
