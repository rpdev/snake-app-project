package se.chalmers.snake.gameengine;

import java.util.List;
import se.chalmers.snake.interfaces.GameEngineIC;
import se.chalmers.snake.interfaces.util.REPoint;
import se.chalmers.snake.interfaces.util.XYPoint;
import se.chalmers.snake.util.EnumObservable;
import se.chalmers.snake.util.EnumObserver;

/**
 *
 */
public class GameEngine extends EnumObservable<GameEngineIC.GameEngineEvent, Void,Void> implements GameEngineIC {
	private  PlayerBody playerBody;
	private final XYPoint gameFieldSize;
	private Object loadLevel;
	
	public GameEngine(Object levelStore, int gameFieldWidth, int gameFieldHeight) {
		super(GameEngineIC.GameEngineEvent.class);
		this.gameFieldSize = new XYPoint(gameFieldWidth, gameFieldHeight);

		
	}

	@Override
	public void startGame() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void pauseGame() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean loadLevel(String name) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getScore() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String getLevelName() {
		throw new UnsupportedOperationException("Not supported yet.");
	}



	@Override
	public List<REPoint> getPlayerBody() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<REPoint> getItems() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<REPoint> getStaticElement() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Object getLevelMetaData() {
		throw new UnsupportedOperationException("Not supported yet.");
	}


	@Override
	public XYPoint getGameFieldSize() {
		return this.gameFieldSize;
	}
}
