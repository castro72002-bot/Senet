public class Square {
    public int index;            // رقم المربع (1..30)
    public Piece occupant;       // القطعة الموجودة (null إذا فارغ)

    public Square(int index) {
        this.index = index;
        this.occupant = null;
    }

    public Square copy() {
        Square newSquare = new Square(this.index);
        // لا ننسخ الـ occupant هنا مباشرة، سنقوم بربطه في كلاس Board لضمان الربط مع النسخ الجديدة من القطع
        return newSquare;
    }

    public boolean isEmpty() {
        return occupant == null;
    }
}
