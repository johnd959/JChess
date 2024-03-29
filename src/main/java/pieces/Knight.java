package pieces;

import board.Board;
import board.Spot;

import java.util.ArrayList;

public class Knight extends Piece{

    public Knight(int[] location, int player) {
        super("Knight", location, player);
        this.location = location;
        if (player == 1){
            //display = '\u265E';
            display = 'N';
        }
        else if (player == 2){
            //display = '\u2658';
            display = 'n';
        }
    }

    @Override
    public void getNewMoves(Spot[][] spotMatrix){

        //getting 2x2 boxes around the knight piece and filtering the moves where the x's and y's do equal (the diagonals)
        // 1 xx xx           --->            x|x
        // 2 xx xx                          x-|-x
        // 3   k                              k
        // 4 xx xx                          x-|-x
        // 5 xx xx                           x|x
        //   12 3 45

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
