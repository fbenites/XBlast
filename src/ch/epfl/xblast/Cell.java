package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A Cell on a 2-dimensional grid.
 * 
 * @author Lorenz Rasch (249937)
 */
public final class Cell {

    /**
     * constants concerning the 2d grid of Cells
     */
    public static int COLUMNS = 15;
    public static int ROWS = 13;
    public static int COUNT = COLUMNS * ROWS;

    /**
     * constant lists containing the grids Cells in row-major and spiral order
     */
    public static List<Cell> ROW_MAJOR_ORDER = Collections
            .unmodifiableList(rowMajorOrder());
    public static List<Cell> SPIRAL_ORDER = Collections
            .unmodifiableList(spiralOrder());

    private final int x, y;

    /**
     * Constructs a new Cell on the 2-dimensional grid with the given
     * coordinates.
     * 
     * @param x
     *            the x coordinate
     * @param y
     *            the y coordinate
     */
    public Cell(int x, int y) {
        this.x = Math.floorMod(x, COLUMNS);
        this.y = Math.floorMod(y, ROWS);
    }

    /**
     * Returns the x component of the Cells coordinates.
     * 
     * @return the x component of the Cells coordinates
     */
    public int x() {
        return this.x;
    }

    /**
     * Returns the y component of the Cells coordinates.
     * 
     * @return the y component of the Cells coordinates
     */
    public int y() {
        return this.y;
    }

    /**
     * Returns the number/index of the Cell in row-major order.
     * 
     * @return the number/index of the Cell in row-major order
     */
    public int rowMajorIndex() {
        return ROW_MAJOR_ORDER.indexOf(this);
    }

    /**
     * Returns the neighbor of the Cell in a given Direction.
     * 
     * @param dir
     *            the Direction
     * @return the neighbor in the given Direction
     */
    public Cell neighbor(Direction dir) {
        int newx = this.x();
        int newy = this.y();
        switch (dir) {
        case N:
            newy = Math.floorMod(newy - 1, ROWS);
            break;
        case E:
            newx = Math.floorMod(newx + 1, COLUMNS);
            break;
        case S:
            newy = Math.floorMod(newy + 1, ROWS);
            break;
        case W:
            newx = Math.floorMod(newx - 1, COLUMNS);
            break;
        }
        return new Cell(newx, newy);
    }

    @Override
    public boolean equals(Object that) {
        if (!(that instanceof Cell)) {
            return false;
        } else {
            Cell second = (Cell) that;
            return (this.x() == second.x() && this.y() == second.y());
        }
    }

    @Override
    public int hashCode() {
        return this.rowMajorIndex();
    }

    @Override
    public String toString() {
        return "(" + this.x() + "," + this.y() + ")";
    }

    /**
     * Constructs the list containing the grids Cells in row-major order.
     * 
     * @return the list containing the grids Cells in row-major order
     */
    private static ArrayList<Cell> rowMajorOrder() {
        ArrayList<Cell> list = new ArrayList<Cell>();
        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLUMNS; x++) {
                list.add(new Cell(x, y));
            }
        }
        return list;
    }

    /**
     * Constructs the list containing the grids Cells in spiral order.
     * 
     * @return the list containing the grids Cells in spiral order
     */
    private static ArrayList<Cell> spiralOrder() {
        /*
         * Algorithm to add cells in spiral order: Add top row, then rotate grid
         * against clock until all cells are added.
         */
        List<Integer> ix = new ArrayList<Integer>();
        for (int i = 0; i < COLUMNS; i++) {
            ix.add(i);
        }
        List<Integer> iy = new ArrayList<Integer>();
        for (int i = 0; i < ROWS; i++) {
            iy.add(i);
        }
        boolean horizontal = true;
        ArrayList<Cell> spiral = new ArrayList<Cell>();
        List<Integer> i1, i2;
        while (!iy.isEmpty() && !ix.isEmpty()) {
            if (horizontal) {
                i1 = ix;
                i2 = iy;
            } else {
                i1 = iy;
                i2 = ix;
            }
            int c2 = i2.remove(0);
            for (int c1 : i1) {
                if (horizontal) {
                    spiral.add(new Cell(c1, c2));
                } else {
                    spiral.add(new Cell(c2, c1));
                }
            }
            Collections.reverse(i1);
            horizontal = !horizontal;
        }
        return spiral;
    }

}