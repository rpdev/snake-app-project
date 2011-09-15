package se.chalmers.snake.leveldatabase;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Iterator;

import android.os.Environment;

import se.chalmers.snake.interfaces.LevelDatabaseIC;
import se.chalmers.snake.interfaces.LevelIC;

public class LevelDatabase implements LevelDatabaseIC {
	private static LevelDatabase instance;
	private final File PATH;
	private final FilenameFilter FILTER  = new FilenameFilter() {			
		@Override
		public boolean accept(File dir, String filename) {
			if((new File(dir, filename)).isDirectory())
				return true;
			String n = filename;
			if(n.contains(".") && n.substring(n.lastIndexOf('.')+1).equalsIgnoreCase("XML"))
				return true;
			else
				return false;
		}
	};
	private final HashMap<String, Data> levelnames = new HashMap<String, Data>();
	private final HashMap<Integer, Data> levelvalues = new HashMap<Integer, Data>();
	
	private LevelDatabase(){
		instance = this;
		PATH = Environment.getExternalStorageDirectory();
		if(PATH.isFile())
			throw new IllegalArgumentException("Database error: " + PATH.getAbsolutePath() + " is a file");
		loadFiles(PATH);
	}
	
	public static LevelDatabaseIC getInstance(){
		if(instance == null)
			new LevelDatabase();
		return instance;
	}
	
	@Override
	public LevelIC getByLevel(int level) {
		return new Level(levelvalues.get(level));
	}

	@Override
	public LevelIC getByName(String name) {
		return new Level(levelnames.get(name));
	}

	@Override
	public int[] getLevelListByLevel() {
		Iterator<Integer> it = levelvalues.keySet().iterator();
		int[] array = new int[levelvalues.size()];
		int i=0;
		while(it.hasNext())
			array[i++] = it.next();
		return array;
	}

	@Override
	public String[] getLevelListByName() {
		return levelnames.keySet().toArray(new String[levelnames.size()]);
	}

	private void loadFiles(File f){
		if(f.isDirectory())
			for(File fi : f.listFiles(FILTER))
				loadFiles(fi);
		else{
			String name = f.getName();
			int level = Integer.parseInt(name.substring(name.indexOf(' '), name.indexOf('.')).trim());
			Data data = new Data(f, name, level);
			levelnames.put(name, data);
			levelvalues.put(level, data);
		}
			
	}
	
	class Data{
		final File file;
		final String name;
		final int level;
		
		private Data(File file, String name, int level){
			this.file = file;
			this.name = name;
			this.level = level;
		}
	}
}
