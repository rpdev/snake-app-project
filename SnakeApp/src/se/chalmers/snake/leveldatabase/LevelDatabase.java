package se.chalmers.snake.leveldatabase;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

import se.chalmers.snake.interfaces.LevelDatabaseIC;
import se.chalmers.snake.interfaces.LevelIC;
import android.app.Activity;
import android.content.res.AssetManager;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LevelDatabase implements LevelDatabaseIC {

	private final String PATH = "levels";
	private final AssetManager am;
	private final List<String> levelNameList = new ArrayList<String>();
	private final Map<String, LevelDatabaseData> levelNames = new HashMap<String, LevelDatabaseData>();
	private final Map<Integer, LevelDatabaseData> levelValues = new TreeMap<Integer, LevelDatabaseData>();

	public LevelDatabase(Activity activty) {
		this.am = activty.getAssets();
		try {
			loadFiles(this.am.list(PATH));
		} catch (IOException e) {
		}
	}

	@Override
	public LevelIC getByLevel(int level) {
		LevelDatabaseData data = levelValues.get(level);
		if (data != null) //return new Level(data);
		{
			try {
				return new XMLLevel(data);
			} catch (Exception ex) {
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
			int levelID = this.levelNames.get(level).level+1;
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

	class LevelDatabaseData {

		final String name, fileName;
		final int level;

		private LevelDatabaseData(String fileName, String name, int level) {
			this.fileName = fileName;
			this.name = name;
			this.level = level;
		}

		InputStream getInputSteam() {
			try {
				return LevelDatabase.this.am.open(fileName);
			} catch (IOException e) {
			}
			return null;
		}
	}
}
