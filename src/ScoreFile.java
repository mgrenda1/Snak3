import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.util.Scanner;
import java.io.FileNotFoundException;

/**
 * Class responsible for creating, reading and writing to the score file.
 */
public class ScoreFile {
    /**
     * Method for creating a new score file.
     * <p>
     * Tries to create a new score file, if exists returns false, on exception it prints the message and returns true, returns true if file was created.
     * @return true if new file was created or there was a failure, false otherwise.
     */
    private static boolean createScoreFile()
    {
        try
        {
            File scoreFile = new File("scores.txt");

            if(!scoreFile.createNewFile())
                return false;
            else
                System.out.println("Scores file created: " + scoreFile.getAbsolutePath());
        }
        catch (IOException e)
        {
            System.out.println("Error creating scores file: " + e.getMessage());
        }

        return true;
    }

    /**
     * Method for reading the best score from a file.
     * Calls createScoreFile(), if no file existed, the best score is set as 0.
     * If there was a score file it reads the current best score.
     * @return the current best score.
     */

    public int readScoreFile()
    {
        int score = 0;
        if(createScoreFile())
            return score;
        else
        {
            try
            {
                File scoreFile = new File("scores.txt");
                Scanner scoreReader = new Scanner(scoreFile);
                while(scoreReader.hasNextLine())
                {
                    String line = scoreReader.nextLine();
                    score = Integer.parseInt(line);
                }
                scoreReader.close();
            }
            catch (FileNotFoundException e)
            {
                System.out.println("Error reading scores file: " + e.getMessage());
            }
        }
        return score;
    }

    /**
     * Method to write current best score to file. Takes a score parameter, which is then saved to "scores.txt" file.
     * @param score - score to be written to file.
     * @return true if successful, false otherwise.
     */
    public boolean writeScoreFile(int score)
    {
        try
        {
            FileWriter scoreWriter = new FileWriter("scores.txt");
            scoreWriter.write(Integer.toString(score));
            scoreWriter.close();
        }
        catch (IOException e)
        {
            System.out.println("Error writing scores file: " + e.getMessage());
            return false;
        }
        return true;
    }
}
