package g58183.qwirkle.model;

import g58183.qwirkle.model.Grid;
import g58183.qwirkle.view.View;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static g58183.qwirkle.model.Color.*;
import static g58183.qwirkle.model.Direction.*;
import static g58183.qwirkle.model.QwirkleTestUtils.*;
import static g58183.qwirkle.model.Shape.*;
import static org.junit.jupiter.api.Assertions.*;

public class NRIGridTest {

    public static final int INITIAL_ROW = 45;
    public static final int INITIAL_COL = 45;

    @Test
    void gridInitiallyEmpty() {
        var g = new Grid();
        for (int row = -45; row < 45; row++) {
            for (int col = -45; col < 45; col++) {
                assertNull(get(g, row, col));
            }
        }
    }

    @Test
    @DisplayName("get outside the grid should return null, not throw")
    void canGetOutsideVirtualGrid() {
        var g = new Grid();
        assertDoesNotThrow(() -> get(g, -250, 500));
        assertNull(get(g, -250, 500));
    }

    // simple adds

    @Test
    void addSimpleUP() {
        var g = new Grid();
        g.firstAdd(UP, TILE_RED_CROSS, TILE_RED_DIAMOND);
        assertSame(TILE_RED_CROSS, get(g, 0, 0));
        assertSame(TILE_RED_DIAMOND, get(g, -1, 0));
        assertNull(get(g, 1, 0));
        assertNull(get(g, 0, 1));
        assertNull(get(g, 0, -1));
    }

    @Test
    void addSimpleDOWN() {
        var g = new Grid();
        g.firstAdd(DOWN, TILE_RED_CROSS, TILE_RED_DIAMOND);
        assertSame(TILE_RED_CROSS, get(g, 0, 0));
        assertSame(TILE_RED_DIAMOND, get(g, 1, 0));
        assertNull(get(g, -1, 0));
        assertNull(get(g, 0, 1));
        assertNull(get(g, 0, -1));
    }

    @Test
    void addSimpleLEFT() {
        var g = new Grid();
        g.firstAdd(LEFT, TILE_RED_CROSS, TILE_RED_DIAMOND);
        assertSame(TILE_RED_CROSS, get(g, 0, 0));
        assertSame(TILE_RED_DIAMOND, get(g, 0, -1));
        assertNull(get(g, 1, 0));
        assertNull(get(g, -1, 0));
        assertNull(get(g, 0, 1));
    }

    @Test
    void addSimpleRIGHT() {
        var g = new Grid();
        g.firstAdd(RIGHT, TILE_RED_CROSS, TILE_RED_DIAMOND);
        assertSame(TILE_RED_CROSS, get(g, 0, 0));
        assertSame(TILE_RED_DIAMOND, get(g, 0, 1));
        assertNull(get(g, 1, 0));
        assertNull(get(g, -1, 0));
        assertNull(get(g, 0, -1));
    }

    @Test
    void addSimpleDoubleShouldThrow() {
        var g = new Grid();
        for (Direction d : Direction.values()) {
            assertThrows(QwirkleException.class, () -> g.firstAdd(d, TILE_RED_CROSS, TILE_RED_CROSS));
            assertNull(get(g, 0, 0));
            assertNull(get(g, -1, 0));
            assertNull(get(g, 1, 0));
            assertNull(get(g, 0, -1));
            assertNull(get(g, 0, 1));
        }

    }

    // firstAdd must be called first

    @Test
    void addFirstCannotBeCalledTwice() {
        var g = new Grid();
        g.firstAdd(UP, TILE_RED_CROSS, TILE_RED_DIAMOND);
        assertThrows(QwirkleException.class, () -> g.firstAdd(DOWN, TILE_RED_CROSS, TILE_RED_DIAMOND));
    }

    @Test
    void addFirstMustBeCalledFirst_dir() {
        var g = new Grid();
        assertThrows(QwirkleException.class, () -> add(g, 0, 0, DOWN, TILE_RED_CROSS, TILE_RED_DIAMOND));
    }

