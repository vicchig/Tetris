/**
 * A manager class that provides extra functionality for work with arrays and 
 * 2d arrays.
 */
package TetrisMain;


public class ArrayController {
    
    /**
     * Displays the 2d array box by row and column.
     * 
     * @param box The given the 2d integer array being displayed.
     */
    public static void display2D(int[][] box){
        for(int i = 0;i<box.length;i++){
            for(int j = 0;j<box[i].length;j++){
                System.out.print(box[i][j]+" ");
            }
            System.out.println(" ");
        }
    }
    
    /**
     * Fills the given row of the 2d array list with the given integer number.
     * 
     * @param list The 2d integer array to be filled.
     * @param number The integer with which the row is to be filled.
     * @param row The row to be filled. (Should be -1 of the actual row since 
     * indeces start at 0)
     */
    public static void insertRow(int[][] list, int number, int row){
        for (int i = 0; i < list[row].length; i++) {
            list[row][i] = number;
        }
    }
    
    /**
     * Fills the given 2d array list with number at each index.
     * 
     * @param list The 2d integer array to be filled.
     * @param number The integer with which the array is to be filled.
     */
    public static void fillArray2D(int[][] list, int number){
        for (int i = 0; i < list.length; i++) {
            for (int j = 0; j < list[i].length; j++) {
                list[i][j] = number;
            }
        }
    }
    
    /**
     * Returns whether array list contains the integer item.
     * 
     * @param list The 1d integer array that is being searched.
     * @param item The integer being searched for.
     * @return true if item i in list, false otherwise.
     */
    public static boolean contains(int[] list, int item){
        for (int i = 0; i < list.length; i++) {
            if (list[i] == item) {
                return true;
            }
        }
        return false;
    } 
}
