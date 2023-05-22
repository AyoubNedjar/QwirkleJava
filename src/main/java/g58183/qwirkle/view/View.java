package g58183.qwirkle.view;

import static java.lang.System.out;

import g58183.qwirkle.model.*;

import java.util.List;

public class View {

    public static final String RESET = "\033[0m";

    public static final String YELLOW_BOLD = "\033[1;33m";


    /**
     * This method displays the game grid in a rectangular form with a border around it.
     * <p>
     * It finds the minimum and maximum coordinates for the tiles on the grid to determine
     * the size of the grid to be displayed. Then it prints the tiles in the grid with the
     * correct colors and shapes.
     *
     * @param grid the game grid to be displayed
     */
    public static void display(GridView grid) {

        int minX = grid.getsize();
        int maxX = 0;
        int minY = grid.getsize();
        int maxY = 0;


        for (int i = 0; i < 91; i++) {

            for (int j = 0; j < 91; j++) {
                if (grid.get(i, j) != null) {
                    minX = Math.min(minX, i);
                    maxX = Math.max(maxX, i);
                    minY = Math.min(minY, j);
                    maxY = Math.max(maxY, j);

                }

            }

        }
        System.out.println("+-----------------------------+");
        System.out.println("|         " + Color.ORANGE + "Qwirkles" + View.RESET + "            |");
        System.out.println("+-----------------------------+");
        for (int i = minX; i <= maxX; i++) {
            out.print("|");
            System.out.print(i + " ");
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
        System.out.print("    ");
        for (int i = minY; i <= maxY; i++) {
            System.out.print("" + i + " ");

        }
        System.out.println();
        System.out.println("-------------------------------");

    }

    /**
     * This method converts a tile to a string representation with the correct color and shape.
     *
     * @param tile the tile to be converted
     * @return a string representation of the tile
     */
    private static String tileToRepresentation(Tile tile) {

        return tile.color().toString() + toGiveShapeTile(tile) + RESET;
    }

    /**
     * This method converts a tile's shape to a string representation.
     *
     * @param tile the tile to be converted
     * @return a string representation of the tile's shape
     */
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


    /**
     * This method displays a player's hand with the tiles' colors and shapes.
     *
     * @param nameplayer the name of the player
     * @param playerHand the hand of the player
     */
    public static void display(String nameplayer, List<Tile> playerHand, int score) {
        System.out.println();
        System.out.println(Color.GREEN+"C'est au tour du joueur "+nameplayer+View.RESET);
        System.out.print("Main { : ");
        for (Tile tile : playerHand) {
            out.print(tileToRepresentation(tile));
        }
        out.println();
        out.print("Index  :  ");

        for (int i = 1; i < 7; i++) {
            System.out.print(i + "  ");
        }
        out.println();
        out.print("Votre score actuel est de : "+ score);
        out.println();

    }


    public static void displayHelp() {

        String text = String.format(
                Color.BLUE+"Qwirkle command: choisissez ce que vous voulez jouer"+View.RESET+" :\n"
                        + " -play 1 tile : "+Color.YELLOW+"o <row> <col> <i>"+View.RESET+"\n"
                        + " -play line: "+Color.YELLOW+"l <row> <col> <direction> <i1> [<i2>]"+View.RESET+"\n"
                        + " -play plic-ploc : "+Color.YELLOW+" m <row1> <col1> <i1> [<row2> <col2> <i2>]"+View.RESET+"\n"
                        + " -play first : "+Color.YELLOW+"f <direction> <i1> [<i2>]"+View.RESET+"\n"
                        + " -pass : "+Color.YELLOW+"p\n"+View.RESET+""
                        + " -quit: "+Color.YELLOW+"q"+View.RESET+"\n"
                        + " -save : : "+Color.YELLOW+"s"+View.RESET+"\n"
                        + "  PS ->    i : index in list of tiles\n"
                        + "     ->    d : direction in l (left), r (right), u (up), d(down)\n"
                        + ""+Color.YELLOW+"Entrez votre choix :"+View.RESET+" : ",
                YELLOW_BOLD, RESET, YELLOW_BOLD, RESET, YELLOW_BOLD, RESET,
                YELLOW_BOLD, RESET, YELLOW_BOLD, RESET, YELLOW_BOLD, RESET
        );

        out.print(text);
    }

    /**

     Displays an error message on the console.
     @param message The error message to display.
     */
    public static void displayError(String message) {
        System.out.println(message);
    }
    /**

     Displays the game title on the console.
     */
    public static void displayTitle(){
        System.out.println("+--------------------------------------+");
        System.out.println("|                                      |");
        System.out.println("|      Welcome to the game of          |");
        System.out.println("|                                      |");
        System.out.println("|            "+Color.ORANGE+"Qwirkles"+View.RESET+"                  |");
        System.out.println("|                                      |");
        System.out.println("|      "+Color.BLUE+"A game of strategy and"+View.RESET+"          |");
        System.out.println("|      "+Color.BLUE+"matching that will test"+View.RESET+"         |");
        System.out.println("|        "+Color.BLUE+"your skills and wit."+View.RESET+"          |");
        System.out.println("|                                      |");
        System.out.println("+--------------------------------------+");
        System.out.println();

    }


}