package ch.epfl.xblast.server;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class GameStateSerializerTest {

    @Test
    public void test() {
        List<Byte> l = GameStateSerializer.serialize(Level.DEFAULT_LEVEL);
        
        List<Integer> x = new ArrayList<Integer>(Arrays.asList(
                121, -50, 2, 1, -2, 0, 3, 1, 3, 1, -2, 0, 1, 1, 3, 1, 3,
                1, 3, 1, 1, -2, 0, 1, 3, 1, 3, -2, 0, -1, 1, 3, 1, 3, 1,
                3, 1, 1, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3,
                2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2,
                3, 1, 0, 0, 3, 1, 3, 1, 0, 0, 1, 1, 3, 1, 1, 0, 0, 1, 3,
                1, 3, 0, 0, -1, 1, 3, 1, 1, -5, 2, 3, 2, 3, -5, 2, 3, 2,
                3, 1, -2, 0, 3, -2, 0, 1, 3, 2, 1, 2,

                4, -128, 16, -63, 16,

                3, 24, 24, 6,
                3, -40, 24, 26,
                3, -40, -72, 46,
                3, 24, -72, 66,

                60));
//        System.out.print(l.toString());
        for(int i = 0; i < x.size(); i++){
            assertEquals(x.get(i).intValue(), l.get(i).intValue());
//            assertTrue(x.get(i).equals(l.get(i)));
        }
    }

}
