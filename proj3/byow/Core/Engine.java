package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static final int maxWid = 6;  // 随机生成的room的最大宽度
    public static final int maxH = 6;  // 随机生成的room的最大高度
    public static final int minWid = 2;  // 随机生成的room的最小宽度
    public static final int minH = 2;  // 随机生成的room的最小高度

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        long seed = Integer.parseInt(input.substring(1, input.length() - 1));  // input的形式为 N##...#S,中间的部分是种子
        Random rand = new Random(seed);

        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        fill(finalWorldFrame, 0, 0, WIDTH, HEIGHT, Tileset.NOTHING);  // 初始化为全空的地图

        // 随机生成的房间数
        int roomNum = RandomUtils.uniform(rand, WIDTH * HEIGHT / (maxWid * maxH) / 3, WIDTH * HEIGHT / (maxWid * maxH) / 2);
        renderRooms(finalWorldFrame, WIDTH, HEIGHT, roomNum, rand);

        return finalWorldFrame;
    }

    // 把world中[x:x+wid][y:y+h]的部分填充为tileType
     public void fill(TETile[][] world, int x, int y, int wid, int h, TETile tileType) {
        for (int i = x; i < x + wid; i++) {
            for (int j = y; j < y + h; j++) {
                world[i][j] = tileType;
            }
        }
    }

    // 在world中随机生成roomNum个房间
    public void renderRooms(TETile[][] world, int wid, int h, int roomNum, Random rand) {
        for (int i = 0; i < roomNum; i++) {
            randRoom(world, wid, h, rand);
        }
    }

    public void randRoom(TETile[][] world, int wid, int h, Random rand) {
        int x, y, roomWid, roomH;
        while (true) {  // 循环直到生成了符合要求的房间
            roomWid = RandomUtils.uniform(rand, minWid, maxWid);
            roomH = RandomUtils.uniform(rand, minH, maxH);
            x = RandomUtils.uniform(rand, 0, WIDTH - roomWid);
            y = RandomUtils.uniform(rand, 0, HEIGHT - roomH);

            boolean flag = true;
            for (int i = x; i < x + roomWid && flag; i++) {
                for (int j = y; j < y + roomH; j++) {
                    if (!world[i][j].equals(Tileset.NOTHING)) {  // 和其他的房间重合了
                        flag = false;
                        break;
                    }
                }
            }

            // 如果符合要求，填充指定部分为房间的地板
            if (flag) {
                fill(world, x, y, roomWid, roomH, Tileset.FLOOR);
                break;
            }
        }
    }
}
