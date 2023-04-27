package g58183.qwirkle;

import g58183.qwirkle.model.Direction;
import g58183.qwirkle.model.Game;
import g58183.qwirkle.view.View;
import g58183.qwirkle.model.*;

import java.util.*;

public class App {
    private static Game game;
    private static View view;

    public static void main(String[] args) {
      /*  int [] a = picpoc();
        for (int ab:a) {
            System.out.println(ab);
        }*/
        Scanner clavier = new Scanner(System.in);
        int nb = 0;
        System.out.println("Bienvenue dans le jeu ");
        System.out.println("Combien de joueurs vont jouer ? : ");
        nb = robuste(2, 4, "");

        game = new Game(askName(nb));//demande de noms
        game.setCurrent(getRandomPlayer(nb));//met current a un indice random;
        View.display(game.getCurrentPlayer().getNom(), game.getCurrentPlayerHand());
        System.out.println("c'est le tour de " + game.getCurrentPlayerName());
        game.first(chooseDirection(), choosTiles());
        View.display(game.getGrid());

        boolean isGameOver = false;
        while(!isGameOver) {

                boolean ok = true;
                while (ok){
                    try {
                        View.display(game.getCurrentPlayer().getNom(), game.getCurrentPlayerHand());//affiche la main du joueur courant
                        char choice = askMethod();//demande la methode choisie en vérifiant bien les commandes entrées.
                        isGameOver = playChoice(choice, isGameOver);
                        View.display(game.getGrid());
                        ok = false;

                    } catch (QwirkleException e) {
                        view.displayError(e.getMessage());
                        e.printStackTrace();

                    }finally {
                        view.displayError("Coup invalide rééssayer!");
                        View.display(game.getGrid());
                    }
                }

            }
        System.out.println("Merci et au revoir");
        }



    private static char askMethod(){
       char choice = robuste2();
       return choice;
    }


    private static boolean playChoice(char choice, boolean isGameOver ){
        do {
            switch (choice){
                case 'o' -> {//joue une seule tuile
                    View.display(game.getGrid());
                    View.display(game.getCurrentPlayer().getNom(), game.getCurrentPlayerHand());
                    System.out.println("Vous allez jouer une seule tuile.");
                    int indexTile  = chooseOneTile();
                    game.play(robuste(0, 90,"Choix de la ligne : "), robuste(0, 90, "Choix de la colonne : "),indexTile);
                    return isGameOver;
                }
                case 'l' -> {//joue plusieurs tuiles a la suite des autres
                    View.display(game.getGrid());
                    View.display(game.getCurrentPlayer().getNom(), game.getCurrentPlayerHand());
                    System.out.println("Vous allez jouer plusieurs tuiles qui vont se suivre.");
                    int [] indexTiles  = choosTiles();
                    Direction d = chooseDirection();
                    game.play(robuste(0, 90,"Choix de la ligne : "), robuste(0, 90,"Choix de la colonne : "),d, indexTiles);
                    return isGameOver;
                }
                case 'm' ->{//joue plusieurs ou une seule tuile dans des positions quelquonques
                    View.display(game.getGrid());
                    View.display(game.getCurrentPlayer().getNom(), game.getCurrentPlayerHand());
                    game.play(picpoc());
                    return isGameOver;
                }
                case 'f' ->{//joue plusieurs ou une seule tuile dans des positions quelquonques
                    View.display(game.getCurrentPlayer().getNom(), game.getCurrentPlayerHand());
                    game.first(chooseDirection(), choosTiles());
                    return isGameOver;
                }
                case 'p' ->{//joue plusieurs ou une seule tuile dans des positions quelquonques
                    System.out.println("La prochaine fois peut être hahah!!!");
                    game.pass();
                    return isGameOver;
                }
                case 'q' -> {
                    isGameOver = true;
                    return isGameOver;
                }
                case 'h' -> {
                    View.display(game.getGrid());
                }
            }
        }while (true);

    }

