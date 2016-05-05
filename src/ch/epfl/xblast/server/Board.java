package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Lists;

/**
 * Class representing a Board which XBlast is played on.
 * 
 * @author Lorenz Rasch (249937)
 */
public final class Board {
    private final List<Sq<Block>> blocks;

    /**
     * Creates a new Board from the list of blocks given.
     * 
     * @param blocks
     *            the list of blocks representing the board
     * @throws IllegalArgumentException
     *             if the list doesn't contain enough blocks
     */
    public Board(List<Sq<Block>> blocks) throws IllegalArgumentException {
        // checks if there are enough blocks in the given list
        if (blocks.size() != Cell.COUNT) {
            throw new IllegalArgumentException(
                    "Specified List of blocks does not contain " + Cell.COUNT
                            + " entries. (" + blocks.size() + ")");
        } else {
            this.blocks = Collections
                    .unmodifiableList(new ArrayList<Sq<Block>>(blocks));
        }
    }

    /**
     * Gives the sequence of blocks at a given point/cell on the board.
     * 
     * @param c
     *            the cell/point of the desired sequence of blocks
     * @return sequence of blocks at the given point/cell
     */
    public Sq<Block> blocksAt(Cell c) {
        return this.blocks.get(c.rowMajorIndex());
    }

    /**
     * Gives the current block at a given point/cell on the board.
     * 
     * @param c
     *            the cell/point of the desired block
     * @return block at the given point/cell
     */
    public Block blockAt(Cell c) {
        return this.blocksAt(c).head();
    }

    /**
     * Creates a new Board with the given list of (constant) blocks.
     * 
     * @param rows
     *            list of blocks given
     * @return a new Board with the given blocks
     * @throws IllegalArgumentException
     *             if the given list doesn't contain the right amount of
     *             rows/columns
     * @throws NullPointerException
     *             if the given list is null
     */
    public static Board ofRows(List<List<Block>> rows)
            throws IllegalArgumentException, NullPointerException {
        // checking correctness of given list
        checkBlockMatrix(rows, Cell.ROWS, Cell.COLUMNS);

        // build new list of constant sequences of every block
        List<Sq<Block>> construct = new ArrayList<Sq<Block>>(Cell.COUNT);
        for (List<Block> row : rows) {
            for (Block cell : row) {
                construct.add(Sq.constant(cell));
            }
        }
        return new Board(construct);
    }

    /**
     * Creates a new Board with the given list of (constant) blocks and a ring
     * of indestructible walls around.
     * 
     * @param innerBlocks
     *            given list of blocks representing the inner Board
     * @return a new Board with the given blocks
     * @throws IllegalArgumentException
     *             if the given list doesn't contain the right amount of
     *             rows/columns
     * @throws NullPointerException
     *             if the given list is null
     */
    public static Board ofInnerBlocksWalled(List<List<Block>> innerBlocks)
            throws IllegalArgumentException, NullPointerException {
        // checking correctness of given list
        checkBlockMatrix(innerBlocks, Cell.ROWS - 2, Cell.COLUMNS - 2);

        // creating list for first/last row of board
        List<Block> outerWall = Collections.nCopies(Cell.COLUMNS,
                Block.INDESTRUCTIBLE_WALL);

        // building the new board
        List<List<Block>> rows = new ArrayList<List<Block>>(Cell.ROWS);

        // adding first row of walls
        rows.add(outerWall);

        // construct inner-rows and add them to list of rows
        List<Block> innerRow;
        for (List<Block> row : innerBlocks) {
            innerRow = new ArrayList<Block>(Cell.COLUMNS);
            // add left-most wall to this row
            innerRow.add(Block.INDESTRUCTIBLE_WALL);
            for (Block b : row) {
                innerRow.add(b);
            }
            // add right-most wall to this row
            innerRow.add(Block.INDESTRUCTIBLE_WALL);
            // add this row to list of rows
            rows.add(innerRow);
        }

        // adding last row of walls
        rows.add(outerWall);

        // return Board with constructed list of rows
        return ofRows(rows);
    }

    /**
     * Creates a new Board with the given list of (constant) blocks representing
     * the NW quadrant of the Board. Given blocks are mirrored as necessary and
     * a ring of indestructible wall is added around.
     * 
     * @param quadrantNWBlocks
     *            given list of blocks representing the NW quadrant of the Board
     * @return a new Board with the given list of blocks
     * @throws IllegalArgumentException
     *             if the given list doesn't contain the right amount of
     *             rows/columns
     * @throws NullPointerException
     *             if the given list is null
     */
    public static Board ofQuadrantNWBlocksWalled(
            List<List<Block>> quadrantNWBlocks)
            throws IllegalArgumentException, NullPointerException {
        // checking correctness of given list
        checkBlockMatrix(quadrantNWBlocks, (Cell.ROWS / 2), (Cell.COLUMNS / 2));

        // building new Board from given list
        List<List<Block>> innerBlocks = new ArrayList<List<Block>>(
                Cell.ROWS - 2);

        // mirror every row in given list, gives upper-half of Board
        for (List<Block> row : quadrantNWBlocks) {
            innerBlocks.add(Lists.mirrored(row));
        }
        // mirror upper-half of Board, return Board with outer wall, represented
        // by newly built Board
        return ofInnerBlocksWalled(Lists.mirrored(innerBlocks));
    }

    /**
     * Checks if the given list of blocks contains the desired number of
     * rows/columns
     * 
     * @param matrix
     *            the list of blocks to check
     * @param rows
     *            the desired number of rows
     * @param columns
     *            the desired number of columns
     * @throws IllegalArgumentException
     *             if the given list doesn't contain the right amount of
     *             rows/columns
     * @throws NullPointerException
     *             if the given list is null
     */
    private static void checkBlockMatrix(List<List<Block>> matrix, int rows,
            int columns) throws IllegalArgumentException, NullPointerException {
        // check number of rows
        if (Objects.requireNonNull(matrix).size() != rows) {
            throw new IllegalArgumentException(
                    "Specified list of rows does not contain " + rows
                            + " entries. (" + matrix.size() + ")");
        } else {
            int count = 0;
            // check number of columns in every row
            for (List<Block> row : matrix) {
                if (row.size() != columns) {
                    throw new IllegalArgumentException("Row " + count
                            + " does not contain " + columns + " entries. ("
                            + row.size() + ")");
                }
                count++;
            }
        }
    }

}
