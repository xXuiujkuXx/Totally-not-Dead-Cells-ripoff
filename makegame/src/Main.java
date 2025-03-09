import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("2D Platformer");
        Map gameMap = new Map();
        CharacterAnimation panel = new CharacterAnimation(gameMap);
        frame.add(panel);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}