
package TetrisMain;

import java.awt.Color;
import java.awt.Graphics2D;


public class ShapeReverseZ extends Shape{
    public Color color;
    public ShapeReverseZ(String fileName, int x, int y, Color color){
        super(fileName, x, y, color);
        this.buildShape(this.fileName);
        centerX = (int)Math.round(tiles.get(0).x + TILEWIDTH/2);
        centerY = tiles.get(0).y + TILEHEIGHT/2;
    }

    @Override
    public void buildShape(String file) {
        tiles.add(new GraphicsObject(file, initialX, initialY + TILEHEIGHT));
        tiles.add(new GraphicsObject(file, initialX - TILEWIDTH, initialY
                + this.TILEHEIGHT));
        tiles.add(new GraphicsObject(file, initialX + TILEWIDTH, initialY));
    }

    @Override
    public void paint(Graphics2D g) {
        super.paint(g); 
    }
    
}
