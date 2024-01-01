package pieces;

import board.Board;
import board.Spot;

import javax.swing.text.html.HTMLDocument;
import java.util.ArrayList;

public class Rook extends Piece{
    public Rook(int[] location, int player){
        super('\u265C', "Rook", new ArrayList<>(), location, player);
        if (player == 1){
            display = '\u265C';
        }
        else if (player == 2){
            display = '\u2656';
        }
    }
    @Override
    public void getNewMoves(Spot[][] spotMatrix) {
        moves.clear();
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