    @Test
    void addFirstMustBeCalledFirst_tap() {
        var g = new Grid();
        assertThrows(QwirkleException.class, () -> g.add(createTileAtpos(0, 0, TILE_RED_CROSS)));
    }

    @Test
    void addFirstMustBeCalledFirst_simple() {
        var g = new Grid();
        assertThrows(QwirkleException.class, () -> add(g, 0, 0, TILE_RED_CROSS));
    }

    // neighbours

    @Test
    void aTileMustHaveNeighbours() {
        var g = new Grid();
        g.firstAdd(UP, TILE_RED_CROSS);
        assertThrows(QwirkleException.class, () -> add(g, 2, 0, TILE_RED_DIAMOND));
        assertNull(get(g, 2, 0));
    }


    // overwriting

    @Test
    void canNotAddTwiceAtTheSamePlace_equalTile() {
        var g = new Grid();
        g.firstAdd(DOWN, TILE_RED_CROSS, TILE_RED_DIAMOND);
        assertThrows(QwirkleException.class, () -> add(g, 1, 0, TILE_RED_DIAMOND_2));
        assertSame(get(g, 1, 0), TILE_RED_DIAMOND);
    }

    @Test
    void canNotAddTwiceAtTheSamePlace_differentTile_simple() {
        var g = new Grid();
        g.firstAdd(DOWN, TILE_RED_CROSS, TILE_RED_DIAMOND);
        assertThrows(QwirkleException.class, () -> add(g, 1, 0, TILE_RED_PLUS));
        assertSame(get(g, 1, 0), TILE_RED_DIAMOND);
    }

    @Test
    void canNotAddTwiceAtTheSamePlace_differentTile_dir() {
        var g = new Grid();
        g.firstAdd(DOWN, TILE_RED_CROSS, TILE_RED_DIAMOND);
        assertThrows(QwirkleException.class, () -> add(g, 0, 0, DOWN, TILE_RED_PLUS, TILE_RED_STAR));
        assertSame(get(g, 0, 0), TILE_RED_CROSS);
        assertSame(get(g, 1, 0), TILE_RED_DIAMOND);
    }

    @Test
    void canNotAddTwiceAtTheSamePlace_differentTile_taps() {
        var g = new Grid();
        g.firstAdd(DOWN, TILE_RED_CROSS, TILE_RED_DIAMOND);
        TileAtPosition tap1 = createTileAtpos(0, 0, TILE_RED_PLUS);
        TileAtPosition tap2 = createTileAtpos(1, 0, TILE_RED_STAR);
        assertThrows(QwirkleException.class, () -> g.add(tap1, tap2));
        assertSame(TILE_RED_CROSS, get(g, 0, 0));
        assertSame(TILE_RED_DIAMOND, get(g, 1, 0));
    }


    // alignment
    @Test
    void canNotAddInDifferentLines() {
        var g = new Grid();
        g.firstAdd(UP, TILE_RED_CROSS);
        var tap1 = createTileAtpos(0, 1, TILE_RED_DIAMOND);
        var tap2 = createTileAtpos(1, 0, TILE_RED_STAR);
        assertThrows(QwirkleException.class, () -> g.add(tap1, tap2));
        assertSame(TILE_RED_CROSS, get(g, 0, 0));
        assertNull(get(g, 0, 1));
        assertNull(get(g, 1, 0));
    }




    // must share common trait
    @Test
    void canNotAddIfNoCommonTrait_tap() {
        var g = new Grid();
        g.firstAdd(UP, TILE_RED_CROSS);
        var tap1 = createTileAtpos(0, 1, TILE_YELLOW_DIAMOND);
        assertThrows(QwirkleException.class, () -> g.add(tap1));
        assertSame(TILE_RED_CROSS, get(g, 0, 0));
        assertNull(get(g, 0, 1));
        assertNull(get(g, 1, 0));
    }

