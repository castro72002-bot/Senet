import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {
    public Board board;
    public List<Piece> whitePieces;
    public List<Piece> blackPieces;
    public String currentTurn; 
    public boolean isVsComputer;
    public Random random;
    public Scanner scanner;

    public Game(boolean vsComputer) {
        board = new Board();
        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();
        random = new Random();
        scanner = new Scanner(System.in);
        currentTurn = "W";
        this.isVsComputer = vsComputer;
        initializePieces();
    }

    public void initializePieces() {
        for (int i = 1; i <= 7; i++) {
            Piece white = new Piece(i, "W", 0);
            Piece black = new Piece(i, "B", 0);
            whitePieces.add(white);
            blackPieces.add(black);
            board.placePiece(white, (i * 2) - 1);
            board.placePiece(black, i * 2);
        }
    }

    public int rollDice() {
        return random.nextInt(5) + 1;
    }

    public void playTurn() {
        System.out.println("\nPlayer turn: " + (currentTurn.equals("W") ? "White" : "Black"));
        int diceValue = rollDice();
        System.out.println("Dice roll result: " + diceValue);

        // If it's the computer's turn (Black) and it's a PvE game
        if (isVsComputer && currentTurn.equals("B")) {
            System.out.println("Computer is thinking...");
            GameState currentState = new GameState(board, whitePieces, blackPieces, currentTurn);
            int bestPieceId = Expectiminimax.getBestMove(currentState, diceValue);

            if (bestPieceId != -1) {
                Piece selectedPiece = null;
                for (Piece p : blackPieces) {
                    if (p.id == bestPieceId) {
                        selectedPiece = p;
                        break;
                    }
                }
                board.movePiece(selectedPiece, diceValue);
                System.out.println("Computer chose to move piece P" + bestPieceId);
            } else {
                System.out.println("Computer has no valid moves!");
            }
        } else {
            // Manual play
            List<Piece> currentPieces = currentTurn.equals("W") ? whitePieces : blackPieces;
            List<Piece> movablePieces = new ArrayList<>();
            for (Piece p : currentPieces) {
                if (p.position <= 30) {
                    movablePieces.add(p);
                }
            }

            if (movablePieces.isEmpty()) {
                System.out.println("No pieces left for this player!");
            } else {
                // Check if any piece can actually move with this dice value
                boolean anyPossibleMove = false;
                for (Piece p : movablePieces) {
                    if (board.isMoveValid(p, diceValue)) {
                        anyPossibleMove = true;
                        break;
                    }
                }

                if (!anyPossibleMove) {
                    System.out.println("No valid moves possible with dice roll: " + diceValue + ". Skipping turn.");
                } else {
                    System.out.println("Choose a piece ID to move:");
                    for (Piece p : movablePieces) {
                        if (board.isMoveValid(p, diceValue)) {
                            System.out.print("P" + p.id + "(Pos:" + p.position + ") ");
                        }
                    }
                    System.out.println();

                    int choice = -1;
                    while (choice == -1) {
                        try {
                            String input = scanner.next();
                            choice = Integer.parseInt(input);
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
                                    System.out.println("Invalid move. Please choose another piece:");
                                    choice = -1;
                                }
                            } else {
                                System.out.println("Invalid piece ID. Try again:");
                                choice = -1;
                            }
                        } catch (Exception e) {
                            System.out.println("Please enter a valid number:");
                            choice = -1;
                        }
                    }
                }
            }
        }

        // Switch turns
        currentTurn = currentTurn.equals("W") ? "B" : "W";
        board.printBoard();
    }

    public boolean isGameOver() {
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
