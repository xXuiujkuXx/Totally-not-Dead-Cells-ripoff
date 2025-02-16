package Main;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import static utilz.Constans.Directions.*;

public class KeyHandler implements KeyListener{
    
    private Gamepanel gamepanel;
    
    public KeyHandler(Gamepanel gamepanel) {
    	this.gamepanel = gamepanel;
    }
    @Override
    public void keyReleased(KeyEvent e){
        int code = e.getKeyCode();
        
        switch (code) {
			case KeyEvent.VK_W: 
				gamepanel.getGame().getPlayer().setUp(false);
				break;
			case KeyEvent.VK_A:
				gamepanel.getGame().getPlayer().setLeft(false);
				break;
			case KeyEvent.VK_D:
				gamepanel.getGame().getPlayer().setRight(false);
				break;
			case KeyEvent.VK_S:
				gamepanel.getGame().getPlayer().setDown(false);
				break;
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e){
        int code = e.getKeyCode();
        
        switch (code) {
			case KeyEvent.VK_W: 
				gamepanel.getGame().getPlayer().setUp(true);
				break;
			case KeyEvent.VK_A:
				gamepanel.getGame().getPlayer().setLeft(true);
				break;
			case KeyEvent.VK_D:
				gamepanel.getGame().getPlayer().setRight(true);
				break;
			case KeyEvent.VK_S:
				gamepanel.getGame().getPlayer().setDown(true);
				break;
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }
}
