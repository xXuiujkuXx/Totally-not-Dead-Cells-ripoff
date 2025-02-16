package utilz;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import Main.Game;

public class LoadSave {
	
	public static final String Player_ATLAS = "Soldier/Soldier/Soldier.png";
	public static final String Level_ATLAS = "Map_Tile/outside_sprites.png";
	public static final String TestLevel_ATLAS = "Map_Tile/level_one_data.png";
	
	public static BufferedImage GetSpriteAtlas(String fileName) {
		BufferedImage img = null;
		InputStream is = LoadSave.class.getResourceAsStream("/"+fileName);
		
		try {
			img = ImageIO.read(is);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return img;
	}
	
	public static int[][] GetLevelData(){
		int[][] lvlData = new int[Game.Game_height][Game.Game_width];
		BufferedImage img = GetSpriteAtlas(TestLevel_ATLAS);
		
		for (int j= 0; j < img.getHeight() ; j++) {
			for (int i = 0 ; i< img.getWidth() ; i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getRed();
				if (value >= 48) {
					value = 0;
				}
				lvlData[j][i] = color.getRed();
			}
		}
		
		return lvlData;
	}
}
