package g58183.qwirkle.model;

import g58183.qwirkle.view.View;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Game implements Serializable {
    private final Grid grid;
    private final Player[] player;

    public static final Path SAVE_DIRECTORY = Path.of("saves");

    private int current;

    public Game(List<String> noms) {
        player = new Player[noms.size()];
        for (int i = 0; i < noms.size(); i++) {
            player[i] = new Player(noms.get(i));
            player[i].refill();

        }

        grid = new Grid();
        current = 0;
    }


    /**
     * pour sauvegarder une partie
     * @param directoryPath
     * @param numFichier
     */
    public void saveGameData(String directoryPath, int numFichier ) {
        String fileName = "game_" + numFichier + ".ser";

        try {
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            FileOutputStream fos = new FileOutputStream(directoryPath + File.separator + fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
            System.out.println("Les données de cette parties sont sauvegardées au : "
                    + directoryPath + File.separator + fileName);
        } catch (IOException e) {
            System.out.println("Erreur lors de la sauvergarde de la partie: " + e.getMessage());
        }
    }

    public void write(String pathName) {
        if (!pathName.endsWith(".save"))
            pathName = pathName + ".save";

        try {
            if (Files.notExists(SAVE_DIRECTORY))
                Files.createDirectory(SAVE_DIRECTORY);

            Path path = SAVE_DIRECTORY.resolve(pathName);

            if (Files.exists(path))
                Files.delete(path);

            Files.createFile(path);

            try (final var outputStream = Files.newOutputStream(path, StandardOpenOption.WRITE);
                 final ObjectOutputStream classOut = new ObjectOutputStream(outputStream)) {
                classOut.writeObject(this);
                classOut.flush();
            }
        } catch (Exception e) {
            throw new QwirkleException("Erreur lors de l'écriture du fichier");
        }
    }

    public static Game getFromFile(String pathName) {
        if(!pathName.endsWith(".save"))
            pathName = pathName + ".save";

        try {
            if(Files.notExists(SAVE_DIRECTORY))
                Files.createDirectory(SAVE_DIRECTORY);

            Path path = SAVE_DIRECTORY.resolve(pathName);

            if(Files.notExists(path))
                throw new QwirkleException("La sauvegarde " + pathName + " n'existe pas");

            try(final var inputStream = Files.newInputStream(path);
                final ObjectInputStream classIn = new ObjectInputStream(inputStream)) {

                return (Game) classIn.readObject();
            }
        }
        catch (Exception e) {
            throw new QwirkleException("Erreur lors de la lecture du fichier");
        }
    }

    public static boolean hasSaves() {
        if(!Files.exists(SAVE_DIRECTORY))
            return false;

        return listSaves().size() > 0;
    }

    public static List<String> listSaves() {
        if(!Files.exists(SAVE_DIRECTORY))
            return List.of();

        try (var stream = Files.list(SAVE_DIRECTORY)) {
            return stream.filter(path -> path.getFileName().toString().endsWith(".save")).map(path -> {
                String fileName = path.getFileName().toString();
                return fileName.substring(0, fileName.length() - 5);
            }).toList();
        } catch (Exception e) {
            throw new QwirkleException("Erreur lors de la lecture du dossier");
        }
    }


    /**
     * Pour génénérer récupéer les détails d'une partie passée en paramètre
     * @param saveFile
     */
    public static void loadGameData(File saveFile) {
        try {
            FileInputStream fis = new FileInputStream(saveFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Game game = (Game) ois.readObject();
            ois.close();

            // Traiter les données de la partie chargée selon les besoins
            System.out.println("Détails de la partie :");
            System.out.println("Grid: " + game.getGrid());
            System.out.println("Players: " + Arrays.toString(game.getPlayers()));
            // Autres traitements...
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement des données de la partie : " + e.getMessage());
        }
    }




    public boolean isOver(){
        return !canAllPlay() && Bag.getInstance().size()==0;
    }
    private boolean canAllPlay(){
        //si un des jouerus peut jouer alors le jeu n'est pas fini
        for (Player p : getPlayers()) {
            if(canPlay(p)){
                return true;
            }

        }
        return false;
    }

    /**
     * Pour chaque joueur on va prendre les tuile qui se trouve dans sa main
     * regarder les tuiles déja jouées sur le plateau
     * placer chaque tuile de la main du joueur à chaque adjacence de toutes les tuiles présente sur le plateau
     * @param player
     * @return
     */
    private boolean canPlay(Player player){

        for (Tile t: player.getHand()) {

                for (TileAtPosition tap: grid.getPlayedList()) {
                    //adjacence de chaque tuile tap
                    int rowUp = tap.row()+1;
                    int rowDown = tap.row()-1;
                    int colLeft = tap.col()-1;
                    int colRight = tap.col()+1;


                    //on passe dans toutes les adjacences des pieces déja jouées
                    //si on voit que il y a au moins une tuile de la main qui peut etre jouée donc qui doit renvoyer true
                    //alors c'est que le joueur peut encore jouer
                    if(checkPossibleMove(tap.row(), colRight, t) //retourne vrai si
                        ||checkPossibleMove(tap.row(), colLeft, t)
                        ||checkPossibleMove(rowUp, tap.col(), t)
                        || checkPossibleMove(rowDown, tap.col(), t)){
                        return true;
                    }
                }
        }
        return false;
    }


    /**
     *
     * la methode met la tuile sur la plateau , si ca lance une erreur la tuile n'est pas ajouter grace a la methode add mais
     * mais si la methode ne renvoi pas d'erreur c'est que une tuile a pu etre ajouté donc une tuile est encore bonne
     * puis on supprime la tuile placée
     * @param row
     * @param col
     * @param t
     * @return
     */
    private boolean checkPossibleMove(int row, int col, Tile t){
        try{
            grid.add(row, col, t);
            grid.remove(row, col);
            return true;
        }catch(QwirkleException e){
            return false;
        }
    }



    /**
     * Retrieves the tiles that the player wants to place on the board from their hand and places them on the grid in the
     * <p>
     * specified direction. The tiles are removed from the player's hand and the next player's turn begins.
     *
     * @param d  the direction in which to place the tiles
     * @param is the indices of the tiles in the player's hand
     */
    public void first(Direction d, int... is) throws QwirkleException{//2,4,1
        int score;
        Tile[] tab = new Tile[is.length];// le nb de tuiles qu'il veut placer
        List<Tile> maliste = getCurrentPlayerHand();

        //roug, bleu, vert, mauve, orange , jaune
        for (int i = 0; i < is.length; i++) {
            tab[i] = maliste.get(is[i]);//is[i] donne l'indice des tuiles
        }

        try {
            score = grid.firstAdd(d, tab);
            updateCurrentPLayerScore(score);
            player[current].remove(tab);
            player[current].refill();
           pass();
        } catch (QwirkleException e) {
            System.out.println(View.RED+e.getMessage()+View.RESET);
        }


    }


    /**
     * Places a single tile on the grid at the specified row and column coordinates. The tile is removed from the
     * player's hand and the next player's turn begins.
     *
     * @param row   the row coordinate of the tile
     * @param col   the column coordinate of the tile
     * @param index the index of the tile in the player's hand
     */
    public void play(int row, int col, int index) throws QwirkleException{
        int score;
        try {
            List<Tile> maliste = getCurrentPlayerHand();
            Tile t = maliste.get(index);
            score = grid.add(row, col, t);
            updateCurrentPLayerScore(score);
            player[current].remove(t);
            player[current].refill();
            pass();
        } catch (QwirkleException e) {
            System.out.println(View.RED+e.getMessage()+View.RESET);
        }
    }

    /**
     * Places a group of tiles on the grid at the specified row and column coordinates in the specified direction. The
     * tiles are removed from the player's hand and the next player's turn begins.
     *
     * @param row     the row coordinate of the first tile
     * @param col     the column coordinate of the first tile
     * @param d       the direction in which to place the tiles
     * @param indexes the indices of the tiles in the player's hand
     */
    public void play(int row, int col, Direction d, int... indexes) throws QwirkleException {
        int score;
        try {
            List<Tile> maliste = getCurrentPlayerHand();
            Tile[] tab = new Tile[indexes.length];

            for (int i = 0; i < indexes.length; i++) {
                tab[i] = maliste.get(indexes[i]);
            }
            score = grid.add(row, col, d, tab);
            updateCurrentPLayerScore(score);
            player[current].remove(tab);
            player[current].refill();
            pass();
        } catch (QwirkleException e) {
            System.out.println(View.RED+e.getMessage()+View.RESET);
        }
    }


    /**
     * Plays the tiles in the current player's hand at the specified positions on the game board.
     *
     * @param is an integer array representing the position and tile indices of the tiles to be played.
     *           The length of the array should be a multiple of 3, where each group of 3 integers represent
     *           the row position, column position, and tile index of a tile to be played
     * @throws QwirkleException if the length of the is array is less than 3 or not a multiple of 3,
     *                          or if any of the positions specified are invalid or if the player tries to play an invalid move.
     */
    public void play(int... is) throws QwirkleException{//48,51,1,47,51,3
        int score;
        if (is.length < 3 || is.length % 3 != 0) {
            throw new QwirkleException("arguments invalides");
        }

        try {
            List<Tile> maliste = getCurrentPlayerHand();
            int j = 0;
            int i = 0;
            int k = 0;
            Tile[] tab = new Tile[is.length / 3];
            TileAtPosition[] array = new TileAtPosition[is.length / 3];

            //45,45,3,50,55,4
            while (i < is.length) {//s'appliquera juste pour les lignes de chaque tuiles
                int row = is[i];
                int col = is[i + 1];//i+1
                tab[j] = maliste.get(is[i + 2]);//i+2
                array[k++] = new TileAtPosition(row, col, tab[j++]);
                i = i + 3;
            }
            score = grid.add(array);
            updateCurrentPLayerScore(score);
            player[current].remove(tab);
            player[current].refill();
            pass();
        } catch (QwirkleException e) {
            System.out.println(View.RED+e.getMessage()+View.RESET);
        }
    }

    /**
     * This method returns a new instance of the GridView class that displays the current state of the game grid.
     *
     * @return a new instance of the GridView class that displays the current state of the game grid.
     */
    public GridView getGrid() {
        return new GridView(grid);
    }

    /**
     * This method returns the name of the current player.
     *
     * @return the name of the current player.
     */
    public String getCurrentPlayerName() {
        return player[current].getNom();
    }

    /**
     * This method returns the list of tiles in the current player's hand.
     *
     * @return the list of tiles in the current player's hand.
     */
    public List<Tile> getCurrentPlayerHand() {
        return player[current].getHand();
    }


    public void updateCurrentPLayerScore(int score){
        getCurrentPlayer().addPoints(score);
    }

    public int getScore(){
        return getCurrentPlayer().getScore();
    }
    /**
     * This method advances the turn to the next player in the game.
     */
    public void pass() {
        setCurrent(getCurrent()+1);
        current = current % player.length;
    }

    /**
     * This method returns an array of all the players in the game.
     *
     * @return an array of all the players in the game.
     */
    public Player[] getPlayers() {
        return player;
    }



    /**
     * This method returns the current player.
     *
     * @return the current player.
     */
    public Player getCurrentPlayer() {
        return player[current];
    }

    /**
     * This method returns the index of the current player.
     *
     * @return the index of the current player.
     */
    public int getCurrent() {
        return current;
    }

    /**
     * This method sets the index of the current player.
     *
     * @param current the index of the current player.
     */
    public void setCurrent(int current) {
        this.current = current;
    }


}
