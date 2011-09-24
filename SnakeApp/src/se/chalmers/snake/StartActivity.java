package se.chalmers.snake;

import android.app.Activity;

import android.content.Context;
import android.hardware.SensorManager;
import se.chalmers.snake.gameengine.TestGameEngine;
import se.chalmers.snake.interfaces.util.XYPoint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import se.chalmers.snake.gameGUI.GameView;
import se.chalmers.snake.highscoreDatabase.HighscoreDatabase;
import se.chalmers.snake.interfaces.GameEngineIC;
import se.chalmers.snake.interfaces.GameEngineIC.GameEngineEvent;
import se.chalmers.snake.motiondetector.MotionDetector;
import se.chalmers.snake.util.EnumObservable;
import se.chalmers.snake.util.EnumObserver;

public class StartActivity extends Activity implements EnumObserver<GameEngineIC.GameEngineEvent, Void, Void> { // implements SensorEventListener

	private RelativeLayout gameHolder;
	private GameView gameView;
	private HighscoreDatabase highData;
	private GameEngineIC gameEngineIC;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.snake_layout);

		highData = new HighscoreDatabase();

		//Instantiate layouts
		background = (RelativeLayout)findViewById(R.id.backgroundImage);
		menu = (LinearLayout)findViewById(R.id.menu_buttons);

		//Instantiate buttons
		newGame = (Button) findViewById(R.id.newGame_button);
		resumeGame = (Button) findViewById(R.id.resumeGame_button);
		help = (Button) findViewById(R.id.help_button);
		highscore = (Button) findViewById(R.id.highscore_button);

		//Instantiate texts
		helpText = (TextView) findViewById(R.id.helpText);
		highscoreText = (TextView) findViewById(R.id.highscoreText);
		back = (Button) findViewById(R.id.back_button);
		
		switchToStartMenu();
		show();
		newGame.setOnClickListener(newGameListener);
		resumeGame.setOnClickListener(resumeGameListener);
		help.setOnClickListener(helpListener);
		highscore.setOnClickListener(highscoreListener);
		back.setOnClickListener(backListener);


	}
	
	/**
	 * 
	 * Takes care of the Menu
	 * 
	 */
	

	private RelativeLayout background;
	private LinearLayout menu;
	//Buttons
	private Button newGame;
	private Button resumeGame;
	private Button help;
	private Button highscore;
	private Button back;
	//Texts
	private TextView helpText;
	private TextView highscoreText;
	//Info
	private boolean hidden;
	private enum MenuState{
		//menuState possibilities
		onStartMenu,
		onPauseMenu,
		onHighscoreMenu,
		onHelpMenu
	};
	private MenuState menuState; 
	private MenuState previousMenuState; 
	

	public void show(){
		menu.setVisibility(View.VISIBLE);
		hidden = false;
	}
	
	public void hide(){
		menu.setVisibility(View.GONE);
		background.setBackgroundColor(0x00000000);
		hidden = true;
	}
	
	public void switchToStartMenu(){
		previousMenuState = menuState;
		menuState = MenuState.onStartMenu;
		//These components should be present
		background.setBackgroundResource(R.drawable.snake_bg);
		menu.setVisibility(View.VISIBLE);
		newGame.setVisibility(View.VISIBLE);
		help.setVisibility(View.VISIBLE);
		highscore.setVisibility(View.VISIBLE);
		//These shouldn't
		resumeGame.setVisibility(View.GONE);
		back.setVisibility(View.GONE);
		helpText.setVisibility(View.GONE);
		highscoreText.setVisibility(View.GONE);
	}
	
	public void switchToPauseMenu(){
		previousMenuState = menuState;
		menuState = MenuState.onPauseMenu;
		//These components should be present
		menu.setVisibility(View.VISIBLE);
		resumeGame.setVisibility(View.VISIBLE);
		newGame.setVisibility(View.VISIBLE);
		help.setVisibility(View.VISIBLE);
		highscore.setVisibility(View.VISIBLE);
		//These shouldn't
		background.setBackgroundColor(0x00000000);
		helpText.setVisibility(View.GONE);
		highscoreText.setVisibility(View.GONE);
		back.setVisibility(View.GONE);
	}
	
	public void switchToHighscoreMenu(){
		previousMenuState = menuState;
		menuState = MenuState.onHighscoreMenu;
		//These components should be present
		menu.setVisibility(View.VISIBLE);
		back.setVisibility(View.VISIBLE);
		highscoreText.setVisibility(View.VISIBLE);
		//These shouldn't
		resumeGame.setVisibility(View.GONE);
		help.setVisibility(View.GONE);
		highscore.setVisibility(View.GONE);
		newGame.setVisibility(View.GONE);
		helpText.setVisibility(View.GONE);
	}
	
	public void switchToHelpMenu(){
		previousMenuState = menuState;
		menuState = MenuState.onHelpMenu;
		//These components should be present
		menu.setVisibility(View.VISIBLE);
		back.setVisibility(View.VISIBLE);
		helpText.setVisibility(View.VISIBLE);
		//These shouldn't
		resumeGame.setVisibility(View.GONE);
		help.setVisibility(View.GONE);
		highscore.setVisibility(View.GONE);
		newGame.setVisibility(View.GONE);
		highscoreText.setVisibility(View.GONE);
	}
	
	private OnClickListener backListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(previousMenuState){
			case onStartMenu: switchToStartMenu(); break;
			case onPauseMenu: switchToPauseMenu(); break;
			default : break;
			}
			
		}
	};

	private View.OnClickListener newGameListener = new View.OnClickListener() {

		public void onClick(View v) {
			FrameLayout spelplan = (FrameLayout) StartActivity.this.findViewById(R.id.spelplan);
			XYPoint xyPoint = new XYPoint(spelplan.getWidth(), spelplan.getHeight());
			
			SensorManager mSensorManager = (SensorManager)StartActivity.this.getSystemService(Context.SENSOR_SERVICE);
			gameEngineIC = TestGameEngine.getGameEngine(xyPoint,null);
			gameEngineIC = TestGameEngine.getGameEngine(xyPoint,new MotionDetector(mSensorManager));
			if(gameView == null){
				gameView = new GameView(StartActivity.this,gameEngineIC);
				gameEngineIC.addObserver(GameEngineIC.GameEngineEvent.PLAYER_LOSE, StartActivity.this);
				gameHolder = (RelativeLayout)findViewById(R.id.gameViewHolder);
				gameHolder.addView(gameView);
			}
			else {
				//gameView.addGameEngine(gameEngineIC);
				//gameEngineIC.addObserver(GameEngineIC.GameEngineEvent.PLAYER_LOSE, StartActivity.this);
			}
			gameEngineIC.startGame();
			hide();
		}
	};

	private View.OnClickListener resumeGameListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			gameView.startGame();
//			findViewById(R.id.menu_buttons).setVisibility(View.GONE);
			hide();
		}
	};

	private View.OnClickListener helpListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switchToHelpMenu();
			show();
		}
	};

	private View.OnClickListener highscoreListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			highscoreText.setText(highData.toString());
			switchToHighscoreMenu();
			show();
		}
	};

	@Override
	public boolean onKeyDown(int keycode, KeyEvent event ) {
	 if(keycode == KeyEvent.KEYCODE_MENU){

			gameView.pauseGame();
			switchToPauseMenu();
			show();
	 }
	 return super.onKeyDown(keycode,event);  
	}

	@Override
	public Void observerNotify(
			EnumObservable<GameEngineEvent, Void, Void> observable,
			GameEngineEvent event, Void arg) {
		highData.addPlayerToHighscore("Auto", 5);
		return null;
	}
	
}
