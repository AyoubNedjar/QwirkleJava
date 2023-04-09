package g58183.qwirkle.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static g58183.qwirkle.model.Color.*;
import static g58183.qwirkle.model.Direction.*;
import static g58183.qwirkle.model.Shape.*;
import static g58183.qwirkle.model.QwirkleTestUtils.*;

class GridTest {

    private Grid grid;

    @BeforeEach
    void setUp() {
        grid = new Grid();
    }

    @Test
    void firstAdd_one_tile() {
        var tile = new Tile(BLUE, CROSS);
        grid.firstAdd(RIGHT, tile);
        assertSame(get(grid, 0, 0), tile);
    }

    @Test
    void rules_sonia_a() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);
        assertEquals(t1, get(grid, 0, 0));
        assertEquals(t2, get(grid, -1, 0));
        assertEquals(t3, get(grid, -2, 0));
    }

    @Test
    void differentColorSameShape() {
        var t1 = new Tile(RED, SQUARE);
        var t2 = new Tile(BLUE, SQUARE);
        var t3 = new Tile(GREEN, SQUARE);
        grid.firstAdd(UP, t1, t2, t3);
        assertEquals(t1, get(grid, 0, 0));
        assertEquals(t2, get(grid, -1, 0));
        assertEquals(t3, get(grid, -2, 0));
    }


    @Test
    void differentColorAndShape() {
        var t1 = new Tile(RED, SQUARE);
        var t3 = new Tile(GREEN, DIAMOND);
        var t2 = new Tile(BLUE, ROUND);

        assertThrows(QwirkleException.class, () -> {
            grid.firstAdd(RIGHT, t1, t2, t3);
        });
    }

    @Test
    void rules_sonia_a_adapted_to_fail() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, DIAMOND);
        assertThrows(QwirkleException.class, () -> {
            grid.firstAdd(UP, t1, t2, t3);
        });
        assertNull(get(grid, 0, 0));
        assertNull(get(grid, -1, 0));
        assertNull(get(grid, -2, 0));
    }

    @Test
    void firstAdd_cannot_be_called_twice() {
        Tile redcross = new Tile(RED, CROSS);
        Tile reddiamond = new Tile(RED, DIAMOND);
        grid.firstAdd(UP, redcross, reddiamond);
        assertThrows(QwirkleException.class, () -> grid.firstAdd(DOWN, redcross, reddiamond));
    }

    @Test
    void firstAdd_must_be_called_first_simple() {
        Tile redcross = new Tile(RED, CROSS);
        assertThrows(QwirkleException.class, () -> add(grid, 0, 0, redcross));
    }

    @Test
    @DisplayName("get outside the grid should return null, not throw")
    void can_get_tile_outside_virtual_grid() {
        var g = new Grid();
        assertDoesNotThrow(() -> get(g, -250, 500));
        assertNull(get(g, -250, 500));
    }

    @Test
    void rules_Cedric_B() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);

        grid.firstAdd(UP, t1, t2, t3);
        grid.add(46, 45, RIGHT, t4, t5, t6);
        assertEquals(t4, get(grid, 1, 0));
        assertEquals(t5, get(grid, 1, 1));
        assertEquals(t6, get(grid, 1, 2));
    }

    @Test
    void rules_Elvire_C() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);

        var t7 = new Tile(BLUE, ROUND);

        grid.firstAdd(UP, t1, t2, t3);
        grid.add(46, 45, RIGHT, t4, t5, t6);
        grid.add(45, 46, t7);
        assertEquals(t7, get(grid, 0, 1));

    }

    @Test
    void rules_Vincent_D() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);

        var t7 = new Tile(BLUE, ROUND);

        var t8 = new Tile(GREEN, PLUS);
        var t9 = new Tile(GREEN, DIAMOND);


        grid.firstAdd(UP, t1, t2, t3);
        grid.add(46, 45, RIGHT, t4, t5, t6);
        grid.add(45, 46, t7);
        grid.add(43, 44, DOWN, t8, t9);

        assertEquals(t8, get(grid, -2, -1));
        assertEquals(t9, get(grid, -1, -1));

    }

    @Test
    void rules_Sonia_E() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);

        var t7 = new Tile(BLUE, ROUND);

        var t8 = new Tile(GREEN, PLUS);
        var t9 = new Tile(GREEN, DIAMOND);

        //var t10 = new TileAtPosition(42,44,);
        //var t11 = new TileAtPosition(45,44,new Tile(GREEN,ROUND));

        var l = new Tile(GREEN, STAR);
        var t10 = createTileAtpos(-3, -1, l);

        var g = new Tile(GREEN, ROUND);
        var t11 = createTileAtpos(0, -1, g);

        grid.firstAdd(UP, t1, t2, t3);//a
        grid.add(46, 45, RIGHT, t4, t5, t6);//b
        grid.add(45, 46, t7);//c
        grid.add(43, 44, DOWN, t8, t9);//d
        grid.add(t10, t11);//e

        assertEquals(l, get(grid, -3, -1));
        assertEquals(g, get(grid, 0, -1));

    }


    @Test
    void rules_Cedric_F_() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);

        var t7 = new Tile(BLUE, ROUND);

        var t8 = new Tile(GREEN, PLUS);
        var t9 = new Tile(GREEN, DIAMOND);


        var l = new Tile(GREEN, STAR);
        var t10 = createTileAtpos(-3, -1, l);//44,46

        var g = new Tile(GREEN, ROUND);
        var t11 = createTileAtpos(0, -1, g);

        var t12 = new Tile(ORANGE, SQUARE);
        var t13 = new Tile(RED, SQUARE);

        grid.firstAdd(UP, t1, t2, t3);//a
        grid.add(46, 45, RIGHT, t4, t5, t6);//b
        grid.add(45, 46, t7);//c
        grid.add(43, 44, DOWN, t8, t9);//d
        grid.add(t10, t11);//e
        grid.add(46, 48, DOWN, t12, t13);
        ;


        assertEquals(t12, get(grid, 1, 3));
        assertEquals(t13, get(grid, 2, 3));


    }
    @Test
    void rules_Cedric_SameRedPlusOverSameLine_() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);

        var t7 = new Tile(BLUE, ROUND);

        var t8 = new Tile(GREEN, PLUS);
        var t9 = new Tile(GREEN, DIAMOND);


        var l = new Tile(GREEN, STAR);
        var t10 = createTileAtpos(-3, -1, l);//44,46

        var g = new Tile(GREEN, ROUND);
        var t11 = createTileAtpos(0, -1, g);

        //var t12 = new Tile(ORANGE, SQUARE);
        //
        var t13 = new Tile(RED, PLUS);

        grid.firstAdd(UP, t1, t2, t3);//a
        grid.add(46, 45, RIGHT, t4, t5, t6);//b
        grid.add(45, 46, t7);//c
        grid.add(43, 44, DOWN, t8, t9);//d
        grid.add(t10, t11);//e
        ;

        assertThrows(QwirkleException.class, () ->  grid.add(47, 45,t13));
        // assertEquals(t12, get(grid, 1, 3));
        //assertEquals(t13, get(grid, 2, 3));


    }

    @Test
    void rules_Elvire_G_() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);

        var t7 = new Tile(BLUE, ROUND);

        var t8 = new Tile(GREEN, PLUS);
        var t9 = new Tile(GREEN, DIAMOND);

        var l = new Tile(GREEN, STAR);
        var t10 = createTileAtpos(-3, -1, l);//44,46

        var g = new Tile(GREEN, ROUND);
        var t11 = createTileAtpos(0, -1, g);

        var t12 = new Tile(ORANGE, SQUARE);
        var t13 = new Tile(RED, SQUARE);

        var t14 = new Tile(YELLOW, STAR);
        var t15 = new Tile(ORANGE, STAR);

        grid.firstAdd(UP, t1, t2, t3);//a
        grid.add(46, 45, RIGHT, t4, t5, t6);//b
        grid.add(45, 46, t7);//c
        grid.add(43, 44, DOWN, t8, t9);//d
        grid.add(t10, t11);//e
        grid.add(46, 48, DOWN, t12, t13);
        grid.add(42, 43, LEFT, t14, t15);

        //assertThrows(QwirkleException.class, () -> grid.add(t10,t11));
        assertEquals(t14, get(grid, -3, -2));
        assertEquals(t15, get(grid, -3, -3));

    }
    @Test
    void rules_Vincent_H_() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);

        var t7 = new Tile(BLUE, ROUND);

        var t8 = new Tile(GREEN, PLUS);
        var t9 = new Tile(GREEN, DIAMOND);

        var l = new Tile(GREEN, STAR);
        var t10 = createTileAtpos(-3, -1, l);//44,46

        var g = new Tile(GREEN, ROUND);
        var t11 = createTileAtpos(0, -1, g);

        var t12 = new Tile(ORANGE, SQUARE);
        var t13 = new Tile(RED, SQUARE);

        var t14 = new Tile(YELLOW, STAR);
        var t15 = new Tile(ORANGE, STAR);

        var t16 = new Tile(ORANGE, CROSS);
        var t17 = new Tile(ORANGE, DIAMOND);

        grid.firstAdd(UP, t1, t2, t3);//a
        grid.add(46, 45, RIGHT, t4, t5, t6);//b
        grid.add(45, 46, t7);//c
        grid.add(43, 44, DOWN, t8, t9);//d
        grid.add(t10, t11);//e
        grid.add(46, 48, DOWN, t12, t13);
        grid.add(42, 43, LEFT, t14, t15);
        grid.add(43, 42, DOWN, t16, t17);

        //assertThrows(QwirkleException.class, () -> grid.add(t10,t11));
        assertEquals(t16, get(grid, -2, -3));
        assertEquals(t17, get(grid, -1, -3));

    }
    @Test
    void rules_Sonia_I_() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);

        var t7 = new Tile(BLUE, ROUND);

        var t8 = new Tile(GREEN, PLUS);
        var t9 = new Tile(GREEN, DIAMOND);

        var l = new Tile(GREEN, STAR);
        var t10 = createTileAtpos(-3, -1, l);//44,46

        var g = new Tile(GREEN, ROUND);
        var t11 = createTileAtpos(0, -1, g);

        var t12 = new Tile(ORANGE, SQUARE);
        var t13 = new Tile(RED, SQUARE);

        var t14 = new Tile(YELLOW, STAR);
        var t15 = new Tile(ORANGE, STAR);

        var t16 = new Tile(ORANGE, CROSS);
        var t17 = new Tile(ORANGE, DIAMOND);

        var t18 = new Tile(YELLOW, DIAMOND);
        var t19 = new Tile(YELLOW, ROUND);

        grid.firstAdd(UP, t1, t2, t3);//a
        grid.add(46, 45, RIGHT, t4, t5, t6);//b
        grid.add(45, 46, t7);//c
        grid.add(43, 44, DOWN, t8, t9);//d
        grid.add(t10, t11);//e
        grid.add(46, 48, DOWN, t12, t13);
        grid.add(42, 43, LEFT, t14, t15);
        grid.add(43, 42, DOWN, t16, t17);
        grid.add(44, 43, DOWN, t18, t19);

        //assertThrows(QwirkleException.class, () -> grid.add(t10,t11));
        assertEquals(t18, get(grid, -1, -2));
        assertEquals(t19, get(grid, 0, -2));

    }
    @Test
    void rules_Cedric_J_() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);

        var t7 = new Tile(BLUE, ROUND);

        var t8 = new Tile(GREEN, PLUS);
        var t9 = new Tile(GREEN, DIAMOND);

        var l = new Tile(GREEN, STAR);
        var t10 = createTileAtpos(-3, -1, l);//44,46

        var g = new Tile(GREEN, ROUND);
        var t11 = createTileAtpos(0, -1, g);

        var t12 = new Tile(ORANGE, SQUARE);
        var t13 = new Tile(RED, SQUARE);

        var t14 = new Tile(YELLOW, STAR);
        var t15 = new Tile(ORANGE, STAR);

        var t16 = new Tile(ORANGE, CROSS);
        var t17 = new Tile(ORANGE, DIAMOND);

        var t18 = new Tile(YELLOW, DIAMOND);
        var t19 = new Tile(YELLOW, ROUND);

        var t20 = new Tile(RED, STAR);

        grid.firstAdd(UP, t1, t2, t3);//a
        grid.add(46, 45, RIGHT, t4, t5, t6);//b
        grid.add(45, 46, t7);//c
        grid.add(43, 44, DOWN, t8, t9);//d
        grid.add(t10, t11);//e
        grid.add(46, 48, DOWN, t12, t13);
        grid.add(42, 43, LEFT, t14, t15);
        grid.add(43, 42, DOWN, t16, t17);
        grid.add(44, 43, DOWN, t18, t19);
        grid.add(42, 45, t20);

        //assertThrows(QwirkleException.class, () -> grid.add(t10,t11));
        assertEquals(t20, get(grid, -3, 0));


    }
    @Test
    void rules_Cedric_J_Fail_() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);

        var t7 = new Tile(BLUE, ROUND);

        var t8 = new Tile(GREEN, PLUS);
        var t9 = new Tile(GREEN, DIAMOND);

        var l = new Tile(GREEN, STAR);
        var t10 = createTileAtpos(-3, -1, l);//44,46

        var g = new Tile(GREEN, ROUND);
        var t11 = createTileAtpos(0, -1, g);

        var t12 = new Tile(ORANGE, SQUARE);
        var t13 = new Tile(RED, SQUARE);

        var t14 = new Tile(YELLOW, STAR);
        var t15 = new Tile(ORANGE, STAR);

        var t16 = new Tile(ORANGE, CROSS);
        var t17 = new Tile(ORANGE, DIAMOND);

        var t18 = new Tile(YELLOW, DIAMOND);
        var t19 = new Tile(YELLOW, ROUND);

        var t20 = new Tile(YELLOW, SQUARE);

        grid.firstAdd(UP, t1, t2, t3);//a
        grid.add(46, 45, RIGHT, t4, t5, t6);//b
        grid.add(45, 46, t7);//c
        grid.add(43, 44, DOWN, t8, t9);//d
        grid.add(t10, t11);//e
        grid.add(46, 48, DOWN, t12, t13);
        grid.add(42, 43, LEFT, t14, t15);
        grid.add(43, 42, DOWN, t16, t17);
        grid.add(44, 43, DOWN, t18, t19);


        assertThrows(QwirkleException.class, () -> grid.add(43, 43, t20));



    }
    @Test
    void rules_Elvire_K_() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);

        var t7 = new Tile(BLUE, ROUND);

        var t8 = new Tile(GREEN, PLUS);
        var t9 = new Tile(GREEN, DIAMOND);

        var l = new Tile(GREEN, STAR);
        var t10 = createTileAtpos(-3, -1, l);//44,46

        var g = new Tile(GREEN, ROUND);
        var t11 = createTileAtpos(0, -1, g);

        var t12 = new Tile(ORANGE, SQUARE);
        var t13 = new Tile(RED, SQUARE);

        var t14 = new Tile(YELLOW, STAR);
        var t15 = new Tile(ORANGE, STAR);

        var t16 = new Tile(ORANGE, CROSS);
        var t17 = new Tile(ORANGE, DIAMOND);

        var t18 = new Tile(YELLOW, DIAMOND);
        var t19 = new Tile(YELLOW, ROUND);

        var t20 = new Tile(RED, STAR);

        var t21 = new Tile(BLUE, CROSS);
        var t22= new Tile(RED, CROSS);
        var t23= new Tile(ORANGE, CROSS);

        grid.firstAdd(UP, t1, t2, t3);//a
        grid.add(46, 45, RIGHT, t4, t5, t6);//b
        grid.add(45, 46, t7);//c
        grid.add(43, 44, DOWN, t8, t9);//d
        grid.add(t10, t11);//e
        grid.add(46, 48, DOWN, t12, t13);
        grid.add(42, 43, LEFT, t14, t15);
        grid.add(43, 42, DOWN, t16, t17);
        grid.add(44, 43, DOWN, t18, t19);
        grid.add(47, 46, LEFT,t21,t22,t23);

        //assertThrows(QwirkleException.class, () -> grid.add(t10,t11));
        assertEquals(t21, get(grid, 2, 1));
        assertEquals(t22, get(grid, 2, 0));
        assertEquals(t23, get(grid, 2, -1));

    }
    @Test
    void rules_Vincent_L_() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);

        var t7 = new Tile(BLUE, ROUND);

        var t8 = new Tile(GREEN, PLUS);
        var t9 = new Tile(GREEN, DIAMOND);

        var l = new Tile(GREEN, STAR);
        var t10 = createTileAtpos(-3, -1, l);//44,46

        var g = new Tile(GREEN, ROUND);
        var t11 = createTileAtpos(0, -1, g);

        var t12 = new Tile(ORANGE, SQUARE);
        var t13 = new Tile(RED, SQUARE);

        var t14 = new Tile(YELLOW, STAR);
        var t15 = new Tile(ORANGE, STAR);

        var t16 = new Tile(ORANGE, CROSS);
        var t17 = new Tile(ORANGE, DIAMOND);

        var t18 = new Tile(YELLOW, DIAMOND);
        var t19 = new Tile(YELLOW, ROUND);

        var t20 = new Tile(RED, STAR);

        var t21 = new Tile(BLUE, CROSS);
        var t22= new Tile(RED, CROSS);
        var t23= new Tile(ORANGE, CROSS);

        var t24 = new Tile(YELLOW, SQUARE);
        var t25 = new Tile(BLUE, SQUARE);

        grid.firstAdd(UP, t1, t2, t3);//a
        grid.add(46, 45, RIGHT, t4, t5, t6);//b
        grid.add(45, 46, t7);//c
        grid.add(43, 44, DOWN, t8, t9);//d
        grid.add(t10, t11);//e
        grid.add(46, 48, DOWN, t12, t13);
        grid.add(42, 43, LEFT, t14, t15);
        grid.add(43, 42, DOWN, t16, t17);
        grid.add(44, 43, DOWN, t18, t19);
        grid.add(47, 46, LEFT,t21,t22,t23);
        grid.add(46, 49, DOWN,t24,t25);

        //assertThrows(QwirkleException.class, () -> grid.add(t10,t11));
        assertEquals(t24, get(grid, 1, 4));
        assertEquals(t25, get(grid, 2, 4));

    }
    @Test
    void rules_Vincent_L_Fail_Inverse_Square() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);

        var t7 = new Tile(BLUE, ROUND);

        var t8 = new Tile(GREEN, PLUS);
        var t9 = new Tile(GREEN, DIAMOND);

        var l = new Tile(GREEN, STAR);
        var t10 = createTileAtpos(-3, -1, l);//44,46

        var g = new Tile(GREEN, ROUND);
        var t11 = createTileAtpos(0, -1, g);

        var t12 = new Tile(ORANGE, SQUARE);
        var t13 = new Tile(RED, SQUARE);

        var t14 = new Tile(YELLOW, STAR);
        var t15 = new Tile(ORANGE, STAR);

        var t16 = new Tile(ORANGE, CROSS);
        var t17 = new Tile(ORANGE, DIAMOND);

        var t18 = new Tile(YELLOW, DIAMOND);
        var t19 = new Tile(YELLOW, ROUND);

        var t20 = new Tile(RED, STAR);

        var t21 = new Tile(BLUE, CROSS);
        var t22= new Tile(RED, CROSS);
        var t23= new Tile(ORANGE, CROSS);

        var t25 = new Tile(YELLOW, SQUARE);
        var t24 = new Tile(BLUE, SQUARE);

        grid.firstAdd(UP, t1, t2, t3);//a
        grid.add(46, 45, RIGHT, t4, t5, t6);//b
        grid.add(45, 46, t7);//c
        grid.add(43, 44, DOWN, t8, t9);//d
        grid.add(t10, t11);//e
        grid.add(46, 48, DOWN, t12, t13);
        grid.add(42, 43, LEFT, t14, t15);
        grid.add(43, 42, DOWN, t16, t17);
        grid.add(44, 43, DOWN, t18, t19);
        grid.add(47, 46, LEFT,t21,t22,t23);


        assertThrows(QwirkleException.class, () -> grid.add(46, 49, DOWN,t24,t25));


    }

}
