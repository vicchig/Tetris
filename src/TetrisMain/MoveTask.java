
package TetrisMain;

import java.util.TimerTask;

public class MoveTask extends TimerTask{
    private Shape shape;
    
    public MoveTask(Shape shape){
        this.shape = shape;
    }
    public void run(){
        if (shape.isActive()) {
            shape.moveY();
        }
        
    }
}
