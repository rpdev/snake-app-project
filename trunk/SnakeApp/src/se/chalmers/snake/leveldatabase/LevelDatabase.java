package se.chalmers.snake.leveldatabase;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

import se.chalmers.snake.interfaces.LevelDatabaseIC;
import se.chalmers.snake.interfaces.LevelIC;
import android.app.Activity;
import android.content.res.AssetManager;

public class LevelDatabase implements LevelDatabaseIC {
	private final String PATH="levels";
	private final AssetManager am;
	private final HashMap<String, Data> levelnames = new HashMap<String, Data>();
	private final HashMap<Integer, Data> levelvalues = new HashMap<Integer, Data>();
	private LevelIC defaultLevel = null;
	
	public LevelDatabase(Activity activty){
		am = activty.getAssets();
		try {
			loadFiles(am.list(PATH));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public LevelIC getByLevel(int level) {
		Data data = levelvalues.get(level);
		if(data != null)
			return new Level(data);
		else{
			if(defaultLevel == null)
				defaultLevel = new LevelDefault();
			return defaultLevel;
		}
	}

	@Override
	public LevelIC getByName(String name) {
		Data data = levelnames.get(name);
		if(data != null)
			return new Level(data);
		else{
			if(defaultLevel == null)
				defaultLevel = new LevelDefault();
			return defaultLevel;
		}
	}
	
	@Override
	public LevelIC getDefaultLevel(){
		if(defaultLevel == null)
			defaultLevel = new LevelDefault();
		return defaultLevel;
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

	private void loadFiles(String[] files){
		for(String file : files){
			if(file.contains(".") && file.substring(file.lastIndexOf('.')+1).equalsIgnoreCase("XML")){
				int level = Integer.parseInt(file.substring(file.indexOf(' '), file.indexOf('.')).trim());
				String name = file.substring(0, file.lastIndexOf('.'));
				Data data = new Data(PATH+"/"+file, name,level);
				levelnames.put(name, data);
				levelvalues.put(level, data);
			}
		}			
	}
	
	class Data{
		final String name, fileName;
		final int level;
		
		private Data(String fileName, String name, int level){
			this.fileName = fileName;
			this.name = name;
			this.level = level;
		}
		
		InputStream getInputSteam(){
			try {
				return am.open(fileName);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
