package ch.epfl.xblast.server;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.epfl.xblast.server.Player.LifeState;

/**
 * @author Lorenz Rasch (249937)
 *
 */
public class LifeStateTest {

    @Test(expected = IllegalArgumentException.class)
    public void constructorExceptionTest01() {
        new LifeState(-1, LifeState.State.DEAD);
    }
    
    @Test(expected = NullPointerException.class)
    public void constructorExceptionTest02(){
        new LifeState(3, null);
    }
    
    @Test
    public void generalTest(){
        LifeState ls = new LifeState(100, LifeState.State.VULNERABLE);
        assertEquals(100, ls.lives());
        assertEquals(LifeState.State.VULNERABLE, ls.state());
        assertTrue(ls.canMove());
        
        ls = new LifeState(0, LifeState.State.INVULNERABLE);
        assertEquals(LifeState.State.INVULNERABLE, ls.state());
        assertTrue(ls.canMove());
    }

}
