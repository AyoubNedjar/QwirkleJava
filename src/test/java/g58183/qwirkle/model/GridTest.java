package g58183.qwirkle.model;

import g58183.qwirkle.view.View;
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

    //Pour afficher la grid c'est plus simple pour comprendre les erreurs
    private GridView gridView;

    @BeforeEach
    void setUp() {
        grid = new Grid();
        gridView = new GridView(grid);
    }

    @Test
    void firstAdd_one_tile() {
        var tile = new Tile(BLUE, CROSS);
        grid.firstAdd(RIGHT, tile);
        assertSame(get(grid, 0, 0), tile);
    }

    @Test
    public void add_Position_At_Row_Equal_Zero() {
        Tile tile2 = new Tile(RED, CROSS);
        grid.firstAdd(UP, tile2);
        Tile tile3 = new Tile(RED, CROSS);


        assertThrows(QwirkleException.class, () -> add(grid, 0, 1, tile3));

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
    void rules_cedric_b() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);

        assertEquals(t4, get(grid, 1, 0));
        assertEquals(t5, get(grid, 1, 1));
        assertEquals(t6, get(grid, 1, 2));

    }

    @Test
    void rules_cedric_b_fail() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);

        var gr = new GridView(grid);
        View.display(gr);
        var t7 = new Tile(YELLOW, SQUARE);
        var t8 = new Tile(YELLOW, STAR);
        var t9 = new Tile(YELLOW, ROUND);



        assertThrows(QwirkleException.class, ()->grid.add(46, 48, RIGHT, t7, t8, t9));
    }

    @Test
    void rules_cedric_b_fail_2() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);
        var gr = new GridView(grid);
        View.display(gr);

        var t7 = new Tile(YELLOW, SQUARE);
        var t8 = new Tile(YELLOW, STAR);
        var t9 = new Tile(PURPLE, ROUND);

        View.display(gr);
        assertThrows(QwirkleException.class, ()->grid.add(46, 48, RIGHT, t7, t8, t9));

    }

    @Test
    void rules_cedric_b_insert_between_lines() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);

        var t7 = new Tile(PURPLE, ROUND);
        var t8 = new Tile(PURPLE, DIAMOND);
        var t9 = new Tile(PURPLE, PLUS);
        grid.add(45, 47, UP, t7, t8, t9);

        var t10 = new Tile(BLUE, ROUND);
        var t11 = new Tile(BLUE, DIAMOND);
        var t12 = new Tile(BLUE, PLUS);
        grid.add(45, 46, UP, t10, t11, t12);
    }

    @Test
    void rules_cedric_b_adapted_to_fail() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(BLUE, SQUARE);
        assertThrows(QwirkleException.class, () -> {
            grid.firstAdd(RIGHT, t4, t5, t6);

        });

    }
    @Test
    void rules_Elvire_c_adapted_fail() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);
        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);
        var t7 = new Tile(BLUE, ROUND);
        assertThrows(QwirkleException.class, () -> {
            grid.add(45, 47, t7);

        });

    }

    @Test
    void rules_elvire_c_adapted() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);
        var t7 = new Tile(BLUE, ROUND);
        grid.add(45, 46, t7);

        assertEquals(t7, get(grid, 0, 1));
    }

    @Test
    void rules_vincent_d_adapted() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);

        var t7 = new Tile(BLUE, ROUND);
        grid.add(45, 46, t7);

        var t8 = new Tile(GREEN, PLUS);
        var t9 = new Tile(GREEN, DIAMOND);
        grid.add(43, 44, DOWN, t8, t9);

        assertEquals(t8, get(grid, -2, -1));
        assertEquals(t9, get(grid, -1, -1));
    }

    @Test
    void rules_vincent_d_adapted_fail() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);

        var t7 = new Tile(BLUE, ROUND);
        grid.add(45, 46, t7);

        var t8 = new Tile(GREEN, PLUS);
        var t9 = new Tile(GREEN, DIAMOND);
        assertThrows(QwirkleException.class, () -> {
            grid.add(46, 43, LEFT, t8, t9);
        });

    }

    @Test
    void rules_sonia_e_adapted() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);

        var t7 = new Tile(BLUE, ROUND);
        grid.add(45, 46, t7);

        var t8 = new Tile(GREEN, DIAMOND);
        var t9 = new Tile(GREEN, PLUS);
        grid.add(44, 44, UP, t8, t9);

        var t10 = new Tile(GREEN, ROUND);
        var t11 = new Tile(GREEN, STAR);
        grid.add(new TileAtPosition(45, 44, t10), new TileAtPosition(42, 44, t11));

        assertEquals(t10, get(grid,0,-1 ));
        assertEquals(t11, get(grid,-3,-1 ));
    }

    @Test
    void rules_sonia_e_adapted_to_fail() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);
        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);
        var t7 = new Tile(BLUE, ROUND);
        grid.add(45, 46, t7);
        var t8 = new Tile(GREEN, DIAMOND);
        var t9 = new Tile(GREEN, PLUS);
        grid.add(44, 44, UP, t8, t9);
        var t10 = new Tile(GREEN, ROUND);
        var t11 = new Tile(GREEN, STAR);
        assertThrows(QwirkleException.class, () -> {
            grid.add(new TileAtPosition(44, 44, t10),new TileAtPosition(42,45,t11));
        });
        assertNull(grid.get(5,6));
        assertNull(grid.get(7,9));
    }
    @Test
    void rules_sonia_e_adapted_to_fail_2() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);
        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);
        var t7 = new Tile(BLUE, ROUND);
        grid.add(45, 46, t7);
        var t8 = new Tile(GREEN, DIAMOND);
        var t9 = new Tile(GREEN, PLUS);
        grid.add(44, 44, UP, t8, t9);
        var t10 = new Tile(GREEN, ROUND);
        var t11 = new Tile(GREEN, DIAMOND);



        assertThrows(QwirkleException.class, () -> {
            grid.add(new TileAtPosition(45, 44, t10),new TileAtPosition(42,44,t11));

        });

        assertNull(grid.get(5,6));
        assertNull(grid.get(7,9));
    }
    @Test
    void rules_cedric_f_adapted() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);
        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);
        var t7 = new Tile(BLUE, ROUND);
        grid.add(45, 46, t7);
        var t8 = new Tile(GREEN, DIAMOND);
        var t9 = new Tile(GREEN, PLUS);
        grid.add(44, 44, UP, t8, t9);
        var t10 = new Tile(GREEN, ROUND);
        var t11 = new Tile(GREEN, STAR);
        grid.add(new TileAtPosition(45, 44, t10), new TileAtPosition(42, 44, t11));
        var t12 = new Tile(RED, SQUARE);
        var t13 = new Tile(ORANGE, SQUARE);
        grid.add(new TileAtPosition(46, 48, t13), new TileAtPosition(47, 48, t12));

    }

    @Test
    void rules_cedric_f_adapted_to_fail() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);

        var t7 = new Tile(BLUE, ROUND);
        grid.add(45, 46, t7);

        var t8 = new Tile(GREEN, DIAMOND);
        var t9 = new Tile(GREEN, PLUS);
        grid.add(44, 44, UP, t8, t9);

        var t10 = new Tile(GREEN, ROUND);
        var t11 = new Tile(GREEN, STAR);
        grid.add(new TileAtPosition(45, 44, t10), new TileAtPosition(42, 44, t11));


        assertThrows(QwirkleException.class, () -> {
            grid.add(new TileAtPosition(46, 48, t10), new TileAtPosition(47, 48, t11));
        });

    }
    @Test
    void rules_cedric_f_adapted_to_fail_2() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);

        var t7 = new Tile(BLUE, ROUND);
        grid.add(45, 46, t7);

        var t8 = new Tile(GREEN, DIAMOND);
        var t9 = new Tile(GREEN, PLUS);
        grid.add(44, 44, UP, t8, t9);

        var t10 = new Tile(GREEN, ROUND);
        var t11 = new Tile(YELLOW, STAR);


        assertThrows(QwirkleException.class, () -> {
            grid.add(new TileAtPosition(46, 48, t10), new TileAtPosition(47, 48, t11));
        });

    }
    @Test
    void rules_elvire_g_adapted(){
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);

        var t7 = new Tile(BLUE, ROUND);
        grid.add(45, 46, t7);

        var t8 = new Tile(GREEN, DIAMOND);
        var t9 = new Tile(GREEN, PLUS);
        grid.add(44, 44, UP, t8, t9);

        var t10 = new Tile(GREEN, ROUND);
        var t11 = new Tile(GREEN, STAR);
        grid.add(new TileAtPosition(45, 44, t10), new TileAtPosition(42, 44, t11));

        var t12 = new Tile(RED, SQUARE);
        var t13 = new Tile(ORANGE, SQUARE);
        grid.add(new TileAtPosition(46, 48, t13), new TileAtPosition(47, 48, t12));

        var t14 = new Tile(YELLOW,STAR);
        var t15 = new Tile(ORANGE,STAR);
        grid.add(42,43,LEFT,t14,t15 );

        assertEquals(t14,get(grid,-3,-2));
        assertEquals(t15,get(grid,-3,-3));
    }
    @Test
    void rules_elvire_g_adapted_to_fail(){
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);
        ;
        var t7 = new Tile(BLUE, ROUND);
        grid.add(45, 46, t7);

        var t8 = new Tile(GREEN, DIAMOND);
        var t9 = new Tile(GREEN, PLUS);
        grid.add(44, 44, UP, t8, t9);

        var t10 = new Tile(GREEN, ROUND);
        var t11 = new Tile(GREEN, STAR);
        grid.add(new TileAtPosition(45, 44, t10), new TileAtPosition(42, 44, t11));

        var t12 = new Tile(RED, SQUARE);
        var t13 = new Tile(ORANGE, SQUARE);
        grid.add(new TileAtPosition(46, 48, t13), new TileAtPosition(47, 48, t12));

        var t14 = new Tile(YELLOW,STAR);
        var t15 = new Tile(YELLOW,DIAMOND);
        assertThrows(QwirkleException.class, () -> {
            grid.add(45, 43, LEFT, t14, t15);

        });
        assertNull(grid.get(0,3));
        assertNull(grid.get(0,3));
    }
    @Test
    void rules_elvire_g_adapted_to_fail_2(){
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);
        ;
        var t7 = new Tile(BLUE, ROUND);
        grid.add(45, 46, t7);

        var t8 = new Tile(GREEN, DIAMOND);
        var t9 = new Tile(GREEN, PLUS);
        grid.add(44, 44, UP, t8, t9);

        var t10 = new Tile(GREEN, ROUND);
        var t11 = new Tile(GREEN, STAR);
        grid.add(new TileAtPosition(45, 44, t10), new TileAtPosition(42, 44, t11));

        var t12 = new Tile(RED, SQUARE);
        var t13 = new Tile(ORANGE, SQUARE);
        grid.add(new TileAtPosition(46, 48, t13), new TileAtPosition(47, 48, t12));

        var t14 = new Tile(YELLOW,STAR);
        var t15 = new Tile(YELLOW,DIAMOND);
        assertThrows(QwirkleException.class, () -> {
            grid.add(46, 43, LEFT, t14, t15);

        });
        assertNull(grid.get(0,3));
        assertNull(grid.get(0,3));
    }
    @Test
    void rules_elvire_g_adapted_to_fail_1(){
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);

        var t7 = new Tile(BLUE, ROUND);
        grid.add(45, 46, t7);

        var t8 = new Tile(GREEN, DIAMOND);
        var t9 = new Tile(GREEN, PLUS);
        grid.add(44, 44, UP, t8, t9);

        var t10 = new Tile(GREEN, ROUND);
        var t11 = new Tile(GREEN, STAR);
        grid.add(new TileAtPosition(45, 44, t10), new TileAtPosition(42, 44, t11));
        var t12 = new Tile(RED, SQUARE);
        var t13 = new Tile(ORANGE, SQUARE);
        grid.add(new TileAtPosition(46, 48, t13), new TileAtPosition(47, 48, t12));
        var t14 = new Tile(YELLOW,STAR);
        var t15 = new Tile(YELLOW,PLUS);
        assertThrows(QwirkleException.class, () -> {
            grid.add(46, 43, LEFT, t14, t15);

        });
        assertNull(grid.get(2,3));
        assertNull(grid.get(1,3));
    }
    @Test
    void rules_vincent_h_adapted(){
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);

        var t7 = new Tile(BLUE, ROUND);
        grid.add(45, 46, t7);

        var t8 = new Tile(GREEN, DIAMOND);
        var t9 = new Tile(GREEN, PLUS);
        grid.add(44, 44, UP, t8, t9);

        var t10 = new Tile(GREEN, ROUND);
        var t11 = new Tile(GREEN, STAR);
        grid.add(new TileAtPosition(45, 44, t10), new TileAtPosition(42, 44, t11));
        var t12 = new Tile(RED, SQUARE);
        var t13 = new Tile(ORANGE, SQUARE);
        grid.add(new TileAtPosition(46, 48, t13), new TileAtPosition(47, 48, t12));
        var t14 = new Tile(YELLOW,STAR);
        var t15 = new Tile(ORANGE,STAR);
        grid.add(42,43,LEFT,t14,t15 );

        var t16 = new Tile(ORANGE, CROSS);
        var t17 = new Tile(ORANGE, DIAMOND);
        grid.add( 43,42,DOWN, t16, t17);

        assertEquals(t16, get(grid,-2,-3));
        assertEquals(t17, get(grid,-1,-3));
    }
    @Test
    void rules_vincent_h_adapted_to_fail() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);

        var t7 = new Tile(BLUE, ROUND);
        grid.add(45, 46, t7);

        var t8 = new Tile(GREEN, DIAMOND);
        var t9 = new Tile(GREEN, PLUS);
        grid.add(44, 44, UP, t8, t9);

        var t10 = new Tile(GREEN, ROUND);
        var t11 = new Tile(GREEN, STAR);
        grid.add(new TileAtPosition(45, 44, t10), new TileAtPosition(42, 44, t11));
        var t12 = new Tile(RED, SQUARE);
        var t13 = new Tile(ORANGE, SQUARE);
        grid.add(new TileAtPosition(46, 48, t13), new TileAtPosition(47, 48, t12));
        var t14 = new Tile(YELLOW, STAR);
        var t15 = new Tile(ORANGE, STAR);
        grid.add(42, 43, LEFT, t14, t15);

        var t16 = new Tile(ORANGE, CROSS);
        var t17 = new Tile(ORANGE, DIAMOND);
        grid.add(43, 42, DOWN, t16, t17);

        assertThrows(QwirkleException.class, () -> {
            grid.add(46, 43, LEFT, t16, t17);

        });
        assertNull(grid.get(2, 3));
        assertNull(grid.get(1, 3));
    }
    @Test
    void rules_vincent_h_adapted_to_fail_2() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);

        var t7 = new Tile(BLUE, ROUND);
        grid.add(45, 46, t7);

        var t8 = new Tile(GREEN, DIAMOND);
        var t9 = new Tile(GREEN, PLUS);
        grid.add(44, 44, UP, t8, t9);

        var t10 = new Tile(GREEN, ROUND);
        var t11 = new Tile(GREEN, STAR);
        grid.add(new TileAtPosition(45, 44, t10), new TileAtPosition(42, 44, t11));
        var t12 = new Tile(RED, SQUARE);
        var t13 = new Tile(ORANGE, SQUARE);
        grid.add(new TileAtPosition(46, 48, t13), new TileAtPosition(47, 48, t12));
        var t14 = new Tile(YELLOW, STAR);
        var t15 = new Tile(ORANGE, STAR);
        grid.add(42, 43, LEFT, t14, t15);

        var t16 = new Tile(ORANGE, CROSS);
        var t17 = new Tile(ORANGE, DIAMOND);
        grid.add(43, 42, DOWN, t16, t17);

        assertThrows(QwirkleException.class, () -> {
            grid.add(46, 43, LEFT, t16, t17);

        });
        assertNull(grid.get(0, 3));
        assertNull(grid.get(0, 3));
    }
    @Test
    void rules_vincent_h_adapted_to_fail_3() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);

        var t7 = new Tile(BLUE, ROUND);
        grid.add(45, 46, t7);

        var t8 = new Tile(GREEN, DIAMOND);
        var t9 = new Tile(GREEN, PLUS);
        grid.add(44, 44, UP, t8, t9);

        var t10 = new Tile(GREEN, ROUND);
        var t11 = new Tile(GREEN, STAR);
        grid.add(new TileAtPosition(45, 44, t10), new TileAtPosition(42, 44, t11));
        var t12 = new Tile(RED, SQUARE);
        var t13 = new Tile(ORANGE, SQUARE);
        grid.add(new TileAtPosition(46, 48, t13), new TileAtPosition(47, 48, t12));
        var t14 = new Tile(YELLOW, STAR);
        var t15 = new Tile(ORANGE, STAR);
        grid.add(42, 43, LEFT, t14, t15);

        var t16 = new Tile(ORANGE, CROSS);
        var t17 = new Tile(ORANGE, DIAMOND);
        grid.add(43, 42, DOWN, t16, t17);

        assertThrows(QwirkleException.class, () -> {
            grid.add(46, 43, LEFT, t16, t17);

        });
        assertNull(grid.get(5, 8));
        assertNull(grid.get(1, 3));
    }


    @Test
    void rules_sonia_i_adapted() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);

        var t7 = new Tile(BLUE, ROUND);
        grid.add(45, 46, t7);

        var t8 = new Tile(GREEN, DIAMOND);
        var t9 = new Tile(GREEN, PLUS);
        grid.add(44, 44, UP, t8, t9);

        var t10 = new Tile(GREEN, ROUND);
        var t11 = new Tile(GREEN, STAR);
        grid.add(new TileAtPosition(45, 44, t10), new TileAtPosition(42, 44, t11));
        var t12 = new Tile(RED, SQUARE);
        var t13 = new Tile(ORANGE, SQUARE);
        grid.add(new TileAtPosition(46, 48, t13), new TileAtPosition(47, 48, t12));
        var t14 = new Tile(YELLOW, STAR);
        var t15 = new Tile(ORANGE, STAR);
        grid.add(42, 43, LEFT, t14, t15);

        var t16 = new Tile(ORANGE, CROSS);
        var t17 = new Tile(ORANGE, DIAMOND);
        grid.add(43, 42, DOWN, t16, t17);

        var t18 = new Tile(YELLOW, DIAMOND);
        var t19 = new Tile(YELLOW, ROUND);
        grid.add(44, 43, DOWN, t18, t19);

        assertEquals(t18, get(grid, -1, -2));
        assertEquals(t19, get(grid, 0, -2));
    }
    @Test
    void rules_sonia_i_adapted_to_fail() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);

        var t7 = new Tile(BLUE, ROUND);
        grid.add(45, 46, t7);
        ;
        var t8 = new Tile(GREEN, DIAMOND);
        var t9 = new Tile(GREEN, PLUS);
        grid.add(44, 44, UP, t8, t9);

        var t10 = new Tile(GREEN, ROUND);
        var t11 = new Tile(GREEN, STAR);
        grid.add(new TileAtPosition(45, 44, t10), new TileAtPosition(42, 44, t11));
        var t12 = new Tile(RED, SQUARE);
        var t13 = new Tile(ORANGE, SQUARE);
        grid.add(new TileAtPosition(46, 48, t13), new TileAtPosition(47, 48, t12));
        var t14 = new Tile(YELLOW, STAR);
        var t15 = new Tile(ORANGE, STAR);
        grid.add(42, 43, LEFT, t14, t15);

        var t16 = new Tile(ORANGE, CROSS);
        var t17 = new Tile(ORANGE, DIAMOND);
        grid.add(43, 42, DOWN, t16, t17);

        var t18 = new Tile(YELLOW, DIAMOND);
        var t19 = new Tile(YELLOW, ROUND);
        assertThrows(QwirkleException.class, () -> {
            grid.add(46, 43, DOWN, t18, t19);

        });
        assertNull(grid.get(-1, -2));
        assertNull(grid.get(1, 3));
    }
    @Test
    void rules_sonia_i_adapted_to_fail_2() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);

        var t7 = new Tile(BLUE, ROUND);
        grid.add(45, 46, t7);

        var t8 = new Tile(GREEN, DIAMOND);
        var t9 = new Tile(GREEN, PLUS);
        grid.add(44, 44, UP, t8, t9);

        var t10 = new Tile(GREEN, ROUND);
        var t11 = new Tile(GREEN, STAR);
        grid.add(new TileAtPosition(45, 44, t10), new TileAtPosition(42, 44, t11));

        var t12 = new Tile(RED, SQUARE);
        var t13 = new Tile(ORANGE, SQUARE);
        grid.add(new TileAtPosition(46, 48, t13), new TileAtPosition(47, 48, t12));

        var t14 = new Tile(YELLOW, STAR);
        var t15 = new Tile(ORANGE, STAR);
        grid.add(42, 43, LEFT, t14, t15);

        var t16 = new Tile(ORANGE, CROSS);
        var t17 = new Tile(ORANGE, DIAMOND);
        grid.add(43, 42, DOWN, t16, t17);

        var t18 = new Tile(YELLOW, DIAMOND);
        var t19 = new Tile(PURPLE, ROUND);
        assertThrows(QwirkleException.class, () -> {
            grid.add(46, 43, DOWN, t18, t19);

        });
        assertNull(grid.get(-1, -2));
        assertNull(grid.get(1, 3));
    }


    @Test
    void rules_cedric_j_adapted() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);

        var t7 = new Tile(BLUE, ROUND);
        grid.add(45, 46, t7);

        var t8 = new Tile(GREEN, DIAMOND);
        var t9 = new Tile(GREEN, PLUS);
        grid.add(44, 44, UP, t8, t9);

        var t10 = new Tile(GREEN, ROUND);
        var t11 = new Tile(GREEN, STAR);
        grid.add(new TileAtPosition(45, 44, t10), new TileAtPosition(42, 44, t11));

        var t12 = new Tile(RED, SQUARE);
        var t13 = new Tile(ORANGE, SQUARE);
        grid.add(new TileAtPosition(46, 48, t13), new TileAtPosition(47, 48, t12));

        var t14 = new Tile(YELLOW, STAR);
        var t15 = new Tile(ORANGE, STAR);
        grid.add(42, 43, LEFT, t14, t15);

        var t16 = new Tile(ORANGE, CROSS);
        var t17 = new Tile(ORANGE, DIAMOND);
        grid.add(43, 42, DOWN, t16, t17);

        var t18 = new Tile(YELLOW, DIAMOND);
        var t19 = new Tile(YELLOW, ROUND);
        grid.add(44, 43, DOWN, t18, t19);
        var t20 = new Tile(RED, STAR);
        grid.add(42, 45, t20);
        assertEquals(t20, get(grid, -3, 0));
    }
    @Test
    void rules_cedric_j_adapted_to_fail() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);

        var t7 = new Tile(BLUE, ROUND);
        grid.add(45, 46, t7);

        var t8 = new Tile(GREEN, DIAMOND);
        var t9 = new Tile(GREEN, PLUS);
        grid.add(44, 44, UP, t8, t9);

        var t10 = new Tile(GREEN, ROUND);
        var t11 = new Tile(GREEN, STAR);
        grid.add(new TileAtPosition(45, 44, t10), new TileAtPosition(42, 44, t11));

        var t12 = new Tile(RED, SQUARE);
        var t13 = new Tile(ORANGE, SQUARE);
        grid.add(new TileAtPosition(46, 48, t13), new TileAtPosition(47, 48, t12));

        var t14 = new Tile(YELLOW, STAR);
        var t15 = new Tile(ORANGE, STAR);
        grid.add(42, 43, LEFT, t14, t15);

        var t16 = new Tile(ORANGE, CROSS);
        var t17 = new Tile(ORANGE, DIAMOND);
        grid.add(43, 42, DOWN, t16, t17);

        var t18 = new Tile(YELLOW, DIAMOND);
        var t19 = new Tile(YELLOW, ROUND);
        grid.add(44, 43, DOWN, t18, t19);

        var t20 = new Tile(RED, STAR);
        assertThrows(QwirkleException.class, () -> {
            grid.add(45, 43, DOWN, t20);

        });
        assertNull(grid.get(0,3));

    }
    @Test
    void rules_cedric_j_adapted_to_fail_2() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);

        var t7 = new Tile(BLUE, ROUND);
        grid.add(45, 46, t7);

        var t8 = new Tile(GREEN, DIAMOND);
        var t9 = new Tile(GREEN, PLUS);
        grid.add(44, 44, UP, t8, t9);

        var t10 = new Tile(GREEN, ROUND);
        var t11 = new Tile(GREEN, STAR);
        grid.add(new TileAtPosition(45, 44, t10), new TileAtPosition(42, 44, t11));
        var t12 = new Tile(RED, SQUARE);
        var t13 = new Tile(ORANGE, SQUARE);
        grid.add(new TileAtPosition(46, 48, t13), new TileAtPosition(47, 48, t12));
        var t14 = new Tile(YELLOW, STAR);
        var t15 = new Tile(ORANGE, STAR);
        grid.add(42, 43, LEFT, t14, t15);

        var t16 = new Tile(ORANGE, CROSS);
        var t17 = new Tile(ORANGE, DIAMOND);
        grid.add(43, 42, DOWN, t16, t17);

        var t18 = new Tile(YELLOW, DIAMOND);
        var t19 = new Tile(YELLOW, ROUND);
        grid.add(44, 43, DOWN, t18, t19);

        var t20 = new Tile(RED, STAR);
        assertThrows(QwirkleException.class, () -> {
            grid.add(45, 44, DOWN, t20);

        });
        assertNull(grid.get(0,3));
    }

    @Test
    void rules_elvire_k_adapted() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);

        var t7 = new Tile(BLUE, ROUND);
        grid.add(45, 46, t7);

        var t8 = new Tile(GREEN, DIAMOND);
        var t9 = new Tile(GREEN, PLUS);
        grid.add(44, 44, UP, t8, t9);

        var t10 = new Tile(GREEN, ROUND);
        var t11 = new Tile(GREEN, STAR);
        grid.add(new TileAtPosition(45, 44, t10), new TileAtPosition(42, 44, t11));

        var t12 = new Tile(RED, SQUARE);
        var t13 = new Tile(ORANGE, SQUARE);
        grid.add(new TileAtPosition(46, 48, t13), new TileAtPosition(47, 48, t12));

        var t14 = new Tile(YELLOW, STAR);
        var t15 = new Tile(ORANGE, STAR);
        grid.add(42, 43, LEFT, t14, t15);

        var t16 = new Tile(ORANGE, CROSS);
        var t17 = new Tile(ORANGE, DIAMOND);
        grid.add(43, 42, DOWN, t16, t17);

        var t18 = new Tile(YELLOW, DIAMOND);
        var t19 = new Tile(YELLOW, ROUND);
        grid.add(44, 43, DOWN, t18, t19);
        var t20 = new Tile(RED, STAR);
        grid.add(42, 45, t20);
        var t21 = new Tile(BLUE, CROSS);
        var t22 = new Tile(RED, CROSS);
        var t23 = new Tile(ORANGE, CROSS);
        grid.add(47, 46, LEFT, t21, t22, t23);
        assertEquals(t21, get(grid, 2, 1));
        assertEquals(t22, get(grid, 2, 0));
        assertEquals(t23, get(grid, 2, -1));
    }
    @Test
    void rules_elvire_k_adapted_fail() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);

        var t7 = new Tile(BLUE, ROUND);
        grid.add(45, 46, t7);

        var t8 = new Tile(GREEN, DIAMOND);
        var t9 = new Tile(GREEN, PLUS);
        grid.add(44, 44, UP, t8, t9);

        var t10 = new Tile(GREEN, ROUND);
        var t11 = new Tile(GREEN, STAR);
        grid.add(new TileAtPosition(45, 44, t10), new TileAtPosition(42, 44, t11));

        var t12 = new Tile(RED, SQUARE);
        var t13 = new Tile(ORANGE, SQUARE);
        grid.add(new TileAtPosition(46, 48, t13), new TileAtPosition(47, 48, t12));

        var t14 = new Tile(YELLOW, STAR);
        var t15 = new Tile(ORANGE, STAR);
        grid.add(42, 43, LEFT, t14, t15);

        var t16 = new Tile(ORANGE, CROSS);
        var t17 = new Tile(ORANGE, DIAMOND);
        grid.add(43, 42, DOWN, t16, t17);

        var t18 = new Tile(YELLOW, DIAMOND);
        var t19 = new Tile(YELLOW, ROUND);
        grid.add(44, 43, DOWN, t18, t19);
        var t20 = new Tile(RED, STAR);
        grid.add(42, 45, t20);
        var t21 = new Tile(BLUE, CROSS);
        var t22 = new Tile(RED, CROSS);
        var t23 = new Tile(ORANGE, CROSS);
        assertThrows(QwirkleException.class, ()->{
            grid.add(52, 52, LEFT, t21, t22, t23);
        });
        assertNull(get(grid,7, 7));
        assertNull(get(grid,7, 6));
        assertNull(get(grid,7, 5));
    }

    @Test
    void rules_vincent_l_adapted_fail() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);

        var t7 = new Tile(BLUE, ROUND);
        grid.add(45, 46, t7);

        var t8 = new Tile(GREEN, DIAMOND);
        var t9 = new Tile(GREEN, PLUS);
        grid.add(44, 44, UP, t8, t9);

        var t10 = new Tile(GREEN, ROUND);
        var t11 = new Tile(GREEN, STAR);
        grid.add(new TileAtPosition(45, 44, t10), new TileAtPosition(42, 44, t11));

        var t12 = new Tile(RED, SQUARE);
        var t13 = new Tile(ORANGE, SQUARE);
        grid.add(new TileAtPosition(46, 48, t13), new TileAtPosition(47, 48, t12));

        var t14 = new Tile(YELLOW, STAR);
        var t15 = new Tile(ORANGE, STAR);
        grid.add(42, 43, LEFT, t14, t15);

        var t16 = new Tile(ORANGE, CROSS);
        var t17 = new Tile(ORANGE, DIAMOND);
        grid.add(43, 42, DOWN, t16, t17);

        var t18 = new Tile(YELLOW, DIAMOND);
        var t19 = new Tile(YELLOW, ROUND);
        grid.add(44, 43, DOWN, t18, t19);
        var t20 = new Tile(RED, STAR);
        grid.add(42, 45, t20);
        var t21 = new Tile(BLUE, CROSS);
        var t22 = new Tile(RED, CROSS);
        var t23 = new Tile(ORANGE, CROSS);
        grid.add(47, 46, LEFT, t21, t22, t23);
        var t24 = new Tile(YELLOW, SQUARE);
        var t25 = new Tile(BLUE, SQUARE);
        grid.add(46, 49, DOWN, t24, t25);
        assertEquals(t24, get(grid, 1, 4));
        assertEquals(t25, get(grid, 2, 4));
    }
    @Test
    void rules_vincent_l_adapted() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);

        var t7 = new Tile(BLUE, ROUND);
        grid.add(45, 46, t7);

        var t8 = new Tile(GREEN, DIAMOND);
        var t9 = new Tile(GREEN, PLUS);
        grid.add(44, 44, UP, t8, t9);

        var t10 = new Tile(GREEN, ROUND);
        var t11 = new Tile(GREEN, STAR);
        grid.add(new TileAtPosition(45, 44, t10), new TileAtPosition(42, 44, t11));

        var t12 = new Tile(RED, SQUARE);
        var t13 = new Tile(ORANGE, SQUARE);
        grid.add(new TileAtPosition(46, 48, t13), new TileAtPosition(47, 48, t12));

        var t14 = new Tile(YELLOW, STAR);
        var t15 = new Tile(ORANGE, STAR);
        grid.add(42, 43, LEFT, t14, t15);

        var t16 = new Tile(ORANGE, CROSS);
        var t17 = new Tile(ORANGE, DIAMOND);
        grid.add(43, 42, DOWN, t16, t17);

        var t18 = new Tile(YELLOW, DIAMOND);
        var t19 = new Tile(YELLOW, ROUND);
        grid.add(44, 43, DOWN, t18, t19);
        var t20 = new Tile(RED, STAR);
        grid.add(42, 45, t20);
        var t21 = new Tile(BLUE, CROSS);
        var t22 = new Tile(RED, CROSS);
        var t23 = new Tile(ORANGE, CROSS);
        grid.add(47, 46, LEFT, t21, t22, t23);
        var t24 = new Tile(YELLOW, SQUARE);
        var t25 = new Tile(BLUE, SQUARE);
        assertThrows(QwirkleException.class, () -> {
            grid.add(-4, -4, DOWN, t24, t25);
        });
        assertNull(get(grid, 1, 4));
        assertNull(get(grid, 2, 4));
    }
    @Test
    void rules_vincent_l_adapted_to_fail() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);

        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(BLUE, SQUARE);
        var t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);

        var t7 = new Tile(BLUE, ROUND);
        grid.add(45, 46, t7);

        var t8 = new Tile(GREEN, DIAMOND);
        var t9 = new Tile(GREEN, PLUS);
        grid.add(44, 44, UP, t8, t9);

        var t10 = new Tile(GREEN, ROUND);
        var t11 = new Tile(GREEN, STAR);
        grid.add(new TileAtPosition(45, 44, t10), new TileAtPosition(42, 44, t11));

        var t12 = new Tile(RED, SQUARE);
        var t13 = new Tile(ORANGE, SQUARE);
        grid.add(new TileAtPosition(46, 48, t13), new TileAtPosition(47, 48, t12));

        var t14 = new Tile(YELLOW, STAR);
        var t15 = new Tile(ORANGE, STAR);
        grid.add(42, 43, LEFT, t14, t15);

        var t16 = new Tile(ORANGE, CROSS);
        var t17 = new Tile(ORANGE, DIAMOND);
        grid.add(43, 42, DOWN, t16, t17);

        var t18 = new Tile(YELLOW, DIAMOND);
        var t19 = new Tile(YELLOW, ROUND);
        grid.add(44, 43, DOWN, t18, t19);
        var t20 = new Tile(RED, STAR);
        grid.add(42, 45, t20);
        var t21 = new Tile(BLUE, CROSS);
        var t22 = new Tile(RED, CROSS);
        var t23 = new Tile(ORANGE, CROSS);
        grid.add(47, 46, LEFT, t21, t22, t23);
        var t24 = new Tile(YELLOW, SQUARE);
        var t25 = new Tile(BLUE, SQUARE);
        assertThrows(QwirkleException.class, () -> {
            grid.add(-3, -4, DOWN, t24, t25);
        });
        assertNull(get(grid, 1, 4));
        assertNull(get(grid, 2, 4));
    }


    @Test
    void firstAdd_cannot_be_called_twice () {
        Tile redcross = new Tile(RED, CROSS);
        Tile reddiamond = new Tile(RED, DIAMOND);
        grid.firstAdd(UP, redcross, reddiamond);
        assertThrows(QwirkleException.class, () -> grid.firstAdd(DOWN, redcross, reddiamond));
    }

    @Test
    void test1 () {
        var t1 = new Tile(RED, CROSS);
        var t2 = new Tile(RED, CROSS);
        assertThrows(QwirkleException.class, () -> grid.firstAdd(UP, t1, t2));
    }
    @Test
    void test2 () {
        var t1 = new Tile(BLUE, DIAMOND);
        var t2 = new Tile(BLUE, CROSS);
        grid.firstAdd(UP, t1, t2);
        assertEquals(t1, grid.get(45, 45));
        assertEquals(t2, grid.get(44, 45));
    }
    @Test
    void test_adapted_to_fail_one () {
        var t1 = new Tile(GREEN, SQUARE);
        var t2 = new Tile(GREEN, SQUARE);
        var t3 = new Tile(BLUE, STAR);
        var t4 = new Tile(RED, DIAMOND);
        assertThrows(QwirkleException.class, () -> grid.firstAdd(DOWN, t1, t2, t3, t4));
    }
    @Test
    void test_adapted_to_fail_twoo () {
        var t1 = new Tile(GREEN, SQUARE);
        var t2 = new Tile(GREEN, SQUARE);
        var t3 = new Tile(BLUE, STAR);
        assertThrows(QwirkleException.class, () -> grid.firstAdd(RIGHT, t1, t2, t3));
    }

    @Test
    void firstadd_test_not_fail() {
        var t1 = new Tile(GREEN, SQUARE);
        var t2 = new Tile(GREEN, ROUND);
        var t3 = new Tile(GREEN, STAR);
        var t4 = new Tile(GREEN, PLUS);
        var t5 = new Tile(GREEN, CROSS);
        var t6 = new Tile(GREEN, DIAMOND);
        grid.firstAdd(UP, t1, t2, t3, t4, t5, t6);
        assertEquals(t1, grid.get(45, 45));
        assertEquals(t2, grid.get(44, 45));
        assertEquals(t3, grid.get(43, 45));
        assertEquals(t4, grid.get(42, 45));
        assertEquals(t5, grid.get(41, 45));
        assertEquals(t6, grid.get(40, 45));
    }
    @Test
    void test_adapted_to_fail_Tree() {
        var t1 = new Tile(GREEN, STAR);
        var t2 = new Tile(GREEN, STAR);
        var t3 = new Tile(BLUE, STAR);
        var t4 = new Tile(RED, STAR);
        var t5 = new Tile(PURPLE, STAR);
        var t6 = new Tile(YELLOW, STAR);
        assertThrows(QwirkleException.class, ()->grid.firstAdd(UP, t1, t2, t3, t4, t5, t6));
    }


    @Test
    void firstAdd_must_be_called_first_simple () {
        Tile redcross = new Tile(RED, CROSS);
        assertThrows(QwirkleException.class, () -> add(grid, 0, 0, redcross));
    }
    @Test
    void firstAdd_Same_Shape_2() {
        Grid grid = new Grid();
        Tile t1 = new Tile(RED, ROUND);
        Tile t2 = new Tile(BLUE, ROUND);

        grid.firstAdd(UP, t1, t2);
        assertEquals(t1, grid.get(45, 45));
        assertEquals(t2, grid.get(44, 45));
    }

    @Test
    void add_TAP_Not_Linked_Short() {
        Grid g = new Grid();
        Tile t1 = new Tile(RED, ROUND);
        Tile t2 = new Tile(RED, DIAMOND);
        Tile t3 = new Tile(RED, PLUS);
        grid.firstAdd(Direction.UP, t1, t2, t3);
        assertEquals(t1, grid.get(45, 45));
        assertEquals(t2, grid.get(44, 45));
        assertEquals(t3, grid.get(43, 45));

        //2
        Tile t4 = new Tile(RED, SQUARE);
        Tile t5 = new Tile(BLUE, SQUARE);
        Tile t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);
        assertEquals(t4, grid.get(46, 45));
        assertEquals(t5, grid.get(46, 46));
        assertEquals(t6, grid.get(46, 47));

        //3
        Tile t7 = new Tile(BLUE, ROUND);
        grid.add(45, 46, t7);
        assertEquals(t6, grid.get(46, 47));

        //4
        Tile t8 = new Tile(GREEN, PLUS);
        Tile t9 = new Tile(GREEN, DIAMOND);

        grid.add(43, 44, DOWN, t8, t9);
        assertEquals(t8, grid.get(43, 44));
        assertEquals(t9, grid.get(44, 44));

        //5
        TileAtPosition t10 = new TileAtPosition(42, 34, new Tile(GREEN, STAR));
        TileAtPosition t11 = new TileAtPosition(42, 35, new Tile(GREEN, ROUND));

        assertThrows(QwirkleException.class, () -> grid.add(t10, t11));

        assertNull(grid.get(t10.row(), t10.col()));
        assertNull(grid.get(t11.row(), t11.col()));
    }
    @Test
    void firstAdd_Grid_Not_Empty() {
        Grid grid = new Grid();
        Tile t1 = new Tile(RED, ROUND);
        Tile t2 = new Tile(RED, ROUND);

        grid.firstAdd(UP, t1);

        assertThrows(QwirkleException.class, () -> {
            grid.firstAdd(UP, t2);
        });
    }

    @Test
    @DisplayName("get outside the grid should return null, not throw")
    void can_get_tile_outside_virtual_grid () {
        var g = new Grid();
        assertDoesNotThrow(() -> get(g, -250, 500));
        assertNull(get(g, -250, 500));
    }
    @Test
    @DisplayName ("Complete a line leaving holes during internediary steps")
    void canCompleteAline_Left_Middle_Right() {
        var g = new Grid();
        Tile TILE_RED_CROSS = new Tile(RED,CROSS);
        Tile TILE_RED_PLUS = new Tile(RED,PLUS);
        Tile TILE_RED_DIAMOND = new Tile(RED,DIAMOND);
        g. firstAdd (RIGHT, TILE_RED_CROSS,TILE_RED_PLUS,TILE_RED_DIAMOND) ;

        Tile TILE_GREEN_CROSS = new Tile(GREEN,CROSS);
        Tile TILE_YELLOW_CROSS = new Tile(YELLOW,CROSS);
        Tile TILE_GREEN_DIAMOND = new Tile(GREEN,DIAMOND);
        Tile TILE_YELLOM_DIAMOND = new Tile(YELLOW,DIAMOND);

        add(g, 1, 0, TILE_GREEN_CROSS);
        add(g, 2, 0, TILE_YELLOW_CROSS);
        add(g, 1, 2, TILE_GREEN_DIAMOND);
        add(g, 2, 2, TILE_YELLOM_DIAMOND);
        Tile TILE_YELLOM_PLUS = new Tile(YELLOW,PLUS);
        Tile TILE_YELLOM_ROUND = new Tile(YELLOW,ROUND);
        Tile TILE_YELLOW_STAR = new Tile(YELLOW,STAR);
        TileAtPosition plus_left = createTileAtpos (2, -1, TILE_YELLOM_PLUS);
        TileAtPosition round_center = createTileAtpos (2, 1, TILE_YELLOM_ROUND);
        TileAtPosition star_right = createTileAtpos(2, 3, TILE_YELLOW_STAR);
        assertDoesNotThrow(() -> {
            g.add(plus_left, star_right, round_center);// make sur having the center tile last does not throw.
        });

        assertEquals(plus_left.tile(), get(g,2,-1));
        assertEquals(round_center.tile(), get(g,2,1));
        assertEquals(star_right.tile(), get(g,2,3));

    }
    private void assertAtCorrectPosition(TileAtPosition tile) {
        assertEquals(tile.tile(), get(grid, tile.row(), tile.col()));
    }
    //-----------------------------------------------------------------
    @Test
    void add_TAP_Example_J_ADD_Good() {
        Grid grid = new Grid();

        //1
        Tile t1 = new Tile(RED, ROUND);
        Tile t2 = new Tile(RED, DIAMOND);
        Tile t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);
        assertEquals(t1, grid.get(45, 45));
        assertEquals(t2, grid.get(44, 45));
        assertEquals(t3, grid.get(43, 45));

        //2
        Tile t4 = new Tile(RED, SQUARE);
        Tile t5 = new Tile(BLUE, SQUARE);
        Tile t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);
        assertEquals(t4, grid.get(46, 45));
        assertEquals(t5, grid.get(46, 46));
        assertEquals(t6, grid.get(46, 47));

        //3
        Tile t7 = new Tile(BLUE, ROUND);
        grid.add(45, 46, t7);
        assertEquals(t6, grid.get(46, 47));

        //4
        Tile t8 = new Tile(GREEN, PLUS);
        Tile t9 = new Tile(GREEN, DIAMOND);
        grid.add(43, 44, DOWN, t8, t9);
        assertEquals(t8, grid.get(43, 44));
        assertEquals(t9, grid.get(44, 44));

        //5
        TileAtPosition t10 = new TileAtPosition(42, 44, new Tile(GREEN, STAR));
        TileAtPosition t11 = new TileAtPosition(45, 44, new Tile(GREEN, ROUND));
        grid.add(t10, t11);
        assertEquals(grid.get(t10.row(), t10.col()), t10.tile());
        assertEquals(grid.get(t11.row(), t11.col()), t11.tile());

        //6
        Tile t12 = new Tile(ORANGE, SQUARE);
        Tile t13 = new Tile(RED, SQUARE);
        grid.add(46, 48, DOWN, t12, t13);
        assertEquals(grid.get(46, 48), t12);
        assertEquals(grid.get(47, 48), t13);

        //7
        Tile t14 = new Tile(YELLOW, STAR);
        Tile t15 = new Tile(ORANGE, STAR);
        grid.add(42, 43, LEFT, t14, t15);
        assertEquals(grid.get(42, 43), t14);
        assertEquals(grid.get(42, 42), t15);

        //8
        Tile t16 = new Tile(ORANGE, CROSS);
        Tile t17 = new Tile(ORANGE, DIAMOND);
        grid.add(43, 42, DOWN, t16, t17);
        assertEquals(grid.get(43, 42), t16);
        assertEquals(grid.get(44, 42), t17);

        //9
        Tile t18 = new Tile(YELLOW, DIAMOND);
        Tile t19 = new Tile(YELLOW, ROUND);
        grid.add(44, 43, DOWN, t18, t19);
        assertEquals(grid.get(44, 43), t18);
        assertEquals(grid.get(45, 43), t19);

        //10
        Tile t20 = new Tile(RED, STAR);
        grid.add(42, 45, t20);
        assertEquals(grid.get(42, 45), t20);
    }

    @Test
    void add_TAP_Example_J_VAR_Good() {
        Grid grid = new Grid();

        //1
        Tile t1 = new Tile(RED, ROUND);
        Tile t2 = new Tile(RED, DIAMOND);
        Tile t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);
        assertEquals(t1, grid.get(45, 45));
        assertEquals(t2, grid.get(44, 45));
        assertEquals(t3, grid.get(43, 45));

        //2
        Tile t4 = new Tile(RED, SQUARE);
        Tile t5 = new Tile(BLUE, SQUARE);
        Tile t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);
        assertEquals(t4, grid.get(46, 45));
        assertEquals(t5, grid.get(46, 46));
        assertEquals(t6, grid.get(46, 47));

        //3
        Tile t7 = new Tile(BLUE, ROUND);
        grid.add(45, 46, t7);
        assertEquals(t6, grid.get(46, 47));

        //4
        Tile t8 = new Tile(GREEN, PLUS);
        Tile t9 = new Tile(GREEN, DIAMOND);
        grid.add(43, 44, DOWN, t8, t9);
        assertEquals(t8, grid.get(43, 44));
        assertEquals(t9, grid.get(44, 44));

        //5
        TileAtPosition t10 = new TileAtPosition(42, 44, new Tile(GREEN, STAR));
        TileAtPosition t11 = new TileAtPosition(45, 44, new Tile(GREEN, ROUND));
        grid.add(t10, t11);
        assertEquals(grid.get(t10.row(), t10.col()), t10.tile());
        assertEquals(grid.get(t11.row(), t11.col()), t11.tile());

        //6
        Tile t12 = new Tile(ORANGE, SQUARE);
        Tile t13 = new Tile(RED, SQUARE);
        grid.add(46, 48, DOWN, t12, t13);
        assertEquals(grid.get(46, 48), t12);
        assertEquals(grid.get(47, 48), t13);

        //7
        Tile t14 = new Tile(YELLOW, STAR);
        Tile t15 = new Tile(ORANGE, STAR);
        grid.add(42, 43, LEFT, t14, t15);
        assertEquals(grid.get(42, 43), t14);
        assertEquals(grid.get(42, 42), t15);

        //8
        Tile t16 = new Tile(ORANGE, CROSS);
        Tile t17 = new Tile(ORANGE, DIAMOND);
        grid.add(43, 42, DOWN, t16, t17);
        assertEquals(grid.get(43, 42), t16);
        assertEquals(grid.get(44, 42), t17);

        //9
        Tile t18 = new Tile(YELLOW, DIAMOND);
        Tile t19 = new Tile(YELLOW, ROUND);
        grid.add(44, 43, DOWN, t18, t19);
        assertEquals(grid.get(44, 43), t18);
        assertEquals(grid.get(45, 43), t19);

        //10
        Tile t20 = new Tile(RED, STAR);
        grid.add(42, 45, LEFT, t20);
        assertEquals(grid.get(42, 45), t20);
    }

    @Test
    void add_TAP_Example_J_TAP_Good() {
        Grid grid = new Grid();

        //1
        Tile t1 = new Tile(RED, ROUND);
        Tile t2 = new Tile(RED, DIAMOND);
        Tile t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);
        assertEquals(t1, grid.get(45, 45));
        assertEquals(t2, grid.get(44, 45));
        assertEquals(t3, grid.get(43, 45));

        //2
        Tile t4 = new Tile(RED, SQUARE);
        Tile t5 = new Tile(BLUE, SQUARE);
        Tile t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);
        assertEquals(t4, grid.get(46, 45));
        assertEquals(t5, grid.get(46, 46));
        assertEquals(t6, grid.get(46, 47));

        //3
        Tile t7 = new Tile(BLUE, ROUND);
        grid.add(45, 46, t7);
        assertEquals(t6, grid.get(46, 47));

        //4
        Tile t8 = new Tile(GREEN, PLUS);
        Tile t9 = new Tile(GREEN, DIAMOND);
        grid.add(43, 44, DOWN, t8, t9);
        assertEquals(t8, grid.get(43, 44));
        assertEquals(t9, grid.get(44, 44));

        //5
        TileAtPosition t10 = new TileAtPosition(42, 44, new Tile(GREEN, STAR));
        TileAtPosition t11 = new TileAtPosition(45, 44, new Tile(GREEN, ROUND));
        grid.add(t10, t11);
        assertEquals(grid.get(t10.row(), t10.col()), t10.tile());
        assertEquals(grid.get(t11.row(), t11.col()), t11.tile());

        //6
        Tile t12 = new Tile(ORANGE, SQUARE);
        Tile t13 = new Tile(RED, SQUARE);
        grid.add(46, 48, DOWN, t12, t13);
        assertEquals(grid.get(46, 48), t12);
        assertEquals(grid.get(47, 48), t13);

        //7
        Tile t14 = new Tile(YELLOW, STAR);
        Tile t15 = new Tile(ORANGE, STAR);
        grid.add(42, 43, LEFT, t14, t15);
        assertEquals(grid.get(42, 43), t14);
        assertEquals(grid.get(42, 42), t15);

        //8
        Tile t16 = new Tile(ORANGE, CROSS);
        Tile t17 = new Tile(ORANGE, DIAMOND);
        grid.add(43, 42, DOWN, t16, t17);
        assertEquals(grid.get(43, 42), t16);
        assertEquals(grid.get(44, 42), t17);

        //9
        Tile t18 = new Tile(YELLOW, DIAMOND);
        Tile t19 = new Tile(YELLOW, ROUND);
        grid.add(44, 43, DOWN, t18, t19);
        assertEquals(grid.get(44, 43), t18);
        assertEquals(grid.get(45, 43), t19);

        //10
        TileAtPosition t20 = new TileAtPosition(42, 45, new Tile(RED, STAR));
        grid.add(t20);
        assertEquals(grid.get(t20.row(), t20.col()), t20.tile());
    }

    @Test
    void add_TAP_Example_J_ADD_Bad() {
        Grid grid = new Grid();

        //1
        Tile t1 = new Tile(RED, ROUND);
        Tile t2 = new Tile(RED, DIAMOND);
        Tile t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);
        assertEquals(t1, grid.get(45, 45));
        assertEquals(t2, grid.get(44, 45));
        assertEquals(t3, grid.get(43, 45));

        //2
        Tile t4 = new Tile(RED, SQUARE);
        Tile t5 = new Tile(BLUE, SQUARE);
        Tile t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);
        assertEquals(t4, grid.get(46, 45));
        assertEquals(t5, grid.get(46, 46));
        assertEquals(t6, grid.get(46, 47));

        //3
        Tile t7 = new Tile(BLUE, ROUND);
        grid.add(45, 46, t7);
        assertEquals(t6, grid.get(46, 47));

        //4
        Tile t8 = new Tile(GREEN, PLUS);
        Tile t9 = new Tile(GREEN, DIAMOND);
        grid.add(43, 44, DOWN, t8, t9);
        assertEquals(t8, grid.get(43, 44));
        assertEquals(t9, grid.get(44, 44));

        //5
        TileAtPosition t10 = new TileAtPosition(42, 44, new Tile(GREEN, STAR));
        TileAtPosition t11 = new TileAtPosition(45, 44, new Tile(GREEN, ROUND));
        grid.add(t10, t11);
        assertEquals(grid.get(t10.row(), t10.col()), t10.tile());
        assertEquals(grid.get(t11.row(), t11.col()), t11.tile());

        //6
        Tile t12 = new Tile(ORANGE, SQUARE);
        Tile t13 = new Tile(RED, SQUARE);
        grid.add(46, 48, DOWN, t12, t13);
        assertEquals(grid.get(46, 48), t12);
        assertEquals(grid.get(47, 48), t13);

        //7
        Tile t14 = new Tile(YELLOW, STAR);
        Tile t15 = new Tile(ORANGE, STAR);
        grid.add(42, 43, LEFT, t14, t15);
        assertEquals(grid.get(42, 43), t14);
        assertEquals(grid.get(42, 42), t15);

        //8
        Tile t16 = new Tile(ORANGE, CROSS);
        Tile t17 = new Tile(ORANGE, DIAMOND);
        grid.add(43, 42, DOWN, t16, t17);
        assertEquals(grid.get(43, 42), t16);
        assertEquals(grid.get(44, 42), t17);

        //9
        Tile t18 = new Tile(YELLOW, DIAMOND);
        Tile t19 = new Tile(YELLOW, ROUND);
        grid.add(44, 43, DOWN, t18, t19);
        assertEquals(grid.get(44, 43), t18);
        assertEquals(grid.get(45, 43), t19);

        //10
        Tile t20 = new Tile(GREEN, STAR);
        assertThrows(QwirkleException.class, () -> grid.add(42, 45, t20));
        assertNull(grid.get(42, 45));
    }

    @Test
    void add_TAP_Example_J_VAR_Bad() {
        Grid grid = new Grid();

        //1
        Tile t1 = new Tile(RED, ROUND);
        Tile t2 = new Tile(RED, DIAMOND);
        Tile t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);
        assertEquals(t1, grid.get(45, 45));
        assertEquals(t2, grid.get(44, 45));
        assertEquals(t3, grid.get(43, 45));

        //2
        Tile t4 = new Tile(RED, SQUARE);
        Tile t5 = new Tile(BLUE, SQUARE);
        Tile t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);
        assertEquals(t4, grid.get(46, 45));
        assertEquals(t5, grid.get(46, 46));
        assertEquals(t6, grid.get(46, 47));

        //3
        Tile t7 = new Tile(BLUE, ROUND);
        grid.add(45, 46, t7);
        assertEquals(t6, grid.get(46, 47));

        //4
        Tile t8 = new Tile(GREEN, PLUS);
        Tile t9 = new Tile(GREEN, DIAMOND);
        grid.add(43, 44, DOWN, t8, t9);
        assertEquals(t8, grid.get(43, 44));
        assertEquals(t9, grid.get(44, 44));

        //5
        TileAtPosition t10 = new TileAtPosition(42, 44, new Tile(GREEN, STAR));
        TileAtPosition t11 = new TileAtPosition(45, 44, new Tile(GREEN, ROUND));
        grid.add(t10, t11);
        assertEquals(grid.get(t10.row(), t10.col()), t10.tile());
        assertEquals(grid.get(t11.row(), t11.col()), t11.tile());

        //6
        Tile t12 = new Tile(ORANGE, SQUARE);
        Tile t13 = new Tile(RED, SQUARE);
        grid.add(46, 48, DOWN, t12, t13);
        assertEquals(grid.get(46, 48), t12);
        assertEquals(grid.get(47, 48), t13);

        //7
        Tile t14 = new Tile(YELLOW, STAR);
        Tile t15 = new Tile(ORANGE, STAR);
        grid.add(42, 43, LEFT, t14, t15);
        assertEquals(grid.get(42, 43), t14);
        assertEquals(grid.get(42, 42), t15);

        //8
        Tile t16 = new Tile(ORANGE, CROSS);
        Tile t17 = new Tile(ORANGE, DIAMOND);
        grid.add(43, 42, DOWN, t16, t17);
        assertEquals(grid.get(43, 42), t16);
        assertEquals(grid.get(44, 42), t17);

        //9
        Tile t18 = new Tile(YELLOW, DIAMOND);
        Tile t19 = new Tile(YELLOW, ROUND);
        grid.add(44, 43, DOWN, t18, t19);
        assertEquals(grid.get(44, 43), t18);
        assertEquals(grid.get(45, 43), t19);

        //10
        Tile t20 = new Tile(ORANGE, STAR);
        assertThrows(QwirkleException.class, () -> grid.add(42, 45, LEFT, t20));
        assertNull(grid.get(42, 45));
    }

    @Test
    void add_TAP_Example_J_TAP_Bad() {
        Grid grid = new Grid();

        //1
        Tile t1 = new Tile(RED, ROUND);
        Tile t2 = new Tile(RED, DIAMOND);
        Tile t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);
        assertEquals(t1, grid.get(45, 45));
        assertEquals(t2, grid.get(44, 45));
        assertEquals(t3, grid.get(43, 45));

        //2
        Tile t4 = new Tile(RED, SQUARE);
        Tile t5 = new Tile(BLUE, SQUARE);
        Tile t6 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t4, t5, t6);
        assertEquals(t4, grid.get(46, 45));
        assertEquals(t5, grid.get(46, 46));
        assertEquals(t6, grid.get(46, 47));

        //3
        Tile t7 = new Tile(BLUE, ROUND);
        grid.add(45, 46, t7);
        assertEquals(t6, grid.get(46, 47));

        //4
        Tile t8 = new Tile(GREEN, PLUS);
        Tile t9 = new Tile(GREEN, DIAMOND);
        grid.add(43, 44, DOWN, t8, t9);
        assertEquals(t8, grid.get(43, 44));
        assertEquals(t9, grid.get(44, 44));

        //5
        TileAtPosition t10 = new TileAtPosition(42, 44, new Tile(GREEN, STAR));
        TileAtPosition t11 = new TileAtPosition(45, 44, new Tile(GREEN, ROUND));
        grid.add(t10, t11);
        assertEquals(grid.get(t10.row(), t10.col()), t10.tile());
        assertEquals(grid.get(t11.row(), t11.col()), t11.tile());

        //6
        Tile t12 = new Tile(ORANGE, SQUARE);
        Tile t13 = new Tile(RED, SQUARE);
        grid.add(46, 48, DOWN, t12, t13);
        assertEquals(grid.get(46, 48), t12);
        assertEquals(grid.get(47, 48), t13);

        //7
        Tile t14 = new Tile(YELLOW, STAR);
        Tile t15 = new Tile(ORANGE, STAR);
        grid.add(42, 43, LEFT, t14, t15);
        assertEquals(grid.get(42, 43), t14);
        assertEquals(grid.get(42, 42), t15);

        //8
        Tile t16 = new Tile(ORANGE, CROSS);
        Tile t17 = new Tile(ORANGE, DIAMOND);
        grid.add(43, 42, DOWN, t16, t17);
        assertEquals(grid.get(43, 42), t16);
        assertEquals(grid.get(44, 42), t17);

        //9
        Tile t18 = new Tile(YELLOW, DIAMOND);
        Tile t19 = new Tile(YELLOW, ROUND);
        grid.add(44, 43, DOWN, t18, t19);
        assertEquals(grid.get(44, 43), t18);
        assertEquals(grid.get(45, 43), t19);

        //10
        TileAtPosition t20 = new TileAtPosition(42, 45, new Tile(RED, ROUND));
        assertThrows(QwirkleException.class, () -> grid.add(t20));
        assertNull(grid.get(t20.row(), t20.col()));
    }


    @Test
    void testScore(){
        Grid grid = new Grid();
        var gr = new GridView(grid);
        //1
        Tile t1 = new Tile(RED, ROUND);
        Tile t2 = new Tile(RED, DIAMOND);
        Tile t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);


       
        //2
        Tile t4 = new Tile(RED, SQUARE);
        Tile t5 = new Tile(BLUE, SQUARE);
        Tile t6 = new Tile(GREEN, SQUARE);
        grid.add(46, 45, RIGHT,  t4, t5,t6);

        //3
        Tile t7 = new Tile(BLUE, ROUND);

        //4 espere avoir 6
        //ce sont cens etre des tileAtPosition
        Tile t8 = new Tile(GREEN, PLUS);
        Tile t9 = new Tile(GREEN, DIAMOND);
        /*int res =grid.add(43, 44,DOWN,  t8, t9);
        assertEquals(6,res);*/
        grid.add(43, 44,DOWN,  t8, t9);

        //5


    }



}