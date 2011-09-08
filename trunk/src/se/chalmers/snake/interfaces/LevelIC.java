package se.chalmers.snake.interfaces;

import java.util.List;

import android.graphics.Point;
import se.chalmers.snake.interfaces.util.REPoint;

/**
 *
 */
public interface LevelIC {
	
	public String getLevelName();
	
	public String getLevelDescription(); 
	
	public int getLevel();
	
	/**
	 * Return the default size of the map in pixels.
	 * @return
	 */
	public Point getMapSize();
	
	/**
	 * Return the length of the snake at level start,
	 * this value should always be equal or larger then two.
	 * @return Snake length at start.
	 */
	public int getSnakeStartLength();
	
	/**
	 * The start location of the snakes head.
	 * @return
	 */
	public Point getSnakeHeadStartLocation();
	
	/**
	 * Get the direction of the snake body from the head at start,
	 * reference is the unit circle in radian.
	 * @return
	 */
	public double getStartAngle();
	
	/**
	 * Return obstacles in the map.
	 * @return
	 */
	public List<REPoint> getObstacles();
	
	/*
	 * BELOW METHODS IS CALLED REPETEDLY
	 */
	
	/**
	 * Get the speed increase after collected item, this value
	 * can differ between collected items. The value is between
	 * 1 - 100.
	 * @param collectTime
	 * @return
	 */
	public int getSpeed(List<Integer> collectTime);
	
	
	/**
	 * Test if the player has reached the goal for this level.
	 * @param collectTime
	 * @return
	 */
	public boolean hasReachedGoal(List<Integer> collectTime);
	
	/**
	 * How many items should be displayed at once.
	 * @param collectTime
	 * @return
	 */
	public int getItemsCount(int totalCollected);
	
	/**
	 * Growth of the snake's body after collected an item, this value
	 * can change between each collected item. Value between 0 - 15.
	 * @param collectTime
	 * @param totalCollected
	 * @return
	 */
	public int getBodyGrowth(int collectTime, int totalCollected);	
}
