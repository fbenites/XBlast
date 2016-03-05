package ch.epfl.xblast;

/**
 * A Direction on a 2-dimensional grid.
 * 
 * @author Lorenz Rasch (249937)
 */
public enum Direction {

    N, E, S, W;

    /**
     * Returns the Direction opposite to this Direction.
     * 
     * @return the Direction opposite
     */
    public Direction opposite() {
        switch (this) {
        case N:
            return S;
        case E:
            return W;
        case S:
            return N;
        case W:
            return E;
        default:
            return null;
        }
    }

    /**
     * Returns true if this Direction is horizontal on a 2d grid, i.e. E or W.
     * 
     * @return true if this Direction is horizontal
     */
    public boolean isHorizontal() {
        return this == E || this == W;
    }

    /**
     * Returns true if this Direction is parallel to the Direction given, i.e.
     * the same or opposite.
     * 
     * @param that
     *            the Direction given
     * @return true if this Direction is parallel to the Direction given
     */
    public boolean isParallelTo(Direction that) {
        return this == that || this == that.opposite();
    }

}
