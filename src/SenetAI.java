import java.util.List;

public class SenetAI {
    private static final int DEPTH = 3;

    public static int findBestMove(SenetState s, int sticks) {
        String turn = s.player;
        boolean isWhite = turn.equals("W");
        
        double bestVal = isWhite ? -1e9 : 1e9;
        int bestId = -1;

        List<SenetState> moves = s.nextStates(sticks);
        
        if (moves.isEmpty()) return -1;
        if (moves.size() == 1) return getMovedId(s, moves.get(0));

        for (SenetState m : moves) {
            double v = isWhite ? minPlayer(m, DEPTH - 1) : maxPlayer(m, DEPTH - 1);
            
            if (isWhite) {
                if (v > bestVal) {
                    bestVal = v;
                    bestId = getMovedId(s, m);
                }
            } else {
                if (v < bestVal) {
                    bestVal = v;
                    bestId = getMovedId(s, m);
                }
            }
        }
        return bestId;
    }

    public static double maxPlayer(SenetState s, int d) {
        if (d == 0 || s.isDone()) return s.evaluate();
        
        double total = 0;
        for (int r = 1; r <= 5; r++) {
            List<SenetState> options = s.nextStates(r);
            if (options.isEmpty()) {
                SenetState skip = s.copy();
                skip.player = "B";
                total += 0.2 * minPlayer(skip, d - 1);
            } else {
                double best = -1e9;
                for (SenetState st : options) {
                    best = Math.max(best, minPlayer(st, d - 1));
                }
                total += 0.2 * best;
            }
        }
        return total;
    }

    public static double minPlayer(SenetState s, int d) {
        if (d == 0 || s.isDone()) return s.evaluate();
        
        double total = 0;
        for (int r = 1; r <= 5; r++) {
            List<SenetState> options = s.nextStates(r);
            if (options.isEmpty()) {
                SenetState skip = s.copy();
                skip.player = "W";
                total += 0.2 * maxPlayer(skip, d - 1);
            } else {
                double best = 1e9;
                for (SenetState st : options) {
                    best = Math.min(best, maxPlayer(st, d - 1));
                }
                total += 0.2 * best;
            }
        }
        return total;
    }

    public static int getMovedId(SenetState oldS, SenetState newS) {
        List<Piece> p1 = oldS.player.equals("W") ? oldS.whiteList : oldS.blackList;
        List<Piece> p2 = oldS.player.equals("W") ? newS.whiteList : newS.blackList;

        for (int i = 0; i < p1.size(); i++) {
            if (p1.get(i).position != p2.get(i).position) {
                return p1.get(i).id;
            }
        }
        return -1;
    }
}
