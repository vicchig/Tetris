
package TetrisMain;

import java.awt.Color;
import java.awt.Graphics2D;


public class ShapeZ extends Shape{
    public Color color;
    public ShapeZ(String fileName, int x, int y, Color color) {
        super(fileName, x, y, color);
        this.buildShape(this.fileName);
        centerX = (int)Math.round(tiles.get(0).x + TILEWIDTH/2);
        centerY = tiles.get(0).y + TILEHEIGHT/2;
    }

    
    public void buildShape(String file) {
        tiles.add(new GraphicsObject(file, this.initialX, this.initialY + this.TILEHEIGHT));
        tiles.add(new GraphicsObject(file, this.initialX + this.TILEWIDTH, this.initialY
                + this.TILEHEIGHT));
        tiles.add(new GraphicsObject(file, this.initialX - this.TILEWIDTH, this.initialY));
    }
    public void paint(Graphics2D g){
        super.paint(g);
    }
}
