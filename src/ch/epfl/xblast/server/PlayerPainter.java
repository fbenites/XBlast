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
    private final static byte MULTIPLIER = 20;
    private final static byte WHITE = 80;

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
                return (byte) (0b1100 + (p.id().ordinal() * MULTIPLIER));
            } else if (p.lives() == 1) {
                // DYING, last live, skeleton
                return (byte) (0b1101 + (p.id().ordinal() * MULTIPLIER));
            } else {
                // DEAD, out of game, invalid number
                return (byte) (0b1111 + (p.id().ordinal() * MULTIPLIER));
            }
        } else {
            // direction of the player
            byte b = (byte) (p.direction().ordinal() * 3);
            // number of picture in this direction
            if (p.direction().isHorizontal()) {
                b += (p.position().x() % 4 == 3) ? 2 : p.position().x() % 2;
            } else {
                b += (p.position().y() % 4 == 3) ? 2 : p.position().y() % 2;
            }
            // white images for invulnerable player at odd tick count
            if (p.lifeState().state() == State.INVULNERABLE && tick % 2 == 1) {
                b += WHITE;
            } else {
                // normal player images
                b += p.id().ordinal() * MULTIPLIER;
            }
            return b;
        }
    }
}