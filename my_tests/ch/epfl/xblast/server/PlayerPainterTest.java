package ch.epfl.xblast.server;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;

public class PlayerPainterTest {

    @Test
    public void byteForPlayerTest01() {
        // player facing south
        Player p = new Player(PlayerID.PLAYER_1, 5, new Cell(1, 1), 5, 5);
        assertEquals(6, PlayerPainter.byteForPlayer(4, p));
        p = new Player(PlayerID.PLAYER_2, 5, new Cell(1, 1), 5, 5);
        assertEquals(26, PlayerPainter.byteForPlayer(4, p));
        p = new Player(PlayerID.PLAYER_3, 5, new Cell(1, 1), 5, 5);
        assertEquals(46, PlayerPainter.byteForPlayer(4, p));
        p = new Player(PlayerID.PLAYER_4, 5, new Cell(1, 1), 5, 5);
        assertEquals(66, PlayerPainter.byteForPlayer(4, p));
    }

    @Test
    public void byteForPlayerTest02() {
        // testing invulnerable players/dead/dying players
        // invulnerable
        Player p = new Player(PlayerID.PLAYER_1,
                Sq.constant(new Player.LifeState(1,
                        Player.LifeState.State.INVULNERABLE)),
                Sq.constant(new Player.DirectedPosition(new SubCell(4, 4),
                        Direction.S)), 5, 5);
        assertEquals(86, PlayerPainter.byteForPlayer(5, p));
        assertEquals(6, PlayerPainter.byteForPlayer(4, p));
        p = new Player(PlayerID.PLAYER_2, Sq.constant(new Player.LifeState(1,
                Player.LifeState.State.INVULNERABLE)),
                Sq.constant(new Player.DirectedPosition(new SubCell(4, 4),
                        Direction.S)), 5, 5);
        assertEquals(86, PlayerPainter.byteForPlayer(5, p));
        assertEquals(26, PlayerPainter.byteForPlayer(4, p));
        // dying
        p = new Player(PlayerID.PLAYER_1, Sq.constant(new Player.LifeState(3,
                Player.LifeState.State.DYING)),
                Sq.constant(new Player.DirectedPosition(new SubCell(4, 4),
                        Direction.N)), 5, 5);
        assertEquals(12, PlayerPainter.byteForPlayer(5, p));
        p = new Player(PlayerID.PLAYER_1, Sq.constant(new Player.LifeState(1,
                Player.LifeState.State.DYING)),
                Sq.constant(new Player.DirectedPosition(new SubCell(4, 4),
                        Direction.N)), 5, 5);
        assertEquals(13, PlayerPainter.byteForPlayer(5, p));
        p = new Player(PlayerID.PLAYER_3, Sq.constant(new Player.LifeState(3,
                Player.LifeState.State.DYING)),
                Sq.constant(new Player.DirectedPosition(new SubCell(4, 4),
                        Direction.N)), 5, 5);
        assertEquals(52, PlayerPainter.byteForPlayer(5, p));
        p = new Player(PlayerID.PLAYER_3, Sq.constant(new Player.LifeState(1,
                Player.LifeState.State.DYING)),
                Sq.constant(new Player.DirectedPosition(new SubCell(4, 4),
                        Direction.N)), 5, 5);
        assertEquals(53, PlayerPainter.byteForPlayer(5, p));
        // dead
        p = new Player(PlayerID.PLAYER_1, Sq.constant(new Player.LifeState(0,
                Player.LifeState.State.DYING)),
                Sq.constant(new Player.DirectedPosition(new SubCell(4, 4),
                        Direction.N)), 5, 5);
        assertEquals(15, PlayerPainter.byteForPlayer(5, p));
        p = new Player(PlayerID.PLAYER_4, Sq.constant(new Player.LifeState(0,
                Player.LifeState.State.DEAD)),
                Sq.constant(new Player.DirectedPosition(new SubCell(4, 4),
                        Direction.N)), 5, 5);
        assertEquals(15, PlayerPainter.byteForPlayer(5, p));
    }

