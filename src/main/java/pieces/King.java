package pieces;

import board.Board;
import board.Spot;

import java.util.ArrayList;

public class King extends Piece {

    private static final int travelDistance = 1;

    public King(int[] location, int player) {
        super('\u265A', "King", new ArrayList<>(), location, player);
        this.location = location;
        if (player == 1){
            display = '\u265A';
        }
        else if (player == 2){
            display = '\u2654';
        }
    }

    @Override
    public void getNewMoves(Spot[][] spotMatrix){
        moves.clear();

        //creating a 3x3 box around the king piece and filtering out the invalid moves
        // x x x
        // x k x
        // x x x
        final int xtl = location[0] - 1;
        final int ytl = location[1] + 1;

        for(int x = xtl; x <= location[0] + 1; x++){
            for(int y = ytl; y >= location[1] - 1; y--){
                //if the x values are within the horizontal bounds of the board
                if(x >= 1 && x <= Board.width){
                    // if the y values are within the vertical bounds of the board
                    if (y >= 1 && y <= Board.height) {
                        // if the values are not the king itself and that of friendly pieces
                        if(spotMatrix[y][x].piece != null) {
                            Piece collision = spotMatrix[y][x].piece;
                            if (collision.getPlayer() != player && collision != this)
                                moves.add(new int[]{x, y});
                        }
                        else
                            moves.add((new int[]{x, y}));
                    }
                }
            }
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
