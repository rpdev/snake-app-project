package se.chalmers.snake.gameengine;

import java.util.ArrayList;
import java.util.List;
import se.chalmers.snake.interfaces.ControlResourcesIC;
import se.chalmers.snake.interfaces.GameEngineIC;
import se.chalmers.snake.interfaces.LevelIC;
import se.chalmers.snake.interfaces.MotionDetectorIC;
import se.chalmers.snake.interfaces.util.REPoint;
import se.chalmers.snake.interfaces.util.XYPoint;
import se.chalmers.snake.util.EnumObservable;

/**
 *
 */
public class GameEngine extends EnumObservable<GameEngineIC.GameEngineEvent, Void, Void> implements GameEngineIC {

	public static final int UPDATE_FREQUENCY = 1;
	private final ControlResourcesIC controlResources;
	private final Oscillator oscillator;
	private final MotionDetectorIC motionDetector;
	private final XYPoint gameFieldSize;
	private LevelEngine currentLevel = null;

	public GameEngine(ControlResourcesIC controlResources) {
		super(GameEngineIC.GameEngineEvent.class);


		this.controlResources = controlResources;
		this.motionDetector = controlResources.getMotionDetector();
		this.gameFieldSize = controlResources.getScreenSize();


		// Config the Oscillator to make 10 fps
		this.oscillator = new Oscillator(1000 / GameEngine.UPDATE_FREQUENCY, new Runnable() {

			@Override
			public void run() {
				GameEngine.this.step();
			}
		});

	}

	/**
	 * Driv the game 1 step further on.
	 */
	private void step() {
		if (this.currentLevel != null) {
			if (this.currentLevel.step(this.motionDetector.getAngleByRadians())) {
				if (this.currentLevel.hasReachedGoal()) {
					this.pauseGame();
					this.fireObserver(GameEngineEvent.LEVEL_END);
				} else {
					this.fireObserver(GameEngineEvent.UPDATE);
				}

			} else {
				this.pauseGame();
				this.fireObserver(GameEngineEvent.PLAYER_LOSE);
			}
		}
		/*
		System.out.println(" -- STEP -- ");
		System.out.println(this.currentLevel.playerBody);
		*/
	}

	@Override
	public synchronized boolean startGame() {
		if (this.currentLevel != null) {
			this.fireObserver(GameEngineEvent.START_GAME);
			this.oscillator.start();
			
			return true;
		} else {
			return false;
		}
	}

	@Override
	public synchronized boolean pauseGame() {
		if (this.currentLevel != null) {
			this.oscillator.stop();
			this.fireObserver(GameEngineEvent.PAUSE_GAME);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public synchronized boolean restartGame() {
		if (this.currentLevel != null) {
			this.loadLevel(this.getLevelName());
			return true;
		} else {
			return false;
		}
	}

	@Override
	public synchronized boolean loadLevel(String name) {
		LevelIC level = this.controlResources.getLevelDatabase().getByName(name);
		if (level != null) {
			this.pauseGame();
			try {
				this.currentLevel = new LevelEngine(level, this.gameFieldSize);
			} catch (Exception ex) {
				ex.printStackTrace();
				return false;
			}
			this.fireObserver(GameEngineEvent.NEW_GAME);
			return true;
		} else {
			return false;
		}
	}

	//<editor-fold defaultstate="collapsed" desc="Override Methods">
	@Override
	public int getScore() {
		return this.currentLevel.getScore();
	}

	@Override
	public String getLevelName() {
		if (this.currentLevel != null) {
			return this.currentLevel.getLevelData().getLevelName();
		} else {
			return null;
		}
	}

	@Override
	public List<REPoint> getPlayerBody() {
		if (this.currentLevel != null) {
			return this.currentLevel.getPlayerBody();
		} else {
			return new ArrayList<REPoint>(0);
		}
	}

	@Override
	public List<REPoint> getItems() {
		if (this.currentLevel != null) {
			return this.currentLevel.getItemsList();
		} else {
			return new ArrayList<REPoint>(0);
		}
	}

	@Override
	public List<REPoint> getStaticElement() {
		if (this.currentLevel != null) {
			return this.currentLevel.getStaticElement();
		} else {
			return new ArrayList<REPoint>(0);
		}
	}

	@Override
	public LevelIC getLevelMetaData() {
		if (this.currentLevel != null) {
			return this.currentLevel.getLevelData();
		} else {
			return null;
		}
	}

	@Override
	public XYPoint getGameFieldSize() {
		return this.gameFieldSize;
	}

	@Override
	public boolean isLevelLoad() {
		return this.currentLevel != null;
	}
	//</editor-fold>
}
