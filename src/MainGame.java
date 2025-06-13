import javax.swing.*;
import java.awt.*;

/**
 * Class handling the game state logic, creates all threads, displays the start and end panels.
 */
public class MainGame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cards;

    /**
     * Representation of the current game state.
     */
    private Frame gameFrame;

    /**
     * Class constructor, on creation displays the start menu.
     */
    public MainGame() {
        setTitle("Snake Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);


        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        StartPanel startPanel = new StartPanel(this);
        cards.add(startPanel, "StartPanel");

        add(cards);
        setVisible(true);
    }


    /**
     * Method launches the game, creates all the required objects, then runs the threads.
     */
    public void startGame() {
        int bestScore = new ScoreFile().readScoreFile();
        gameFrame = new Frame(20, 20);
        GameBoard gameBoard = new GameBoard(gameFrame);

        UserSnake userSnake = new UserSnake(gameFrame);
        Frog botSnakeB = new Frog(gameFrame, 1);
        BotSnake botSnakeA = new BotSnake(gameFrame, 0);

        cards.add(gameBoard, "GameBoard");
        cardLayout.show(cards, "GameBoard");

        addKeyListener(userSnake);
        setFocusable(true);
        requestFocusInWindow();

        userSnake.start();
        botSnakeA.start();
        botSnakeB.start();

        new Thread(() -> {
            while (!gameFrame.isOver()) {
                try {
                    Thread.sleep(500);
                    SwingUtilities.invokeLater(() -> {
                        gameBoard.revalidate();
                        gameBoard.repaint();
                    });
                } catch (InterruptedException e) {
                    return;
                }
            }

            SwingUtilities.invokeLater(() -> showEndPanel(gameFrame.getScore(), bestScore));
        }).start();

    }

    /**
     * Method dispplaying the end panel.
     * @param score - current score achieved during the game.
     * @param bestScore - best score read from file.
     */
    public void showEndPanel(int score, int bestScore) {
        EndPanel endPanel = new EndPanel(this, score, bestScore);
        cards.add(endPanel, "EndPanel");
        cardLayout.show(cards, "EndPanel");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainGame::new);
    }
}

