
package TetrisMain;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Debris {
    
    public Rectangle rect = new Rectangle();
    private Color color;
    public double speedY, speedX;
    private boolean active = true;
    private double distanceMoved = 0;
    public Debris(int x, int y, Color color){
        rect.x = x - 1;
        rect.y = y - 1;
        rect.width = 2;
        rect.height = rect.width;
        this.color = color;
    }
    
    public void move(){
        double tempX = rect.x;
        double tempY = rect.y;
        
        tempX += speedX;
        tempY += speedY;
        
        rect.x = (int)tempX;
        rect.y = (int)tempY;
        
        distanceMoved += Math.sqrt(Math.pow(speedX, 2) + Math.pow(speedY, 2));
        if (distanceMoved >= 40) {
            active = false;
        }
    }
    
    public void paint(Graphics g){
        g.setColor(color);
        g.fillRect(rect.x, rect.y, rect.width, rect.height);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
