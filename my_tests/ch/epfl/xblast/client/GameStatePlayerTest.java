package ch.epfl.xblast.client;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;

import org.junit.Test;

import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;

public class GameStatePlayerTest {

    @Test (expected = NullPointerException.class)
    public void exceptionTest01() {
        new GameState.Player(null, 4, new SubCell(1,1), new BufferedImage(1,1,1));
    }

    @Test (expected = NullPointerException.class)
    public void exceptionTest03() {
        new GameState.Player(PlayerID.PLAYER_1, 4, null, new BufferedImage(1,1,1));
    }

    @Test (expected = NullPointerException.class)
    public void exceptionTest04() {
        new GameState.Player(PlayerID.PLAYER_1, 4, new SubCell(1,1), null);
    }

    @Test
    public void idTest() {
        GameState.Player p = new GameState.Player(PlayerID.PLAYER_1, 4, new SubCell(1,1), new BufferedImage(1,1,1));
        assertEquals(PlayerID.PLAYER_1, p.id());
    }

    @Test
    public void livesTest() {
        GameState.Player p = new GameState.Player(PlayerID.PLAYER_1, 4, new SubCell(1,1), new BufferedImage(1,1,1));
        assertEquals(4, p.lives());
    }

    @Test
    public void positionTest() {
        GameState.Player p = new GameState.Player(PlayerID.PLAYER_1, 4, new SubCell(1,1), new BufferedImage(1,1,1));
        assertEquals(new SubCell(1,1), p.position());
    }

    @Test
    public void isAliveTest() {
        GameState.Player p = new GameState.Player(PlayerID.PLAYER_1, 4, new SubCell(1,1), new BufferedImage(1,1,1));
        assertTrue(p.isAlive());
        p = new GameState.Player(PlayerID.PLAYER_1, 0, new SubCell(1,1), new BufferedImage(1,1,1));
        assertFalse(p.isAlive());
        p = new GameState.Player(PlayerID.PLAYER_1, -2, new SubCell(1,1), new BufferedImage(1,1,1));
        assertFalse(p.isAlive());
    }

}
