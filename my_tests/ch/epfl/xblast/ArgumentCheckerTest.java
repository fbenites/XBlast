package ch.epfl.xblast;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Lorenz Rasch (249937)
 */
public class ArgumentCheckerTest {

    @Test
    public void normalTest() {
        assertEquals(9, ArgumentChecker.requireNonNegative(9));
        assertEquals(0, ArgumentChecker.requireNonNegative(0));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void exceptionTest(){
        ArgumentChecker.requireNonNegative(-1);
    }

}
