package se.chalmers.snake;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import se.chalmers.snake.gameGUI.DrawTest;

import android.app.Activity;
import android.os.Bundle;

public class DrawTestActivity extends Activity { // implements SensorEventListener

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
				if (pw != null) {
					pw.close();
				}
				if (sw != null) {
					sw.close();
				}
			} catch (IOException ignore) {
			}
		}
		return retValue;
	}
}
