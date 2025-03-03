import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.HashSet;
import java.util.Set;

public class CharacterAnimation extends JPanel {
    private boolean moving = false;
    private boolean faceRight = true;
    private boolean faceLeft = false;
    private BufferedImage spriteSheet;
    private BufferedImage spriteSheetIdle;
    private BufferedImage[][] animations;
    private BufferedImage[] knightIdle;
    private int curFrame = 0;
    private static final int MOVING_FRAMES = 7;
    private static final int IDLE_FRAMES = 4;
    private static final int ATTACK1_FRAMES = 5;
    private static final int ATTACK2_FRAMES = 4;
    private static final int ATTACK3_FRAMES = 4;
    private boolean isAttack1 = false;
    private boolean queueAttack2 = false;
    private boolean isAttack2 = false;
    private boolean queueAttack3 = false;
    private boolean isAttack3 = false;
    private int playerX = 100;
    private int playerY = 100;
    private final int speed = 5;
    private Set<Integer> pressedKeys = new HashSet<>();
    private Timer animationTimer, movementTimer;
    private boolean resetFrame = true;

    public CharacterAnimation() {
        loadAnimation();

        animationTimer = new Timer(150, e -> nextFrame());
        animationTimer.start();

        movementTimer = new Timer(0, e -> movePlayer());
        movementTimer.start();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                pressedKeys.add(e.getKeyCode());
                updateMovementState();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                pressedKeys.remove(e.getKeyCode());
                updateMovementState();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (isAttack2) {  
                        queueAttack3 = true;
                    } else if (isAttack1) {  
                        queueAttack2 = true;
                    } else if (!isAttack1 && !isAttack2 && !isAttack3) {  
                        isAttack1 = true;
                        curFrame = 0;
                        animationTimer.setDelay(80);
                    }
                }
            }
        });

        setFocusable(true);
    }

    private void loadAnimation() {
        try {
            spriteSheet = ImageIO.read(getClass().getResourceAsStream("/imag/knight.png"));
            spriteSheetIdle = ImageIO.read(getClass().getResourceAsStream("/imag/Idle.png"));

            int rows = 11;
            int cols = 8;
            int spriteWidth = 128;
            int spriteHeight = 128;
            animations = new BufferedImage[rows - 1][cols];

            for (int j = 0; j < rows - 1; j++) {
                for (int i = 0; i < cols; i++) {
                    animations[j][i] = spriteSheet.getSubimage(i * spriteWidth, j * spriteHeight, spriteWidth, spriteHeight);
                }
            }

            int idleCols = 4;
            knightIdle = new BufferedImage[idleCols];

            for (int i = 0; i < idleCols; i++) {
                knightIdle[i] = spriteSheetIdle.getSubimage(i * spriteWidth, 0, spriteWidth, spriteHeight);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void nextFrame() {
        if (isAttack1) {
            curFrame++;
            if (curFrame >= ATTACK1_FRAMES) {
                isAttack1 = false;
                curFrame = 0;
                animationTimer.setDelay(150);
                if (queueAttack2) {  
                    isAttack2 = true;
                    queueAttack2 = false;
                    curFrame = 0;
                    animationTimer.setDelay(80);
                }
            }
        } else if (isAttack2) {
            curFrame++;
            if (curFrame >= ATTACK2_FRAMES) {
                isAttack2 = false;
                curFrame = 0;
                animationTimer.setDelay(150);
                if (queueAttack3) {  
                    isAttack3 = true;
                    queueAttack3 = false;
                    curFrame = 0;
                    animationTimer.setDelay(80);
                }
            }
        } else if (isAttack3) {
            curFrame++;
            if (curFrame >= ATTACK3_FRAMES) {
                isAttack3 = false;
                curFrame = 0;
                animationTimer.setDelay(150);
            }
        } else if (moving) {
            curFrame = (curFrame + 1) % MOVING_FRAMES;
        } else {
            curFrame = (curFrame + 1) % IDLE_FRAMES;
        }
        repaint();
    }

    private void movePlayer() {
        if (!pressedKeys.isEmpty() && !isAttack1 && !isAttack2 && !isAttack3) {  // Ignore movement if attacking
            if (pressedKeys.contains(KeyEvent.VK_W)) playerY -= speed;
            if (pressedKeys.contains(KeyEvent.VK_S)) playerY += speed;
            if (pressedKeys.contains(KeyEvent.VK_A)) {
                playerX -= speed;
                faceLeft = true;
                faceRight = false;
            }
            if (pressedKeys.contains(KeyEvent.VK_D)) {
                playerX += speed;
                faceLeft = false;
                faceRight = true;
            }
            repaint();
        }
    }

    private void updateMovementState() {
        if (!isAttack1 && !isAttack2 && !isAttack3) {
            if (!pressedKeys.isEmpty()) {
                if (resetFrame) {
                    curFrame = 0;
                    resetFrame = false;
                }
                moving = true;
                animationTimer.setDelay(80);
            } else {
                moving = false;
                animationTimer.setDelay(150);
                resetFrame = true;
            }
        } else {
            moving = false;
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);

        BufferedImage currentFrame;
        if (isAttack1) {
            currentFrame = animations[3][curFrame];
        } else if (isAttack2) {
            currentFrame = animations[4][curFrame];
        } else if (isAttack3) {
            currentFrame = animations[5][curFrame];   
        } else if (moving) {
            currentFrame = animations[1][curFrame];
        } else {
            currentFrame = knightIdle[curFrame];
        }

        int drawX = playerX;
        int drawWidth = 200;

        if ((isAttack1 || isAttack2 || isAttack3) && !moving) {
            drawX -= 50;
        } else if (moving) {
            drawX -= 50;
        }

        if (faceLeft && moving) {
            drawX += 100;
        }

        if ((isAttack1 || isAttack2 || isAttack3) && !moving && faceLeft) {
            drawX += 100;
        }

        if (isAttack2 && currentFrame.getWidth() > 20) {
            currentFrame = currentFrame.getSubimage(20, 0, currentFrame.getWidth() - 20, currentFrame.getHeight());
            drawWidth = 180;
            if (faceLeft) {
                drawX -= 20;
            } else {
                drawX += 20;
            }
        }

        if (faceLeft) {
            g.drawImage(currentFrame, drawX + 100, playerY, -drawWidth, 200, null);
        } else {
            g.drawImage(currentFrame, drawX, playerY, drawWidth, 200, null);
        }
    }



    public static void main(String[] args) {
        JFrame frame = new JFrame("Sprite Animation");
        CharacterAnimation panel = new CharacterAnimation();
        frame.add(panel);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
