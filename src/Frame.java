import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.abs;

/**
 * Class representing the current state of the game.
 */
public class Frame {
    /**
     * Holds the number of rows on the board.
     */
    private final int rows;
    /**
     * Holds the number of columns on the board.
     */
    private final int cols;
    /**
     * Holds the current state of each board pixel.
     */
    private final char[][] pixels;
    /**
     * Collection of each snake's points, represents their position on the board.
     */
    public final List<List<Point>> snakes;
    /**
     * Collection of obstacle points, represents their position on the board.
     */
    private final List<Point> obstacles;
    /**
     * The point which represents fruit position on the board.
     */
    private Point fruit;
    /**
     * An array holding the direction in which each snake will move based upon the index.
     */
    private final char[] directions;
    /**
     * An array holding the state of each snake's condition
     * False if snake is alive.
     * True if snake is dead.
     */
    private final boolean[] lost;
    /**
     * An array holding the current score of each snake.
     */
    private final int[] score;
    /**
     * Index of the frog.
     */
    private int frogIndex;

    /**
     * Class constructor, initializes the board state.
     * @param rows - number of rows to be created.
     * @param cols - number of columns to be created.
     */
    public Frame(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.score = new int[]{0, 0, 0};
        this.pixels = new char[rows][cols];
        this.snakes = new ArrayList<>();
        this.obstacles = new ArrayList<>();
        this.directions = new char[3];
        this.lost = new boolean[3];
        directions[0] = 'N';
        directions[1] = 'W';
        directions[2] = 'S';

        clear();
        initializeSnakes();
        placeObstacles();
        placeFruit();
    }

    /**
     * Initializes the state of snakes on the board.
     */
    private void initializeSnakes() {
        int midRow = rows / 2;

        // Snake A
        List<Point> snakeA = new ArrayList<>();
        snakeA.add(new Point(midRow, 3));     // head A
        snakeA.add(new Point(midRow, 4));     // body a
        snakeA.add(new Point(midRow, 5));     // tail x
        snakes.add(snakeA);

        // Snake B
        List<Point> snakeB = new ArrayList<>();
        snakeB.add(new Point(midRow + 2, 10+3)); // head B
        //snakeB.add(new Point(midRow + 2, 10+4)); // body b
        //snakeB.add(new Point(midRow + 2, 10+5)); // tail y
        snakes.add(snakeB);
        frogIndex = 1;

        // Snake S (user snake)
        List<Point> snakeS = new ArrayList<>();
        snakeS.add(new Point(midRow + 4, 3)); // head S
        snakeS.add(new Point(midRow + 4, 4)); // body s
        snakeS.add(new Point(midRow + 4, 5)); // tail z
        snakes.add(snakeS);
    }

