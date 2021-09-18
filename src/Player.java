import java.io.FileNotFoundException;
import java.util.Random;

public class Player {
    private String name;
    private int xLocation;
    private int yLocation;

    public Player(String name, int yLocation, int xLocation ) {
        this.name = name;
        this.xLocation = xLocation;
        this.yLocation = yLocation;
    }


    public String getName() {
        return name;
    }

    public int getxLocation() {
        return xLocation;
    }

    public int getyLocation() {
        return yLocation;
    }

    public void setxLocation(int xLocation) {
        this.xLocation = xLocation;
    }

    public void setyLocation(int yLocation) {
        this.yLocation = yLocation;
    }

    public boolean doYouWannaMove (int value){ // Player is asked if he/she wants to move
        if (value == 1) return true; // if player enter 1 it means player will move
        if (value == 0) return false; // if player enter 0 it means player won't move
        return false; // if player enter other value player won't move too.
    }

    public int move() throws FileNotFoundException {
        Random random = new Random();
        int direction = random.nextInt(4);

        if (direction == 0 ) // state 0 represents NORTH
            return 0;

        else if (direction == 1) // state 1 represents EAST
            return 1;

        else if (direction == 2) // state 2 represents SOUTH
            return 2;

        else // state 3 represents WEST
            return 3;



    }

}
