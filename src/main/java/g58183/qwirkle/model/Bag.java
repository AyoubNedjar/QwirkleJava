package g58183.qwirkle.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Bag represent the bag of tiles
 */
public class Bag implements Serializable {

    private static Bag instance = null;
    private List<Tile> tiles;

    /**
     * initializes the 108 tiles in the bag only once
     */
    private Bag() {
        Shape[] formes = Shape.values();
        Color[] couleurs = Color.values();
        tiles = new ArrayList<>();

        for (int i = 0; i < couleurs.length; i++) {
            for (int j = 0; j < formes.length; j++) {
                for (int k = 0; k < 3; k++) {
                    tiles.add(new Tile(couleurs[i], formes[j]));
                }

            }

        }

    }

    /**
     * This method returns an instance of the Bag class which represents a single tile bag.
     * If no instance has been created yet, a new one is created and returned. If an instance
     * already exists, that instance is returned.
     *
     * @return an instance of the Bag class
     */
    public static Bag getInstance() {
        if (instance == null) {
            instance = new Bag();
        }
        return instance;
    }

    /**
     * gives random tiles in the bag
     *
     * @param n which represents the number of tiles to flip
     * @return the number of tiles
     */
    public Tile[] getRandomTiles(int n) {
        int nbtuiles = tiles.size();

        if (nbtuiles == 0) {
            return null;
        }

        //si le nombre de tuiles restantes est inferieur à n
        if (nbtuiles <= n) {
            return (Tile[]) tiles.toArray();// cela va convertir la liste en un tableau vu que les restantes on les prend quand meme
//
        } else {
            Tile[] tab2 = new Tile[n];
            for (int i = 0; i < n; i++) {
                int index = (int) (Math.random() * nbtuiles);
                Tile t = tiles.get(index);
                tab2[i] = new Tile(t.color(), t.shape());//cela creer une autre référence car on supprime l'objet juste après
                tiles.remove(index);
                nbtuiles--;
            }
            return tab2;
        }

    }

    /**
     * give the size of the bag
     *
     * @return the numbers of tiles in the bag
     */
    public int size() {
        return tiles.size();
    }
}
