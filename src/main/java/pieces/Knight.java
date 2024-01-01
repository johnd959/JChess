package pieces;

import board.Board;
import board.Spot;

import java.util.ArrayList;

public class Knight extends Piece{

    public Knight(int[] location, int player) {
        super('\u265E', "Knight", new ArrayList<>(), location, player);
        this.location = location;
        if (player == 1){
            display = '\u265E';
        }
        else if (player == 2){
            display = '\u2658';
        }
    }

    @Override
    public void getNewMoves(Spot[][] spotMatrix){
        if(!moves.isEmpty())
            moves.clear();
        for(int x = -2; x <= 2; x++){
            if(x == 0) continue;
            for(int y = -2; y <= 2; y++){
                if(y == 0) continue;
                if (Math.abs(x / y) == 1) continue;
                if(checkValidPosition(x, y)){
                    moves.add(new int[]{location[0] + x, location[1] + y});
                }
            }
        }

    }
    private boolean checkValidPosition(int x, int y){
        if (location[0] + x >= 1 && location[0] +  x <= Board.width){
            return location[1] + y >= 1 && location[1] + y <= Board.width;
        }
        return false;
    }

    @Override
    public void move(int[] location) {
        this.location = location;
        this.moved++;
    }


    public ArrayList<int[]> getMoves() {
        return moves;
    }

    public int[] getLocation() {
        return location;
    }
}
