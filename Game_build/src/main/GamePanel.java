package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import entity.Player;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {
	
	// Screen settings
	final int originalTitleSize = 16; //16x16 tile will be default size of characters etc.
	final int scale = 3; // change if want to re-scale later

	public final int tileSize = originalTitleSize * scale; // 48x48 tile
	public final int maxScreenCol = 16; // like 4x3 dim
	public final int maxScreenRow = 12;
	// game screen size - adjust by adjusting values above
	public final int screenWidth = tileSize * maxScreenCol; // 768 pixels 
	public final int screenHeigth = tileSize * maxScreenRow; // 576 pixels
	
	// World settings
	public final int maxWorldCol = 64;
	public final int maxWorldRow = 32;
	public final int worldWidth = tileSize * maxWorldCol;
	public final int worldHeight = tileSize * maxWorldRow;
	
	int FPS = 60;
	
	TileManager tileM = new TileManager(this);
	
	KeyHandler keyH = new KeyHandler();
	// keeps program running until you stop it
	// calls run()
	Thread gameThread;
	public Player player = new Player(this, keyH);
	
	// set player's default position
//	int playerX = 100;
//	int playerY = 100;
//	int playerSpeed = 4;
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeigth));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true); // True sets drawing to be done in off-screen painting buffer - improves rendering perf
		this.addKeyListener(keyH);
		this.setFocusable(true);
	}

	
	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	// create loop to keep game running
	@Override
	public void run() {
		
		// this is 1 second or 1 billion nanoseconds over FPS
		double drawInterval = 1_000_000_000/FPS; // draws screen every 0.01666 seconds
		double nextDrawTime = System.nanoTime() + drawInterval; // draw screen when time hits this
		
		// creates a loop for repainting time to set to a normal amount to update to it doesn't process 
		// millions of times a second
		while(gameThread != null) {
			
			// 1) update information like character position
			update();
			
			// 2) draw screen with updated info
			repaint();
			
			
			try {
				double remainingTime = nextDrawTime - System.nanoTime(); // finds remaining time
				// need to convert to miliseconds for Thread.sleep()
				remainingTime /= 1_000_000;
				
				if(remainingTime < 0) {
					remainingTime = 0; // set in case it took more than the time
				}
				
				Thread.sleep((long) remainingTime); // pauses game until sleep time is over
				
				nextDrawTime += drawInterval;
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	public void update() {
		
		player.update();
		
	}
	
	public void paintComponent(Graphics g) {
		
		// must be called every time it uses paintComponent
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		// must draw tiles before player
		tileM.draw(g2);
		
		player.draw(g2);
		g2.dispose(); // works without this but saves memory
		
	}

	
}
