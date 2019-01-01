
package TetrisMain;

import java.awt.Color;
import java.awt.Graphics2D;

public class ShapeLine extends Shape{
   public ShapeLine(String fileName, int x, int y, Color color) {
        super(fileName, x, y, color);
        this.buildShape(this.fileName);
        centerX = (int)Math.round(tiles.get(0).x + TILEWIDTH/2);
        centerY = (int)Math.round(tiles.get(1).y + TILEHEIGHT/2);
    }

    @Override
    public void buildShape(String file) {
        tiles.add(new GraphicsObject(file, initialX, initialY + TILEHEIGHT));
        tiles.add(new GraphicsObject(file, initialX, initialY + 2*TILEHEIGHT));
    }
    public void paint(Graphics2D g){
        super.paint(g);
    }
}
