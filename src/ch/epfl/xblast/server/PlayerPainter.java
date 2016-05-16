package ch.epfl.xblast.server;

import ch.epfl.xblast.server.Player.LifeState.State;

/**
 * Class tasked with converting a player into its byte representation. (byte
 * representation means number of image)
 * 
 * @author Lorenz Rasch (249937)
 */
public final class PlayerPainter {

    // constants for calculations
    private final static byte PLAYER_MULTIPLIER = 20;
    private final static byte DIRECTION_MULTIPLIER = 3;
    private final static int POSITION_DIVIDER = 4;
    private final static byte WHITE = 80;

    // constants for pictures
    private final static byte DYING = 0b1100;
    private final static byte SKELETON = 0b1101;
    private final static byte DEAD = 0b1111;

    private PlayerPainter() {
    }

    /**
     * Gives the byte representation of a player. Depending on player id,
     * direction, state and tick.
     * 
     * @param tick
     *            the tick/time
     * @param p
     *            the player
     * @return the byte number representing the player
     */
    public static byte byteForPlayer(int tick, Player p) {
        if (!p.lifeState().canMove()) {
            if (p.lives() > 1) {
                // DYING, > 1 lives left
                return (byte) (DYING + (p.id().ordinal() * PLAYER_MULTIPLIER));
            } else if (p.lives() == 1) {
                // DYING, last live, skeleton
                return (byte) (SKELETON + (p.id().ordinal() * PLAYER_MULTIPLIER));
            } else {
                // DEAD, out of game, invalid number
                return DEAD;
            }
        } else {
            // direction of the player
            byte b = (byte) (p.direction().ordinal() * DIRECTION_MULTIPLIER);
            // number of picture in this direction
            if (p.direction().isHorizontal()) {
                b += (p.position().x() % POSITION_DIVIDER == 3) ? 2 : p
                        .position().x() % 2;
            } else {
                b += (p.position().y() % POSITION_DIVIDER == 3) ? 2 : p
                        .position().y() % 2;
            }
            // white images for invulnerable player at odd tick count
            if (p.lifeState().state() == State.INVULNERABLE && tick % 2 == 1) {
                b += WHITE;
            } else {
                // normal player images
                b += p.id().ordinal() * PLAYER_MULTIPLIER;
            }
            return b;
        }
    }
}