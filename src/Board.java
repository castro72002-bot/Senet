public class Board {
    public Square[] squares;
    public boolean silentMode = false;

    public Board() {
        squares = new Square[31];
        init();
    }

    public void init() {
        for (int i = 1; i <= 30; i++) {
            squares[i] = new Square(i);
        }
    }

    public Square getSquare(int idx) {
        if (idx < 1 || idx > 30) return null;
        return squares[idx];
    }

    public void placePiece(Piece p, int pos) {
        if (pos > 30) return;
        squares[pos].occupant = p;
        p.position = pos;
    }

    public boolean isMoveValid(Piece p, int steps) {
        int oldPos = p.position;
        if (oldPos > 30) return false;
        
        if (oldPos == 28 || oldPos == 29 || oldPos == 30) return true;
        if (oldPos == 26 && steps == 5) return true;

        int newPos = oldPos + steps;
        
        if (oldPos < 26 && newPos > 26) return false;
        if (newPos > 30) return false;

        Square target = squares[newPos];
        if (target.occupant != null) {
            return !target.occupant.owner.equals(p.owner);
        }
        return true;
    }

    public boolean movePiece(Piece p, int steps) {
        int oldPos = p.position;

        if (oldPos == 26) {
            if (steps == 5) {
                if (!silentMode) System.out.println("Got a 5! Exiting from 26");
                exitPiece(p, oldPos);
                return true;
            }
        }

        if (oldPos == 28) {
            if (steps == 3) {
                exitPiece(p, oldPos);
            } else {
                if (!silentMode) System.out.println("Back to Rebirth (15) - result wasn't 3");
                sendToRebirth(p, oldPos);
            }
            return true;
        }

        if (oldPos == 29) {
            if (steps == 2) {
                exitPiece(p, oldPos);
            } else {
                if (!silentMode) System.out.println("Back to Rebirth (15) - result wasn't 2");
                sendToRebirth(p, oldPos);
            }
            return true;
        }

        if (oldPos == 30) {
            exitPiece(p, oldPos);
            return true;
        }

        int newPos = oldPos + steps;

        if (oldPos < 26 && newPos > 26) {
            if (!silentMode) System.out.println("Stop at square 26 first!");
            return false;
        }

        if (newPos > 30) {
            if (!silentMode) System.out.println("Can't go past 30");
            return false;
        }

        if (newPos == 27) {
            if (!silentMode) System.out.println("Landed in Water! Rebirth time.");
            sendToRebirth(p, oldPos);
            return true;
        }

        Square target = squares[newPos];
        if (target.occupant != null) {
            Piece other = target.occupant;
            if (!other.owner.equals(p.owner)) {
                target.occupant = p;
                p.position = newPos;
                squares[oldPos].occupant = other;
                other.position = oldPos;
                return true;
            } else {
                if (!silentMode) System.out.println("Blocked by your own piece");
                return false;
            }
        } else {
            removePiece(oldPos);
            placePiece(p, newPos);
            return true;
        }
    }

    public void exitPiece(Piece p, int pos) {
        removePiece(pos);
        p.position = 31;
        if (!silentMode) System.out.println("Piece " + p.id + " is out!");
    }

    public void forceReturnToRebirth(Piece p) {
        int pos = p.position;
        if (pos == 28 || pos == 30 || pos == 29) {
            if (!silentMode) System.out.println("Penalty: Piece " + p.id + " didn't move from " + pos);
            sendToRebirth(p, pos);
        }
    }

    public void sendToRebirth(Piece p, int current) {
        removePiece(current);
        int rebirth = 15;
        if (squares[rebirth].occupant == null) {
            placePiece(p, rebirth);
        } else {
            int n = rebirth - 1;
            while (n >= 1 && squares[n].occupant != null) {
                n--;
            }
            if (n >= 1) {
                placePiece(p, n);
            }
        }
    }

    public void removePiece(int pos) {
        if (pos >= 1 && pos <= 30) {
            squares[pos].occupant = null;
        }
    }

    public void printBoard() {
        System.out.println("\n--- Senet Board ---");
        for (int i = 1; i <= 10; i++) drawSquare(i);
        System.out.println();
        for (int i = 20; i >= 11; i--) drawSquare(i);
        System.out.println();
        for (int i = 21; i <= 30; i++) drawSquare(i);
        System.out.println("\n-------------------");
    }

    public void drawSquare(int i) {
        String txt = "  ";
        if (squares[i].occupant != null) {
            txt = squares[i].occupant.owner + squares[i].occupant.id;
        }
        char mark = ' ';
        switch (i) {
            case 15 -> mark = 'R';
            case 26 -> mark = 'H';
            case 27 -> mark = 'W';
            case 28 -> mark = '3';
            case 29 -> mark = '2';
            case 30 -> mark = 'F';
        }
        System.out.printf("[%2s%c] ", txt, mark);
    }
}
