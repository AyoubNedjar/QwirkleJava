package g58183.qwirkle.model;

import java.io.Serializable;

public class GridView implements Serializable {

    private Grid grid;
    public GridView(Grid grid) {
        this.grid = grid;
    }
    public Tile get(int row, int col){
        return this.grid.get(row, col);
    }
    public boolean isEmpty(){
        return this.grid.isEmpty();
    }
    public int getsize(){
        return grid.getTiles().length;
    }
}
