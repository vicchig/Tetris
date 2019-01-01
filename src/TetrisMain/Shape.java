/*
Generic Shape that is used to create other shapes.
*/
package TetrisMain;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;


public class Shape {
    
    public ArrayList<GraphicsObject> tiles = new ArrayList();//list of rectangles that make up a Shape
    
    protected static final int TILEWIDTH = 20;// constants for the width and height of each reactangle in tiles
    protected static final int TILEHEIGHT = 20;
    
    protected int initialX, initialY; //coordinates of the first rectangle from which the rest of the Shape is built from
    protected int centerX = 0; //coordinates of the rotation center of the shape
    protected int centerY = 0;
    private int speed = 20; //descent speed
    
    protected String fileName; //file from which the image is loaded
    
    private boolean rotate = false; //controls shape rotation
    private boolean active = true;
    
    private Color color;
    
//  ==========GENERAL==========
    /*
    Initializes a new Shape with initial coordinates x and y
    */
    public Shape(String fileName, int x, int y, Color color){
        this.initialX = x;
        this.initialY = y;
        this.fileName = fileName;
        active = true;
        tiles.add(new GraphicsObject(this.fileName, initialX, initialY));
        this.color = color;
    }
    
    //creates a new Shape according to its type
    public void buildShape(String file){}
    
    /*
    Draws all Recntangle in this.tiles to g
    */
    public void paint(Graphics2D g){
        //if (!rotate) {
            for (int i = 0; i < tiles.size(); i++) {
                tiles.get(i).draw(g);
            }
        //}
    }
    
//  ==========MOVEMENT==========
    /*
    Moves all Rectangles in this.tiles by the specified this.speed
    */
    public void moveY(){
        int closestToBottom = 0;
        int grid1 = 0;
        int grid2 = 0;
        int grid3 = 0;
        int grid4 = 0;

        for (int i = 0; i < tiles.size(); i++) {
            if (closestToBottom <= tiles.get(i).y) {
                closestToBottom = tiles.get(i).y;
            }
        }
        
        //only move the shape down if the grid spaces on the bottom are not taken already
        if (Main.tileRows[0] + 1 < 35 && Main.tileRows[1] + 1 < 35 && Main.tileRows[2] + 1 < 35 && Main.tileRows[3] + 1 < 35) {
            grid1 = Main.grid[Main.tileRows[0] + 1][Main.tileColumns[0]];
            grid2 = Main.grid[Main.tileRows[1] + 1][Main.tileColumns[1]];
            grid3 = Main.grid[Main.tileRows[2] + 1][Main.tileColumns[2]];
            grid4 = Main.grid[Main.tileRows[3] + 1][Main.tileColumns[3]];
        }
        
        if (grid1 != 1 && grid2 != 1 && grid3 != 1 && grid4 != 1) {
            for (int i = 0; i < tiles.size(); i++) {
                //moving the shape down
                if(Main.tileRows[0] < 34 && Main.tileRows[1] < 34 && Main.tileRows[2] < 34 && Main.tileRows[3] < 34){
                //if (closestToBottom + this.HEIGHT < Main.HEIGHT) {
                    tiles.get(i).y += speed;
                }
                //representing the shape on the grid once its down
                if (closestToBottom + this.TILEHEIGHT >= Main.HEIGHT) {
                    active = false;
                    for (int j = 0; j < Main.tileRows.length; j++) {
                        Main.grid[Main.tileRows[j]][Main.tileColumns[j]] = 1;
                    }
                }
            }
            if (closestToBottom + this.TILEHEIGHT < Main.HEIGHT) {
                centerY += speed;
            }
        }
        else{
            active = false;
        }
    }
    
    public void moveRight(){
        int closestToEdge = 0;
        int col1 = 0;
        int col2 = 0;
        int col3 = 0;
        int col4 = 0;
        
        for (int i = 0; i < tiles.size(); i++) {
            if (closestToEdge <= tiles.get(i).x) {
                closestToEdge = tiles.get(i).x;
            }
        }
        
        //move only if no other shape occupies the space to the right of the current one
        if (Main.tileColumns[0] < 24 && Main.tileColumns[1] < 24 && Main.tileColumns[2] < 24 && Main.tileColumns[3] < 24) {
            col1 = Main.grid[Main.tileRows[0]][Main.tileColumns[0] + 1];
            col2 = Main.grid[Main.tileRows[1]][Main.tileColumns[1] + 1];
            col3 = Main.grid[Main.tileRows[2]][Main.tileColumns[2] + 1];
            col4 = Main.grid[Main.tileRows[3]][Main.tileColumns[3] + 1];
        }
        
        if (col1 != 1 && col2 != 1 && col3 != 1 && col4 != 1) {
            for (int i = 0; i < tiles.size(); i++) {
                if (Main.tileColumns[0] < 24 && Main.tileColumns[1] < 24 && Main.tileColumns[2] < 24 && Main.tileColumns[3] < 24) {
                    tiles.get(i).x += TILEWIDTH;
                }
            }
            if (closestToEdge + TILEWIDTH < Main.TRUERIGHTBORDER) {
                centerX += TILEWIDTH;
            }  
        }       
    }
    
