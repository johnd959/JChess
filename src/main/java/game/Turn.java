package game;

import board.Board;
import characters.SpecialCharacters;

import java.util.ArrayList;
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

            if(check){
                System.out.println("Player " + player.playerNum + " ,you are in check");
                playerArgs = player.promptMove();
            }
            else{
                boolean end = false;
                while(!end){
                    playerArgs = player.makeCommand();
                    boolean success = processCommand(playerArgs, player);
                    if(success) {
                        end = true;
                        endTurn = true;
                    }
                }
            }

        }
    }

//process the command that the player makes
    public boolean processCommand(ArrayList<String> args, Player player){
        switch (args.get(0)){
            case "M":{
                ArrayList<String> stringCoords = new ArrayList<>();
                stringCoords.add(args.get(1));
                stringCoords.add(args.get(2));
                ArrayList<int[]> moveCoordinates = convertCoordinates(stringCoords);
                boolean valid = validateMove(moveCoordinates);
                if(valid)
                    board.movePiece(moveCoordinates.get(0), moveCoordinates.get(1));

                break;
            }
        }
        return true;
    }

    public boolean validateMove(ArrayList<int[]> coordinates){
        int[] origin = coordinates.get(0);
        int[] destination = coordinates.get(1);
        ArrayList<int[]> validMoves = board.getMoves(origin);
        for(int[] validMove : validMoves){
            if(validMove[0] == destination[0] && validMove[1] == destination[1])
                return true;
        }
        return false;
    }

    private ArrayList<int[]> convertCoordinates(ArrayList<String> stringCoordinates){
        ArrayList<int[]> intCoordinates = new ArrayList<>();
        for(String coordinate : stringCoordinates){
            int x = (int) coordinate.substring(0).charAt(1) - SpecialCharacters.letterDifference;
            int y = Integer.parseInt(coordinate.substring(2));
            intCoordinates.add(new int[]{x, y});
        }
        return intCoordinates;
    }
}
