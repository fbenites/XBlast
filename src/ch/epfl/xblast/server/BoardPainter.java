package ch.epfl.xblast.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;

/**
 * Class tasked with converting a board into its byte representation. (byte
 * representation means number of image)
 * 
 * @author Lorenz Rasch (249937)
 */
public final class BoardPainter {

    private final Map<Block, BlockImage> palete;
    private final BlockImage shadedBlock;

    /**
     * Gives a new BoardPainter with the parameters given.
     * 
     * @param palete
     *            a mapping of blocks and their image
     * @param shadedBlock
     *            the image for a shaded (free) block
     * @throws NullPointerException
     *             if any of the parameters is null
     * @throws IllegalArgumentException
     *             if the provided palete/mapping is empty
     */
    public BoardPainter(Map<Block, BlockImage> palete, BlockImage shadedBlock) {
        if (Objects.requireNonNull(palete).isEmpty()) {
            throw new IllegalArgumentException("Provided palete is empty.");
        } else {
            this.palete = Collections
                    .unmodifiableMap(new HashMap<Block, BlockImage>(palete));
        }
        this.shadedBlock = Objects.requireNonNull(shadedBlock);
    }

    /**
     * Gives the byte representation of a cell/block on a given board.
     * 
     * @param b
     *            the board
     * @param c
     *            the cell
     * @return a byte number representing the block at the cell
     */
    public byte byteForCell(Board b, Cell c) {
        if (b.blockAt(c).isFree()
                && b.blockAt(c.neighbor(Direction.W)).castsShadow()) {
            /*
             * for free blocks in x = 0: 1) block in x = 15 not wall, player
             * could pass over the edges of the board, no shadow 2) block in x =
             * 15 is wall, player can not pass over the edges of the board,
             * shadow on x = 0
             */
            return (byte) shadedBlock.ordinal();
        } else {
            return (byte) palete.get(b.blockAt(c)).ordinal();
        }
    }

}
