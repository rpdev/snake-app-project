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

	private static final double RAD_TO_DEG = (180.0f / Math.PI);
	private static final double PI_TIMES_2 = Math.PI * 2;
	private MotionDetectorIC.ReferenceSurface referenceSurface;
	private SensorManager sensorManager;
	private boolean run;
	private float[] mGData = new float[3];
	private float[] mMData = new float[3];
	private float[] mR = new float[16];
	private float[] mOrientation = new float[3];
	private boolean isUpdate = false;
	private double radianus;
	private int degrees;
	private int count = 0;
	private double sensitivity = 0;
	private Runnable callWhileUpdate;

	public MotionDetector(SensorManager sensorManager) {
		this.sensorManager = sensorManager;
		this.run = false;
		this.degrees = 0;
		this.radianus = 0.0;
		this.referenceSurface = null;
		this.callWhileUpdate = null;
		this.setSensitivity(7);
	}

	public MotionDetector(SensorManager sensorManager, Runnable callWhileUpdate) {
		this.sensorManager = sensorManager;
		this.run = false;
		this.degrees = 0;
		this.radianus = 0.0;
		this.referenceSurface = null;
		this.callWhileUpdate = callWhileUpdate;
		this.setSensitivity(7);

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		int type = event.sensor.getType();
		if (type == Sensor.TYPE_ACCELEROMETER) {
			System.arraycopy(event.values, 0, this.mGData, 0, 3);
			this.isUpdate = false;

		} else if (type == Sensor.TYPE_MAGNETIC_FIELD) {
			System.arraycopy(event.values, 0, this.mMData, 0, 3);
			this.isUpdate = false;

		} else {
			return;
		}
		if (this.callWhileUpdate != null) {
			this.callWhileUpdate.run();
		}

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int i) {
	}

	@Override
	public synchronized void setSensitivity(int sensitivity) {
		this.sensitivity = (sensitivity * (2.22 / 100));
	}

	@Override
	public synchronized void start() {
		if (this.run == false) {

			Sensor gsensor = this.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			Sensor msensor = this.sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
			this.sensorManager.registerListener(this, gsensor, SensorManager.SENSOR_DELAY_GAME);
			this.sensorManager.registerListener(this, msensor, SensorManager.SENSOR_DELAY_GAME);

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
		if (rs != null) {
			this.referenceSurface = rs;
		}
	}

	@Override
	public int getAngleByDegrees() {
		if (this.run == false) {
			this.start();
		}
		if (this.isUpdate == false) {
			this.recalc();
		}
		return this.degrees;
	}

	@Override
	public double getAngleByRadians() {
		if (this.run == false) {
			this.start();
		}
		if (this.isUpdate == false) {
			this.recalc();
		}
		return this.radianus;
	}

	private void recalc() {

		if (SensorManager.getRotationMatrix(this.mR, null, this.mGData, this.mMData)) {
			SensorManager.getOrientation(this.mR, this.mOrientation);
			if (this.referenceSurface != null) {

				switch (this.referenceSurface) {
					case FLAT_TOP: {
						if (Math.sqrt(this.mOrientation[1] * this.mOrientation[1] + this.mOrientation[2] * this.mOrientation[2]) > this.sensitivity) {
							this.radianus = Math.atan2(this.mOrientation[1], -this.mOrientation[2]);
						}
					}
					break;

					default: {
						if (Math.sqrt(this.mOrientation[1] * this.mOrientation[1] + this.mOrientation[2] * this.mOrientation[2]) > this.sensitivity) {
							this.radianus = -Math.atan2(this.mOrientation[1], this.mOrientation[2]);
						}
					}
				}

			} else {
				if (Math.sqrt(this.mOrientation[1] * this.mOrientation[1] + this.mOrientation[2] * this.mOrientation[2]) > this.sensitivity) {
					this.radianus = -Math.atan2(this.mOrientation[1], this.mOrientation[2]);
				}
			}
			if (this.radianus < 0) {
				this.radianus += PI_TIMES_2;
			}

			this.degrees = (int) (this.radianus * MotionDetector.RAD_TO_DEG);
			this.count++;
			this.isUpdate = true;
		}
	}

	@Override
	public String toString() {
		this.recalc();
		return "MotionDetector{" + "run=" + run + ", radianus=" + radianus + ", degrees=" + degrees + ", count=" + count + '}';
	}
}
