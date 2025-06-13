import javax.swing.*;
import java.awt.*;

/** unused */

public class GameWindow {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Frame gameFrame = new Frame(20, 20);
            UserSnake userSnake = new UserSnake(gameFrame);
            BotSnake botSnakeA = new BotSnake(gameFrame, 1);
            BotSnake botSnakeB = new BotSnake(gameFrame, 0);
            GameBoard gamePanel = new GameBoard(gameFrame);

            JFrame window = new JFrame("Snake Game");
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setUndecorated(true); // Optional: remove title bar
            window.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize to fill screen

            window.add(gamePanel);
            window.setFocusable(true);
            window.addKeyListener(userSnake);
            window.setVisible(true);

            userSnake.start();
            botSnakeA.start();
            botSnakeB.start();

            // Use Swing Timer for reliable repainting
            Timer repaintTimer = new Timer(100, e -> {
                if (gameFrame.isOver()) {
                    ((Timer) e.getSource()).stop();
                } else {
                    gamePanel.repaint();
                }
            });
            repaintTimer.start();
        });
    }
}
