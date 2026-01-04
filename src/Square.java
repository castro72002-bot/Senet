public class Square {
    public int index;            // رقم المربع (1..30)
    public SquareType type;      // نوعه
    public Piece occupant;       // القطعة الموجودة (null إذا فارغ)

    public Square(int index, SquareType type) {
        this.index = index;
        this.type = type;
        this.occupant = null;
    }

    public boolean isEmpty() {
        return occupant == null;
    }
}
