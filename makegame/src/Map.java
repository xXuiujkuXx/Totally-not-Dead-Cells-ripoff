import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Map extends JPanel {
    private int[][] tileMap;
    private TilesetLoader tilesetLoader;
    private int tileSize = 32;
    private int mapWidth = 24;
    private int mapHeight = 19;

    public Map() {
        tilesetLoader = new TilesetLoader();
        loadMap();
    }
    
    public int[][] getTileMap() {
        return tileMap;
    }
    
    private void loadMap() {
        tileMap = new int[mapHeight][mapWidth];

        File file = new File("D:\\test python\\Java\\makegame\\src\\imag/corrected_tileset_map.csv");
        if (!file.exists()) {
            System.err.println("ERROR: CSV file not found. fuck you bro, it is there");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int row = 0;
            while ((line = br.readLine()) != null && row < mapHeight) {
                String[] tiles = line.split(",");
                for (int col = 0; col < tiles.length && col < mapWidth; col++) {
                    tileMap[row][col] = Integer.parseInt(tiles[col].trim());
                }
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int changey = -84;
        for (int row = 0; row < mapHeight; row++) {
            for (int col = 0; col < mapWidth; col++) {
                BufferedImage tile = tilesetLoader.getTile(tileMap[row][col]);
                if (tile != null) {
                    g.drawImage(tile, col * tileSize, row * tileSize + changey, tileSize, tileSize, null);
                }
            }
        }
    }
}