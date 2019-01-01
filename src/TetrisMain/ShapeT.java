
package TetrisMain;

import java.awt.Color;
import java.awt.Graphics2D;

public class ShapeT extends Shape{
    public Color color;
    public ShapeT(String fileName, int x, int y, Color color){
        super(fileName, x, y, color);
        this.buildShape(this.fileName);
        centerX = (int)Math.round(tiles.get(0).x + TILEWIDTH/2);
        centerY = (int)Math.round(tiles.get(0).y + TILEHEIGHT * 1.5);
    }

    @Override
    public void buildShape(String file) {
        tiles.add(new GraphicsObject(file, initialX, initialY + TILEHEIGHT));
        tiles.add(new GraphicsObject(file, initialX + TILEWIDTH, initialY + TILEHEIGHT));
        tiles.add(new GraphicsObject(file, initialX - TILEWIDTH, initialY + TILEHEIGHT));
    }

    @Override
    public void paint(Graphics2D g) {
        super.paint(g);
    }
    
}
