package ch.epfl.xblast.server;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;

/**
 * @author Lorenz Rasch (249937)
 */
public class BombTest {

    //primary constructor exceptions
    @Test(expected = NullPointerException.class)
    public void constructorExceptionTest01() {
        new Bomb(null, new Cell(0,0), Sq.iterate(9, u -> u-1).limit(9), 9);
    }

    @Test(expected = NullPointerException.class)
    public void constructorExceptionTest02() {
        new Bomb(PlayerID.PLAYER_1, null, Sq.iterate(9, u -> u-1).limit(9), 9);
    }

    @Test(expected = NullPointerException.class)
    public void constructorExceptionTest03() {
        new Bomb(PlayerID.PLAYER_1, new Cell(0,0), null, 9);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void constructorExceptionTest04(){
        new Bomb(PlayerID.PLAYER_1, new Cell(0,0), Sq.empty(), 9);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void constructorExceptionTest05(){
        new Bomb(PlayerID.PLAYER_1, new Cell(0,0), Sq.iterate(9, u -> u-1).limit(9), -1);
    }
    
    //secondary constructor exceptions
    @Test(expected = IllegalArgumentException.class)
    public void constructorExceptionTest06(){
        new Bomb(PlayerID.PLAYER_1, new Cell(0,0), -1, 9);
    }
    
    @Test
    public void getOwnerTest(){
        Bomb b = new Bomb(PlayerID.PLAYER_1, new Cell(0,0), 9, 9);
        assertEquals(PlayerID.PLAYER_1, b.ownerId());
    }
    
    @Test
    public void getPositionTest(){
        Bomb b = new Bomb(PlayerID.PLAYER_1, new Cell(0,0), 9, 9);
        assertEquals(new Cell(0,0), b.position());
    }
    
    @Test
    public void getFuseLengthsTest(){
        //secondary constructor
        Bomb b = new Bomb(PlayerID.PLAYER_1, new Cell(0,0), 9, 9);
        Sq<Integer> fuse2 = b.fuseLengths();
        for(int i = 9; i >0; i--){
            assertEquals(i, fuse2.head().intValue());
            fuse2 = fuse2.tail();
        }
        //primary constructor
        Sq<Integer> fuse = Sq.iterate(9, u -> u-1).limit(9);
        b = new Bomb(PlayerID.PLAYER_1, new Cell(0,0), fuse, 9);
        fuse2 = b.fuseLengths();
        for(int i = 9; i > 0; i--){
            assertEquals(i, fuse2.head().intValue());
            fuse2 = fuse2.tail();
        }
    }
    
    @Test
    public void getFuseLengthTest(){
        //secondary constructor
        Bomb b = new Bomb(PlayerID.PLAYER_1, new Cell(0,0), 9, 9);
        assertEquals(9, b.fuseLength());
        //primary constructor
        Sq<Integer> fuse = Sq.iterate(7, u -> u-1).limit(9);
        b = new Bomb(PlayerID.PLAYER_1, new Cell(0,0), fuse, 9);
        assertEquals(7, b.fuseLength());
    }
    
    @Test
    public void getRangeTest(){
        //secondary constructor
        Bomb b = new Bomb(PlayerID.PLAYER_1, new Cell(0,0), 9, 9);
        assertEquals(9, b.range());
        //primary constructor
        b = new Bomb(PlayerID.PLAYER_1, new Cell(0,0), Sq.iterate(7, u -> u-1).limit(9), 7);
        assertEquals(7, b.range());
    }
    
    //XXX test not that important, methods really easy
    @Test
    public void explosionTest(){
        List<Sq<Sq<Cell>>> ex = new Bomb(PlayerID.PLAYER_1, new Cell(4,4), 9, 4).explosion();
        for(Sq<Sq<Cell>> arm : ex){
            while(!arm.tail().isEmpty()){
                assertEquals(new Cell(4,4), arm.head().head());
                arm = arm.tail();
            }
        }
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void explosionImutabilityTest(){
        List<Sq<Sq<Cell>>> ex = new Bomb(PlayerID.PLAYER_1, new Cell(4,4), 9, 4).explosion();
        ex.add(Sq.constant(Sq.constant(new Cell(1,1))));
    }
}
