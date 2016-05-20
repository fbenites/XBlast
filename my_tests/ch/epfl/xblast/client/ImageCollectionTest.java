package ch.epfl.xblast.client;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;

import org.junit.Test;

public class ImageCollectionTest {

    @Test
    public void testPlayers() {
        ImageCollection ic = ImageCollection.PLAYERS;
        for(int i = 0; i < 14; i++){
            ic.image(i);
            assertEquals(ic.image(i), ic.imageOrNull(i));
            ic.image(i+20);
            assertEquals(ic.image(i+20), ic.imageOrNull(i+20));
            ic.image(i+40);
            assertEquals(ic.image(i+40), ic.imageOrNull(i+40));
            ic.image(i+60);
            assertEquals(ic.image(i+60), ic.imageOrNull(i+60));
        }
        for(int i = 80; i < 92; i++){
            ic.image(i);
            assertEquals(ic.image(i), ic.imageOrNull(i));
        }
        assertNull(ic.imageOrNull(16));
    }

    @Test
    public void testBlocks() {
        ImageCollection ic = ImageCollection.BLOCKS;
        for(int i = 0; i < 7; i++){
            ic.image(i);
            assertEquals(ic.image(i), ic.imageOrNull(i));
        }
        assertNull(ic.imageOrNull(9));
    }

    @Test
    public void testExplosion() {
        ImageCollection ic = ImageCollection.EXPLOSIONS;
        for(int i = 0; i < 16; i++){
            ic.image(i);
            assertEquals(ic.image(i), ic.imageOrNull(i));
        }
        ic.image(20);
        ic.image(21);
        assertNull(ic.imageOrNull(18));
        assertNull(ic.imageOrNull(22));
    }

    @Test
    public void testScore() {
        ImageCollection ic = ImageCollection.SCORE;
        for(int i = 0; i < 8; i++){
            ic.image(i);
            assertEquals(ic.image(i), ic.imageOrNull(i));
        }
        ic.image(10);
        ic.image(11);
        ic.image(12);
        ic.image(20);
        ic.image(21);
        assertNull(ic.imageOrNull(22));
    }
    
    @Test(expected = NoSuchElementException.class)
    public void exceptionTest(){
        ImageCollection ic = ImageCollection.SCORE;
        ic.image(8);
    }
}
