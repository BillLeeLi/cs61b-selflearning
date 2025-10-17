package byow.lab12;
import afu.org.checkerframework.checker.oigj.qual.World;
import com.google.common.cache.CacheStats;
import edu.neu.ccs.filter.CaseActionFilter;
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
    private static final int LEN = 4;
    private static final int WIDTH = (2 * LEN - 1) * 5 + LEN - 1;
    private static final int HEIGHT = 10 * LEN;
    private static final Random rand = new Random();

    /*
    在指定的位置创建一个六边形
    tiles是地图，len是六边形边长，x和y指定六边形的位置，tileType是tile的类型
    x的取值范围0~4,y的取值范围是0~7
     */
    public static void addHexgon(TETile[][] tiles, int len, int x, int y, TETile tileType) {
        for (int i = 0; i < len; i++) {
            fill(tiles, x * (2 * len - 1) + len - 1 - i, len + 2 * i, y * len + i, tileType);
            fill(tiles, x * (2 * len - 1) + len - 1 - i, len + 2 * i, (y + 2) * len - i - 1, tileType);
        }
    }

    /*
    将tiles中下标为y的列，从下标x开始长度为len的一段填充为tileType
     */
    public static void fill(TETile[][] tiles, int x, int len, int y, TETile tileType) {
        for (int i = 0; i < len; i++) {
            tiles[x + i][y] = tileType;
        }
    }

    /*
    将tiles用tileType填充
     */
    public static void fillWorld(TETile[][] tiles, TETile tileType, int width, int height) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                tiles[i][j] = tileType;
            }
        }
    }

    /*
    随机生成一种tile
     */
    public static TETile nextTile() {
        int num = rand.nextInt(8);
        return switch (num) {
            case 0 -> Tileset.WATER;
            case 1 -> Tileset.TREE;
            case 2 -> Tileset.SAND;
            case 3 -> Tileset.MOUNTAIN;
            case 4 -> Tileset.GRASS;
            case 5 -> Tileset.WALL;
            case 6 -> Tileset.UNLOCKED_DOOR;
            case 7 -> Tileset.LOCKED_DOOR;
            default -> Tileset.NOTHING;
        };
    }

    /*
    随机填充整个world
     */
    public static void fillWorldRandom(TETile[][] tiles, int len) {
        for (int i = 0; i < 3; i++) {
            addHexgon(tiles, len, 0, 2 + 2 * i, nextTile());
        }
        for (int i = 0; i < 4; i++) {
            addHexgon(tiles, len, 1, 1 + 2 * i, nextTile());
        }
        for (int i = 0; i < 5; i++) {
            addHexgon(tiles, len, 2, 2 * i, nextTile());
        }
        for (int i = 0; i < 4; i++) {
            addHexgon(tiles, len, 3, 1 + 2 * i, nextTile());
        }
        for (int i = 0; i < 3; i++) {
            addHexgon(tiles, len, 4, 2 + 2 * i, nextTile());
        }
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] tiles = new TETile[WIDTH][HEIGHT];
        HexWorld.fillWorld(tiles, Tileset.NOTHING, WIDTH, HEIGHT);
//        HexWorld.addHexgon(tiles, LEN, 0, 0, Tileset.FLOWER);
//        HexWorld.addHexgon(tiles, LEN, 1, 7, Tileset.WALL);
//        HexWorld.addHexgon(tiles, LEN, 2, 0, Tileset.WATER);

        HexWorld.fillWorldRandom(tiles, LEN);
        ter.renderFrame(tiles);
    }
}
