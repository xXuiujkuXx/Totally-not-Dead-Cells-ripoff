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
    private static final int JUMPING_FRAMES = 6; // dont forget to freeze at 3
    private boolean isAttack1 = false;
    private boolean queueAttack2 = false;
    private boolean isAttack2 = false;
    private boolean queueAttack3 = false;
    private boolean isAttack3 = false;
    private int playerX = 100;
    private int playerY = 250;
    private int drawWidth = 200;
    private int drawHeight = 200;
    private final int speed = 5;
    private Set<Integer> pressedKeys = new HashSet<>();
    private Timer animationTimer, movementTimer;
    private boolean resetFrame = true;
    private Map map;
    private Rectangle hitbox;
    // how tf do i extends 2 class
    private int tileSize = 32;
    private int mapWidth = 24;
    private int mapHeight = 19;
    // gravity here
    private boolean isJumping = false;
    private boolean isOnGround = false;
    private double velocityY = 0.0;
    private final double gravity = 0.6;
    private final double jumpStrength = -12;
    private final int floatOffset = tileSize / 2;
    public CharacterAnimation(Map map) {
        this.map = map;

        loadAnimation();
        animationTimer = new Timer(150, e -> nextFrame());
        animationTimer.start();

        movementTimer = new Timer(10, e -> movePlayer());
        movementTimer.start();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                pressedKeys.add(e.getKeyCode());
                updateTime();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                pressedKeys.remove(e.getKeyCode());
                updateTime();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (isAttack2) queueAttack3 = true;
                    else if (isAttack1) queueAttack2 = true;
                    else if (!isAttack1 && !isAttack2 && !isAttack3) {
                        isAttack1 = true;
                        curFrame = 0;
                        animationTimer.setDelay(80);
                    }
                }
            }
        });
        createHitbox();
        setFocusable(true);
    }
    public void createHitbox() {
        hitbox = new Rectangle((int) playerX, (int) playerY, drawWidth, drawHeight);
    }
    public void updateHitbox() {
        int newX = playerX;
        int newY = playerY + 80;
        int leftTile = Math.max(0, newX / 32);
        int rightTile = Math.min(23, (newX + hitbox.width - 102) / 32);
        int topTile = Math.max(0, newY / 32);
        int bottomTile = Math.min(18, (newY + hitbox.height) / 32);

        boolean collision = map.getTileMap()[topTile][leftTile] != -1 ||
                            map.getTileMap()[topTile][rightTile] != -1 ||
                            map.getTileMap()[bottomTile][leftTile] != -1 ||
                            map.getTileMap()[bottomTile][rightTile] != -1;
        if (!collision) {
            hitbox.x = newX;
            hitbox.y = newY;
        } else {
            playerX = hitbox.x;
            playerY = hitbox.y - 80;
        }
    }
    private void movePlayer() {
        if (!pressedKeys.isEmpty() && !isAttack1 && !isAttack2 && !isAttack3) {
            int newX = playerX;
            if (pressedKeys.contains(KeyEvent.VK_A)) {
                newX -= speed;
                faceLeft = true;
                faceRight = false;
            }
            if (pressedKeys.contains(KeyEvent.VK_D)) {
                newX += speed;
                faceLeft = false;
                faceRight = true;
            }
            if (!checkHorizontalCollision(newX)) {
                playerX = hitbox.x = newX;
            }
            if (pressedKeys.contains(KeyEvent.VK_W) && isOnGround) {
                isJumping = true;
                isOnGround = false;
                velocityY = jumpStrength;
                curFrame = 0;
            }
        }
        applyGravity();
        playerY = hitbox.y - 80;
        repaint();
    }

    
    private boolean checkHorizontalCollision(int newX) {
        int leftTile = Math.max(0, newX / tileSize);
        int rightTile = Math.min(mapWidth - 1, (newX + drawWidth - 102) / tileSize);
        int topTile = hitbox.y / tileSize;
        int bottomTile = (hitbox.y + hitbox.height) / tileSize;

        return map.getTileMap()[topTile][leftTile] != -1 ||
               map.getTileMap()[bottomTile][leftTile] != -1 ||
               map.getTileMap()[topTile][rightTile] != -1 ||
               map.getTileMap()[bottomTile][rightTile] != -1;
    }
    
    private void applyGravity() {
        if (!isOnGround) {
            velocityY += gravity;
        }
        double newHitboxY = hitbox.y + velocityY;
        int bottomTileY = Math.min(mapHeight - 1, (int) ((newHitboxY + hitbox.height) / tileSize));

        boolean checkGround = false;
        for (int xOffset = 0; xOffset < drawWidth - 102; xOffset += tileSize / 2) {
            int tileX = (playerX + xOffset) / tileSize;
            if (map.getTileMap()[bottomTileY][tileX] != -1) {
                checkGround = true;
                break;
            }
        }

        if (checkGround) {
            hitbox.y = (bottomTileY * tileSize) - hitbox.height - 1;
            isOnGround = true;
            isJumping = false;
            velocityY = 0.0;
        } else {
            hitbox.y = (int) newHitboxY;
            isOnGround = false;
        }
    }


    public Rectangle getHitbox() {
        return hitbox;
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
        } else if (isJumping) {
            animationTimer.setDelay(30);

            if (curFrame < 2) {
                curFrame++;
            } 
            else {
                curFrame = 2;
            }

        } else if (!isOnGround) {
            if (curFrame < JUMPING_FRAMES - 1) {
                curFrame++;
            } else {
                curFrame = 0;
            }
        } else {
            animationTimer.setDelay(150);

            if (moving) {
                curFrame = (curFrame + 1) % MOVING_FRAMES;
            } else {
                curFrame = (curFrame + 1) % IDLE_FRAMES;
            }
        }
        repaint();
    }

    private void updateTime() {
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

        if (isJumping) {
            curFrame = Math.min(curFrame, 2);
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        map.paintComponent(g);
        BufferedImage currentFrame;
        if (isJumping) {
            currentFrame = animations[8][curFrame];
        } else if (isAttack2) {
            currentFrame = animations[4][curFrame];
        } else if (isAttack3) {
            currentFrame = animations[5][curFrame];   
        } else if (moving) {
            currentFrame = animations[1][curFrame];
        } else if (isAttack1) {
            currentFrame = animations[3][curFrame];
        } else {
            currentFrame = knightIdle[curFrame];
        }
        int drawX = playerX;
        drawWidth = 200;

        if ((isAttack1 || isAttack2 || isAttack3 || isJumping) && !moving) {
            drawX -= 50;
        } else if (moving) {
            drawX -= 50;
        }

        if (faceLeft && moving) {
            drawX += 100;
        }

        if ((isAttack1 || isAttack2 || isAttack3 || isJumping) && !moving && faceLeft) {
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
            g.drawImage(currentFrame, drawX + 100, playerY, -drawWidth, drawHeight, null);
            updateHitbox();
            g.setColor(Color.PINK);
            g.drawRect(hitbox.x, hitbox.y, hitbox.width-100, hitbox.height-80); 

        } else {
            g.drawImage(currentFrame, drawX, playerY, drawWidth, drawHeight, null);
            updateHitbox();
            g.setColor(Color.PINK);
            g.drawRect(hitbox.x, hitbox.y, hitbox.width-100, hitbox.height-80); 
            
        }
    }
}