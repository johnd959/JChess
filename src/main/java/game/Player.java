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
    public Piece getSelectedPiece() {
        return selectedPiece;
    }

    public void setSelectedPiece(Piece selectedPiece) {
        this.selectedPiece = selectedPiece;
    }

    public final int playerNum;
    private Piece selectedPiece;

    public boolean inCheck;

    public Player(int playerNum){
        this.playerNum = playerNum;
        this.inCheck = false;
    }

    public ArrayList<String> promptMove(){
        return makeCommand();
    }

    public ArrayList<String> makeCommand(){
        String message = "Player " + playerNum + " Please make a move" +
                "\nEnter M and two coordinates to make a move" +
                "\nEG. M A1 B2 (Move Piece at A1 to B2)";

        ArrayList<String> arguments = getCommand(message);
        while(!(validateArguments(arguments, arguments.size()))){
            arguments = getCommand(message);
        }
        return arguments;
    }
    //prompt the player for a selection in the form of coordinates
    public ArrayList<int[]> getSelection(ArrayList<String> arguments){
        ArrayList<int[]> selectedCoordinates = convertCoordinates(arguments);
        return selectedCoordinates;
    }
    public Move makeMove(){
        String message = "Enter M + origin + destination to make a move";
        ArrayList<String> arguments = getCommand(message);
        while(arguments.size() != 3){
            arguments = getCommand(message);
        }
        return new Move(arguments);
    }

    private ArrayList<String> getCommand(String message){
        System.out.println(message);
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
        Pattern pattern = Pattern.compile("^[A-Za-z][0-9]$$", Pattern.CASE_INSENSITIVE);
        switch (length){
            case 1:{
                if(!(arguments.get(0).equals("D"))) {
                    System.out.println("Invalid command");
                    return false;
                }
            }
            case 2:{
                if((!arguments.get(0).equals("S"))) {
                    System.out.println("Invalid command format" +
                            "\n Enter S + a coordinate to select a piece");
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
                if(!(arguments.get(0).equals("M"))) {
                    System.out.println("Invalid Command");
                    return false;
                }
                for(int i = 1; i < arguments.size(); i++){
                    Matcher matcher = pattern.matcher(arguments.get(i));
                    if(!(matcher.find())) {
                        System.out.println("Coordinate format is invalid: " + arguments.get(i));
                        return false;
                    }
                }
                break;
            }
        }
        return true;
    }

    private ArrayList<int[]> convertCoordinates(List<String> stringCoordinates){
        ArrayList<int[]> intCoordinates = new ArrayList<>();
        for(String coordinate : stringCoordinates){
            int x = (int) coordinate.substring(0).charAt(0) - SpecialCharacters.letterDifference;
            int y = Integer.parseInt(coordinate.substring(1));
            intCoordinates.add(new int[]{x, y});
        }
        return intCoordinates;
    }
}
