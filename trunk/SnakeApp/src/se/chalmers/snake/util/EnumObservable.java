package se.chalmers.snake.util;

/**
 * This class represents an observable object, and can hold multi 
 * EnumObserver object.
 * Each EnumObservable has a enum class as event type list,
 * while this EnumObservable is fire a event, the event most be once of the
 * constants in the Enum of this.
 * @param <E> is a Enum clas and is use as for specific the list of event.
 * @param <A> is the type of the argument can has.
 * @param <R> is the type the return value will has for the "fireObserver"
 */
public class EnumObservable<E extends Enum<E>, A, R> implements EnumObservableInterface<E, A, R> {

	private final java.util.EnumMap<E, java.util.Vector<EnumObserver<E, A, R>>> map;

	/**
	 * Private Entry class.
	 * @param <E>
	 * @param <R> 
	 */
	private static class ObsEntry<E, R> implements java.util.Map.Entry<E, R> {

		private E e;
		private R r;

		private ObsEntry(E e, R r) {
			this.e = e;
			this.r = r;
		}

		@Override
		public E getKey() {
			return this.e;
		}

		@Override
		public R getValue() {
			return this.r;
		}

		@Override
		public R setValue(R value) {
			throw new IllegalAccessError("Modify this is not allow.");
		}
	}

	/**
	 * Make a new EnumObservable with a Enum as event type scoures.
	 * While the EnumObservable is make a EnumObserver of same type 
	 * can be add to this for gets events of select type.
	 * The type is define by the keys in the Enum.
	 * @param keyType A Class Object of the type Enum this will use as a Events
	 */
	public EnumObservable(Class<E> keyType) {
		this.map = new java.util.EnumMap<E, java.util.Vector<EnumObserver<E, A, R>>>(keyType);
	}

	/**
	 * Add a EnumObserver to this EnumObservable with select event key
	 * @param key The event key this EnumObserver will listen for.
	 * @param observer The EnumObserver observer Object.
	 * @return True if this EnumObserver has be add, otherwise false.
	 */
	@Override
	public boolean addObserver(E key, EnumObserver<E, A, R> observer) {
		if (key == null || observer == null) {
			throw new NullPointerException();
		}
		synchronized (this.map) {
			if (this.map.containsKey(key)) {
				if (!this.map.get(key).contains(observer)) {
					this.map.get(key).add(observer);
					return true;
				}
			} else {
				final java.util.Vector<EnumObserver<E, A, R>> vec = new java.util.Vector<EnumObserver<E, A, R>>(5);
				vec.add(observer);
				this.map.put(key, vec);
			}
		}
		return false;
	}

	/**
	 * Remove a EnumObserver from this EnumObservable,
	 * This will remove the EnumObserver from all events type.
	 * @param observer The EnumObserver to remove.
	 * @return Return true if the EnumObserver has been removed from at
	 * least one event type, otherwise false.
	 */
	@Override
	public boolean removeObserver(EnumObserver<E, A, R> observer) {
		if (observer == null) {
			throw new NullPointerException();
		}
		boolean returnValue = false;
		synchronized (this.map) {
			for (java.util.Vector<EnumObserver<E, A, R>> vec : this.map.values()) {
				if (vec.remove(observer)) {
					returnValue = true;
				}
			}
		}
		return returnValue;
	}

	/**
	 * Remove a EnumObserver from this EnumObservable for select event type.
	 * @param key The event type to remove from.
	 * @param observer The EnumObserver to remove from.
	 * @return Return true if the EnumObserver has been removed the event type,
	 * otherwise false.
	 */
	@Override
	public boolean removeObserver(E key, EnumObserver<E, A, R> observer) {
		if (key == null || observer == null) {
			throw new NullPointerException();
		}
		synchronized (this.map) {
			if (this.map.containsKey(key)) {
				return this.map.get(key).remove(observer);
			}
		}
		return false;
	}

	/**
	 * Remove all listened observer from all events.
	 */
	public void removeAllObserver() {
		synchronized (this.map) {
			this.map.clear();
		}
	}

	/**
	 * Fire a event with a event key, and null as argument.
	 * @param key the event key.
	 * @return Return a Collection of the return value for each Objserver that has be notify.
	 */
	public java.util.Collection<java.util.Map.Entry<EnumObserver<E, A, R>, R>> fireObserver(E key) {
		return this.fireObserver(key, null);
	}

	/**
	 * Fire a event with a event key, and null as argument.
	 * @param key the event key.
	 * @param arg argument to be transferred to the Observer.
	 * @return Return a Collection of the return value for each Objserver that has be notify.
	 */
	public java.util.Collection<java.util.Map.Entry<EnumObserver<E, A, R>, R>> fireObserver(E key, A arg) {
		if (key == null) {
			throw new NullPointerException();
		}
		Object[] obsList = null;
		synchronized (this.map) {
			if (this.map.containsKey(key)) {
				obsList = this.map.get(key).toArray();
			}
		}


		if (obsList != null) {
			java.util.ArrayList<java.util.Map.Entry<EnumObserver<E, A, R>, R>> returnValue =
					  new java.util.ArrayList<java.util.Map.Entry<EnumObserver<E, A, R>, R>>(obsList.length);
			for (Object obs : obsList) {
				returnValue.add(
						  new ObsEntry<EnumObserver<E, A, R>, R>(
						  (EnumObserver<E, A, R>) obs,
						  ((EnumObserver<E, A, R>) obs).observerNotify(this, key, arg)));
			}
			return returnValue;
		}
		return new java.util.ArrayList<java.util.Map.Entry<EnumObserver<E, A, R>, R>>(0);
	}

	/**
	 * Get the numbers of Observers that listen on this Key Event
	 * @param key The key event to get numbers of Observers for.
	 * @return Return the number of Observers.
	 */
	public int getNumberOfObserver(E key) {
		if (this.map.containsKey(key)) {
			return this.map.get(key).size();
		}
		return 0;
	}
}
