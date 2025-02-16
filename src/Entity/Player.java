package Entity;

import static utilz.Constans.PlayerConstans.GetSpriteAmount;
import static utilz.Constans.PlayerConstans.*;
import static utilz.Constans.Directions.*;
import static utilz.Tool.*;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import utilz.LoadSave;

public class Player extends Entity {
	
	private BufferedImage[][] animation;
    private int aniTrick, aniIndex , aniSpeed = 10;
    private int player_Acotion =  Idle;
    private int playerDir = -1;
    private  boolean moving = false;
    private boolean attack = false;
    private boolean  left,up, right , down;
    private float playerSpeed = 2f;
    private int[][] lvlData;

	public Player(float x, float y ,int width , int height) {
		this(x,y , width , height, 0, 0);
	}
	
	public Player(float x, float y ,int width , int height ,int marginX, int marginY) {
		super(x,y , width , height, marginX, marginY);
		loadAnimation();
	}
	
	
	public void update() {
		updatePos();
		updateHitBox();
		updateAnimatetionTrick();
        setAnimation();
      
	}
	
	public void render(Graphics g) {
		g.drawImage(animation[player_Acotion][aniIndex], (int)x , (int)y ,256 , 256 , null);
		drawHitBox(g);
	}
	
	private void updatePos() {
		
		moving = false;
		if(!left && !right && !up && !down) {
			return;
		}
		
		float xSpeed = 0, ySpeed = 0;
		
		if(left && !right) {
			xSpeed = -playerSpeed;

		}else if(right && !left){
			xSpeed = playerSpeed;
		}
		
		if(up && !down) {
			ySpeed = -playerSpeed;
		}else if(down && !up){
			ySpeed = playerSpeed;
		}
		
		if(CanMove((x + marginX) + xSpeed , (y + marginY) + ySpeed , width , height , lvlData)) {
			this.x += xSpeed;
			this.y += ySpeed;
			moving = true;
		}
	}
	
	public void LoadlvData(int[][] lvlvData){
		this.lvlData =lvlvData;
	}
    

    
    private void updateAnimatetionTrick() {
		aniTrick++;
		if (aniTrick >= aniSpeed) {
			aniTrick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(player_Acotion)) {
				aniIndex  = 0;
				attack = false;
			}
		}
	}
    
    private void setAnimation() {
    	int startAnimation = player_Acotion;
    	
    	if (moving) {
    		player_Acotion = Walk;
    	}else {
    		player_Acotion = Idle;
    	}
    	
    	if(attack) {
    		player_Acotion = Attack01;
    	}
    	
    	if (startAnimation != player_Acotion) {
    		resetAntiTick();
    	}
    }
    
    private void resetAntiTick() {
    	aniTrick = 0;
    	aniIndex = 0;
    }
	
	private void loadAnimation() {
		
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.Player_ATLAS);

		animation =  new BufferedImage[7][9];
		
		for (int j = 0 ; j < animation.length; j++)
			for (int i = 0; i < animation[j].length ; i++ )
				animation[j][i] = img.getSubimage(i * 100,j * 100, 100, 100);

	}
	
	public void resetDirBoolean() {
		left = false;
		right = false;
		up = false;
		down = false;
	}
	
	public void setAttack(boolean attack) {
		this.attack = attack;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}
	
	
}
