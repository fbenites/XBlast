package ch.epfl.xblast.server;

//TODO comments

import ch.epfl.xblast.Time;

public interface Ticks {
    public static int PLAYER_DYING_TICKS = 8;
    public static int PLAYER_INVULNERABLE_TICKS = 64;
    public static int BOMB_FUSE_TICKS = 100;
    public static int EXPLOSION_TICKS = 30;
    public static int WALL_CRUMBLING_TICKS = 30;
    public static int BONUS_DISAPPEARING_TICKS = 30;
    
    public static int TICKS_PER_SECOND = 20;
    public static int TICK_NANOSECOND_DURATION = Time.NS_PER_S / TICKS_PER_SECOND;
    //FIXME game time hardcoded
    public static int TOTAL_TICKS = TICKS_PER_SECOND * Time.S_PER_MIN * 2;
}
