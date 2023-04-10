package g58183.qwirkle.view;
import g58183.qwirkle.model.Grid;
import g58183.qwirkle.model.Game;
import g58183.qwirkle.model.Player;
import g58183.qwirkle.model.GridView;

public class View {


    public static void display(GridView grid){

        int[][] table = new int[91][91];
        System.out.println();
        for (int i = 0; i < 91; i++) {
            System.out.print(String.format("%2d ", i));
            for (int j = 0; j < 90; j++) {
                System.out.print(String.format("%3d", table[i][j]));
            }
            System.out.print("  ");

            System.out.println();

        }
        for (int j =0 ; j < 90; j++) {
            System.out.print(String.format("%3d", j));
        }


    }
    public static void display(Player player){
        System.out.println(player.getNom());
    }
    public static void displayHelp(){
        String h ="Qwirkle command:\n"
        +"play 1 tile : o <row> <col> <i>\n"
        +" play line: l <row> <col> <direction> <i1> [<i2>]\n"
        +"- play plic-ploc : m <row1> <col1> <i1> [<row2> <col2> <i2>]\n"
        +"- play first : f <i1> [<i2>]\n"
        +"- pass : p\n"
        +"- quit : q\n"
        +"i : index in list of tiles\n"
        +" direction in l (left), r (right), u (up), d(down)\n";
    }
    public static void displayError(String message)

}