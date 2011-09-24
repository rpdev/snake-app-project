package se.chalmers.snake.interfaces;

import se.chalmers.snake.interfaces.util.XYPoint;
import se.chalmers.snake.util.Storage;

/**
 *
 */
public interface ControlResourcesIC {
	
	public LevelDatabaseIC getLevelDatabase();
	
	public MotionDetectorIC getMotionDetector();

	public SystemEventIC getSystemEventController();	

	public XYPoint getScreenSize();
	
	public GameEngineIC getGameEngine();
	
	public HighscoreDatabaseIC getHighscoreDatabase();
	
	public Storage getStorage();
	
}
