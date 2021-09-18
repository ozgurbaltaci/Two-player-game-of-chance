/**
 * @Author Özgür BALTACI
 * @Since 06/11/2020
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Tile { // Actually my Tile Class represents Terrain I just mixed the words but doesn't matter.
    private int height;
    private int width;
    private int numOfPlayer;
    private int numOfObstacle;
    private String[][] tile;
    Player[] playersArray;
    int[][] numOfPlayerInOneCell;



    public Tile() throws FileNotFoundException {
        File varFile = new File("var.cfg");
        Scanner scn = new Scanner(varFile);

        String[] details = new String[4];
        String line;
        for (int i = 0; scn.hasNextLine(); i++) {
            line = scn.nextLine();
            String[] temp = line.split(": ");
            details[i] = temp[1];
        }

        this.height = Integer.parseInt(details[0]);
        this.width = Integer.parseInt(details[1]);
        this.numOfPlayer = Integer.parseInt(details[2]);
        this.numOfObstacle = Integer.parseInt(details[3]);
        this.tile = new String[this.height][this.width];

        playersArray = new Player[this.numOfPlayer];
        numOfPlayerInOneCell = new int[this.height][this.width];

        for (int row = 0; row < this.width; row++) {
            for (int column = 0; column < this.height; column++) {
                tile[row][column] = "-";
            }
        }

        Random random = new Random();

        int xRandomFinalPoint = random.nextInt(this.width);
        int yRandomFinalPoint = random.nextInt(this.height);
        tile[yRandomFinalPoint][xRandomFinalPoint] = "F";

        for (int i = 0; i < this.numOfObstacle; i++) {  // Putting obstacles
            int XRandomObstacle = random.nextInt(this.width);
            int YRandomObstacle = random.nextInt(this.height);
            if (!tile[YRandomObstacle][XRandomObstacle].equals("O") && !tile[YRandomObstacle][XRandomObstacle].equals("F"))
                tile[YRandomObstacle][XRandomObstacle] = "O";
            else
                i--;
        }

        for (int i = 0; i < this.numOfPlayer; i++) { // Putting players if there is no obstacle

            int xRandomPlayer = random.nextInt(this.width);
            int yRandomPlayer = random.nextInt(this.height);

              if(!tile[yRandomPlayer][xRandomPlayer].equals("O") && !tile[yRandomPlayer][xRandomPlayer].equals("F")){
                  Player player = new Player("p" + (i + 1), yRandomPlayer, xRandomPlayer);
                  playersArray[i] = player;

                  if (!tile[yRandomPlayer][xRandomPlayer].equals("-")){ // if there is another player
                      numOfPlayerInOneCell[yRandomPlayer][xRandomPlayer]++;
                      tile[yRandomPlayer][xRandomPlayer] = Integer.toString(numOfPlayerInOneCell[yRandomPlayer][xRandomPlayer]);
                  }
                  else {
                      tile[yRandomPlayer][xRandomPlayer] = playersArray[i].getName();
                      numOfPlayerInOneCell[yRandomPlayer][xRandomPlayer] = 1;
                  }
            } else
                i--;
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getNumOfPlayer() {
        return numOfPlayer;
    }

    public int getNumOfObstacle() {
        return numOfObstacle;
    }

    public void setTile(String[][] tile) {
        this.tile = tile;
    }

    public String[][] getTile() {
        return tile;
    }
    public Player[] getPlayersArray() {
        return playersArray;
    }

    public int[][] getNumOfPlayerInOneCell() {
        return numOfPlayerInOneCell;
    }
    public boolean isInteger(String s){
        try{
            Integer.parseInt(s);
        }catch(NumberFormatException e){
            return false;
        }
        return true;
    }

    public void reOrganizeCurrentCell (Player currentPlayer) {
        if (numOfPlayerInOneCell[currentPlayer.getyLocation()][currentPlayer.getxLocation()] == 1) {
            tile[currentPlayer.getyLocation()][currentPlayer.getxLocation()] = "-";
            numOfPlayerInOneCell[currentPlayer.getyLocation()][currentPlayer.getxLocation()] = 0;
        }
        else if (numOfPlayerInOneCell[currentPlayer.getyLocation()][currentPlayer.getxLocation()] == 2) {
            for (int x = 0; x < numOfPlayer; x++) {
                if (playersArray[x] != currentPlayer) {
                    if (currentPlayer.getxLocation() == playersArray[x].getxLocation() && currentPlayer.getyLocation() == playersArray[x].getyLocation()) {
                        tile[currentPlayer.getyLocation()][currentPlayer.getxLocation()] = playersArray[x].getName();
                        numOfPlayerInOneCell[currentPlayer.getyLocation()][currentPlayer.getxLocation()] = 1;
                    }
                }
            }
        }
        else if (numOfPlayerInOneCell[currentPlayer.getyLocation()][currentPlayer.getxLocation()] > 2 ){
            numOfPlayerInOneCell[currentPlayer.getyLocation()][currentPlayer.getxLocation()]--;
            tile[currentPlayer.getyLocation()][currentPlayer.getxLocation()] = Integer.toString(numOfPlayerInOneCell[currentPlayer.getyLocation()][currentPlayer.getxLocation()]);
        }
    }


    public static void main(String[] args) throws FileNotFoundException, InterruptedException {

        Tile tile = new Tile();
        File atTheBeginning = new File ("output_Beginning.cfg");
        PrintWriter Beginning = new PrintWriter(atTheBeginning);
        System.out.println("Terrain view at the beginning of the game: \n**********************************************");
        Beginning.println("Terrain view at the beginning of the game.");
        for (int i = 0; i < tile.getHeight(); i++) {
            for (int j = 0; j < tile.getWidth(); j++) {
                System.out.printf("%5s",tile.getTile()[i][j]);
                Beginning.printf("%5s",tile.getTile()[i][j]);
            }
            System.out.println();
            Beginning.println();
        }
        Beginning.close();
        
        boolean isGameOver = false;
        int count = 1;
        while (!isGameOver){

            for(int i =0; i<tile.getPlayersArray().length; i++){

                File outputFiles = new File("output_" + count++ +".txt");
                PrintWriter output = new PrintWriter(outputFiles);

                int movetile = tile.getPlayersArray()[i].move();

                boolean wannaMove;
                Scanner input = new Scanner (System.in);

                Player currentPlayer = tile.getPlayersArray()[i];

                int xLocationOfPlayer = currentPlayer.getxLocation() ;
                int yLocationOfPlayer = currentPlayer.getyLocation();
                int xAfterMoving = xLocationOfPlayer;
                int yAfterMoving = yLocationOfPlayer;
                String nameOfPlayer = currentPlayer.getName();

                String northOfPlayer = "";

                boolean didPlayerMove = false;
                boolean isNorthUsable = false;
                if(yLocationOfPlayer - 1 < tile.getHeight() && yLocationOfPlayer - 1 >= 0){
                    northOfPlayer = tile.getTile()[yLocationOfPlayer - 1][xLocationOfPlayer];
                    isNorthUsable = true;
                }

                String eastOfPlayer = "";
                boolean isEastUsable = false;
                if (xLocationOfPlayer + 1 < tile.getWidth() && xLocationOfPlayer + 1 >= 0) {
                    eastOfPlayer = tile.getTile()[yLocationOfPlayer][xLocationOfPlayer + 1];
                    isEastUsable = true;
                }

                String southOfPlayer = "";
                boolean isSouthUsable = false;
                if(yLocationOfPlayer + 1 < tile.getHeight() && yLocationOfPlayer + 1 >= 0) {
                    southOfPlayer = tile.getTile()[yLocationOfPlayer + 1][xLocationOfPlayer];
                    isSouthUsable = true;
                }

                String westOfPlayer = "";
                boolean isWestUsable = false;
                if (xLocationOfPlayer - 1 < tile.getWidth() && xLocationOfPlayer - 1 >= 0) {
                    westOfPlayer = tile.getTile()[yLocationOfPlayer][xLocationOfPlayer - 1];
                    isWestUsable = true;
                }

                if(movetile == 0){ //player move to NORTH
                    if (!northOfPlayer.equals("O") && isNorthUsable){
                        didPlayerMove = true;
                        System.out.println(nameOfPlayer + " do you want to move up ? (press 1 to move and press 0 to stay -You are in (" + yLocationOfPlayer + "," + xLocationOfPlayer +") now 'according to description in readme.txt'-");
                        int answerN = input.nextInt();
                        if (currentPlayer.doYouWannaMove(answerN)) {
                            System.out.println(currentPlayer.getName() + " moved to the up");
                            output.println(currentPlayer.getName() + " moved to the up");

                            yAfterMoving--;
                            if (tile.getTile()[yAfterMoving][xAfterMoving].equals("F")) {
                                System.out.println(currentPlayer.getName() + " has won !!");
                                output.println(currentPlayer.getName() + " has won !!");
                                isGameOver = true;
                                output.close();
                                break;
                            }
                        }
                        else {
                            output.println(nameOfPlayer + " did not want to move up and didn't move");
                            System.out.println(nameOfPlayer + " did not want to move up and didn't move");
                        }
                    }
                }
                if(movetile == 1) { //player move to EAST
                    if (!eastOfPlayer.equals("O") && isEastUsable) {
                        didPlayerMove = true;
                        System.out.println(nameOfPlayer + " do you want to move right ? (press 1 to move and press 0 to stay) -You are in (" + yLocationOfPlayer + "," + xLocationOfPlayer +") now 'according to description in readme.txt'-");
                        int answerN = input.nextInt();
                        if (currentPlayer.doYouWannaMove(answerN)) {
                            System.out.println(currentPlayer.getName() + " moved to right");
                            output.println(currentPlayer.getName() + " moved to the right");

                            xAfterMoving++;
                            if (tile.getTile()[yAfterMoving][xAfterMoving].equals("F")) {
                                System.out.println(currentPlayer.getName() + " has won !!");
                                output.println(currentPlayer.getName() + " has won !!");
                                isGameOver = true;
                                output.close();
                                break;

                            }
                        }
                        else {
                            output.println(nameOfPlayer + " did not want to move right and didn't move");
                            System.out.println(nameOfPlayer + " did not want to move right and didn't move");
                        }
                    }
                }
                if(movetile == 2){ //player move to SOUTH
                    if (!southOfPlayer.equals("O") && isSouthUsable) {
                        didPlayerMove = true;
                        System.out.println(nameOfPlayer + " do you want to move down ? (press 1 to move and press 0 to stay ) -You are in (" + yLocationOfPlayer + "," + xLocationOfPlayer +") now 'according to description in readme.txt'-");
                        int answerN = input.nextInt();
                        if (currentPlayer.doYouWannaMove(answerN)) {
                            System.out.println(currentPlayer.getName() + " moved to the down");
                            output.println(currentPlayer.getName() + " moved to the down.");

                            yAfterMoving++;
                            if (tile.getTile()[yAfterMoving][xAfterMoving].equals("F")) {
                                System.out.println(currentPlayer.getName() + " has won !!");
                                output.println(currentPlayer.getName() + " has won !!");
                                isGameOver = true;
                                output.close();
                                break;
                            }
                        }
                        else {
                            output.println(nameOfPlayer + " did not want to move down and didn't move");
                            System.out.println(nameOfPlayer + " did not want to move down and didn't move");
                        }

                    }
                }
                if(movetile == 3){ //player move to WEST
                    if (!westOfPlayer.equals("O") && isWestUsable){
                        System.out.println(nameOfPlayer + " do you want to move left ? (press 1 to move and press 0 to stay) -You are in (" + yLocationOfPlayer + "," + xLocationOfPlayer +") now 'according to description in readme.txt'-" );
                        int answerN = input.nextInt();
                        didPlayerMove = true;
                        if (currentPlayer.doYouWannaMove(answerN)) {
                            System.out.println(currentPlayer.getName() + " moved to the left.");
                            output.println(currentPlayer.getName() + " moved to the left.");

                            xAfterMoving--;
                            if (tile.getTile()[yAfterMoving][xAfterMoving].equals("F")) {
                                System.out.println(currentPlayer.getName() + " has won !!");
                                output.println(currentPlayer.getName() + " has won !!");
                                isGameOver = true;
                                output.close();
                                break;
                            }
                        }

                        else {
                            output.println(nameOfPlayer + " did not want to move left and didn't move");
                            System.out.println(nameOfPlayer + " did not want to move left and didn't move");
                        }
                    }
                }

                if(didPlayerMove) {

                    tile.reOrganizeCurrentCell(currentPlayer);

                    if(tile.getTile()[yAfterMoving][xAfterMoving].equals("-")){
                        tile.getTile()[yAfterMoving][xAfterMoving] = currentPlayer.getName();
                        tile.getNumOfPlayerInOneCell()[yAfterMoving][xAfterMoving] = 1;
                    }
                    else if (tile.getNumOfPlayerInOneCell()[yAfterMoving][xAfterMoving] > 0) {
                        tile.getNumOfPlayerInOneCell()[yAfterMoving][xAfterMoving]++;
                        tile.getTile()[yAfterMoving][xAfterMoving] = Integer.toString(tile.getNumOfPlayerInOneCell()[yAfterMoving][xAfterMoving]);
                    }

                    System.out.println("************************************************");
                    output.println("************************************************");
                    for (int x = 0; x < tile.getHeight(); x++) {
                        for (int b = 0; b < tile.getWidth(); b++) {
                            System.out.printf("%5s",tile.getTile()[x][b]);
                            output.printf("%5s",tile.getTile()[x][b]);
                        }
                        System.out.println();
                        output.println();
                    }
                    System.out.println();
                    Thread.sleep(500);
                    currentPlayer.setyLocation(yAfterMoving);
                    currentPlayer.setxLocation(xAfterMoving);
                }
                else {
                    System.out.println(currentPlayer.getName() + " couldn't move because there is an obstacle or outside of terrain.");
                    output.println(currentPlayer.getName() + " couldn't move because there is an obstacle or outside of terrain.");
                }

                output.close();
            }

        }
        System.out.println("Gamer over in " + (count - 1) + " turn(s).");
        }

    }