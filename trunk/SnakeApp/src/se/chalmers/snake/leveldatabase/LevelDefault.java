package se.chalmers.snake.leveldatabase;

import java.util.ArrayList;
import java.util.List;

import se.chalmers.snake.interfaces.LevelIC;
import se.chalmers.snake.interfaces.util.REPoint;
import se.chalmers.snake.interfaces.util.XYPoint;

/**
 * The default level is returned when another level isn't found,
 * it dosen't contain any obstacles it's simply a 'blank' map.
 */
public class LevelDefault implements LevelIC {

	private final XYPoint MAP_SIZE = new XYPoint(150, 200);
	private final int RADIUS = 5;

	@Override
	public String getLevelName() {
		return "Default level";
	}

	@Override
	public String getLevelDescription() {
		return "Catch all apples";
	}

	@Override
	public int getLevel() {
		return 1;
	}

	@Override
	public XYPoint getMapSize() {
		return MAP_SIZE;
	}

	@Override
	public int getSnakeStartLength() {
		return 3;
	}

	@Override
	public XYPoint getSnakeHeadStartLocation() {
		return new XYPoint(MAP_SIZE.x / 2, MAP_SIZE.y / 2);
	}

	@Override
	public double getStartAngle() {
		return Math.PI / 2;
	}

	@Override
	public List<REPoint> getObstacles() {
		return new ArrayList<REPoint>();
	}

	@Override
	public int getPlayerBodyWidth() {
		return RADIUS;
	}

	@Override
	public int getItemsRadius() {
		return RADIUS;
	}

	@Override
	public float getSpeed(List<Integer> collectTime) {
		return 3.0f;
	}

	@Override
	public boolean hasReachedGoal(List<Integer> collectTime) {
		return false;
	}

	@Override
	public int getAddItems(int totalCollected, int totalItemInGame) {
		if (totalItemInGame == 0) {
			return 1;
		}
		return 0;
	}

	@Override
	public int getBodyGrowth(int collectTime, int totalCollected) {
		return 1;
	}
}
