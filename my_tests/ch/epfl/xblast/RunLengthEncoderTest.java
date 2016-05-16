package ch.epfl.xblast;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class RunLengthEncoderTest {

    @Test(expected = IllegalArgumentException.class)
    public void encodeExceptionTest() {
        List<Byte> b = new ArrayList<Byte>(Arrays.asList((byte) 1, (byte) 2,
                (byte) 3, (byte) -5));
        RunLengthEncoder.encode(b);
    }

    @Test
    public void encodeTest() {
        List<Byte> b = new ArrayList<Byte>(Arrays.asList((byte) 1, (byte) 1,
                (byte) 1, (byte) 2, (byte) 3, (byte) 3, (byte) 4, (byte) 4,
                (byte) 4, (byte) 4, (byte) 5, (byte) 5, (byte) 5, (byte) 5,
                (byte) 5));
        for (int i = 0; i < 130; i++) {
            b.add((byte) 6);
        }
        for (int i = 0; i < 131; i++) {
            b.add((byte) 7);
        }
        for (int i = 0; i < 132; i++) {
            b.add((byte) 8);
        }
        for (int i = 0; i < 140; i++) {
            b.add((byte) 9);
        }
        List<Byte> c = RunLengthEncoder.encode(b);
        List<Byte> a = new ArrayList<Byte>(Arrays.asList(
                (byte) 22,
                (byte) -1, (byte) 1,
                (byte) 2, 
                (byte) 3, (byte) 3, 
                (byte) -2, (byte) 4, 
                (byte) -3, (byte) 5, 
                (byte) Byte.MIN_VALUE, (byte) 6,
                (byte) Byte.MIN_VALUE, (byte) 7, (byte) 7,
                (byte) Byte.MIN_VALUE, (byte) 8, (byte) 8, (byte) 8,
                (byte) Byte.MIN_VALUE, (byte) 9, (byte) -8, (byte) 9));

        for (int i = 0; i < a.size(); i++) {
            assertEquals(a.get(i), c.get(i));
        }
    }

    @Test
    public void decodeTest() {
        List<Byte> a = new ArrayList<Byte>(Arrays.asList(
                (byte) 22,
                (byte) -1, (byte) 1,
                (byte) 2,
                (byte) 3, (byte) 3,
                (byte) -2, (byte) 4,
                (byte) -3, (byte) 5,
                (byte) Byte.MIN_VALUE, (byte) 6,
                (byte) Byte.MIN_VALUE, (byte) 7, (byte) 7,
                (byte) Byte.MIN_VALUE, (byte) 8, (byte) 8, (byte) 8,
                (byte) Byte.MIN_VALUE, (byte) 9, (byte) -8, (byte) 9));
        List<Byte> b = new ArrayList<Byte>(Arrays.asList((byte) 1, (byte) 1,
                (byte) 1, (byte) 2, (byte) 3, (byte) 3, (byte) 4, (byte) 4,
                (byte) 4, (byte) 4, (byte) 5, (byte) 5, (byte) 5, (byte) 5,
                (byte) 5));
        for (int i = 0; i < 130; i++) {
            b.add((byte) 6);
        }
        for (int i = 0; i < 131; i++) {
            b.add((byte) 7);
        }
        for (int i = 0; i < 132; i++) {
            b.add((byte) 8);
        }
        for (int i = 0; i < 140; i++) {
            b.add((byte) 9);
        }
        List<Byte> c = RunLengthEncoder.decode(a);
        for(int i = 0; i < b.size(); i++){
            assertEquals(b.get(i), c.get(i));
        }
    }

}
