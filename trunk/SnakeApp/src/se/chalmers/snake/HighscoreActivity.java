package se.chalmers.snake;

import se.chalmers.snake.highscoreDatabase.HighscoreDatabase;
import se.chalmers.snake.util.Storage;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import se.chalmers.snake.mastercontroller.ControlResources;

public class HighscoreActivity extends Activity {

	private HighscoreDatabase highscoreDatabase;
	private Storage storage;
	private int points;
	
	private Button skipButton;
	private Button submitButton;
	private TextView youMadeIt;
	private TextView youDidntMakeIt;
	private EditText inputName;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		this.setContentView(R.layout.highscore_layout);
		
		Bundle extras = this.getIntent().getExtras();
		if(extras != null){
			points = extras.getInt("points");
		}
		
		this.setObjects();
		this.storage = ControlResources.get().getStorage();
		this.highscoreDatabase = storage.getObject("highscore");
		if(this.highscoreDatabase == null){
			this.highscoreDatabase = new HighscoreDatabase();
		}
		
		if(checkIfEnoughPoints()){
			showYouMadeItMenu();
		} else {
			showSorryMenu();
		}
		
	}
	
	private void setObjects(){
		//Set up buttons
		this.skipButton = (Button)findViewById(R.id.skip_button);
		this.skipButton.setOnClickListener(skipListener);
		this.submitButton = (Button)findViewById(R.id.submit_button);
		this.submitButton.setOnClickListener(submitListener);
		//Set up text views
		this.youMadeIt = (TextView)findViewById(R.id.you_made_it_text);
		this.youDidntMakeIt = (TextView)findViewById(R.id.you_didnt_make_it_text);
		//Set up edittext
		this.inputName = (EditText)findViewById(R.id.input_edittext);
	}
	
	private boolean checkIfEnoughPoints(){
		return highscoreDatabase.checkIfEnoughPoints(points);
	}
	
	private void showYouMadeItMenu(){
		//These elements should be visible
		this.youMadeIt.setVisibility(View.VISIBLE);
		this.skipButton.setVisibility(View.VISIBLE);
		this.submitButton.setVisibility(View.VISIBLE);
		this.inputName.setVisibility(View.VISIBLE);
		//These elements shouldn't
		this.youDidntMakeIt.setVisibility(View.GONE);
	}
	
	private void showSorryMenu(){
		//These elements should be visible
		this.youDidntMakeIt.setVisibility(View.VISIBLE);
		this.skipButton.setVisibility(View.VISIBLE);
		//skipbutton should be renamed
//		this.skipButton.setText("Exit");
		//These elements shouldn't
		this.inputName.setVisibility(View.GONE);
		this.youMadeIt.setVisibility(View.GONE);
		this.submitButton.setVisibility(View.GONE);
	}
	
	private void savePoints(String name, int points){
		this.highscoreDatabase.addPlayerToHighscore(name, points);
		this.storage.storeObject("highscore", this.highscoreDatabase);
		if(this.storage.getObject("highscore") == null)
			submitButton.setText("FAIL");
	}

	private View.OnClickListener skipListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			HighscoreActivity.this.finish();
			
		}
	};
	private View.OnClickListener submitListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String name = inputName.getText().toString();
			HighscoreActivity.this.savePoints(name,points);			
		}
	};
}
