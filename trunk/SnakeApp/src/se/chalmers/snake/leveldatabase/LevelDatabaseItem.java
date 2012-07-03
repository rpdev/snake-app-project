package se.chalmers.snake.leveldatabase;

import android.content.res.AssetManager;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
/**
 * Class that contains information about one level, this information consist
 * of level name, filename, difficulty and possible access to an {@link InputStream}
 * for the file that contains info about the level.
 */
class LevelDatabaseItem {
    private final AssetManager am;
    final String name;
    final String fileName;
    final int level;

    /**
     * Create an instance of the class that contains information about a
     * level.
     *
     * @param fileName
     * @param name
     * @param level Difficulty rating.
     */
    LevelDatabaseItem(AssetManager am, String fileName, String name, int level) {
        this.am = am;
        this.fileName = fileName;
        this.name = name;
        this.level = level;
    }

    /**
     * Return an inputstream to the level file.
     *
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
