import java.util.ArrayList;
import java.util.List;

public class SenetState {
    public Board b;
    public List<Piece> whiteList;
    public List<Piece> blackList;
    public String player;

    public SenetState(Board board, List<Piece> w, List<Piece> bl, String p) {
        this.b = board;
        this.whiteList = w;
        this.blackList = bl;
        this.player = p;
    }

    public SenetState copy() {
        Board newB = new Board();
        newB.silentMode = this.b.silentMode;
        List<Piece> w = new ArrayList<>();
        List<Piece> bl = new ArrayList<>();

        for (Piece p : whiteList) {
            Piece cp = p.copy();
            w.add(cp);
            if (cp.position >= 1 && cp.position <= 30) newB.placePiece(cp, cp.position);
        }

        for (Piece p : blackList) {
            Piece cp = p.copy();
            bl.add(cp);
            if (cp.position >= 1 && cp.position <= 30) newB.placePiece(cp, cp.position);
        }

        return new SenetState(newB, w, bl, this.player);
    }

    public List<SenetState> nextStates(int val) {
        List<SenetState> list = new ArrayList<>();
        List<Piece> current = player.equals("W") ? whiteList : blackList;

        for (Piece p : current) {
            if (p.position <= 30 && b.isMoveValid(p, val)) {
                SenetState s = this.copy();
                List<Piece> target = s.player.equals("W") ? s.whiteList : s.blackList;
                
                Piece tp = null;
                for (Piece temp : target) {
                    if (temp.id == p.id) {
                        tp = temp;
                        break;
                    }
                }

                if (tp != null) {
                    s.b.movePiece(tp, val);
                    s.player = s.player.equals("W") ? "B" : "W";
                    list.add(s);
                }
            }
        }
        return list;
    }

    public double evaluate() {
        double score = 0;

        for (Piece p : whiteList) {
            if (p.position > 30) score += 150;
            else score += p.position; 
        }

        for (Piece p : blackList) {
            if (p.position > 30) score -= 150;
            else score -= p.position;
        }

        score += evalSquares(whiteList, 1);
        score += evalSquares(blackList, -1);

        score += evalDefense(whiteList, 1);
        score += evalDefense(blackList, -1);

        return score;
    }

    public double evalSquares(List<Piece> pieces, int m) {
        double s = 0;
        for (Piece p : pieces) {
            if (p.position == 26) s += 30;
            if (p.position >= 28 && p.position <= 30) s += 40;
            if (p.position == 27) s -= 50;
        }
        return s * m;
    }

    public double evalDefense(List<Piece> pieces, int m) {
        double d = 0;
        for (int i = 0; i < pieces.size(); i++) {
            for (int j = i + 1; j < pieces.size(); j++) {
                if (Math.abs(pieces.get(i).position - pieces.get(j).position) == 1) {
                    d += 10;
                }
            }
        }
        return d * m;
    }

    public boolean isDone() {
        boolean wOut = whiteList.stream().allMatch(p -> p.position > 30);
        boolean bOut = blackList.stream().allMatch(p -> p.position > 30);
        return wOut || bOut;
    }
}
