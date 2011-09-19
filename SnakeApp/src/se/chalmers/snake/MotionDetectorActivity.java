/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.snake;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import se.chalmers.snake.motiondetector.MotionDetector;

/**
 * Test case for MotionDetector
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
		this.calcViewO = (TextView) findViewById(R.id.calcvalue);
		this.mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		this.motionDetector = new MotionDetector(this.mSensorManager, new Runnable() {
			public void run() {
				MotionDetectorActivity.this.calcViewO.setText(motionDetector.toString());
			}
		});
		this.motionDetector.setSensitivity(15);
		
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
