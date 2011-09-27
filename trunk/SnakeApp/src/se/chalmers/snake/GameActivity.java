package se.chalmers.snake;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import se.chalmers.snake.gameguiold.GameView;
import se.chalmers.snake.interfaces.GameEngineIC;
import se.chalmers.snake.interfaces.GameEngineIC.GameEngineEvent;
import se.chalmers.snake.interfaces.util.XYPoint;
import se.chalmers.snake.mastercontroller.ControlResources;
import se.chalmers.snake.util.EnumObservable;
import se.chalmers.snake.util.EnumObserver;

/**
 * This GameActivity will run the Game 
 */
public class GameActivity extends Activity implements EnumObserver<GameEngineIC.GameEngineEvent, Void, Void> {

	public static final String LEVEL = "se.chalmers.snake.GameActivity.level";
	private GameView gameView;
	private LinearLayout menu;
	private Button buttonResume;
	private Button buttonMenu;
	private Button buttonRestart;
	private Button buttonStart;
	private GameEngineIC gameEngine;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		

		this.setContentView(R.layout.game_layout);
		//<editor-fold defaultstate="collapsed" desc="Menu">

		this.menu = (LinearLayout) this.findViewById(R.id.game_menu_button);
		this.buttonResume = (Button) this.findViewById(R.id.game_menu_button_resume);
		this.buttonResume.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				GameActivity.this.hide();
				GameActivity.this.gameView.startGame();
			}
		});

		this.buttonRestart = (Button) this.findViewById(R.id.game_menu_button_restart);
		this.buttonRestart.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				GameActivity.this.hide();
				GameActivity.this.gameView.restartGame();
				GameActivity.this.gameView.startGame();
			}
		});

		this.buttonMenu = (Button) this.findViewById(R.id.game_menu_button_menu);
		this.buttonMenu.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				GameActivity.this.finish();
			}
		});

		this.buttonStart = (Button) this.findViewById(R.id.game_menu_button_start);
		this.buttonStart.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				GameActivity.this.hide();
				if (GameActivity.this.gameEngine.getStatus() != GameEngineIC.GameEngineStatus.NEW_LEVEL) {
					GameActivity.this.gameView.restartGame();
				}
				GameActivity.this.gameView.startGame();
			}
		});


		//</editor-fold>

		this.gameEngine = ControlResources.get().getGameEngine();
		this.gameEngine.loadLevel("");
		this.gameView = new GameView(this, this.gameEngine);
		this.gameEngine.addObserver(GameEngineEvent.PLAYER_LOSE, this);
		RelativeLayout layout = ((RelativeLayout) this.findViewById(R.id.game_view_holder));
		layout.addView(this.gameView);

		this.showStartMenu();
	}

	@Override
	public void onPause() {
		this.showPauseMenu();
		super.onPause();
	}

	public void showPauseMenu() {
		if (this.gameView.isRun()) {
			this.gameView.pauseGame();
		}


		if (GameActivity.this.gameEngine.getStatus() != GameEngineIC.GameEngineStatus.LEVEL_END) {
					this.buttonResume.setVisibility(View.VISIBLE);
		}
		
		this.buttonMenu.setVisibility(View.VISIBLE);
		this.buttonRestart.setVisibility(View.VISIBLE);
		this.menu.setVisibility(View.VISIBLE);

	}

	public void showStartMenu() {
		if (this.gameView.isRun()) {
			this.gameView.pauseGame();
		}


		this.buttonStart.setVisibility(View.VISIBLE);
		this.buttonMenu.setVisibility(View.VISIBLE);
		this.menu.setVisibility(View.VISIBLE);

	}

	public void hide() {
		this.menu.setVisibility(View.GONE);
		this.buttonResume.setVisibility(View.GONE);
		this.buttonMenu.setVisibility(View.GONE);
		this.buttonRestart.setVisibility(View.GONE);
		this.buttonStart.setVisibility(View.GONE);
	}

	@Override
	public void onBackPressed() {
		if (this.gameView.isRun()) {
			this.showPauseMenu();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onKeyDown(int keycode, KeyEvent event) {
		if (keycode == KeyEvent.KEYCODE_MENU) {
			this.showPauseMenu();
		}
		return super.onKeyDown(keycode, event);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	public Void observerNotify(EnumObservable<GameEngineEvent, Void, Void> observable, GameEngineEvent event, Void arg) {
		System.out.println("Event " + event);
		Intent highscoreIntent = new Intent(GameActivity.this, HighscoreActivity.class);
		highscoreIntent.putExtra("points", 5);
		GameActivity.this.startActivity(highscoreIntent);
		this.finish();
		return null;
	}
}
