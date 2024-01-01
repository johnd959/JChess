package pieces;

import board.Board;
import board.Spot;

import java.util.ArrayList;

public class Queen extends Piece {

    public Queen(int[] location, int player){
        super('\u265B', "Queen", new ArrayList<>(), location, player);
        if (player == 1){
            display = '\u265B';
        }
        else if (player == 2){
            display = '\u2655';
        }
    }
    @Override
    public void getNewMoves(Spot[][] spotMatrix) {
        moves.clear();

        final int x = location[0];
        final int y = location[1];

        ArrayList<int[]> collisions = new ArrayList<>();


        //getting top diagonal right of the queen
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
        //getting bottom diagonal right of the queen
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

        //getting top diagonal left of the queen
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

        //getting bottom diagonal left of the queen
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

        //validate if collisions are opponent pieces and add to available moves
        for(int[] collision : collisions){
            if(spotMatrix[collision[1]][collision[0]].piece.getPlayer() != player){
                moves.add(collision);
            }
        }

        //getting all horizontal moves
        for (int i = location[0] + 1; i <= Board.width; i++){
            Piece collision = spotMatrix[location[1]][i].piece;
            if(collision != null){
                if(collision.getPlayer() != player){
                    moves.add(new int[]{i, location[1]});
                }
                break;
            }
            moves.add(new int[]{i, location[1]});
        }
        for (int j = location[0] - 1; j >= 1; j--){
            Piece collision = spotMatrix[location[1]][j].piece;
            if(collision != null){
                if(collision.getPlayer() != player){
                    moves.add(new int[]{j, location[1]});
                }
                break;
            }
            moves.add(new int[]{j, location[1]});
        }
        //getting all vertical moves
        for (int k = location[1] + 1; k <= Board.height; k++){
            Piece collision = spotMatrix[k][location[0]].piece;
            if(collision != null){
                if(collision.getPlayer() != player){
                    moves.add(new int[]{location[0], k});
                }
                break;
            }
            moves.add(new int[]{location[0], k});
        }
        for (int l = location[1] - 1; l >= 1; l--){
            Piece collision = spotMatrix[l][location[0]].piece;
            if(collision != null){
                if(collision.getPlayer() != player){
                    moves.add(new int[]{location[0], l});
                }
                break;
            }
            moves.add(new int[]{location[0], l});
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
