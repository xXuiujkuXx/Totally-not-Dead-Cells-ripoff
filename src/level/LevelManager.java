package level;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import Main.Game;
import utilz.LoadSave;

public class LevelManager {
	private Game game;
	private BufferedImage[] levelSprit;
	private Level levelOne;
	
	public LevelManager(Game game) {
		this.game = game;
		importOutSpirites();
		levelOne = new Level(LoadSave.GetLevelData());
	}
	
	private void importOutSpirites() {
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.Level_ATLAS);
		levelSprit = new BufferedImage[180];
		for (int j=0 ; j < 4; j++) {
			for (int i=0 ; i < 12; i++) {
				int index = j*12 + i;
				levelSprit[index] = img.getSubimage(i*32, j*32, 32, 32);
			}
		}
		
	}

	public void draw(Graphics g) {
		for (int j = 0; j < Game.Game_height ; j++)
			for (int i = 0 ; i < Game.Game_width ; i++) {
				int index = levelOne.getSprintIndex(i , j);
				g.drawImage(levelSprit[index], game.tileSize*i, game.tileSize*j, game.tileSize, game.tileSize, null);
				
			}
	}
	
	public void update() {
		
	}
	
	public Level getCurretLevel() {
		return levelOne;
	}
}
