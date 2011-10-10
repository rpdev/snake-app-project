package se.chalmers.snake;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import se.chalmers.snake.interfaces.LevelHistoryIC;
import se.chalmers.snake.mastercontroller.ControlResources;

/**
 * Show the meny for select a level to start from.
 * @author 
 */
public class SelectLevelActivity extends Activity {

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		this.setContentView(R.layout.select_level_layout);
		this.findViewById(R.id.select_level_back_button).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				SelectLevelActivity.this.finish();
			}
		});

		String[] allLevels = ControlResources.get().getLevelDatabase().getLevelListByName();
		LevelHistoryIC isPlay = ControlResources.get().getLevelHistory();
		int levelCount = 0;

		
	LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
     LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	layoutParams.setMargins(0,0,0,4);

		LinearLayout layout = (LinearLayout) this.findViewById(R.id.select_level_layout_inner);
		for (String level : allLevels) {
			Button button = new Button(this);
			
			button.setOnClickListener(this.onClick(level));
			if (levelCount == 0 || isPlay.is(level)) {
				button.setText(level);
			} else {
				button.setText("[" + level + "]");
				button.setEnabled(false);
			}
			layout.addView(button,layoutParams);
			levelCount++;
		}
	}

	private View.OnClickListener onClick(final String level) {
		return new View.OnClickListener() {

			public void onClick(View view) {
				Intent gameIntent = new Intent(SelectLevelActivity.this, GameActivity.class);
				gameIntent.putExtra("level", level);
				SelectLevelActivity.this.startActivity(gameIntent);
			}
		};

	}
}
