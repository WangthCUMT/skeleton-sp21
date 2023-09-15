package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;

    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    /**
     * Draw a line on tile
     */
    public static void drawRow(TETile[][] tiles, Position p, TETile tile, int length){
        for (int dx = 0; dx < length; dx = dx + 1){
            tiles[p.x + dx][p.y] = tile;
        }
    }
    private static void addHexagonHelper(TETile[][] tiles, Position p, TETile tile,int b, int t){
        //Draw the first row
        Position startOfRow = p.shift(b,0); // Move p to the position you should start.
        drawRow(tiles,startOfRow,tile,t);
        // Draw remaing row recursively
        if (b > 0){
            Position nextP = p.shift(0, -1);
            addHexagonHelper(tiles,nextP,tile,b - 1, t + 2);
        }
        //Draw this row again reflection
        Position startOfReflection = startOfRow.shift(0, -(2 * b + 1));
        drawRow(tiles,startOfReflection,tile,t);
    }

    /**
     * Add a hexagons.
     */
    public static void addHexagon(TETile[][] tiles, Position p, int size, TETile t){
        if (size < 2) return;
        addHexagonHelper(tiles,p,t,size - 1,size);
    }
    /** Picks a RANDOM tile with a 33% change of being
     *  a wall, 33% chance of being a flower, and 33%
     *  chance of being empty space.
     */
    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(5);
        switch (tileNum) {
            case 0: return Tileset.GRASS;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.SAND;
            case 3: return Tileset.MOUNTAIN;
            case 4: return Tileset.TREE;
            default: return Tileset.NOTHING;
        }
    }
    /**
     * Adds a column of hexagons
     */
    public static void addHexColumn(TETile[][] tiles, Position p, int size, int num){
        if (num < 1) return;

        addHexagon(tiles,p,size,randomTile());
        if (num > 1){
            Position bottom = getBottom(p,size);
            addHexColumn(tiles,bottom,size,num - 1);
        }
    }
    private static Position getBottom(Position p, int n){
        return p.shift(0, -2*n);
    }
    private static Position getTopRight(Position p, int n){
        return p.shift(2*n - 1, n);
    }
    private static Position getBottomLeft(Position p, int n){
        return p.shift(2*n - 1, -n);
    }
    /**
     * Fills the given 2D array of tiles with Blank tiles.
     * @param tiles
     */
    public static void fillWithBlank(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }
    public static void drawWorld(TETile[][] tiles, Position p, int hexSize, int tessSize){
        fillWithBlank(tiles);
        addHexColumn(tiles,p ,hexSize,tessSize);
        // Expand up to the right
        for (int i = 1; i < tessSize; i++){
            p = getTopRight(p,hexSize);
            addHexColumn(tiles, p, hexSize, tessSize + i);
        }

        for (int i = tessSize - 2; i >= 0; i--){
            p = getBottomLeft(p,hexSize);
            addHexColumn(tiles, p, hexSize, tessSize + i);
        }
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        Position p = new Position(10,40);
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        drawWorld(world, p,3, 3);

        ter.renderFrame(world);
    }

    private static class Position {
        int x;
        int y;
        public Position(int x, int y){
            this.x = x;
            this.y = y;
        }
        public Position shift(int dx, int dy){
            return new Position(x + dx, y+ dy);
        }
    }
}