    @Test
    public void byteForPlayerTest03() {
        // testing direction
        // south
        Player p = new Player(PlayerID.PLAYER_1,
                Sq.constant(new Player.LifeState(1,
                        Player.LifeState.State.VULNERABLE)),
                Sq.constant(new Player.DirectedPosition(new SubCell(4, 4),
                        Direction.S)), 5, 5);
        assertEquals(6, PlayerPainter.byteForPlayer(5, p));
        p = new Player(PlayerID.PLAYER_1, Sq.constant(new Player.LifeState(1,
                Player.LifeState.State.VULNERABLE)),
                Sq.constant(new Player.DirectedPosition(new SubCell(4, 5),
                        Direction.S)), 5, 5);
        assertEquals(7, PlayerPainter.byteForPlayer(5, p));
        p = new Player(PlayerID.PLAYER_1, Sq.constant(new Player.LifeState(1,
                Player.LifeState.State.VULNERABLE)),
                Sq.constant(new Player.DirectedPosition(new SubCell(4, 6),
                        Direction.S)), 5, 5);
        assertEquals(6, PlayerPainter.byteForPlayer(5, p));
        p = new Player(PlayerID.PLAYER_1, Sq.constant(new Player.LifeState(1,
                Player.LifeState.State.VULNERABLE)),
                Sq.constant(new Player.DirectedPosition(new SubCell(4, 7),
                        Direction.S)), 5, 5);
        assertEquals(8, PlayerPainter.byteForPlayer(5, p));

        // north
        p = new Player(PlayerID.PLAYER_1, Sq.constant(new Player.LifeState(1,
                Player.LifeState.State.VULNERABLE)),
                Sq.constant(new Player.DirectedPosition(new SubCell(4, 4),
                        Direction.N)), 5, 5);
        assertEquals(0, PlayerPainter.byteForPlayer(5, p));
        p = new Player(PlayerID.PLAYER_1, Sq.constant(new Player.LifeState(1,
                Player.LifeState.State.VULNERABLE)),
                Sq.constant(new Player.DirectedPosition(new SubCell(4, 5),
                        Direction.N)), 5, 5);
        assertEquals(1, PlayerPainter.byteForPlayer(5, p));
        p = new Player(PlayerID.PLAYER_1, Sq.constant(new Player.LifeState(1,
                Player.LifeState.State.VULNERABLE)),
                Sq.constant(new Player.DirectedPosition(new SubCell(4, 6),
                        Direction.N)), 5, 5);
        assertEquals(0, PlayerPainter.byteForPlayer(5, p));
        p = new Player(PlayerID.PLAYER_1, Sq.constant(new Player.LifeState(1,
                Player.LifeState.State.VULNERABLE)),
                Sq.constant(new Player.DirectedPosition(new SubCell(4, 7),
                        Direction.N)), 5, 5);
        assertEquals(2, PlayerPainter.byteForPlayer(5, p));

        // east
        p = new Player(PlayerID.PLAYER_1, Sq.constant(new Player.LifeState(1,
                Player.LifeState.State.VULNERABLE)),
                Sq.constant(new Player.DirectedPosition(new SubCell(4, 4),
                        Direction.E)), 5, 5);
        assertEquals(3, PlayerPainter.byteForPlayer(5, p));
        p = new Player(PlayerID.PLAYER_1, Sq.constant(new Player.LifeState(1,
                Player.LifeState.State.VULNERABLE)),
                Sq.constant(new Player.DirectedPosition(new SubCell(5, 4),
                        Direction.E)), 5, 5);
        assertEquals(4, PlayerPainter.byteForPlayer(5, p));
        p = new Player(PlayerID.PLAYER_1, Sq.constant(new Player.LifeState(1,
                Player.LifeState.State.VULNERABLE)),
                Sq.constant(new Player.DirectedPosition(new SubCell(6, 4),
                        Direction.E)), 5, 5);
        assertEquals(3, PlayerPainter.byteForPlayer(5, p));
        p = new Player(PlayerID.PLAYER_1, Sq.constant(new Player.LifeState(1,
                Player.LifeState.State.VULNERABLE)),
                Sq.constant(new Player.DirectedPosition(new SubCell(7, 4),
                        Direction.E)), 5, 5);
        assertEquals(5, PlayerPainter.byteForPlayer(5, p));

        // east
        p = new Player(PlayerID.PLAYER_1, Sq.constant(new Player.LifeState(1,
                Player.LifeState.State.VULNERABLE)),
                Sq.constant(new Player.DirectedPosition(new SubCell(4, 4),
                        Direction.W)), 5, 5);
        assertEquals(9, PlayerPainter.byteForPlayer(5, p));
        p = new Player(PlayerID.PLAYER_1, Sq.constant(new Player.LifeState(1,
                Player.LifeState.State.VULNERABLE)),
                Sq.constant(new Player.DirectedPosition(new SubCell(5, 4),
                        Direction.W)), 5, 5);
        assertEquals(10, PlayerPainter.byteForPlayer(5, p));
        p = new Player(PlayerID.PLAYER_1, Sq.constant(new Player.LifeState(1,
                Player.LifeState.State.VULNERABLE)),
                Sq.constant(new Player.DirectedPosition(new SubCell(6, 4),
                        Direction.W)), 5, 5);
        assertEquals(9, PlayerPainter.byteForPlayer(5, p));
        p = new Player(PlayerID.PLAYER_1, Sq.constant(new Player.LifeState(1,
                Player.LifeState.State.VULNERABLE)),
                Sq.constant(new Player.DirectedPosition(new SubCell(7, 4),
                        Direction.W)), 5, 5);
        assertEquals(11, PlayerPainter.byteForPlayer(5, p));
    }

}
