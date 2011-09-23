/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.snake.gameGUI;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import java.util.List;
import se.chalmers.snake.R;
import se.chalmers.snake.interfaces.GameEngineIC;
import se.chalmers.snake.interfaces.GameEngineIC.GameEngineEvent;
import se.chalmers.snake.interfaces.util.REPoint;
import se.chalmers.snake.util.EnumObservable;
import se.chalmers.snake.util.EnumObserver;

/**
 *
 * @author 
 */
public class GameView extends View implements EnumObserver<GameEngineIC.GameEngineEvent, Void, Void> {

	private GameEngineIC gameEngine;
	private Paint paint;
	private List<REPoint> snakeBody;
	private List<REPoint> items;
	private List<REPoint> walls;
	private Resources mRes;
	private static Bitmap bodySeg;
	//Bör implementeras på GameEngine
	public static enum GameState{
		PAUSED,
		RUNNING
	};
	public GameState gameState;
	
	public GameView(Context context, GameEngineIC gameEngine) {
		super(context);
		mRes = context.getResources();
		this.paint = new Paint();
		this.paint.setStyle(Paint.Style.FILL);
		this.gameEngine = gameEngine;

		this.setBackgroundResource(R.drawable.spelplan_bg);
		
		gameEngine.addObserver(GameEngineIC.GameEngineEvent.START_GAME, this);
		gameEngine.addObserver(GameEngineIC.GameEngineEvent.PAUSE_GAME, this);
		gameEngine.addObserver(GameEngineIC.GameEngineEvent.UPDATE, this);
		this.snakeBody = gameEngine.getPlayerBody();
		this.items = gameEngine.getItems();
		this.walls = gameEngine.getObstacles();

		bodySeg = Bitmap.createScaledBitmap(
				BitmapFactory.decodeResource(mRes, R.drawable.snake_body), 5 * 2 , 5 * 2, true);
		
		this.postInvalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		this.gameEngine.restartGame();
		this.gameEngine.startGame();
		return super.onTouchEvent(event);

	}

	public void pauseGame(){
		gameEngine.pauseGame();
		gameState = GameState.PAUSED;
	}
	
	public void startGame(){
		gameEngine.startGame();
		gameState = GameState.RUNNING;
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		if (this.snakeBody != null) {
			for (REPoint reP : this.snakeBody) {
				canvas.drawBitmap(bodySeg, 
						reP.x - reP.radius, reP.y - reP.radius,null);
			}
		}
		if (this.items != null) {
			paint.setColor(Color.RED);
			for (REPoint reP : this.items) {
				canvas.drawCircle(reP.x, reP.y, reP.radius, this.paint);
			}
		}

		if (this.walls != null) {
			paint.setColor(Color.BLACK);
			for (REPoint reP : this.walls) {
				canvas.drawCircle(reP.x, reP.y, reP.radius, this.paint);
			}
		}

	}

	public Void observerNotify(EnumObservable<GameEngineEvent, Void, Void> observable, GameEngineEvent event, Void arg) {
		this.snakeBody = this.gameEngine.getPlayerBody();
		this.items = this.gameEngine.getItems();
		this.postInvalidate();
		return null;
	}

}
