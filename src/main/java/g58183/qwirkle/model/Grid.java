package g58183.qwirkle.model;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    private Tile[][] tiles;
    private boolean isEmpty;

    public Grid() {
        this.tiles = new Tile[91][91];
        this.isEmpty = true;
    }


    public Tile[][] getTiles() {
        return tiles;
    }

    public Tile get(int row, int col) {
        return tiles[row][col];
    }

    /**
     * This method can only be used once when there are no
     * deposited tiles.
     * She will place the tiles "in the middle" of
     * the game board (the first tile will be placed in 45.45)
     * varargs for divers tiles
     */
    public void firstAdd(Direction d, Tile... line) {

        int ligne = d.getDeltaRow();
        int col = d.getDeltaColumn();
        int l = 45;               // t,T,T
        int c = 45;
        Color couleur = null;
        Shape forme = null;
        List<Tile> maliste = new ArrayList<>();

        if (line.length == 1) {
            tiles[45][45] = line[0];
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

        if (row > 0 && row < 90 && col > 0 && col < 90) {
            if (tiles[row][col] == null) {
                if (hasAdjacent(row, col)) {

                    tiles[row][col] = tile;
                } else {
                    throw new QwirkleException("La tuile n'a pas de voisin ");
                }

            } else {
                throw new QwirkleException("Une tuile existe déja à cet emplacement");
            }
        } else {
            throw new QwirkleException("la position ne se trouve pas sur le plateau ");
        }

    }

    private boolean hasAdjacent(int row, int col) {
        //elle ne doit pas se retrouver au milieu de null part

        if (row > 0 && tiles[row - 1][col] != null) { // Vérifier la case au-dessus
            return true;
        }
        if (row < 90 && tiles[row + 1][col] != null) { // Vérifier la case en-dessous
            return true;
        }
        if (col > 0 && tiles[row][col - 1] != null) { // Vérifier la case à gauche
            return true;
        }
        if (col < 90 && tiles[row][col + 1] != null) { // Vérifier la case à droite
            return true;
        }

        return false;

    }




                   /* while (tiles[nvlg][nvcol]!=null && cpt < 2){


        nvlg = nvlg+row;
        nvcol = nvcol + col;
    }*/


}




