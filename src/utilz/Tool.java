package utilz;

import Main.Game;

public class Tool {
	
	public static boolean CanMove(float x , float y , int width , int height , int[][] lvlData) {
		if(!IsBox(x , y, lvlData)) {
			if(!IsBox(x+width , y+width , lvlData)) {
				if(!IsBox(x + width, y , lvlData)) {
					if (!IsBox(x , y + height , lvlData)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private static boolean IsBox(float x , float y , int[][] lvlData) {
		if(x < 0 || x >= Game.Game_width) {
			return true;
		}
		
		if (y < 0 || y >= Game.Game_height) {
			return true;
		}
		
		float xIndex = x/Game.tileSize;
		float yIndex = y/Game.tileSize;
		
		int value = lvlData[(int)yIndex][(int)xIndex];
		
		if (value >=48 || value < 0 || value != 11) {
			return true;
		}else {
			return false;
		}
		
	}
}
