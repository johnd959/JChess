package org.example;


import board.Board;
import characters.SpecialCharacters;
import game.Game;
import game.Player;
import pieces.*;

import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {

        Game game = new Game();
        game.StartGame();

//        try{
//            Class pieceClass = Class.forName("pieces." + "Queen");
//            Constructor pieceCon = pieceClass.getConstructor(int[].class, int.class);
//            Piece newPiece = (Piece) pieceCon.newInstance("King", new int[]{0,0}, 1);
//        }catch (Exception exception){
//            System.out.println(exception.getMessage());
//        }


    }

}