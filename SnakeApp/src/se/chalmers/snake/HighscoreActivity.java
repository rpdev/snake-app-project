package se.chalmers.snake;

import se.chalmers.snake.interfaces.HighscoreDatabaseIC;
import se.chalmers.snake.mastercontroller.ControlResources;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class HighscoreActivity extends Activity {

	private HighscoreDatabaseIC highscoreDatabase;
	private int points;
	private Button skipButton;
	private Button submitButton;
	private TextView youMadeIt;
	private EditText inputName;
	private View.OnClickListener skipListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			HighscoreActivity.this.finish();
		}
	};
	private View.OnClickListener submitListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			String name = HighscoreActivity.this.inputName.getText().toString().trim();
			if (HighscoreActivity.this.savePoints(name, HighscoreActivity.this.points)) {
				HighscoreActivity.this.finish();
			}
		}
	};

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		this.setContentView(R.layout.highscore_layout);

		Bundle extras = this.getIntent().getExtras();
		if (extras != null) {
			this.points = extras.getInt("points");
		}

		this.setObjects();
		this.highscoreDatabase = ControlResources.get().getHighscoreDatabase();
		if (checkIfEnoughPoints()) {
			showYouMadeItMenu();
		} else {
			this.finish();
		}

	}

	private void setObjects() {
		//Set up buttons
		this.skipButton = (Button) findViewById(R.id.skip_button);
		this.skipButton.setOnClickListener(skipListener);
		this.submitButton = (Button) findViewById(R.id.submit_button);
		this.submitButton.setOnClickListener(submitListener);
		//Set up text views
		this.youMadeIt = (TextView) findViewById(R.id.you_made_it_text);
		//Set up edittext
		this.inputName = (EditText) findViewById(R.id.input_edittext);
	}

	private boolean checkIfEnoughPoints() {
		return highscoreDatabase.checkIfEnoughPoints(points);
	}

	private void showYouMadeItMenu() {
		this.youMadeIt.setVisibility(View.VISIBLE);
		this.skipButton.setVisibility(View.VISIBLE);
		this.submitButton.setVisibility(View.VISIBLE);
		this.inputName.setVisibility(View.VISIBLE);
	}

	private boolean savePoints(String name, int points) {
		this.highscoreDatabase.addPlayerToHighscore(name, points);
		return this.highscoreDatabase.saveHighscore();
	}
}
