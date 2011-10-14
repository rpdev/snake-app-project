package se.chalmers.snake.interfaces;

/**
 * Hold count if a select level has be player or  not yet.
 */
public interface LevelHistoryIC {

	/**
	 * Store the level name of this level for later can be test if the level
	 * name exist in the list.
	 * @param levelName 
	 */
	public void set(String levelName);

	/**
	 * Test if this exist exist since before.
	 * @param levelName
	 * @return 
	 */
	public boolean is(String levelName);

	/**
	 * Empty the LevelHistoryIC and all {@link LevelHistoryIC#is(java.lang.String)) will be false.
	 */
	public void clear();
}