    private static int robuste(int a, int b, String message){
            boolean ok = true;
            int nb = -1;
            do {
                try {
                    if(!message.equals("")){
                        System.out.print(message);
                    }

                    Scanner clavier = new Scanner(System.in);
                    nb = clavier.nextInt();
                    if (nb < a || nb > b) {
                        System.out.println("entrez un nombre entre "+a+" et "+b+" svp : ");
                    } else {
                        ok = false;
                    }

                } catch (InputMismatchException e) {
                    System.out.println("veuillez entrez un numéro svp");
                }
            } while (ok);
            return nb;
    }
    private static char robuste2(){
        String choice = "";

        do {
            //View.displayHelp();
            System.out.print("Si vous avez besoin d'aide pour les commandes tapez 'h', sinon tapez le commande de votre choix  : ");
            Scanner clavier = new Scanner(System.in);
            choice = clavier.nextLine();
            choice = choice.toLowerCase(Locale.ROOT);

            if (game.getGrid().isEmpty()){
                if (choice.charAt(0)=='f'){
                    return 'f';
                }else{
                    throw new QwirkleException("Le plateau est vide , tapez 'f' pour commencer !");
                }
            }
            switch (choice.charAt(0)) {
                case 'o' -> {
                    return 'o';
                }
                case 'l' -> {
                    return 'l';
                }
                case 'm' -> {
                    return 'm';
                }
                case 'f' -> {
                    System.out.println("Les premières tuiles ont déja été jouées");
                    View.display(game.getGrid());
                    View.display(game.getCurrentPlayerName(),game.getCurrentPlayerHand());
                }
                case 'p' -> {
                    return 'p';
                }
                case 'q' -> {
                    return 'q';
                }
                case 'h' -> {
                    View.displayHelp();
                }
                default -> {
                    System.out.println("Entrez une commande valide svp :");
                    View.display(game.getGrid());
                }

            }

        } while (true);

    }

    private static int[] picpoc(){
        int []tab = new int[robuste(1,6,"Combien de tuiles voulez-vous jouez ? : ")*3];
        int i = 0;
        int row =0;
        int col = 0;
        int iTile = 0;
        int cpt = 1;
        do {

                iTile = robuste(1,6,"Entrez la numéro de la tuile numéro "+cpt+" dont vous voulez jouez : ")-1;
                row = robuste(0,90,"Entrez la ligne de la tuile numéro"+cpt+" : ");
                col = robuste(0,90,"Entrez la colonne de la tuile numéro"+cpt+" : ");
                tab[i] = row;
                tab[i+1]= col;
                tab[i+2] = iTile;
                cpt++;
                i = i+3;
        }while (i< tab.length);

        return tab;
    }
    private  static  int chooseOneTile(){
       return robuste(1, 6, "Entrez la numéro de la tuile dont vous voulez jouez")-1;//pour accéder aux indices

    }
    private static int[] choosTiles() {
        System.out.println("Choisissez vos tuiles : ");
        int nb;
        boolean ok = true;
        List<Integer> maliste = new ArrayList<>();
        int i = 0;
        do {
            try {
                System.out.println("choix de tuile numéro " + (i + 1));
                Scanner clavier = new Scanner(System.in);
                nb = clavier.nextInt();
                if (nb > 6 || nb < 1) {
                    System.out.println("entrez un nombre entre 1 et 6 svp");
                } else {
                    if (maliste.contains(nb-1)) {
                        System.out.println("cette tuile est déja choisi , essayer une autre");
                    } else {
                        maliste.add(nb-1);//pour accéder aux indices de la main
                        i++;
                        String a = "";
                        do{
                            System.out.println("Voulez vous entrez une nouvelle piece ? tapez '+' , sinon tapez N");
                            clavier = new Scanner(System.in);
                            a = clavier.nextLine();

                        }while (!a.contains("N") && !a.contains("+"));

                        if(a.contains("N")){
                            ok = false;
                        }

                    }

                }

            } catch (InputMismatchException e) {
                System.out.println("veuillez entrez un numéro svp");
            }
        } while (ok && i < 6);

        return  maliste.stream().mapToInt(x->x.intValue()).toArray();
        //
    }

    private static Direction chooseDirection() {
        System.out.println("Choisissez la Direction à prendre : ");
        String d = "";
        boolean ok = true;
        int i = 0;
        do {
                System.out.println("Tapez r pour Droite, d pour Bas, u pour Haut, l pour gauche");
                Scanner clavier = new Scanner(System.in);
                d = clavier.nextLine();
                d = d.toLowerCase(Locale.ROOT);

                switch (d.charAt(0)) {
                    case 'r' -> {
                        return Direction.RIGHT;
                    }
                    case 'l' -> {
                        return Direction.LEFT;
                    }
                    case 'u' -> {
                        return Direction.UP;
                    }
                    case 'd' -> {
                        return Direction.DOWN;
                    }
                    default -> System.out.println("Entrez une direction valide svp :");
                }
            }while (true) ;

        }


        private static int getRandomPlayer ( int nb){
            int i = (int) (Math.random() * (nb - 1));
            return i;
        }

        private static List<String> askName ( int nb){

            List<String> names = new ArrayList<>();
            Scanner clavier = new Scanner(System.in);
            System.out.println("Saisies des noms");

            for (int i = 0; i < nb; i++) {
                System.out.println("entrez le nom du joueur numéro " + (i + 1));
                String noms = clavier.nextLine();
                names.add(noms);
            }
            // names.stream().forEach(System.out::println);

            return names;
        }
    }
