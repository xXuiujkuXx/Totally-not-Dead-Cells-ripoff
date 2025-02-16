package Main;

import java.awt.Graphics;

import Entity.Player;
import level.LevelManager;

public class Game implements Runnable{
	private GameWindow gameWindow;
	private Gamepanel gamePanel;
	Thread gameThread;
	private final int FPS = 60;
	private final int UPS = 200;
	
	private Player player;
	private LevelManager levelManager;
		
	public final static int originalTileSize = 32;
	public final static float scale = 1.0f;
	public final static int tileSize  = (int)(originalTileSize*scale);//32*32 tile 
	public final static int maxSeneCollum = 26;
	public final static int maxSeneRow = 14;
	public final static int weightScale = 6;
	
	public final static int Game_width = tileSize*maxSeneCollum;
	public final static int Game_height = tileSize*maxSeneRow;

	
	public Game() {
		initClasses();
		gamePanel = new Gamepanel(this);
		gameWindow = new GameWindow(gamePanel);
		gamePanel.requestFocus();
		StratGmaeThread();
	}
	
	private void initClasses() {
		levelManager = new LevelManager(this);
		player = new Player(50 ,100 , (int)(50*scale) , (int)(50*scale)
				, 100, 100);
		player.LoadlvData(levelManager.getCurretLevel().getLevelData());
		
	}

	public void StratGmaeThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }
    
    public void update(){
    	player.update();
    	levelManager.update();
    	gamePanel.updateGame();
    }
     public void render(Graphics g) {
    	 levelManager.draw(g);
    	 player.render(g);
     }

    @Override
    public void run(){
    	double timePerFrame = 1000000000.0/FPS;
    	double timePerUpdate = 1000000000.0/UPS;
    	
    	long previousTime = System.nanoTime();
    	
    	int  frame = 0;
    	int updates = 0;
    	
    	long lastCheck = System.currentTimeMillis();
    	
    	double deltaU = 0;
    	double deltaF = 0;
    	
    	while(true) {

    		long currentTime = System.nanoTime();
    		
    		deltaU += (currentTime - previousTime)/timePerUpdate;
    		deltaF += (currentTime - previousTime)/timePerFrame;
    		
    		previousTime = currentTime;
    		
    		if (deltaU >= 1) {
    			update();
    			updates++;
    			deltaU--;
    		}
    		if (deltaF >= 1) {
    			gamePanel.repaint();		
    			frame++;
    			deltaF--;
    		}
    		
//    		if(now - lastFrame >= timePerFrame) {
//    			this.repaint();
//    			lastFrame = now;
//    			frame++;
//    		}
    		
    		if (System.currentTimeMillis() - lastCheck >=1000) {
    			lastCheck = System.currentTimeMillis();
    			System.out.println("FPS :"+ frame + " UPS : "+ updates);
    			frame = 0;
    			updates = 0;
    		}
    		
    	}
    }
    
    public void  windowFocusLost() {
    	player.resetDirBoolean();
    }
    
    public Player getPlayer() {
    	return player;
    }
    
}
