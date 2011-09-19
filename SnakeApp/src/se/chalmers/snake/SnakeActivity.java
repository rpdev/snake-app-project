package se.chalmers.snake;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import se.chalmers.snake.leveldatabase.LevelDatabase;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SnakeActivity extends Activity { // implements SensorEventListener


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tv = new TextView(this);
		tv.append("SnakeActivity");
		try {
			for (String s : LevelDatabase.getInstance().getLevelListByName())
				tv.append(LevelDatabase.getInstance().getByName(s).getMapSize().toString() + "\n");
		} catch (Exception e) {
			tv.append(stackTraceToString(e));
		}
		setContentView(tv);
	}
	public static TextView tv;

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
