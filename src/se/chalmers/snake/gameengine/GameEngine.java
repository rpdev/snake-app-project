package se.chalmers.snake.gameengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import se.chalmers.snake.interfaces.ControlResourcesIC;
import se.chalmers.snake.interfaces.GameEngineIC;
import se.chalmers.snake.interfaces.MotionDetectorIC;
import se.chalmers.snake.interfaces.util.REPoint;
import se.chalmers.snake.interfaces.util.XYPoint;
import se.chalmers.snake.util.EnumObservable;

/**
 *
 */
public class GameEngine extends EnumObservable<GameEngineIC.GameEngineEvent, Void, Void> implements GameEngineIC {

	private final ControlResourcesIC controlResources;
	private final Oscillator oscillator;
	private final MotionDetectorIC motionDetector;
	private final XYPoint gameFieldSize;
	private LevelStore currentLevel = null;

	private class LevelStore {

		private int score;
		private final Object level;
		private final PlayerBody playerBody;
		private final ArrayList<REPoint> items;
		private final List<REPoint> staticElement;

		private LevelStore(Object level) {
			this.level = level;
			this.items = new ArrayList<REPoint>(5);
			this.playerBody = new PlayerBody(null, null, 0.0, 5, 5, 0);


			this.staticElement = Collections.unmodifiableList(this.listStaticElement(level));


		}

		private List<REPoint> listStaticElement(Object level) {
			ArrayList<REPoint> alRE = new ArrayList<REPoint>();

			return alRE;
		}

		private List<REPoint> clonePlayerBody() {
			ArrayList al = null;
			synchronized (this.playerBody) {
				al = new ArrayList(this.playerBody.size());
				al.addAll(this.playerBody);
			}
			return al;
		}

		private List<REPoint> cloneItemList() {
			ArrayList al = null;
			synchronized (this.items) {
				al = new ArrayList(this.items.size());
				al.addAll(this.items);
			}
			return al;
		}
	}

	public GameEngine(ControlResourcesIC controlResources) {
		super(GameEngineIC.GameEngineEvent.class);


		this.controlResources = controlResources;
		this.motionDetector = controlResources.getMotionDetector();
		this.gameFieldSize = controlResources.getScreenSize();
		// Config the Oscillator to make 10 fps
		this.oscillator = new Oscillator(100, new Runnable() {

			@Override
			public void run() {
				GameEngine.this.step();
			}
		});

	}

	private void step() {
		if (this.currentLevel != null && this.currentLevel.playerBody != null && motionDetector != null) {
			this.currentLevel.playerBody.step(motionDetector.getAngleByRadians());
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
		this.fireObserver(GameEngineEvent.NEW_GAME);
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getScore() {
		return this.currentLevel.score;
	}

	@Override
	public String getLevelName() {
		throw new UnsupportedOperationException("Not supported yet.");
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
			return this.currentLevel.staticElement;
		} else {
			return new ArrayList<REPoint>(0);
		}
	}

	@Override
	public Object getLevelMetaData() {
		if (this.currentLevel != null) {
			return this.currentLevel.level;
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
}
