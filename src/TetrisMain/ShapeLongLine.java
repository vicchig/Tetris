
package TetrisMain;

import java.awt.Color;
import java.awt.Graphics2D;

public class ShapeLongLine extends ShapeLine{
    public Color color;
    public ShapeLongLine(String fileName, int x, int y, Color color) {
        super(fileName, x, y, color);
    }

    @Override
    public void buildShape(String file) {
        super.buildShape(file);
        tiles.add(new GraphicsObject(file, initialX, initialY + 3*TILEHEIGHT));
    }
    public void paint(Graphics2D g){
        super.paint(g);
    }
}
