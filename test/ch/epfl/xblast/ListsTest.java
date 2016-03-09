package ch.epfl.xblast;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ListsTest {
    @Test
    public void integerListCheck() {
        List<Integer> li = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> l2 = Lists.mirrored(li);
        List<Integer> l1 = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,4,3,2,1));
        // check if li has been altered
        for(Integer i = 1; i <= li.size(); i++){
            assertEquals(i, li.get(i-1));
        }
        for(int i = 0; i < l1.size(); i++){
            assertEquals(l1.get(i), l2.get(i));
        }
    }
    
    @Test
    public void stringListCheck() {
        List<String> li = new ArrayList<String>(Arrays.asList("k","a","y"));
        List<String> l2 = Lists.mirrored(li);
        List<String> l1 = new ArrayList<String>(Arrays.asList("k","a","y","a","k"));
        for(int i = 0; i < l1.size(); i++){
            assertEquals(l1.get(i), l2.get(i));
        }
    }
    
    @Test
    public void oneElementCheck(){
        List<Integer> li = new ArrayList<Integer>(Arrays.asList(1));
        List<Integer> l2 = Lists.mirrored(li);
        List<Integer> l1 = new ArrayList<Integer>(Arrays.asList(1));
        for(int i = 0; i < l1.size(); i++){
            assertEquals(l1.get(i), l2.get(i));
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void zeroElementExceptionCheck(){
        List<Integer> li = new ArrayList<Integer>();
        Lists.mirrored(li);
    }
}
