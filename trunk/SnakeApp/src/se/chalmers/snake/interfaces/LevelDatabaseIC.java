package se.chalmers.snake.interfaces;

public interface LevelDatabaseIC {
		
	/**
	 * Return a level map from the database based on the level
	 * of that game map.  
	 * @param level
	 * @return
	 */
	public LevelIC getByLevel(int level);
	
	/**
	 * Return level map form the database based on the name of
	 * the level.
	 * @param name
	 * @return
	 */
	public LevelIC getByName(String name);
	
	/**
	 * Return the name of the next level from the logic order of level.
	 * @param level
	 * @return Return name, or null if no next level exist
	 */
	public String getNextLevel(String level); 
	
	/**
	 * Return the level ID of the next level from the logic order of levels
	 * @param level
	 * @return Return the ID or -1 if no next level exist.
	 */
	public int getNextLevel(int level);
	
	/**
	 * Return an array with the name of levels in the database.
	 * @return
	 */
	public String[] getLevelListByName();
	
	/**
	 * Return an array with the level of the level maps in the database,
	 * from easiest to hardest.
	 * @return
	 */
	public int[] getLevelListByLevel();

	/**
	 * Return the default level that contains no obstacles and where
	 * the goal is to catch 10 items.
	 * @return
	 */
	public LevelIC getDefaultLevel();
}
