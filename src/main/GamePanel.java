package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import entity.Player;
import object.SuperObject;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable{
	//works as a game screen
	
	// SCREEN SETTINGS
	final int originalTileSize = 16; // 16x16 tile
	final int scale = 3; // as 16x16 is very small in current res so we make it 48x48 but actually creat the tiles 16x16
	
	public final int tileSize = originalTileSize * scale; // 48x48 tile
	public final int maxScreenCol = 16;
	public final int maxScreenRow = 12;
	public final int screenWidth = tileSize * maxScreenCol; // 768 pixels
	public final int screenHeight = tileSize * maxScreenRow; // 576 pixels 4:3 ratio
	
	// WORLD SETTINGS
	public final int maxWorldCol = 50;
	public final int maxWorldRow = 50;
//	public final int worldWidth = tileSize * maxWorldCol;
//	public final int worldHeight = tileSize * maxWorldRow;
	
	// FPS
	int FPS = 60;
	
	// SYSTEM
	TileManager tileM = new TileManager(this);
	KeyHandler keyH = new KeyHandler();
	Sound music = new Sound(); // 2 different sound classes so that they are handled differently and music doesn't stop
	Sound se = new Sound();
	public CollisionChecker cChecker = new CollisionChecker(this);
	public AssetSetter aSetter = new AssetSetter(this); 
	public UI ui = new UI(this);
	Thread gameThread;
	// ENTITY AND OBJECT
	public Player player = new Player(this, keyH);
	public SuperObject obj[] = new SuperObject[10];
	// this means we can displays up to 10 objects on the screen 
	// this not the number of objects that can be created
	
	// once a thread has started it keeps your program running until the thread has stopped
	// thread is used to repeat some process such as updating screen 60 times per second i.e 60fps
	//implements runnable for thread 
	
	// set players default position
	int playerX = 100;
	int playerY = 100;
	int playerSpeed = 4;
	
	public GamePanel() {
		
		this.setPreferredSize(new Dimension(screenWidth,screenHeight));
		// set the size of this class(JPanel)
		this.setBackground(Color.black); // background color
		this.setDoubleBuffered(true); // this improves games rendering performance
		// if set true all the drawing from this component will be done in an off screen painting buffer
		this.addKeyListener(keyH);
		this.setFocusable(true);
	}
	
	public void setupGame() {
		
		aSetter.setObject();
		
		playMusic(0);
	}
	
	public void startGameThread() {
		
		gameThread = new Thread(this); // this == gamePanel
		gameThread.start(); // this calls run method
	}
	
	// when we start this game thread it automatically calls this run method
	// game loop is created in this run method which is the core of the game
	@Override
	
// SLEEP METHOD GAME LOOP 
	
//	public void run() {
//		
//		double drawInterval = 1000000000/FPS; // 1b nanoseconds / 60 = 0.016s
//		double nextDrawTime = System.nanoTime() + drawInterval;
//		
//		// without this interval the computer does the operation so fast 
//		
//		while(gameThread != null) {
//			
////			long currentTime = System.nanoTime();
////			long currentTime2 = System.currentTimeMillis();
////			System.out.println(currentTime);
//			
//			
//			
//			// 1 UPDATE : update info such as char positions
//			update();
//			
//			// 2 DRAW : draw the screen with the updated info
//			repaint(); // this is how to call paintComponent
//			
//			try {
//				double remainingTime = nextDrawTime - System.nanoTime();
//				remainingTime /= 1000000; //sleep accepts time in ms
//				
//				if(remainingTime < 0) {
//					remainingTime = 0;
//				}
//				
//				Thread.sleep((long) remainingTime); // we make the thread sleep for the remaining time 
//				
//				nextDrawTime += drawInterval;
//				
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			} 
//		}
//	}
	
// DELTA / ACCUMULATOR GAME LOOP
	
	public void run() {
		
		double drawInterval = 1000000000/FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0;
		
		while(gameThread != null) {
			
			currentTime = System.nanoTime();
			
			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;
			
			if(delta >= 1) {
				update();
				repaint();
				delta--;
				drawCount++;
			}
			
			if(timer >= 1000000000) {
				System.out.println("FPS " + drawCount);
				drawCount = 0;
				timer = 0;
			}
		}
	}
	
	//upper left corner is (0,0)
	
	public void update() {
		
		player.update();
	}
	//graphics class has many functions to draw objects on screen
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g); // from JPanel
		
		Graphics2D g2 = (Graphics2D)g; // g2 has bit more functions 
		
		// TILE
		tileM.draw(g2); // tiles before player 
		
		// OBJECT
		for(int i = 0; i < obj.length; i++) {
			if(obj[i] != null) obj[i].draw(g2,this);
		} 
		
		// PLAYER
		player.draw(g2);
		
		// UI
		ui.draw(g2);
		
		g2.dispose(); // saves memory
	}
	public void playMusic(int i) {
		
		music.setFile(i);
		music.play();
		music.loop();
	}
	public void stopMusic() {
		
		music.stop();
	}
	public void playSE(int i) {
		
		se.setFile(i);
		se.play(); 
	}
	
}
