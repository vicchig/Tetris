package TetrisMain;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Timer;


public class GameMenuManager {
    public boolean runMenu, runScore, runCredits, runEndGame, runControl, played, played2, getName, addNewScore, sort;
    public boolean playMenuMusic = true;
    private final Color grey1;
    private final Color grey2;
    private final Button playButton, creditsButton, quitButton, controlButton, endQuitButton, scoreButton, backButton1;// backButton2, backButton3; 
    private ArrayList<String>scores, tempScores;
    private int numberOfLines, removeIndex;
    private String[] scoresToDisplay, tempScoresArray;
    private final CustomComparator compare;
    private final FileManager fileManager= new FileManager();
    private final int width, height;
    private final AudioClip menuButtonSound, menuMusic, gameMusic;
    private final Font arial20, arial30, arial45, arial60;
    
    public GameMenuManager(int width, int height){
        scores = new ArrayList();
        runMenu = true;
        played = false;
        grey1 = new Color(168,162,162);
        grey2 = new Color(222,218,218);
        arial20 = new Font("Arial", Font.PLAIN, 20);
        arial30 = new Font("Arial", Font.PLAIN, 30);
        arial45 = new Font("Arial", Font.PLAIN, 45);
        arial60 = new Font("Arial", Font.PLAIN, 60);

        playButton = new Button(width/2, 300, "Play", grey1, grey2, arial30);
        scoreButton = new Button(width/2, 375, "Highscores", grey1, grey2, arial30);
        quitButton = new Button(width/2, 600, "Quit", grey1, grey2, arial30);
        endQuitButton = new Button(width/2, 450, "Quit", grey1, grey2, arial30);
        creditsButton = new Button(width/2, 525, "Credits", grey1, grey2, arial30);
        controlButton = new Button(width/2, 450, "Controls", grey1, grey2, arial30);
        backButton1 = new Button(width/2, 600, "Back", grey1, grey2, arial30);
        
        played2 = false;
        getName = false;
        numberOfLines = 0;
        tempScores = new ArrayList();
        addNewScore = false;
        scoresToDisplay = new String[5];
        tempScoresArray = new String[5]; 
        compare = new CustomComparator();
          
        menuButtonSound = Applet.newAudioClip(this.getClass().getResource("/sounds/menuButtonPress.wav"));
        menuMusic = Applet.newAudioClip(this.getClass().getResource("/sounds/Analog-Nostalgia.wav"));
        gameMusic = Applet.newAudioClip(this.getClass().getResource("/sounds/Hypnotic-Puzzle3.wav"));
        
        this.width = width;
        this.height = height;
    }
    
    public void runCredits(int mx, int my){
        if(backButton1.mouseCollision(mx, my)){
            //changeColorBack1 = true;
            backButton1.setChangeColor(true);
            if (Main.mousePressed) {
                menuButtonSound.play();
                runMenu = true;
                runCredits = false;
                Main.mousePressed = false;
            }
        }
        else{
            backButton1.setChangeColor(false);
            //changeColorBack1 = false;
        }
    }
    
    public void displayCredits(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);

        backButton1.paint(g, width/2 - 20);
        g.setColor(Color.WHITE);
        g.setFont(arial45);
        g.drawString("Credits", width/2-80,80);
        g.setFont(arial30);

