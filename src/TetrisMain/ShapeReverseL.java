
package TetrisMain;

import java.awt.Color;


public class ShapeReverseL extends ShapeLine{
    public Color color;
    public ShapeReverseL(String fileName, int x, int y, Color color){
        super(fileName, x, y, color);
    }

    @Override
    public void buildShape(String file) {
        super.buildShape(file);
        tiles.add(new GraphicsObject(file, initialX - TILEWIDTH, initialY + TILEHEIGHT * 2));
    }
    
    
}
