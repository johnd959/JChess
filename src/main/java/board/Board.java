package board;


import characters.SpecialCharacters;
import game.Player;
import game.exceptions.EmptySpotException;
import game.exceptions.InvalidMoveException;
import pieces.*;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.util.*;
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

        if((pieceLocation[1] > Board.height || pieceLocation[1] < 1) || (pieceLocation[0] > Board.width || pieceLocation[0] < 1)){
            throw new InvalidMoveException("The spot of the piece to be moved specified is invalid");
        }

        if((newLocation[1] > Board.height || newLocation[1] < 1) || (newLocation[0] > Board.width || newLocation[0] < 1)){
            throw new InvalidMoveException("The destination specified is invalid");
        }


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

    public boolean replacePiece(Piece piece, int[] newLocation) throws InvalidMoveException{

        // if the coordinates of the newLocation provided are off the board
        if(newLocation[0] > Board.width || newLocation[0] < 1)
            return false;

        if(newLocation[1] > Board.height || newLocation[1] < 1)
            return false;

        //getting the piece at the destination
        Piece pieceAtDestination = null;

        try{
            pieceAtDestination = getPieceAt(newLocation);
        }catch (EmptySpotException exception){}

        if(pieceAtDestination != null){

            //removing the piece to be replaced from the board and updating the trackers
            spotMatrix[newLocation[1]][newLocation[0]].piece = null;
            disqualifiedPieces.add(pieceAtDestination);
            if(pieceAtDestination.getPlayer() == 1)
                player1Pieces.remove(pieceAtDestination);
            else
                player2Pieces.remove(pieceAtDestination);

            //moving the new piece into place
            spotMatrix[newLocation[1]][newLocation[0]].piece = piece;
            piece.move(newLocation);
        }

        return true;
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
            ArrayList<int[]> allAvailableMoves = selected.getMoves();

            //filtering out moves that collide with friendly pieces
            for(int[] move : allAvailableMoves){
                Piece collision = null;
                if((move[1] > Board.height || move[1] < 1) || (move[0] > Board.width || move[0] < 1))
                    continue;
                else
                    collision = spotMatrix[move[1]][move[0]].piece;

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

    public void renderBoard(){
        PrintStream printWriter = null;
        try{
            printWriter = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        } catch (Exception ex){
            System.out.println("Game was not able to print board");
            System.exit(1);
        }

        System.out.println("\n");
        for(int y = Board.height; y >= 0; y--){
            for(int x = 0; x <= Board.width; x++){
                if(x == 0 && y == 0){
                    printWriter.print(" ");
                }
                else if(x == 0){
                    printWriter.print(y);
                }
                else if(y == 0){
                    printWriter.print((SpecialCharacters.letterSpace + ((char) (64 + x)) + SpecialCharacters.letterSpace));
                }
                else if (spotMatrix[y][x].piece != null) {
                    printWriter.print(spotMatrix[y][x].piece.display);
                }
                else{
                    printWriter.print(SpecialCharacters.emptySpot);
                }
                printWriter.print(" | ");
            }
            printWriter.println();
        }
        printWriter.println();
        printWriter.print("Player 1: ");
        for(Piece piece: disqualifiedPieces){
            if(piece.getPlayer() == 2)
                printWriter.print(piece.display + SpecialCharacters.letterSpace);
        }
        System.out.println();
        System.out.print("Player 2: ");
        for(Piece piece : disqualifiedPieces){
            if(piece.getPlayer() == 1)
                printWriter.print(piece.display + SpecialCharacters.letterSpace);
        }
        printWriter.println("\n");
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
        addPiece(new Knight(new int[]{2,1}, 1));
        addPiece(new Bishop(new int[]{3,1}, 1));
        addPiece(new Queen(new int[]{4,1}, 1));
        addPiece(new King(new int[]{5,1}, 1));
        addPiece(new Bishop(new int[]{6,1}, 1));
        addPiece(new Knight(new int[]{7,1}, 1));
        addPiece(new Rook(new int[]{8,1}, 1));
        for(int x = 1; x <= Board.width; x++){
            addPiece(new Pawn(new int[]{x, 2}, 1));
        }

        //pieces for player 2
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
     * @return Key is 0 if neither player are in check<br>1 if the opponent is in check<br>2 if player is in check<br>3 if both are in
     * check
     */
    public AbstractMap.SimpleEntry<Integer, ArrayList<Piece>> isCheck(int[] origin, int[] destination, Player player) {

        boolean playerCheck = false;
        boolean opponentCheck = false;

        Piece pieceAtDestination = null;
        //getting piece at destination for safekeeping if there is one

        try {
            pieceAtDestination = this.getPieceAt(destination);
        } catch (EmptySpotException exception) {
            pieceAtDestination = null;
        }

        //moving piece to new location temporarily for checking
        try {
            this.movePiece(origin, destination);
        } catch (InvalidMoveException exception) {
            // if the location is the same, return 0 because this would not affect check
            return new AbstractMap.SimpleEntry<Integer, ArrayList<Piece>>(0, null);
        }


        // getting all possible moves for current player
        ArrayList<Piece> allPlayerPieces = null;
        ArrayList<Piece> allOpponentPieces = null;
        if (player.playerNum == 1) {
            allPlayerPieces = this.getPlayer1Pieces();
            allOpponentPieces = this.getPlayer2Pieces();
        } else {
            allPlayerPieces = this.getPlayer2Pieces();
            allOpponentPieces = this.getPlayer1Pieces();
        }

        //player's king
        Piece playerKing = null;

        //opponent's king
        Piece opponentKing = null;

        //all pieces checking opponent king
        ArrayList<Piece> piecesCheckingOpp = new ArrayList<>();


        //getting all moves for the player pieces
        //ArrayList<ArrayList<int[]>> allPlayerMoves = new ArrayList<>();
        for (Piece piece : allPlayerPieces) {
//            piece.getNewMoves(spotMatrix);
//            allPlayerMoves.add(piece.getMoves());
            if (piece.name.equals("King")) {
                playerKing = piece;
            }
        }

        // getting all moves for the opponent pieces
        //ArrayList<ArrayList<int[]>> allOpponentMoves = new ArrayList<>();
        for (Piece piece : allOpponentPieces) {
//            piece.getNewMoves(spotMatrix);
//            allOpponentMoves.add(piece.getMoves());
            if (piece.name.equals("King")) {
                opponentKing = piece;
            }
        }

        // if playerKing or opponentKing is null after moving the piece, the king is in check
        if(playerKing == null)
            return new AbstractMap.SimpleEntry<>(2, null);
        else if(opponentKing == null){
            return new AbstractMap.SimpleEntry<>(1, piecesCheckingOpp);
        }

        // checking if any of player moves coincide with the opponent's kings' position
        int[] opponentKingLocation = opponentKing.getLocation();
        for (Piece piece : allPlayerPieces) {
            piece.getNewMoves(spotMatrix);
            for (int[] move : piece.getMoves()) {
                if (Arrays.equals(move, opponentKingLocation)) {
                    opponentCheck = true;
                    piecesCheckingOpp.add(piece);
                }

            }
        }

        //checking if any of opponent moves coincide with player's king's position
        int[] playerKingPosition = playerKing.getLocation();
        for (Piece piece : allOpponentPieces) {
            piece.getNewMoves(spotMatrix);
            for (int[] move : piece.getMoves()) {
                if (Arrays.equals(move, playerKingPosition)) {
                    playerCheck = true;
                    break;
                }
            }
        }

        //returning pieces to their original locations and removing pieceAtDestination from disqualified pieces if any
        try {
            this.movePiece(destination, origin);
        } catch (InvalidMoveException exception) {

        }

        if (pieceAtDestination != null) {
            spotMatrix[destination[1]][destination[0]].piece = pieceAtDestination;
            this.getDisqualifiedPieces().remove(pieceAtDestination);
            if(player.playerNum == 1){
                player2Pieces.add(pieceAtDestination);
            }else {
                player1Pieces.add(pieceAtDestination);
            }
        }

        AbstractMap.SimpleEntry<Integer, ArrayList<Piece>> result;

        if(playerCheck){
            result = new AbstractMap.SimpleEntry<>(2, null);
        }
        else if(opponentCheck){
            result = new AbstractMap.SimpleEntry<>(1, piecesCheckingOpp);
        }
        else{
            result = new AbstractMap.SimpleEntry<>(0, null);
        }
        return result;

    }

    public boolean isCheckMate(ArrayList<Piece> piecesCheckingPlayer, Player player){

        //base logic
        //get all pieces whose valid moves would extend into the area created by pieces checking the king
        // for all those pieces, check if that/those move(s) would prevent the king from being in check
        // if yes then not checkmate
        // if no then checkmate
        //piecesCheckingPlayer is a list of all pieces checking the king

        //List of moves (area) of pieces checking the king

        //if player is in check
        if(!player.inCheck){
            return false;
        }

        boolean checkmate = true;

        ArrayList<int[]> areaInCheck = new ArrayList<>();
        for(Piece piece : piecesCheckingPlayer){
            piece.getNewMoves(spotMatrix);
            areaInCheck.addAll(piece.getMoves());
        }
        // List of pieces that have a move that extends into the check area // triple for loop, yikes! But limited data set
        // and the moves of those pieces
        HashMap<Piece, ArrayList<int[]>> checkAreaCollisionMoves = new HashMap<Piece, ArrayList<int[]>>();

        Piece playerKing = null;

        // for every piece that a player has
        for(Piece piece : player.getPieces()){

            if(piece.name.equals(("King")))
                playerKing = piece;

            //gets its moves
            piece.getNewMoves(spotMatrix);
            checkAreaCollisionMoves.put(piece, new ArrayList<>());

            // for every move that that piece can make
            for(int[] move : piece.getMoves()){

                //for every spot in the checked area
                for(int[] checkedArea : areaInCheck){

                    // if the move that the piece can make coincides with a spot in that checked area
                    if(Arrays.equals(checkedArea, move)){

                        // add the move and associate it with the piece that can make it
                        checkAreaCollisionMoves.get(piece).add(move);
                    }
                }
            }
        }

        //for every piece that extends into the checked area
        for(Map.Entry<Piece, ArrayList<int[]>> piece : checkAreaCollisionMoves.entrySet()){

            // for every move of that piece
            for(int[] move : piece.getValue()){

                // check if making that move would put remove the king from check
                if(isCheck(piece.getKey().getLocation(), move, player).getKey() == 0){
                    // break out of all loops and return false if there is even one
                    // move that prevents check
                    checkmate = false;
                }
            }
        }

        //check if the king making a move would remove it from being checked
        if(playerKing != null){

            // for every move the king can make
            playerKing.getNewMoves(spotMatrix);
            ArrayList<int[]> moves = (ArrayList<int[]>) playerKing.getMoves().clone();
            int[] kingLocation = playerKing.getLocation();

            for(int[] move : moves){

                // if the kind made that move means getting it out of check
                if(isCheck(kingLocation, move, player).getKey() == 0){
                    checkmate = false;
                }
            }
        }

        // if none of the moves prevent check, then it is checkmate
        return checkmate;
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
                if(isCheck(kingLocation, move, player).getKey() != 0){
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

    public boolean exchange(int[] oldPiece, String newPieceType) throws InvalidMoveException{

        Pawn piece;
        try{
            piece = (Pawn) getPieceAt(oldPiece);
        }catch (Exception exception){
            throw new InvalidMoveException("There is no pawn at the location specified");
        }

        //checking if the pawn specified is at the end of the board
        if(piece.getLocation()[1] != 1 && piece.getLocation()[1] != 8)
            throw new InvalidMoveException("The pawn specified is not eligible for an exchange at this point");

        // instantiating the new piece
        Piece newPiece = null;
        Class pieceClass = null;
        Constructor<?> newPieceConst;

        try{
            pieceClass = Class.forName("pieces." + newPieceType.strip());
            newPieceConst = pieceClass.getConstructor(int[].class, int.class);
        } catch (Exception exception){
            throw new InvalidMoveException("There is no piece named " + newPieceType);
        }


        if(pieceClass != null)
            try{
                newPiece = (Piece) newPieceConst.newInstance(oldPiece, piece.getPlayer());
            } catch (Exception exception){
                throw new InvalidMoveException("Unable to exchange piece, please try again");
            }



        //if the new piece could not be instantiated
        if(newPiece == null){
            throw new InvalidMoveException("Cannot exchange " + Arrays.toString(oldPiece) + " for " + newPieceType);
        }

        // adding newly created piece to the trackers
        if(piece.getPlayer() == 1){
            player1Pieces.add(newPiece);
        }else {
            player2Pieces.add(newPiece);
        }

        // exchanging pieces
        return replacePiece(newPiece, oldPiece);
    }

    public void setDisqualifiedPieces(ArrayList<Piece> disqualifiedPieces) {
        this.disqualifiedPieces = disqualifiedPieces;
    }

}
