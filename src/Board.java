public class Board {

    public Square[] squares;

    public Board() {
        squares = new Square[31]; // نهمل index 0
        initializeSquares();
    }

    private void initializeSquares() {
        for (int i = 1; i <= 30; i++) {
            squares[i] = new Square(i);
        }
    }

    // الحصول على مربع
    public Square getSquare(int index) {
        if (index < 1 || index > 30) return null;
        return squares[index];
    }

    // وضع قطعة على مربع
    public void placePiece(Piece piece, int position) {
        if (position > 30) return;
        squares[position].occupant = piece;
        piece.position = position;
    }

    // منطق حركة القطعة
    public boolean movePiece(Piece piece, int steps) {
        int oldPosition = piece.position;
        
        // 1. التعامل مع المربعات الخاصة قبل التحريك (26، 28, 29, 30)
        if (oldPosition == 26) {
            if (steps == 5) {
                System.out.println("حصلت على 5! الخروج فورا من مربع السعادة (26)");
                exitPiece(piece, oldPosition);
                return true;
            }
            // إذا لم تكن الرمية 5، يكمل الحركة العادية ليدخل في 27 أو 28 إلخ
        }
        if (oldPosition == 28) {
            if (steps == 3) {
                exitPiece(piece, oldPosition);
            } else {
                System.out.println("did not get 3! returning to square 15");
                handleReturnToRebirth(piece, oldPosition);
            }
            return true;
        }
        if (oldPosition == 29) {
            if (steps == 2) {
                exitPiece(piece, oldPosition);
            } else {
                System.out.println("did not get 2! returning to square 15");
                handleReturnToRebirth(piece, oldPosition);
            }
            return true;
        }
        if (oldPosition == 30) {
            // أي رمية تخرج الحجر من المربع 30
            exitPiece(piece, oldPosition);
            return true;
        }

        int newPosition = oldPosition + steps;

        // 2. قاعدة المربع 26 (HAPPINESS): يجب الهبوط عليه بالضبط
        if (oldPosition < 26 && newPosition > 26) {
            System.out.println("did not get 26! must get 26 to move forward");
            return false; // فشل الحركة
        }

        // 3. إذا تجاوز الحجر المربع 30 (حالة عامة قبل القواعد الخاصة)
        if (newPosition > 30) {
            System.out.println("did not get 30! must get 30 to move forward");      
            return false; // فشل الحركة
        }

        Square targetSquare = squares[newPosition];

        // 4. التعامل مع المربع 27 (WATER) فور الهبوط عليه
        if (newPosition == 27) {
            System.out.println("you must go to Rebirth (15)");
            handleReturnToRebirth(piece, oldPosition);
            return true;
        }

        // 5. منطق الهبوط والتبديل
        if (targetSquare.occupant != null) {
            Piece otherPiece = targetSquare.occupant;
            if (!otherPiece.owner.equals(piece.owner)) {
                System.out.println("did not get 27! must get 27 to move forward");
                targetSquare.occupant = piece;
                piece.position = newPosition;
                squares[oldPosition].occupant = otherPiece;
                otherPiece.position = oldPosition;
                return true;
            } else {
                System.out.println("did not get 27! must get 27 to move forward");
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
        System.out.println("P" + piece.id + " (" + piece.owner + ") exited the board successfully!");
    }

    private void handleReturnToRebirth(Piece piece, int currentPos) {
        removePiece(currentPos);
        int rebirthPos = 15;
        if (squares[rebirthPos].occupant == null) {
            placePiece(piece, rebirthPos);
        } else {
            // البحث عن أقرب مربع فارغ قبل 15
            int nearest = rebirthPos - 1;
            while (nearest >= 1 && squares[nearest].occupant != null) {
                nearest--;
            }
            if (nearest >= 1) {
                placePiece(piece, nearest);
                System.out.println("did not get 15! must get 15 to move forward, placed in square " + nearest);
            } else {
                System.out.println("did not get 15! must get 15 to move forward, no empty square found before 15");
            }
        }
    }

    // إزالة قطعة من مربع
    public void removePiece(int position) {
        if (position >= 1 && position <= 30) {
            squares[position].occupant = null;
        }
    }

    // طباعة البورد (للتجربة)
    public void printBoard() {
        System.out.println("\n--- لوحة السينيت (R:Rebirth, H:Happiness, W:Water, 3:Truths, 2:Atoum, F:Horus) ---");
        for (int i = 1; i <= 30; i++) {
            String content = "  ";
            if (squares[i].occupant != null) {
                content = squares[i].occupant.owner + squares[i].occupant.id;
            }

            // إضافة رموز للمربعات الخاصة
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
        System.out.println("\n");
    }
}
