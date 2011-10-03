package se.chalmers.snake.gameui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import java.util.List;
import se.chalmers.snake.R;
import se.chalmers.snake.StartActivity;
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
	private Bitmap bodySeg;
	private Bitmap apple;
	/**
	 * This will not work, the obstacle can has diffrent size on a map.
	 */
	private Bitmap obstacle;

	public GameView(Context context, GameEngineIC gameEngine) {
		super(context);
		this.mRes = context.getResources();
		this.paint = new Paint();
		this.paint.setStyle(Paint.Style.FILL);
		this.addGameEngine(gameEngine);
		this.setBackgroundResource(R.drawable.spelplan_bg);
		this.initLevel();
	}

	/**
	 * Methods that will be call for each new level.
	 */
	private void initLevel() {
		int playerBodyWidth = this.gameEngine.getPlayerHead().radius;
		int appleWidth = this.gameEngine.getItemRadius();
		this.bodySeg = Bitmap.createScaledBitmap(
				  BitmapFactory.decodeResource(this.mRes, R.drawable.snake_body), playerBodyWidth * 2, playerBodyWidth * 2, true);
		this.apple = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(mRes, R.drawable.apple),
				  appleWidth * 2, appleWidth * 2, true);
		this.walls = this.gameEngine.getObstacles();
		this.postInvalidate();
	}
	
	public void addGameEngine(GameEngineIC gameEngine) {
		this.gameEngine = gameEngine;
		gameEngine.addObserver(GameEngineIC.GameEngineEvent.START_GAME, this);
		gameEngine.addObserver(GameEngineIC.GameEngineEvent.PAUSE_GAME, this);
		gameEngine.addObserver(GameEngineIC.GameEngineEvent.UPDATE, this);
		gameEngine.addObserver(GameEngineIC.GameEngineEvent.PLAYER_LOSE, this);
		gameEngine.addObserver(GameEngineIC.GameEngineEvent.NEW_GAME, this);
		gameEngine.addObserver(GameEngineIC.GameEngineEvent.LEVEL_END, this);

		this.snakeBody = gameEngine.getPlayerBody();
		this.items = gameEngine.getItems();
		this.walls = gameEngine.getObstacles();

	}

	@Override
	public void onDraw(Canvas canvas) {
		if (this.snakeBody != null) {
			for (REPoint reP : this.snakeBody) {
				canvas.drawBitmap(bodySeg,
						  reP.x - reP.radius, reP.y - reP.radius, null);
			}
		}
		if (this.items != null) {
			paint.setColor(Color.RED);
			for (REPoint reP : this.items) {
				canvas.drawBitmap(apple, reP.x - reP.radius, reP.y - reP.radius, null);
			}
		}

		if (this.walls != null) {
			paint.setColor(Color.BLACK);
			for (REPoint reP : this.walls) {
				canvas.drawCircle(reP.x, reP.y, reP.radius, this.paint);
			}
		}

	}

	public Void observerNotify(EnumObservable<GameEngineEvent, Void, Void> observable,
			  GameEngineEvent event, Void arg) {
		this.snakeBody = this.gameEngine.getPlayerBody();
		this.items = this.gameEngine.getItems();
		if(event == GameEngineEvent.NEW_GAME) {
			
			this.initLevel();
		}
		
		this.postInvalidate();

		return null;
	}

	public void pauseGame() {
		this.gameEngine.pauseGame();
	}

	public void startGame() {
		this.gameEngine.startGame();
	}

	public boolean isRun() {
		return this.gameEngine.isRun();
	}

	public void restartGame() {
		this.gameEngine.restartGame();
	}

	public int getScore() {
		return this.gameEngine.getScore();
	}
}
