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

        if (game == null) {//quand je veux jouer une nouvelle partie
            int nb;
            View.displayTitle();
            System.out.println("Combien de joueurs vont jouer ? : ");
            nb = robuste(2, 4, "");

            game = new Game(askName(nb));//demande de noms
            game.setCurrent(getRandomPlayer(nb));//met current a un indice random;
            try {
                playGame();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }else{
            try {
                playGame();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    }



    /**
     * Parses the command string and checks if it matches the format for the first command.
     * If it matches, it extracts the direction and tile indices, and calls the corresponding game method.
     * If an exception occurs during the command execution, it prints an error message.
     *
     * @param command the command string to be parsed and executed
     * @return true if the command matches the first command format and is executed successfully, false otherwise
     */
    private static boolean firstCommand(String command) {
        // The playFirstPattern represents the pattern for the first command format
        String playFirstPattern = "f\\s+([lrud])\\s+\\d+(\\s+\\d+)*";

        if (Pattern.matches(playFirstPattern, command)) {
            // Split the command into parameters
            String[] params = command.split(" ");
            String direction = params[1];
            int[] tileIndices = new int[params.length - 2];
            int j = 0;

            // Extract the tile indices from the command
            for (int i = 2; i < params.length; i++) {
                tileIndices[j] = Integer.parseInt(params[i]) - 1;
                j++;
            }

            try {
                // Call the game's 'first' method with the extracted direction and tile indices
                game.first(chooseDirection(direction), tileIndices);
                return true;
            } catch (QwirkleException e) {
                System.out.println(Color.ORANGE + e.getMessage() + View.RESET);
                return false;
            } catch (NumberFormatException e) {
                System.out.println(Color.ORANGE + "Please leave a single space between all elements of a command" + View.RESET);
                return false;
            }
        }
        return false;
    }

    /**
     * Executes the main game loop until the game is over.
     * Within the loop, it handles the player's input commands and performs the corresponding actions.
     * If an exception occurs during the command execution, it prints an error message.
     *
     * @throws Exception if an error occurs during the game execution
     */
    private static void playGame() throws Exception {
        boolean ok;
        while (!game.isOver()) {
            try {
                ok = true;
                while (ok) {
                    if (game.getGrid().isEmpty()) {
                        // Handle the first command when the grid is empty
                        // Prompt the user for a valid first command until one is entered
                        Scanner clavier;
                        do {
                            System.out.println(Bag.getInstance().size());
                            view.display(game.getGrid());
                            view.display(game.getCurrentPlayer().getNom(), game.getCurrentPlayerHand(), game.getScore());
                            clavier = new Scanner(System.in);
                            System.out.print(Color.BLUE + "Please enter the first command to start:\n" + View.RESET +
                                    Color.GREEN + "play first : f <d> <i1> [<i2>] \n" +
                                    "PS -> d: direction in l (left), r (right), u (up), d (down)\n" +
                                    "   -> i: index in list of tiles \n" +
                                    "Enter here ->: " + View.RESET);
                        } while (!firstCommand(clavier.nextLine()));
                        ok = false;
                    } else {
                        // Handle regular game commands when the grid is not empty
                        // Prompt the user for a valid command until one is entered
                        Scanner clavier;
                        do {
                            view.display(game.getGrid());
                            view.display(game.getCurrentPlayer().getNom(), game.getCurrentPlayerHand(), game.getScore());
                            view.displayHelp();
                            clavier = new Scanner(System.in);
                        } while (!playQwirkleCommand(clavier.nextLine()));
                        ok = false;
                    }
                }
            } catch (QwirkleException e) {
                view.displayError(Color.RED + e.getMessage() + View.RESET);
                view.displayError(Color.RED + "Invalid move, please try again!" + View.RESET);
            } catch (NoSuchElementException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Parses and executes the player's input command.
     * It checks if the command matches any of the predefined patterns,
     * and calls the corresponding game method accordingly.
     * If an exception occurs during the command execution, it prints an error message.
     *
     * @param command the command string to be parsed and executed
     * @return true if the command is valid and executed successfully, false otherwise
     */
    public static boolean playQwirkleCommand(String command) {
        // Patterns for each type of command
        String playTilePattern = "o\\s+\\d+\\s+\\d+\\s+\\d+";
        String playLinePattern = "l\\s+\\d+\\s+\\d+\\s+([lrud])(\\s+\\d+)*";
        String playPlicPlocPattern = "m\\s+\\d+\\s+\\d+\\s+\\d+(\\s+\\d+\\s+\\d+\\s+\\d+)*";
        String passPattern = "p$";
        String quitPattern = "q$";
        String savePattern = "s$";
        String helping = "h$";

        Scanner clavier;
        try {
            if (Pattern.matches(playTilePattern, command)) {
                // Call the corresponding method for the 'play one tile' command
                playOneTile(command);
                return true;
            } else if (Pattern.matches(playLinePattern, command)) {
                // Call the corresponding method for the 'play line' command
                playLine(command);
                return true;
            } else if (Pattern.matches(playPlicPlocPattern, command)) {
                // Call the corresponding method for the 'play plic-ploc' command
                playPlic_Ploc(command);
                return true;
            } else if (Pattern.matches(passPattern, command)) {
                // Call the 'pass' method
                game.pass();
                return true;
            } else if (Pattern.matches(quitPattern, command)) {
                // Quit the game
                System.out.println("Game ended!");
                System.exit(0);
                return true;
            } else if (Pattern.matches(savePattern, command)) {
                // Ask to save the game
                askToSaveGame();
                return true;
            } else if (Pattern.matches(helping, command)) {
                // Display the game help
                View.displayHelp();
            } else {
                System.out.println(Color.ORANGE + "Please enter a valid command." + View.RESET);
            }
        } catch (QwirkleException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
        return false;
    }

    /**
     * Parses the command string and calls the game's 'play' method to play a single tile.
     * It extracts the row, column, and tile index from the command string.
     *
     * @param command the command string to be parsed and executed
     */
    private static void playOneTile(String command) {
        String[] params = command.split("\\s+");
        int row = Integer.parseInt(params[1]);
        int col = Integer.parseInt(params[2]);
        int tileIndex = Integer.parseInt(params[3]);
        game.play(row, col, tileIndex - 1);
    }

    /**
     * Parses the command string and calls the game's 'play' method to play a line of tiles.
     * It extracts the row, column, direction, and tile indices from the command string.
     *
     * @param command the command string to be parsed and executed
     */
    private static void playLine(String command) {
        String[] params = command.split("\\s+");
        int row = Integer.parseInt(params[1]);
        int col = Integer.parseInt(params[2]);
        String direction = params[3];
        int[] tileIndices = new int[params.length - 4];
        int j = 0;

        // Extract the tile indices from the command
        for (int i = 4; i < params.length; i++) {
            tileIndices[j] = Integer.parseInt(params[i]) - 1;
            j++;
        }

        game.play(row, col, chooseDirection(direction), tileIndices);
    }

    /**
     * Parses the command string and calls the game's 'play' method to play a plic-ploc move.
     * It extracts the tile indices from the command string.
     *
     * @param command the command string to be parsed and executed
     */
    private static void playPlic_Ploc(String command) {
        String[] params = command.split("\\s+");
        int[] tileIndices = new int[params.length - 1];
        int j = 0;

        // Extract the tile indices from the command
        for (int i = 1; i < params.length; i++) {
            if (i % 3 == 0) {
                tileIndices[j] = Integer.parseInt(params[i]) - 1;
            } else {
                tileIndices[j] = Integer.parseInt(params[i]);
            }
            j++;
        }

        game.play(tileIndices);
    }

    /**
     * Asks the user to provide a name for the game save file.
     * It prompts the user for a save name and handles the saving process.
     */
    private static void askToSaveGame() {
        Scanner in = new Scanner(System.in);
        System.out.println("What name would you like to give to your game save?");
        String saveName = in.nextLine().trim();
        if (Game.listSaves().contains(saveName)) {
            System.out.println(Color.YELLOW + "A game with this name already exists. It will be overwritten. (y/n)" + View.RESET);
            char answer = yesOrNot(in.nextLine().trim().charAt(0));
            if (answer != 'y') {
                System.out.println("The game was not saved.");
            }
            game.write(saveName);
            System.out.println("The game was overwritten.");
        } else {
            game.write(saveName);
            System.out.println("The game was saved.");
        }
    }

    /**
     * Searches for saved game files and asks the user if they want to load a game.
     * It lists the available saved games and handles the loading process if requested.
     *
     * @param in the Scanner object used for user input
     * @return the loaded game if a valid game is selected, null otherwise
     */
    private static Game searchForSavedGame(Scanner in) {
        if (Game.hasSaves()) {
            List<String> saves = Game.listSaves();
            System.out.println("Found saved games:");
            saves.forEach(System.out::println);
            System.out.println("Do you want to load a game? (y/n)");
            char answer = yesOrNot(in.nextLine().trim().charAt(0));
            if (answer == 'y') {
                System.out.println("Which game do you want to load?");
                String saveName = in.nextLine().trim();
                if (saves.contains(saveName)) {
                    game = Game.getFromFile(saveName);
                    return game;
                } else {
                    System.out.println("No saved game was found.");
                }
            } else {
                System.out.println("No saved game was loaded.");
            }
        } else {
            System.out.println("No saved game was found.");
        }
        return null;
    }

    /**
     * Handles user input for 'yes' or 'no' choices.
     * It validates the input and prompts the user for valid input if necessary.
     *
     * @param choice the user's input choice
     * @return 'y' if the input represents 'yes', 'n' if the input represents 'no'
     */
    private static char yesOrNot(char choice) {
        while (true) {
            switch (choice) {
                case 'y':
                case 'Y':
                    return 'y';
                case 'n':
                case 'N':
                    return 'n';
                default:
                    System.out.println("Enter 'y' or 'n'");
                    Scanner scanner = new Scanner(System.in);
                    choice = scanner.nextLine().charAt(0);
                    break;
            }
        }
    }

    /**
     * Takes input from the user and ensures it is within a specified range.
     * It handles cases where the input is not within the range or is not a valid number.
     *
     * @param a       the lower bound of the range
     * @param b       the upper bound of the range
     * @param message the message to display before prompting for input
     * @return the validated input number within the specified range
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
                    System.out.println(Color.ORANGE + "Enter a number between " + a + " and " + b + " please: " + View.RESET);
                } else {
                    checkName(n);
                    ok = false;
                }
            } catch (InputMismatchException e) {
                System.out.println(Color.ORANGE + "Please enter a number." + View.RESET);
            } catch (QwirkleException e) {
                System.out.println(Color.ORANGE + "You entered a space, please try again!" + View.RESET);
            }
        } while (ok);
        return number;
    }


    /**
     * Prompts the user to select a direction to play the tiles in.
     * @return the selected direction
     */
    private static Direction chooseDirection(String d) {

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
                default -> System.out.println(Color.ORANGE + "Entrez une direction valide svp, entrez 'h' " +
                        "pour de l'aide :" + View.RESET);
            }
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println(Color.ORANGE + "Entrez une direction valide svp : " + View.RESET);
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
        System.out.println(Color.GREEN + "Saisies des noms" + View.RESET);

        while (i < nb) {
            try {
                Scanner clavier = new Scanner(System.in);
                System.out.print("Entrez le nom du joueur numéro " + (i + 1) + ": ");
                name = clavier.nextLine();
                if (names.contains(name.trim())) {
                    throw new QwirkleException(Color.ORANGE + "Un joueur a le même nom que vous," +
                            " tapez des noms différents pour les différents joueurs" + View.RESET);
                }
                checkName(name);
                names.add(name);
                i++;
            } catch (QwirkleException e) {
                System.out.println(e.getMessage());
            } catch (StringIndexOutOfBoundsException e) {
                System.out.println(Color.ORANGE + "Entrez une direction valide svp : " + View.RESET);
            }
        }
        return names;
    }

    /**
     * Checks if the given name is valid.
     *
     * @param name The name to be checked.
     * @throws QwirkleException if the given name is null, empty or contains only whitespace characters.
     */
    private static void checkName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new QwirkleException(Color.ORANGE + "Vous avez entrez un espace , veuillez entrer un nom valide svp : " +
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
    //view.display(game.getCurrentPlayer().getNom(), game.getCurrentPlayerHand(),game.getScore());
               /* Scanner clavier;

                int cpt = 0;
                do {


                    view.display(game.getGrid());
                    view.display(game.getCurrentPlayer().getNom(), game.getCurrentPlayerHand(), game.getScore());

                    if (cpt == 0) {
                        System.out.print(View.BLUE + "Merci de saisir cette commande pour commencer :\n" + View.RESET +
                                View.GREEN + "play first : f <d> <i1> [<i2>] \n" +
                                "PS -> d : direction in l (left), r (right), u (up), d(down)\n" +
                                "   -> i : index in list of tiles \n" +
                                "Veuillez saisir ici ->: " + View.RESET);
                    } else {
                        System.out.print(View.ORANGE + "Merci de saisir une commande valide comme celle-ci :\n" +
                                "play first : f <d> <i1> [<i2>] \n" +
                                "PS -> d : direction in l (left), r (right), u (up), d(down)\n" +
                                "   -> i : index in list of tiles\n" +
                                "Veuillez saisir ici ->: " + View.RESET);
                    }
                    clavier = new Scanner(System.in);
                    cpt++;

                } while (!firstCommande(clavier.nextLine()));//tant que la commande n'est pas bonne on boucle


                view.display(game.getGrid());

            } catch (QwirkleException e) {
                System.out.println(View.ORANGE + e.getMessage() + View.RESET);

            } catch (NumberFormatException e) {
                System.out.println(View.ORANGE + "Veuillez laisser un seul espace entre tout les élements d'une commande" + View.RESET);

            }
        }


        while (!game.isOver()) {

            boolean ok = true;// met a true pour que si tt se passe bien ok  = false et on peut continuer a jouer
            //si un probleme est la alors ok reste true car va dans le catch et rejoue dans la boucle imbriquée
            while (ok) {
                try {
                    View.display(game.getCurrentPlayer().getNom(), game.getCurrentPlayerHand(), game.getScore());
                    //affiche la main du joueur courant
                    Scanner clavier;
                    do {
                        view.display(game.getGrid());
                        view.display(game.getCurrentPlayer().getNom(), game.getCurrentPlayerHand(), game.getScore());
                        view.displayHelp();
                        clavier = new Scanner(System.in);
                    } while (!playQwirkleCommand(clavier.nextLine()));
                    View.display(game.getGrid());
                    ok = false;

                } catch (QwirkleException e) {
                    view.displayError(View.RED + e.getMessage() + View.RESET);
                    view.displayError(View.RED + "Coup invalide rééssayer!" + View.RESET);

                } catch (NoSuchElementException e) {
                    System.out.println("Bonjour");
                }
            }
        }
        */




}
