import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Class defining the StartPanel which is displayed on first launch of the game.
 */
public class StartPanel extends JPanel {
    /**
     * Class constructor, creates the visual representation of the start panel, letting the user start the game using mouse input.
     * @param mainFrame - the main frame which handles the game state logic.
     */
    public StartPanel(MainGame mainFrame) {
        setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome to the Game!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(welcomeLabel, BorderLayout.CENTER);

        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.PLAIN, 18));
        startButton.addActionListener((ActionEvent e) -> mainFrame.startGame());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
