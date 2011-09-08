package se.chalmers.snake.mastercontroller;

import se.chalmers.snake.interfaces.ControlResourcesIC;
import se.chalmers.snake.interfaces.LevelDatabaseIC;
import se.chalmers.snake.interfaces.MotionDetectorIC;
import se.chalmers.snake.interfaces.util.XYPoint;

/**
 *
 */
public class ControlResources implements ControlResourcesIC {

	@Override
	public LevelDatabaseIC getLevelDatabase() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public MotionDetectorIC getMotionDetector() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Object getSystemEventController() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public XYPoint getScreenSize() {
		return new XYPoint(400,400);
	}
	
}
