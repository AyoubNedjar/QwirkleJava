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
            View.display(game.getCurrentPlayer().getNom(), game.getCurrentPlayerHand(),game.getScore());
            game.first(chooseDirection(), chooseTiles());
            view.display(game.getGrid());

        } catch (QwirkleException e) {
            System.out.println(View.ORANGE + e.getMessage() + View.RESET);
            view.display(game.getGrid());
        }

        boolean replay = true;
        boolean isGameOver = false;
        while (replay) {
            while (!isGameOver) {

                boolean ok = true;
                while (ok) {
                    try {
                        View.display(game.getCurrentPlayer().getNom(), game.getCurrentPlayerHand(),game.getScore());
                        //affiche la main du joueur courant
                        char choice = askMethod();//demande la methode choisie en vérifiant bien les commandes entrées.
                        isGameOver = playChoice(choice, isGameOver);
                        View.display(game.getGrid());
                        ok = false;

                    } catch (QwirkleException e) {
                        view.displayError(View.RED + e.getMessage() + View.RESET);
                        view.displayError(View.RED + "Coup invalide rééssayer!" + View.RESET);

                    }

                }

            }
            isGameOver = replayOrNot(isGameOver);
            if (isGameOver) {
                replay = false;
            }

        }
        System.out.println(View.GREEN + "Merci et au revoir");
    }


    /**
     * Prompts the user for a character input and returns the input character.
     *
     * @return the character input by the user
     */
    private static char askMethod() {
        return robusteChar();
    }


    /**
     * Prompts the user to replay the game or not.
     *
     * @param isGameOver true if the game is over, false otherwise
     * @return true if the user chooses to quit the game, false if the user chooses to play again
     */
    private static boolean replayOrNot(boolean isGameOver) {
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
    }

    /**
     * Executes the player's chosen action and updates the game state.
     *
     * @param choice     the player's chosen action
     * @param isGameOver true if the game is over, false otherwise
     * @return true if the game is over after executing the action, false otherwise
     */
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
                    System.out.println(View.ORANGE + "Vous avez passé votre tour" + View.RESET);
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


    /**
     * Executes the user's selected move based on their input.
     *
     * @param choice the user's selected move
     */
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
                Direction d = chooseDirection();
                game.play(robuste(0, 90, "Choix de la ligne : "), robuste(0, 90,
                        "Choix de la colonne : "), d, indexTiles);
            }
            case 'm' -> {//joue plusieurs ou une seule tuile dans des positions quelquonques
                System.out.println(View.GREEN + "Vous allez jouer plusieurs ou une seule tuile dans " +
                        "des positions quelquonques" + View.RESET);
                game.play(picpoc());
            }
            case 'f' -> game.first(chooseDirection(), chooseTiles());

            case 'h' -> View.displayHelp();
        }
    }


    /**
     * Prompts the user to enter a number between a and b, and validates their input.
     *
     * @param a       the lower bound of the accepted input range
     * @param b       the upper bound of the accepted input range
     * @param message the message to display to the user when prompting for input
     * @return the user's input as an integer
     */
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

    /**
     * Prompts the user to enter a character and validates their input.
     *
     * @return the user's input as a character
     */
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

    /**
     * This method generates an array of integers representing the selected tiles by the user.
     * The user is asked to enter the number of tiles they want to play, as well as the row, column, and index of each tile.
     * The input for the row and column must be between 0 and 90, and the input for the index must be between 1 and 6.
     * The method returns an integer array with the coordinates and index of each selected tile.
     **/
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
     */
    private static int chooseOneTile() {
        return robuste(1, 6, "Entrez la numéro de la tuile dont vous voulez jouez : ") - 1;
        //pour accéder aux indices

    }

    /**
     * Prompts the user to select multiple tiles to play on their turn.
     *
     * @return an array containing the indices of the selected tiles
     */
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

    /**
     * Prompts the user to select a direction to play the tiles in.
     *
     * @return the selected direction
     */
    private static Direction chooseDirection() {
        System.out.println(View.GREEN + "Choisissez la Direction à prendre : " + View.RESET);
        String d;

        do {
            System.out.println("Tapez " + View.YELLOW_BOLD + "'r'" + View.RESET + " pour Droite, " + View.YELLOW_BOLD + "'d'"
                    + View.RESET + " pour Bas, " + View.YELLOW_BOLD + "'u'" + View.RESET + " pour Haut, " + View.YELLOW_BOLD
                    + "'l'" + View.RESET + " pour gauche");
            Scanner clavier = new Scanner(System.in);
            d = clavier.nextLine();
            d = d.toLowerCase(Locale.ROOT);

            try {
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

        } while (true);

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


}
