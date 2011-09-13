package se.chalmers.snake.gameGUI;

import se.chalmers.snake.interfaces.ControlResourcesIC;
import se.chalmers.snake.interfaces.GameEngineIC;
import se.chalmers.snake.interfaces.GameEngineIC.GameEngineEvent;
import se.chalmers.snake.interfaces.util.REPoint;
import se.chalmers.snake.util.EnumObservable;
import se.chalmers.snake.util.EnumObserver;

import java.util.List;

public class Primary implements EnumObserver<GameEngineIC.GameEngineEvent, Void,Void>{
	
	private GameEngineIC gameEngine;
	private List<REPoint> snakeBody;
	
	public Primary(ControlResourcesIC controlResources){
		this.gameEngine = controlResources.getGameEngine();
		this.snakeBody = gameEngine.getPlayerBody();

		this.gameEngine.addObserver(GameEngineIC.GameEngineEvent.NEW_GAME,this);
		this.gameEngine.addObserver(GameEngineIC.GameEngineEvent.START_GAME,this);
		this.gameEngine.addObserver(GameEngineIC.GameEngineEvent.PAUSE_GAME,this);
		this.gameEngine.addObserver(GameEngineIC.GameEngineEvent.UPDATE,this);
		this.gameEngine.addObserver(GameEngineIC.GameEngineEvent.LEVEL_END,this);
		this.gameEngine.addObserver(GameEngineIC.GameEngineEvent.PLAYER_LOSE,this);
	}

	@Override
	public Void observerNotify(
			EnumObservable<GameEngineEvent, Void, Void> observable,
			GameEngineEvent event, Void arg) {
		// TODO Auto-generated method stub
		return null;
	}
}
