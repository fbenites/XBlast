package ch.epfl.xblast.server;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.server.Player.DirectedPosition;

public class DirectedPositionTest {

    @Test(expected = NullPointerException.class)
    public void constructorExceptionTest01() {
        new DirectedPosition(null, Direction.W);
    }

    @Test(expected = NullPointerException.class)
    public void constructorExceptionTest02() {
        new DirectedPosition(new SubCell(0,1), null);
    }
    
    @Test
    public void stoppedTest(){
        Sq<DirectedPosition> dp = DirectedPosition.stopped(new DirectedPosition(new SubCell(15,34), Direction.E));
        for(int i = 0; i < 20; i++){
            assertEquals(new SubCell(15,34), dp.head().position());
            assertEquals(Direction.E, dp.head().direction());
            dp = dp.tail();
        }
    }
    
    @Test
    public void movingTest(){
        Sq<DirectedPosition> dp = DirectedPosition.moving(new DirectedPosition(new SubCell(0,1), Direction.E));
        for(int i = 0; i < 300; i++){
            assertEquals(new SubCell(i, 1), dp.head().position());
            dp = dp.tail();
        }
    }
    
    @Test
    public void withPositionTest(){
        DirectedPosition dp = new DirectedPosition(new SubCell(0,0), Direction.W);
        dp = dp.withPosition(new SubCell(15,34));
        assertEquals(new SubCell(15,34), dp.position());
    }
    
    @Test
    public void withDirectionTest(){
        DirectedPosition dp = new DirectedPosition(new SubCell(0,0), Direction.W);
        dp = dp.withDirection(Direction.E);
        assertEquals(Direction.E, dp.direction());
    }

}
