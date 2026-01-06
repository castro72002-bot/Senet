import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {
    public Board board;
    public List<Piece> whitePieces;
    public List<Piece> blackPieces;
    public String currentTurn; // "W" or "B"
    private Random random;
    private Scanner scanner;

    public Game() {
        board = new Board();
        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();
        random = new Random();
        scanner = new Scanner(System.in);
        currentTurn = "W"; // يبدأ اللاعب صاحب القطع البيضاء
        initializePieces();
    }

    private void initializePieces() {
        // تهيئة 7 قطع لكل لاعب ووضعها في بداية اللوحة بشكل متبادل (كما في السينيت التقليدية)
        for (int i = 1; i <= 7; i++) {
            Piece white = new Piece(i, "W", 0);
            Piece black = new Piece(i, "B", 0);
            whitePieces.add(white);
            blackPieces.add(black);
            
            // وضع القطع في المربعات الأولى بشكل متبادل (1:W, 2:B, 3:W, 4:B...)
            board.placePiece(white, (i * 2) - 1);
            board.placePiece(black, i * 2);
        }
    }

    public int rollDice() {
        return random.nextInt(5) + 1; // من 1 إلى 5
    }

    public void playTurn() {
        System.out.println("\n player turn " + (currentTurn.equals("W") ? "white" : "black"));
        int diceValue = rollDice();
        System.out.println("dice roll result: " + diceValue);

        List<Piece> currentPieces = currentTurn.equals("W") ? whitePieces : blackPieces;
        
        // عرض الخيارات المتاحة للاعب (القطع التي لم تخرج بعد)
        List<Piece> movablePieces = new ArrayList<>();
        for (Piece p : currentPieces) {
            if (p.position <= 30) {
                movablePieces.add(p);
            }
        }

        if (movablePieces.isEmpty()) {
            System.out.println("no movable pieces for this player!");
            return;
        }

        System.out.println("choose a piece to move (ID):");
        for (Piece p : movablePieces) {
            System.out.print("P" + p.id + "(Pos:" + p.position + ") ");
        }
        System.out.println();

        int choice = -1;
        while (choice == -1) {
            try {
                System.out.println("choose a piece to move (ID):");
                choice = Integer.parseInt(scanner.nextLine());
                Piece selectedPiece = null;
                for (Piece p : movablePieces) {
                    if (p.id == choice) {
                        selectedPiece = p;
                        break;
                    }
                }

                if (selectedPiece != null) {
                    boolean moved = board.movePiece(selectedPiece, diceValue);
                    if (!moved) {
                        System.out.println("did not move the piece. choose another piece:");
                        choice = -1; // إعادة المحاولة بنفس قيمة النرد
                    }
                } else {
                    System.out.println("invalid piece ID, try again:");
                    choice = -1;
                }
            } catch (Exception e) {
                System.out.println("invalid input, enter a valid piece ID:");
                choice = -1;
            }
        }

        // تبديل الأدوار
        currentTurn = currentTurn.equals("W") ? "B" : "W";
        board.printBoard();
    }

    public boolean isGameOver() {
        // فحص إذا أخرج أحد اللاعبين جميع قطعه (التي أصبحت في الموقع 31)
        boolean allWhiteOut = whitePieces.stream().allMatch(p -> p.position > 30);
        boolean allBlackOut = blackPieces.stream().allMatch(p -> p.position > 30);

        if (allWhiteOut) {
            System.out.println("game over! white player wins!");
            return true;
        }
        if (allBlackOut) {
            System.out.println("game over! black player wins!");
            return true;
        }
        return false;
    }
}
