package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

}