    /**
     * Clears the board.
     */
    public void clear() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                pixels[i][j] = ' ';
            }
        }
        //placeObstacles();
        //placeFruit();
    }

    /**
     * Sets the direction "dir" in which the snake with index "index" will move.
     * @param index - index of the snake.
     * @param dir - direction in which it will move.
     */
    public synchronized void setDirection(int index, char dir) {
        if (index < 0 || index >= directions.length) {
            throw new IllegalArgumentException("Invalid snake index");
        }
        if (!lost[index]) {
            if(isOpposite(dir, index)){
                return;
            }
            directions[index] = dir;
        }
    }

    /**
     * Places the obstacles on the board.
     */
    private void placeObstacles() {
        obstacles.clear();
        obstacles.add(new Point(4, 4));
        obstacles.add(new Point(6, 6));
        obstacles.add(new Point(7, 7));

        for (Point p : obstacles) {
            pixels[p.x][p.y] = 'p';
        }
    }

    /**
     * Places the fruit on the board based on the current contents of the board.
     */
    private void placeFruit() {
        List<Point> emptySpaces = new ArrayList<>();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (pixels[row][col] == ' ' && !obstacles.contains(new Point(row, col)) && !isSnakeBody(row, col, 0) && !isSnakeBody(row, col, 1) && !isSnakeBody(row, col, 2)) {
                    emptySpaces.add(new Point(row, col));
                }
            }
        }

        if (!emptySpaces.isEmpty()) {
            fruit = emptySpaces.get(new Random().nextInt(emptySpaces.size()));
            pixels[fruit.x][fruit.y] = 'o';
        }
    }

    /**
     * Handles the updates of the board condition.
     * @return the current state of the frame.
     */
    public synchronized char[][] getFrame() {

        notifyFrog();

        for (int i = 0; i < directions.length; i++) {
            if (!lost[i]) {
                moveSnake(i, directions[i]);
            }
        }
        for (int i = 0; i < lost.length; i++) {
            if (lost[i]) {
                clearSnake(i);
            }
        }
        char[][] copy = new char[pixels.length][];
        for (int i = 0; i < pixels.length; i++) {
            copy[i] = pixels[i].clone();
        }
        return copy;
    }

    /**
     * Moves the snake with index "index" in the specified direction "dir". Checks whether the move is valid. If not, snake is killed.
     * @param index - index of the snake to be moved.
     * @param dir - direction in which to move the snake.
     */
    private void moveSnake(int index, char dir) {

        if (lost[index]) {
            return;
        }
        List<Point> snake = snakes.get(index);
        Point head = snake.getFirst();
        int newRow = head.x;
        int newCol = head.y;

        switch (dir) {
            case ' ': break;
            case 'N': newRow--; break;
            case 'S': newRow++; break;
            case 'E': newCol++; break;
            case 'W': newCol--; break;
        }

        if(newRow == head.x && newCol == head.y)
        {
            pixels[newRow][newCol] = (index == 0 ? 'a' : (index == 1 ? 'b' : 's'));
            return;
        }

        if (!isInBounds(newRow, newCol) || isSnakeBody(newRow, newCol,0) || isSnakeBody(newRow, newCol,1) || isSnakeBody(newRow, newCol,2) || isObstacle(newRow, newCol)) {
            lost[index] = true;
            for(Point p : snakes.get(index)) {
                pixels[p.x][p.y] = ' ';
            }
            snakes.get(index).clear();
            return;
        }

        snake.addFirst(new Point(newRow, newCol));

        if (new Point(newRow, newCol).equals(fruit)) {
            score[index]++;
            placeFruit();
        } else {
            Point tail = snake.removeLast();
            pixels[tail.x][tail.y] = ' ';
        }
        pixels[newRow][newCol] = (index == 0 ? 'A' : (index == 1 ? 'B' : 'S'));
        for (int i = 1; i < snake.size(); i++) {
            Point bodyPart = snake.get(i);
            pixels[bodyPart.x][bodyPart.y] = (index == 0 ? 'a' : (index == 1 ? 'b' : 's'));
        }
    }

    /**
     * Checkes whether a said pixel is in bounds of the board.
     * @param row - row of the pixel.
     * @param col - column of the pixel.
     * @return true if is, false otherwise.
     */
    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    /**
     * Checks whether a said pixel is a body of snake with index "index"
     * @param row - row of the pixel.
     * @param col - column of the pixel.
     * @param index - index of the snake to be checked.
     * @return true if is, false otherwise.
     */
    private boolean isSnakeBody(int row, int col, int index) {
        List<Point> snake = snakes.get(index);
        for (int i = 0; i < snake.size(); i++) {  // Head isn't skipped
            if (snake.get(i).equals(new Point(row, col))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Clears the snake with index "index" from the board.
     * @param index - index of the snake to be cleared.
     */
    private void clearSnake(int index) {
        List<Point> snake = snakes.get(index);
        for (Point p : snake) {
            pixels[p.x][p.y] = ' ';
        }
    }

    /**
     * Unused
     * @return number of board columns.
     */
    public int getCols() {
        return cols;
    }
    /**
     * Unused
     * @return number of board rows.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Checkes whether said pixel is an obstacle.
     * @param row - row of the pixel.
     * @param col - column of the pixel.
     * @return true if is, false otherwise.
     */
    private boolean isObstacle(int row, int col) {
        for (Point p : obstacles) {
            if (p.x == row && p.y == col) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether a move made by a snake with index "index" in the "dir" direction is a valid move.
     * @param index - index of the snake.
     * @param dir - direction of the movement.
     * @return true if is, false otherwise.
     */
    public boolean isValidMove(int index, char dir) {
        Point head = snakes.get(index).getFirst();
        int newRow = head.x, newCol = head.y;

        switch (dir) {
            case 'N' -> newRow--;
            case 'S' -> newRow++;
            case 'E' -> newCol++;
            case 'W' -> newCol--;
        }

        if (!isInBounds(newRow, newCol)) return false;
        if (isObstacle(newRow, newCol)) return false;
        if (isSnakeBody(newRow, newCol, index)) return false;

        return true;
    }

    /**
     * Returns the distance from fruit of the point p.
     * @param p - point from which the distance is measured
     * @return distance from fruit.
     */
    int getDistanceFromFruit(Point p){
        return abs(fruit.x - p.x) + abs(fruit.y-p.y);
    }

    /**
     * Returns the position of the fruit.
     * @return Point containing the fruit positon.
     */
    public Point getFruitPosition() {
        return new Point(fruit.x, fruit.y);
    }

    /**
     * Checkes whether the direction "dir" and the movement of the snake with index "index" are opposite to each other.
     * @param dir - direction to be checked.
     * @param index - index of the snake.
     * @return true if is, false otherwise.
     */
    private boolean isOpposite(char dir, int index) {
        if(dir == 'E' && directions[index] == 'W'){
            return true;
        }else if(dir == 'W' && directions[index] == 'E'){
            return true;
        }else if(dir == 'S' && directions[index] == 'N'){
            return true;
        }else if(dir == 'N' && directions[index] == 'S'){
            return true;
        }
        return false;
    }

    /**
     * Checks whether the player has lost the game.
     * @return true if player lost the game, false otherwise.
     */
    boolean isOver(){
        return lost[2];
    }

    /**
     * Returns the players current score.
     * @return current score of the player.
     */
    int getScore(){
    return score[2];}

    /**
     * Returns the shortest distance to Frog of any other snake's head.
     * @return shortest distance of snake head to Frog.
     */
    public int distanceToFrog()
    {
        int minDistance = Integer.MAX_VALUE;
        for(int i = 0; i < snakes.size(); i++)
        {
            if(i == frogIndex)
                continue;
            if(lost[i])
                continue;
            if(lost[frogIndex])
            {
                return minDistance;
            }
            Point head = snakes.get(i).getFirst();
            var xDistance = head.x - snakes.get(frogIndex).getFirst().x;
            var yDistance = head.y - snakes.get(frogIndex).getFirst().y;

            minDistance = Math.min(minDistance, Math.abs(xDistance) + Math.abs(yDistance));
        }

        return minDistance;
    }

    /**
     * Notifies the Frog thread if a head of any other snake is closer than 4 units.
     */
    synchronized void notifyFrog()
    {
        if(distanceToFrog() < 4)
        {
            notifyAll();
        }
    }

}
