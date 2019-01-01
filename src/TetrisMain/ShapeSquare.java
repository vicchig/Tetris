
package TetrisMain;

import java.awt.Color;


public class ShapeSquare extends Shape{
    public Color color;
    public ShapeSquare(String fileName, int x, int y, Color color){
        super(fileName, x, y, color);
        buildShape(fileName);
        centerX = tiles.get(0).x + TILEWIDTH;
        centerY = tiles.get(0).y + TILEHEIGHT;
    }

    @Override
    public void buildShape(String file) {
        tiles.add(new GraphicsObject(file, initialX + TILEWIDTH, initialY));
        tiles.add(new GraphicsObject(file, initialX, initialY + TILEHEIGHT));
        tiles.add(new GraphicsObject(file, initialX + TILEWIDTH, initialY + TILEHEIGHT));
    }

    @Override
    public void rotateShape() {
        // A square cannot be rotated.
    }
    
}
