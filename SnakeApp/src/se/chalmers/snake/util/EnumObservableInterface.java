package se.chalmers.snake.util;

/**
 * This interface represents an observable object, and can hold multi 
 * EnumObserver object.
 * The main function of the Interface is for get the basic public API interface 
 * to be extends in a other interface.
 * 
 * Each EnumObservable has a enum class as event type list,
 * while this EnumObservable is fire a event, the event most be once of the
 * constants in the Enum of this.
 * @param <E> is a Enum clas and is use as for specific the list of event.
 * @param <A> is the type of the argument can has.
 * @param <R> is the type the return value will has for the "fireObserver"
 * @see EnumObservable
 */
public interface EnumObservableInterface<E extends Enum<E>, A, R> {

	/**
	 * Add a EnumObserver to this EnumObservable with select event key
	 * @param key The event key this EnumObserver will listen for.
	 * @param observer The EnumObserver observer Object.
	 * @return True if this EnumObserver has be add, otherwise false.
	 */
	public boolean addObserver(E key, EnumObserver<E, A, R> observer);

	/**
	 * Remove a EnumObserver from this EnumObservable,
	 * This will remove the EnumObserver from all events type.
	 * @param observer The EnumObserver to remove.
	 * @return Return true if the EnumObserver has been removed from at
	 * least one event type, otherwise false.
	 */
	public boolean removeObserver(EnumObserver<E, A, R> observer);

	/**
	 * Remove a EnumObserver from this EnumObservable for select event type.
	 * @param key The event type to remove from.
	 * @param observer The EnumObserver to remove from.
	 * @return Return true if the EnumObserver has been removed the event type,
	 * otherwise false.
	 */
	public boolean removeObserver(E key, EnumObserver<E, A, R> observer);
}
