/***
 * @author Daniel DeMasi
 * October 2, 2022
 * Board: Class to model the entity Board for a sodoku board
 * IDE: VSCode, Java JDK 11
 */
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
public class Board{
    //data members
    private ArrayList<ArrayList<Integer>> board;
    private ArrayList<Integer> availableNumbers;
    private final int Empty = 0;
    /***
	 * Constructor with one parameter
	 * @param	filename for the name of file
     * Initilizes the board with contents from the file
     * Initilizes availableNumbers with how many of each digit are needed (9)
	 */
    public Board(String filename){
        ArrayList<Integer> emptyList = new ArrayList<>(9);
        availableNumbers = new ArrayList<>(9);
        board = new ArrayList<>(9);

        for(int j = 0; j < 9; j++)
            emptyList.add(Empty);
        for(int i = 0; i < 9; i++)
            board.add(emptyList);
        for(int k = 0; k < 9; k++)
            availableNumbers.add(9);

        readBoard(filename);
    }
    /***
     * Method to read in the 
     * @param filename for the name of the file with suduku info
     * @throws IllegalArgumentException
     */
    private void readBoard(String filename)
                    throws IllegalArgumentException{
        File file = new File(filename);
        try{
            Scanner readFile = new Scanner(file);
            do{
                for(int i = 0; i < 9; i++){
                    ArrayList<Integer> row = new ArrayList<>(9);
                    for(int j = 0; j < 9; j++){
                        int number = readFile.nextInt();
                        if(number == Empty){
                            row.add(number);
                        }
                        else if(isAvailable(number)){  
                            //decrement availableNumbers
                            availableNumbers.set(number - 1, availableNumbers.get(number - 1) - 1);
                            //add number to row
                            row.add(number);
                            board.set(i, row);
                        }
                        else{
                            throw new IllegalArgumentException();
                        }  
                    }
                }
            } while(readFile.hasNextLine());

            for(int i = 0; i < 9; i++){
                for(int j = 0; j < 9; j++){
                    if(!checkMove(i, j)){
                        throw new IllegalArgumentException();
                    }
                }
            }

            readFile.close();
        }
        catch(FileNotFoundException e){
            System.out.println("Could not find file " + filename);
        }
    }
    /***
     * Recursive method to solve the sudoku puzzle
     * @return true only if can be solved, false otherwise
     * Fills all Empty (0) digits on board if possible
     */
    public boolean solve(){
        //base case
        if(noNumbersLeft()){ //checks if availableNumbers is empty
            return true; //all numbers are used
        }
        //iterate through entire board
        for(int row = 0; row < board.size(); row++){
            for(int col = 0; col < board.get(row).size(); col++){
                if(board.get(row).get(col) == Empty){ //only want to change 0's on board
                    for(int i = 1; i <= 9; i++){ //test every number 1 through 9
                        if(isAvailable(i)){ //check availableNumbers
                            board.get(row).set(col, i); //set number
                            availableNumbers.set(i - 1, availableNumbers.get(i - 1) - 1); //decrement availableNumbers
                            //recursive call
                            //check if number works in that position
                            if(checkMove(row, col) && solve()){ //solve next position until noNumbersLeft == true
                                return true;
                            } else{ //number does not work
                                availableNumbers.set(i - 1, availableNumbers.get(i - 1) + 1); //increment availableNumbers
                                board.get(row).set(col, 0); //set position back to Empty 0
                            }
                        }
                    } return false; //if position does not work
                }
            }
        }
        throw new IllegalArgumentException(); //if imposible to solve
    }
    /***
     * Method to check if a move is valid according to Sudoku rules
     * @param row for the row of the board
     * @param col for the column of the board
     * @return true only if the location is the only occurence of that 
     * digit in a given column, row, and block
     * false otherwise
     */
    private boolean checkMove(int row, int col){
        int value = board.get(row).get(col);
    
        //check if Empty
        if(value == Empty){
            return true;
        }

        //check row
        for(int i = 0; i < board.get(row).size(); i++){
            if(i != col && board.get(row).get(i) == value){
                return false;
            }
        }

        //check column
        for(int i = 0; i < 9; i++){
            if(i != row && board.get(i).get(col) == value){
                return false;
            }
        }

        /* Create an array for desired block
         * 1 2 3
         * 4 5 6
         * 7 8 9
         */
        ArrayList<Integer> block = new ArrayList<>(9);
        switch(row){
            case 0:
            case 1:
            case 2:
                if(col < 3){
                    block = createBlock(0, 0);
                } else if(col < 6){
                    block = createBlock(0, 3);
                } else{
                    block = createBlock(0, 6);
                }
                break;
            case 3:
            case 4:
            case 5:
                if(col < 3){
                    block = createBlock(3, 0);
                } else if(col < 6){
                    block = createBlock(3, 3);
                } else{
                    block = createBlock(3, 6);
                }
                break;
            case 6:
            case 7:
            case 8:
                if(col < 3){
                    block = createBlock(6, 0);
                } else if(col < 6){
                    block = createBlock(6, 3);
                } else{
                    block = createBlock(6, 6);
                }
                break;
        }

        //check block
        int count = 0;
        for(int i = 0; i < block.size(); i++){
            if(value == block.get(i)){
                count++;
            }
            if(count > 1){ //need to count position being checked
                return false;
            }
        }

        return true;

    }
    /***
     * Helper method to create an array for each block
     * @param row
     * @param col
     * @return one dimensional array of block
     */
    private ArrayList<Integer> createBlock(int row, int col){
        ArrayList<Integer> block = new ArrayList<>(9);
        for(int i = row; i < row + 3; i++){
            for(int j = col; j < col + 3; j++){
                block.add(board.get(i).get(j));
            }
        }
        return block;
    }
    /***
     * Method to determin wheter a digit is available to be placed
     * @param digit for the number being checking
     * @return true only if availableNumbers is greater than 0, false otherwise
     */
    private boolean isAvailable(int digit){
        return availableNumbers.get(digit - 1) != 0;
    }
    /***
     * Method to check if all occurances of each digit have been placed
     * @return true only if each cell in availableNumbers is 0, false otherwise
     */
    private boolean noNumbersLeft(){
        for(int i = 0; i < availableNumbers.size(); i++){
            if(availableNumbers.get(i) != 0){
                return false;
            }
        }
        return true;
    }
    /***
     * Method to override toString
     * @return 9 x 9 board with a space between each digit
     */
    @Override
    public String toString(){
        String x = "";
        for(int i = 0; i < board.size(); i++){
            for(int j = 0; j < board.get(i).size(); j++){
                x += board.get(i).get(j) + " ";
            }
            x += "\n";
        }
        return x;
    }
}
