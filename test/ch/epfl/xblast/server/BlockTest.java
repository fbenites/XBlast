package ch.epfl.xblast.server;

import static org.junit.Assert.*;

import org.junit.Test;

public class BlockTest {

    @Test
    public void isFreeTest() {
        int i = 0;
        for(Block b : Block.values()){
            if(b.isFree()){
                i++;
            }
        }
        assertEquals(1, i);
    }

    @Test
    public void canHostPlayerTest() {
        int i = 0;
        for(Block b : Block.values()){
            if(b.canHostPlayer()){
                i++;
            }
        }
        assertEquals(1, i);
    }

    @Test
    public void castsShadowTest() {
        int i = 0;
        for(Block b : Block.values()){
            if(b.castsShadow()){
                i++;
            }
        }
        assertEquals(3, i);
    }

}
