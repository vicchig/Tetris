package TetrisMain;

import java.awt.Color;
import java.awt.Graphics2D;

public class ShapeL extends ShapeLine{
    public Color color;
    public ShapeL(String fileName, int x, int y, Color color){
        super(fileName, x, y, color);
    }

    public void buildShape(String file) {
        super.buildShape(file); 
        this.tiles.add(new GraphicsObject(file, initialX + TILEWIDTH,
                initialY + 2*TILEHEIGHT));
    }
    public void paint(Graphics2D g){
        super.paint(g);
    }
}
