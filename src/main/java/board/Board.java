package board;


import characters.SpecialCharacters;
import game.exceptions.EmptySpotException;
import pieces.*;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

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

    public void movePiece(int[] pieceLocation, int[] newLocation){
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

    public ArrayList<int[]> getMoves(int[] pieceLocation){

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

    public void setDisqualifiedPieces(ArrayList<Piece> disqualifiedPieces) {
        this.disqualifiedPieces = disqualifiedPieces;
    }
}
