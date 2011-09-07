package se.chalmers.snake.gameengine;

import java.util.List;
import se.chalmers.snake.interfaces.GameEngineIC;
import se.chalmers.snake.interfaces.util.REPoint;
import se.chalmers.snake.util.EnumObserver;

/**
 *
 */
public class GameEngine implements GameEngineIC {

	
	public GameEngine(Object levelStore, int gameFieldWidth, int gameFieldHeight) {
		
		
		
		
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
	public boolean addObserver(GameEngineEvent key, EnumObserver<GameEngineEvent, Void, Void> observer) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getGameFieldWidth() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getGameFieldHeight() {
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
	public boolean removeObserver(EnumObserver<GameEngineEvent, Void, Void> observer) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean removeObserver(GameEngineEvent key, EnumObserver<GameEngineEvent, Void, Void> observer) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
