package se.chalmers.snake;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import se.chalmers.snake.highscoreDatabase.HighscoreDatabase;
import se.chalmers.snake.interfaces.GameEngineIC;
import se.chalmers.snake.interfaces.GameEngineIC.GameEngineEvent;
import se.chalmers.snake.interfaces.HighscoreDatabaseIC;
import se.chalmers.snake.mastercontroller.ControlResources;
import se.chalmers.snake.util.EnumObservable;
import se.chalmers.snake.util.EnumObserver;


public class StartActivity extends Activity { // implements SensorEventListener

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.snake_layout);



		//Instantiate layouts
		background = (RelativeLayout) findViewById(R.id.backgroundImage);
		menu = (LinearLayout) findViewById(R.id.menu_buttons);

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
		//resumeGame.setOnClickListener(resumeGameListener);
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

	private enum MenuState {
		//menuState possibilities

		onStartMenu,
		onPauseMenu,
		onHighscoreMenu,
		onHelpMenu
	};
	private MenuState menuState;
	private MenuState previousMenuState;

	public void show() {
		menu.setVisibility(View.VISIBLE);

	}

	public void hide() {
		menu.setVisibility(View.GONE);
		background.setBackgroundColor(0x00000000);
	}

	public void switchToStartMenu() {
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

	public void switchToPauseMenu() {
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

	public void switchToHighscoreMenu() {
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

	public void switchToHelpMenu() {
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
			ControlResources.make(StartActivity.this, R.id.spelplan);
			switch (previousMenuState) {
				case onStartMenu:
					switchToStartMenu();
					break;
				case onPauseMenu:
					switchToPauseMenu();
					break;
				default:
					break;
			}

		}
	};
	private View.OnClickListener newGameListener = new View.OnClickListener() {
		public void onClick(View v) {
			// This call most be down after first rend of screen but before some recuses nead this.
			ControlResources.make(StartActivity.this, R.id.spelplan);
			Intent gameIntent = new Intent(StartActivity.this, GameActivity.class);
			StartActivity.this.startActivity(gameIntent);

		}
	};

	
	private View.OnClickListener helpListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			ControlResources.make(StartActivity.this, R.id.spelplan);
			switchToHelpMenu();
			show();
		}
	};
	private View.OnClickListener highscoreListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			ControlResources.make(StartActivity.this, R.id.spelplan);


			HighscoreDatabaseIC highData = ControlResources.get().getHighscoreDatabase();
			highscoreText.setText(highData.toString());
			
			switchToHighscoreMenu();
			show();
		}
	};
	/*
	@Override
	public boolean onKeyDown(int keycode, KeyEvent event) {
	if (keycode == KeyEvent.KEYCODE_MENU) {
	
	gameView.pauseGame();
	switchToPauseMenu();
	show();
	}
	return super.onKeyDown(keycode, event);
	}
	 */

	/**
	 * This will block the Back Button in case the game is run and only show the menu.
	 * If the back button are press in pause mode the will work as normal. and return/exit app.
	 */
	/*
	@Override
	public void onBackPressed() {
	if (this.gameView.isRun()) {
	this.gameView.pauseGame();
	switchToPauseMenu();
	show();
	} else {
	super.onBackPressed();
	}
	}
	 */
}
