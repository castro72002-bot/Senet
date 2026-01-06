import java.util.ArrayList;
import java.util.List;

public class GameState {
    public Board board;
    public List<Piece> whitePieces;
    public List<Piece> blackPieces;
    public String currentTurn;

    public GameState(Board board, List<Piece> whitePieces, List<Piece> blackPieces, String currentTurn) {
        this.board = board;
        this.whitePieces = whitePieces;
        this.blackPieces = blackPieces;
        this.currentTurn = currentTurn;
    }

    // دالة لنسخ الحالة بالكامل (Deep Copy)
    public GameState copy() {
        Board newBoard = new Board();
        List<Piece> newWhite = new ArrayList<>();
        List<Piece> newBlack = new ArrayList<>();

        // نسخ القطع البيضاء
        for (Piece p : whitePieces) {
            Piece copyP = p.copy();
            newWhite.add(copyP);
            if (copyP.position >= 1 && copyP.position <= 30) {
                newBoard.placePiece(copyP, copyP.position);
            }
        }

        // نسخ القطع السوداء
        for (Piece p : blackPieces) {
            Piece copyP = p.copy();
            newBlack.add(copyP);
            if (copyP.position >= 1 && copyP.position <= 30) {
                newBoard.placePiece(copyP, copyP.position);
            }
        }

        return new GameState(newBoard, newWhite, newBlack, this.currentTurn);
    }

    // الحصول على جميع الحالات الممكنة التالية بناءً على قيمة نرد معينة
    public List<GameState> getNextStates(int diceValue) {
        List<GameState> nextStates = new ArrayList<>();
        List<Piece> currentPieces = currentTurn.equals("W") ? whitePieces : blackPieces;

        for (Piece p : currentPieces) {
            if (p.position <= 30) {
                GameState newState = this.copy();
                // العثور على نفس القطعة في الحالة الجديدة
                List<Piece> targetList = currentTurn.equals("W") ? newState.whitePieces : newState.blackPieces;
                Piece targetPiece = null;
                for (Piece tp : targetList) {
                    if (tp.id == p.id) {
                        targetPiece = tp;
                        break;
                    }
                }

                if (targetPiece != null) {
                    boolean moved = newState.board.movePiece(targetPiece, diceValue);
                    if (moved) {
                        // تبديل الدور في الحالة الجديدة
                        newState.currentTurn = newState.currentTurn.equals("W") ? "B" : "W";
                        nextStates.add(newState);
                    }
                }
            }
        }
        return nextStates;
    }

    // دالة التقييم (Heuristic): تعطي قيمة عالية إذا كانت الحالة في صالح اللاعب الأبيض
    public double evaluate() {
        double score = 0;

        // 1. التقدم نحو النهاية (كلما كانت القطعة أقرب للـ 31 كان أفضل)
        for (Piece p : whitePieces) {
            if (p.position > 30) score += 100; // قطعة خرجت
            else score += p.position;
        }

        for (Piece p : blackPieces) {
            if (p.position > 30) score -= 100; // قطعة خرجت للخصم
            else score -= p.position;
        }

        // 2. المربعات الخاصة
        // المربعات 28, 29, 30 قوية جداً لأنها قريبة من الخروج
        for (Piece p : whitePieces) {
            if (p.position >= 28 && p.position <= 30) score += 20;
        }
        for (Piece p : blackPieces) {
            if (p.position >= 28 && p.position <= 30) score -= 20;
        }

        return score;
    }

    public boolean isGameOver() {
        boolean allWhiteOut = whitePieces.stream().allMatch(p -> p.position > 30);
        boolean allBlackOut = blackPieces.stream().allMatch(p -> p.position > 30);
        return allWhiteOut || allBlackOut;
    }
}
