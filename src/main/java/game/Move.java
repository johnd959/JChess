package game;

import characters.SpecialCharacters;

import java.util.ArrayList;
import java.util.List;

public class Move {

    int[] origin;
    int[] destination;

    public Move(List<String> coordinates){
        ArrayList<int[]> moveCoordinates = convertCoordinates(coordinates);
        this.origin = moveCoordinates.get(0);
        this.destination = moveCoordinates.get(1);
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
