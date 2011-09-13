package se.chalmers.snake;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import se.chalmers.snake.motiondetector.MotionDetector;


public class SnakeActivity extends Activity{ //  implements SensorEventListener 
	private SensorManager mSensorManager;
	TextView iViewO = null;
	TextView calcViewO = null;
	MotionDetector motionDetector;
	public SnakeActivity() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		iViewO = (TextView) findViewById(R.id.iboxo);
		calcViewO = (TextView) findViewById(R.id.calcvalue);
		this.mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		this.motionDetector = new MotionDetector(this.mSensorManager,new Runnable() {
			public void run() {
				calcViewO.setText(motionDetector.toString());
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		this.motionDetector.start();

	}

	@Override
	protected void onPause() {
		super.onPause();
		this.motionDetector.stop();
	}


}
