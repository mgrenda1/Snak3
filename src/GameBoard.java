import javax.swing.*;
import java.awt.*;

/**
 * Class handling the painting of each frame.
 */
public class GameBoard extends JPanel {
    /**
     * Attribute that represents the current frame state.
     */
    private final Frame gameFrame;

    /**
     * Class constructor.
     * @param gameFrame - sets current frame state.
     */
    public GameBoard(Frame gameFrame) {
        this.gameFrame = gameFrame;
    }

    /**
     * Function paints the objects based on their position in the frame.
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        char[][] pixels = gameFrame.getFrame();

        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int rows = pixels.length;
        int cols = pixels[0].length;

        int cellWidth = panelWidth / cols;
        int cellHeight = panelHeight / rows;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                char c = pixels[row][col];

                switch (c) {
                    case 'A': case 'a': case 'x':
                        g.setColor(Color.RED); break;
                    case 'B': case 'b': case 'y':
                        g.setColor(Color.BLUE); break;
                    case 'S': case 's': case 'z':
                        g.setColor(Color.GREEN); break;
                    case 'o':
                        g.setColor(Color.ORANGE); break;
                    case 'p':
                        g.setColor(Color.DARK_GRAY); break;
                    default:
                        g.setColor(Color.WHITE);
                }

                int x = col * cellWidth;
                int y = row * cellHeight;
                g.fillRect(x, y, cellWidth, cellHeight);

                g.setColor(Color.GRAY); // grid line
                g.drawRect(x, y, cellWidth, cellHeight);
            }
        }
    }
}
