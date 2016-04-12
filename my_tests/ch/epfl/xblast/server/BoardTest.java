package ch.epfl.xblast.server;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import ch.epfl.xblast.Cell;

/**
 * @author Lorenz Rasch (249937)
 */
public class BoardTest {

    @Test
    public void blocksAtTest() {
        Block __ = Block.FREE;
        Block XX = Block.INDESTRUCTIBLE_WALL;
        Block xx = Block.DESTRUCTIBLE_WALL;
        Board board = Board.ofQuadrantNWBlocksWalled(
          Arrays.asList(
            Arrays.asList(__, __, __, __, __, xx, __),
            Arrays.asList(__, XX, xx, XX, xx, XX, xx),
            Arrays.asList(__, xx, __, __, __, xx, __),
            Arrays.asList(xx, XX, __, XX, XX, XX, XX),
            Arrays.asList(__, xx, __, xx, __, __, __),
            Arrays.asList(xx, XX, xx, XX, xx, XX, __)));
        
        assertEquals(Block.FREE, board.blocksAt(new Cell(1,1)).head());
        assertEquals(Block.FREE, board.blocksAt(new Cell(1,1)).tail().head());
        
        assertEquals(Block.INDESTRUCTIBLE_WALL, board.blocksAt(new Cell(0,0)).head());
        assertEquals(Block.INDESTRUCTIBLE_WALL, board.blocksAt(new Cell(0,0)).tail().head());
    }

    //also tests blockAt()
    @Test
    public void builderConstructors() {
        Block __ = Block.FREE;
        Block XX = Block.INDESTRUCTIBLE_WALL;
        Block xx = Block.DESTRUCTIBLE_WALL;
        Board board = Board.ofQuadrantNWBlocksWalled(
          Arrays.asList(
            Arrays.asList(__, __, __, __, __, xx, __),
            Arrays.asList(__, XX, xx, XX, xx, XX, xx),
            Arrays.asList(__, xx, __, __, __, xx, __),
            Arrays.asList(xx, XX, __, XX, XX, XX, XX),
            Arrays.asList(__, xx, __, xx, __, __, __),
            Arrays.asList(xx, XX, xx, XX, xx, XX, __)));
        
        //check outside walls
        for(int i = 0; i < Cell.COLUMNS; i++){
            assertEquals(Block.INDESTRUCTIBLE_WALL, board.blockAt(new Cell(i,  0)));
            assertEquals(Block.INDESTRUCTIBLE_WALL, board.blockAt(new Cell(i, 12)));
        }
        for(int i = 0; i < Cell.ROWS; i++){
            assertEquals(Block.INDESTRUCTIBLE_WALL, board.blockAt(new Cell( 0, i)));
            assertEquals(Block.INDESTRUCTIBLE_WALL, board.blockAt(new Cell(14, i)));
        }
        
        //destructible walls in row 1 and 11
        //checks for correct mirroring
        assertEquals(Block.DESTRUCTIBLE_WALL, board.blockAt(new Cell( 6,  1)));
        assertEquals(Block.DESTRUCTIBLE_WALL, board.blockAt(new Cell( 8,  1)));
        assertEquals(Block.DESTRUCTIBLE_WALL, board.blockAt(new Cell( 6, 11)));
        assertEquals(Block.DESTRUCTIBLE_WALL, board.blockAt(new Cell( 8, 11)));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void exceptionTest(){
        Block __ = Block.FREE;
        Block XX = Block.INDESTRUCTIBLE_WALL;
        Block xx = Block.DESTRUCTIBLE_WALL;
        Board.ofQuadrantNWBlocksWalled(
          Arrays.asList(
            Arrays.asList(__, __, __, __, __, xx, __),
            Arrays.asList(__, XX, xx, XX, xx, XX, xx),
            Arrays.asList(__, xx, __, __, __, xx, __),
            Arrays.asList(xx, XX, __, XX, XX, XX, XX),
            Arrays.asList(__, xx, __, xx, __, __, __),
            Arrays.asList(xx, XX, xx, XX, xx, XX))); //one block missing
    }
    
    @Test(expected = NullPointerException.class)
    public void nullTest(){
        Board.ofQuadrantNWBlocksWalled(null);
    }

}
