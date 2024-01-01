package game;

import board.Board;
import pieces.King;
import pieces.Piece;

import java.util.ArrayList;
import java.util.UUID;

public class Game {
    public String id;
    private Player player1;
    private Player player2;

    private Board board;

    public Game(){
        this.id = UUID.randomUUID().toString();
        InitializeGame();
    }


    public void StartGame(){
        System.out.println("Welcome to chess with myself (for now) \uD83D\uDE05");
        boolean flipFlop = true;
        while(isOngoing()){
            if(flipFlop){
                new Turn(player1, player2, board).start();
                flipFlop = false;
            }
            else {
                new Turn(player2, player1, board).start();
                flipFlop = true;
            }
        }
    }
    private void InitializeGame() {
        this.player1 = new Player(1);
        this.player2 = new Player(2);
        this.board = new Board();
    }

    private boolean isOngoing() {
        boolean king1 = false;
        boolean king2 = false;

        for(Piece piece : board.getPlayer1Pieces()){
            if(piece.getClass().equals(King.class))
                king1 = true;
        }

        for(Piece piece: board.getPlayer2Pieces()){
            if(piece.getClass().equals(King.class))
                king2 = true;
        }
        return king1 && king2;
    }
}
