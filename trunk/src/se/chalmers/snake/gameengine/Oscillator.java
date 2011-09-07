package se.chalmers.snake.gameengine;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Make a Oscillator for short times interval
 */
class Oscillator {

	private int interval;
	private boolean run;
	private final Timer timer;
	private final Runnable calls;

	public Oscillator(int interval, Runnable call) {
		this.interval = interval;
		this.timer = new Timer("Oscillator", false);
		this.calls = call;
	}

	public synchronized void start() {
		if (this.run == false && this.calls!=null) {
			this.run = true;
			TimerTask tt = new TimerTask() {

				@Override
				public void run() {
					Oscillator.this.calls.run();
				}
			};
			this.timer.scheduleAtFixedRate(tt, new Date(), this.interval);
		}
	}

	public synchronized void stop() {
		if (this.run == true) {
			this.timer.cancel();
			this.run = false;
		}
	}
}
