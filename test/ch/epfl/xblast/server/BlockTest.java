package ch.epfl.xblast.server;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;

import org.junit.Test;

/**
 * @author Lorenz Rasch (249937)
 */
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
        assertEquals(3, i);
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
    
    @Test
    public void isBonusTest(){
        int i = 0;
        for(Block b : Block.values()){
            if(b.isBonus()){
                i++;
            }
        }
        assertEquals(2, i);
    }
    
    @Test
    public void associatedBonusTest(){
        assertEquals(Bonus.INC_BOMB, Block.BONUS_BOMB.associatedBonus());
        assertEquals(Bonus.INC_RANGE, Block.BONUS_RANGE.associatedBonus());
    }
    
    @Test(expected = NoSuchElementException.class)
    public void bonusExceptionTest(){
        Block.INDESTRUCTIBLE_WALL.associatedBonus();
    }

}