    public void moveLeft(){
        int closestToEdge = 1;
        int col1 = 0;
        int col2 = 0;
        int col3 = 0;
        int col4 = 0;
        
        for (int i = 0; i < tiles.size(); i++) {
            if (closestToEdge >= tiles.get(i).x) {
                closestToEdge = tiles.get(i).x;
            }
        }
        
        //move only if no other shape occupies the space to the right of the current one
        if (Main.tileColumns[0] > 0 && Main.tileColumns[1] > 0 && Main.tileColumns[2] > 0 && Main.tileColumns[3] > 0) {
            col1 = Main.grid[Main.tileRows[0]][Main.tileColumns[0] - 1];
            col2 = Main.grid[Main.tileRows[1]][Main.tileColumns[1] - 1];
            col3 = Main.grid[Main.tileRows[2]][Main.tileColumns[2] - 1];
            col4 = Main.grid[Main.tileRows[3]][Main.tileColumns[3] - 1];
        }
        if (col1 != 1 && col2 != 1 && col3 != 1 && col4 != 1) {
            for (int i = 0; i < tiles.size(); i++) {
                if (closestToEdge > 0) {
                    tiles.get(i).x -= TILEWIDTH;
                }
            }
            if (closestToEdge > 0) {
                centerX -= TILEWIDTH;
            }
        } 
    }
    
    public void rotateShape(){
        int prevSpeed = speed;
        boolean occupied = false;
        speed = 0; //stop the shape from moving while rotating
        
        //store the coordinates of the lower left corners of each square
        int[] xCoords = new int[tiles.size()];
        int[] yCoords = new int[tiles.size()];
        int[] grids = new int[tiles.size()];
        //get x and y coordinates
        for (int i = 0; i < tiles.size(); i++) {
            xCoords[i] = tiles.get(i).x;
            yCoords[i] = tiles.get(i).y + TILEHEIGHT;
        }
        
        //rotate the shape around the specified center
        /*
            Rotation is done by rotating the lower left corner of each squar around the center of the shape.
            Rotating the lower left corner because it becomes the upper left after a 90 degree clockwise rotation.
            Rotation is done using the rotation matrix [cosx, -sinx] and multiplying each coordinate vector by the matrix.
                                                       [sinx,  cosx]
            First translate the shape to the origin, perform the rotation and then perform the reverse translation to put the shape back.
            The formula ends up being (x, y) * rotationMatrix = [(x - rotationPointXCoordinate)*cos(rotationAngle) + ( (y - rotationY) * -sin(rotAngle)) + rotationPointX, (x - rotationX)sin(rotationAngle) + (y - rotationPointY) * cos(rotationAngle) + rotationY]
        */
        
        
        //check if the rotated coordinates are occipied by another shape and if they are, don't allow the shape to rotate
        
        //getting the rotated x coordinates
        for (int i = 0; i < xCoords.length; i++) {
            xCoords[i] = (int)Math.round(-(yCoords[i] - centerY) + centerX);
        }
        //getting the rotated y coordinates
        for (int i = 0; i < yCoords.length; i++) {
            yCoords[i] = (int)Math.round(tiles.get(i).x - centerX + centerY);
        }
        //correct the rotate coordinate to what they should be in case of going out of bounds
        //check if any coordinates are outside of the game window and return them back
        int closestRight = getClosestToRight(xCoords);
        int closestLeft = getClosestToLeft(xCoords);
        int closestBottom = getClosestToBottom(yCoords);
        int closestTop = getClosestToTop(yCoords);
        
        //correct the right side
        if (closestRight + TILEWIDTH >= Main.TRUERIGHTBORDER) {
            correctRight(closestRight, Main.TRUERIGHTBORDER, xCoords);
        }
        //correct the left side
        if (closestLeft <= 0) {
            correctLeft(closestLeft, 0, xCoords);
        }
        //correct bottom
        if (closestBottom >= Main.HEIGHT) {
            correctBottom(closestBottom, Main.HEIGHT, yCoords);
        }
        //correct top
        if (closestTop < 0) {
            correctTop(closestTop, 0, yCoords);
        }
        //check for spaces occupied by other shapes
        int i = 0;
        while(i < tiles.size() && !occupied){
            grids[i] = Main.grid[yCoords[i]/20][xCoords[i]/20];
            if(grids[i] == 1){
                occupied = true;
            }
            i += 1;
        }
        
        //rotate
        if(!occupied ){
            for (int j = 0; j < tiles.size(); j++) {
                tiles.get(j).x = xCoords[j];
                tiles.get(j).y = yCoords[j];
            }
//        for (int i = 0; i < xCoords.length; i++) {
//            tiles.get(i).x = (int)Math.round(-(yCoords[i] - centerY) + centerX);
//            tiles.get(i).y = (int)Math.round(xCoords[i] - centerX + centerY);
//        }
                
            //let the shape move again
            speed = prevSpeed;
            rotate = false;//setting it to false here prevents multiple rotations per click
        }
        else{
            rotate = false;
            speed = prevSpeed;
        }
        
        
    }
        //NOTE: This returns the closest left side x coordinate to the right boundary, it does not return the ride side x coordinate (ie does not add WIDTH to the result)
    private int getClosestToRight(int[] coordinates){
        int closestToEdge = coordinates[0];//tracks the side of the shape that is closest to the right screen boundary
        //finds the side closest to the boundary
        for (int i = 0; i < coordinates.length; i++) {
            if (closestToEdge <= coordinates[i]) {
                closestToEdge = coordinates[i];
            }
        }
        return closestToEdge;
    }
    
