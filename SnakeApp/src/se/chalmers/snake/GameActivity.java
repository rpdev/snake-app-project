package se.chalmers.snake;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import se.chalmers.snake.gameGUI.GameView;
import se.chalmers.snake.mastercontroller.ControlResources;

/**
 * This GameActivity will run the Game in select mode and has a in arg with select level name and scores.
 */
public class GameActivity extends Activity {

	public static final String LEVEL = "se.chalmers.snake.GameActivity.level";
	private GameView gameView;
	private LinearLayout menu;
	private Button buttonResume;
	private Button buttonMenu;
	

	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		ControlResources.make(this);
		
		this.setContentView(R.layout.game_layout);
		this.menu = (LinearLayout) this.findViewById(R.id.game_menu_button);

		//Instantiate buttons
		this.buttonResume = (Button) this.findViewById(R.id.game_menu_button_resume);
		this.buttonResume.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				GameActivity.this.hide();
				GameActivity.this.gameView.startGame();
			}
		});
		
		//Instantiate buttons
		this.buttonMenu = (Button) this.findViewById(R.id.game_menu_button_menu);
		this.buttonMenu.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				GameActivity.this.finish();
			}
		});
		
		

		this.gameView = new GameView(this, ControlResources.get().getGameEngine());
		
		RelativeLayout layout = ((RelativeLayout) this.findViewById(R.id.game_view_holder));
		layout.addView(this.gameView);
		ControlResources.get().getGameEngine().startGame();
	}
	
	public void showPauseMenu() {
		this.gameView.pauseGame();
		this.menu.setVisibility(View.VISIBLE);
		this.buttonResume.setVisibility(View.VISIBLE);
		this.buttonMenu.setVisibility(View.VISIBLE);
	}
	

	public void hide() {
		this.menu.setVisibility(View.GONE);
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
}
