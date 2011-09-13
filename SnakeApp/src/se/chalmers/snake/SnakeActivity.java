package se.chalmers.snake;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;


public class SnakeActivity extends Activity implements SensorEventListener {
	private SensorManager mSensorManager;
	TextView xViewO = null;
	TextView yViewO = null;
	TextView zViewO = null;
	TextView iViewO = null;
	TextView calcViewO = null;
	// Buffer data store
	private float[] mGData = new float[3];
	private float[] mMData = new float[3];
	private float[] mR = new float[16];
	//private float[] mI = new float[16];
	private float[] mOrientation = new float[3];

	public SnakeActivity() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		xViewO = (TextView) findViewById(R.id.xboxo);
		yViewO = (TextView) findViewById(R.id.yboxo);
		zViewO = (TextView) findViewById(R.id.zboxo);
		iViewO = (TextView) findViewById(R.id.iboxo);
		calcViewO = (TextView) findViewById(R.id.calcvalue);


		this.mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);






	}

	@Override
	protected void onResume() {
		// Ideally a game should implement onResume() and onPause()
		// to take appropriate action when the activity looses focus
		super.onResume();
		Sensor gsensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		Sensor msensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		mSensorManager.registerListener(this, gsensor, SensorManager.SENSOR_DELAY_GAME);
		mSensorManager.registerListener(this, msensor, SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	protected void onPause() {
		// Ideally a game should implement onResume() and onPause()
		// to take appropriate action when the activity looses focus
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	public void onSensorChanged(SensorEvent event) {
		int type = event.sensor.getType();
		//if (type == Sensor.TYPE_ACCELEROMETER) {
			if (type == Sensor.) {
			
			System.arraycopy(event.values, 0, this.mGData, 0, 3);

		} else if (type == Sensor.TYPE_MAGNETIC_FIELD) {
			System.arraycopy(event.values, 0, this.mMData, 0, 3);
		} else {
			return;
		}
		
		
		//if (SensorManager.getRotationMatrix(mR, mI, mGData, mMData)) {
		if (SensorManager.getRotationMatrix(mR, null, mGData, mMData)) {	
			SensorManager.getOrientation(mR, mOrientation);

			//float incl = SensorManager.getInclination(mI);
			final float rad2deg = (float) (180.0f / Math.PI);
			xViewO.setText("X: " + (mGData[0]));
			yViewO.setText("Y: " + (mGData[1]));
			zViewO.setText("Z: " + (mGData[2]));
			iViewO.setText("I: " + (mOrientation[1]*mOrientation[1]+mOrientation[2]*mOrientation[2]));
			calcViewO.setText("Angle: " + Math.atan2(mOrientation[1], mOrientation[2])*rad2deg+180);
		}
	}

}
