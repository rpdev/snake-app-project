package se.chalmers.snake.interfaces;

public interface LevelDatabaseIC {
	
	/**
	 * Return the instance of the level database.
	 * @return LevelDatabase
	 */
	public LevelDatabaseIC getInstance();
	
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
}
