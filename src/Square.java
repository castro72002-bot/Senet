public class Square {
    public int index;
    public Piece occupant;

    public Square(int index) {
        this.index = index;
        this.occupant = null;
    }

    public Square copy() {
        Square newSquare = new Square(this.index);
        return newSquare;
    }

    public boolean isEmpty() {
        return occupant == null;
    }
}
