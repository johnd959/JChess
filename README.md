## About

This is a chess game built using bare Java. The game is played on the command line and
it is ideal that it is run with [JDK 21](https://www.oracle.com/java/technologies/downloads/). 

## How to run

You can clone this repository and run it in your preferred IDE. This project was coded in IntelliJ IDEA.
The chess piece characters are known to work on the IntelliJ IDEA console, so to utilize those pieces,
uncomment the special characters inside the SpecialCharacters class and the Piece classes. 

#### Or

You can download the [jar (Java Archive)](https://github.com/johnd959/JChess/tree/main/out/artifacts/chess_jar) located in this repository and run it like so: 

    java -jar [file location] 

### Example

    java -jar "C:\Users\UserName\Downloads\chess"

The jar is located at [JChess/out/artifacts/chess_jar](https://github.com/johnd959/JChess/tree/main/out/artifacts/chess_jar).

## How to play 

The rules of the game should be identical to that of regular chess. 
Upon running the program, the current state of the board should be printed to the console
and Player 1 is then prompted for a move. The game should alternate between Player 1 and Player 2.

### Commands 

- (move) M (origin) (destination)

- (castle) C (rook location)

- (upgrade/exchange) E (pawn location) (piece desired)

### Examples

- Move piece at A2 to A4: M A2 A4
- Castle left rook: C A1
- Exchange pawn for a king: 

Play until a checkmate!

## Disclaimer

There were no bugs when last tested, but the game could very well be buggy; therefore, there may be some unexpected behavior. 

### These include

- Not being able to make a move even when legal
- Game not being able to detect checkmate properly

