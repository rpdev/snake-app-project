package se.chalmers.snake.interfaces;

import java.util.List;

import se.chalmers.snake.interfaces.util.REPoint;
import se.chalmers.snake.interfaces.util.XYPoint;

public interface LevelIC {

	public String getLevelName();

	public String getLevelDescription();

	public int getLevel();

	/**
	 * Return the default size of the map in pixels.
	 * @return
	 */
	public XYPoint getMapSize();

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
	public XYPoint getSnakeHeadStartLocation();

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

	/**
	 * Return how width the player body will be.
	 * @return 
	 */
	public int getPlayerBodyWidth();

	/**
	 * Return how big each item will be.
	 * @return 
	 */
	public int getItemsRadius();

	/*
	 * BELOW METHODS IS CALLED REPETEDLY
	 */
	/**
	 * Get the speed increase after collected item, this value
	 * can differ between collected items. The value is between
	 * 0.1 - 50.
	 * @param collectTime
	 * @return
	 */
	public float getSpeed(List<Integer> collectTime);

	/**
	 * Test if the player has reached the goal for this level.
	 * @param collectTime
	 * @return
	 */
	public boolean hasReachedGoal(List<Integer> collectTime);

	/**
	 * How many items should add this time to the game.
	 * @param totalCollected is the number of collects item the player has collect
	 * @param totalItemInGame is the number of items on in the game now.
	 * @return
	 */
	//public int getItemsCount(int totalCollected);
	public int getAddItems(int totalCollected, int totalItemInGame);

	/**
	 * Growth of the snake's body after collected an item, this value
	 * can change between each collected item. Value between 0 - 15.
	 * @param collectTime
	 * @param totalCollected
	 * @return
	 */
	public int getBodyGrowth(int collectTime, int totalCollected);
}
