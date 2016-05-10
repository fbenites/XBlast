package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class representing a encoder/decoder by run length encoding for Byte. Code:
 * Number of repetitions(negative) - Object/Byte(positive).
 * 
 * @author Lorenz Rasch (249937)
 */
public final class RunLengthEncoder {

    private RunLengthEncoder() {
    }

    /**
     * Endcodes a list of bytes by run length encoding. First is the number of
     * repetitions(negative), then the byte(positive). Object only once/twice
     * for short repetitions. Up to 130 repetitions.
     * 
     * @param message
     *            the list of bytes to encode
     * @return life of bytes representing the code
     * @throws IllegalArgumentException
     *             if a negative entry in the argument list is encountered
     */
    public static List<Byte> encode(List<Byte> message) {
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
     * Decodes a list of bytes from run length encoding. First is the number of
     * repetitions(negative) then the byte(positive). Object only once/twice for
     * short repetitions. Up to 130 repetitions.
     * 
     * @param code
     *            the message to decode
     * @return list of bytes representing the original message
     * @throws IllegalArgumentException
     *             if the last entry in the list is negative
     */
    public static List<Byte> decode(List<Byte> code) {
        // check last element
        if (code.get(code.size() - 1) < 0) {
            throw new IllegalArgumentException(
                    "Last entry of list is negative.");
        }
        // go through list
        List<Byte> full = new ArrayList<Byte>();
        int count = 1;
        for (Byte b : code) {
            if (b < 0) {
                // negative number is a counter
                count = -b + 2;
            } else {
                // positive number is added to the message
                full.addAll(Collections.nCopies(count, b));
                count = 1;
            }
        }
        return full;
    }

}
