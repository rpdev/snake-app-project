package se.chalmers.snake.mastercontroller;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import se.chalmers.snake.gameengine.GameEngine;
import se.chalmers.snake.gameengine.TestGameEngine;
import se.chalmers.snake.highscoreDatabase.HighscoreDatabase;
import se.chalmers.snake.interfaces.GameEngineIC;
import se.chalmers.snake.interfaces.HighscoreDatabaseIC;
import se.chalmers.snake.interfaces.LevelDatabaseIC;
import se.chalmers.snake.interfaces.MotionDetectorIC;
import se.chalmers.snake.interfaces.SystemEventIC;
import se.chalmers.snake.interfaces.util.XYPoint;
import se.chalmers.snake.leveldatabase.LevelDatabase;
import se.chalmers.snake.motiondetector.MotionDetector;
import se.chalmers.snake.util.Storage;

/**
 *
 */
public class ControlResources {

	private static ControlResources controlResources = null;
	private MotionDetectorIC motionDetectorIC;
	private GameEngineIC gameEngineIC;
	private XYPoint screenSize;
	private SystemEventIC systemEventIC;
	private Storage storage;
	private HighscoreDatabaseIC highscoreDatabase;

	private ControlResources(Activity currentActivity, SensorManager sensorManager, XYPoint screenSize) {
		this.motionDetectorIC = new MotionDetector(sensorManager);
		this.systemEventIC = new SystemEventIC() {

			public void systemInterrupt() {
				if (ControlResources.this.gameEngineIC != null) {
					ControlResources.this.gameEngineIC.pauseGame();
				}
			}
		};

		this.highscoreDatabase = new HighscoreDatabase();
		this.storage = new Storage(currentActivity);
		//this.gameEngineIC = new GameEngine(this);
		this.gameEngineIC = TestGameEngine.getGameEngine(screenSize,this.motionDetectorIC);
	}

	public static void make(Activity currentActivity) {
		if(ControlResources.controlResources==null) {
		XYPoint screenSize = new XYPoint(currentActivity.getWindowManager().getDefaultDisplay().getWidth(), currentActivity.getWindowManager().getDefaultDisplay().getHeight());
		SensorManager sensorManager = (SensorManager) currentActivity.getSystemService(Context.SENSOR_SERVICE);
		ControlResources.controlResources = new ControlResources(currentActivity, sensorManager, screenSize);
		}
	}
	
	public static ControlResources get() {
		if (ControlResources.controlResources != null) {
			return ControlResources.controlResources;
		} else {
			throw new RuntimeException("Can not get ControlResources Instance call new ControlResources() first.");
		}
	}

	
	public LevelDatabaseIC getLevelDatabase() {
		return LevelDatabase.getInstance();
	}

	
	public MotionDetectorIC getMotionDetector() {
		return this.motionDetectorIC;
	}

	
	public XYPoint getScreenSize() {
		return this.screenSize;
	}

	public GameEngineIC getGameEngine() {
		
		
		return this.gameEngineIC;
	}

	public SystemEventIC getSystemEventController() {
		return this.systemEventIC;
	}

	public Storage getStorage() {
		return this.storage;
	}

	public HighscoreDatabaseIC getHighscoreDatabase() {
		return this.highscoreDatabase;
	}
}
