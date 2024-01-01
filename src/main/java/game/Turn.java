package game;

import board.Board;
import board.Spot;
import characters.SpecialCharacters;
import game.exceptions.EmptySpotException;
import game.exceptions.InvalidMoveException;
import pieces.Piece;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Turn {
    Player player;
    Player opponent;

    Board board;

    public Turn(Player player, Player opponent, Board board){
        this.player = player;
        this.opponent = opponent;
        this.board = board;
    }

    public void start(){
        boolean endTurn = false;
        Move move;
        ArrayList<String> playerArgs = null;
        board.renderBoard();
        while(!endTurn){

            boolean check = player.inCheck;

            // redundant?
            if(check){
                System.out.println("Player " + player.playerNum + ", you are in check");
                boolean end = false;
                while(!end) {
                    playerArgs = player.makeCommand();
                    try {
                        processCommand(playerArgs, player);
                    } catch (InvalidMoveException exception) {
                        board.renderBoard();
                        System.out.println(exception.getMessage());
                        continue;
                    }
                    end = true;
                    endTurn = true;
                }
            }
            else{
                boolean end = false;
                while(!end){
                    playerArgs = player.makeCommand();
                    try{
                         end = processCommand(playerArgs, player);
                    }
                    catch (InvalidMoveException exception){
                        board.renderBoard();
                        System.out.println(exception.getMessage());
                        continue;
                    }
                    endTurn = true;
                }
            }
        }
    }

//process the command that the player makes
    public boolean processCommand(ArrayList<String> args, Player player) throws InvalidMoveException{

        // extracting coordinates from player command arguments
        //converting the provided arguments to int coordinates
        ArrayList<String> stringCoords = new ArrayList<>();
        for(int i = 1; i < args.size(); i++){
            stringCoords.add((args.get(i)));
        }
        ArrayList<int[]> moveCoordinates = convertCoordinates(stringCoords);

        switch (args.get(0)){
            case "M":{
                //validating the move to be performed
                boolean valid = validateMove(moveCoordinates);
                if(valid)
                    board.movePiece(moveCoordinates.get(0), moveCoordinates.get(1));
                else
                    throw new InvalidMoveException("Please check your move, it is illegal or invalid");
                break;
            }
            case "C":{
                    boolean success = board.castle(moveCoordinates.get(0), player);
                    if(!success){
                        throw new InvalidMoveException("Castling now is illegal, please check your move");
                    }
            }
        }
        return true;
    }

    private boolean validateMove(ArrayList<int[]> coordinates) throws InvalidMoveException{

        //making flag
        boolean valid = false;
        //getting the piece selected
        int[] origin = coordinates.get(0);
        int[] destination = coordinates.get(1);
        Piece selected = null;

        try{
            selected = board.getPieceAt(new int[]{origin[0], origin[1]});
        }catch (EmptySpotException exception){
            System.out.println(exception.getMessage());
            throw new InvalidMoveException(exception.getMessage());
        }

        //checking if the selected piece is that of the manipulator
        if(player.playerNum != selected.getPlayer()){
            throw new InvalidMoveException("This piece does not belong to player " + player.playerNum);
        }

        //getting moves for the piece selected
        ArrayList<int[]> validMoves = board.getMoves(selected.getLocation());

        // returning false immediately if there are no available moves
        if(validMoves.isEmpty()){
            valid = false;
        }

        //validating the moves provided against the actual moves that can be performed by the piece selected
        for(int[] validMove : validMoves){
            if(Arrays.equals(validMove, destination))
                valid = true;
        }

        switch (board.isCheck(origin, destination, player)){
            case 0:{
                player.inCheck = false;
                break;
            }
            case 1:{
                opponent.inCheck = true;
                break;
            }
            case 2:{
                String message;
                if(player.inCheck == false)
                     message = "Making this move would result in your being in check";
                else
                    message = "Please make a move that would prevent you from being in check";
                throw new InvalidMoveException(message);
            }
        }


        return valid;
    }

    private ArrayList<int[]> convertCoordinates(ArrayList<String> stringCoordinates){
        ArrayList<int[]> intCoordinates = new ArrayList<>();
        for(String coordinate : stringCoordinates){
            int x = (int) coordinate.substring(0).charAt(0) - SpecialCharacters.letterDifference;
            int y = Integer.parseInt(coordinate.substring(1));
            intCoordinates.add(new int[]{x, y});
        }
        return intCoordinates;
    }
}
