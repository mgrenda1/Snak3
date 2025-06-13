/**
 *  The interface for any Snake-like controllable.
 * <p>
 *  Any Snake-like controllable needs to have:
 *  an attribute of Frame type to hold the current frame
 *  an attribute of Int type to hold the snake's index
 */

public interface Snake
{
    /**
     * Method that defines the control method for Snake-like controllable.
     * <p>
     * Control method has to use the Frame.setDirection() method
     */
    public void control();
}
