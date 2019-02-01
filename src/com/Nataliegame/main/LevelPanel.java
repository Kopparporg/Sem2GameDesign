
package com.Nataliegame.main;

import com.jamesgames.state.LevelManager;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class LevelPanel extends JPanel implements KeyListener {
    
// Refactored - changed name to reflect that this is the size of the panel
public static final int PANEL_WIDTH = Game.WINDOW_WIDTH - 10;
public static final int PANEL_HEIGHT = Game.WINDOW_HEIGHT - 10;

private final double FPS = 60; // Number of frames per second we want to generate
private final double TARGET_UPDATE_TIME = 1000/FPS; // Number of milliseconds to elapse between updates

private BufferedImage screenBuffer; // Off screen image - draw into this and copy to the screen
private Graphics2D graphics; // Graphics context for drawing into the off screen image
private Thread gameLoop = null; // Thread - this allows us to run the game level ina separate thread

//Game States
private boolean isRunning;
private boolean isPaused;

// Level manager object
private LevelManager lm;

public LevelPanel()
{
    super();

    initPanel();

    //default states
    isRunning = true;
    isPaused = false;

    lm = new LevelManager();
 
}
public void startGame()
{
    initGraphics();

    gameLoop = new Thread()
    {
        @Override
        public void run()
        {
            gameLoop();
        }
    };
gameLoop.start();
}

private void gameLoop()
{
    double startTime;
    double finishTime;
    double deltaT;
    double waitT;
    double fps;
    
    while(isRunning) 
    {
        startTime = System.nanoTime();
        
        if(!isPaused)
        {
            lm.update();
            lm.updateScreenBuffer(graphics);
            
            repaint();
            
            finishTime = System.nanoTime();
            
            deltaT = finishTime - startTime;
            
            waitT = TARGET_UPDATE_TIME - deltaT / 1000000;
            
            if(waitT < 5)
                waitT = 5;
            
            try
            {
                Thread.sleep((long)waitT);
            }catch(InterruptedException e)
            {
                e.printStackTrace();
            }
            
            
        }
    }
}
}

