package g58183.qwirkle.model;

import g58183.qwirkle.view.View;

import java.util.List;

public class Game {
    private final Grid grid;
    private final Player[] player;
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
     Checks if the game is over.
     @return true if the game is over, false otherwise.
     */
    public boolean isGameOver() {
        return true;
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
