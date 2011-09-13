package se.chalmers.snake.interfaces;

import se.chalmers.snake.interfaces.util.XYPoint;

/**
 *
 */
public interface ControlResourcesIC {
	
	public LevelDatabaseIC getLevelDatabase();
	
	public MotionDetectorIC getMotionDetector();

	public Object getSystemEventController();	

	public XYPoint getScreenSize();
	
	public GameEngineIC getGameEngine();
	
}
