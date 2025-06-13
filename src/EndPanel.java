import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Class defining the StartPanel which is displayed after game ends, can relaunch the game.
 * Writes to file if new best score is achieved.
 */
public class EndPanel extends JPanel {
    /**
     * Class constructor, creates the visual representation of the end panel, letting the user restart the game using mouse input.
     * Displays the last best score and current game score.
     * @param mainFrame - the main frame which handles the game state logic.
     * @param currentScore - score achieved during the game.
     * @param bestScore - best score achieved saved in "scores.txt" file.
     */
    public EndPanel(MainGame mainFrame, int currentScore, int bestScore) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel welcomeLabel = new JLabel("Your score is: " + currentScore, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));

        //welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        //welcomeLabel.setVerticalAlignment(SwingConstants.CENTER);

        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

        textPanel.add(welcomeLabel, BorderLayout.CENTER);

        textPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel bestLabel = new JLabel("Best score: " + bestScore, SwingConstants.CENTER);
        bestLabel.setFont(new Font("Arial", Font.BOLD, 24));

        bestLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bestLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

        textPanel.add(bestLabel, BorderLayout.CENTER);


        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.add(textPanel);
        add(centerWrapper, BorderLayout.CENTER);


        add(centerWrapper, BorderLayout.CENTER);
        JButton startButton = new JButton("Play Again");
        startButton.setFont(new Font("Arial", Font.PLAIN, 18));
        startButton.addActionListener((ActionEvent e) -> mainFrame.startGame());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        add(buttonPanel, BorderLayout.SOUTH);
        if(currentScore >= bestScore)
        {
            new ScoreFile().writeScoreFile(currentScore);
        }
    }
}
