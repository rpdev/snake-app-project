/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.snake.motiondetector;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import se.chalmers.snake.R;

/**
 *
 * @author 
 */
public class MotionDetectorActivity extends Activity {

	private SensorManager mSensorManager;
	TextView iViewO = null;
	TextView calcViewO = null;
	MotionDetector motionDetector;

	public MotionDetectorActivity() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.motiondetector);
		calcViewO = (TextView) findViewById(R.id.calcvalue);
		this.mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		this.motionDetector = new MotionDetector(this.mSensorManager, new Runnable() {
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
