package g58183.qwirkle;

import g58183.qwirkle.model.Direction;
import g58183.qwirkle.model.Game;
import g58183.qwirkle.view.View;
import g58183.qwirkle.model.*;

import java.util.*;
import java.util.regex.Pattern;

import java.io.File;

public class App {
    private static Game game;
    private static View view;

    private static int numFile = 0;

    public static void main(String[] args) {

        System.out.println("Vérification des parties sauvegardées...");
        final Scanner in = new Scanner(System.in);
        game = searchForSavedGame(in);

        if(game==null){
            int nb ;
            View.displayTitle();
            System.out.println("Combien de joueurs vont jouer ? : ");
            nb = robuste(2, 4, "");

            try {
                game = new Game(askName(nb));//demande de noms
                game.setCurrent(getRandomPlayer(nb));//met current a un indice random;
                //view.display(game.getCurrentPlayer().getNom(), game.getCurrentPlayerHand(),game.getScore());
                Scanner clavier;


                int cpt = 0;
                do{


                    view.display(game.getGrid());
                    view.display(game.getCurrentPlayer().getNom(), game.getCurrentPlayerHand(),game.getScore());

                    if(cpt==0){
                        System.out.print(View.BLUE+"Merci de saisir cette commande pour commencer :\n" +View.RESET +
                                View.GREEN+"play first : f <d> <i1> [<i2>] \n" +
                                "PS -> d : direction in l (left), r (right), u (up), d(down)\n" +
                                "   -> i : index in list of tiles \n" +
                                "Veuillez saisir ici ->: " +View.RESET);
                    }else{
                        System.out.print(View.ORANGE+"Merci de saisir une commande valide comme celle-ci :\n"+
                                "play first : f <d> <i1> [<i2>] \n" +
                                "PS -> d : direction in l (left), r (right), u (up), d(down)\n" +
                                "   -> i : index in list of tiles\n" +
                                "Veuillez saisir ici ->: " +View.RESET);
                    }
                    clavier = new Scanner(System.in);
                    cpt++;

                }while (!firstCommande(clavier.nextLine()));//tant que la commande n'est pas bonne on boucle


                view.display(game.getGrid());

            } catch (QwirkleException e) {
                System.out.println(View.ORANGE + e.getMessage() + View.RESET);

            }
        }


        while (!game.isOver()) {

            boolean ok = true;
            while (ok) {
                try {
                    View.display(game.getCurrentPlayer().getNom(), game.getCurrentPlayerHand(),game.getScore());
                    //affiche la main du joueur courant
                    Scanner clavier ;
                    do{
                        view.display(game.getGrid());
                        view.display(game.getCurrentPlayer().getNom(), game.getCurrentPlayerHand(),game.getScore());
                        view.displayHelp();
                        clavier  = new Scanner(System.in);
                    }while (!playQwirkleCommand(clavier.nextLine()));
                    View.display(game.getGrid());
                    ok = false;

                } catch (QwirkleException e) {
                    view.displayError(View.RED + e.getMessage() + View.RESET);
                    view.displayError(View.RED + "Coup invalide rééssayer!" + View.RESET);

                }catch (NoSuchElementException e){
                    System.out.println("Bonjour");
                }
            }
        }

    }



    //si la commande est bonne
    // Extraction des paramètres de la commande play first

