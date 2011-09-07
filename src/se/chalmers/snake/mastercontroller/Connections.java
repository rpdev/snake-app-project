package se.chalmers.snake.mastercontroller;

import se.chalmers.snake.interfaces.ConnectionsIC;
import se.chalmers.snake.interfaces.MotionDetectorIC;

/**
 *
 */
public class Connections implements ConnectionsIC {

	@Override
	public Object getLevelDatabase() {
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
	
}