/* if (tiles[row][col] != null) {


                //j 'en registre les directionn auqueles apartiennent deja une ligne de tuiles
                Direction[] d = Direction.values();
                List<Direction> maliste = new ArrayList<>();

                for (int i = 0; i < d.length; i++) {
                    int ligne = d[i].getDeltaRow();
                    int colonne = d[i].getDeltaColumn();

                    int nvligne = ligne + row;
                    int nvcolonne = colonne + col;

                    if (tiles[nvligne][nvcolonne] != null) {
                        maliste.add(d[i]);
                    }
                }
                if (!maliste.isEmpty()) {
                    int taille = maliste.size();

                    if (taille == 1) {// je dois alors trouver juste le point en commun des deux suivante pour comparer
                        Direction dir = maliste.get(0);

                        int deltal = maliste.get(0).getDeltaRow();
                        int deltac = maliste.get(0).getDeltaColumn();
                        int nvlg = deltal + row;
                        int nvcol = deltac + col;


                        //Color couleur = null;
                        //Shape forme = null;
                        // probleme  =  je dois verifier deja si il existe au moins deux tuiles pour connaitre le point en commun
                        //je dois savoir le lien en commun des deux premiere tuiles  pour continuer à ajouter.
                        //if (tiles[nvlg][nvcol].color() == line[1].color() && line[0].shape() != line[1].shape()) {
                        //     couleur = line[0].color();
                        //} else if (line[0].color() != line[1].color() && line[0].shape() == line[1].shape()) {
                        //  forme = line[0].shape();
                        //}


                        //ps = situation : j ai deux tuiles en diagonales et je dois mettre ma tuile au tournant
                        //                :j ai trois tuiles chacune du coter ou je dois deposer la nouvelle tuile
                        //                :j ai 4 tuiles en forme de croix et ma nouvelles tuile doit etre au milieu (si j ai
                        //                 deux tuiles en face a face alors je sais qu'elle on le meme point commun obligatoirement
                        //                  si je veux placer la tuile sinon je n ai pas le drooit


                    } else if (taille == 2) {
                        //je dois voir si la taille est de deux
                        // alors voir si ils sont de direction opposé exemple :  rondRouge Rouge , Rien , Carré roug
                        //ou voir si ils sont juste en diagonal
                        //commencons par opposé
                        Direction one = maliste.get(0);
                        Direction second = maliste.get(0);

                        if (one.opposite() == second) {//cad que je vais plcer ma nouvelle tuile entre les deux
                            Color couleur = null;
                            Shape forme = null;

                            int nvlg = maliste.get(0).getDeltaRow() + row;
                            int nvcol = maliste.get(0).getDeltaColumn() + col;

                            int nvlg2 = maliste.get(1).getDeltaRow() + row;
                            int nvcol2 = maliste.get(1).getDeltaColumn() + col;

                            // probleme  =  je dois verifier deja si il existe au moins deux tuiles pour connaitre le point en commun
                            //il faut que ma tuile aie la meme point en commun que les deux de cotés
                            if (tiles[nvlg][nvcol].color() == tiles[nvlg2][nvcol2].color() && (tiles[nvlg][nvcol].shape() != tiles[nvlg2][nvcol2].shape())) {
                                couleur = tiles[nvlg][nvcol].color();
                            } else if (tiles[nvlg][nvcol].color() != tiles[nvlg2][nvcol2].color() && tiles[nvlg][nvcol].shape() == tiles[nvlg2][nvcol2].shape()) {
                                forme = tiles[nvlg][nvcol].shape();
                            }
                            //si il existe un point en commun entre les deux tuiles de cotés il faut que ma tuile aie le meme point en commun

                            if (tile.color() == couleur || tile.shape() == forme) {
                                //je peux alors la metre le fait que


                            } else {
                                throw new QwirkleException("la tuile n'a aucun point en commun entre les deux autres tuiles");
                            }


                        }

                    }
                    boolean ok = true;
                    for (int i = 0; i < maliste.size(); i++) {

                        //ce sont les direction à suivre
                        Direction dir = maliste.get(i);
                        int nvlg = maliste.get(i).getDeltaRow() + row;
                        int nvcol = maliste.get(i).getDeltaColumn() + col;


                    }

                } else {
                    throw new QwirkleException("l'emplacement n'est sur aucune ligne de tuiles");
                }
            } else {
                throw new QwirkleException("Il y a déja une tuile à cet emplacement");
            }















































/*try {


            List<Tile> maliste = new ArrayList<>();

            int ligne = d.getDeltaRow();
            int col = d.getDeltaColumn();

            //
            tiles[45][45] = line[0];
            maliste.add(line[0]);


            int l = 45;
            int c = 45;



            if (line.length > 1) {

                Color couleur = null;
                Shape forme = null;


                //je dois savoir le lien en commun des deux premiere tuiles  pour continuer à ajouter.
                if (line[0].color() == line[1].color() && line[0].shape() != line[1].shape()) {
                    couleur = line[0].color();
                } else if (line[0].color() != line[1].color() && line[0].shape() == line[1].shape()) {
                    forme = line[0].shape();
                }

                if (couleur == null && forme == null) {
                    throw new QwirkleException("");
                }
                for (int i = 1; i < line.length; i++) {

                    if (!maliste.contains(line[i])) {//si ma liste ne contient pas exactement la meme tuile

                        maliste.add(line[i]);

                        if (line[i].color().equals(couleur) || line[i].shape().equals(forme)) {
                            l += ligne;
                            c += col;
                            tiles[l][c] = line[i];
                        }

                    }else {
                        throw new QwirkleException("Il y'a déja exactement la même tuile sur la ligne");
                    }


                }


            }

        }catch(QwirkleException e){
            System.out.println(e.getMessage());
        }
