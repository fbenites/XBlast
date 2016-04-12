package ch.epfl.xblast.server;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.server.Player.DirectedPosition;
import ch.epfl.xblast.server.Player.LifeState;

/**
 * @author Lorenz Rasch (249937)
 */
public class PlayerTest {

    // primary constructor
    @Test(expected = NullPointerException.class)
    public void constructorExceptionTest01() {
        new Player(null, Sq.constant(new LifeState(5,
                LifeState.State.VULNERABLE)), Sq.constant(new DirectedPosition(
                new SubCell(0, 0), Direction.S)), 9, 9);
    }

    @Test(expected = NullPointerException.class)
    public void constructorExceptionTest02() {
        new Player(PlayerID.PLAYER_1, null, Sq.constant(new DirectedPosition(
                new SubCell(0, 0), Direction.S)), 9, 9);
    }

    @Test(expected = NullPointerException.class)
    public void constructorExceptionTest03() {
        new Player(PlayerID.PLAYER_1, Sq.constant(new LifeState(5,
                LifeState.State.VULNERABLE)), null, 9, 9);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorExceptionTest04() {
        new Player(PlayerID.PLAYER_1, Sq.constant(new LifeState(5,
                LifeState.State.VULNERABLE)), Sq.constant(new DirectedPosition(
                new SubCell(0, 0), Direction.S)), 12, 9);
        new Player(PlayerID.PLAYER_1, Sq.constant(new LifeState(5,
                LifeState.State.VULNERABLE)), Sq.constant(new DirectedPosition(
                new SubCell(0, 0), Direction.S)), -1, 9);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorExceptionTest05() {
        new Player(PlayerID.PLAYER_1, Sq.constant(new LifeState(5,
                LifeState.State.VULNERABLE)), Sq.constant(new DirectedPosition(
                new SubCell(0, 0), Direction.S)), 9, 12);
        new Player(PlayerID.PLAYER_1, Sq.constant(new LifeState(5,
                LifeState.State.VULNERABLE)), Sq.constant(new DirectedPosition(
                new SubCell(0, 0), Direction.S)), 9, -1);
    }

    // secondary constructor
    @Test(expected = NullPointerException.class)
    public void constructorExceptionTest06() {
        new Player(PlayerID.PLAYER_1, 0, null, 9, 5);
    }

    @Test
    public void getIdTest() {
        Player p = new Player(PlayerID.PLAYER_3, 0, new Cell(1, 1), 9, 5);
        assertEquals(PlayerID.PLAYER_3, p.id());
    }

    @Test
    public void getLifeStatesTest() {
        Player p = new Player(PlayerID.PLAYER_1, 5, new Cell(1, 1), 9, 9);
        assertEquals(5, p.lifeStates().head().lives());
        assertEquals(LifeState.State.INVULNERABLE, p.lifeStates().head()
                .state());

        Sq<LifeState> ls = Sq.constant(new LifeState(3, LifeState.State.DYING));
        p = new Player(PlayerID.PLAYER_1, ls, Sq.constant(new DirectedPosition(
                new SubCell(4, 4), Direction.S)), 9, 9);
        assertEquals(ls, p.lifeStates());
        assertEquals(LifeState.State.DYING, p.lifeStates().head().state());

        p = new Player(PlayerID.PLAYER_1, 0, new Cell(1, 1), 9, 9);
        assertEquals(LifeState.State.DEAD, p.lifeStates().head().state());
    }

    @Test
    public void getLifeStateTest() {
        Player p = new Player(PlayerID.PLAYER_1, 5, new Cell(1, 1), 9, 9);
        assertEquals(5, p.lifeState().lives());
        assertEquals(LifeState.State.INVULNERABLE, p.lifeState().state());

        LifeState ls = new LifeState(3, LifeState.State.DYING);
        p = new Player(
                PlayerID.PLAYER_1,
                Sq.constant(ls),
                Sq.constant(new DirectedPosition(new SubCell(4, 4), Direction.S)),
                9, 9);
        assertEquals(ls, p.lifeState());
        assertEquals(LifeState.State.DYING, p.lifeState().state());

        p = new Player(PlayerID.PLAYER_1, 0, new Cell(1, 1), 9, 9);
        assertEquals(LifeState.State.DEAD, p.lifeState().state());
    }

    @Test
    public void statesForNextLifeTest() {
        Player p = new Player(PlayerID.PLAYER_1, 5, new Cell(1, 1), 9, 9);
        Sq<LifeState> ls = p.statesForNextLife();
        assertEquals(5, ls.head().lives());
        assertEquals(LifeState.State.DYING, ls.head().state());
        ls = ls.dropWhile(u -> u.state() == LifeState.State.DYING);
        assertEquals(4, ls.head().lives());
        assertEquals(LifeState.State.INVULNERABLE, ls.head().state());
        ls = ls.dropWhile(u -> u.state() == LifeState.State.INVULNERABLE);
        assertEquals(4, ls.head().lives());
        assertEquals(LifeState.State.VULNERABLE, ls.head().state());

        p = new Player(PlayerID.PLAYER_1, 1, new Cell(1, 1), 9, 9);
        ls = p.statesForNextLife();
        assertEquals(1, ls.head().lives());
        assertEquals(LifeState.State.DYING, ls.head().state());
        ls = ls.dropWhile(u -> u.state() == LifeState.State.DYING);
        assertEquals(0, ls.head().lives());
        assertEquals(LifeState.State.DEAD, ls.head().state());

        p = new Player(PlayerID.PLAYER_1, 0, new Cell(1, 1), 9, 9);
        ls = p.statesForNextLife();
        assertEquals(0, ls.head().lives());
        assertEquals(LifeState.State.DEAD, ls.head().state());
    }

    @Test
    public void getLivesTest() {
        Player p = new Player(PlayerID.PLAYER_1, 3, new Cell(1, 1), 9, 9);
        assertEquals(3, p.lives());
        p = new Player(PlayerID.PLAYER_1, 0, new Cell(1, 1), 9, 9);
        assertEquals(0, p.lives());
    }

    @Test
    public void isAliveTest() {
        Player p = new Player(PlayerID.PLAYER_1, 3, new Cell(1, 1), 9, 9);
        assertTrue(p.isAlive());
        p = new Player(PlayerID.PLAYER_1, 0, new Cell(1, 1), 9, 9);
        assertFalse(p.isAlive());
    }

    @Test
    public void getDirectedPositionTest() {
        Sq<DirectedPosition> dpos = Sq.constant(new DirectedPosition(new SubCell(1,1), Direction.S));
        Player p = new Player(PlayerID.PLAYER_1, Sq.constant(new LifeState(0, LifeState.State.DEAD)), dpos, 9, 9);
        assertEquals(dpos, p.directedPositions());
        
        p = new Player(PlayerID.PLAYER_1, 3, new Cell(1, 1), 9, 9);
        assertEquals(SubCell.centralSubCellOf(new Cell(1,1)), p.directedPositions().head().position());
        assertEquals(Direction.S, p.directedPositions().head().direction());
    }
    
    @Test
    public void getPositionTest(){
        Sq<DirectedPosition> dpos = Sq.constant(new DirectedPosition(new SubCell(1,1), Direction.S));
        Player p = new Player(PlayerID.PLAYER_1, Sq.constant(new LifeState(0, LifeState.State.DEAD)), dpos, 9, 9);
        assertEquals(new SubCell(1,1), p.position());
        
        p = new Player(PlayerID.PLAYER_1, 3, new Cell(1, 1), 9, 9);
        assertEquals(SubCell.centralSubCellOf(new Cell(1,1)), p.position());
    }
    
    @Test
    public void getDirectionTest(){
        Sq<DirectedPosition> dpos = Sq.constant(new DirectedPosition(new SubCell(1,1), Direction.N));
        Player p = new Player(PlayerID.PLAYER_1, Sq.constant(new LifeState(0, LifeState.State.DEAD)), dpos, 9, 9);
        assertEquals(Direction.N, p.direction());
        
        p = new Player(PlayerID.PLAYER_1, 3, new Cell(1, 1), 9, 9);
        assertEquals(Direction.S, p.direction());
    }
    
    @Test
    public void getMaxBombsTest(){
        //also checks withMaxBombs
        Player p = new Player(PlayerID.PLAYER_1, 3, new Cell(1, 1), 7, 5);
        assertEquals(7, p.maxBombs());
        p = Bonus.INC_BOMB.applyTo(p);
        assertEquals(8, p.maxBombs());
        p = Bonus.INC_BOMB.applyTo(p);
        p = Bonus.INC_BOMB.applyTo(p);
        assertEquals(9, p.maxBombs());
        
        p = new Player(PlayerID.PLAYER_1, 3, new Cell(1, 1), 12, 5);
        assertEquals(12, p.maxBombs());
        p = Bonus.INC_BOMB.applyTo(p);
        assertEquals(12, p.maxBombs());
    }
    
    @Test
    public void getBombRangeTest(){
        //also checks withBombRange
        Player p = new Player(PlayerID.PLAYER_1, 3, new Cell(1, 1), 5, 7);
        assertEquals(7, p.bombRange());
        p = Bonus.INC_RANGE.applyTo(p);
        assertEquals(8, p.bombRange());
        p = Bonus.INC_RANGE.applyTo(p);
        p = Bonus.INC_RANGE.applyTo(p);
        assertEquals(9, p.bombRange());
        
        p = new Player(PlayerID.PLAYER_1, 3, new Cell(1, 1), 5, 12);
        assertEquals(12, p.bombRange());
        p = Bonus.INC_RANGE.applyTo(p);
        assertEquals(12, p.bombRange());
    }
    
    @Test
    public void dropBombTest(){
        Player p = new Player(PlayerID.PLAYER_1, 3, new Cell(1, 1), 5, 7);
        Bomb b = p.newBomb();
        assertEquals(PlayerID.PLAYER_1, b.ownerId());
        assertEquals(new Cell(1,1), b.position());
        assertEquals(Ticks.BOMB_FUSE_TICKS, b.fuseLength());
        assertEquals(7, b.range());
    }

}
