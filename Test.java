/***
 * @author Daniel DeMasi
 * October 2, 2022
 * Test: Includes main method to test object Board
 * IDE: VSCode, Java JDK 11
 */
public class Test {
    public static void main(String[] args){
        long runtime = 0;
        try{
            Board puzzle = new Board("sudoku.txt");
            System.out.println(puzzle);
            
            //calculate runtime for solve method
            long startTime = System.nanoTime();
            puzzle.solve();
            long endTime = System.nanoTime();
            runtime = endTime - startTime;

            System.out.println(puzzle);
        }
        catch(IllegalArgumentException e) {
            System.out.println("Soduko puzzle is unsolvable!");
        }

        System.out.println("Time to solve is " + runtime + " nanoseconds.");
    }
}
