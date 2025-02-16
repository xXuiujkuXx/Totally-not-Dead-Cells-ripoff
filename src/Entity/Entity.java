package Entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Entity {
	
	protected  float x,y;
	protected int width , height, marginX, marginY;
	protected Rectangle hitBox;
	
	public Entity(float x, float y ,int width , int height) {
		this(x,  y , width ,  height , 0,  0);
	}
	
	public Entity(float x, float y ,int width , int height ,int marginX, int marginY) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.marginX = marginX;
		this.marginY = marginY;
		inithitbox();
	}
	
	protected void drawHitBox(Graphics g) {
		g.setColor(Color.YELLOW);
		g.drawRect(hitBox.x, hitBox.y, hitBox.width, hitBox.height);
	}

	private void inithitbox() {
		hitBox = new Rectangle((int) (x + marginX),(int) (y + marginY) 
				, width , height);
		
	}
	
	public void updateHitBox() {
		hitBox.x = (int) (x + marginX);
		hitBox.y = (int) (y + marginY);
	}
	
	public Rectangle getHitBox() {
		return hitBox;
	}
}
