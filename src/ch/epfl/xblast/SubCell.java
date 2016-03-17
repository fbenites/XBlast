package ch.epfl.xblast;

/**
 * A SubCell on a 2-dimensional grid. A Cell contains 16*16 SubCells.
 * 
 * @author Lorenz Rasch (249937)
 */
public final class SubCell {

    private final int x, y;

    /**
     * Constants concerning the grid of SubCells. The "center"-SubCell is the
     * SubCell with coordinates x, y = half the SubCell-Count
     */
    private static int SUBCELL_COUNT = 16;
    private static int COLUMNS = Cell.COLUMNS * SUBCELL_COUNT;
    private static int ROWS = Cell.ROWS * SUBCELL_COUNT;

    /**
     * Returns the central SubCell of a given cell.
     * 
     * @param cell
     *            the cell given
     * @return the central SubCell of the given cell
     */
    public static SubCell centralSubCellOf(Cell cell) {
        return new SubCell((cell.x() * SUBCELL_COUNT) + SUBCELL_COUNT / 2,
                (cell.y() * SUBCELL_COUNT) + SUBCELL_COUNT / 2);
    }

    /**
     * Constructs a new SubCell on the 2-dimensional grid with the given
     * coordinates.
     * 
     * @param x
     *            the x coordinate
     * @param y
     *            the y coordinate
     */
    public SubCell(int x, int y) {
        this.x = Math.floorMod(x, COLUMNS);
        this.y = Math.floorMod(y, ROWS);
    }

    /**
     * Returns the x component of the SubCells coordinates.
     * 
     * @return the x component of the SubCells coordinates
     */
    public int x() {
        return this.x;
    }

    /**
     * Returns the y component of the SubCells coordinates.
     * 
     * @return the y component of the SubCells coordinates
     */
    public int y() {
        return this.y;
    }

    /**
     * Returns the distance from this SubCell to the closest "center"-SubCell
     * 
     * @return
     */
    public int distanceToCentral() {
        SubCell central = centralSubCellOf(this.containingCell());
        return Math.abs(this.x - central.x) + Math.abs(this.y - central.y);
    }

    /**
     * Returns true, if this SubCell is a "center"-SubCell.
     * 
     * @return true if this SubCell is a "center"-SubCell
     */
    public boolean isCentral() {
        return (this.distanceToCentral() == 0);
    }

    /**
     * Returns the neighbor of the SubCell in a given Direction.
     * 
     * @param dir
     *            the Direction
     * @return the neighbor in the given Direction
     */
    public SubCell neighbor(Direction dir) {
        int newx = this.x;
        int newy = this.y;
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
        return new SubCell(newx, newy);
    }

    /**
     * Returns the Cell containing this SubCell.
     * 
     * @return the Cell containing this SubCell
     */
    public Cell containingCell() {
        return new Cell(this.x / SUBCELL_COUNT, this.y / SUBCELL_COUNT);
    }

    @Override
    public boolean equals(Object that) {
        if (!(that instanceof SubCell)) {
            return false;
        } else {
            SubCell second = (SubCell) that;
            return (this.x == second.x && this.y == second.y);
        }
    }

    @Override
    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }
}
