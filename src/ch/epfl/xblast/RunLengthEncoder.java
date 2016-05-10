package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TODO
 * 
 * @author Lorenz Rasch (249937)
 *
 */
public final class RunLengthEncoder {

    private RunLengthEncoder() {
    }

    /**
     * TODO
     * 
     * @param message
     * @return
     * @throws IllegalArgumentException
     */
    public static List<Byte> encode(List<Byte> message)
            throws IllegalArgumentException {
        List<Byte> code = new ArrayList<Byte>();
        // work on list if not empty
        if (!message.isEmpty()) {
            // get first element and set counter
            byte last = message.get(0);
            int counter = 0;
            // iterate over list, except first element
            for (Byte b : message.subList(1, message.size())) {
                // negative bytes throw exception
                if (b < 0) {
                    throw new IllegalArgumentException(
                            "Negative entry encountered.");
                } else if (b == last) {
                    // advance counter if byte is same as before
                    counter++;
                    if ((-counter) < Byte.MIN_VALUE) {
                        code.add(Byte.MIN_VALUE);
                        code.add(last);
                        counter = -1;
                    }
                } else {
                    // add encoding, according to counter
                    switch (counter) {
                    case -1:
                        break;
                    case 0:
                        // no repetition, only add 1 of last byte
                        code.add(last);
                        break;
                    case 1:
                        // 1 repetition, add 2 of last byte
                        code.addAll(Collections.nCopies(2, last));
                        break;
                    default:
                        // more than 1 repetition, add number of repetitions
                        // and the byte
                        code.add((byte) ((counter - 1) * -1));
                        code.add(last);
                    }
                    // reset counter and byte
                    last = b;
                    counter = 0;
                }
            }
            // add encoding for last byte/sequence
            switch (counter) {
            case -1:
                break;
            case 0:
                // no repetition, only add 1 of last byte
                code.add(last);
                break;
            case 1:
                // 1 repetition, add 2 of last byte
                code.addAll(Collections.nCopies(2, last));
                break;
            default:
                // more than 1 repetition, add number of repetitions and the
                // byte
                code.add((byte) ((counter - 2) * -1));
                code.add(last);
            }
        }
        return code;
    }

    /**
     * TODO
     * 
     * @param code
     * @return
     * @throws IllegalArgumentException
     */
    public static List<Byte> decode(List<Byte> code)
            throws IllegalArgumentException {
        if (code.get(code.size() - 1) < 0) {
            throw new IllegalArgumentException(
                    "Last entry of list is negative.");
        }
        List<Byte> full = new ArrayList<Byte>();
        int count = 1;
        for (Byte b : code) {
            if (b < 0) {
                count = -b + 2;
            } else {
                full.addAll(Collections.nCopies(count, b));
                count = 1;
            }
        }
        return full;
    }

}
