package TetrisMain;

import java.util.Collections;
import java.util.Comparator;


public class CustomComparator implements Comparator<String> {
    
    public int compare(String s1, String s2){
        int num1 = Integer.parseInt(s1.split(":")[1]);
        int num2 = Integer.parseInt(s2.split(":")[1]);
        
        if(num1>num2){
            return -1;
        }
        else if(num1<num2){
            return 1;
        }
        return 0;
    }
}