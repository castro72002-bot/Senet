public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        System.out.println("Senet");
        game.board.printBoard();

        while (!game.isGameOver()) {
            game.playTurn();
        }
    }
}
