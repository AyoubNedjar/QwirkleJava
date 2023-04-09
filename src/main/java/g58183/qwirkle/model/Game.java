package g58183.qwirkle.model;

import java.util.Arrays;
import java.util.List;

public class Game {
    private Grid grid;
    private Player[] player;
    private int current;

    public Game(List<String> noms) {
        for (int i = 0; i < noms.size() ; i++) {
            player[i] = new Player(noms.get(i));
            player[i].refill();
        }

        grid = new Grid();
        current =0;
    }

    /**
     * récupére les tuiles que le joueur veut de sa main
     * pour les placer sur le plateau
     * @param d la direction dont l'on veut placer les tuiles
     * @param is ce sont les indices des tuiles dans sa mains
     */
    public void first(Direction d, int... is){//2,4,1

        Tile[] tab = new Tile[is.length];// le nb de tuiles qu'il veut placer
        List<Tile> maliste = player[current].getHand();

        //roug, bleu, vert, mauve, orange , jaune
        for (int i = 0; i < is.length; i++) {
            tab[i] = maliste.get(is[i]);//is[i] donne l'indice des tuiles
        }
        try{
            grid.firstAdd(d,tab);
            player[current].remove(tab);
            nextPlayer();
        }catch(QwirkleException e){
            System.out.println(e.getMessage());
        }



    }

    private void nextPlayer(){
        current++;   
        current = current % player.length; //pour que si il depasse il revient au premier
    }

    /**
     * va jouer un coup avec une seule tuile
     * @param row
     * @param col
     * @param index
     */
    public void play(int row , int col, int index){
        try {
            player[current].refill();
            List<Tile> maliste = getCurrentPlayerHand();
            Tile t = maliste.get(index);
            grid.add(row, col,t);
            player[current].remove(t);
            nextPlayer();
        }catch (QwirkleException e){
            System.out.println(e.getMessage());

        }

    }
    public void play(int row, int col, Direction d, int... indexes){
        try{
            player[current].refill();
            List<Tile> maliste = getCurrentPlayerHand();
            Tile[] tab = new Tile[indexes.length];


            for (int i = 0; i < indexes.length; i++) {
                tab[i] = maliste.get(indexes[i]);
            }
            grid.add(row, col,d,tab);
            player[current].remove(tab);
            nextPlayer();
        }catch(QwirkleException e){
            System.out.println(e.getMessage()); 
        }
        ;
    }
    public void play(int... is){//45,45,3,50,55,4

        if(is.length < 3 || is.length % 3 != 0){
            throw new QwirkleException("arguments invalides");
        }

        try{
            player[current].refill();
            List<Tile> maliste = getCurrentPlayerHand();
            int j = 0;
            int i = 0;
            Tile[] tab = new Tile[is.length/3];
            while(i< is.length-2){//s'appliquera juste pour les lignes de chaque tuiles
                int row = is[i]    ;
                int col = is[++i];
                tab[j] = maliste.get(is[++i]);
                j++;
                TileAtPosition t = new TileAtPosition(row,col,tab[j]);
                grid.add(t);
                i++;
            }
            player[current].remove(tab);
            nextPlayer();
        }catch (QwirkleException e){
            System.out.println(e.getMessage());
        }


    }
    public String getCurrentPlayerName(){
        return player[current].getNom();
    }
    public List<Tile> getCurrentPlayerHand(){
        return player[current].getHand();
    }

    public void pass(){
        current++;
    }
}