        g.setColor(Color.WHITE);
        g.drawString("Music: ", 30, 140);
        g.drawString("Art: ", 30, 250);
        g.setFont(arial20);
        g.drawString("Analog-Nostalgia by Eric Matyas (www.soundimage.org)", 30, 160);
        g.drawString("Hypnotic-Puzzle3 by Eric Matyas (www.soundimage.org)", 30, 180);
        g.drawString("All other sound effects were created using ChipTone.", 30, 200);
        g.drawString("Tetrimino art (stated to be public domain) by Damian Yerrick from", 30, 270);
        g.drawString("Wikimedia Commons.", 30, 290);
    }
    
    
    
    public void runEndGame(int mx, int my){
        gameMusic.stop();
        getName = true;
        //THIS MUST BE HERE DO NOT REMOVE, OTHERWISE THE SCORES MESS UP, I FORGOT THE REASON WHY FOR NOW. ALSO DO NOT PUT THIS INTO RESET VARS TOOK ME 1.5 HOURS LAST TIME TO FIND THE MISTAKE, IF YOU PUT IT THERE IT DELETES WHILE RECORDING THE SCORE WHICH MESSES IT UP
        for (int i = 0; i < scores.size(); i++) {
            scores.remove(i);
        }

        if(endQuitButton.mouseCollision(mx, my)){
            endQuitButton.setChangeColor(true);
            if(Main.mousePressed){
                menuButtonSound.play();
                if(Main.name.trim().equals("")){
                    Main.name = "NoName";
                }
                recordScore(true);
                resetVars();
                playMenuMusic = true;
                runMenu = true;
                runScore = false;
                runEndGame = false;
                runControl = false;
                runCredits = false;
                getName = false;
                Main.mousePressed = false;
            }
        }
        else{
            endQuitButton.setChangeColor(false);
        }
    }
    
    public void runControls(int mx, int my){
        if(backButton1.mouseCollision(mx, my)){
            backButton1.setChangeColor(true);
            if (Main.mousePressed) {
                menuButtonSound.play();
                runMenu = true;
                Main.mousePressed = false;
                runControl = false;
            }
        }
        else{
            backButton1.setChangeColor(false);
        }
    }
    
    public void runMenu(int mx, int my, Shape shape){
        Main.name = "";
        if (playMenuMusic) {
            playMenuMusic = false;
            menuMusic.loop();
        }
        if(playButton.mouseCollision(mx, my)){
            playButton.setChangeColor(true);
            if(Main.mousePressed){
                menuButtonSound.play();
                runMenu = false;
                Main.mousePressed = false;
                menuMusic.stop();
                gameMusic.loop();
            }
        } 
        else{
            playButton.setChangeColor(false);
        }
        
        if(scoreButton.mouseCollision(mx, my)){
            scoreButton.setChangeColor(true);
            if(Main.mousePressed){
                menuButtonSound.play();
                runMenu = false;
                runScore = true;
                Main.mousePressed = false;
            }
        } 
        else{
            scoreButton.setChangeColor(false);
        }
        
        if(quitButton.mouseCollision(mx, my)){
            quitButton.setChangeColor(true);
            if(Main.mousePressed){
                menuButtonSound.play();
                System.exit(0);
            }
        } 
        else{
            quitButton.setChangeColor(false);
        }
        
        if(controlButton.mouseCollision(mx, my)){
            controlButton.setChangeColor(true);
            if (Main.mousePressed) {
                menuButtonSound.play();
                runMenu = false;
                Main.mousePressed = false;
                runControl = true;
            }
        }
        else{
            controlButton.setChangeColor(false);
        }
        if(creditsButton.mouseCollision(mx, my)){
            creditsButton.setChangeColor(true);
            if (Main.mousePressed) {
                menuButtonSound.play();
                runCredits = true;
                runMenu = false;
                Main.mousePressed = false;
            }
        }
        else{
            creditsButton.setChangeColor(false);
        }
    }
    
    public boolean mouseCollision(int x, int y, Polygon shape){
        Rectangle mouse = new Rectangle(x, y, 1, 1);
        
        if(shape.contains(mouse)){
            return true;
        }
        return false;
    }
    
    private void changeColor(Polygon shape, Graphics g, String text, int x, int y, Color color) {
        g.setColor(color);
        g.fillPolygon(shape);
        g.setColor(Color.BLACK);
        g.drawString(text, x, y);
    }
    
    public void runHighscores(int mx, int my){
        if(backButton1.mouseCollision(mx, my)){
            backButton1.setChangeColor(true);
            
            if(Main.mousePressed){
                menuButtonSound.play();
                runMenu = true;
                runScore = false;
                Main.mousePressed = false;
            }
        }  
        else{
            backButton1.setChangeColor(false);
        }
    }
    
    public void displayControls(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0,0,Main.WIDTH, Main.HEIGHT);
        backButton1.paint(g, width/2 - 20);
        g.setFont(arial60);
        g.setColor(Color.WHITE);
        g.drawString("Controls",width/2-110,100);
        g.setFont(arial45);
        g.setColor(Color.BLACK);
        
        
        g.setColor(Color.WHITE);
        g.setFont(arial30);
        g.drawString("P - ", 40, 150);
        g.drawString("L/R Arrow Keys - ", 40, 185);
        g.drawString("Up Arrow Key - ", 40, 220);
        g.drawString("Down Arrow Key - ", 40, 255);
        
        g.setFont(arial20);
        g.drawString(" pause the game", 80, 148);
        g.drawString(" move shape left and right", 260, 183);
        g.drawString(" rotate shape", 240, 218);
        g.drawString(" move shape down", 275, 253);
    }
    
    public void displayHighscores(Graphics g){
        int y = 200;
        int place = 1;

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
        backButton1.paint(g, width/2 - 20);
        g.setColor(Color.WHITE);
        g.setFont(arial60);
        g.drawString("HIGHSCORES",width/2-200,100);
        g.setFont(arial45);
        g.setColor(Color.BLACK);
        

        if (scores.isEmpty()) {
            sortScores();
        }
        g.setColor(Color.WHITE);
        g.setFont(arial30); 
        for(int i = 0;i<scores.size();i++){
            y+=50;
            g.drawString(place+"."+" "+scores.get(i),200,y);
            place++;
        }
        g.setFont(arial30);  
    }
    
    public void resetVars(){
        Main.grid = new int[Main.HEIGHT/Shape.TILEHEIGHT][Main.TRUERIGHTBORDER/Shape.TILEWIDTH];
        Main.nextShape = new Shape("greenSquare.png", -100, -100, Color.WHITE);
        Main.currentShape = new ShapeL("redSquare.png", 0, 0, Color.RED);
        Main.timer.cancel();
        Main.timer = new Timer();
        

        Main.name = "";


        Main.tileRows = new int[4];
        Main.tileColumns = new int[4];
        
        
        Main.stopMoving = false;
        Main.beginGame = true;
        Main.changeDelay = false;
        Main.pause = false;
        Main.setTimer = false;
        Main.generateShape = false;
        Main.mousePressed = false;


        Main.xOffset = 0;
        Main.level = 1;
        Main.nextLevelScore = 200;
        Main.score = 0;
        Main.initialDelay = 500; 
        
 
        Main.shapes = new ArrayList();
        Main.debrisList = new ArrayList();
        
        Main.currentShape = Main.generateShape();
        Main.nextShape = Main.generateShape();
    }
    
    public void recordScore(boolean append){
        fileManager.clearFile("Scores.txt");
        for (int i = 0; i < scores.size(); i++) {
            scores.remove(i);
        }
        if (append) {
            readScore();
        }
        append = false;
        determineNewScore(Main.score);
            try{
                File scoreFile = new File("Scores.txt");

                FileWriter writer = new FileWriter(scoreFile, append);
                PrintWriter printLine = new PrintWriter(writer);
                for (int i = 0; i < scores.size(); i++) {
                    printLine.println(scores.get(i));
                }

                printLine.close();
                writer.close();
            }
            catch(IOException e){}
    }
    
    public void determineNewScore(int currentScore){
        if (scores.size() == 5) {
            scores.add(Main.name+":"+Main.score);
            Collections.sort(scores, compare);
            scores.remove(5);
        }
        else{
            scores.add(Main.name+":"+Main.score);
            Collections.sort(scores, compare);
        }
    }
    
    public void readScore(){
        try{
            FileReader fr = new FileReader("Scores.txt");
            BufferedReader textReader = new BufferedReader(fr);
            numberOfLines = fileManager.countLines("Scores.txt");
            for(int i = 0;i<numberOfLines;i++){
                scores.add(textReader.readLine());
            }
            textReader.close();
        }
        catch(IOException e){
            System.out.printf(e.getMessage());

        }
    }
    
    public void sortScores(){
            readScore();
            Collections.sort(scores,compare);
    }
    
    public void displayMenu(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);
        g.setColor(grey1);
        g.setFont(arial60);
        g.drawString(Main.GAMENAME, width/2-65, 100);
        playButton.paint(g, width/2 - 25);
        scoreButton.paint(g, width/2 - 65);
        quitButton.paint(g, width/2 - 25);
        controlButton.paint(g, width/2 - 50);
        creditsButton.paint(g, width/2 - 45);
    }
    
    public void displayEndGame(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0,0, width, height);
        g.setColor(Color.YELLOW);
        g.setFont(arial30);
        g.drawString("GAME OVER",width/2-95,200);
        g.drawString("SCORE: "+Main.score,width/2-80,300);
        endQuitButton.paint(g, width/2 - 25);
        g.setColor(Color.WHITE);
        g.drawString("ENTER NAME:" + Main.name,100,400);
    }
}
