package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class providing methods for useful operations on Lists.
 * 
 * @author Lorenz Rasch (249937)
 */
public final class Lists {

    private Lists() {
    }

    /**
     * Gives a mirrored "image" of the List it is applied to. It's last element
     * will be the middle of the mirrored List.
     * 
     * @param l
     *            the List to mirror
     * @return the mirrored List
     * @throws IllegalArgumentException
     *             if the provided List is empty
     */
    public static <T> List<T> mirrored(List<T> l)
            throws IllegalArgumentException {
        if (l.isEmpty()) {
            throw new IllegalArgumentException("Specified list is empty.");
        }
        // create new list from provided list
        List<T> full = new ArrayList<T>(l);

        // get sublist of provided list without last element, mirror and add
        List<T> sub = new ArrayList<T>(l).subList(0, l.size() - 1);
        Collections.reverse(sub);
        full.addAll(sub);
        return Collections.unmodifiableList(full);
    }

    /**
     * Gives all possible permutations of a provided List.
     * 
     * @param l
     *            the List
     * @return a List with all permutations
     */
    public static <T> List<List<T>> permutations(List<T> l) {
        List<List<T>> result = new ArrayList<List<T>>();
        // permutation of empty List is empty List
        if (l.isEmpty()) {
            result.add(Collections.emptyList());
        } else {
            // take first element, compute permutations of List w/o first
            // element
            T t = l.get(0);
            List<List<T>> ll = permutations(l.subList(1, l.size()));
            // add removed element into every permutation, at every position
            for (List<T> list : ll) {
                for (int i = 0; i <= list.size(); i++) {
                    List<T> list2 = new ArrayList<T>(list);
                    list2.add(i, t);
                    result.add(Collections.unmodifiableList(list2));
                }
            }
        }
        return Collections.unmodifiableList(result);
    }

}
