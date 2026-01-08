import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println("--- Welcome to Senet ---");
        System.out.println("Select Mode:");
        System.out.println("1. PvP (2 players)");
        System.out.println("2. PvE (vs Computer)");

        boolean vsAI = false;
        String choice = s.nextLine();
        if (choice.equals("2")) {
            vsAI = true;
            System.out.println("Playing against AI.");
        } else {
            System.out.println("Local PvP started.");
        }

        Game game = new Game(vsAI);
        game.board.printBoard();

        while (!game.isGameOver()) {
            game.playTurn();
        }
        
        System.out.println("Thanks for playing!");
    }
}
