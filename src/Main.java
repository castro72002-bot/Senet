public class Main {
    public static void main(String[] args) {
        Board board = new Board();
        Square HouseOfRebirth = new Square(15, SquareType.REBIRTH);
        Square HouseOfHappiness = new Square(26, SquareType.HAPPINESS);
        Square HouseOfWater = new Square(27, SquareType.WATER);
        Square HouseOfThreeTruths = new Square(28, SquareType.THREE_TRUTHS);
        Square HouseOfReAtoum = new Square(29, SquareType.RE_ATOUM);
        Square HouseOfHorus = new Square(30, SquareType.HORUS);
        Piece p1 = new Piece(1, "B", 1);
        Piece p2 = new Piece(2, "W", 2);
        Piece p3 = new Piece(3, "B" , 3 );
        Piece p4 = new Piece(4, "W" , 4 );

        board.placePiece(p1, 1);
        board.placePiece(p2, 2);
        board.placePiece(p3, 3);
        board.placePiece(p4, 4);

        board.printBoard();
    }
}
