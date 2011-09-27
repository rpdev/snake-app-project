package se.chalmers.snake.gameui;

import se.chalmers.snake.interfaces.ControlResourcesIC;
import se.chalmers.snake.interfaces.GameEngineIC;
import se.chalmers.snake.interfaces.GameEngineIC.GameEngineEvent;
import se.chalmers.snake.interfaces.util.REPoint;
import se.chalmers.snake.util.EnumObservable;
import se.chalmers.snake.util.EnumObserver;

import java.util.List;

import android.content.Context;
import android.widget.FrameLayout;
import se.chalmers.snake.gameengine.TestGameEngine;

public class Primary implements EnumObserver<GameEngineIC.GameEngineEvent, Void, Void> {

	private GameEngineIC gameEngine;
	private FrameLayout gameView;
	//private List<REPoint> snakeBody;
	private Context context;

	public Primary(ControlResourcesIC controlResources, FrameLayout gameView, Context context) {


		//this.gameEngine = TestGameEngine.getGameEngine(); // Replace this with this line below.
		//this.gameEngine = controlResources.getGameEngine();
		//this.snakeBody = gameEngine.getPlayerBody();
		this.gameView = gameView;
		this.context = context;

		//this.gameEngine.addObserver(GameEngineIC.GameEngineEvent.NEW_GAME,this);
		this.gameEngine.addObserver(GameEngineIC.GameEngineEvent.START_GAME, this);
		this.gameEngine.addObserver(GameEngineIC.GameEngineEvent.PAUSE_GAME, this);
		this.gameEngine.addObserver(GameEngineIC.GameEngineEvent.UPDATE, this);
		//this.gameEngine.addObserver(GameEngineIC.GameEngineEvent.LEVEL_END,this);
		//this.gameEngine.addObserver(GameEngineIC.GameEngineEvent.PLAYER_LOSE,this);
		this.gameEngine.startGame();


	}

	@Override
	public Void observerNotify(
			  EnumObservable<GameEngineEvent, Void, Void> observable,
			  GameEngineEvent event, Void arg) {
		List<REPoint> snakeBody = this.gameEngine.getPlayerBody();
		System.out.println("Event:" + event + " Size:" + snakeBody.size());
		gameView.removeAllViews();
		for (REPoint point : snakeBody) {
			gameView.addView(new BodySegment(context, point.getX(), point.getY(), point.getRadius()));
		}
		
		gameView.invalidate();
		return null;
	}
	/*
	public void setTestObjects(List<REPoint> pb, GameEngineIC ts){
	this.snakeBody = pb;
	ts.addObserver(GameEngineIC.GameEngineEvent.UPDATE, this);
	for(REPoint point : snakeBody){
	spelplan.addView(new BodySegment(context, point.getX(), point.getY(), point.getRadius()));
	}
	}
	 */
}
