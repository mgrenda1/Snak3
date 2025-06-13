import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Frog - a Snake-like controllable thread class.
 * Used to simulate a chaotically moving snake when another Snake comes too close.
 */
public class Frog extends Thread implements Snake {

    /**
     * Attribute which holds the current state of the frame.
     */
    private final Frame frame;
    /**
     * Attribute which holds the snakeIndex of the Frog
     */
    private final int snakeIndex;
    /**
     * Attribute which determines whether the Thread should be running.
     */
    private volatile boolean running = true;

    /**
     * Class constructor.
     * @param frame - current frame
     * @param snakeIndex - index of Frog
     */
    public Frog(Frame frame, int snakeIndex) {
        this.frame = frame;
        this.snakeIndex = snakeIndex;
        this.frame.setDirection(this.snakeIndex, ' ');
    }

    /**
     * Implementation of run() from Thread class, defines the function which is executed by the thread upon creation.
     * <p>
     * In a loop calls the control() function.
     */
    @Override
    public void run() {
        while (running) {

            control();

        }
    }

    /**
     * The control of the Frog.
     * Waits for a notification from the Frame, after which it starts moving chaotically, until it moves far enough away from other snakes.
     */
    public void control()
    {
        synchronized (this.frame) {
            if (frame.distanceToFrog() < 5)
            {
                frame.setDirection(snakeIndex, getRandomValidDirection());
            }
            else
            {
                frame.setDirection(snakeIndex, ' ');
                try {
                    this.frame.wait();
                }
                catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    /**
     * Generates a randomized valid direction that Frog will move in.
     * @return a char of valid move direction.
     */
    private char getRandomValidDirection() {
        final char[] DIRECTIONS = {'N', 'S', 'E', 'W'};
        final Random random = new Random();

        int index = random.nextInt(DIRECTIONS.length);

        return DIRECTIONS[index];
    }

    /**
     * Unused
     * Sets running to false.
     */
    public void stopRunning() {
        running = false;
    }

}
