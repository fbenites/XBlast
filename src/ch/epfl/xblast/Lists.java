package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//TODO comments

public final class Lists {
    
    private Lists(){}
    
    public static <T> List<T> mirrored(List<T> l) throws IllegalArgumentException{
        if(l.size() == 0){
            throw new IllegalArgumentException("Specified list is empty.");
        }
        List<T> full = new ArrayList<T>(l);
        List<T> sub = new ArrayList<T>(l).subList(0, l.size() - 1);
        Collections.reverse(sub);
        full.addAll(sub);
        return full;
    }
    
    public static <T> List<List<T>> permutations(List<T> l){
        List<List<T>> result = new ArrayList<List<T>>();
        if(l.isEmpty()){
            result.add(new ArrayList<T>());
        } else {
            T t = l.get(0);
            List<List<T>> ll = permutations(l.subList(1, l.size()));
            for(List<T> list : ll){
                for(int i = 0; i <= list.size(); i++){
                    List<T> list2 = new ArrayList<T>(list);
                    list2.add(i, t);
                    result.add(list2);
                }
            }
        }
        return result;
    }

}
