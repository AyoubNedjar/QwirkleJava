package g58183.qwirkle.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

/**

 The Player class represents a player in the Qwirkle game.

 Each player has a name and a list of tiles in their hand.
 */
public class Player implements Serializable {
    private final String nom;
    private final List<Tile> tiles;
    private  int score;

    /**

     Constructor for Player class.
     @param nom the name of the player.
     */
    public Player(String nom) {
        this.nom = nom;
        tiles = new ArrayList<>();
        this.score = 0;
    }
    /**

     Returns the name of the player.
     @return the name of the player.
     */
    public String getNom() {
        return nom;
    }

    /**
     Returns the score of the player.
     @return the score of the player.
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Sets the score of the player.
     *
     * @param score the new score to be set
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Adds the specified number of points to the player's score.
     *
     * @param points the number of points to be added
     */
    public void addPoints(int points) {
        setScore(getScore() + points);
    }



    /**

     Returns an unmodifiable list of tiles in the player's hand.
     @return an unmodifiable list of tiles in the player's hand.
     */
    public List<Tile> getHand() {return Collections.unmodifiableList(tiles);}
    /**

     Refills the player's hand with tiles from the bag until the player has 6 tiles in their hand.

     If there are no tiles left in the bag, a QwirkleException is thrown.
     */
    public void refill() {
        Tile[] myTiles = Bag.getInstance().getRandomTiles(6 - tiles.size());

        if (myTiles != null) {
            tiles.addAll(Arrays.asList(myTiles));
        } else {
            throw new QwirkleException("There are no more tiles left in the bag.");
        }
    }

    /**

     Removes the specified tiles from the player's hand.
     @param tile the tiles to be removed from the player's hand.
     */
    public void remove(Tile... tile) {

        for (Tile t: tile) {
            tiles.remove(t);
        }

    }
}
