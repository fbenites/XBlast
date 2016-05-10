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
     * @param nesw
     *            boolean specifying if there is another blast on any of the
     *            neighboring cells
     * @return the byte number representing the blast
     */
    public static byte byteForBlast(boolean[] nesw) {
        byte b = 0;
        for (boolean x : nesw) {
            b = (byte) (b << 1);
            if (x) {
                b += 1;
            }
        }
        return b;
    }

}
