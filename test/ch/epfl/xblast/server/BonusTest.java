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
public class BonusTest {

    @Test
    public void incBombTest() {
        Player p = new Player(PlayerID.PLAYER_1, Sq.constant(new LifeState(1, LifeState.State.VULNERABLE)),
            Sq.constant(new DirectedPosition(SubCell.centralSubCellOf(new Cell(0,0)), Direction.S)), 0, 5);
        for(int i = 0; i <= 9; i++){
            assertEquals(i, p.maxBombs());
            p = Bonus.INC_BOMB.applyTo(p);
        }
        p = Bonus.INC_BOMB.applyTo(p);
        p = Bonus.INC_BOMB.applyTo(p);
        assertEquals(9, p.maxBombs());
        
        p = new Player(PlayerID.PLAYER_1, Sq.constant(new LifeState(1, LifeState.State.VULNERABLE)),
                Sq.constant(new DirectedPosition(SubCell.centralSubCellOf(new Cell(0,0)), Direction.S)), 11, 5);
        assertEquals(11, p.maxBombs());
        p = Bonus.INC_BOMB.applyTo(p);
        p = Bonus.INC_BOMB.applyTo(p);
        assertEquals(11, p.maxBombs());
    }

    @Test
    public void incRangeTest() {
        Player p = new Player(PlayerID.PLAYER_1, Sq.constant(new LifeState(1, LifeState.State.VULNERABLE)),
            Sq.constant(new DirectedPosition(SubCell.centralSubCellOf(new Cell(0,0)), Direction.S)), 0, 0);
        for(int i = 0; i <= 9; i++){
            assertEquals(i, p.bombRange());
            p = Bonus.INC_RANGE.applyTo(p);
        }
        p = Bonus.INC_RANGE.applyTo(p);
        p = Bonus.INC_RANGE.applyTo(p);
        assertEquals(9, p.bombRange());
        
        p = new Player(PlayerID.PLAYER_1, Sq.constant(new LifeState(1, LifeState.State.VULNERABLE)),
                Sq.constant(new DirectedPosition(SubCell.centralSubCellOf(new Cell(0,0)), Direction.S)), 11, 11);
        assertEquals(11, p.bombRange());
        p = Bonus.INC_RANGE.applyTo(p);
        p = Bonus.INC_RANGE.applyTo(p);
        assertEquals(11, p.bombRange());
    }

}
