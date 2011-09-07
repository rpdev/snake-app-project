package se.chalmers.snake.interfaces;

import se.chalmers.snake.interfaces.util.XYPoint;

/**
 *
 */
public interface ControlResourcesIC {
	
	public Object getLevelDatabase();
	
	public MotionDetectorIC getMotionDetector();

	public Object getSystemEventController();	

	public XYPoint getScreenSize();
	
}
