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

            if(check){
                System.out.println("Player " + player.playerNum + ", you are in check");
                boolean end = false;
                while(!end) {
                    playerArgs = player.makeCommand();
                    try {
                        processCommand(playerArgs, player);
                    } catch (InvalidMoveException exception) {
                        System.out.println(exception.getMessage());
                        board.renderBoard();
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
                         processCommand(playerArgs, player);
                    }
                    catch (InvalidMoveException exception){
                        System.out.println(exception.getMessage());
                        board.renderBoard();
                        continue;
                    }
                    end = true;
                    endTurn = true;
                }
            }
        }
    }

//process the command that the player makes
    public boolean processCommand(ArrayList<String> args, Player player) throws InvalidMoveException{
        switch (args.get(0)){
            case "M":{
                //converting the provided arguments to int coordinates
                ArrayList<String> stringCoords = new ArrayList<>();
                stringCoords.add(args.get(1));
                stringCoords.add(args.get(2));
                ArrayList<int[]> moveCoordinates = convertCoordinates(stringCoords);

                //validating the move to be performed
                boolean valid = validateMove(moveCoordinates);
                if(valid)
                    board.movePiece(moveCoordinates.get(0), moveCoordinates.get(1));
                else
                    throw new InvalidMoveException("Please check your move, it is illegal or invalid");
                break;
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

        switch (isCheck(origin, destination)){
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

    /**
     *
     * @param origin
     * @param destination
     * @return 0 if neither player are in check<br>1 if the opponent is in check<br>2 if player is in check<br>3 if both are in
     * check
     */
    private int isCheck(int[] origin, int[] destination){

        boolean playerCheck = false;
        boolean opponentCheck = false;

        //getting piece at destination for safekeeping if there is one
        Piece pieceAtDestination;
        try{
            pieceAtDestination = board.getPieceAt(destination);
        }catch (EmptySpotException exception){
            pieceAtDestination = null;
        }

        //moving piece to new location temporarily for checking
        board.movePiece(origin, destination);

        // getting all possible moves for current player
        ArrayList<Piece> allPlayerPieces = null;
        ArrayList<Piece> allOpponentPieces = null;
        if(player.playerNum == 1){
            allPlayerPieces = board.getPlayer1Pieces();
            allOpponentPieces = board.getPlayer2Pieces();
        }else{
            allPlayerPieces = board.getPlayer2Pieces();
            allOpponentPieces = board.getPlayer1Pieces();
        }

        //player's king
        Piece playerKing = null;

        //opponent's king
        Piece opponentKing = null;

        //getting all moves for the player pieces
        ArrayList<ArrayList<int[]>> allPlayerMoves = new ArrayList<>();
        for(Piece piece : allPlayerPieces){
            if(piece.name.equals("King")){
                playerKing = piece;
            }
            piece.getNewMoves(board.spotMatrix);
            allPlayerMoves.add(piece.getMoves());
        }

        // getting all moves for the opponent pieces
        ArrayList<ArrayList<int[]>> allOpponentMoves = new ArrayList<>();
        for(Piece piece : allOpponentPieces){
            if(piece.name.equals("King")){
                opponentKing = piece;
            }
            piece.getNewMoves(board.spotMatrix);
            allOpponentMoves.add(piece.getMoves());
        }

        // checking if any of player moves coincide with the opponent's kings' position
        int[] opponentKingLocation = opponentKing.getLocation();
        for(ArrayList<int[]> pieceMoves : allPlayerMoves){
            for(int[] move : pieceMoves){
                if(Arrays.equals(move, opponentKingLocation)){
                    opponentCheck = true;
                    break;
                }
            }
        }

        //checking if any of opponent moves coincide with player's king's position
        int[] playerKingPosition = playerKing.getLocation();
        for(ArrayList<int[]> pieceMoves : allOpponentMoves){
            for(int[] move : pieceMoves){
                if(Arrays.equals(move, playerKingPosition)){
                    playerCheck = true;
                    break;
                }
            }
        }

        //returning pieces to their original locations and removing pieceAtDestination from disqualified pieces if any
        board.movePiece(destination, origin);
        if(pieceAtDestination != null) {
            board.spotMatrix[destination[1]][destination[0]].piece = pieceAtDestination;
            board.getDisqualifiedPieces().remove(pieceAtDestination);
        }



        // returning coded ints
        if(playerCheck && opponentCheck)
            return 3;
        else if(playerCheck)
            return 2;
        else if(opponentCheck)
            return 1;
        else
            return 0;
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
