package se.chalmers.snake.interfaces;

import se.chalmers.snake.interfaces.util.XYPoint;
import se.chalmers.snake.util.Storage;

/**
 * This holde a collections of all servers the game is given.
 */
public interface ControlResourcesIC {

	public LevelDatabaseIC getLevelDatabase();

	public MotionDetectorIC getMotionDetector();

	public XYPoint getScreenSize();

	public GameEngineIC getGameEngine();

	public HighscoreDatabaseIC getHighscoreDatabase();

	public Storage getStorage();

	public LevelHistoryIC getLevelHistory();
}
