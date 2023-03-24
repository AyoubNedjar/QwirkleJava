package g58183.qwirkle.model;

/**
 * Utility constants and methods for testing Qwirkle.
 * <p>
 * In the game we require the center of the game to be at position (45, 45).
 * That's a little odd, because it feels more natural to use (0, 0) as the center.
 * <p>
 * The present class aims to circumvent this specific oddity, by factoring out the constants,
 * and providing methods to work with them.
 */
public class QwirkleTestUtils {
    /**
     * The initial row is mandated to be 45.
     */
    static final int INITIAL_ROW = 45;
    /**
     * The initial column is mandated to be 45.
     */
    static final int INITIAL_COLUMN = 45;

    /**
     * Get a tile at a given 0-based position.
     * <p>
     * A 0-based position means a position where (0, 0) denotes the center ;
     * whereas the model accepts a position where (INITIAL_ROW, INITIAL_COLUMN) is the actual center.
     *
     * @param grid The grid to get the tile from.
     * @param row  a row number, with 0 being the center row.
     * @param col  a column number, with 0 being the center column
     * @return the tile at the given position
     */
    static Tile get(Grid grid, int row, int col) {
        return grid.get(INITIAL_ROW + row, INITIAL_COLUMN + col);
    }

    /**
     * Add a given tile at a given 0-based position.
     * <p>
     * A 0-based position means a position where (0, 0) denotes the center ;
     * whereas the model accepts a position where (INITIAL_ROW, INITIAL_COLUMN) is the actual center.
     *
     * @param grid The grid where to add the tile to.
     * @param row  a row number, with 0 being the center row.
     * @param col  a column number, with 0 being the center column.
     * @param tile a tile to add.
     */
    static void add(Grid grid, int row, int col, Tile tile) {
        grid.add(INITIAL_ROW + row, INITIAL_COLUMN + col, tile);
    }


    /**
     * Add tiles at a given 0-based position
     * <p>
     * A 0-based position means a position where (0, 0) denotes the center ;
     * whereas the model accepts a position where (INITIAL_ROW, INITIAL_COLUMN) is the actual center.
     *
     * @param grid The grid where to add the tile to.
     * @param row  a row number, with 0 being the center row.
     * @param col  a column number, with 0 being the center column.
     * @param d    a direction
     * @param line tiles to add.
     */
    static void add(Grid grid, int row, int col, Direction d, Tile... line) {
        grid.add(INITIAL_ROW + row, INITIAL_COLUMN + col, d, line);
    }


    /**
     * Create a TileAtPosition object using a 0-based position.
     * <p>
     * A 0-based position means a position where (0, 0) denotes the center ;
     * whereas the model accepts a position where (INITIAL_ROW, INITIAL_COLUMN) is the actual center.
     *
     * @param row  a row number, with 0 being the center row.
     * @param col  a column number, with 0 being the center column.
     * @param tile a tile to insert into the object.
     */
    static TileAtPosition createTileAtpos(int row, int col, Tile tile) {
        return new TileAtPosition(INITIAL_ROW + row, INITIAL_COLUMN + col, tile);
    }
}