    private static boolean firstCommande(String command){
        String playFirstPattern = "f\\s+([lrud])\\s+\\d+(\\s+\\d+)*";
        if (Pattern.matches(playFirstPattern, command)) {

            String[] params = command.split(" ");
            String direction = params[1];
            int[] i1 = new int[params.length-2];//on va juste prendre les tuiles donc on enleve 2 car les deux premier arguments c'est direction et f
            int j  = 0;

            for (int i = 2; i < params.length; i++) {//on commence à 2 pour les tuiles
                i1[j] = Integer.parseInt(params[i])-1;
                //System.out.println(i1[j]);
                j++;
            }
            try{
                game.first(chooseDirection(direction),i1);
                return true;
            }catch (QwirkleException e){
                System.out.println(View.ORANGE+e.getMessage()+View.RESET);
                return false;
            }

        }
        return false;

    }
    public static boolean playQwirkleCommand(String command) {
        // Pattern pour chaque type de commande
        //le /d = entier
        //le //s = pour représenter un espace
        //le ([lrud]) = pour un caractere parmis les 4direction
        String playTilePattern = "o\\s+\\d+\\s+\\d+\\s+\\d+";
        String playLinePattern = "l\\s+\\d+\\s+\\d+\\s+([lrud])(\\s+\\d+)*";
        String playPlicPlocPattern = "m\\s+\\d+\\s+\\d+\\s+\\d+(\\s+\\d+\\s+\\d+\\s+\\d+)*";


        String passPattern = "p$";
        String quitPattern = "q$";
        String savePattern = "s$";
        String helping = "h$";

        try{
            // Vérifier la correspondance avec chaque motif de commande
            if (Pattern.matches(playTilePattern, command)) {
                // Extraction des paramètres de la commande play 1 tile
                String[] params = command.split(" ");
                int row = Integer.parseInt(params[1]);
                int col = Integer.parseInt(params[2]);
                int i = Integer.parseInt(params[3]);
                game.play(row, col, i-1);

                return true;


            } else if (Pattern.matches(playLinePattern, command)) {
                // Extraction des paramètres de la commande play line
                String[] params = command.split(" ");
                int row = Integer.parseInt(params[1]);
                int col = Integer.parseInt(params[2]);
                String direction = params[3];
                int[] iTiles = new int[params.length-2];
                int j  = 0;

                for (int i = 4; i < params.length; i++) {
                    iTiles[j] = Integer.parseInt(params[i])-1;
                    j++;
                }
                game.play(row, col,chooseDirection(direction),iTiles);
                return true;

            } else if (Pattern.matches(playPlicPlocPattern, command)) {
                // Extraction des paramètres de la commande play plic-ploc
                String[] params = command.split(" ");
                int[] tab = new int[params.length-1];
                int j = 0;

                for (int i = 1; i < params.length; i++) {
                    if(i%3==0){
                        tab[j] = Integer.parseInt(params[i])-1;
                    }else{
                        tab[j] = Integer.parseInt(params[i]);
                    }

                    j++;
                }

                game.play(tab);
                return true;

            }else if (Pattern.matches(passPattern, command)) {
                // Extraction des paramètres de la commande play 1 tile
               game.pass();
                return true;
            }else if (Pattern.matches(quitPattern, command)) {

                System.out.println("Bonne fin de partie !");
                System.exit(0);

                return true;
            }else if (Pattern.matches(savePattern, command)) {
                Scanner in = new Scanner(System.in);
                System.out.println("Quel nom voulez-vous donner à votre partie ?");
                String saveName = in.nextLine().trim();
                if(Game.listSaves().contains(saveName))
                {
                    System.out.println(Color.YELLOW + "Une partie avec ce nom existe déjà, elle sera écrasée. (y/n)" + View.RESET);
                    String answer = in.nextLine().trim();
                    if(!answer.equalsIgnoreCase("y"))
                    {
                        System.out.println("La partie n'a pas été sauvegardée.");

                    }

                    game.write(saveName);
                    System.out.println("La partie a été écrasée.");
                }else{
                    game.write(saveName);
                    System.out.println("La partie a été sauvegardée.");
            }

        }else if(Pattern.matches(helping,command)) {
            View.displayHelp();
        }

    }catch(QwirkleException e){
            System.out.println(e.getMessage());
            return false;
        }
        return false;

    }


