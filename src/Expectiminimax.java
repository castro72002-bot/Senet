import java.util.List;

public class Expectiminimax {
    private static final int MAX_DEPTH = 3; 

    public static int getBestMove(GameState state, int diceValue) {
        String aiPlayer = state.currentTurn;
        boolean isAiWhite = aiPlayer.equals("W");
        
        double bestVal = isAiWhite ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
        int bestPieceId = -1;

        List<GameState> nextStates = state.getNextStates(diceValue);
        
        if (nextStates.isEmpty()) return -1;
        if (nextStates.size() == 1) {
            return getPieceIdFromTransition(state, nextStates.get(0));
        }

        for (GameState next : nextStates) {
            // After AI moves, it's the other player's turn (MIN if AI is MAX, and vice-versa)
            // But expectiminimax handles the turn based on GameState.currentTurn
            double val = expectiminimax(next, MAX_DEPTH - 1);
            
            if (isAiWhite) {
                if (val > bestVal) {
                    bestVal = val;
                    bestPieceId = getPieceIdFromTransition(state, next);
                }
            } else {
                if (val < bestVal) {
                    bestVal = val;
                    bestPieceId = getPieceIdFromTransition(state, next);
                }
            }
        }
        return bestPieceId;
    }

    private static double expectiminimax(GameState state, int depth) {
        if (depth == 0 || state.isGameOver()) {
            return state.evaluate();
        }

        boolean isWhiteTurn = state.currentTurn.equals("W");
        double res = 0;

        // Chance Node: Dice roll 1-5
        for (int d = 1; d <= 5; d++) {
            List<GameState> nextStates = state.getNextStates(d);
            
            if (nextStates.isEmpty()) {
                // Skip turn logic
                GameState skipState = state.copy();
                skipState.currentTurn = isWhiteTurn ? "B" : "W";
                res += 0.2 * expectiminimax(skipState, depth - 1);
            } else {
                // If it's White's turn, they want to MAXIMIZE evaluate()
                // If it's Black's turn, they want to MINIMIZE evaluate()
                double extremeVal = isWhiteTurn ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
                
                for (GameState next : nextStates) {
                    double val = expectiminimax(next, depth - 1);
                    if (isWhiteTurn) {
                        extremeVal = Math.max(extremeVal, val);
                    } else {
                        extremeVal = Math.min(extremeVal, val);
                    }
                }
                res += 0.2 * extremeVal;
            }
        }
        return res;
    }

    private static int getPieceIdFromTransition(GameState before, GameState after) {
        List<Piece> oldPieces = before.currentTurn.equals("W") ? before.whitePieces : before.blackPieces;
        List<Piece> newPieces = before.currentTurn.equals("W") ? after.whitePieces : after.blackPieces;

        for (int i = 0; i < oldPieces.size(); i++) {
            if (oldPieces.get(i).position != newPieces.get(i).position) {
                return oldPieces.get(i).id;
            }
        }
        return -1;
    }
}
