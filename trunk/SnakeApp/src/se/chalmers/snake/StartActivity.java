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
import se.chalmers.snake.interfaces.HighscoreDatabaseIC;
import se.chalmers.snake.mastercontroller.ControlResources;

public class StartActivity extends Activity { // implements SensorEventListener

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.snake_layout);



		//Instantiate layouts
		background = (RelativeLayout) findViewById(R.id.backgroundImage);
		menu = (LinearLayout) findViewById(R.id.menu_buttons);

		//Instantiate buttons
		newGameButton = (Button) findViewById(R.id.snake_new_game_button);
		
		helpButton = (Button) findViewById(R.id.snake_help_button);
		highscoreButton = (Button) findViewById(R.id.snake_highscore_button);

		//Instantiate texts
		helpText = (TextView) findViewById(R.id.helpText);
		highscoreText = (TextView) findViewById(R.id.highscoreText);
		back = (Button) findViewById(R.id.back_button);

		this.selectLevel = (Button) this.findViewById(R.id.select_level_button);
		this.selectLevel.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				ControlResources.make(StartActivity.this, R.id.spelplan);
				Intent gameIntent = new Intent(StartActivity.this, SelectLevelActivity.class);
				StartActivity.this.startActivity(gameIntent);
				
			}
		});


		switchToStartMenu();

		show();
		newGameButton.setOnClickListener(newGameListener);
		//resumeGame.setOnClickListener(resumeGameListener);
		helpButton.setOnClickListener(helpListener);
		highscoreButton.setOnClickListener(highscoreListener);
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
	private Button newGameButton;
	private Button helpButton;
	private Button highscoreButton;
	private Button back;
	private Button selectLevel;
	//Texts
	private TextView helpText;
	private TextView highscoreText;
	//Info

	private enum MenuState {
		//menuState possibilities

		onStartMenu,
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
		newGameButton.setVisibility(View.VISIBLE);
		helpButton.setVisibility(View.VISIBLE);
		highscoreButton.setVisibility(View.VISIBLE);
		this.selectLevel.setVisibility(View.VISIBLE);
		//These shouldn't
		back.setVisibility(View.GONE);
		helpText.setVisibility(View.GONE);
		highscoreText.setVisibility(View.GONE);
	}

	public void switchToHighscoreMenu() {
		previousMenuState = menuState;
		menuState = MenuState.onHighscoreMenu;
		//These components should be present
		menu.setVisibility(View.VISIBLE);
		back.setVisibility(View.VISIBLE);
		highscoreText.setVisibility(View.VISIBLE);
		//These shouldn't
		helpButton.setVisibility(View.GONE);
		highscoreButton.setVisibility(View.GONE);
		newGameButton.setVisibility(View.GONE);
		helpText.setVisibility(View.GONE);
		this.selectLevel.setVisibility(View.GONE);
	}

	public void switchToHelpMenu() {
		previousMenuState = menuState;
		menuState = MenuState.onHelpMenu;
		//These components should be present
		menu.setVisibility(View.VISIBLE);
		back.setVisibility(View.VISIBLE);
		helpText.setVisibility(View.VISIBLE);
		//These shouldn't
		helpButton.setVisibility(View.GONE);
		highscoreButton.setVisibility(View.GONE);
		newGameButton.setVisibility(View.GONE);
		highscoreText.setVisibility(View.GONE);
		this.selectLevel.setVisibility(View.GONE);
	}
	private OnClickListener backListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ControlResources.make(StartActivity.this, R.id.spelplan);
			switch (previousMenuState) {
				case onStartMenu:
					switchToStartMenu();
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
}
