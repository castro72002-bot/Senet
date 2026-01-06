public class Piece {
    public int id;
    public String owner;
    public int position;

    public Piece(int id, String owner, int position) {
        this.id = id;
        this.owner = owner;
        this.position = position;
    }

    public Piece copy() {
        return new Piece(this.id, this.owner, this.position);
    }
}
