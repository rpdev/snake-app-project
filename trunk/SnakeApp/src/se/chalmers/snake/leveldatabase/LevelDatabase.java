package se.chalmers.snake.leveldatabase;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import se.chalmers.snake.interfaces.LevelDatabaseIC;
import se.chalmers.snake.interfaces.LevelIC;
import android.app.Activity;
import android.content.res.AssetManager;

/**
 * LevelDatabase is a class that implements {@link LevelDatabaseIC},
 * it provide access to the level files or if a level don't exist does it provide
 * a default level.
 */
public class LevelDatabase implements LevelDatabaseIC {

	private final String PATH = "levels";
	private final AssetManager am;
	private final List<String> levelNameList = new ArrayList<String>();
	private final Map<String, LevelDatabaseData> levelNames = new HashMap<String, LevelDatabaseData>();
	private final Map<Integer, LevelDatabaseData> levelValues = new TreeMap<Integer, LevelDatabaseData>();

	/**
	 * Constructor for {@link LevelDatabase} when it's used will
	 * it scan for level files in the assets folder.
	 * @param activty Activity is needed for accessing the assets folder.
	 */
	public LevelDatabase(Activity activty) {
		am = activty.getAssets();
		try {
			loadFiles(am.list(PATH));
		} catch (IOException e) {
		}
	}

	@Override
	public LevelIC getByLevel(int level) {
		LevelDatabaseData data = levelValues.get(level);
		if (data != null) { //return new Level(data);
			try {
				return new XMLLevel(data);
			} catch (IOException e) {
				// http://source.android.com/source/code-style.html#dont-catch-generic-exception
			}
		}
		return new LevelDefault();
	}

	@Override
	public LevelIC getByName(String name) {
		LevelDatabaseData data = levelNames.get(name);
		if (data != null) {
			try {
				return new XMLLevel(data);
			} catch (Exception ex) {
			}
		}
		return new LevelDefault();
	}

	@Override
	public LevelIC getDefaultLevel() {
		return new LevelDefault();
	}

	@Override
	public int[] getLevelListByLevel() {
		Iterator<Integer> it = levelValues.keySet().iterator();
		int[] array = new int[levelValues.size()];
		int i = 0;
		while (it.hasNext()) {
			array[i++] = it.next();
		}
		return array;
	}

	@Override
	public String[] getLevelListByName() {
		return levelNameList.toArray(new String[levelNameList.size()]);
	}

	/**
	 * Iterate over the files in <code>files</code> and the level files
	 * path into memory, this function won't search for levels in subfolders.
	 * @param files Files to be scanned.
	 */
	private void loadFiles(String[] files) {
		for (String file : files) {
			if (file.contains(".") && file.substring(file.lastIndexOf('.') + 1).equalsIgnoreCase("XML")) {
				int level = Integer.parseInt(file.substring(file.indexOf(' '), file.indexOf('.')).trim());
				String name = file.substring(0, file.lastIndexOf('.'));
				LevelDatabaseData data = new LevelDatabaseData(this.PATH + "/" + file, name, level);
				this.levelNames.put(name, data);
				this.levelValues.put(level, data);
			}
		}
		Iterator<LevelDatabaseData> it = levelValues.values().iterator();
		while (it.hasNext()) {
			this.levelNameList.add(it.next().name);
		}
	}

	@Override
	public String getNextLevel(String level) {
		if (this.levelNames.containsKey(level)) {
			int levelID = this.levelNames.get(level).level + 1;
			if (this.levelValues.containsKey(levelID)) {
				return this.levelValues.get(levelID).name;
			}
		}
		return null;
	}

	@Override
	public int getNextLevel(int level) {
		if (this.levelValues.containsKey(level + 1)) {
			return level + 1;
		} else {
			return -1;
		}
	}

	/**
	 * Class that contains information about one level, this
	 * information consist of level name, filename, difficulty and possible
	 * access to an {@link InputStream} for the file that contains
	 * info about the level.
	 */
	class LevelDatabaseData {

		final String name, fileName;
		final int level;

		/**
		 * Create an instance of the class that contains information about
		 * a level.
		 * @param fileName
		 * @param name
		 * @param level Difficulty rating.
		 */
		private LevelDatabaseData(String fileName, String name, int level) {
			this.fileName = fileName;
			this.name = name;
			this.level = level;
		}

		/**
		 * Return an inputstream to the level file. 
		 * @return Inputstream to the file.
		 */
		InputStream getInputSteam() {
			try {
				return am.open(fileName);
			} catch (IOException e) {
			}
			return null;
		}
	}
}
