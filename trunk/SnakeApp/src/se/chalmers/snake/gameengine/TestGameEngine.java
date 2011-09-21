package se.chalmers.snake.gameengine;

import java.util.ArrayList;
import java.util.List;

import se.chalmers.snake.interfaces.GameEngineIC;
import se.chalmers.snake.interfaces.GameEngineIC.GameEngineEvent;
import se.chalmers.snake.interfaces.util.REPoint;
import se.chalmers.snake.util.EnumObservable;

public class TestGameEngine extends EnumObservable<GameEngineIC.GameEngineEvent, Void, Void>{
	
	private List<REPoint> snakeBody;
	
	public TestGameEngine(){
		super(GameEngineIC.GameEngineEvent.class);
		snakeBody = new ArrayList<REPoint>();

		snakeBody.add(new REPoint(REPoint.REType.HEADSEG,30,5,5));
		snakeBody.add(new REPoint(REPoint.REType.BODYSEG,25,5,5));
		snakeBody.add(new REPoint(REPoint.REType.BODYSEG,20,5,5));
		snakeBody.add(new REPoint(REPoint.REType.BODYSEG,15,5,5));
		snakeBody.add(new REPoint(REPoint.REType.TAILSEG,10,5,5));
	}
	
	public void startGame(){
				List<REPoint> snake = new ArrayList<REPoint>();
				for(REPoint point : snakeBody){
					snake.add(new REPoint(point.getType(),point.getX() + 40,5,5));
				}
				snakeBody = snake;
				this.fireObserver(GameEngineEvent.UPDATE);
		
	}
	
	public List<REPoint> getPlayerBody(){
		return snakeBody;
	}

}
