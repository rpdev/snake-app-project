package se.chalmers.snake.interfaces;

import se.chalmers.snake.interfaces.util.XYPoint;

/**
 *
 */
public interface ControlResourcesIC {
	
	public LevelDatabaseIC getLevelDatabase();
	
	public MotionDetectorIC getMotionDetector();

	public SystemEventIC getSystemEventController();	

	public XYPoint getScreenSize();
	
	public GameEngineIC getGameEngine();
	
}
