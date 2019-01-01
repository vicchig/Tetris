
/*
Main game class for handling game logic.
*/
package TetrisMain;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;


public class Main extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener{ //Canavas is just a blank rectangle onto which the game will be drawn and which will track user input
                                                      //Runnable is implemented so that the class could be passed to a thread in which it will be run. Runnable defines the single run method used by the thread.
//  =======JFRAME VARS=======    
    public static final int WIDTH = 700;
    public static final int HEIGHT = 700;
    public static final int TRUERIGHTBORDER = 500;
    public static final int SCALE = 2;
    public static final String GAMENAME = "Tetris";
    
    private final BufferStrategy strat; 
    private final JFrame mainFrame; //the main program window that will contain the Canvas on which the game is drawn and display it
    
//  =======GAME VARS=======
    private static final String GREEN = "greenSquare.png";
    private static final String RED = "redSquare.png";
    private static final String TURQ = "turqSquare.png";
    private static final String PURPLE = "purpSquare.png";
    private static final String YELLOW = "yellowSquare.png";
    private static final String BLUE = "blueSquare.png";
    private static final String ORANGE = "orangeSquare.png";
    
    
    public static final Random rand = new Random();
    public static final ArrayController aController = new ArrayController();
    private final Font UIFont = new Font("Times New Roman", 0, 20);
    private final Font generalFont = new Font("Times New Roman", 0, 50);
    private final Thread mainThread = new Thread();
    private final GameMenuManager gameMenu = new GameMenuManager(WIDTH, HEIGHT);
    public static Shape nextShape = new Shape(GREEN, -100, -100, Color.WHITE);
    public static Shape currentShape = new ShapeL(RED, 0, 0, Color.RED);
    public static Timer timer = new Timer();//timer for scheduling the movement of the current shape
    public static ArrayList<Shape> shapes = new ArrayList();//currently placed and inactive shapes
    public static ArrayList<Debris> debrisList = new ArrayList();


    public static String name = "";

    
    public static int[][] grid;//the game grid
    public static int[] tileRows = new int[4];//the rows that the tiles of the current shape occupy
    public static int[] tileColumns = new int[4];//the columns that the tiles of the current shape occupy
    
    
    private boolean gameRunning;
    private boolean rotate;
    public static boolean stopMoving = false;
    public static boolean beginGame = true;
    public static boolean changeDelay = false;
    public static boolean pause = false;
    public static boolean setTimer = false;
    public static boolean generateShape = false;
    public static boolean mousePressed = false;

    
    public static  int xOffset = 0;
    public static int level = 1;
    public static int nextLevelScore = 200;
    private static int mouseX;
    private static int mouseY;
    public static int score = 0;
    public static int initialDelay = 500; //the delay for the timer that moves the shape in milliseconds  
    
    private AudioClip rowDestruction = Applet.newAudioClip(this.getClass().getResource("/sounds/rowDestructionSound.wav"));
    
    public Main(){
//  =======JFRAME SETUP=======
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        
        mainFrame = new JFrame(GAMENAME);
        
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //specifies what should be done to the frame when the program is closed
        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(this); //adds this since this class is the canvas that the game will use
        mainFrame.add(this, BorderLayout.CENTER); //centers the frame
        mainFrame.pack();
        mainFrame.setResizable(true);
        mainFrame.setLocationRelativeTo(null); //no relative location for the frame, just centered on the screen
    
        mainFrame.setVisible(true);

//  =======CANVAS SETUP=======
        this.setBounds(0, 0, WIDTH, HEIGHT);
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.createBufferStrategy(2);
        strat = this.getBufferStrategy();
        this.requestFocus();
    }
    
    
    public void run(){ //contains code that should be run when the Thread is started. In this case it will be the game code, since the game is run in a thread.

//  ===========MAIN GAME LOOP ==============
        long previousTime = System.nanoTime();
        double nSecondsPerUpdate = (Math.pow(10.0, 9))/60.0;//number of nano seconds per update
        
        int updates = 0; //how many frame updates are currently happening
        int currentFrames = 0;
        
        long lastUpdate = System.currentTimeMillis();// time the game has been last updated
        double nanoPassed = 0; //amount of nano seconds passed since last update
        
        while (gameRunning){

            long currentTime = System.nanoTime();
            nanoPassed += (currentTime - previousTime)/nSecondsPerUpdate;
            previousTime = currentTime;
            boolean beginPaint = false;
            
            while(nanoPassed >= 1){
                updates ++;
                if(!pause){
                    actionPerformed();
                }
                
                nanoPassed -= 1;
                beginPaint = true;
            }
            if (beginPaint) {
                currentFrames ++;
                graphicsBuffer();
            }
             
            if (System.currentTimeMillis() - lastUpdate > 1000) {
                lastUpdate += 1000;
                currentFrames = 0;
                updates = 0;
            }
        }
    }
    
