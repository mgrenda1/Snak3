import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * BotSnake - a Snake-like controllable thread class.
 * Used to provide snake control to the player.
 */
class UserSnake extends Thread implements KeyListener, Snake {
    /**
     * Attribute which holds the current state of the frame.
     */
    private final Frame frame;
    /**
     * Attribute which holds the current direction of the UserSnake.
     */
    private volatile char currentDirection = ' ';
    /**
     * Attribute which determines whether the Thread should be running.
     */
    private volatile boolean running = true;

    /**
     * Class constructor.
     * @param frame - current frame
     */
    public UserSnake(Frame frame) {
        this.frame = frame;
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
     * The control of the UserSnake.
     * Chooses a direction towards the key pressed by the player, otherwise moves in the last chosen direction.
     */
    @Override
    public void control()
    {
        if (currentDirection != ' ')
        {
            frame.setDirection(2, currentDirection);
            currentDirection = ' ';
        }
    }

    /**
     * Sets the currentDirection based upon the key pressed.
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:    currentDirection = 'N'; break;
            case KeyEvent.VK_DOWN:  currentDirection = 'S'; break;
            case KeyEvent.VK_LEFT:  currentDirection = 'W'; break;
            case KeyEvent.VK_RIGHT: currentDirection = 'E'; break;
        }
    }

    /**
     * Unused
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {
    }

    /**
     * Unused
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Unused
     * Sets running to false.
     */
    public void stopRunning() {
        running = false;
    }
}