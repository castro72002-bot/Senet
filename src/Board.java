public class Board {

    public Square[] squares;

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

    // التحقق من صحة الحركة دون تنفيذها
    public boolean isMoveValid(Piece piece, int steps) {
        int oldPosition = piece.position;
        if (oldPosition > 30) return false;

        // Special squares that always allow move (either exit or rebirth)
        if (oldPosition == 28 || oldPosition == 29 || oldPosition == 30) return true;
        if (oldPosition == 26 && steps == 5) return true;

        int newPosition = oldPosition + steps;

        // Rule: Must land exactly on 26
        if (oldPosition < 26 && newPosition > 26) return false;

        // Rule: Cannot exceed 30 (except special squares handled above)
        if (newPosition > 30) return false;

        Square targetSquare = squares[newPosition];
        if (targetSquare.occupant != null) {
            // Can only swap if it's an opponent's piece
            return !targetSquare.occupant.owner.equals(piece.owner);
        }

        return true;
    }

    // منطق حركة القطعة
    public boolean movePiece(Piece piece, int steps) {
        int oldPosition = piece.position;
        
        // 1. Handling special squares before moving (26, 28, 29, 30)
        if (oldPosition == 26) {
            if (steps == 5) {
                System.out.println("Rolled a 5! Exiting immediately from House of Happiness (26)");
                exitPiece(piece, oldPosition);
                return true;
            }
            // If not 5, continue normal move
        }
        if (oldPosition == 28) {
            if (steps == 3) {
                exitPiece(piece, oldPosition);
            } else {
                System.out.println("Did not roll a 3! Returning to Rebirth (15)");
                handleReturnToRebirth(piece, oldPosition);
            }
            return true;
        }
        if (oldPosition == 29) {
            if (steps == 2) {
                exitPiece(piece, oldPosition);
            } else {
                System.out.println("Did not roll a 2! Returning to Rebirth (15)");
                handleReturnToRebirth(piece, oldPosition);
            }
            return true;
        }
        if (oldPosition == 30) {
            // Any roll exits from square 30
            exitPiece(piece, oldPosition);
            return true;
        }

        int newPosition = oldPosition + steps;

        // 2. Square 26 (HAPPINESS) rule: Must land exactly
        if (oldPosition < 26 && newPosition > 26) {
            System.out.println("Cannot bypass House of Happiness (26). Pick another piece or wait for exact roll.");
            return false;
        }

        // 3. Exceeding square 30 (General case)
        if (newPosition > 30) {
            System.out.println("Cannot exceed square 30 except via special exit rules (from 28, 29, 30).");
            return false;
        }

        Square targetSquare = squares[newPosition];

        // 4. Handle Square 27 (WATER) immediately upon landing
        if (newPosition == 27) {
            System.out.println("Landed in Water! Returning to Rebirth (15)");
            handleReturnToRebirth(piece, oldPosition);
            return true;
        }

        // 5. Landing and Swapping logic
        if (targetSquare.occupant != null) {
            Piece otherPiece = targetSquare.occupant;
            if (!otherPiece.owner.equals(piece.owner)) {
                System.out.println("Swapping positions: Piece " + piece.owner + " takes " + otherPiece.owner + "'s place");
                targetSquare.occupant = piece;
                piece.position = newPosition;
                squares[oldPosition].occupant = otherPiece;
                otherPiece.position = oldPosition;
                return true;
            } else {
                System.out.println("Square occupied by same color piece. Cannot move.");
                return false;
            }
        } else {
            removePiece(oldPosition);
            placePiece(piece, newPosition);
            return true;
        }
    }

    private void exitPiece(Piece piece, int currentPos) {
        removePiece(currentPos);
        piece.position = 31;
        System.out.println("Piece P" + piece.id + " (" + piece.owner + ") successfully exited!");
    }

    private void handleReturnToRebirth(Piece piece, int currentPos) {
        removePiece(currentPos);
        int rebirthPos = 15;
        if (squares[rebirthPos].occupant == null) {
            placePiece(piece, rebirthPos);
        } else {
            // Search for nearest empty square before 15
            int nearest = rebirthPos - 1;
            while (nearest >= 1 && squares[nearest].occupant != null) {
                nearest--;
            }
            if (nearest >= 1) {
                placePiece(piece, nearest);
                System.out.println("Square 15 occupied, piece placed at square " + nearest);
            } else {
                System.out.println("No empty square found to return to!");
            }
        }
    }

    // Removing piece from square
    public void removePiece(int position) {
        if (position >= 1 && position <= 30) {
            squares[position].occupant = null;
        }
    }

    // Printing board (for testing)
    public void printBoard() {
        System.out.println("\n--- Senet Board (R:Rebirth, H:Happiness, W:Water, 3:Truths, 2:Atoum, F:Horus) ---");
        for (int i = 1; i <= 30; i++) {
            String content = "  ";
            if (squares[i].occupant != null) {
                content = squares[i].occupant.owner + squares[i].occupant.id;
            }

            // Special square symbols
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

            if (i == 10 || i == 20) System.out.println();
        }
        System.out.println("\n-------------------------------------------------------------------------");
    }
}
