import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Map extends JPanel {
    private int[][] tileMap;
    private TilesetLoader tilesetLoader;
    private int tileSize = 32;
    private int mapWidth = 24;
    private int mapHeight = 22;

    public Map() {
        tilesetLoader = new TilesetLoader();
        loadMap();
    }
    
    public int[][] getTileMap() {
        return tileMap;
    }
    
    private void loadMap() {
        tileMap = new int[mapHeight][mapWidth];

        try (InputStream inputStream = getClass().getResourceAsStream("/imag/corrected_tileset_map.csv")) {
            if (inputStream == null) {
                System.err.println("ERROR: CSV file not found in resources.");
                return;
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                int row = 0;
                while ((line = br.readLine()) != null && row < mapHeight) {
                    String[] tiles = line.split(",");
                    for (int col = 0; col < tiles.length && col < mapWidth; col++) {
                        tileMap[row][col] = Integer.parseInt(tiles[col].trim());
                    }
                    row++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int changey = -81;
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