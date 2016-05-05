package ch.epfl.xblast;

/**
 * Interface containing constants about Time.
 * 
 * @author Lorenz Rasch (249937)
 */
public interface Time {
    /**
     * Number of seconds per minute.
     */
    public static final int S_PER_MIN = 60;
    /**
     * Number of milliseconds per second.
     */
    public static final int MS_PER_S = 1000;
    /**
     * Number of microseconds per second.
     */
    public static final int US_PER_S = 1000 * MS_PER_S;
    /**
     * Number of nanoseconds per second.
     */
    public static final int NS_PER_S = 1000 * US_PER_S;
}
