public class Square {
    public int index;            // رقم المربع (1..30)
    public Piece occupant;       // القطعة الموجودة (null إذا فارغ)

    public Square(int index) {
        this.index = index;
        this.occupant = null;
    }

    public boolean isEmpty() {
        return occupant == null;
    }
}