    private int getClosestToLeft(int[] coordinates){
        //look at moveRight for explanation
        int closestToEdge = coordinates[0];
        
        for (int i = 0; i < coordinates.length; i++) {
            if (closestToEdge >= coordinates[i]) {
                closestToEdge = coordinates[i];
            }
        }
        return closestToEdge;
    }
    
    //NOTE: This returns the closest top y coordinate to the bottom, ie it does not add HEIGHT to the result
    private int getClosestToBottom(int[] coordinates){
        int closestToEdge = coordinates[0];
        
        for (int i = 0; i < coordinates.length; i++) {
            if (closestToEdge <= coordinates[i]) {
                closestToEdge = coordinates[i];
            }
        }
        return closestToEdge;
    }
    
    //moves the tiles to the left by how much the right most side of the shape crosses the boundary
    private void correctRight(int closest, int boundary, int[] coordinates){
        for (int i = 0; i < coordinates.length; i++) {
            coordinates[i] -= (closest + TILEWIDTH - boundary);
        }
        centerX -= (closest + TILEWIDTH - boundary);
    }
    
    //move the tiles to the right by how far the shape's left most side is outside of the boundary on the left
    private void correctLeft(int closest, int boundary, int[] coordinates){
        for (int i = 0; i < coordinates.length; i++) {
            coordinates[i] += Math.abs(boundary - closest);
        }
        centerX += Math.abs(boundary - closest);
    }
    //moves the tiles of the shape up by however much the lowest point of the shape crosses the bottom boundary
    private void correctBottom(int closest, int boundary, int[] coordinates){
        for (int i = 0; i < coordinates.length; i++) {
            coordinates[i] -= (closest + TILEHEIGHT - boundary);
        }
        centerY -= (closest + TILEHEIGHT - boundary);
    }
    
    private int getClosestToTop(int[] coordinates){
        int closestToEdge = coordinates[0];
        
        for (int i = 1; i < tiles.size(); i++) {
            if (closestToEdge >= coordinates[i]) {
                closestToEdge = coordinates[i];
            }
        }
        return closestToEdge;
    }
    

    //moves the tiles down by however much the top most tile crosses the boundary
    private void correctTop(int closest, int boundary, int[] coordinates){
        for (int i = 0; i < coordinates.length; i++) {
            coordinates[i] += Math.abs(boundary - closest);
        }
        centerY += Math.abs(boundary - closest);
    }
    
    
    
//  ==========GETTERS AND SETTERS==========

    public int getSpeed() {
        return speed;
    }

    public Color getColor() {
        return color;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
    

    public boolean isRotate() {
        return rotate;
    }

    public void setRotate(boolean rotate) {
        this.rotate = rotate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    
    @Override
    public String toString() {
        String info = "";
        info = fileName.substring(0,fileName.indexOf("S"));
        return info;
    }
    
    
}
