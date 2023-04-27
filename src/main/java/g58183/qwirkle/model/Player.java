package g58183.qwirkle.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

public class Player {
    private final String nom;
    private List<Tile> tiles;

    public Player(String nom) {
        this.nom = nom;
        tiles = new ArrayList<>();
    }

    public String getNom() {

        return nom;
    }
    public List<Tile> getHand(){
        return Collections.unmodifiableList(tiles);
    }

    /**
     * qui remplit la main du joueur c’est-à-dire qui complète la
     * liste de tuiles pour que le joueur en ait 6 en piochant dans le sac de tuiles
     *
     */
    public void refill(){
        Tile[] mestuiles = Bag.getInstance().getRandomTiles(6- tiles.size());

        if(mestuiles != null){
            for (Tile t: mestuiles) {
                tiles.add(t);
            }
        }else {
            throw new QwirkleException("il n'y a plus de tuiles dans la pioche");
        }
    }

    /**
     * qui retire effectivement des tuiles de la main du
     * joueur.
     * @param tile
     */
    public void remove(Tile... tile){
       /* for (int i = 0; i < tile.length; i++) {
            tiles.remove(tile[i]);
        }*/
        tiles.removeAll(Arrays.stream(tile).toList());//supprime une liste d'une liste
    }


}
