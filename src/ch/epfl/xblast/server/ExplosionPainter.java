package ch.epfl.xblast.server;

/**
 * Class tasked with converting bombs and blasts into their byte representation.
 * (byte representation means number of image)
 * 
 * @author Lorenz Rasch (249937)
 */
public final class ExplosionPainter {

    // constants for bombs
    private static final byte BLACK_BOMB = 20;
    private static final byte WHITE_BOMB = 21;

    // constants for blasts
    private static final byte N = 0b1000;
    private static final byte E = 0b0100;
    private static final byte S = 0b0010;
    private static final byte W = 0b0001;

    /**
     * Constant for "no" blast/cell without blast
     */
    public static final byte BYTE_FOR_EMPTY = 16;

    private ExplosionPainter() {
    }

    /**
     * Gives the byte representation of a bomb. A white bomb, if the fuse has a
     * length of 2^n, a black bomb otherwise.
     * 
     * @param b
     *            the bomb
     * @return the byte number representing the bomb
     */
    public static byte byteForBomb(Bomb b) {
        if (Integer.bitCount(b.fuseLength()) == 1) {
            return WHITE_BOMB;
        } else {
            return BLACK_BOMB;
        }
    }

    /**
     * Gives the byte representation of a blast particle. Parameters signal
     * another blast on the adjacent cell.
     * 
     * @param n
     *            for a blast on the northern neighbor
     * @param e
     *            for a blast on the eastern neighbor
     * @param s
     *            for a blast on the southern neighbor
     * @param w
     *            for a blast on the western neighbor
     * @return the byte number representing the blast
     */
    public static byte byteForBlast(boolean n, boolean e, boolean s, boolean w) {
        byte b = 0b0000;
        if (n) {
            b += N;
        }
        if (e) {
            b += E;
        }
        if (s) {
            b += S;
        }
        if (w) {
            b += W;
        }
        return b;
    }

}