    private static Game searchForSavedGame(Scanner in) {
        if (Game.hasSaves()) {
            final var saves = Game.listSaves();
            System.out.println("Parties sauvegardées trouvées :");
            saves.forEach(System.out::println);
            System.out.println("Voulez-vous charger une partie ? (y/n)");
            String answer = in.nextLine().trim();
            if (answer.equalsIgnoreCase("y")) {
                System.out.println("Quelle partie voulez-vous charger ?");
                String saveName = in.nextLine().trim();
                if (saves.contains(saveName)) {

                    game = Game.getFromFile(saveName);

                    return game; // Ajoutez cette ligne pour retourner la partie chargée
                } else {
                    System.out.println("Aucune partie sauvegardée n'a été trouvée.");
                }
            } else {
                System.out.println("Aucune partie sauvegardée n'a été chargée.");
            }
        } else {
            System.out.println("Aucune partie sauvegardée n'a été trouvée.");
        }
        return null;
    }

    private static int[] parseTiles(String colonnesString) {
        String[] colonnesArray = colonnesString.split(",\\s*");
        int[] colonnes = new int[colonnesArray.length];

        for (int i = 0; i < colonnesArray.length; i++) {
            colonnes[i] = Integer.parseInt(colonnesArray[i]);
        }

        return colonnes;
    }


    private static int robuste(int a, int b, String message) {
        boolean ok = true;
        int number = -1;
        Scanner clavier;

        do {
            try {
                if (!message.equals("")) {
                    System.out.print(message);
                }

                clavier = new Scanner(System.in);
                number = clavier.nextInt();
                String n = String.valueOf(number);
                if (number < a || number > b) {
                    System.out.println(View.ORANGE + "Entrez un nombre entre " + a + " et " + b + " svp : " + View.RESET);
                } else {
                    checkName(n);
                    ok = false;
                }

            } catch (InputMismatchException e) {
                System.out.println(View.ORANGE + "veuillez entrez un numéro svp" + View.RESET);
            } catch (QwirkleException e) {
                System.out.println(View.ORANGE + "Vous avez rentré un espace , réésayez ! " + View.RESET);

            }
        } while (ok);
        return number;
    }



