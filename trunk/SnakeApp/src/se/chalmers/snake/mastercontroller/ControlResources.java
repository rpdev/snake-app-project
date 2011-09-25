package se.chalmers.snake.mastercontroller;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import android.view.View;
import se.chalmers.snake.gameengine.TestGameEngine;
import se.chalmers.snake.highscoreDatabase.HighscoreDatabase;
import se.chalmers.snake.interfaces.GameEngineIC;
import se.chalmers.snake.interfaces.HighscoreDatabaseIC;
import se.chalmers.snake.interfaces.LevelDatabaseIC;
import se.chalmers.snake.interfaces.MotionDetectorIC;
import se.chalmers.snake.interfaces.util.XYPoint;
import se.chalmers.snake.leveldatabase.LevelDatabase;
import se.chalmers.snake.motiondetector.MotionDetector;
import se.chalmers.snake.util.Storage;

/**
 *
 */
public class ControlResources {

	private static ControlResources controlResources = null;
	private MotionDetectorIC motionDetector;
	private GameEngineIC gameEngine;
	private XYPoint screenSize;
	private Storage storage;
	private HighscoreDatabaseIC highscoreDatabase;

	private ControlResources(Activity currentActivity, SensorManager sensorManager, XYPoint screenSize) {
		this.motionDetector = new MotionDetector(sensorManager);
		this.highscoreDatabase = new HighscoreDatabase();
		this.storage = new Storage(currentActivity);
		//this.gameEngineIC = new GameEngine(this);
		this.gameEngine = TestGameEngine.getGameEngine(screenSize, this.motionDetector);	
	}

	public static void make(Activity currentActivity, int mainViewID) {
		if (ControlResources.controlResources == null) {
			currentActivity.findViewById(mainViewID).getHeight();
			try {

				View mainView = currentActivity.findViewById(mainViewID);
				XYPoint screenSize = new XYPoint(mainView.getWidth(), mainView.getHeight());

				//XYPoint screenSize = new XYPoint(currentActivity.getWindowManager().getDefaultDisplay().getWidth(), currentActivity.getWindowManager().getDefaultDisplay().getHeight());
				SensorManager sensorManager = (SensorManager) currentActivity.getSystemService(Context.SENSOR_SERVICE);
				ControlResources.controlResources = new ControlResources(currentActivity, sensorManager, screenSize);
			} catch (Exception ex) {}
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
		return this.motionDetector;
	}

	public XYPoint getScreenSize() {
		return this.screenSize;
	}

	public GameEngineIC getGameEngine() {
		return this.gameEngine;
	}

	public Storage getStorage() {
		return this.storage;
	}

	public HighscoreDatabaseIC getHighscoreDatabase() {
		return this.highscoreDatabase;
	}
}
