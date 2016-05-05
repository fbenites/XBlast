package ch.epfl.xblast.server;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import org.junit.Test;

import ch.epfl.xblast.Cell;

public class BoardPainterTest {

    Block __ = Block.FREE;
    Block XX = Block.INDESTRUCTIBLE_WALL;
    Block xx = Block.DESTRUCTIBLE_WALL;
    Block aa = Block.CRUMBLING_WALL;
    Block bb = Block.BONUS_BOMB;
    Block rr = Block.BONUS_RANGE;
    Board board = Board.ofQuadrantNWBlocksWalled(Arrays.asList(
            Arrays.asList(__, __, __, __, __, xx, __),
            Arrays.asList(__, XX, xx, XX, xx, XX, xx),
            Arrays.asList(aa, xx, bb, __, __, xx, __),
            Arrays.asList(xx, XX, __, XX, XX, XX, XX),
            Arrays.asList(__, xx, rr, xx, __, __, __),
            Arrays.asList(xx, XX, xx, XX, xx, XX, __)));

    @Test(expected = NullPointerException.class)
    public void constructorException01() {
        new BoardPainter(null, BlockImage.IRON_FLOOR);
    }

    @Test(expected = NullPointerException.class)
    public void constructorException02() {
        new BoardPainter(Collections.singletonMap(Block.FREE,
                BlockImage.IRON_FLOOR), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorException03() {
        new BoardPainter(Collections.emptyMap(), BlockImage.IRON_FLOOR);
    }

    @Test
    public void byteForCellTest() {
        Map<Block, BlockImage> m = new HashMap<Block, BlockImage>();
        m.put(Block.FREE, BlockImage.IRON_FLOOR);
        m.put(Block.INDESTRUCTIBLE_WALL, BlockImage.DARK_BLOCK);
        m.put(Block.DESTRUCTIBLE_WALL, BlockImage.EXTRA);
        m.put(Block.CRUMBLING_WALL, BlockImage.EXTRA_O);
        m.put(Block.BONUS_BOMB, BlockImage.BONUS_BOMB);
        m.put(Block.BONUS_RANGE, BlockImage.BONUS_RANGE);
        BoardPainter bp = new BoardPainter(m, BlockImage.IRON_FLOOR_S);

        assertEquals(0, bp.byteForCell(board, new Cell(2, 1)));
        assertEquals(1, bp.byteForCell(board, new Cell(1, 1)));
        assertEquals(2, bp.byteForCell(board, new Cell(0, 0)));
        assertEquals(3, bp.byteForCell(board, new Cell(3, 2)));
        assertEquals(4, bp.byteForCell(board, new Cell(1, 3)));
        assertEquals(5, bp.byteForCell(board, new Cell(3, 3)));
        assertEquals(6, bp.byteForCell(board, new Cell(3, 5)));
    }

}
