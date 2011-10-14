package se.chalmers.snake.mastercontroller;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import se.chalmers.snake.interfaces.LevelHistoryIC;

class LevelHistory implements LevelHistoryIC, Serializable {

	private static final long serialVersionUID = -1153388461696947441L;
	public static final String SAVE_NAME = "levelhistory";
	private final Set<String> set;

	public LevelHistory() {
		this.set = new HashSet<String>();

	}

	public void set(String levelName) {
		if (levelName != null) {
			this.set.add(levelName);
			ControlResources.get().getStorage().storeObject(SAVE_NAME, this);
		}
	}

	public boolean is(String levelName) {
		return this.set.contains(levelName);
	}

	public void clear() {
		this.set.clear();
		ControlResources.get().getStorage().storeObject(SAVE_NAME, this);
	}
}
