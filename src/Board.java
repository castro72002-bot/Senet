public class Board {

    public Square[] squares;

    public Board() {
        squares = new Square[31]; // نهمل index 0
        initializeSquares();
    }

    private void initializeSquares() {
        for (int i = 1; i <= 30; i++) {
            SquareType type = SquareType.NORMAL;

            switch (i) {
                case 15 -> type = SquareType.REBIRTH;
                case 26 -> type = SquareType.HAPPINESS;
                case 27 -> type = SquareType.WATER;
                case 28 -> type = SquareType.THREE_TRUTHS;
                case 29 -> type = SquareType.RE_ATOUM;
                case 30 -> type = SquareType.HORUS;
            }

            squares[i] = new Square(i, type);
        }
    }

    // الحصول على مربع
    public Square getSquare(int index) {
        if (index < 1 || index > 30) return null;
        return squares[index];
    }

    // وضع قطعة على مربع
    public void placePiece(Piece piece, int position) {
        squares[position].occupant = piece;
        piece.position = position;
    }

    // إزالة قطعة من مربع
    public void removePiece(int position) {
        squares[position].occupant = null;
    }

    // طباعة البورد (للتجربة)
    public void printBoard() {
        for (int i = 1; i <= 30; i++) {
            if (squares[i].occupant == null)
                System.out.print("[  ] ");
            else
                System.out.print("[P" + squares[i].occupant.owner + "] ");

            if (i == 10 || i == 20) System.out.println();
        }
        System.out.println();
    }
}
