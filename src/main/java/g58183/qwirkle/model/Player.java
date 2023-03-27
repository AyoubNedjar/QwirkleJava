package g58183.qwirkle.model;

import java.util.List;
import java.util.Collections;

public class Player {
    private String nom;
    private List<Tile> liste;

    public Player(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }
    public List<Tile> getHand(){
        return Collections.unmodifiableList(liste);
    }
    public void refill(){
        Tile[] mestuiles = Bag.getInstance().getRandomTiles(6- liste.size());

        if(mestuiles != null){
            for (Tile t: mestuiles) {
                liste.add(t);
            }
        }
    }
    public void remove(Tile... tile){
        for (int i = 0; i < tile.length; i++) {
            liste.remove(tile[i]);
        }
    }
}
