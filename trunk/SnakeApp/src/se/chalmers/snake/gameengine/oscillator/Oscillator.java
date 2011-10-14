package se.chalmers.snake.gameengine.oscillator;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Make a Oscillator to repeatedly at regular intervals call a method.
 */
public class Oscillator {

	private final int interval;
	private Timer timer;
	private final Runnable calls;

	/**
	 * Make a new Oscillator for repeatedly at regular intervals call a method.
	 * At start the Oscillator is off and can be start by call {@link Oscillator#start()} 
	 * or {@link Oscillator#stop()} for stop a runing Oscillator.
	 * The Oscillator will give a puse directly at start.
	 * @param interval Intervall in ms
	 * @param call Method to be call.
	 */
	public Oscillator(final int interval, final Runnable call) {
		this.interval = interval;
		this.calls = call;
	}

	/**
	 * Start the Oscillator and this will call a method in a regular intervall
	 */
	public synchronized void start() {
		if (this.timer == null && this.calls != null) {
			this.timer = new Timer("Oscillator", false);
			TimerTask tt = new TimerTask() {

				@Override
				public void run() {
					Oscillator.this.calls.run();
				}
			};
			this.timer.scheduleAtFixedRate(tt, new Date(), this.interval);
		}
	}

	/**
	 * Stop the Oscillator.
	 */
	public synchronized void stop() {
		if (this.timer != null) {
			this.timer.cancel();
			this.timer = null;
		}
	}
}
