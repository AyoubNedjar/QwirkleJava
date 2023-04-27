package g58183.qwirkle.model;

/**
 * An enum representing the four cardinal directions in a two-dimensional grid:
 * left, right, up, and down.
 */
public enum Direction {

    LEFT(0, -1),

    RIGHT(0, 1),

    UP(-1, 0),


    DOWN(1, 0);

    private final int deltaRow;
    private final int deltaColumn;

    /**
     * Constructs a direction with the specified row and column deltas.
     *
     * @param deltaRow the change in row position
     * @param deltaColumn the change in column position
     */
    Direction(int deltaRow, int deltaColumn) {
        this.deltaRow = deltaRow;
        this.deltaColumn = deltaColumn;
    }

    /**
     * Returns the change in row position associated with this direction.
     *
     * @return the change in row position
     */
    public int getDeltaRow() {
        return deltaRow;
    }

    /**
     * Returns the change in column position associated with this direction.
     *
     * @return the change in column position
     */
    public int getDeltaColumn() {
        return deltaColumn;
    }

    /**
     * Returns the opposite direction to this one. For example, the opposite of
     * {@code UP} is {@code DOWN}.
     *
     * @return the opposite direction
     */
    public Direction opposite(){
        switch (this){
            case DOWN : return UP;
            case LEFT:  return RIGHT;
            case UP: return DOWN;
            case RIGHT: return LEFT;
        }
        return null;
    }
}
