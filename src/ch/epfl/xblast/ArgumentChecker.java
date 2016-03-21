package ch.epfl.xblast;

/**
 * Class helping with checking of arguments.
 * 
 * @author Lorenz Rasch (249937)
 */
public final class ArgumentChecker {

    private ArgumentChecker() {
    }

    /**
     * Checks if a given value is bigger or equal to zero.
     * 
     * @param value
     *            the value to be checked
     * @return the given value if it is bigger or equal to zero
     * @throws IllegalArgumentException
     *             if the given value is smaller than zero
     */
    public static int requireNonNegative(int value)
            throws IllegalArgumentException {
        if (value >= 0) {
            return value;
        } else {
            throw new IllegalArgumentException(
                    "The given value is smaller than zero. (" + value + ")");
        }
    }

}
