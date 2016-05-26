package ch.epfl.xblast;

import static org.junit.Assert.*;

import org.junit.Test;

public class PlayerActionTest {

    @Test
    public void test() {
        assertEquals(7, PlayerAction.values().length);
        assertEquals(0, PlayerAction.JOIN_GAME.ordinal());
        assertEquals(1, PlayerAction.MOVE_N.ordinal());
        assertEquals(2, PlayerAction.MOVE_E.ordinal());
        assertEquals(3, PlayerAction.MOVE_S.ordinal());
        assertEquals(4, PlayerAction.MOVE_W.ordinal());
        assertEquals(5, PlayerAction.STOP.ordinal());
        assertEquals(6, PlayerAction.DROP_BOMB.ordinal());
    }

}
