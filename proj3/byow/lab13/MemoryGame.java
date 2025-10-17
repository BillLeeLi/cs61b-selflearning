package byow.lab13;

import byow.Core.RandomUtils;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    /** The width of the window of this game. */
    private final int width;
    /** The height of the window of this game. */
    private final int height;
    /** The current round the user is on. */
    private int round;
    /** The Random object used to randomly generate Strings. */
    private final Random rand;
    /** Whether or not the game is over. */
    private boolean gameOver;
    /** Whether or not it is the player's turn. Used in the last section of the
     * spec, 'Helpful UI'. */
    private boolean playerTurn;
    /** The characters we generate random Strings from. */
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    /** Encouraging phrases. Used in the last section of the spec, 'Helpful UI'. */
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }
        long seed = Long.parseLong(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        //TODO: Initialize random number generator
        rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        String res = "";
        for (int i = 0; i < n; i++) {
            res += CHARACTERS[rand.nextInt(CHARACTERS.length)];  // 随机选取一个字符加入尾部
        }
        return res;
    }

    public void drawFrame(String s) {
        //TODO: Take the string and display it in the center of the screen
        //TODO: If game is not over, display relevant game information at the top of the screen
        StdDraw.clear(Color.BLACK);  // 清空画布
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);  // 设置字体
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text((double) width / 2, (double) height / 2, s);  // 把字符串放置在画布中央
        StdDraw.show();
    }

    public void flashSequence(String letters) {
        //TODO: Display each character in letters, making sure to blank the screen between letters
        char[] chars = letters.toCharArray();
        for (char c: chars) {
            drawFrame(Character.toString(c));  // 一次显示一个字符
            try {
                Thread.sleep(1000);  // 把字符展示1s
            } catch (InterruptedException e) {
                System.out.println("Exception during sleeping.");
                Thread.currentThread().interrupt();  // 恢复中断状态
            }

            drawFrame("");
            try {
                Thread.sleep(500);  // 休眠0.5s再展示下一个字符
            } catch (InterruptedException e) {
                System.out.println("Exception during sleeping.");
                Thread.currentThread().interrupt();  // 恢复中断状态
            }
        }
    }

    public String solicitNCharsInput(int n) {
        //TODO: Read n letters of player input
        String res = "";
        for (int i = 0; i < n; i++) {
            while (!StdDraw.hasNextKeyTyped());  // 等待用户键入下一个字符
            res += StdDraw.nextKeyTyped();
            drawFrame(res);
        }
        return res;
    }

    public void startGame() {
        //TODO: Set any relevant variables before the game starts
        round = 1;
        gameOver = false;

        //TODO: Establish Engine loop
        while (!gameOver) {
            drawFrame("Round: " + round);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("Exception during sleeping.");
                Thread.currentThread().interrupt();  // 恢复中断状态
            }

            drawFrame("");  // 清空屏幕，等待用户输入
            String answer = generateRandomString(round);
            flashSequence(answer);
            String res = solicitNCharsInput(round);
            if (!answer.equals(res)) {
                gameOver = true;
            }
            round++;
        }
        drawFrame("Game over! Your score: " + (round - 2));
    }
}
