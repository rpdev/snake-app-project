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
 * Make a new GameEngine of this game call "Snake App"
 */
public class GameEngine extends EnumObservable<GameEngineIC.GameEngineEvent, Void, Void> implements GameEngineIC {

	public static final int UPDATE_FREQUENCY = 16;
	private static final double PI_TIMES_2 = Math.PI * 2;
	private final ControlResourcesIC controlResources;
	private final Oscillator oscillator;
	private final MotionDetectorIC motionDetector;
	private LevelEngine currentLevel = null;
	private boolean isRun = false;
	private double currentAngle;
	private GameEngineStatus currentStatus;
	private int startScore;

	/**
	 * The gameEngine nead a ControlResourcesIC class for get some metadata from the system and 
	 * link recuses.
	 * @param controlResources 
	 */
	public GameEngine(ControlResourcesIC controlResources) {
		super(GameEngineIC.GameEngineEvent.class);
		this.currentStatus = GameEngineStatus.NO_LEVEL_LOAD;
		this.startScore = 0;
		this.controlResources = controlResources;
		this.motionDetector = controlResources.getMotionDetector();
		this.isRun = false;
		this.currentAngle = 0;

		// Config the Oscillator to make 16 fps
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
			double newAngle = this.motionDetector.getAngleByRadians();

			if (0 <= newAngle && newAngle <= PI_TIMES_2) {
				double P = this.currentAngle - newAngle;
				double M = ((newAngle < this.currentAngle) ? -PI_TIMES_2 : PI_TIMES_2) + P;
				newAngle += ((Math.abs(P) < Math.abs(M) ? P : M)) * 0.65;
				if (this.currentLevel.step(newAngle)) {
					this.currentAngle = newAngle;
					if (this.currentLevel.hasReachedGoal()) {
						System.out.println("GameEngine Run - Level End - Goal");
						this.pauseGame();
						this.currentStatus = GameEngineStatus.LEVEL_END;
						this.fireObserver(GameEngineEvent.LEVEL_END);
					} else {
						this.fireObserver(GameEngineEvent.UPDATE);

					}

				} else {
					this.pauseGame();
					System.out.println("GameEngine Run - Level End - Player Lose");
					this.currentStatus = GameEngineStatus.LEVEL_END;
					this.fireObserver(GameEngineEvent.PLAYER_LOSE);

				}
			}
		} else {
		}
	}

	@Override
	public synchronized boolean startGame() {
		if (this.currentLevel != null) {
			this.currentStatus = GameEngineStatus.RUNNING;
			this.fireObserver(GameEngineEvent.START_GAME);
			this.oscillator.start(); // Start the Oscillator
			this.motionDetector.start(); // Start the Motion Detector.
			this.isRun = true;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public synchronized boolean pauseGame() {
		if (this.currentLevel != null && this.isRun == true) {
			this.oscillator.stop();
			this.motionDetector.stop();
			this.currentStatus = GameEngineStatus.PAUSE;
			this.fireObserver(GameEngineEvent.PAUSE_GAME);
			this.isRun = false;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public synchronized boolean restartGame() {
		if (this.currentLevel != null) {
			return this.loadLevel(this.currentLevel.getLevelData(), false);
		}
		return false;
	}

	@Override
	public synchronized boolean loadLevel(String name) {
		LevelIC level = this.controlResources.getLevelDatabase().getByName(name);
		return this.loadLevel(level, true);
	}

	private boolean loadLevel(LevelIC level, boolean newGame) {
		if (level != null) {
			this.pauseGame();
			try {
				this.currentLevel = new LevelEngine(level, this.controlResources.getScreenSize());
				this.currentAngle = this.currentLevel.getLevelData().getStartAngle();
			} catch (Exception ex) {
				return false;
			}
			this.currentStatus = GameEngineStatus.NEW_LEVEL;
			if (newGame) {
				this.fireObserver(GameEngineEvent.NEW_GAME);
			} else {
				this.fireObserver(GameEngineEvent.RESTART_GAME);
			}
			return true;
		} else {
			return false;
		}
	}

	//<editor-fold defaultstate="collapsed" desc="Override Methods">
	@Override
	public int getScore() {
		return this.startScore + this.currentLevel.getScore();
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
	public REPoint getPlayerHead() {
		if (this.currentLevel != null) {
			return this.currentLevel.getPlayerHead();
		} else {
			return null;
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
	public List<REPoint> getObstacles() {
		if (this.currentLevel != null) {
			return this.currentLevel.getObstacles();
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
		return this.controlResources.getScreenSize();
	}

	@Override
	public boolean isLevelLoad() {
		return this.currentLevel != null;
	}
	//</editor-fold>

	@Override
	public boolean isRun() {
		return this.isRun;
	}

	@Override
	public GameEngineStatus getStatus() {
		return this.currentStatus;
	}

	@Override
	public int getItemRadius() {
		if (this.currentLevel != null) {
			return this.currentLevel.getItemsRadius();
		} else {
			return 0;
		}
	}

	@Override
	public void setStartScore(int score) {
		if (score >= 0) {
			this.startScore = score;
		}
	}
}
