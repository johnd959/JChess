package pieces;

import board.Spot;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Piece {

    public final String name;

    public static final String[] availableNames = new String[]{"King", "Queen", "Rook", "Bishop", "Knight","Pawn"};
    public char display;

    protected int[] location;
    protected ArrayList<int[]> moves;

    public int getPlayer() {
        return player;
    }

    protected int player;

    public abstract void getNewMoves(Spot[][] spotMatrix);

    public abstract void move(int[] location);

    protected int moved;

    public Piece(String name, int[] location, int player){
        this.name = name;
        this.moves = new ArrayList<>();
        this.location = location;
        this.player = player;
        this.moved = 0;
    }

    public ArrayList<int[]> getMoves() {
        return moves;
    }

    public int[] getLocation() {
        return location;
    }

    public int getMoved() {
        return moved;
    }
}
