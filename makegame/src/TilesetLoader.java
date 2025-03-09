import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class TilesetLoader {
    private ArrayList<BufferedImage> tiles;
    private int tileWidth = 32, tileHeight = 32;
    private int tilesPerRow = 12, tilesPerCol = 8;

    public TilesetLoader() {
        tiles = new ArrayList<>();
        loadTileset();
    }

    private void loadTileset() {
        InputStream stream = getClass().getResourceAsStream("/imag/Tileset.png");

        if (stream == null) {
            System.err.println("ERROR: Tileset file not found in package.");
            return;
        }

        try {
            BufferedImage tileset = ImageIO.read(stream);
            for (int row = 0; row < tilesPerCol; row++) {
                for (int col = 0; col < tilesPerRow; col++) {
                    tiles.add(tileset.getSubimage(col * tileWidth, row * tileHeight, tileWidth, tileHeight));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getTile(int id) {
        if (id >= 0 && id < tiles.size()) {
            return tiles.get(id);
        }
        return null;
    }
}