    @Test
    void canNotAddIfNoCommonTrait_simple() {
        var g = new Grid();
        g.firstAdd(UP, TILE_RED_CROSS);
        assertThrows(QwirkleException.class, () -> add(g, 0, 1, TILE_YELLOW_DIAMOND));
        assertSame(TILE_RED_CROSS, get(g, 0, 0));
        assertNull(get(g, 0, 1));
        assertNull(get(g, 1, 0));
    }

    @Test
    void canNotAddIfNoCommonTrait_dir() {
        var g = new Grid();
        g.firstAdd(UP, TILE_RED_CROSS);
        assertThrows(QwirkleException.class, () -> add(g, 0, 1, LEFT, TILE_YELLOW_DIAMOND));
        assertSame(TILE_RED_CROSS, get(g, 0, 0));
        assertNull(get(g, 0, 1));
        assertNull(get(g, 1, 0));
    }

    @Test
    void canNotCompleteToALineWithDifferentTraits_simple() {
        var g = new Grid();
        g.firstAdd(RIGHT, TILE_RED_CROSS, TILE_RED_STAR, TILE_RED_DIAMOND);

        add(g, 1, 0, TILE_RED_DIAMOND_2);
        add(g, 2, 0, TILE_RED_PLUS);

        add(g, 1, 2, TILE_GREEN_DIAMOND);
        add(g, 2, 2, TILE_YELLOW_DIAMOND);

        // the "hole" in 2, 1 can never be filled because 2, 0 and 2, 2 share no trait
        for (var color : Color.values()) {
            for (var shape : Shape.values()) {
                assertThrows(QwirkleException.class, () -> add(g, 2, 1, new Tile(color, shape)));
                assertNull(get(g, 2, 1));
            }
        }
    }

    @Test
    void canNotCompleteToALineWithDifferentTraits_dir() {
        var g = new Grid();
        var gr = new GridView(g);
        g.firstAdd(RIGHT, TILE_RED_CROSS, TILE_RED_STAR, TILE_RED_DIAMOND);

        add(g, 1, 0, TILE_RED_DIAMOND_2);
        add(g, 2, 0, TILE_RED_PLUS);

        add(g, 1, 2, TILE_GREEN_DIAMOND);
        add(g, 2, 2, TILE_YELLOW_DIAMOND);

        //add(g, 2, 1, UP, new Tile(RED, DIAMOND));
        View.display(gr);
        // the "hole" in 2, 1 can never be filled because 2, 0 and 2, 2 share no trait
      for (var color : Color.values()) {
         for (var shape : Shape.values()) {
             for (Direction dir : Direction.values()) {

                 assertThrows(QwirkleException.class, () -> add(g, 2, 1, dir, new Tile(color, shape)));

                 assertNull(get(g, 2, 1));
              }
            }

        }

    }

    @Test
    void canNotCompleteToALineWithDifferentTraits_tap() {
        var g = new Grid();
        g.firstAdd(RIGHT, TILE_RED_CROSS, TILE_RED_STAR, TILE_RED_DIAMOND);

        add(g, 1, 0, TILE_RED_DIAMOND_2);
        add(g, 2, 0, TILE_RED_PLUS);

        add(g, 1, 2, TILE_GREEN_DIAMOND);
        add(g, 2, 2, TILE_YELLOW_DIAMOND);

        // the "hole" in 2, 1 can never be filled because 2, 0 and 2, 2 share no trait
        for (var color : Color.values()) {
            for (var shape : Shape.values()) {
                assertThrows(QwirkleException.class, () -> g.add(createTileAtpos(2, 1, new Tile(color, shape))));
                assertNull(get(g, 2, 1));
            }
        }
    }

