package ch.epfl.xblast.server;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;

public class ExplosionPainterTest {

    @Test
    public void constants() {
        assertEquals(16, ExplosionPainter.BYTE_FOR_EMPTY);
    }

    @Test
    public void byteForBombTest() {
        Bomb b = new Bomb(PlayerID.PLAYER_1, new Cell(1, 1), 65, 8);
        assertEquals(20, ExplosionPainter.byteForBomb(b));
        b = new Bomb(PlayerID.PLAYER_2, new Cell(3, 2), 64, 7);
        assertEquals(21, ExplosionPainter.byteForBomb(b));
        b = new Bomb(PlayerID.PLAYER_2, new Cell(3, 2), 2, 7);
        assertEquals(21, ExplosionPainter.byteForBomb(b));
        b = new Bomb(PlayerID.PLAYER_2, new Cell(3, 2), 1, 7);
        assertEquals(21, ExplosionPainter.byteForBomb(b));
    }

    @Test
    public void byteForBlastTest() {
        boolean no = false, ea = false, so = false, we = false;
        byte counter = 0;
        for (int n = 0; n < 2; n++) {
            if (n == 1)
                no = true;
            else
                no = false;
            for (int e = 0; e < 2; e++) {
                if (e == 1)
                    ea = true;
                else
                    ea = false;
                for (int s = 0; s < 2; s++) {
                    if (s == 1)
                        so = true;
                    else
                        so = false;
                    for (int w = 0; w < 2; w++) {
                        if (w == 1)
                            we = true;
                        else
                            we = false;
                        assertEquals(counter,
                                ExplosionPainter.byteForBlast(no, ea, so, we));
                        counter++;
                    }
                }
            }
        }
    }

}
