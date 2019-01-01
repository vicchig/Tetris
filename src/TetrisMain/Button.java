/**
 * A trapezoid button implementation.
 */
package TetrisMain;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;


public class Button {
    private Polygon buttonShape;
    private String text;
    private Color color, color2, textColor;
    private int x, y;
    private Font textFont;
    private boolean changeColor;
    
    /**
     * Creates a new button with the specified x and y coordinate of the top 
     * left corner as well as the specified text, font and button color as well
     * as the specified second color and specified text color. Text color
     * is set to Color.BLACK by default.
     * 
     * @param x The x position of the top left corner of the button.
     * @param y The y position of the top left corner of the button.
     * @param text The text displayed on the button.
     * @param color The color of the button.
     * @param color2 The color the shape changes to when needed.
     * @param font Text font of the button.
     */
    public Button(int x, int y, String text, Color color, Color color2,
            Font font){
        this.text = text;
        this.color = color;
        this.color2 = color2;
        this.textColor = Color.BLACK;
        this.x = x;
        this.y = y;
        
        textFont = font;
        changeColor = false;
        
        buttonShape = new Polygon();
        buttonShape.addPoint(x - 175, y);//top left corner
        buttonShape.addPoint(x + 175, y);//top right corner
        buttonShape.addPoint(x + 200, y + 50);//bottom right
        buttonShape.addPoint(x - 200, y + 50);//bottom left
    }
    
    /**
     * Creates a new button with the specified x and y coordinate of the top 
     * left corner as well as the specified text, font and button color as well
     * as the specified second color and specified text color.
     * 
     * @param x The x position of the top left corner of the button.
     * @param y The y position of the top left corner of the button.
     * @param text The text displayed on the button.
     * @param color The color of the button.
     * @param color2 The color the shape changes to when needed.
     * @param textColor The color of this button's text.
     * @param font Text font of the button.
     */
    public Button(int x, int y, String text, Color color, Color color2, 
            Color textColor, Font font){
        this.text = text;
        this.color = color;
        this.color2 = color2;
        this.textColor = textColor;
        this.x = x;
        this.y = y;
        
        textFont = font;
        changeColor = false;
        
        buttonShape = new Polygon();
        buttonShape.addPoint(x - 175, y);//top left corner
        buttonShape.addPoint(x + 175, y);//top right corner
        buttonShape.addPoint(x + 200, y + 50);//bottom right
        buttonShape.addPoint(x - 200, y + 50);//bottom left
    }
    
    /**
     * Returns whether the mouse located at coordinate (x, y)is contained 
     * within this button.
     * 
     * @param x The x coordinate of the mouse.
     * @param y The y coordinates of the mouse.
     * @return true if the mouse is inside this button, false otherwise.
     */
    public boolean mouseCollision(int x, int y){
        Rectangle mouse = new Rectangle(x, y, 1, 1);
        
        if(buttonShape.contains(mouse)){
            return true;
        }
        return false;
    }
    
    /**
     * Changes the color of this button to color2 using the Graphics object and 
     * redraws the text at position x.
     * 
     * @param g Graphics object used to draw this button.
     * @param x x coordinate of the button text.
     */
    public void changeColor(Graphics g, int x){
        g.setColor(color2);
        g.fillPolygon(buttonShape);
        g.setColor(textColor);
        g.setFont(textFont);
        g.drawString(text, x, y + 35);
    }
    
    /**
     * Paints the button using the Graphics object g and draws the button text 
     * at coordinate x.
     * 
     * @param g Graphics object used to paint this button.
     * @param x x coordinate of the button text.
     */
    public void paint(Graphics g, int x){
        g.setColor(this.color);
        g.fillPolygon(buttonShape);
        g.setFont(textFont);
        g.setColor(textColor);
        g.drawString(text, x, y + 35);
        
        if (changeColor) {
            this.changeColor(g, x);
        }
    }
    
    //============GETTERS AND SETTERS================

    public Polygon getButtonShape() {
        return buttonShape;
    }

    public Color getColor() {
        return color;
    }

    public String getText() {
        return text;
    }

    public Font getTextFont() {
        return textFont;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isChangeColor() {
        return changeColor;
    }
    
    public void setButtonShape(Polygon buttonShape) {
        this.buttonShape = buttonShape;
    }

    public void setChangeColor(boolean changeColor) {
        this.changeColor = changeColor;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTextFont(Font textFont) {
        this.textFont = textFont;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }  
}
