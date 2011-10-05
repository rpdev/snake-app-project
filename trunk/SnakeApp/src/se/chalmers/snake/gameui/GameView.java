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
	private Bitmap[] obstacles;
	private int score;

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
		int playerBodyWidth = this.gameEngine.getPlayerHead().radius * 2;
		int appleWidth = this.gameEngine.getItemRadius() * 2;
		int totalObstacles = this.gameEngine.getObstacles().size();
		
		int[] obstacleWidths = new int[totalObstacles];
		for(int i = 0; i < totalObstacles; i++){
			obstacleWidths[i] = this.gameEngine.getObstacles().get(i).radius * 2;
		}
		
		this.bodySeg = Bitmap.createScaledBitmap(
				  BitmapFactory.decodeResource(this.mRes, R.drawable.snake_body), playerBodyWidth, playerBodyWidth, true);
		this.apple = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(mRes, R.drawable.apple),
				  appleWidth , appleWidth, true);
		obstacles = new Bitmap[totalObstacles];
		for(int i = 0; i < totalObstacles; i++){
			obstacles[i] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(mRes, R.drawable.obstacle),
					  obstacleWidths[i], obstacleWidths[i], true);
		}
		
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
			for (REPoint reP : this.items) {
				canvas.drawBitmap(apple, reP.x - reP.radius, reP.y - reP.radius, null);
			}
		}

		if (this.walls != null) {
			for(int i = 0; i < this.walls.size(); i++){
				REPoint reP = this.walls.get(i);
				canvas.drawBitmap(obstacles[i], reP.x - reP.radius, reP.y - reP.radius, null);
			}
		}
		
		paint.setColor(Color.BLACK);
		canvas.drawText("Score: "+score, 10,20, paint);

	}

	public Void observerNotify(EnumObservable<GameEngineEvent, Void, Void> observable,
			  GameEngineEvent event, Void arg) {
		this.snakeBody = this.gameEngine.getPlayerBody();
		this.items = this.gameEngine.getItems();
		this.score = this.gameEngine.getScore();
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
