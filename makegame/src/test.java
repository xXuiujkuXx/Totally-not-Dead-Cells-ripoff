import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class test extends JPanel {
    private BufferedImage spriteSheet;
    private BufferedImage[][] animations;
    private int curFrame = 0;
    private int animationRow = 0;
    private String curAni = "Idle";
    private long lastAttackTime;
    private boolean canCombo = false;
    private boolean isAttacking = false;
    private boolean playattack2 = false;
    private Timer animationTimer;
    private static final int IDLE_FRAMES = 6;
    private static final int ATTACK1_FRAMES = 6;
    private static final int ATTACK2_FRAMES = 6;
    private static final int MOVING_FRAMES = 8;
    private boolean isMoving = false;

    public test() {
        loadAnimation();
        setPreferredSize(new Dimension(400, 400));
        animationTimer = new Timer(50, e -> nextFrame());
        animationTimer.start();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (!isAttacking) {
                        startAttack1();
                    } else if (canCombo) {
                        playattack2 = true;
                    }
                }
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!isAttacking && (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_D)) {
                    if (!isMoving) {
                        curAni = "Moving";
                        animationRow = 1;
                        curFrame = 0;
                        isMoving = true;
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (!isAttacking && (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_D)) {
                    curAni = "Idle";
                    animationRow = 0;
                    curFrame = 0;
                    isMoving = false;
                    repaint();
                }
            }
        });
        setFocusable(true);
    }

    private void loadAnimation() {
        try {
            spriteSheet = ImageIO.read(getClass().getResourceAsStream("/imag/Soldier.png"));
            int rows = 7;
            int cols = 9;
            int spriteWidth = 100;
            int spriteHeight = 100;
            animations = new BufferedImage[rows][cols];
            for (int j = 0; j < rows; j++) {
                for (int i = 0; i < cols; i++) {
                    animations[j][i] = spriteSheet.getSubimage(i * spriteWidth, j * spriteHeight, spriteWidth, spriteHeight);
                }
            }
        } catch (IOException e) {
        }
    }

    private void startAttack1() {
        curAni = "Attack1";
        animationRow = 2;
        curFrame = 0;
        isAttacking = true;
        canCombo = true;
        playattack2 = false;
        lastAttackTime = System.currentTimeMillis();
        repaint();
    }

    private void startAttack2() {
        curAni = "Attack2";
        animationRow = 3;
        curFrame = 0;
        isAttacking = true;
        canCombo = false;
        playattack2 = false;
        repaint();
    }

    private void nextFrame() {
        int maxFrames;
        if (curAni.equals("Idle")) {
            maxFrames = IDLE_FRAMES;
        } else if (curAni.equals("Attack1")) {
            maxFrames = ATTACK1_FRAMES;
        } else if (curAni.equals("Attack2")) {
            maxFrames = ATTACK2_FRAMES;
        } else if (curAni.equals("Moving")) {
            maxFrames = MOVING_FRAMES;
        } else {
            maxFrames = animations[animationRow].length;
        }
        curFrame++;
        if (curAni.equals("Moving") && curFrame >= maxFrames) {
            curFrame = 0;
        } else if (curFrame >= maxFrames) {
            if (curAni.equals("Attack1")) {
                if (playattack2) {
                    startAttack2();
                } else {
                    isAttacking = false;
                    canCombo = false;
                    if (isMoving) {
                        curAni = "Moving";
                        animationRow = 1;
                    } else {
                        curAni = "Idle";
                        animationRow = 0;
                    }
                }
            } else if (curAni.equals("Attack2")) {
                isAttacking = false;
                if (isMoving) {
                    curAni = "Moving";
                    animationRow = 1;
                } else {
                    curAni = "Idle";
                    animationRow = 0;
                }
            }
            curFrame = 0;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.drawString("Current Animation: " + curAni, 50, 50);
        g.drawImage(animations[animationRow][curFrame], 100, 100, 200,200,null);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Character Animation");
        test animationPanel = new test();
        frame.add(animationPanel);
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
