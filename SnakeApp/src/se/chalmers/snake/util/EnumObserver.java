package se.chalmers.snake.util;

/**
 * A class can implement the <code>EnumObserver</code> interface when it
 * wants to be informed of changes in observable objects.
 */
public interface EnumObserver<E extends Enum<E>, A, R> {

	/**
	 * This method is called whenever the observed object is changed. An
	 * application calls an <tt>EnumObservable</tt> object's
	 * <code>fireObserver</code> method to have all the object's
	 * observers notified of the change.
	 * @param observable The EnumObservable for this event
	 * @param event The event type
	 * @param arg a argument for this event
	 * @return In case if a return Object will be past back to the scoure of the Event.
	 * The return data will be available as a <code>java.util.Collection&lt;java.util.Map.Entry&lt;EnumObserver&lt;E, A, R&gt;, R&gt;&gt;</code>
	 */
	public R observerNotify(EnumObservable<E, A, R> observable, E event, A arg);
}
