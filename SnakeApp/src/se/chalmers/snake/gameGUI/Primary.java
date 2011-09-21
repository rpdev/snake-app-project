package se.chalmers.snake.gameGUI;

import se.chalmers.snake.gameengine.TestGameEngine;
import se.chalmers.snake.interfaces.ControlResourcesIC;
import se.chalmers.snake.interfaces.GameEngineIC;
import se.chalmers.snake.interfaces.GameEngineIC.GameEngineEvent;
import se.chalmers.snake.interfaces.util.REPoint;
import se.chalmers.snake.util.EnumObservable;
import se.chalmers.snake.util.EnumObserver;

import java.util.List;

import android.content.Context;
import android.widget.FrameLayout;
import se.chalmers.snake.leveldatabase.LevelDatabase;

public class Primary implements EnumObserver<GameEngineIC.GameEngineEvent, Void,Void>{
	
	private GameEngineIC gameEngine;
	private FrameLayout spelplan;
	private List<REPoint> snakeBody;
	private Context context;
	
	public Primary(ControlResourcesIC controlResources, FrameLayout spelplan, Context context){
		this.gameEngine = controlResources.getGameEngine();
		this.snakeBody = gameEngine.getPlayerBody();
		this.spelplan = spelplan;
		this.context = context;

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
		if(event == GameEngineIC.GameEngineEvent.UPDATE){
			for(REPoint point : snakeBody){
				spelplan.addView(new BodySegment(context, point.getX(), point.getY(), point.getRadius()));
			}
		}
		return null;
	}
	
	/**
	 * test
	 */
	public void setTestObjects(List<REPoint> pb, TestGameEngine ts){
		this.snakeBody = pb;
		ts.addObserver(GameEngineIC.GameEngineEvent.UPDATE, this);
		for(REPoint point : snakeBody){
			spelplan.addView(new BodySegment(context, point.getX(), point.getY(), point.getRadius()));
		}
	}
}
