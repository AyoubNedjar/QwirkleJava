package g58183.qwirkle.view;

import g58183.qwirkle.model.*;

public abstract class View {

    public static final String RESET = "\033[0m";  // Text Reset
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String BLUE = "\033[0;34m";
    public static final String PURPLE = "\033[0;35m";
    public static final String ORANGE = "\033[0;43m";



    public static void display(GridView grid) {
        int minX = 91;
        int maxX = 0;
        int minY = 91;
        int maxY = 0;


        for (int i = 0; i < 91; i++) {

            for (int j = 0; j < 91; j++) {
                if (grid.get(i,j) != null) {
                    minX = Math.min(minX, i);
                    maxX = Math.max(maxX, i);
                    minY = Math.min(minY, j);
                    maxY = Math.max(maxY, j);

                }

            }

        }
        for (int i = minX; i <= maxX; i++) {
            System.out.println(" ");
            for (int j = minY; j <= maxY; j++) {
                Tile tile = grid.get(i, j);
                if (tile == null) {
                    System.out.print("   ");
                } else {
                    System.out.print(tileToRepresentation(tile));
                }
            }
            System.out.println();
        }

    }

    private static String tileToRepresentation(Tile tile) {
        String color = RESET;
        switch (tile.color()) {
            case BLUE -> {
                color = BLUE;
            }
            case RED -> {
                color = RED;
            }
            case GREEN -> {
                color = GREEN;
            }
            case ORANGE -> {

                color = ORANGE;
            }
            case YELLOW -> {
                color = YELLOW;
            }
            case PURPLE -> {
                color = PURPLE;
            }
        }
        return color + toGiveShapeTile(tile) + RESET;
    }

    private static String toGiveShapeTile(Tile tile) {
        switch (tile.shape()) {

            case CROSS -> {
                return " X ";
            }
            case SQUARE -> {
                return "[ ]";
            }
            case ROUND -> {
                return " O ";
            }
            case STAR -> {
                return " * ";
            }
            case PLUS -> {
                return " + ";
            }
            case DIAMOND -> {
                return "< >";
            }
        }
        return " ";
    }

    public static void display(Player player) {
        System.out.println(player.getNom());
    }

    public static void displayHelp() {
        String h = "Qwirkle command:\n"
                + "play 1 tile : o <row> <col> <i>\n"
                + " play line: l <row> <col> <direction> <i1> [<i2>]\n"
                + "- play plic-ploc : m <row1> <col1> <i1> [<row2> <col2> <i2>]\n"
                + "- play first : f <i1> [<i2>]\n"
                + "- pass : p\n"
                + "- quit : q\n"
                + "i : index in list of tiles\n"
                + " direction in l (left), r (right), u (up), d(down)\n";
    }

    public static void displayError(String message) {
    }

}