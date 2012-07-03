package se.chalmers.snake.leveldatabase;

import java.io.IOException;
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
import android.util.Log;
import java.util.*;

/**
 * LevelDatabase is a class that implements {@link LevelDatabaseIC}, it provide
 * access to the level files or if a level don't exist does it provide a default
 * level.
 */
public class LevelDatabase implements LevelDatabaseIC {

    private final String PATH = "levels";
    private final AssetManager am;
    private final List<String> levelNameList = new ArrayList<String>();
    private final Map<String, LevelDatabaseItem> levelNames = new HashMap<String, LevelDatabaseItem>();
    private final List<LevelDatabaseItem> levelIDs = new ArrayList<LevelDatabaseItem>();

    /**
     * Constructor for {@link LevelDatabase} when it's used will it scan for
     * level files in the assets folder.
     *
     * @param activty Activity is needed for accessing the assets folder.
     */
    public LevelDatabase(Activity activty) {
        this.am = activty.getAssets();
        try {
            loadFiles(this.am.list(PATH));
        } catch (IOException e) {
        }
    }

    @Override
    public LevelIC getByLevel(int level) {
        LevelDatabaseItem data = levelIDs.get(level);
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
        LevelDatabaseItem data = levelNames.get(name);
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
        int[] array = new int[this.levelIDs.size()];
        for(int i=0;i<this.levelIDs.size();i++) {
            array[i]=i;
        }
        return array;
    }

    @Override
    public String[] getLevelListByName() {
        return levelNameList.toArray(new String[levelNameList.size()]);
    }

    /**
     * Iterate over the files in
     * <code>files</code> and the level files path into memory, this function
     * won't search for levels in subfolders.
     *
     * @param files Files to be scanned.
     */
    private void loadFiles(String[] files) {
        Arrays.sort(files, new Comparator<String>() {

            public int compare(String t0, String t1) {
                return t0.compareToIgnoreCase(t1);
            }
        });


        for (String file : files) {
            if (file != null && file.matches("([0-9]{3})(.*)(\\.xml)")) {
                String levelName = file.substring(3, file.length() - 4).trim();
                int levelID = Integer.parseInt(file.substring(0, 3));
                LevelDatabaseItem data = new LevelDatabaseItem(this.am, this.PATH + "/" + file, levelName, levelID);
                this.levelNames.put(levelName, data);
                this.levelIDs.add(data);
                this.levelNameList.add(levelName);
            }
        }
        
        Log.d("LevelDatabase",this.toString());

    }

    @Override
    public String getNextLevel(String level) {
        int cclevel = this.levelNameList.indexOf(level);
        if (cclevel >= 0 && cclevel + 1 < this.levelNameList.size()) {
            return this.levelNameList.get(cclevel + 1);
        }

        return null;
    }

    @Override
    public int getNextLevel(int level) {
        if (this.levelIDs.size() < level + 1) {
            return level + 1;
        } else {
            return -1;
        }
    }

    @Override
    public String toString() {
        return "LevelDatabase{levelNameList=" + levelNameList + ", levelNames=" + levelNames + ", levelIDs=" + levelIDs + '}';
    }
    
    
}
