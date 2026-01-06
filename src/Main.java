import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("--- Welcome to Senet ---");
        System.out.println("Select Game Mode:");
        System.out.println("1. Player vs Player (PvP)");
        System.out.println("2. Player vs Computer (PvE)");

        boolean vsComputer = false;
        String choice = scanner.nextLine();
        if (choice.equals("2")) {
            vsComputer = true;
            System.out.println("Mode: Player vs Computer selected.");
        } else {
            System.out.println("Mode: Player vs Player selected.");
        }

        Game game = new Game(vsComputer);
        game.board.printBoard();

        while (!game.isGameOver()) {
            game.playTurn();
        }
    }
}
