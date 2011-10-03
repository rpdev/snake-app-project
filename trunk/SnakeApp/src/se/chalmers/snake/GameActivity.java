package se.chalmers.snake;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import se.chalmers.snake.gameui.GameView;
import se.chalmers.snake.interfaces.GameEngineIC;
import se.chalmers.snake.interfaces.GameEngineIC.GameEngineEvent;
import se.chalmers.snake.mastercontroller.ControlResources;
import se.chalmers.snake.util.EnumObservable;
import se.chalmers.snake.util.EnumObserver;

/**
 * This GameActivity will run the Game 
 */
public class GameActivity extends Activity implements EnumObserver<GameEngineIC.GameEngineEvent, Void, Void> {

	private GameEngineIC gameEngine;
	private int totalScore;
	private String currentLevel;
	private GameView gameView;
	private WakeLock wakeLock;
	private MenuControll mColl;

	private class MenuControll {

		private LinearLayout menu;
		private final Button RESUME;
		private final Button EXIT;
		private final Button RESTART;
		private final Button START;
		private final Button NEXT_LEVEL;
		private final Button ENTER_HIGHSCORE;

		private MenuControll() {
			this.menu = (LinearLayout) GameActivity.this.findViewById(R.id.game_menu_button);
			this.RESUME = (Button) GameActivity.this.findViewById(R.id.game_menu_button_resume);
			this.RESUME.setOnClickListener(new View.OnClickListener() {

				public void onClick(View view) {
					MenuControll.this.hidden();
					GameActivity.this.gameView.startGame();
				}
			});

			this.RESTART = (Button) GameActivity.this.findViewById(R.id.game_menu_button_restart);
			this.RESTART.setOnClickListener(new View.OnClickListener() {

				public void onClick(View view) {
					MenuControll.this.hidden();
					GameActivity.this.gameView.restartGame();
					GameActivity.this.gameView.startGame();
				}
			});

			this.EXIT = (Button) GameActivity.this.findViewById(R.id.game_menu_button_menu);
			this.EXIT.setOnClickListener(new View.OnClickListener() {

				public void onClick(View view) {
					GameActivity.this.finish();
				}
			});

			this.START = (Button) GameActivity.this.findViewById(R.id.game_menu_button_start);
			this.START.setOnClickListener(new View.OnClickListener() {

				public void onClick(View view) {
					MenuControll.this.hidden();
					if (GameActivity.this.gameEngine.getStatus() != GameEngineIC.GameEngineStatus.NEW_LEVEL) {
						GameActivity.this.gameView.restartGame();
					}
					GameActivity.this.gameView.startGame();
				}
			});

			this.NEXT_LEVEL = (Button) GameActivity.this.findViewById(R.id.game_menu_button_next_level);
			this.NEXT_LEVEL.setOnClickListener(new View.OnClickListener() {

				public void onClick(View view) {
					GameActivity.this.loadNextLevel();
				}
			});

			this.ENTER_HIGHSCORE = (Button) GameActivity.this.findViewById(R.id.game_menu_button_enter_highscore);
			this.ENTER_HIGHSCORE.setOnClickListener(new View.OnClickListener() {

				public void onClick(View view) {
					Intent highscoreIntent = new Intent(GameActivity.this, HighscoreActivity.class);
					highscoreIntent.putExtra("points", GameActivity.this.gameView.getScore() + GameActivity.this.totalScore);
					GameActivity.this.startActivity(highscoreIntent);
					GameActivity.this.finish();
				}
			});

		}

		private void show(Button... buttons) {

			RESUME.setVisibility(View.GONE);
			EXIT.setVisibility(View.GONE);
			RESTART.setVisibility(View.GONE);
			START.setVisibility(View.GONE);
			NEXT_LEVEL.setVisibility(View.GONE);
			ENTER_HIGHSCORE.setVisibility(View.GONE);

			for (Button button : buttons) {
				if (button != null) {
					button.setVisibility(View.VISIBLE);
				}
			}
			this.menu.setVisibility(View.VISIBLE);

		}

		private void hidden() {
			this.menu.setVisibility(View.GONE);
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);




		this.setContentView(R.layout.game_layout);
		this.mColl = new MenuControll();
		this.totalScore = 0;

		PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
		this.wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "GameActivityHold");

		this.gameEngine = ControlResources.get().getGameEngine();
		
		this.gameEngine.loadLevel("Level 1");
		this.currentLevel = "Level 1";
		
		this.gameView = new GameView(this, this.gameEngine);
		this.gameEngine.addObserver(GameEngineEvent.PLAYER_LOSE, this);
		this.gameEngine.addObserver(GameEngineEvent.LEVEL_END, this);
		this.gameEngine.addObserver(GameEngineEvent.UPDATE, this);
		RelativeLayout layout = ((RelativeLayout) this.findViewById(R.id.game_view_holder));
		layout.addView(this.gameView);
		this.showStartMenu();

	}

	@Override
	public void onPause() {
		this.showPauseMenu(false);
		if (this.wakeLock != null && this.wakeLock.isHeld()) {
			this.wakeLock.release();
		}
		super.onPause();
	}

	@Override
	public void onResume() {
		if (this.wakeLock != null) {
			this.wakeLock.acquire();
		}
		super.onResume();
	}

	@Override
	public void finish() {
		if (this.wakeLock != null && this.wakeLock.isHeld()) {
			this.wakeLock.release();
		}
		this.gameEngine.pauseGame(); // Force Stop of gameEngine on exit.
		super.finish();
	}

	public void showPauseMenu(boolean showHighscore) {
		if (this.gameView.isRun()) {
			this.gameView.pauseGame();
		}
		boolean showResume = GameActivity.this.gameEngine.getStatus() != GameEngineIC.GameEngineStatus.LEVEL_END;
		boolean showEnterHighscore = showHighscore && ControlResources.get().getHighscoreDatabase().checkIfEnoughPoints(this.totalScore + this.gameView.getScore());

		this.mColl.show(showResume ? this.mColl.RESUME : null, showEnterHighscore ? this.mColl.ENTER_HIGHSCORE : null, this.mColl.RESTART, this.mColl.EXIT);

	}

	public void showNextLevelMenu() {
		if (this.gameView.isRun()) {
			this.gameView.pauseGame();
		}
		this.mColl.show(this.mColl.NEXT_LEVEL, this.mColl.RESTART, this.mColl.EXIT);
	}

	public void showStartMenu() {
		if (this.gameView.isRun()) {
			this.gameView.pauseGame();
		}

		this.mColl.show(this.mColl.START, this.mColl.EXIT);
	}

	@Override
	public void onBackPressed() {
		if (this.gameView.isRun()) {
			this.showPauseMenu(false);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onKeyDown(int keycode, KeyEvent event) {
		if (keycode == KeyEvent.KEYCODE_MENU) {
			this.showPauseMenu(false);
		}
		return super.onKeyDown(keycode, event);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	private void loadNextLevel() {
		this.totalScore += this.gameView.getScore();
		String[] levels = ControlResources.get().getLevelDatabase().getLevelListByName();
		for (int index = 0; index < levels.length; index++) {
			if (this.currentLevel.equals(levels[index])) {
				if (levels.length < (index + 1)) {
					this.currentLevel = levels[index + 1];
					break;
				} else {
					this.currentLevel = null;
					break;
				}
			}
		}
		if (this.currentLevel != null) {
			this.gameEngine.loadLevel(this.currentLevel);
		} else {
		}
	}

	public Void observerNotify(EnumObservable<GameEngineEvent, Void, Void> observable, GameEngineEvent event, Void arg) {


		if (event == GameEngineEvent.PLAYER_LOSE) {
			this.runOnUiThread(new Runnable() {

				public void run() {
					GameActivity.this.showPauseMenu(true);
					/*
					Intent highscoreIntent = new Intent(GameActivity.this, HighscoreActivity.class);
					highscoreIntent.putExtra("points",GameActivity.this.gameView.getScore());
					GameActivity.this.startActivity(highscoreIntent);
					 */
				}
			});
			return null;
		}
		if (event == GameEngineEvent.LEVEL_END) { // Try to load next level if 
			this.runOnUiThread(new Runnable() {

				public void run() {
					GameActivity.this.showNextLevelMenu();
				}
			});
		}
		return null;
	}
}
