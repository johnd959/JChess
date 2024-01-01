package pieces;

import board.Board;
import board.Spot;

import java.util.ArrayList;

public class Bishop extends Piece{

    public Bishop(int[] location, int player){
        super('\u26D7',"Bishop", new ArrayList<>(), location, player);
        this.moves = new ArrayList<>();
        this.location = location;
        if (player == 1){
            display = '\u265D';
        }
        else if (player == 2){
            display = '\u2657';
        }
    }
    @Override
    public void getNewMoves(Spot[][] spotMatrix) {
        moves.clear();

        final int x = location[0];
        final int y = location[1];

        ArrayList<int[]> collisions = new ArrayList<>();


        //getting top diagonal right of the bishop
        int xr = x + 1;
        int ytr = y + 1;
        while (xr <= Board.width) {
            if (ytr <= Board.height) {
                //if the move will coincide with the position of another piece
                //add to collisions and end search
                if (spotMatrix[ytr][xr].piece != null) {
                    collisions.add(new int[]{xr, ytr});
                    break;
                }
                //else keep adding and searching for new moves
                else
                    moves.add(new int[]{xr, ytr});
            }
            xr++;
            ytr++;
        }
        //getting bottom diagonal right of the bishop
        xr = x + 1;
        int ybr = y - 1;
        while (xr <= Board.width) {
            if (ybr >= 1) {
                if (spotMatrix[ybr][xr].piece != null) {
                    collisions.add(new int[]{xr, ybr});
                    break;
                } else
                    moves.add(new int[]{xr, ybr});
            }
            xr++;
            ybr--;
        }

        //getting top diagonal left of the bishop
        int xl = x - 1;
        int ytl = y + 1;

        while(xl >= 1) {
            if (ytl <= Board.height) {
                if (spotMatrix[ytl][xl].piece != null) {
                    collisions.add(new int[]{xl, ytl});
                    break;
                } else
                    moves.add(new int[]{xl, ytl});
            }
            xl--;
            ytl++;
        }
        xl = x - 1;
        int ybl = y - 1;
        while(xl >= 1){
            if(ybl >= 1){
                if(spotMatrix[ybl][xl].piece != null){
                    collisions.add(new int[]{xl, ybl});
                    break;
                }
                else
                    moves.add(new int[]{xl, ybl});
            }
            xl--;
            ybl--;
        }

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
