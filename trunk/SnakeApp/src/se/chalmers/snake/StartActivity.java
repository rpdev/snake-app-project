package se.chalmers.snake;

import android.app.Activity;

import android.content.Context;
import android.hardware.SensorManager;
import se.chalmers.snake.gameengine.TestGameEngine;
import se.chalmers.snake.interfaces.util.XYPoint;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Button;
import se.chalmers.snake.gameGUI.GameView;
import se.chalmers.snake.interfaces.GameEngineIC;
import se.chalmers.snake.motiondetector.MotionDetector;

public class StartActivity extends Activity { // implements SensorEventListener

	private FrameLayout gameView = null;
	private Button newGame;
	private View.OnClickListener newGameListener = new View.OnClickListener() {

		public void onClick(View v) {
			gameView = (FrameLayout) StartActivity.this.findViewById(R.id.spelplan);
			XYPoint xyPoint = new XYPoint(gameView.getWidth(), gameView.getHeight());
			SensorManager mSensorManager = (SensorManager)StartActivity.this.getSystemService(Context.SENSOR_SERVICE);
			GameEngineIC gameEngineIC = TestGameEngine.getGameEngine(xyPoint,null);
			//gameEngineIC = TestGameEngine.getGameEngine(xyPoint,new MotionDetector(mSensorManager));
			//GameEngineIC gameEngineIC = TestGameEngine.getGameEngine(xyPoint,null);
			StartActivity.this.setContentView(new GameView(StartActivity.this,gameEngineIC));
			gameEngineIC.startGame();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.snake_layout);
		newGame = (Button) findViewById(R.id.newGame_button);
		newGame.setOnClickListener(newGameListener);


	}
}
