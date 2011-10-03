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
 * Use <code>
 *  Intent gameIntent = new Intent(StartActivity.this, GameActivity.class);
 *  gameIntent.putExtra("level", "STRING LEVEL NAME");
 *  StartActivity.this.startActivity(gameIntent);<code> for select level to be load.
 */
public class GameActivity extends Activity implements EnumObserver<GameEngineIC.GameEngineEvent, Void, Void> {

	private GameEngineIC gameEngine;
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
					GameActivity.this.goToHighscoreActivity();
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

		PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
		this.wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "GameActivityHold");

		this.gameEngine = ControlResources.get().getGameEngine();
		//<editor-fold defaultstate="collapsed" desc="Load Select Level">
		String startLevelName = null;
		try {
			Bundle extras = this.getIntent().getExtras();
			startLevelName = extras.getString("level");
		} catch(Exception ex) {}
		if(startLevelName!=null && startLevelName.length()>0) {
			String [] allLevel = ControlResources.get().getLevelDatabase().getLevelListByName();
			if(allLevel.length>0){
				startLevelName = allLevel[0];
			}
		}
		
		this.currentLevel = startLevelName;
		this.gameEngine.loadLevel(startLevelName);
		//</editor-fold>
		this.gameView = new GameView(this, this.gameEngine);
		this.gameEngine.addObserver(GameEngineEvent.PLAYER_LOSE, this);
		this.gameEngine.addObserver(GameEngineEvent.LEVEL_END, this);
		this.gameEngine.addObserver(GameEngineEvent.NEW_GAME, this);
		this.gameEngine.addObserver(GameEngineEvent.START_GAME, this);
		this.gameEngine.addObserver(GameEngineEvent.PAUSE_GAME, this);

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
		this.gameEngine.pauseGame(); // Force Stop of gameEngine on exit, in case run
		super.finish();
	}

	public void showPauseMenu(boolean showHighscore) {
		if (this.gameView.isRun()) {
			this.gameView.pauseGame();
		}
		boolean showResume = GameActivity.this.gameEngine.getStatus() != GameEngineIC.GameEngineStatus.LEVEL_END;
		boolean showEnterHighscore = showHighscore && ControlResources.get().getHighscoreDatabase().checkIfEnoughPoints(this.gameView.getScore());

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
		this.mColl.hidden();
		this.gameEngine.setStartScore(this.gameView.getScore());
		this.currentLevel = ControlResources.get().getLevelDatabase().getNextLevel(this.currentLevel);
		if (this.currentLevel != null) {
			if (!this.gameEngine.loadLevel(this.currentLevel)) {
				this.goToHighscoreActivity();
			}
		} else {
			this.goToHighscoreActivity();
		}
	}

	public Void observerNotify(EnumObservable<GameEngineEvent, Void, Void> observable, GameEngineEvent event, Void arg) {
		if (event == GameEngineEvent.PLAYER_LOSE) {
			this.runOnUiThread(new Runnable() {

				public void run() {
					GameActivity.this.showPauseMenu(true);
				}
			});
			return null;
		} else if (event == GameEngineEvent.LEVEL_END) {
			this.runOnUiThread(new Runnable() {

				public void run() {
					GameActivity.this.showNextLevelMenu();
				}
			});
		} else if (event == GameEngineEvent.NEW_GAME) {
			this.runOnUiThread(new Runnable() {

				public void run() {
					GameActivity.this.showStartMenu();
				}
			});
		}
		return null;
	}

	private void goToHighscoreActivity() {
		Intent highscoreIntent = new Intent(GameActivity.this, HighscoreActivity.class);
		highscoreIntent.putExtra("points", GameActivity.this.gameView.getScore());
		GameActivity.this.startActivity(highscoreIntent);
		GameActivity.this.finish();
	}


}
