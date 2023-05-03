package g58183.qwirkle.model;

import g58183.qwirkle.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class Grid {
    private final Tile[][] tiles;
    private boolean isEmpty;

    private static final int CENTRE = 45;
    private static final int SIZE = 91;

    public Grid() {
        this.tiles = new Tile[SIZE][SIZE];
        this.isEmpty = true;
    }


    public Tile[][] getTiles() {
        return tiles;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    /**
     * This method allows to know the tile present at a position.
     *
     * @param row the row of the tile.
     * @param col the column of the tile.
     * @return the tile at the given position, or null if the position is out of bounds.
     */
    public Tile get(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            return null;
        }
        return tiles[row][col];
    }

    /**
     * This method can only be used once when there are no
     * deposited tiles.
     * She will place the tiles "in the middle" of
     * the game board (the first tile will be placed in 45.45)
     * varargs for divers tiles
     */
    public int firstAdd(Direction d, Tile... line) throws QwirkleException {
        int row = d.getDeltaRow();
        int col = d.getDeltaColumn();
        int l = CENTRE;              // t,T,T
        int c = CENTRE;
        Color couleur = null;
        Shape forme = null;
        List<Tile> maliste = new ArrayList<>();
        int score = 0;

        if (!isEmpty()) {
            throw new QwirkleException(View.ORANGE + "Ce n'est pas le premier coup " + View.RESET);
        }

        if (line.length == 1) {
            tiles[l][c] = line[0];
            score++;
        } else {

            //vérification des doublons/couleur ou shape
            for (int j = 0; j < line.length; j++) {

                if (maliste.contains(line[j])) {
                    throw new QwirkleException("Il ne peut y avoir deux même tuiles sur la même line");
                }
                if (j == 1) {//le point en commun des deux première

                    //je dois savoir le lien en commun des deux premiere tuiles  pour continuer à ajouter.
                    if (line[0].color() == line[1].color()) {
                        couleur = line[0].color();
                    } else if (line[0].shape() == line[1].shape()) {
                        forme = line[0].shape();
                    } else {
                        throw new QwirkleException("Les deux premiere tuiles n'ont aucun point en commun");
                    }
                } else if (j > 1 && !(line[j].color().equals(couleur)) && !(line[j].shape().equals(forme))) {
                    throw new QwirkleException("Cette tuile n'est pas bonne");
                }
                maliste.add(line[j]);
            }

            for (Tile tile : line) {//je peux mettre toutes les tuiles à la suite des autres vu que tout est ok
                tiles[l][c] = tile;
                l += row;
                c += col;
                score++;
            }
        }
        return score;

    }


    /**
     * Adds a tile to the specified row and column if the position is valid and
     * satisfies the game rules.
     *
     * @param row  the row of the tile
     * @param col  the column of the tile
     * @param tile the tile to add
     * @throws QwirkleException if the position is invalid or does not satisfy
     *                          the game rules
     */
    public int add(int row, int col, Tile tile) throws QwirkleException {
        //vérifier si la coordonnée est correcte
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            throw new QwirkleException(View.ORANGE + "La position ne se trouve pas sur le plateau." + View.RESET);
        }

        //vérifier si la case est vide
        if (tiles[row][col] != null) {
            throw new QwirkleException(View.ORANGE + "Une tuile existe déjà à cette position." + View.RESET);
        }

        //vérifier que la tuile a au moins un voisin valide
        if (!hasValidNeighbor(row, col, tile)) {
            throw new QwirkleException(View.ORANGE + "La tuile n'a pas de voisin valide." + View.RESET);
        }

        //vérifier que la ligne ne contient pas plus de 6 tuiles et qu'il n'y a pas de doublon
        if (!isValidNbAndDoublon(row, col, tile)) {
            throw new QwirkleException(View.ORANGE + "La tuile créerait un doublon." + View.RESET);
        }

        tiles[row][col] = tile;
        return 1;
    }

    /**
     * This method allows to compare colors or shapes to be applied on the line
     * by comparing adjacent tiles.
     *
     * @param t1 the first tile
     * @param t2 the second tile
     * @param t3 the third tile
     * @return true if the color or shape of the tiles are valid, false otherwise.
     */
    private boolean isValidColorOrShape(Tile t1, Tile t2, Tile t3) {
        if (t2 == null) {
            return true;
        }
        if (t3 == null) {
            return (t1.color() == t2.color()) || (t1.shape() == t2.shape());
        }
        return (t1.color() == t2.color() && t1.color() == t3.color()) || (t1.shape() == t2.shape() && t1.shape() == t3.shape());
    }

    /**
     * This method will check if the given tile has any neighbors adjacent to it.
     *
     * @param row the row coordinate of the tile
     * @param col the column coordinate of the tile
     * @return true if the tile has at least one adjacent neighbor, false otherwise
     */
    private boolean hasNeighbor(int row, int col) {
        if (tiles[row][col - 1] != null) {
            return true;
        }
        if (tiles[row][col + 1] != null) {
            return true;
        }
        if (tiles[row - 1][col] != null) {
            return true;
        }
        return tiles[row + 1][col] != null;
    }

    /**
     * This method checks whether a tile has correct neighboring tiles based on color or shape
     *
     * @param row  the row index of the tile
     * @param col  the column index of the tile
     * @param tile the tile to be checked
     * @return true if the tile has correct neighboring tiles based on color or shape, false otherwise
     */
    private boolean hasCorrectNeighbor(int row, int col, Tile tile) {
        return isValidColorOrShape(tile, get(row - 1, col), get(row - 2, col)) //up
                && isValidColorOrShape(tile, get(row, col + 1), get(row, col + 2)) //right
                && isValidColorOrShape(tile, get(row + 1, col), get(row + 2, col)) //down
                && isValidColorOrShape(tile, get(row, col - 1), get(row, col - 2)); //left
    }

    /**
     * This method checks if a given tile has a valid neighbor
     * by calling the hasNeighbor() and hasCorrectNeighbor() methods.
     *
     * @param row  The row index of the tile
     * @param col  The column index of the tile
     * @param tile The tile to check for valid neighbors
     * @return true if the tile has valid neighbors, false otherwise
     */
    private boolean hasValidNeighbor(int row, int col, Tile tile) {
        return hasNeighbor(row, col) && hasCorrectNeighbor(row, col, tile);
    }


    /**
     * This method takes a direction and checks if there are no more than 6 tiles on the same line and
     * if there is no duplicate tile, and it does the same thing with the opposite direction.
     *
     * @param row  the row of the tile
     * @param col  the column of the tile
     * @param tile the tile to check
     * @param d    the direction to check
     * @param max  the maximum number of tiles allowed on the same line
     * @return true if there are no more than max tiles on the same line and no duplicate tile, false otherwise
     * @throws QwirkleException if there is a duplicate tile on the same line
     */
    private boolean isValidDoublonByAxe(int row, int col, Tile tile, Direction d, int max) throws QwirkleException {

        int deltaRow = d.getDeltaRow();
        int deltaColumn = d.getDeltaColumn();
        int lg = row;
        int cln = col;

        int deltaRowOpposite = d.opposite().getDeltaRow();
        int deltaColumnOpposite = d.opposite().getDeltaColumn();
        int lgoposite = row;
        int clnoppiste = col;
        int cptdir1 = 0;
        int cptdir2 = 0;
        lg += deltaRow;
        cln += deltaColumn;

        while (get(lg, cln) != null && cptdir1 < max) {
            if (get(lg, cln).equals(tile)) { // check for duplicates
                throw new QwirkleException(View.ORANGE + "On ne peut pas avoir la même tuile sur la meme ligne" + View.RESET);
            }
            cptdir1++;
            lg += deltaRow;
            cln += deltaColumn;
        }

        lgoposite += deltaRowOpposite;
        clnoppiste += deltaColumnOpposite;

        while (get(lgoposite, clnoppiste) != null && cptdir2 < max) {
            if (get(lgoposite, clnoppiste).equals(tile)) { // check for duplicates
                throw new QwirkleException(View.ORANGE + "On ne peut pas avoir la meme tuile sur la meme ligne" + View.RESET);
            }
            cptdir2++;
            lgoposite += deltaRowOpposite;
            clnoppiste += deltaColumnOpposite;
        }

        return (cptdir1 + cptdir2) < max;
    }


    /**
     * This method checks if there are no duplicates on the same line and if we do not exceed the maximum
     * of 6 tiles in all directions.
     *
     * @param row  the row index of the tile being placed
     * @param col  the column index of the tile being placed
     * @param tile the tile being placed
     * @return true if there are no duplicates and the maximum number of tiles has not been exceeded, false otherwise
     */
    private boolean isValidNbAndDoublon(int row, int col, Tile tile) {
        return isValidDoublonByAxe(row, col, tile, Direction.UP, 6)
                && isValidDoublonByAxe(row, col, tile, Direction.RIGHT, 6);
    }


    /**
     * This method allows to place multiple tiles consecutively in a given direction
     *
     * @param row  the row where to start placing the tiles
     * @param col  the column where to start placing the tiles
     * @param d    the direction in which to place the tiles
     * @param line the tiles to be placed
     */
    public int add(int row, int col, Direction d, Tile... line) {

        int deltaRow = d.getDeltaRow();
        int deltaColumn = d.getDeltaColumn();
        int lg = row;
        int cln = col;
        int score = 0;

        add(lg, cln, line[0]);
        score++;
        lg += deltaRow;
        cln += deltaColumn;

        for (int i = 1; i < line.length; i++) {
            add(lg, cln, line[i]);
            lg += deltaRow;
            cln += deltaColumn;
            score++;
        }
        return score;
    }

    /**
     * This method allows to place multiple tiles not consecutively
     * but will ensure that the player places them on the same row or column
     *
     * @param tile the tiles to be placed
     * @throws QwirkleException if the tiles are not on the same row or column
     */
    public int add(TileAtPosition... tile) throws QwirkleException {


        if (containsDoublonsBeforeToPlace(tile)) {
            throw new QwirkleException("Il ne peut pas avoir la même tuile sur la même ligne");
        }
        try {
            if (sameRowOrCol(tile)) {
                checkTilesAfterPosed(tile);
                return tile.length;
            } else {
                throw new QwirkleException(View.ORANGE + "Pas la même ligne ou colonne" + View.RESET);
            }

        } catch (QwirkleException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {

        }
    }

    /**
     * Checks if the tiles can be placed at the given positions, based on Qwirkle rules.
     *
     * @param tile The tiles to be placed.
     * @throws QwirkleException Throws a QwirkleException if the tiles can't be placed.
     */
    private void checkTilesAfterPosed(TileAtPosition... tile) throws QwirkleException {
        List<TileAtPosition> myList = Arrays.stream(tile).toList();
        int cpt = 1;

        if (!caseVides(tile)) {//if the positions are not empty
            throw new QwirkleException(View.ORANGE + "Il y'a déjà des tuiles à cette position " + View.RESET);
        }
        if (!isInBoard(tile)) {
            throw new QwirkleException(View.ORANGE + "La position n'est pas sur le plateau" + View.RESET);
        }

        List<Boolean> isValid = new ArrayList<>();

        for (TileAtPosition t : tile) {
//this is just recording a boolean value which informs us if a tile has neighbors
            isValid.add(hasValidNeighbor(t.row(), t.col(), t.tile()));
        }

        if (!isValid.contains(true)) {//if there is at least one tile that has neighbors,
            throw new QwirkleException(View.ORANGE + "Les tuiles placées ne sont réliées à aucune ligne." + View.RESET);
        }

        for (TileAtPosition t : tile) {
            if (!isValidNbAndDoublon(t.row(), t.col(), t.tile())) {
                throw new QwirkleException(View.ORANGE + " Il y'a déja la même tuile sur la ligne" + View.RESET);
            }
        }

        myList.forEach(t -> tiles[t.row()][t.col()] = t.tile());

    }

    /**
     * Checks if the given positions are on the board.
     *
     * @param tile The positions to be checked.
     * @return Returns true if all positions are on the board, false otherwise.
     */
    private boolean isInBoard(TileAtPosition... tile) {
        int row;
        int col;

        for (TileAtPosition tileAtPosition : tile) {
            row = tileAtPosition.row();
            col = tileAtPosition.col();
            if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if there are duplicate tiles in the given positions before placing them.
     *
     * @param tile The positions to be checked.
     * @return Returns true if there are duplicate tiles, false otherwise.
     */
    private boolean containsDoublonsBeforeToPlace(TileAtPosition... tile) {
        List<TileAtPosition> myList = new ArrayList<>();

        for (TileAtPosition t : tile) {
            if (myList.contains(t)) {
                return true;
            } else {
                myList.add(t);
            }
        }
        return false;
    }

    /**
     * Checks if the given positions are empty.
     *
     * @param tile The positions to be checked.
     * @return Returns true if all positions are empty, false otherwise.
     */
    private boolean caseVides(TileAtPosition... tile) {
        for (TileAtPosition t : tile) {
            if (tiles[t.row()][t.col()] != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method will compare the row or column of all the tiles passed as parameter
     * <p>
     * using stream with filter (count the number of tiles that meet the condition)
     *
     * @param tile the tiles to be placed
     * @return a boolean that checks if the number of tiles is the same as those that meet the condition
     */
    private boolean sameRowOrCol(TileAtPosition... tile) {
        int row = tile[0].row();
        int col = tile[0].col();
        long a = Arrays.stream(tile).filter(t -> t.col() == col).count();
        long b = Arrays.stream(tile).filter(t -> t.row() == row).count();

        return a == tile.length || b == tile.length;
    }

    /**
     * This method allows us to know if the grid is empty or not
     *
     * @return a boolean indicating whether the grid is empty or not
     */
    public boolean isEmpty() {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j] != null) {
                    return false;
                }
            }
        }
        return true;
    }


}







