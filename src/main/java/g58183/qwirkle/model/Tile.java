package g58183.qwirkle.model;

import java.util.Objects;

/**
 *
 */
public final class Tile {
    private final Color color;
    private final Shape shape;

    /**
     * @param color color of the tile
     * @param shape
     */
    public Tile(Color color, Shape shape) {
        this.color = color;
        this.shape = shape;
    }

    public Color color() {
        return color;
    }

    public Shape shape() {
        return shape;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Tile) obj;
        return Objects.equals(this.color, that.color) &&
                Objects.equals(this.shape, that.shape);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, shape);
    }

    @Override
    public String toString() {
        return "Tile[" +
                "color=" + color + ", " +
                "shape=" + shape + ']';
    }
}


