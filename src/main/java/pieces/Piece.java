package pieces;

import board.Spot;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Piece {

    public final String name;
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

    public Piece(char display, String name, ArrayList<int[]> moves, int[] location, int player){
        this.name = name;
        this.display = display;
        this.moves = moves;
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
