package se.chalmers.snake;

import se.chalmers.snake.mastercontroller.*;
import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import se.chalmers.snake.gameGUI.Primary;
import se.chalmers.snake.gameengine.GameEngine;
import se.chalmers.snake.gameengine.TestGameEngine;
import se.chalmers.snake.interfaces.util.XYPoint;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Button;

public class StartActivity extends Activity { // implements SensorEventListener

	private ControlResources controlResources;
	private FrameLayout spelplan = null;
	private GameEngine gameEngine;
	private Primary primary;
	private Button newGame;
	private Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.snake_layout);
		context = this;

		newGame = (Button) findViewById(R.id.newGame_button);
		newGame.setOnClickListener(newGameListener);


	}
	private View.OnClickListener newGameListener = new View.OnClickListener() {

		public void onClick(View v) {
			findViewById(R.id.menu_buttons).setVisibility(View.INVISIBLE);
			spelplan = (FrameLayout) findViewById(R.id.spelplan);
			XYPoint xyPoint = new XYPoint(spelplan.getWidth(), spelplan.getHeight());
			controlResources = new ControlResources((SensorManager) getSystemService(SENSOR_SERVICE), xyPoint);
			primary = new Primary(controlResources, spelplan, context);

//	        //test
			TestGameEngine testGameEngine = new TestGameEngine();
			primary.setTestObjects(testGameEngine.getPlayerBody(), testGameEngine);
			testGameEngine.startGame();
//
//			gameEngine = (GameEngine)controlResources.getGameEngine();
//			
//			gameEngine.loadLevel("level 1"); //GameEngineEvent.NEW_GAME
//		
//			gameEngine.startGame(); // GameEngineEvent.START_GAME



		}
	};
}
