package ch.epfl.xblast.server;

import ch.epfl.xblast.Time;

/**
 * Interface containing constants representing durations in the game in ticks.
 * 
 * @author Lorenz Rasch (249937)
 */
public interface Ticks {
    /**
     * Duration of Player in DYING state.
     */
    public static final int PLAYER_DYING_TICKS = 8;
    /**
     * Duration of Player in INVULNERABLE state.
     */
    public static final int PLAYER_INVULNERABLE_TICKS = 64;
    /**
     * Duration of Bomb fuses.
     */
    public static final int BOMB_FUSE_TICKS = 100;
    /**
     * Duration of Explosions.
     */
    public static final int EXPLOSION_TICKS = 30;
    /**
     * Duration of a Wall crumbling.
     */
    public static final int WALL_CRUMBLING_TICKS = 30;
    /**
     * Duration of a Bonus disappearing.
     */
    public static final int BONUS_DISAPPEARING_TICKS = 30;

    /**
     * Number of Ticks per second.
     */
    public static final int TICKS_PER_SECOND = 20;
    /**
     * Tick duration in nanoseconds
     */
    public static final int TICK_NANOSECOND_DURATION = Time.NS_PER_S
            / TICKS_PER_SECOND;
    /**
     * Total Ticks of a Game (of 2 Minutes).
     */
    public static final int TOTAL_TICKS = TICKS_PER_SECOND * Time.S_PER_MIN * 2;
}
