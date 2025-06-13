import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * BotSnake - a Snake-like controllable thread class.
 * Used to simulate a snake moving towards the fruit choosing the shortest path possible.
 */
public class BotSnake extends Thread implements Snake {
    /**
     * Attribute which holds the current state of the frame.
     */
    private final Frame frame;
    /**
     * Attribute which holds the snakeIndex of the BotSnake
     */
    private final int snakeIndex;
    /**
     * Attribute which determines whether the Thread should be running.
     */
    private volatile boolean running = true;

    /**
     * Class constructor.
     * @param frame - current frame
     * @param snakeIndex - index of BotSnake
     */
    public BotSnake(Frame frame, int snakeIndex) {
        this.frame = frame;
        this.snakeIndex = snakeIndex;
    }

    /**
     * Implementation of run() from Thread class, defines the function which is executed by the thread upon creation.
     * <p>
     * In a loop calls the control() function, then sleeps for 100 milliseconds, on failure to sleep sets running to false.
     */
    @Override
    public void run() {
        while (running) {

            control();

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                running = false;
            }
        }
    }

    /**
     * The control of the BotSnake.
     * Chooses a direction towards the fruit. Takes the shortest path possible. If the shortest path is obstructed, chooses a random valid direction.
     */
    @Override
    public void control()
    {
        char dir = getFruitDirection(); // non-zero chance of getting stuck in an endless loop or killing itself
        //System.out.println(dir);
        if(!frame.isValidMove(snakeIndex, dir))
        {
            dir = getRandomValidDirection();
            //System.out.println("invalid: "+ dir);
        }
        if (dir != ' ') {
            frame.setDirection(snakeIndex, dir);
        }
    }

    /**
     * Generates a randomized valid direction that BotSnake will move in.
     * @return a char of valid move direction.
     */
    private char getRandomValidDirection() {
        List<Character> shuffled = new ArrayList<>(List.of('N', 'S', 'E', 'W'));
        char dirG = 'N';
        int smallestDistance = Integer.MAX_VALUE;
        for (char dir : shuffled) {
            if (frame.isValidMove(snakeIndex, dir)) {
                Point head = frame.snakes.get(snakeIndex).getFirst();
                int headRow = head.x;
                int headCol = head.y;
                switch (dir) {
                    case 'N': headRow--; break;
                    case 'S': headRow++; break;
                    case 'E': headCol++; break;
                    case 'W': headCol--; break;
                }
                Point headCopy = new Point(headRow, headCol);
                if(frame.getDistanceFromFruit(head) < smallestDistance) {
                    smallestDistance = frame.getDistanceFromFruit(head);
                    dirG = dir;
                }
            }
        }
        return dirG;
    }

    /**
     * Generates a direction that BotSnake will move in.
     * @return a char of fruit direction.
     */
    private char getFruitDirection()
    {
        char dir = ' ';
        Point head = frame.snakes.get(snakeIndex).getFirst();
        Point fruit = frame.getFruitPosition();

        if(head.x < fruit.x)
            dir = 'S';
        else if(head.x > fruit.x)
            dir = 'N';
        if(!frame.isValidMove(snakeIndex, dir))
        {
            if (head.y < fruit.y)
                dir = 'E';
            else
                dir = 'W';
        }
        return dir;
    }

    /**
     * Unused
     * Sets running to false.
     */
    public void stopRunning() {
        running = false;
    }
}
