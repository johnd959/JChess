package board;


import characters.SpecialCharacters;
import game.Player;
import game.exceptions.EmptySpotException;
import game.exceptions.InvalidMoveException;
import pieces.*;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Board {
    public static final int height = 8;
    public static final int width = 8;

    protected ArrayList<Piece> player1Pieces;
    protected ArrayList<Piece> player2Pieces;
    protected ArrayList<Piece> disqualifiedPieces;

    public Spot[][] spotMatrix;

    public Board(){
        //2-dimensional arrays are [height/y][width/x]
        spotMatrix = new Spot[height + 2][width + 2];
        this.player1Pieces = new ArrayList<>();
        this.player2Pieces = new ArrayList<>();
        this.disqualifiedPieces = new ArrayList<>();
        initializeBoard();
    }

    public void movePiece(int[] pieceLocation, int[] newLocation) throws InvalidMoveException{

        if(Arrays.equals(pieceLocation, newLocation))
            throw new InvalidMoveException("The destination " + Arrays.toString(pieceLocation) + " cannot be the same as the origin " + Arrays.toString(newLocation));

        Piece destinationPiece = spotMatrix[newLocation[1]][newLocation[0]].piece;
        Piece pieceToMove = spotMatrix[pieceLocation[1]][pieceLocation[0]].piece;
        spotMatrix[pieceLocation[1]][pieceLocation[0]].piece = null;
        if(destinationPiece != null){
            disqualifiedPieces.add(destinationPiece);
            if(destinationPiece.getPlayer() == 1){
                player1Pieces.remove(destinationPiece);
            }
            else if(destinationPiece.getPlayer() == 2){
                player2Pieces.remove(destinationPiece);
            }
        }
        spotMatrix[newLocation[1]][newLocation[0]].piece = pieceToMove;
        pieceToMove.move(newLocation);
    }

    /**
     *
     * @param pieceLocation The location of the piece to be moved
     * @return An arraylist of all valid moves
     */
    public ArrayList<int[]> getMoves(int[] pieceLocation) throws InvalidMoveException{

        Piece selected = null;
        ArrayList<int[]> availableMoves = new ArrayList<>();
        try{
            selected = spotMatrix[pieceLocation[1]][pieceLocation[0]].piece;
            selected.getNewMoves(spotMatrix);

            //filtering out moves that collide with friendly pieces
            for(int[] move : selected.getMoves()){
                Piece collision = spotMatrix[move[1]][move[0]].piece;
                if(collision != null) {
                    if (collision.getPlayer() != selected.getPlayer())
                        availableMoves.add(move);
                }
                else
                    availableMoves.add(move);
            }

        }
        catch (NullPointerException exception){
            throw new InvalidMoveException("Spot is most likely empty");
        }
        return availableMoves;
    }
    /**
     *
     * @param pieceLocation The location of the piece to be moved
     * @return An arraylist of all moves including the original collisions
     */
    public ArrayList<int[]> getRawMoves(int[] pieceLocation){

        Piece selected = null;
        ArrayList<int[]> availableMoves = null;
        try{
            selected = spotMatrix[pieceLocation[1]][pieceLocation[0]].piece;
            selected.getNewMoves(spotMatrix);
            availableMoves = selected.getMoves();

        }
        catch (NullPointerException exception){
            System.out.println("Spot is empty");
        }
        return availableMoves;
    }

    public void renderBoard(){
        System.out.println("\n");
        for(int y = Board.height; y >= 0; y--){
            for(int x = 0; x <= Board.width; x++){
                if(x == 0 && y == 0){
                    System.out.print(" ");
                }
                else if(x == 0){
                    System.out.print(y);
                }
                else if(y == 0){
                    System.out.print((SpecialCharacters.letterSpace + ((char) (64 + x)) + SpecialCharacters.letterSpace));
                }
                else if (spotMatrix[y][x].piece != null) {
                    System.out.print(spotMatrix[y][x].piece.display);
                }
                else{
                    System.out.print(SpecialCharacters.emptySpot);
                }
                System.out.print(" | ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.print("Player 1: ");
        for(Piece piece: disqualifiedPieces){
            if(piece.getPlayer() == 2)
                System.out.print(piece.display + SpecialCharacters.letterSpace);
        }
        System.out.println();
        System.out.print("Player 2: ");
        for(Piece piece : disqualifiedPieces){
            if(piece.getPlayer() == 1)
                System.out.print(piece.display + SpecialCharacters.letterSpace);
        }
        System.out.println("\n");
    }

    private void addPiece(Piece piece){
        int[] pieceLocation = piece.getLocation();
        if(piece.getPlayer() == 1){
            player1Pieces.add(piece);
        }
        else if(piece.getPlayer() == 2){
            player2Pieces.add(piece);
        }
        spotMatrix[pieceLocation[1]][pieceLocation[0]].piece = piece;
    }

    /**
     *
     * @param location int[]{x,y}
     * @return A piece at the location
     * @throws EmptySpotException
     */
    public Piece getPieceAt(int[] location) throws EmptySpotException {
        Piece toBeReturned = spotMatrix[location[1]][location[0]].piece;

        if(toBeReturned == null){
            throw new EmptySpotException("There is no piece located at " + location[0] + "," + location[1]);
        }

        return toBeReturned;
    }

    private void initializeBoard(){
        //initializing board with spots
        for(int y = 1; y <= Board.height; y++){
            for(int x = 1; x <= Board.width; x++){
                spotMatrix[y][x] = new Spot(new int[]{x,y});
            }
        }

        //pieces for player 1
        addPiece(new Rook(new int[]{1,1}, 1));
        //addPiece(new Knight(new int[]{2,1}, 1));
        //addPiece(new Bishop(new int[]{3,1}, 1));
        //addPiece(new Queen(new int[]{4,1}, 1));
        addPiece(new King(new int[]{5,1}, 1));
        //addPiece(new Bishop(new int[]{6,1}, 1));
        //addPiece(new Knight(new int[]{7,1}, 1));
        addPiece(new Rook(new int[]{8,1}, 1));
        for(int x = 1; x <= Board.width; x++){
            addPiece(new Pawn(new int[]{x, 2}, 1));
        }

        //pieces for player one
        for(int x = 1; x <= Board.width; x++){
            addPiece(new Pawn(new int[]{x, 7}, 2));
        }
        addPiece(new Rook(new int[]{1,8}, 2));
        addPiece(new Knight(new int[]{2,8}, 2));
        addPiece(new Bishop(new int[]{3,8}, 2));
        addPiece(new Queen(new int[]{4,8}, 2));
        addPiece(new King(new int[]{5,8}, 2));
        addPiece(new Bishop(new int[]{6,8}, 2));
        addPiece(new Knight(new int[]{7,8}, 2));
        addPiece(new Rook(new int[]{8,8}, 2));
    }

    public ArrayList<Piece> getPlayer1Pieces() {
        return player1Pieces;
    }

    public ArrayList<Piece> getPlayer2Pieces() {
        return player2Pieces;
    }

    public ArrayList<Piece> getDisqualifiedPieces() {
        return disqualifiedPieces;
    }

    /**
     *
     * @param origin The location of the piece that is moving
     * @param destination The destination of the moving piece
     * @param player The player making the move under consideration
     * @return 0 if neither player are in check<br>1 if the opponent is in check<br>2 if player is in check<br>3 if both are in
     * check
     */
    public int isCheck(int[] origin, int[] destination, Player player){

        boolean playerCheck = false;
        boolean opponentCheck = false;

        //getting piece at destination for safekeeping if there is one
        Piece pieceAtDestination;
        try{
            pieceAtDestination = this.getPieceAt(destination);
        }catch (EmptySpotException exception){
            pieceAtDestination = null;
        }

        //moving piece to new location temporarily for checking
        try{
            this.movePiece(origin, destination);
        }catch (InvalidMoveException exception){
            // if the location is the same, return 0 because this would not affect check
            return 0;
        }


        // getting all possible moves for current player
        ArrayList<Piece> allPlayerPieces = null;
        ArrayList<Piece> allOpponentPieces = null;
        if(player.playerNum == 1){
            allPlayerPieces = this.getPlayer1Pieces();
            allOpponentPieces = this.getPlayer2Pieces();
        }else{
            allPlayerPieces = this.getPlayer2Pieces();
            allOpponentPieces = this.getPlayer1Pieces();
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
            piece.getNewMoves(spotMatrix);
            allPlayerMoves.add(piece.getMoves());
        }

        // getting all moves for the opponent pieces
        ArrayList<ArrayList<int[]>> allOpponentMoves = new ArrayList<>();
        for(Piece piece : allOpponentPieces){
            if(piece.name.equals("King")){
                opponentKing = piece;
            }
            piece.getNewMoves(spotMatrix);
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
        try{
            this.movePiece(destination, origin);
        }catch (InvalidMoveException exception){

        }

        if(pieceAtDestination != null) {
            spotMatrix[destination[1]][destination[0]].piece = pieceAtDestination;
            this.getDisqualifiedPieces().remove(pieceAtDestination);
            player.getPieces().add(pieceAtDestination);
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

    public boolean castle(int[] rookLocation, Player player) throws InvalidMoveException {

        // if player is in check
        if(player.inCheck){
            throw new InvalidMoveException("Castling is an illegal move while player is in check");
        }

        //getting the king
        Piece king = null;

        for(Piece piece : player.getPieces()){
            if(piece.name.equals("King")) {
                king = piece;
                break;
            }
        }

        //king is null
        if(king == null){
            throw new InvalidMoveException("Game is supposed to be over. Player " + player.playerNum + " has no king");
        }

        //getting the rook
        Piece rook = null;
        try{
            rook = getPieceAt(rookLocation);
        } catch (EmptySpotException exception){
            throw new InvalidMoveException("The location specified is empty and does not contain a rook");
        }

        //checking if the rook belongs to the player
        if(rook.getPlayer() != player.playerNum)
            throw new InvalidMoveException("The rook selected does not belong to player " + player.playerNum);

        // checking if the rook has been moved from its original configuration
        if(rook.getMoved() > 0)
            throw new InvalidMoveException("The rook selected has been moved and is no longer eligible for castling");

        // checking if the king has been moved from its original configuration
        if(king.getMoved() > 0)
            throw new InvalidMoveException("The king selected has been moved and is no longer eligible for castling");

        //checking if the rook's valid moves extend to the king,
        //which means the rook has a direct line of sight to the king
        int[] kingLocation = king.getLocation();
        rook.getNewMoves(spotMatrix);
        //getting only the horizontal moves
        List<int[]> horizontalMoves = rook.getMoves().stream()
                .filter(move -> move[1] == kingLocation[1])
                .collect(Collectors.toList());


        // please revise the section of this function from here onwards
        //there is most likely a more efficient way to check where the king should move
        boolean kingInSight = false;
        for(int[] move : horizontalMoves){
            //if the rook's non-colliding moves coincide with
            if(Arrays.equals(move, kingLocation)){
                kingInSight = true;
                break;
            }
        }


        if(kingInSight){
            for(int[] move : horizontalMoves){
                if(isCheck(kingLocation, move, player) != 0){
                    throw new InvalidMoveException("King will be in check at one point while castling");
                }
            }
            if(rookLocation[0] > kingLocation[0]){
                int[] newKingLocation = new int[]{kingLocation[0] + 2, kingLocation[1]};
                int[] newRookLocation = new int[]{newKingLocation[0] - 1, kingLocation[1]};
                movePiece(kingLocation, newKingLocation);
                movePiece(rookLocation, newRookLocation);
            }
            else{
                int[] newKingLocation = new int[]{kingLocation[0] - 2, kingLocation[1]};
                int[] newRookLocation = new int[]{newKingLocation[0] + 1, kingLocation[1]};
                movePiece(kingLocation, newKingLocation);
                movePiece(rookLocation, newRookLocation);
            }
            return true;
        }

        return false;

    }

    public void setDisqualifiedPieces(ArrayList<Piece> disqualifiedPieces) {
        this.disqualifiedPieces = disqualifiedPieces;
    }
}
