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

	public static final int UPDATE_FREQUENCY = 100;
	private final ControlResourcesIC controlResources;
	private final Oscillator oscillator;
	private final MotionDetectorIC motionDetector;
	private final XYPoint gameFieldSize;
	private LevelEngine currentLevel = null;
	private int currentSpeed;

	public GameEngine(ControlResourcesIC controlResources) {
		super(GameEngineIC.GameEngineEvent.class);


		this.controlResources = controlResources;
		this.motionDetector = controlResources.getMotionDetector();
		this.gameFieldSize = controlResources.getScreenSize();
		

		// Config the Oscillator to make 10 fps
		this.oscillator = new Oscillator(GameEngine.UPDATE_FREQUENCY, new Runnable() {
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
		if(this.currentLevel!=null) {
			this.currentLevel.step(this.motionDetector.getAngleByRadians(), this.currentSpeed);
			/** Include **/
			
			
			
		}
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
				this.currentLevel = new LevelEngine(level,this.gameFieldSize);
				this.currentSpeed = this.currentLevel.getPlayerSpeed();
			} catch (Exception ex) {
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
		if(this.currentLevel!=null) {
			return this.currentLevel.getLevelData().getLevelName();
		} else {
			return null;
		}
	}
	
	@Override
	public List<REPoint> getPlayerBody() {
		if (this.currentLevel != null) {
			return this.currentLevel.clonePlayerBody();
		} else {
			return new ArrayList<REPoint>(0);
		}
	}
	
	@Override
	public List<REPoint> getItems() {
		if (this.currentLevel != null) {
			return this.currentLevel.cloneItemList();
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
	public Object getLevelMetaData() {
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
