package org.example;


import board.Board;
import characters.SpecialCharacters;
import game.Game;
import game.Player;
import pieces.*;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {


//        Board board = new Board();
//        board.renderBoard();
//
//        board.movePiece(new int[]{4,8}, new int[]{5,4});
//
//        board.renderBoard();
//
//        //feed x - y pairs
//        ArrayList<int[]> moves = board.getMoves(new int[]{5,4});
//
//        for(int[] move : moves){
//            System.out.println(Arrays.toString(move));
//        }

        //Player player = new Player(1);
        //player.promptMove();

        Game game = new Game();
        game.StartGame();

    }

}