    public void startGame(){ //should contain all initial variables
        gameRunning = true;
        timer.schedule(new MoveTask(currentShape), 0, initialDelay);
        grid = new int[HEIGHT/currentShape.TILEHEIGHT][TRUERIGHTBORDER/currentShape.TILEWIDTH];
        Thread mainThread = new Thread(this);//the start method of the thread starts the thread and calls its run method
        mainThread.start(); 
        currentShape = generateShape();
        nextShape = generateShape();
    }
    
    public void stopGame(){
        gameRunning = false;
    }
    
    private void graphicsBuffer() {
        Graphics2D g = (Graphics2D) strat.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);
        paint(g);
        g.dispose();
        strat.show();
    }
    
    public void actionPerformed(){
        if (!gameMenu.runMenu && !gameMenu.runScore && !gameMenu.runEndGame && !gameMenu.runControl && !gameMenu.runCredits) {
            
    //      ===============RECORD THE ROWS AND TILES THE SHAPE OCCUPIES===============
            if (currentShape.isActive()) {
                //get the rows and columns that the shape occupies
                for (int i = 0; i < currentShape.tiles.size(); i++) {
                    //remember to add 1 to each of these values in order to get the actual row and column 
                    tileColumns[i] = currentShape.tiles.get(i).x/currentShape.TILEWIDTH;
                    tileRows[i] = currentShape.tiles.get(i).y/currentShape.TILEHEIGHT;
                }
            }
    //      ==========================================================================
        
    //      ===============SHAPE DESCENT WHEN VK_DOWN IS PRESSED===============  
            //check two grids(rows) down from the shape and if any are occupied, cancel out pressing VK_DOWN
            int grid1 = 0;
            int grid2 = 0;
            int grid3 = 0;
            int grid4 = 0;

            //only check the grid if there is enough space to move down    
            if(tileRows[0] < 33 && tileRows[1] < 33 && tileRows[2] < 33 && tileRows[3] < 33){
                grid1 = grid[tileRows[0] + 2][tileColumns[0]];
                grid2 = grid[tileRows[1] + 2][tileColumns[1]];
                grid3 = grid[tileRows[2] + 2][tileColumns[2]];
                grid4 = grid[tileRows[3] + 2][tileColumns[3]];
            }
        
    //      if spaces are occupied or the tile is about to hit the lower boundary stop moving the shape with VK_DOWN
    //      otherwise allow it to move
            if(grid1 == 1 || grid2 == 1 || grid3 == 1 || grid4 == 1){
                stopMoving = true;
            }
            else if(tileRows[0] >= 33 || tileRows[1] >= 33 || tileRows[2] >= 33 || tileRows[3] >= 33){
                stopMoving = true;
            }
            else{
                stopMoving = false;
            }
    //      ===================================================================

    //      ===============ROTATING THE SHAPE===============
            if(currentShape.isRotate()){
                currentShape.rotateShape();
            }
    //      ===================================================================

    //      ===============GENERATING A NEW SHAPE===============
            if (currentShape.isActive() == false) {
                //put the shape on the grid
                for (int i = 0; i < tileRows.length; i++) {
                    grid[tileRows[i]][tileColumns[i]] = 1;
                }
                //save the current shape
                if (!beginGame) {
                    Shape oldShape = new Shape(currentShape.fileName, -1000, -100, currentShape.getColor());
                    oldShape.tiles.remove(0);

                    for (int i = 0; i < tileRows.length; i++) {
                        oldShape.tiles.add(new GraphicsObject(oldShape.fileName, (tileColumns[i])*oldShape.TILEWIDTH, (tileRows[i])*oldShape.TILEHEIGHT));
                    }

                    shapes.add(oldShape);
                    for (int i = 0; i < currentShape.tiles.size(); i++) {
                        currentShape.tiles.remove(i);
                    }
                }
                xOffset = randomizeX(140, 560);
                for (int i = 0; i < nextShape.tiles.size(); i++) {
                    nextShape.tiles.get(i).x -= xOffset;
                    nextShape.tiles.get(i).y -= 220;
                }
                nextShape.centerX -= xOffset;
                nextShape.centerY -= 220;
                currentShape = nextShape;
                //need to update the columns and rows for the new shape, otherwise the old ones are used and the coordinate mess up,don't remove this
                for (int i = 0; i < currentShape.tiles.size(); i++) {
                    //remember to add 1 to each of these values in order to get the actual row and column 
                    tileColumns[i] = currentShape.tiles.get(i).x/currentShape.TILEWIDTH;
                    tileRows[i] = currentShape.tiles.get(i).y/currentShape.TILEHEIGHT;
                }
                nextShape = generateShape();
            }
            deleteRow(); //don't move this from here, otherwise the last shape will not have its tiles checked, this has to be after shape generation has finished
            nextLevel();
            if (changeDelay && initialDelay > 100) {
                changeDelay = false;
                initialDelay = 500 - level * 50 ; //500 is the initial value of the delay
                timer.cancel();
                timer = new Timer();
                timer.schedule(new MoveTask(currentShape), 0, initialDelay);
            }
    //      ===================================================================

    //      ===============DEBRIS MOVEMENT AND REMOVAL===============
            for (int i = 0; i < debrisList.size(); i++) {
                debrisList.get(i).move();
                if (!debrisList.get(i).isActive()) {
                    debrisList.remove(i);
                }
            }
    //      ===================================================================

    //      ===============END GAME===============
            if (isGameOver()) {
                gameMenu.runEndGame = true;
            }
            if(generateShape){
                timer.cancel();
                timer = new Timer();
                timer.schedule(new MoveTask(currentShape), 0, initialDelay);
                generateShape = false;
            }
            
    //      =================================================================== 
       }
        else if (gameMenu.runMenu) {
            timer.cancel();
            setTimer = true;
            gameMenu.runMenu(mouseX, mouseY, currentShape);
        }
        else if(gameMenu.runEndGame){
            gameMenu.runEndGame(mouseX, mouseY);
        }
        else if(gameMenu.runScore){
            gameMenu.runHighscores(mouseX, mouseY);
        }
        else if(gameMenu.runControl){
            gameMenu.runControls(mouseX, mouseY);
        }
        else if(gameMenu.runCredits){
            gameMenu.runCredits(mouseX, mouseY);
        }
    }
    
    public void paint(Graphics2D g){
        //=======SET UP=======
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(Color.BLACK);
        g.fillRect(0,0, WIDTH, HEIGHT);
        g.setColor(Color.WHITE);
        
        //vertical lines
        for(int i = 0; i < TRUERIGHTBORDER/20; i++){
            g.drawLine(currentShape.TILEWIDTH*i, 0, currentShape.TILEWIDTH*i, HEIGHT);
        }
        //gortizontal lines
        for (int i = 0; i < HEIGHT; i++) {
            g.drawLine(0, currentShape.TILEHEIGHT*i, TRUERIGHTBORDER, currentShape.TILEHEIGHT*i);
        }
        g.setColor(Color.RED);
        g.drawLine(TRUERIGHTBORDER, 0, TRUERIGHTBORDER, HEIGHT);
        g.setColor(Color.WHITE);
        
        //==============
        
        //=======SHAPES AND DEBRIS=======
        currentShape.paint(g);
        for (int i = 0; i < shapes.size(); i++) {
            shapes.get(i).paint(g);
        }
        
        for (int i = 0; i < debrisList.size(); i++) {
            debrisList.get(i).paint(g);
        }
        //==============
        
        //=======UI=======
        g.setFont(UIFont);
        g.setColor(Color.WHITE);
        g.drawString("Level: " + level, 510, 20);
        g.drawString("Score: " + score, 510, 60);
        g.drawString("Next Shape: ", 570,195);
        nextShape.paint(g);
        
        //==============
        
        //=======PAUSE DIPLAY=======
        if (pause) {
            g.setFont(generalFont);
            g.setColor(Color.RED);
            g.drawString("PAUSED", 200, 350);
        }
        //==============
        
        if (gameMenu.runMenu) {
            gameMenu.displayMenu(g);
        }
        else if(gameMenu.runScore){
            gameMenu.displayHighscores(g);
        }
        else if(gameMenu.runEndGame){
            gameMenu.displayEndGame(g);
        }
        else if (gameMenu.runControl) {
            gameMenu.displayControls(g);
        }
        else if(gameMenu.runCredits){
            gameMenu.displayCredits(g);
        }
    }
    
    public void nextLevel(){
        if (score >= nextLevelScore) {
            level ++;
            nextLevelScore += 100 * level;
            changeDelay = true;
        }
    }
    public static int randomizeX(int min, int max){
        int x = rand.nextInt(max - min + 1) + min;
        if (x % Shape.TILEWIDTH != 0) {
            x = x - (x % Shape.TILEWIDTH);
        }
        return x;
    }
    public static Shape generateShape(){
        int shapeIndex;
        int x = 600;
        int y = 220;
        Shape shape = null;

        aController.display2D(grid);
        System.out.println("");

        shapeIndex = rand.nextInt(7);
        if(beginGame){
            x = randomizeX(40, 460);
            y = 0;
        }

        if (shapeIndex == 0) {
            shape = new ShapeL(GREEN, x, y, Color.GREEN);
        }
        else if(shapeIndex == 1){
            shape = new ShapeReverseL(RED, x, y, Color.RED);
        }
        else if(shapeIndex == 2){
            shape = new ShapeLongLine(TURQ, x, y, Color.CYAN);
        }
        else if(shapeIndex == 3){
            shape = new ShapeT(PURPLE, x, y, new Color(160, 32, 240));
        }
        else if(shapeIndex == 4){
            shape = new ShapeZ(YELLOW, x, y, Color.YELLOW);
        }
        else if(shapeIndex == 5){
            shape = new ShapeReverseZ(BLUE, x, y, Color.BLUE);
        }
        else if(shapeIndex == 6){
            shape = new ShapeSquare(ORANGE, x, y, Color.ORANGE);
        }
        beginGame = false;
        generateShape = true;
        return shape;
    }
        
    public void deleteRow(){
        int row = findFilledRow();
        int[] coordinates = new int[4];
        
        if (row != -1) {
            score += level*(25*getFilledRows());
            //fill the row with 0s
            aController.insertRow(grid, 0, row);
            //delete the tiles in this row
            for (int i = 0; i < shapes.size(); i++) {
                for (int j = 0; j < shapes.get(i).tiles.size(); j++) {
                    coordinates[j] = shapes.get(i).tiles.get(j).y;
                }
                while(aController.contains(coordinates, row*20)){
                    for (int j = shapes.get(i).tiles.size() - 1; j >= 0; j--) {
                        if(coordinates[j] == row*shapes.get(i).TILEHEIGHT){
                            //////////////DEBRIS GENERATION/////////////////////////
                            for (double k = 0; k < Math.PI * 2; k += 0.1) {
                                debrisList.add(new Debris(shapes.get(i).tiles.get(j).x + 9, shapes.get(i).tiles.get(j).y + 9, shapes.get(i).getColor()));
                                debrisList.get(debrisList.size() - 1).speedX = Math.sin(k) * 2;
                                debrisList.get(debrisList.size() - 1).speedY = Math.cos(k) * 2;
                            }
                            ////////////////////////////////////////////////////////
                            shapes.get(i).tiles.remove(j);
                            coordinates[j] = 0;
                        }
                    }
                }
            }

            //shift each shape down
            aController.fillArray2D(grid, 0);
            for (int i = 0; i < shapes.size(); i++) {
                for (int j = 0; j < shapes.get(i).tiles.size(); j++) {
                    if (shapes.get(i).tiles.get(j).y/shapes.get(i).TILEHEIGHT <= row) {
                        shapes.get(i).tiles.get(j).y += shapes.get(i).TILEHEIGHT;
                    }
                }
            }
            //refill the grid
            for (int i = 0; i < shapes.size(); i++) {
                for (int j = 0; j < shapes.get(i).tiles.size(); j++) {
                    grid[(shapes.get(i).tiles.get(j).y)/shapes.get(i).TILEHEIGHT][(shapes.get(i).tiles.get(j).x)/shapes.get(i).TILEWIDTH] = 1;
                }
            }
            aController.display2D(grid);
            System.out.println("");
        }
    }
    //checks if the game is over by seeing if any tile in the top most row is filled
    public boolean isGameOver(){
        for (int i = 0; i < grid[0].length; i++) {
            if (grid[0][i] == 1) {
                return true;
            }
        }
        return false;
    }
    //returns the amount of currently fully filled rows (used for score calculation)
    public int getFilledRows(){
        int rows = 0;
        for (int i = 0; i < grid.length; i++) {
            int count = 0;
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 1) {
                    count += 1;
                }
            }
            if (count == TRUERIGHTBORDER/20) {
                rows += 1;
            }
        }
        return rows;
    }
    //return the number of the first filled row
    //if no row is completely filled, return -1
    public int findFilledRow(){
        for (int i = 0; i < grid.length; i++) {
            int j = 0;
            while(j < grid[i].length && grid[i][j] == 1){
                j += 1;
            }
            if (j == TRUERIGHTBORDER/20) {
                System.out.println("Row: " + i);
                rowDestruction.play();
                return i;
            }
        }
        return -1;
    }
    
    public static void main(String[] args){//starts the game, remember that the main method is the first one called when this class is accessed
        Main game = new Main();
        game.startGame();
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        int key = ke.getKeyCode();
        
        if(key == KeyEvent.VK_UP && currentShape.isActive()){
            currentShape.setRotate(rotate);
            rotate = false;
        }
        if (key == KeyEvent.VK_RIGHT && currentShape.isActive()) {
            currentShape.moveRight();
        }
        if (key == KeyEvent.VK_LEFT && currentShape.isActive()) {
            currentShape.moveLeft();
        }
        if (key == KeyEvent.VK_DOWN && currentShape.isActive() && !stopMoving) {
            currentShape.moveY();
        }
        if (key == KeyEvent.VK_P && !pause) {
            pause = !pause;
            timer.cancel();
        }
        else if (key == KeyEvent.VK_P && pause) {
            pause = !pause;
            timer = new Timer();
            timer.schedule(new MoveTask(currentShape), 0, initialDelay);
        }
        if (key == KeyEvent.VK_BACK_SPACE) {
            if (name.length() >= 1) {
                name = name.substring(0, name.length()-1);
            }
            
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        int key = ke.getKeyCode();
        if(key == KeyEvent.VK_UP && currentShape.isActive()){
            rotate =  true;
            //currentShape.setRotate(rotate);
        }
        if (key == KeyEvent.VK_RIGHT) {
        }
        if (key == KeyEvent.VK_BACK_SPACE) {
            
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        char c = ke.getKeyChar();
        
        if(gameMenu.getName == true){
            
            if(c != KeyEvent.CHAR_UNDEFINED && c!= KeyEvent.VK_BACK_SPACE
                    && name.length()<20){
                name = name+c;
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

    @Override
    public void mousePressed(MouseEvent me) {
        mousePressed = true;
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        mousePressed = false;
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        mouseX = me.getX();
        mouseY = me.getY();
    }

    @Override
    public void mouseDragged(MouseEvent me) {
    }

//    //a new timer task in order to move the currentShape at regular time intervals
//    public class Task extends TimerTask{
//        public void run(){
//            if (currentShape.isActive()) {
//               currentShape.moveY(); 
//            }
//        }
//    }
}


