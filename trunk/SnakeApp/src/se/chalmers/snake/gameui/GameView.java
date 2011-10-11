package se.chalmers.snake.gameui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
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
 * This is the GUI part of the game, and show the game map, and current game on the display
 * @author 
 */
public class GameView extends View implements EnumObserver<GameEngineIC.GameEngineEvent, Void, Void> {

	private class BitSet {

		private Bitmap bitmap;
		private REPoint point;

		private BitSet(Bitmap b, REPoint r) {
			this.bitmap = b;
			this.point = r;
		}
	}
	private GameEngineIC gameEngine;
	private Paint paint;
	private List<REPoint> snakeBody;
	private List<REPoint> items;
	private Resources mRes;
	private List<Bitmap> headSegs;
	private List<Bitmap> bodySegs;
	private List<Bitmap> tailSegs;
	private Bitmap apple;
	private List<BitSet> obstacles;
	private int score;

	/**
	 * Start up and config the GameView
	 * @param context The place that this GameView will put data into.
	 * @param gameEngine Take the GameEngine that will be use, are use for debug modes.
	 */
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


		this.bodySegs = this.getBodySegBitMap(R.drawable.snake_body1, playerBodyWidth);
		this.headSegs = this.getBodySegBitMap(R.drawable.snake_head2, playerBodyWidth);
		this.tailSegs = this.getBodySegBitMap(R.drawable.snake_tail2, playerBodyWidth);


		this.apple = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(mRes, R.drawable.apple),
				  appleWidth, appleWidth, true);

		List<REPoint> obstaclesPos = this.gameEngine.getObstacles();
		int totalObstacles = obstaclesPos.size();
		this.obstacles = new ArrayList<BitSet>();
		for (int i = 0; i < totalObstacles; i++) {
			REPoint bitsetKey = obstaclesPos.get(i);
			int obstacleWidths = bitsetKey.radius * 2;
			if (obstacleWidths > 0) {
				Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.mRes, R.drawable.obstacle),
						  obstacleWidths, obstacleWidths, true);
				this.obstacles.add(new BitSet(bitmap, bitsetKey));
			}

		}


		this.postInvalidate();
	}

	/**
	 * Make the BitMap Arrays for the snake body parts, this will be call with 3 tims.
	 * each body type.
	 * @param recID The id of the image resuser.
	 * @param playerBodyWidth
	 * @return 
	 */
	private List<Bitmap> getBodySegBitMap(int recID, int playerBodyWidth) {
		Bitmap bodySeg = Bitmap.createScaledBitmap(
				  BitmapFactory.decodeResource(this.mRes, recID), playerBodyWidth, playerBodyWidth, true);
		List<Bitmap> segs = new ArrayList<Bitmap>(90);
		for (int i = 0; i < 90; i++) {
			Matrix matrix = new Matrix();
			matrix.postRotate(i * 4, bodySeg.getWidth() / 2, bodySeg.getHeight() / 2);
			segs.add(Bitmap.createBitmap(bodySeg, 0, 0, playerBodyWidth, playerBodyWidth, matrix, true));
		}
		return segs;
	}

	/**
	 * Add the game engine, this will add the event observer for get events from the game engine.
	 * @param gameEngine 
	 */
	private void addGameEngine(GameEngineIC gameEngine) {
		this.gameEngine = gameEngine;
		gameEngine.addObserver(GameEngineIC.GameEngineEvent.START_GAME, this);
		gameEngine.addObserver(GameEngineIC.GameEngineEvent.PAUSE_GAME, this);
		gameEngine.addObserver(GameEngineIC.GameEngineEvent.UPDATE, this);
		gameEngine.addObserver(GameEngineIC.GameEngineEvent.PLAYER_LOSE, this);
		gameEngine.addObserver(GameEngineIC.GameEngineEvent.NEW_GAME, this);
		gameEngine.addObserver(GameEngineIC.GameEngineEvent.LEVEL_END, this);
		this.obstacles = new ArrayList<BitSet>();
	}

	/**
	 * Call while the windows will be redraw.
	 * @param canvas 
	 */
	@Override
	public void onDraw(Canvas canvas) {


		if (this.snakeBody != null) {
			for (REPoint reP : this.snakeBody) {
				int item = (int) ((reP.angle * (180.0 / Math.PI) / 4) % 90);
				if (item < 0) {
					item += 90;
				}

				switch (reP.type) {
					case HEADSEG: {
						canvas.drawBitmap(headSegs.get(item), reP.x - reP.radius, reP.y - reP.radius, null);
						break;
					}
					case BODYSEG: {
						canvas.drawBitmap(bodySegs.get(item), reP.x - reP.radius, reP.y - reP.radius, null);
						break;
					}
					case TAILSEG: {
						canvas.drawBitmap(tailSegs.get(item), reP.x - reP.radius, reP.y - reP.radius, null);
						break;
					}
				}
			}
		}
		if (this.items != null) {
			for (REPoint reP : this.items) {
				canvas.drawBitmap(apple, reP.x - reP.radius, reP.y - reP.radius, null);
			}
		}

		if (this.obstacles != null) {
			for (BitSet bitset : this.obstacles) {
				canvas.drawBitmap(bitset.bitmap, bitset.point.x - bitset.point.radius, bitset.point.y - bitset.point.radius, null);
			}
		}

		paint.setColor(Color.BLACK);
		canvas.drawText("Score: " + score, 10, 20, paint);

	}

	public Void observerNotify(EnumObservable<GameEngineEvent, Void, Void> observable,
			  GameEngineEvent event, Void arg) {
		this.snakeBody = this.gameEngine.getPlayerBody();
		this.items = this.gameEngine.getItems();
		this.score = this.gameEngine.getScore();
		if (event == GameEngineEvent.NEW_GAME) {
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
