/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.snake.gameGUI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import java.util.ArrayList;
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

	public GameView(Context context, GameEngineIC gameEngine) {
		super(context);
		this.paint = new Paint();
		this.paint.setStyle(Paint.Style.FILL);
		this.gameEngine = gameEngine;
		this.snakeBody = new ArrayList(0);
		this.setBackgroundResource(R.drawable.spelplan_bg);
		gameEngine.addObserver(GameEngineIC.GameEngineEvent.UPDATE, this);
		this.gameEngine.startGame();
	}

	@Override
	public void onDraw(Canvas canvas) {
		if (this.snakeBody != null) {
			paint.setColor(Color.YELLOW);
			for (REPoint reP : this.snakeBody) {
				canvas.drawCircle(reP.x, reP.y, reP.radius, this.paint);
			}
			
			paint.setColor(Color.RED);
			for (REPoint reP : this.items) {
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
