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

        int nb = 0;
        View.displayTitle();
        System.out.println("Combien de joueurs vont jouer ? : ");
        nb = robuste(2, 4, "");

        try {
            game = new Game(askName(nb));//demande de noms
            game.setCurrent(getRandomPlayer(nb));//met current a un indice random;
            View.display(game.getCurrentPlayer().getNom(), game.getCurrentPlayerHand());
            System.out.println("C'est le tour de " + game.getCurrentPlayerName());
            game.first(chooseDirection(), chooseTiles());
            view.display(game.getGrid());

        }catch(QwirkleException e){
            System.out.println(View.ORANGE+e.getMessage()+View.RESET);
            view.display(game.getGrid());
        }

        boolean replay = true;
        boolean isGameOver = false;
        while (replay) {
            while (!isGameOver) {

                boolean ok = true;
                while (ok) {
                    try {//TODO vérifier juste pour la methode de choix que la taille de clavier == 1
                        View.display(game.getCurrentPlayer().getNom(), game.getCurrentPlayerHand());//affiche la main du joueur courant
                        char choice = askMethod();//demande la methode choisie en vérifiant bien les commandes entrées.
                        isGameOver = playChoice(choice, isGameOver);
                        View.display(game.getGrid());
                        ok = false;

                    } catch (QwirkleException e) {
                        //view.displayError(View.RED+e.getMessage()+View.RESET);
                        view.displayError(View.RED+"Coup invalide rééssayer!"+View.RESET);
                        View.display(game.getGrid());
                    } finally {

                    }
                }

            }
                isGameOver =  replayOrNot(isGameOver);
                if(isGameOver){
                    replay = false;
                }

        }
        System.out.println(View.GREEN+"Merci et au revoir");
    }


    private static char askMethod() {
        return robusteChar();
    }


    private static  boolean replayOrNot(boolean isGameOver){
        String choice  ;
        if(isGameOver)
            do {
                System.out.println("Entrez 'y' pour recommencer, sinon 'n' pour quitter définitivement");
                Scanner clavier = new Scanner(System.in);
                choice = clavier.nextLine();
                choice = choice.toLowerCase();

                switch (choice.charAt(0)){
                    case 'y' ->{
                        return false;
                    }
                    case 'n' ->{
                        return true;
                    }
                }

            }while (true);
        return false;
    }
    private static boolean playChoice(char choice, boolean isGameOver) {
        do {
                switch (choice) {
                    //joue plusieurs tuiles a la suite des autres
                    //joue plusieurs ou une seule tuile dans des positions quelquonques
                    case 'o', 'l', 'm', 'f' -> {
                        executeChooice(choice);
                        return isGameOver;
                    }
                    case 'p' -> {
                        System.out.println(View.ORANGE+"Vous avez passé votre tour"+View.RESET);
                        game.pass();
                        return isGameOver;
                    }
                    case 'q' -> {
                        isGameOver = true;
                        return isGameOver;
                    }
                }

        } while (true);

    }

    private static void executeChooice(char choice){
        View.display(game.getGrid());
        View.display(game.getCurrentPlayer().getNom(), game.getCurrentPlayerHand());

        switch (choice) {
            case 'o' -> {//joue une seule tuile
                System.out.println(View.GREEN+"Vous allez jouer une seule tuile."+View.RESET);
                int indexTile = chooseOneTile();
                game.play(robuste(0, 90, "Choix de la ligne : "), robuste(0, 90, "Choix de la colonne : "), indexTile);
            }
            case 'l' -> {//joue plusieurs tuiles a la suite des autres
                System.out.println(View.GREEN+"Vous allez jouer plusieurs tuiles qui vont se suivre."+View.RESET);
                int[] indexTiles = chooseTiles();
                Direction d = chooseDirection();
                game.play(robuste(0, 90, "Choix de la ligne : "), robuste(0, 90, "Choix de la colonne : "), d, indexTiles);
            }
            case 'm' -> {//joue plusieurs ou une seule tuile dans des positions quelquonques
                System.out.println(View.GREEN+"Vous allez jouer plusieurs ou une seule tuile dans des positions quelquonques"+View.RESET);
                game.play(picpoc());
            }
            case 'f' -> {//joue le premier coup
                game.first(chooseDirection(), chooseTiles());
            }
        }
    }

    private static int robuste(int a, int b, String message) {
        boolean ok = true;
        int number = -1;
        Scanner clavier = new Scanner(System.in);;
        do {
            //try {
                if (!message.equals("")) {
                    System.out.print(message);
                }

                 clavier = new Scanner(System.in);
                number = clavier.nextInt();
                String n = String.valueOf(number);
                if (number < a || number > b) {
                    System.out.println(View.ORANGE+"entrez un nombre entre " + a + " et " + b + " svp : "+View.RESET);
                } else {
                    checkName(n);
                    ok = false;
                }

            /*} catch (InputMismatchException e) {
                System.out.println(View.ORANGE+"veuillez entrez un numéro svp"+View.RESET);
            }catch (QwirkleException e){
                System.out.println(View.ORANGE+"Vous avez rentré un espace , réésayez ! "+View.RESET);

            }*/
        } while (ok);
        return number;
    }

    private static char robusteChar() {
        String choice ;

        do {
            System.out.print("Si vous avez besoin d'aide pour les commandes tapez 'h'," +
            " sinon tapez le commande de votre choix  : ");
            try {
                Scanner clavier = new Scanner(System.in);
                choice = clavier.nextLine();
                choice = choice.toLowerCase(Locale.ROOT);

                if (game.getGrid().isEmpty()) {
                    if (choice.charAt(0) == 'f') {
                        return 'f';
                    } else {
                        throw new QwirkleException(View.ORANGE+"Le plateau est vide , tapez "+View.YELLOW_BOLD+"'f'"+View.RESET+ "pour commencer !"+View.RESET);
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
                        System.out.println(View.ORANGE+"Les premières tuiles ont déja été jouées"+View.RESET);
                        View.display(game.getGrid());
                        View.display(game.getCurrentPlayerName(), game.getCurrentPlayerHand());
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
                        System.out.println("Entrez une commande valide svp : ");
                        View.display(game.getGrid());
                    }
                }
            }catch (StringIndexOutOfBoundsException e){
                System.out.println(View.ORANGE+"Vous avez rentré un espace , réésayez ! "+View.RESET);
            }


        } while (true);
    }

    private static int[] picpoc() {
        int[] tab = new int[robuste(1, 6, "Combien de tuiles voulez-vous jouez ? : ") * 3];
        int i = 0;
        int row ;
        int col;
        int indextile;
        int cpt = 1;
        do {

            indextile = robuste(1, 6, "Entrez la numéro de la tuile numéro " + cpt + " dont vous voulez jouez : ") - 1;
            row = robuste(0, 90, "Entrez la ligne de la tuile numéro" + cpt + " : ");
            col = robuste(0, 90, "Entrez la colonne de la tuile numéro" + cpt + " : ");
            tab[i] = row;
            tab[i + 1] = col;
            tab[i + 2] = indextile;
            cpt++;
            i = i + 3;
        } while (i < tab.length);

        return tab;
    }

    private static int chooseOneTile() {
        return robuste(1, 6, "Entrez la numéro de la tuile dont vous voulez jouez : ") - 1;//pour accéder aux indices

    }

    private static int[] chooseTiles() {
        System.out.println(View.GREEN+"Choisissez vos tuiles : "+View.RESET);
        int number;
        boolean ok = true;
        List<Integer> maliste = new ArrayList<>();
        int i = 0;
        do {
            try {
                System.out.print("Choix de tuile numéro " + (i + 1)+": ");
                Scanner clavier = new Scanner(System.in);
                number = clavier.nextInt();
                if (number > 6 || number < 1) {
                    System.out.println(View.ORANGE+"Entrez un nombre entre 1 et 6 svp :"+View.RESET);
                } else {
                    if (maliste.contains(number - 1)) {
                        System.out.println(View.ORANGE+"Cette tuile est déja choisi , essayez une autre"+View.RESET);
                    } else {
                        maliste.add(number - 1);//pour accéder aux indices de la main
                        i++;
                        String a ;
                        do {
                            System.out.print("Voulez vous entrez une nouvelle piece ? tapez '+' , sinon tapez N : ");
                            clavier = new Scanner(System.in);
                            a = clavier.nextLine();

                        } while (!a.contains("N") && !a.contains("+") && !a.contains(" "));

                        if (a.contains("N")) {
                            ok = false;
                        }

                    }

                }

            } catch (InputMismatchException e) {
                System.out.println(View.ORANGE+"Veuillez entrez un numéro svp."+View.RESET);
            }catch (StringIndexOutOfBoundsException e){
                System.out.println(View.ORANGE+"Vous avez rentré un espace , réésayez ! "+View.RESET);
            }
        } while (ok && i < 6);

        return maliste.stream().mapToInt(Integer::intValue).toArray();// qui retourne une liste de int(entier simplement)
        // et pas de Integer(classe objet)

    }

    private static Direction chooseDirection() {
        System.out.println(View.GREEN+"Choisissez la Direction à prendre : "+View.RESET);
        String d ;

        do {
            System.out.println("Tapez "+View.YELLOW_BOLD+"'r'"+View.RESET+" pour Droite, "+View.YELLOW_BOLD+"'d'"+View.RESET+ " pour Bas, "+View.YELLOW_BOLD+"'u'"+View.RESET+ " pour Haut, "+View.YELLOW_BOLD+"'l'"+View.RESET+ " pour gauche");
            Scanner clavier = new Scanner(System.in);
            d = clavier.nextLine();
            d = d.toLowerCase(Locale.ROOT);

            try{
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
                    default -> System.out.println("Entrez une direction valide svp, entrez 'h' pour de l'aide :");
                }
            }catch (StringIndexOutOfBoundsException e){
                System.out.println(View.ORANGE+"Entrez une direction valide svp : "+View.RESET);
            }

        } while (true);

    }


    private static int getRandomPlayer(int nb) {
        return (int) (Math.random() * (nb - 1));
    }

    private static List<String> askName(int nb) {
        List<String> names = new ArrayList<>();
        int i = 0;
        String name;
        System.out.println(View.GREEN+"Saisies des noms"+View.RESET);

        while(i<nb){
               try {
                   Scanner clavier = new Scanner(System.in);
                   System.out.print("Entrez le nom du joueur numéro " + (i + 1)+": ");
                   name = clavier.nextLine();
                   if(names.contains(name.trim())){
                       throw new QwirkleException(View.ORANGE+"Un joueur a le même nom que vous, tapez des noms différents pour les différents joueurs"+View.RESET);
                   }
                   checkName(name);
                   names.add(name);
                   i++;
               }catch (QwirkleException e){
                   System.out.println(e.getMessage());
               }catch (StringIndexOutOfBoundsException e){
                   System.out.println(View.ORANGE+"Entrez une direction valide svp : "+View.RESET);
               }
        }
        return names;
    }

    private static void checkName(String name){
        if(name == null || name.trim().isEmpty()){
            throw new QwirkleException(View.ORANGE+"Vous avez entrez un espace , veuillez entrer un nom valide svp : "+View.RESET);
        }
    }


}
