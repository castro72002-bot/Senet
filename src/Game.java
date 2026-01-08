import java.util.*;

public class Game {
    public Board board;
    public List<Piece> whites;
    public List<Piece> blacks;
    public String turn; 
    public boolean vsAI;
    public Random rng = new Random();
    public Scanner sc = new Scanner(System.in);

    public Game(boolean computer) {
        board = new Board();
        whites = new ArrayList<>();
        blacks = new ArrayList<>();
        turn = "W";
        this.vsAI = computer;
        setup();
    }

    public void setup() {
        for (int i = 1; i <= 7; i++) {
            Piece w = new Piece(i, "W", 0);
            Piece b = new Piece(i, "B", 0);
            whites.add(w);
            blacks.add(b);
            board.placePiece(w, (i * 2) - 1);
            board.placePiece(b, i * 2);
        }
    }

    public int sticks() {
        int sticks_result=0;
        int stick1 = rng.nextInt(2);
        int stick2 = rng.nextInt(2);
        int stick3 = rng.nextInt(2);
        int stick4 = rng.nextInt(2);
        if (stick1 == 0 && stick2 == 0 && stick3 == 0 && stick4 == 0) {
            sticks_result = 5;
        }else{
        sticks_result = stick1 + stick2 + stick3 + stick4;
        }
        return sticks_result;
    }

    public void playTurn() {
        System.out.println("\n>>> Turn: " + (turn.equals("W") ? "White" : "Black"));
        int dice = sticks();
        System.out.println("Sticks result: " + dice);

        List<Piece> penaltyList = new ArrayList<>();
        List<Piece> myPieces = turn.equals("W") ? whites : blacks;
        
        for (Piece p : myPieces) {
            if (p.position == 28 || p.position == 30 || p.position == 29) {
                penaltyList.add(p);
            }
        }

        Piece moved = null;

        if (vsAI && turn.equals("B")) {
            System.out.println("AI is thinking...");
            board.silentMode = true; 
            SenetState state = new SenetState(board, whites, blacks, turn);
            int id = SenetAI.findBestMove(state, dice);

            if (id != -1) {
                for (Piece p : blacks) {
                    if (p.id == id) {
                        moved = p;
                        break;
                    }
                }
                board.movePiece(moved, dice);
                board.silentMode = false; 
                System.out.println("AI moved piece P" + id);
            } else {
                board.silentMode = false;
                System.out.println("AI skipped turn - no moves");
            }
        } else {
            List<Piece> options = new ArrayList<>();
            for (Piece p : myPieces) {
                if (p.position <= 30 && board.isMoveValid(p, dice)) {
                    options.add(p);
                }
            }

            if (options.isEmpty()) {
                System.out.println("Hard luck! No valid moves this time.");
            } else {
                System.out.println("Available pieces: ");
                for (Piece p : options) {
                    System.out.print("P" + p.id + "[" + p.position + "] ");
                }
                System.out.println("\nEnter piece ID: ");

                int pick = -1;
                while (pick == -1) {
                    try {
                        String raw = sc.next();
                        pick = Integer.parseInt(raw);
                        for (Piece p : options) {
                            if (p.id == pick) {
                                moved = p;
                                break;
                            }
                        }

                        if (moved != null) {
                            if (!board.movePiece(moved, dice)) {
                                System.out.println("Can't do that move. Try again:");
                                pick = -1;
                                moved = null;
                            }
                        } else {
                            System.out.println("Wrong ID. Pick from the list above:");
                            pick = -1;
                        }
                    } catch (Exception e) {
                        System.out.println("Just type the number please:");
                        pick = -1;
                    }
                }
            }
        }

        for (Piece p : penaltyList) {
            if (p != moved) {
                board.forceReturnToRebirth(p);
            }
        }

        turn = turn.equals("W") ? "B" : "W";
        board.printBoard();
    }

    public boolean isGameOver() {
        boolean wWin = whites.stream().allMatch(p -> p.position > 30);
        boolean bWin = blacks.stream().allMatch(p -> p.position > 30);
        
        if (wWin) {
            System.out.println("!!! White Wins !!!");
            return true;
        }
        if (bWin) {
            System.out.println("!!! Black Wins !!!");
            return true;
        }
        return false;
    }
}
