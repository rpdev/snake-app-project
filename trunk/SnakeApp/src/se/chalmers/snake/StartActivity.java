package se.chalmers.snake;

import android.app.Activity;

import se.chalmers.snake.gameengine.TestGameEngine;
import se.chalmers.snake.interfaces.util.XYPoint;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Button;
import se.chalmers.snake.gameGUI.GameView;
import se.chalmers.snake.interfaces.GameEngineIC;

public class StartActivity extends Activity { // implements SensorEventListener

	private FrameLayout gameView = null;
	private Button newGame;
	private View.OnClickListener newGameListener = new View.OnClickListener() {

		public void onClick(View v) {
			gameView = (FrameLayout) StartActivity.this.findViewById(R.id.spelplan);
			XYPoint xyPoint = new XYPoint(gameView.getWidth(), gameView.getHeight());
			GameEngineIC gameEngineIC = TestGameEngine.getGameEngine(xyPoint);
			StartActivity.this.setContentView(new GameView(StartActivity.this,gameEngineIC));
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