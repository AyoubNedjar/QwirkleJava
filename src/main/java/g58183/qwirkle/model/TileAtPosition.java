package g58183.qwirkle.model;

import java.io.Serializable;
import java.util.Objects;

public final class TileAtPosition implements Serializable {
    private final int row;
    private final int col;
    private final Tile tile;

    public TileAtPosition(int row, int col, Tile tile) {
        this.row = row;
        this.col = col;
        this.tile = tile;
    }

    public int row() {
        return row;
    }

    public int col() {
        return col;
    }

    public Tile tile() {
        return tile;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (TileAtPosition) obj;
        return this.row == that.row &&
                this.col == that.col &&
                Objects.equals(this.tile, that.tile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col, tile);
    }

    @Override
    public String toString() {
        return "TileAtPosition[" +
                "row=" + row + ", " +
                "col=" + col + ", " +
                "tile=" + tile + ']';
    }
}
