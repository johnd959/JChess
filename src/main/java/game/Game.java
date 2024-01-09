package game;

import board.Board;
import game.exceptions.CheckmateException;
import pieces.King;
import pieces.Piece;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
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
        System.out.println("Welcome to chess with myself (for now) ♞");
        boolean flipFlop = true;
        while(true){
            try{
                if(flipFlop){
                    new Turn(player1, player2, board).start();
                    flipFlop = false;
                }
                else {
                    new Turn(player2, player1, board).start();
                    flipFlop = true;
                }
            }catch (CheckmateException exception){
                System.out.println(exception.getMessage() + "\n Thank you for playing!");
                board.renderBoard();
                break;
            }
        }
    }
    private void InitializeGame() {
        this.board = new Board();
        this.player1 = new Player(1, board.getPlayer1Pieces());
        this.player2 = new Player(2, board.getPlayer2Pieces());

    }

}
