package se.chalmers.snake.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import java.io.Serializable;

/**
 * The Storage class is used for saving application data in an 
 * persictent way, so that can be access even if the application
 * is terminated. This is done by utilizing two ways of saving 
 * data on the android system.
 * The first way used by {@link Storage#storePrimitive(Type, String, Object)}
 * and {@link Storage#getPrimitive(Type, String, Object)} uses
 * SharedPreferences and can therefore only store primitives including
 * String.
 * The second way can store and load any serializable object, the methods
 * using this is {@link Storage#storeObject(String, Serializable)} and
 * {@link Storage#getObject(String)}.
 */
public class Storage {

	private static final String PREFS_NAME = "SnakeApp";
	private final SharedPreferences settings;
	private final Activity activity;

	/**
	 * Constructor for the Storage class, to be able to store object
	 * those this class require access to an activity.
	 * @param activity A class that extends Activity for the application using this class.
	 */
	public Storage(Activity activity) {
		settings = activity.getSharedPreferences(PREFS_NAME, 0);
		this.activity = activity;
	}

	/**
	 * Store an primitive type, these types are booleans, floats, ints,
	 * longs, and strings. If the type is non of these will
	 * illegal argument be thrown.
	 * @param <T> Class type of the primitive value to be stored.
	 * @param type Class type of the primitive value to be stored.
	 * @param name Key for the value to be stored.
	 * @param value The actual value.
	 */
	public <T> void storePrimitive(Type type, String name, T value) {
		SharedPreferences.Editor editor = settings.edit();
		if (type == Boolean.class) {
			editor.putBoolean(name, (Boolean) value);
		} else if (type == Float.class) {
			editor.putFloat(name, (Float) value);
		} else if (type == Integer.class) {
			editor.putInt(name, (Integer) value);
		} else if (type == Long.class) {
			editor.putLong(name, (Long) value);
		} else if (type == String.class) {
			editor.putString(name, (String) value);
		} else {
			throw new IllegalArgumentException("Type: " + type + " , isn't a primitive type");
		}
		editor.commit();
	}

	/**
	 * Fetch an primitive type from memory, primitive types are booleans, floats, ints,
	 * longs, and strings. If the method is called with any other type will illegal argument
	 * be thrown. Note that values stored by {@link Storage#storeObject(Type, String, Object)}
	 * can't be fetched by this method.
	 * @param <T> Class type of the primitive value to be fetched.
	 * @param type Class type of the primitive value to be fetched.
	 * @param name The name of the key that should be fetched.
	 * @param defaultValue Value to return if the key wasen't found, this can only be null for String's.
	 * @return The fetched key's value or the default value if key not found.
	 */
	@SuppressWarnings("unchecked")
	public <T> T getPrimitive(Type type, String name, T defaultValue) {
		if (type == Boolean.class) {
			return (T) new Boolean(settings.getBoolean(name, (Boolean) defaultValue));
		} else if (type == Float.class) {
			return (T) new Float(settings.getFloat(name, (Float) defaultValue));
		} else if (type == Integer.class) {
			return (T) new Integer(settings.getInt(name, (Integer) defaultValue));
		} else if (type == Long.class) {
			return (T) new Long(settings.getLong(name, (Long) defaultValue));
		} else if (type == String.class) {
			return (T) settings.getString(name, (String) defaultValue);
		} else {
			throw new IllegalArgumentException("Type: " + type + " , isn't a primitive type");
		}
	}

	/**
	 * Store an object to a file as bytes, note that this function do <b>not</b> store
	 * values in the same place as {@link Storage#storePrimitive(Type, String, Object)}.
	 * @param <T> Class type of the primitive value to be stored.
	 * @param name Key for the value to be stored, used to fetch the item.
	 * @param value The actual value.
	 * @return True if successful otherwise false.
	 */
	public <T extends Serializable> boolean storeObject(String name, T value) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(activity.openFileOutput(name, Context.MODE_PRIVATE));
			oos.writeObject(value);
			oos.close();
			return true;
		} catch (IOException ex) {
			// http://source.android.com/source/code-style.html#dont-catch-generic-exception
		}
		return false;
	}

	/**
	 * Get the object that was stored by the name in the <code>name</code> argument.
	 * If the value isn't found or if any exception occur will the return value be
	 * null.
	 * @param <T> Class of the type to be returned.
	 * @param name The name of the object to be fetched, identical to was the value was stored as.
	 * @return Object of the type <code>T</code> if successful otherwise null.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Serializable> T getObject(String name) {
		try {
			ObjectInputStream ois = new ObjectInputStream(this.activity.openFileInput(name));
			return (T) ois.readObject();
		} catch (IOException ex) {
		} catch (ClassNotFoundException e) {
			// http://source.android.com/source/code-style.html#dont-catch-generic-exception
		}
		return null;
	}
}