    public void loadSavedGame() {
        File savesDirectory = new File("saves/");
        File[] saveFiles = savesDirectory.listFiles();

        if (saveFiles == null || saveFiles.length == 0) {
            System.out.println("Aucune partie sauvegardée disponible.");
            return;
        }

        System.out.println("Parties sauvegardées disponibles :");
        for (int i = 0; i < saveFiles.length; i++) {
            System.out.println((i + 1) + ". " + saveFiles[i].getName());
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Choisissez un numéro de partie à charger : ");
        int selectedOption = scanner.nextInt();

        if (selectedOption < 1 || selectedOption > saveFiles.length) {
            System.out.println("Numéro de partie invalide.");
            return;
        }

        File selectedFile = saveFiles[selectedOption - 1];
        Game.loadGameData(selectedFile);
    }
    public static int getNumFile() {
        return numFile;
    }

    public static void setNumFile(int numFile) {
        App.numFile = numFile;
    }


    /**
     * Prompts the user to select a direction to play the tiles in.
     *
     * @return the selected direction
     */
    private static Direction chooseDirection(String d) {

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
                    default -> System.out.println(View.ORANGE + "Entrez une direction valide svp, entrez 'h' " +
                            "pour de l'aide :" + View.RESET);
                }
            } catch (StringIndexOutOfBoundsException e) {
                System.out.println(View.ORANGE + "Entrez une direction valide svp : " + View.RESET);
            }
        return null;
    }


    /**
     * Returns a random player index between 0 (inclusive) and nb (exclusive).
     *
     * @param nb the number of players
     * @return a random player index
     */
    private static int getRandomPlayer(int nb) {
        return (int) (Math.random() * (nb - 1));
    }

    /**
     * Asks the user to enter the names of the players and returns a list of the names.
     *
     * @param nb the number of players
     * @return a list of the names of the players
     */
    private static List<String> askName(int nb) {
        List<String> names = new ArrayList<>();
        int i = 0;
        String name;
        System.out.println(View.GREEN + "Saisies des noms" + View.RESET);

        while (i < nb) {
            try {
                Scanner clavier = new Scanner(System.in);
                System.out.print("Entrez le nom du joueur numéro " + (i + 1) + ": ");
                name = clavier.nextLine();
                if (names.contains(name.trim())) {
                    throw new QwirkleException(View.ORANGE + "Un joueur a le même nom que vous," +
                            " tapez des noms différents pour les différents joueurs" + View.RESET);
                }
                checkName(name);
                names.add(name);
                i++;
            } catch (QwirkleException e) {
                System.out.println(e.getMessage());
            } catch (StringIndexOutOfBoundsException e) {
                System.out.println(View.ORANGE + "Entrez une direction valide svp : " + View.RESET);
            }
        }
        return names;
    }

    /**

     Checks if the given name is valid.
     @param name The name to be checked.
     @throws QwirkleException if the given name is null, empty or contains only whitespace characters.
     */
    private static void checkName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new QwirkleException(View.ORANGE + "Vous avez entrez un espace , veuillez entrer un nom valide svp : " +
                    "" + View.RESET);
        }
    }


























    /**
     * isGameOver = replayOrNot(isGameOver);
     *             if (isGameOver) {
     *                 replay = false;
     *             }
     * Prompts the user to replay the game or not.
     *
     * @param isGameOver true if the game is over, false otherwise
     * @return true if the user chooses to quit the game, false if the user chooses to play again
     */
    /*private static boolean replayOrNot(boolean isGameOver) {
        String choice;
        if (isGameOver)
            do {
                System.out.println("Entrez 'y' pour recommencer, sinon 'n' pour quitter définitivement");
                Scanner clavier = new Scanner(System.in);
                choice = clavier.nextLine();
                choice = choice.toLowerCase();
                if (choice.length() != 1) {
                    throw new QwirkleException(View.ORANGE + "Veuillez entrer un seul caractère : " + View.RESET);
                }

                switch (choice.charAt(0)) {
                    case 'y' -> {
                        return false;
                    }
                    case 'n' -> {
                        return true;
                    }
                }

            } while (true);
        return false;
    }*/

    /*
     /**
     * Prompts the user to select multiple tiles to play on their turn.
     *
     * @return an array containing the indices of the selected tiles
     *
     *

    private static int[] chooseTiles() {
        System.out.println(View.GREEN + "Choisissez vos tuiles : " + View.RESET);
        int number;
        boolean ok = true;
        List<Integer> maliste = new ArrayList<>();
        int i = 0;
        do {
            try {
                System.out.print("Choix de tuile numéro " + (i + 1) + ": ");
                Scanner clavier = new Scanner(System.in);
                number = clavier.nextInt();
                if (number > 6 || number < 1) {
                    System.out.println(View.ORANGE + "Entrez un nombre entre 1 et 6 svp :" + View.RESET);
                } else {
                    if (maliste.contains(number - 1)) {
                        System.out.println(View.ORANGE + "Cette tuile est déja choisi , essayez une autre" + View.RESET);
                    } else {
                        maliste.add(number - 1);//pour accéder aux indices de la main
                        i++;
                        String a;
                        do {
                            System.out.print("Voulez vous entrez une nouvelle piece ? tapez '+' , sinon tapez N : ");
                            clavier = new Scanner(System.in);
                            a = clavier.nextLine();

                        } while (!a.contains("N") && !a.contains("+") && !a.contains(" "));
                        D
                        if (a.contains("N")) {
                            ok = false;
                        }

                    }

                }

            } catch (InputMismatchException e) {
                System.out.println(View.ORANGE + "Veuillez entrez un numéro svp." + View.RESET);
            } catch (StringIndexOutOfBoundsException e) {
                System.out.println(View.ORANGE + "Vous avez rentré un espace , réésayez ! " + View.RESET);
            }
        } while (ok && i < 6);

        return maliste.stream().mapToInt(Integer::intValue).toArray();// qui retourne une liste de int(entier simplement)
        // et pas de Integer(classe objet)

    }


    private static char askMethod() {
        return robusteChar();
    }


    private static char robusteChar() {
        String choice;

        do {
            System.out.print("Si vous avez besoin d'aide pour les commandes tapez 'h'," +
                    " sinon tapez le commande de votre choix  : ");


            try {
                Scanner clavier = new Scanner(System.in);
                choice = clavier.nextLine();
                choice = choice.toLowerCase(Locale.ROOT);
                if (choice.length() != 1) {
                    throw new QwirkleException(View.ORANGE + "Veuillez entrer un seul caractère : " + View.RESET);
                }

                if (game.getGrid().isEmpty()) {
                    if (choice.charAt(0) == 'f') {
                        return 'f';
                    } else if (choice.charAt(0) == 'h') {
                        View.displayHelp();
                    } else {
                        throw new QwirkleException(View.ORANGE + "Le plateau est vide , tapez " + View.YELLOW_BOLD + "'f'"
                                + View.RESET + View.YELLOW_BOLD + " pour commencer !" + View.RESET);
                    }
                } else {
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
                            System.out.println(View.ORANGE + "Les premières tuiles ont déja été jouées" + View.RESET);
                            View.display(game.getGrid());
                            View.display(game.getCurrentPlayerName(), game.getCurrentPlayerHand(),game.getScore());
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
                            System.out.println(View.ORANGE + "Entrez une commande valide svp : " + View.RESET);
                            View.display(game.getGrid());
                        }
                    }
                }

            } catch (StringIndexOutOfBoundsException e) {
                System.out.println(View.ORANGE + "Vous avez rentré un espace , réésayez ! " + View.RESET);
            }


        } while (true);
    }




    private static void playChoice(char choice) {
        do {
            switch (choice) {
                //joue plusieurs tuiles a la suite des autres
                //joue plusieurs ou une seule tuile dans des positions quelquonques
                case 'o', 'l', 'm', 'f' -> {
                    executeChooice(choice);

                }
                case 'p' -> {
                    System.out.println(View.ORANGE + "Vous avez passé votre tour" + View.RESET);
                    game.pass();
                }
                case 'q' -> {
                    //game.saveGameData(getNumFile());
                    setNumFile(numFile++);

                }
            }

        } while (true);

    }


    private static void executeChooice(char choice) {
        View.display(game.getGrid());
        View.display(game.getCurrentPlayer().getNom(), game.getCurrentPlayerHand(),game.getScore());

        switch (choice) {
            case 'o' -> {//joue une seule tuile
                System.out.println(View.GREEN + "Vous allez jouer une seule tuile." + View.RESET);
                int indexTile = chooseOneTile();
                game.play(robuste(0, 90, "Choix de la ligne : "), robuste(0, 90,
                        "Choix de la colonne : "), indexTile);
            }
            case 'l' -> {//joue plusieurs tuiles a la suite des autres
                System.out.println(View.GREEN + "Vous allez jouer plusieurs tuiles qui vont se suivre." + View.RESET);
                int[] indexTiles = chooseTiles();
                Direction d = chooseDirection("");
                game.play(robuste(0, 90, "Choix de la ligne : "), robuste(0, 90,
                        "Choix de la colonne : "), d, indexTiles);
            }
            case 'm' -> {//joue plusieurs ou une seule tuile dans des positions quelquonques
                System.out.println(View.GREEN + "Vous allez jouer plusieurs ou une seule tuile dans " +
                        "des positions quelquonques" + View.RESET);
                game.play(picpoc());
            }
            case 'f' -> game.first(chooseDirection(""), chooseTiles());

            case 'h' -> View.displayHelp();
        }
    }



    private static int[] picpoc() {
        int[] tab = new int[robuste(1, 6, "Combien de tuiles voulez-vous jouez ? : ") * 3];
        int i = 0;
        int row;
        int col;
        int indextile;
        int cpt = 1;
        do {

            indextile = robuste(1, 6, "Entrez la numéro de la tuile numéro " + cpt
                    + " dont vous voulez jouez : ") - 1;
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

    /**
     * Prompts the user to select a tile to play on their turn.
     *
     * @return the index of the selected tile
     *
    private static int chooseOneTile() {
        return robuste(1, 6, "Entrez la numéro de la tuile dont vous voulez jouez : ") - 1;
        //pour accéder aux indices

    }
    */


}
