package g58183.qwirkle.model;

public enum Direction {

    LEFT(0, -1),RIGHT(0, 1),HIGH(-1, 0),DOWN(1, 0);

    private final int  deltaRow;
    private final int deltaColumn;

    Direction(int deltaRow, int deltaColumn) {
        this.deltaRow = deltaRow;
        this.deltaColumn = deltaColumn;
    }

    public int getDeltaRow() {
        return deltaRow;
    }

    public int getDeltaColumn() {
        return deltaColumn;
    }

    public Direction opposite(){
        switch (this){
            case DOWN : return HIGH;
            case LEFT:  return RIGHT;
            case HIGH: return DOWN;
            case RIGHT: return LEFT;

        }
        return null;
    }
}
