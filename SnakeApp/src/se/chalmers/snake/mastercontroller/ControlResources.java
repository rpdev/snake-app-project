package se.chalmers.snake.mastercontroller;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import android.view.View;
import se.chalmers.snake.gameengine.GameEngine;
import se.chalmers.snake.highscoreDatabase.HighscoreDatabase;
import se.chalmers.snake.interfaces.ControlResourcesIC;
import se.chalmers.snake.interfaces.GameEngineIC;
import se.chalmers.snake.interfaces.HighscoreDatabaseIC;
import se.chalmers.snake.interfaces.LevelDatabaseIC;
import se.chalmers.snake.interfaces.LevelHistoryIC;
import se.chalmers.snake.interfaces.MotionDetectorIC;
import se.chalmers.snake.interfaces.util.XYPoint;
import se.chalmers.snake.leveldatabase.LevelDatabase;
import se.chalmers.snake.motiondetector.MotionDetector;
import se.chalmers.snake.util.Storage;

/**
 *
 */
public class ControlResources implements ControlResourcesIC {

	private static ControlResources controlResources = null;
	private MotionDetectorIC motionDetector;
	private GameEngineIC gameEngine;
	private XYPoint screenSize;
	private Storage storage;
	private LevelDatabaseIC levelDatabase;
	private HighscoreDatabaseIC highscoreDatabase;
	private LevelHistoryIC levelHistory;

	private ControlResources(Activity currentActivity, SensorManager sensorManager, XYPoint screenSize) {
		this.motionDetector = new MotionDetector(sensorManager);


		this.storage = new Storage(currentActivity);

		HighscoreDatabase highscoreDatabaseObj = this.storage.getObject("highscore");
		if (highscoreDatabaseObj instanceof HighscoreDatabaseIC) {
			this.highscoreDatabase = (HighscoreDatabaseIC) highscoreDatabaseObj;
		} else {
			this.highscoreDatabase = new HighscoreDatabase();
		}

		this.levelDatabase = new LevelDatabase(currentActivity);
		this.screenSize = screenSize;
		this.gameEngine = new GameEngine(this);

		LevelHistory levelHistoryObj = this.storage.getObject(LevelHistory.SAVE_NAME);
		if (levelHistoryObj instanceof LevelHistoryIC) {
			this.levelHistory = (LevelHistoryIC) levelHistoryObj;
		} else {
			this.levelHistory = new LevelHistory();
		}


	}

	public static void make(Activity currentActivity, int mainViewID) {
		if (ControlResources.controlResources == null) {
			currentActivity.findViewById(mainViewID).getHeight();
			try {
				View mainView = currentActivity.findViewById(mainViewID);
				XYPoint screenSize = new XYPoint(mainView.getWidth(), mainView.getHeight());
				SensorManager sensorManager = (SensorManager) currentActivity.getSystemService(Context.SENSOR_SERVICE);
				ControlResources.controlResources = new ControlResources(currentActivity, sensorManager, screenSize);
			} catch (Exception ex) {
			}
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
		return levelDatabase;
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

	public LevelHistoryIC getLevelHistory() {


		return this.levelHistory;
	}
}
