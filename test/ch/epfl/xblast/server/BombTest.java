package ch.epfl.xblast.server;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;

/**
 * @author Lorenz Rasch (249937)
 */
public class BombTest {

    //TODO check if tests complete
    @Test(expected = NullPointerException.class)
    public void constructorExceptionTest() {
        new Bomb(null, new Cell(0,0), Sq.iterate(0, u -> u+1).limit(9), 9);
        //new Bomb(PlayerID.PLAYER_1, null, Sq.iterate(0, u -> u+1).limit(9), 9);
        //new Bomb(PlayerID.PLAYER_1, new Cell(0,0), null, 9);
    }

}
