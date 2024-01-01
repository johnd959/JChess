package board;
import pieces.Piece;
public class Spot {
    public final int[] location;
    public Piece piece;

    public Spot(int[] location){
        this.location = location;
    }
}
