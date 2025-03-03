import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class ArrowFollower extends JPanel implements MouseMotionListener {
    private BufferedImage gunImage;
    private double angle = 0;
    private int centerX, centerY;
    private double scale = 0.03;

    public ArrowFollower() {
        try {
            gunImage = ImageIO.read(getClass().getResource("/imag/desert_eagle.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        addMouseMotionListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        centerX = getWidth() / 2;
        centerY = getHeight() / 2;

        int imgWidth = (int) (gunImage.getWidth() * scale);
        int imgHeight = (int) (gunImage.getHeight() * scale);

        int rotationX = -20;
        int rotationY = imgHeight / 2;

        AffineTransform transform = new AffineTransform();
        transform.translate(centerX, centerY - rotationY);
        transform.rotate(angle, rotationX, rotationY);
        transform.scale(scale, scale);
        
        g2d.drawImage(gunImage, transform, this);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        
        angle = Math.atan2(mouseY - centerY, mouseX - centerX);
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Rotating Gun");
        ArrowFollower panel = new ArrowFollower();
        
        frame.add(panel);
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}