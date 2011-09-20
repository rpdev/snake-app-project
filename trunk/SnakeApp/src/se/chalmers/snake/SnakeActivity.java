package se.chalmers.snake;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import se.chalmers.snake.gameGUI.DrawTest;
import se.chalmers.snake.leveldatabase.LevelDatabase;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SnakeActivity extends Activity { // implements SensorEventListener
	/*
	 * private SensorManager mSensorManager; TextView iViewO = null; TextView
	 * calcViewO = null; MotionDetector motionDetector; public SnakeActivity() {
	 * }
	 * 
	 * @Override public void onCreate(Bundle savedInstanceState) {
	 * super.onCreate(savedInstanceState); setContentView(R.layout.main); iViewO
	 * = (TextView) findViewById(R.id.iboxo); calcViewO = (TextView)
	 * findViewById(R.id.calcvalue); calcViewO.setText("asdasd");
	 * /*this.mSensorManager = (SensorManager)
	 * getSystemService(Context.SENSOR_SERVICE); this.motionDetector = new
	 * MotionDetector(this.mSensorManager,new Runnable() { public void run() {
	 * calcViewO.setText(motionDetector.toString()); } }); }
	 * 
	 * @Override protected void onResume() { super.onResume();
	 * this.motionDetector.start();
	 * 
	 * }
	 * 
	 * @Override protected void onPause() { super.onPause();
	 * this.motionDetector.stop(); }
	 */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DrawTest d = new DrawTest(this);
		setContentView(d);
	}

	// http://javahowto.blogspot.com/2006/08/save-exception-stacktrace-to-string.html
	private String stackTraceToString(Throwable e) {
		String retValue = null;
		StringWriter sw = null;
		PrintWriter pw = null;
		try {
			sw = new StringWriter();
			pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			retValue = sw.toString();
		} finally {
			try {
				if (pw != null)
					pw.close();
				if (sw != null)
					sw.close();
			} catch (IOException ignore) {}
		}
		return retValue;
	}

}
