package ch.epfl.xblast;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * @author Lorenz Rasch (249937)
 */
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
    
    @Test
    public void permutationsTest(){
        List<Integer> li = new ArrayList<Integer>(Arrays.asList(1,2,3,4));
        List<List<Integer>> lip = Lists.permutations(li);
        //print out all permutations for checking by hand
//        for(List<Integer> lint : lip){
//            for(Integer i : lint){
//                System.out.print(i + " ,");
//            }
//            System.out.println();
//        }
        assertEquals(24, lip.size());
        
        li = new ArrayList<Integer>(Arrays.asList(1,2,3));
        lip = Lists.permutations(li);
        assertEquals(6, lip.size());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void mirroredImmutabilityTest(){
        List<Integer> li = new ArrayList<Integer>(Arrays.asList(2,3));
        li = Lists.mirrored(li);
        li.add(1);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void permutationsImmutabilityTest01(){
        List<Integer> li = new ArrayList<Integer>(Arrays.asList(2,3));
        List<List<Integer>> lip = Lists.permutations(li);
        lip.add(Arrays.asList(1));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void permutationsImmutabilityTest02(){
        List<Integer> li = new ArrayList<Integer>(Arrays.asList(2,3));
        List<List<Integer>> lip = Lists.permutations(li);
        lip.get(0).add(1);
    }
}