    // never identical
    @Test
    void canNotCompleteToALineWithIdenticalTiles_simple() {
        var g = new Grid();
        var gr = new GridView(g);
        g.firstAdd(RIGHT, TILE_RED_CROSS, TILE_RED_STAR, TILE_RED_DIAMOND);

        add(g, 1, 0, TILE_RED_SQUARE);
        add(g, 2, 0, TILE_RED_PLUS);

        add(g, 1, 2, TILE_RED_ROUND);
        add(g, 2, 2, TILE_RED_PLUS_2);
        View.display(gr);
        // the "hole" in 2, 1 can never be filled because 2, 0 and 2, 2 are identical
        for (var color : Color.values()) {
            for (var shape : Shape.values()) {
                 assertThrows(QwirkleException.class, () -> add(g, 2, 1, new Tile(color, shape)));
                assertNull(get(g, 2, 1));
            }
        }
    }

    @Test
    void canNotCompleteToALineWithIdenticalTiles_tap() {
        var g = new Grid();
        var gr = new GridView(g);
        g.firstAdd(RIGHT, TILE_RED_CROSS, TILE_RED_STAR, TILE_RED_DIAMOND);

        add(g, 1, 0, TILE_RED_SQUARE);
        add(g, 2, 0, TILE_RED_PLUS);

        add(g, 1, 2, TILE_RED_ROUND);
        add(g, 2, 2, TILE_RED_PLUS_2);
        View.display(gr);
        // the "hole" in 2, 1 can never be filled because 2, 0 and 2, 2 are identical
        for (var color : Color.values()) {
            for (var shape : Shape.values()) {
                assertThrows(QwirkleException.class, () -> g.add(createTileAtpos(2, 1, new Tile(color, shape))));
                assertNull(get(g, 2, 1));
            }
        }
    }

    @Test
    void canNotCompleteToALineWithIdenticalTiles_dir() {
        var g = new Grid();
        var gr = new GridView(g);
        g.firstAdd(RIGHT, TILE_RED_CROSS, TILE_RED_STAR, TILE_RED_DIAMOND);

        add(g, 1, 0, TILE_RED_SQUARE);
        add(g, 2, 0, TILE_RED_PLUS);

        add(g, 1, 2, TILE_RED_ROUND);
        add(g, 2, 2, TILE_RED_PLUS_2);

        View.display(gr);
        // the "hole" in 2, 1 can never be filled because 2, 0 and 2, 2 are identical
        for (var color : Color.values()) {
            for (var shape : Shape.values()) {
                // there is only one tile but let's try to add it in all directions anyway
                for (Direction direction : Direction.values()) {
                    assertThrows(QwirkleException.class, () -> add(g, 2, 1, direction, new Tile(color, shape)));
                    assertNull(get(g, 2, 1));
                }
            }
        }
    }

    // various other tests, pertaining to filling existing holes
    @Test
    void canCompleteToALineLeftRight() {
        var g = new Grid();
        g.firstAdd(RIGHT, TILE_RED_CROSS, TILE_RED_STAR, TILE_RED_DIAMOND);

        add(g, 1, 0, TILE_GREEN_CROSS);
        add(g, 2, 0, TILE_YELLOW_CROSS);

        add(g, 1, 2, TILE_GREEN_DIAMOND);
        add(g, 2, 2, TILE_YELLOW_DIAMOND);

        add(g, 2, 1, TILE_YELLOW_PLUS);
        assertSame(TILE_YELLOW_PLUS, get(g, 2, 1));

    }

    @Test
    void canCompleteToALineLeftRightUpDown() {
        var g = new Grid();
        g.firstAdd(RIGHT, TILE_RED_CROSS, TILE_RED_PLUS, TILE_RED_DIAMOND);

        add(g, 1, 0, TILE_GREEN_CROSS);
        add(g, 2, 0, TILE_YELLOW_CROSS);

        add(g, 1, 2, TILE_GREEN_DIAMOND);
        add(g, 2, 2, TILE_YELLOW_DIAMOND);

        add(g, 2, 1, TILE_YELLOW_PLUS);
        add(g, 1, 1, TILE_GREEN_PLUS);
        assertSame(TILE_GREEN_PLUS, get(g, 1, 1));
    }

