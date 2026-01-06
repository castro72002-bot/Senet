import java.util.List;

public class Expectiminimax {
    private static final int MAX_DEPTH = 4; // Depth set to 4 as requested

    public static int getBestMove(GameState state, int diceValue) {
        double bestVal = Double.NEGATIVE_INFINITY;
        int bestPieceId = -1;

        List<Piece> currentPieces = state.currentTurn.equals("W") ? state.whitePieces : state.blackPieces;

        for (Piece p : currentPieces) {
            if (p.position <= 30) {
                GameState next = state.copy();
                List<Piece> targetList = next.currentTurn.equals("W") ? next.whitePieces : next.blackPieces;
                Piece targetPiece = null;
                for (Piece tp : targetList) {
                    if (tp.id == p.id) {
                        targetPiece = tp;
                        break;
                    }
                }

                if (targetPiece != null && next.board.movePiece(targetPiece, diceValue)) {
                    next.currentTurn = next.currentTurn.equals("W") ? "B" : "W";
                    double val = expectiminimax(next, MAX_DEPTH - 1, false);
                    if (val > bestVal) {
                        bestVal = val;
                        bestPieceId = p.id;
                    }
                }
            }
        }
        return bestPieceId;
    }

    private static double expectiminimax(GameState state, int depth, boolean isMax) {
        if (depth == 0 || state.isGameOver()) {
            return state.evaluate();
        }

        if (isMax) {
            // عقدة Chance (رمي النرد)
            double res = 0;
            for (int d = 1; d <= 5; d++) {
                double prob = 0.2; // احتمال متساوي لكل رقم 1/5
                res += prob * minimax(state, depth, true);
            }
            return res;
        } else {
            // دور الخصم (MIN)
            double res = 0;
            for (int d = 1; d <= 5; d++) {
                double prob = 0.2;
                res += prob * minimax(state, depth, false);
            }
            return res;
        }
    }

    private static double minimax(GameState state, int depth, boolean isMax) {
        if (depth == 0 || state.isGameOver()) {
            return state.evaluate();
        }

        if (isMax) {
            double maxEval = Double.NEGATIVE_INFINITY;
            List<GameState> nextStates = state.getNextStates(1); // تبسيط: نأخذ الحالات الممكنة
            // في الواقع، Expectiminimax يحتاج لتجربة كل الاحتمالات، سنقوم بتبسيطها هنا
            // لتجنب التعقيد الزائد في الحسابات
            for (int d = 1; d <= 5; d++) {
                for (GameState next : state.getNextStates(d)) {
                    double eval = expectiminimax(next, depth - 1, false);
                    maxEval = Math.max(maxEval, eval);
                }
            }
            return maxEval == Double.NEGATIVE_INFINITY ? state.evaluate() : maxEval;
        } else {
            double minEval = Double.POSITIVE_INFINITY;
            for (int d = 1; d <= 5; d++) {
                for (GameState next : state.getNextStates(d)) {
                    double eval = expectiminimax(next, depth - 1, true);
                    minEval = Math.min(minEval, eval);
                }
            }
            return minEval == Double.POSITIVE_INFINITY ? state.evaluate() : minEval;
        }
    }
}
