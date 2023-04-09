package g58183.qwirkle.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Grid {
    private Tile[][] tiles;
    private boolean isEmpty;

    private static final int CENTRE = 45;
    private static final int TAILLE = 91;

    public Grid() {
        this.tiles = new Tile[TAILLE][TAILLE];
        this.isEmpty = true;
    }


    public Tile[][] getTiles() {
        return tiles;
    }

    /**
     * Cette methode va permettre de connaitre la tuile présente à une position
     * @param row
     * @param col
     * @return
     */
    public Tile get(int row, int col) {
        if(row<0 || row>TAILLE || col<0 || col>TAILLE){
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
    public void firstAdd(Direction d, Tile... line) throws QwirkleException{
        if (!isEmpty()){
            throw new QwirkleException("ce n'est pas le premier coup ");
        }
        int ligne = d.getDeltaRow();
        int col = d.getDeltaColumn();
        int l = CENTRE;              // t,T,T
        int c = CENTRE;
        Color couleur = null;
        Shape forme = null;
        List<Tile> maliste = new ArrayList<>();

        if (line.length == 1) {
            tiles[l][c] = line[0];
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
                        throw new QwirkleException("les deux premiere tuiles n'ont aucun point en commun");
                    }
                } else if (j > 1 && !(line[j].color().equals(couleur)) && !(line[j].shape().equals(forme))) {
                    throw new QwirkleException("Cette tuile n'est pas bonne");
                }
                maliste.add(line[j]);
            }

            for (int j = 0; j < line.length; j++) {//je peux mettre toutes les tuiles à la suite des autres vu que tout est ok
                tiles[l][c] = line[j];
                l += ligne;
                c += col;
            }
        }

    }


    public void add(int row, int col, Tile tile) {
        //vérifier si la coordonnées sont correct
        //vérifier si la case est vide
        //vérifier que la ligne ne contient pas plus de 6 tuiles
        //vérifier que la piece ajoutée a une ou des voisines
        //vérifier la couleur ou la forme des lignes adjacentes et si la ligne de ne contient déja pas la meme tuile

        if (row > 0 && row < TAILLE && col > 0 && col < TAILLE) {   //coordonnées
            if (tiles[row][col] == null) {//si la case est vide
                if (hasValidNeighbor(row,col,tile)) {//que la piece ajoutée a une ou des voisines
                    if (isValidNbAndDoublon(row, col, tile,6)) { //vérification des 6 tuiles sur la mem ligne et pas de doublons

                        tiles[row][col] = tile;
                    } else {
                        throw new QwirkleException("il y' a plus que 6 tuiles sur la même ligne");
                    }
                } else {
                    throw new QwirkleException("voisins invalides");
                }

            } else {
                throw new QwirkleException("Une tuile existe déja à cet emplacement");
            }
        } else {
            throw new QwirkleException("la position ne se trouve pas sur le plateau ");
        }

    }

    //idée pour is valid: a chaque fois qu'on va placer une tuile on voit sa ligne et sa colone , pour la
    //placée il faut que le truc commun (il faut voir le point en commun des deux premier élément de la liste) pour chaque direction
    //si il a les deux point en commun il peut mettre la tuile

    /**
     * Cette methode va permettre de comparer les couleurs ou les formes a appliquer sur la ligne
     * en comparant les tuiles adjacentes
     * @param t1
     * @param t2
     * @param t3
     * @return
     */
    private boolean   isValidColorOrShape(Tile t1, Tile t2, Tile t3) {
        if (t2 == null) {
            return true;
        }
        if (t3 == null) {
            return (t1.color() == t2.color()) || (t1.shape() == t2.shape()); //si
        }
        return (t1.color() == t2.color() && t1.color() == t3.color()) || (t1.shape() == t2.shape() && t1.shape() == t3.shape());
    }

    private boolean hasNeighbor(int row, int col, Tile tile) {//va vérifier juste si la tuile a des voisins

        if (tiles[row][col - 1] != null) { // Vérifier la case à gauche
            return true;
        }

        if (tiles[row][col + 1] != null) { // Vérifier la case à droite
            return true;
        }
        if (tiles[row - 1][col] != null) { // Vérifier la case en haut
            return true;
        }

        if (tiles[row + 1][col] != null) { // Vérifier la case en bas
            return true;
        }

        return false;
    }
    private boolean hasCorrectNeighbor(int row,int col, Tile tile){


        return isValidColorOrShape(tile,get(row-1,col),get(row-2,col))//haut,
                && isValidColorOrShape(tile,get(row,col+1),get(row,col+2))//droite
                && isValidColorOrShape(tile,get(row+1,col),get(row+2,col))//bas
                && isValidColorOrShape(tile,get(row,col-1),get(row,col-2));//gauche

    }

    private boolean hasValidNeighbor(int row,int col, Tile tile){
        return hasNeighbor(row ,col,tile) && hasCorrectNeighbor(row,col,tile);
    }


    /**
     * Cette methode va prendre une direction et vérifier si il n' y a pas plus 6 tuiles sur la meme ligne et si il n'y a pas la meme tuile et
     * elle va faire de meme avec la direction opposée
     * @param row
     * @param col
     * @param tile
     * @param d
     * @param max
     * @return
     */
    private boolean isValidDoublonByAxe(int row, int col, Tile tile, Direction d , int max){


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

        while (get(lg,cln) != null && cptdir1 < max) {

            if(get(lg,cln).equals(tile)){//vérifie les doublons
                throw new QwirkleException("on ne peut pas avoir la meme tuiole sur la meme ligne");
            }
            cptdir1++;
            lg += deltaRow;
            cln += deltaColumn;
        }

        lgoposite  += deltaRowOpposite;
        clnoppiste += deltaColumnOpposite;

        while ( get(lgoposite,clnoppiste) != null && cptdir2 < max) {

            if(get(lgoposite,clnoppiste).equals(tile)){//vérifie les doublons
                throw new QwirkleException("on ne peut pas avoir la meme tuiole sur la meme ligne");
            }
            cptdir2++;
            lgoposite  += deltaRowOpposite;
            clnoppiste += deltaColumnOpposite;

        }
        if ((cptdir1 + cptdir2) >= max) {
            return false;
        }
        return true;
    }


    /**
     * Cette methode va vérifier si il n'y a pas de doublons sur la meme ligne et qu'on ne dépasse *
     * pas les 6 tuiles max et ceci dans toutes les direction
     * @param row
     * @param col
     * @param tile
     * @param max
     * @return
     */
    private boolean isValidNbAndDoublon(int row, int col, Tile tile, int max){
        return isValidDoublonByAxe(row, col, tile, Direction.UP, max) && isValidDoublonByAxe(row, col, tile, Direction.RIGHT, max);
    }


    /**
     * Cette methode va permettre de placer plusieurs tuiles à la suite dans une direction donnée
     * @param row
     * @param col
     * @param d
     * @param line
     */
    public void add(int row, int col, Direction d, Tile... line) {

        int deltaRow = d.getDeltaRow();
        int deltaColumn = d.getDeltaColumn();
        int lg = row;
        int cln = col;

        add(lg, cln, line[0]);
        lg += deltaRow;
        cln += deltaColumn;

        for (int i = 1; i < line.length; i++) {
            add(lg, cln, line[i]);
            lg += deltaRow;
            cln += deltaColumn;
        }


    }

    /**
     * Cette methode va permettre de placer plusieurs tuiles pas à la suite des autres
     * mais va s'assurer que le joueur les places sur la même ligne ou la meme colonne
     * @param tile
     */
    public void add(TileAtPosition... tile) {
        List<TileAtPosition> maliste = new ArrayList<>();

        try {
            if(sameRowOrCol(tile)){
                for (TileAtPosition t :tile) {
                    add(t.row(), t.col(), t.tile());
                    maliste.add(t);
                }
            }else {
                throw new QwirkleException("pas meme ligne ou col");
            }

        }catch(QwirkleException e){
            maliste.stream().forEach(t->tiles[t.row()][t.col()] = null);//remettre à null les tuiles mises auparavant
            System.out.println(e.getMessage());

        }
    }

    /**
     * Cette methode va s'occuper de mettre comparer la ligne ou la colonne de toutes les pieces données en paramètre
     * utilisation de stream avec le filtre (compter le nombre de tuiles qui respectent la condition)
     * @param tile le nombres de tuiles à mettre
     * @return un boolean qui vérifie si la nombre de tuiles est le meme que celles qui respectent la condition
     */
    private boolean sameRowOrCol(TileAtPosition... tile){
        int row = tile[0].row();
        int col = tile[0].col();
        Long  a = Arrays.stream(tile).filter(t->t.col()==col).count();
        Long b = Arrays.stream(tile).filter(t->t.row()==row).count();

        return a==tile.length || b==tile.length;
    }


    /**
     * Cette methode permet de nous dire si la grille est vide ou pas
     * @return
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







