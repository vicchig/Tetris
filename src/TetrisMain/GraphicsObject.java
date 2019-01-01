/*
A class for loading images into the game. This implementation does not use SpriteSheet since Tetris does not need it, for the SpriteSheet implementation look at Trek code.
*/
package TetrisMain;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GraphicsObject {
    public int x, y;
    private BufferedImage img;
    
    public GraphicsObject(String fileName, int x, int y){
        this.x = x;
        this.y = y;
        try{
            img = ImageIO.read(this.getClass().getResource("/images/"+fileName));
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public void draw(Graphics g){
        g.drawImage(img, x, y, null);
    }
}
