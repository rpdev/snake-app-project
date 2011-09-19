package se.chalmers.snake.mastercontroller;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import se.chalmers.snake.gameengine.GameEngine;
import se.chalmers.snake.interfaces.ControlResourcesIC;
import se.chalmers.snake.interfaces.GameEngineIC;
import se.chalmers.snake.interfaces.LevelDatabaseIC;
import se.chalmers.snake.interfaces.MotionDetectorIC;
import se.chalmers.snake.interfaces.SystemEventIC;
import se.chalmers.snake.interfaces.util.XYPoint;
import se.chalmers.snake.leveldatabase.LevelDatabase;
import se.chalmers.snake.motiondetector.MotionDetector;

/**
 *
 */
public class ControlResources implements ControlResourcesIC {

	private MotionDetectorIC motionDetectorIC;
	private GameEngineIC gameEngineIC;
	private XYPoint screenSize;
	private SystemEventIC systemEventIC;
	private ControlResources(SensorManager sensorManager, XYPoint screenSize) {
		this.motionDetectorIC = new MotionDetector(sensorManager);
		this.systemEventIC= new SystemEventIC() {
			public void systemInterrupt() {
				if (ControlResources.this.gameEngineIC != null) {
					ControlResources.this.gameEngineIC.pauseGame();
				}
			}
		};
		this.gameEngineIC = new GameEngine(this);
		
	}

	@Override
	public LevelDatabaseIC getLevelDatabase() {
		return LevelDatabase.getInstance();
	}

	@Override
	public MotionDetectorIC getMotionDetector() {
		return this.motionDetectorIC;
	}

	@Override
	public XYPoint getScreenSize() {
		return this.screenSize;
	}

	public GameEngineIC getGameEngine() {
		return this.gameEngineIC;
	}

	public SystemEventIC getSystemEventController() {
		return this.systemEventIC;
	}
}
