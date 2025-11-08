package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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

    // Room类，记录一个房间的信息,包括它的大小和在地图中的位置
    public static class Room {
        int minX;
        int minY;
        int maxX;
        int maxY;

        Room(int ix, int iy, int w, int h) {
            minX = ix;
            minY = iy;
            maxX = ix + w - 1;
            maxY = iy + h - 1;
        }
    }

    // Edge类,用于计算最小生成树。一条边的两个端点都是Room
    // Edge类可以通过距离来进行比较
    public static class Edge implements Comparable<Edge> {
        Room r1;
        Room r2;
        int idx1;
        int idx2;
        int dis = -1;

        Edge(Room ir1, Room ir2, int i1, int i2) {
            r1 = ir1;
            r2 = ir2;
            idx1 = i1;
            idx2 = i2;
            dis = distance();
        }

        // 计算两个房间之间的曼哈顿距离
        public int distance() {
            if (dis >= 0) {
                return dis;
            }
            int dx = Math.max(r1.minX - r2.maxX, r2.minX - r1.maxX);
            int dy = Math.max(r1.minY - r2.maxY, r2.minY - r1.maxY);
            return Math.max(dx, 0) + Math.max(dy, 0);
        }

        @Override
        public int compareTo(Edge rhs) {
            if (dis < rhs.dis) {
                return -1;
            } else if (dis > rhs.dis) {
                return 1;
            }
            return 0;
        }
    }

    public static class EdgeComparator implements Comparator<Edge> {
        public int compare(Edge e1, Edge e2) {
            return e1.compareTo(e2);
        }
    }

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww"). The engine should
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
        Random rand = new Random();

        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        fill(finalWorldFrame, 0, 0, WIDTH, HEIGHT, Tileset.NOTHING);  // 初始化为全空的地图

        // 随机生成房间
        int roomNum = RandomUtils.uniform(rand, WIDTH * HEIGHT / (maxWid * maxH) / 3, WIDTH * HEIGHT / (maxWid * maxH) / 2);
        List<Room> rooms = renderRooms(finalWorldFrame, WIDTH, HEIGHT, roomNum, rand);
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < roomNum; i++) {
            for (int j = i + 1; j < roomNum; j++) {
                edges.add(new Edge(rooms.get(i), rooms.get(j), i, j));
            }
        }
        List<Edge> chosenEdges = kruskal(edges, roomNum);
        for (Edge e: chosenEdges) {
            addEgde(finalWorldFrame, e, rand);  // 用随机的方式添加边
        }

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

    // 在world中随机生成roomNum个房间,并返回这些房间的列表
    public List<Room> renderRooms(TETile[][] world, int wid, int h, int roomNum, Random rand) {
        List<Room> rooms = new ArrayList<>();
        for (int i = 0; i < roomNum; i++) {
            rooms.add(randRoom(world, wid, h, rand));
        }
        return rooms;
    }

    public Room randRoom(TETile[][] world, int wid, int h, Random rand) {
        int x, y, roomWid, roomH;
        while (true) {  // 循环直到生成了符合要求的房间
            roomWid = RandomUtils.uniform(rand, minWid, maxWid);
            roomH = RandomUtils.uniform(rand, minH, maxH);
            x = RandomUtils.uniform(rand, 0, wid - roomWid);  // 注意这个上界已经保证了生成的矩形不会延申到地图之外
            y = RandomUtils.uniform(rand, 0, h - roomH);

            boolean flag = true;
            for (int i = x; i < x + roomWid && flag; i++) {
                for (int j = y; j < y + roomH; j++) {
                    if (check(world, wid, h, i, j)) {  // 和其他的房间相邻了
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
        return new Room(x, y, roomWid, roomH);
    }

    // 检查world[x][y]有无相邻的非空块
    boolean check(TETile[][] world, int wid, int h, int x, int y) {
        int tx, ty;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                tx = x + i;
                ty = y + j;
                if (0 <= tx && tx < wid && 0 <= ty && ty < h && !world[tx][ty].equals(Tileset.NOTHING)) {
                    return true;
                }
            }
        }
        return false;
    }

    // n个顶点的kruskal算法,返回值是最小生成树中的边集
    List<Edge> kruskal(List<Edge> edges, int n) {
        UnionSet us = new UnionSet(edges.size());
        ArrayList<Edge> chosenEdge = new ArrayList<>();
        int cnt = 0, i = 0;  // 已经被选中的边的个数
        Edge e;
        edges.sort(new EdgeComparator());  // 按照边的距离排序
        while (cnt < n - 1) {
            e = edges.get(i++);
            if (!us.isConnected(e.idx1, e.idx2)) {  // 未连通,则选择这条边
                cnt++;
                chosenEdge.add(e);
                us.connect(e.idx1, e.idx2);
            }
        }
        return chosenEdge;
    }

    // 在world中添加边e
    public void addEgde(TETile[][] world, Edge e, Random rand) {
        int dx = Math.max(e.r1.minX - e.r2.maxX, e.r2.minX - e.r1.maxX);
        int dy = Math.max(e.r1.minY - e.r2.maxY, e.r2.minY - e.r1.maxY);
        if (dx <= 0) {  // 两个room在水平方向上有重叠,那么它们之间的最短路径是直线
            int x1 = Math.max(e.r1.minX, e.r2.minX);
            int x2 = Math.min(e.r1.maxX, e.r2.maxX);
            int x = RandomUtils.uniform(rand, x1, x2 + 1);
            for (int i = Math.min(e.r1.maxY, e.r2.maxY) + 1; i < Math.max(e.r1.minY, e.r2.minY); i++) {
                world[x][i] = Tileset.FLOOR;
            }
        } else if (dy <= 0) {  // 两个room在竖直方向上有重叠
            int y1 = Math.max(e.r1.minY, e.r2.minY);
            int y2 = Math.min(e.r1.maxY, e.r2.maxY);
            int y = RandomUtils.uniform(rand, y1, y2 + 1);
            for (int i = Math.min(e.r1.maxX, e.r2.maxX) + 1; i < Math.max(e.r1.minX, e.r2.minX); i++) {
                world[i][y] = Tileset.FLOOR;
            }
        } else {  // 在水平和竖直方向上都没有重叠
            if (e.r1.maxX < e.r2.minX && e.r1.maxY < e.r2.minY) {  // r1在r2左下角
                int x = RandomUtils.uniform(rand, e.r1.minX, e.r1.maxX + 1);
                int y = RandomUtils.uniform(rand, e.r2.minY, e.r2.maxY + 1);
                for (int j = e.r1.maxY + 1; j < y; j++) {
                    world[x][j] = Tileset.FLOOR;
                }
                for (int i = x; i < e.r2.minX; i++) {
                    world[i][y] = Tileset.FLOOR;
                }
            } else if (e.r1.maxX < e.r2.minX && e.r1.minY > e.r2.maxY) {
                int x = RandomUtils.uniform(rand, e.r2.minX, e.r2.maxX + 1);
                int y = RandomUtils.uniform(rand, e.r1.minY, e.r1.maxY + 1);
                for (int j = e.r2.maxY + 1; j < y; j++) {
                    world[x][j] = Tileset.FLOOR;
                }
                for (int i = e.r1.maxX + 1; i <= x; i++) {
                    world[i][y] = Tileset.FLOOR;
                }
            } else if (e.r1.minX > e.r2.maxX && e.r1.minY > e.r2.maxY) {
                int x = RandomUtils.uniform(rand, e.r1.minX, e.r1.maxX + 1);
                int y = RandomUtils.uniform(rand, e.r2.minY, e.r2.maxY + 1);
                for (int j = y; j < e.r1.minY; j++) {
                    world[x][j] = Tileset.FLOOR;
                }
                for (int i = e.r2.maxX + 1; i < x; i++) {
                    world[i][y] = Tileset.FLOOR;
                }
            } else {
                int x = RandomUtils.uniform(rand, e.r2.minX, e.r2.maxX + 1);
                int y = RandomUtils.uniform(rand, e.r1.minY, e.r1.maxY + 1);
                for (int j = y; j < e.r2.minY; j++) {
                    world[x][j] = Tileset.FLOOR;
                }
                for (int i = x; i < e.r1.minX; i++) {
                    world[i][y] = Tileset.FLOOR;
                }
            }
        }
    }
}
