package pieces;

import java.util.ArrayList;
import board.Board;
import board.Spot;

public class Pawn extends Piece {

    private static int travelDistance = 2;

    public Pawn(int[] location, int player) {
        super('\u265F', "Pawn", new ArrayList<>(), location, player );
        if (player == 1){
            display = '\u265F';
        }
        else if (player == 2){
            display = '\u2659';
        }

    }

    @Override
    public void getNewMoves(Spot[][] spotMatrix){
        moves.clear();
        //creating a 3x1 box in front of the pawn piece and filtering out the invalid moves
        // x x x
        //   p
        final int xtl = location[0] - 1;
        final int y = location[1] + 1;

        for(int x = xtl; x <= location[0] + 1; x++){
            //if the x values are within the horizontal bounds of the board
            if(x >= 1 && x <= Board.width){
                    // if the y values are within the vertical bounds of the board
                if (y >= 1 && y <= Board.height) {
                        // if the diagonal spots in front of the pawn are occupied
                    if(spotMatrix[y][x].piece != null && location[0] != x && location[1] + 1 == y) {
                        Piece collision = spotMatrix[y][x].piece;
                        if (collision.getPlayer() != player)
                            moves.add(new int[]{x, y});
                    }
                    else if(location[0] == x){
                        if (spotMatrix[y][x].piece == null)
                        {
                            moves.add(new int[]{x,y});
                            if(travelDistance == 2)
                                moves.add(new int[]{x, y + 1});
                        }
                    }
                }
            }
        }
    }

    @Override
    public void move(int[] location) {
         this.location = location;
         this.moved++;
         travelDistance = 1;
    }


    public ArrayList<int[]> getMoves() {
        return moves;
    }

    public int[] getLocation() {
        return location;
    }
}
