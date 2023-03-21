package g58183.qwirkle.model;

import java.util.List;

/**
 * Bag represent the bag of tiles
 */
public class Bag {

    private static Bag instance = null;
    private List<Tile> tiles;

    /**
     * initializes the 108 tiles in the bag only once
     */
    private Bag() {
        Shape[] formes = {Shape.CROSS, Shape.SQUARE, Shape.ROUND, Shape.STAR, Shape.PLUS, Shape.DIAMOND};
        Color[] couleurs = {Color.BLUE, Color.RED, Color.GREEN, Color.ORANGE, Color.YELLOW, Color.PURPLE};


        for (int i = 0; i < couleurs.length; i++) {
            for (int j = 0; j < formes.length; j++) {
                for (int k = 0; k < 3; k++) {
                    tiles.add(new Tile(couleurs[i], formes[j]));
                }

            }

        }

    }

    /**
     * instantiation check of a single tile bag
     * @return
     */
    public static Bag getInstance() {
        if (instance == null) {
            return new Bag();
        }
        return instance;
    }


    /**
     * gives random tiles in the bag
     * @param n which represents the number of tiles to flip
     * @return the number of tiles
     */
    public Tile[] getRandomTiles(int n) {


        int nbtuiles = tiles.size();
        if (nbtuiles == 0) {
            return null;
        }

        //si le nombre de tuiles restantes est inferieur à n
        if (nbtuiles < n) {
            Tile[] tab = new Tile[nbtuiles];
            for (int i = 0; i < nbtuiles; i++) {
                int index = (int) (Math.random() * nbtuiles);
                tab[i] = tiles.get(index);
                tiles.remove(index);
                nbtuiles--;
            }
            return tab;
            //on retourne un tableau contenant les tuiles restantes

        } else {
            Tile[] tab2 = new Tile[n];
            for (int i = 0; i < n; i++) {
                int index = (int) (Math.random() * nbtuiles);
                tab2[i] = tiles.get(index);
                tiles.remove(index);
                nbtuiles--;
            }
            return tab2;
        }

    }

    /**
     * give the size of the bag
     * @return the numbers of tiles in the bag
     */
    public int size() {
        return tiles.size();
    }
}
