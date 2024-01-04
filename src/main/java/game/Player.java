package game;

import characters.SpecialCharacters;
import pieces.Piece;

import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Player {
    public final int playerNum;

    public boolean inCheck;

    private ArrayList<Piece> pieces;

    public ArrayList<Piece> checkingPieces;

    public Player(int playerNum, ArrayList<Piece> pieces){
        this.playerNum = playerNum;
        this.inCheck = false;
        this.pieces = pieces;
        this.checkingPieces = null;
    }

    public ArrayList<String> makeCommand(){
        String message = "Player " + playerNum + " Please make a move" +
                "\nEnter M and two coordinates to make a move — Enter C and the coordinate of a rook to castle — Enter C to exchange a pawn" +
                "\nEG. M A1 B2 (Move Piece at A1 to B2) — C A1 (Castle with rook at A1) — E A8 Queen (Exchange pawn at A8 with a Queen";

        ArrayList<String> arguments = getCommand(message);
        while(!(validateArguments(arguments, arguments.size()))){
            arguments = getCommand(message);
        }
        return arguments;
    }

    private ArrayList<String> getCommand(String message){
        System.out.println(message);
        System.out.print("Move: ");
        Scanner scanner = new Scanner(System.in);
        char[] input = scanner.nextLine().toCharArray();
        ArrayList<String> argList = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < input.length; i++)
        {
            if(Character.isSpaceChar(input[i])){
                argList.add(builder.toString().strip());
                builder.setLength(0);
            }

            if(Character.isAlphabetic(input[i]))
                builder.append(Character.toUpperCase(input[i]));
            else
                builder.append(input[i]);
        }
        argList.add(builder.toString().strip());
        return argList;
    }

    private boolean validateArguments(ArrayList<String> arguments, int length){
        Pattern pattern = Pattern.compile("^[ABCDEFGH][0-9]$", Pattern.CASE_INSENSITIVE);
        switch (length){
            case 1:{
                if(!(arguments.get(0).equals("F"))) {
                    System.out.println("Invalid command");
                    return false;
                }
                break;
            }
            case 2:{
                if(!(arguments.get(0).equals("C"))) {
                    System.out.println("Invalid command format" +
                            "\n Enter C + a rook's coordinate");
                    return false;
                }
                Matcher matcher = pattern.matcher(arguments.get(1));
                if(!(matcher.find())){
                    System.out.println("Coordinate format is invalid: " + arguments.get(1));
                    return false;
                }
                break;
            }
            case 3:{
                if(arguments.get(0).equals("M")) {
                    for(int i = 1; i < arguments.size(); i++){
                        Matcher matcher = pattern.matcher(arguments.get(i));
                        if(!(matcher.find())) {
                            System.out.println("Coordinate format is invalid: " + arguments.get(i));
                            return false;
                        }
                    }
                }
                else if(arguments.get(0).equals("E")){
                    Matcher matcher = pattern.matcher(arguments.get(1));
                    if(!(matcher.find())){
                        System.out.println("Coordinate format is invalid: " + arguments.get(1));
                        return false;
                    }

                    //checking if piece type is valid
                    if(arguments.get(2).equalsIgnoreCase("king")) {
                        System.out.println("A pawn cannot be exchanged for a king");
                        return false;
                    }
                }
                break;
            }
            default:{
                return false;
            }
        }
        return true;
    }
    public ArrayList<Piece> getPieces() {
        return pieces;
    }

}
