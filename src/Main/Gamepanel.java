package Main;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import static utilz.Constans.PlayerConstans.*;
import static utilz.Constans.Directions.*;

public class Gamepanel extends JPanel{

    private Game game;
    KeyHandler keyH = new KeyHandler(this); 
    MouseInputs mouseI = new  MouseInputs(this);
    
    public Gamepanel(Game game){
    	
    	this.game = game;
        this.setPreferredSize(new Dimension(game.Game_width , game.Game_height));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        
        this.addKeyListener(keyH);
        this.addMouseListener(mouseI);
        this.setFocusable(true);
    }
    
    public void updateGame() {
    	
    }
    
    public void paintComponent(Graphics g){
        
        super.paintComponent(g);
        
        game.render(g);

    }
    
    public Game getGame() {
    	return game;
    }
}