    @Test
    @DisplayName("Complete a line leaving holes during intermediary steps")
    void canCompleteALine_Left_Middle_Right() {
        var g = new Grid();
        g.firstAdd(RIGHT, TILE_RED_CROSS, TILE_RED_PLUS, TILE_RED_DIAMOND);

        add(g, 1, 0, TILE_GREEN_CROSS);
        add(g, 2, 0, TILE_YELLOW_CROSS);

        add(g, 1, 2, TILE_GREEN_DIAMOND);
        add(g, 2, 2, TILE_YELLOW_DIAMOND);

        TileAtPosition plus_left = createTileAtpos(2, -1, TILE_YELLOW_PLUS);
        TileAtPosition round_center = createTileAtpos(2, 1, TILE_YELLOW_ROUND);
        TileAtPosition star_right = createTileAtpos(2, 3, TILE_YELLOW_STAR);
        assertDoesNotThrow(() -> {
            g.add(plus_left, star_right, round_center); // make sur having the center tile last does not throw.
        });
        assertAtCorrectPosition(g, plus_left);
        assertAtCorrectPosition(g, round_center);
        assertAtCorrectPosition(g, star_right);
    }

    @Test
    @DisplayName("Complete a line leaving holes during intermediary steps")
    void canCompleteALine_Left2_Left() {
        var g = new Grid();
        g.firstAdd(RIGHT, TILE_RED_CROSS, TILE_RED_PLUS, TILE_RED_DIAMOND);

        add(g, 1, 0, TILE_GREEN_CROSS);
        add(g, 2, 0, TILE_YELLOW_CROSS);

        add(g, 1, 2, TILE_GREEN_DIAMOND);
        add(g, 2, 2, TILE_YELLOW_DIAMOND);

        TileAtPosition plus_left_left = createTileAtpos(2, -2, TILE_YELLOW_PLUS);
        TileAtPosition round_left = createTileAtpos(2, -1, TILE_YELLOW_ROUND);
        assertDoesNotThrow(() -> {
            g.add(plus_left_left, round_left); // make sur having the "left" tile after the "left left" tile does not throw
        });
        assertAtCorrectPosition(g, plus_left_left);
        assertAtCorrectPosition(g, round_left);
    }


    @Test
    void canNotCompleteALine_leaving_a_hole() {
        var g = new Grid();
        g.firstAdd(RIGHT, TILE_RED_PLUS, TILE_RED_DIAMOND);



        assertThrows(QwirkleException.class, () -> {
            add(g, 2, 1,UP ,TILE_RED_DIAMOND, TILE_RED_CROSS);
        });

    }



    // private methods

    private void add(Grid g, int row, int col, Tile tile) {
        g.add(INITIAL_ROW + row, INITIAL_COL + col, tile);
    }

    private void add(Grid g, int row, int col, Direction d, Tile... line) {
        g.add(INITIAL_ROW + row, INITIAL_COL + col, d, line);
    }

    private Tile get(Grid g, int row, int col) {
        return g.get(INITIAL_ROW + row, INITIAL_COL + col);
    }

    private TileAtPosition createTileAtpos(int row, int col, Tile tile) {
        return new TileAtPosition(INITIAL_ROW + row, INITIAL_COL + col, tile);
    }


    private void assertAtCorrectPosition(Grid g, TileAtPosition tileAtPosition) {
        int row = tileAtPosition.row();
        int col = tileAtPosition.col();
        assertSame(tileAtPosition.tile(), g.get(row, col));
    }

    @Test
    void testaddDirection() {
        var g = new Grid();
        var gr = new GridView(g);
        g.firstAdd(RIGHT, TILE_RED_PLUS, TILE_RED_DIAMOND);

        View.display(gr);
        assertThrows(QwirkleException.class, () -> {
            add(g, 2, 1, UP, TILE_RED_DIAMOND,TILE_RED_CROSS );
        });

    }
}
