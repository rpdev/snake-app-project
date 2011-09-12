package se.chalmers.snake.motiondetector;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import se.chalmers.snake.interfaces.MotionDetectorIC;

/**
 * This is the MotionDetector
 */
public class MotionDetector implements SensorEventListener, MotionDetectorIC {
	private MotionDetectorIC.ReferenceSurface referenceSurface;
	private SensorManager sensorManager;
	private boolean run;
	private float x, y, z;
	private double radianus;
	private int degrees;

	public MotionDetector() {
		this.run = false;
		this.degrees = 0;
		this.radianus = 0.0;
		this.referenceSurface = null;

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ORIENTATION && event.values.length >= 2) {
			this.x = event.values[0];
			this.y = event.values[1];
			this.x = event.values[2];
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int i) {
	}


	@Override
	public synchronized void setSensitivity(int sensitivity) {
		
	}

	@Override
	public synchronized void start() {
		if (this.run == false) {
			this.sensorManager.registerListener(this,
					  this.sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
					  SensorManager.SENSOR_DELAY_GAME);
			this.run = true;
		}
	}

	@Override
	public synchronized void stop() {
		if (this.run == true) {
			this.sensorManager.unregisterListener(this);
			this.run = false;
		}
	}
	
	@Override
	public synchronized void setReferenceSurface(ReferenceSurface rs) {
		if(rs!=null) {
		this.referenceSurface = rs;
		}
	}

	@Override
	public int getAngleByDegrees() {
		return this.degrees;
	}

	@Override
	public double getAngleByRadians() {
		return this.radianus;
	}
	
	
}
