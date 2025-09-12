package game2048;

import java.util.Formatter;
import java.util.Observable;


/**
 * The state of a game of 2048.
 *
 * @author TODO: YOUR NAME HERE
 */
public class Model extends Observable {
    /**
     * Current contents of the board.
     */
    private Board board;
    /**
     * Current score.
     */
    private int score;
    /**
     * Maximum score so far.  Updated when game ends.
     */
    private int maxScore;
    /**
     * True iff game is ended.
     */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /**
     * Largest piece value.
     */
    public static final int MAX_PIECE = 2048;

    /**
     * A new 2048 game on a board of size SIZE with no pieces
     * and score 0.
     */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /**
     * A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes.
     */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /**
     * Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     * 0 <= COL < size(). Returns null if there is no tile there.
     * Used for testing. Should be deprecated and removed.
     */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /**
     * Return the number of squares on one side of the board.
     * Used for testing. Should be deprecated and removed.
     */
    public int size() {
        return board.size();
    }

    /**
     * Return true iff the game is over (there are no moves, or
     * there is a tile with value 2048 on the board).
     */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /**
     * Return the current score.
     */
    public int score() {
        return score;
    }

    /**
     * Return the current maximum game score (updated at end of game).
     */
    public int maxScore() {
        return maxScore;
    }

    /**
     * Clear the board to empty and reset the score.
     */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /**
     * Add TILE to the board. There must be no Tile currently at the
     * same position.
     */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /**
     * Tilt the board toward SIDE. Return true iff this changes the board.
     * <p>
     * 1. If two Tile objects are adjacent in the direction of motion and have
     * the same value, they are merged into one Tile of twice the original
     * value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     * tilt. So each move, every tile will only ever be part of at most one
     * merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     * value, then the leading two tiles in the direction of motion merge,
     * and the trailing tile does not.
     */
    public boolean tilt(Side side) {
        boolean changed;
        changed = false;

        // TODO: Modify this.board (and perhaps this.score) to account
        board.setViewingPerspective(side);  // 把板子的方向一下，使得tiles移动方向总是NORTH

        // 以side=NORTH为例考虑解决这个问题的方法
        // 对每一列，求出有多少个不为空的块,把它们依次编号(0-based)。然后求出merge发生的次数和位置(用靠上的那个块的序号)
        // 比如某一列从上到下是 [4, 4, 2, 2], merge次数是2，发生位置是 [0, 2]
        // 那么每个块的最后位置是原始序号减去小于原序号的merge位置的个数
        // 比如上面的例子是 [0, 0, 1, 1] 0-0=0, 1-1=0, 2-1=1, 3-2=1
        int sz = board.size();
        for (int viewCol = 0; viewCol < sz; viewCol++) {
            int tileNum = 0, merge = 0;
            int[] tileRow = new int[sz];  // 记录每个不为空的tile的行号
            for (int viewRow = sz - 1; viewRow >= 0; viewRow--) {
                if (board.tile(viewCol, viewRow) != null) {
                    tileRow[tileNum++] = viewRow;
                }
            }
            int[] mergeIdx = new int[tileNum];  // 记录发生merge的tile的序号

            // 统计发生merge的信息，包括merge发生的位置
            for (int i = 0; i < tileNum; i++) {
                if (i < tileNum - 1 && board.tile(viewCol, tileRow[i]).value() == board.tile(viewCol, tileRow[i + 1]).value()) {
                    score += 2 * board.tile(viewCol, tileRow[i]).value();  // 计分
                    mergeIdx[merge++] = i;  // 表示第i个tile和第i+1个tile发生了merge
                    i++;
                }
            }

            // 确定每个tile最后移动到的位置，从上到下进行计算防止出错
            for (int idx = 0; idx < tileNum; idx++) {
                int prevMerge = 0;  // 记录上方发生的merge次数
                for (int i = 0; i < merge && mergeIdx[i] < idx; i++) {
                    prevMerge++;
                }

                int newRow = (sz - 1) - idx + prevMerge;
                if (newRow != tileRow[idx]) {  // 如果有任何一个tile的行发生了变化，记录下来
                    changed = true;
                }
                board.move(viewCol, newRow, board.tile(viewCol, tileRow[idx]));
            }
        }

        board.setViewingPerspective(Side.NORTH);  // 最后记得把板子的方向转回来

        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.

        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }

    /**
     * Checks if the game is over and sets the gameOver variable
     * appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /**
     * Determine whether game is over.
     */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /**
     * Returns true if at least one space on the Board is empty.
     * Empty spaces are stored as null.
     */
    public static boolean emptySpaceExists(Board b) {
        // TODO: Fill in this function.
        int sz = b.size();
        for (int i = 0; i < sz; i++) {
            for (int j = 0; j < sz; j++) {
                if (b.tile(i, j) == null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        // TODO: Fill in this function.
        int sz = b.size();
        for (int i = 0; i < sz; i++) {
            for (int j = 0; j < sz; j++) {
                if (b.tile(i, j) != null && b.tile(i, j).value() == MAX_PIECE) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        // TODO: Fill in this function.
        if (emptySpaceExists(b)) {
            return true;
        }

        // the board is not empty
        int sz = b.size();
        for (int i = 0; i < sz; i++) {
            for (int j = 0; j < sz - 1; j++) {
                if (b.tile(i, j).value() == b.tile(i, j + 1).value() ) {
                    return true;
                }
                if (b.tile(j, i).value() == b.tile(j + 1, i).value()) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
