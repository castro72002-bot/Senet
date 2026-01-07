public class Board {
    public Square[] squares;
    public boolean silentMode = false;

    public Board() {
        squares = new Square[31];
        initializeSquares();
    }

    public void initializeSquares() {
        for (int i = 1; i <= 30; i++) {
            squares[i] = new Square(i);
        }
    }

    public Square getSquare(int index) {
        if (index < 1 || index > 30) return null;
        return squares[index];
    }

    public void placePiece(Piece piece, int position) {
        if (position > 30) return;
        squares[position].occupant = piece;
        piece.position = position;
    }

    public boolean isMoveValid(Piece piece, int steps) {
        int oldPosition = piece.position;
        if (oldPosition > 30) return false;
        if (oldPosition == 28 || oldPosition == 29 || oldPosition == 30) return true;
        if (oldPosition == 26 && steps == 5) return true;
        int newPosition = oldPosition + steps;
        if (oldPosition < 26 && newPosition > 26) return false;
        if (newPosition > 30) return false;
        Square targetSquare = squares[newPosition];
        if (targetSquare.occupant != null) {
            return !targetSquare.occupant.owner.equals(piece.owner);
        }
        return true;
    }

    public boolean movePiece(Piece piece, int steps) {
        int oldPosition = piece.position;
        if (oldPosition == 26) {
            if (steps == 5) {
                if (!silentMode) System.out.println("Rolled a 5! Exiting immediately from House of Happiness (26)");
                exitPiece(piece, oldPosition);
                return true;
            }
        }
        if (oldPosition == 28) {
            if (steps == 3) {
                exitPiece(piece, oldPosition);
            } else {
                if (!silentMode) System.out.println("Did not roll a 3! Returning to Rebirth (15)");
                handleReturnToRebirth(piece, oldPosition);
            }
            return true;
        }
        if (oldPosition == 29) {
            if (steps == 2) {
                exitPiece(piece, oldPosition);
            } else {
                if (!silentMode) System.out.println("Did not roll a 2! Returning to Rebirth (15)");
                handleReturnToRebirth(piece, oldPosition);
            }
            return true;
        }
        if (oldPosition == 30) {
            exitPiece(piece, oldPosition);
            return true;
        }
        int newPosition = oldPosition + steps;
        if (oldPosition < 26 && newPosition > 26) {
            if (!silentMode) System.out.println("Cannot bypass House of Happiness (26). Pick another piece or wait for exact roll.");
            return false;
        }
        if (newPosition > 30) {
            if (!silentMode) System.out.println("Cannot exceed square 30 except via special exit rules (from 28, 29, 30).");
            return false;
        }
        Square targetSquare = squares[newPosition];
        if (newPosition == 27) {
            if (!silentMode) System.out.println("Landed in Water! Returning to Rebirth (15)");
            handleReturnToRebirth(piece, oldPosition);
            return true;
        }
        if (targetSquare.occupant != null) {
            Piece otherPiece = targetSquare.occupant;
            if (!otherPiece.owner.equals(piece.owner)) {
                targetSquare.occupant = piece;
                piece.position = newPosition;
                squares[oldPosition].occupant = otherPiece;
                otherPiece.position = oldPosition;
                return true;
            } else {
                if (!silentMode) System.out.println("Square occupied by same color piece. Cannot move.");
                return false;
            }
        } else {
            removePiece(oldPosition);
            placePiece(piece, newPosition);
            return true;
        }
    }

    public void exitPiece(Piece piece, int currentPos) {
        removePiece(currentPos);
        piece.position = 31;
        if (!silentMode) System.out.println("Piece" + piece.id + " (" + piece.owner + ") successfully exited!");
    }

    public void forceReturnToRebirth(Piece piece) {
        int oldPos = piece.position;
        if (oldPos == 28 || oldPos == 30 || oldPos == 29) {
            if (!silentMode) System.out.println("Penalty! Piece P" + piece.id + " was on square " + oldPos + " but not moved. Returning to Rebirth (15).");
            handleReturnToRebirth(piece, oldPos);
        }
    }

    private void handleReturnToRebirth(Piece piece, int currentPos) {
        removePiece(currentPos);
        int rebirthPos = 15;
        if (squares[rebirthPos].occupant == null) {
            placePiece(piece, rebirthPos);
        } else {
            int nearest = rebirthPos - 1;
            while (nearest >= 1 && squares[nearest].occupant != null) {
                nearest--;
            }
            if (nearest >= 1) {
                placePiece(piece, nearest);
                if (!silentMode) System.out.println("Square 15 occupied, piece placed at square " + nearest);
            } else {
                if (!silentMode) System.out.println("No empty square found to return to!");
            }
        }
    }

    public void removePiece(int position) {
        if (position >= 1 && position <= 30) {
            squares[position].occupant = null;
        }
    }

    public void printBoard() {
        System.out.println("\nSenet Board (R:Rebirth, H:Happiness, W:Water, 3:Truths, 2:Atoum, F:Horus)");
        System.out.println("-----------------------------------------------------------");
        for (int i = 1; i <= 10; i++) {
            printSingleSquare(i);
        }
        System.out.println();
        for (int i = 20; i >= 11; i--) {
            printSingleSquare(i);
        }
        System.out.println();
        for (int i = 21; i <= 30; i++) {
            printSingleSquare(i);
        }
        System.out.println("\n-------------------------------------------------------------------------");
    }

    private void printSingleSquare(int i) {
        String content = "  ";
        if (squares[i].occupant != null) {
            content = squares[i].occupant.owner + squares[i].occupant.id;
        }
        String special = " ";
        switch (i) {
            case 15 -> special = "R";
            case 26 -> special = "H";
            case 27 -> special = "W";
            case 28 -> special = "3";
            case 29 -> special = "2";
            case 30 -> special = "F";
        }

        System.out.printf("[%2s%s] ", content, special);
    }
}
