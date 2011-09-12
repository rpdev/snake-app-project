package se.chalmers.snake.leveldatabase;

import java.util.List;

import se.chalmers.snake.interfaces.LevelIC;
import se.chalmers.snake.interfaces.util.REPoint;
import se.chalmers.snake.interfaces.util.XYPoint;
import se.chalmers.snake.leveldatabase.LevelDatabase.Data;

class Level implements LevelIC {

	Level(Data data) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getAddItems(int totalCollected, int totalItemInGame) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBodyGrowth(int collectTime, int totalCollected) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getItemsRadius() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getLevelDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLevelName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XYPoint getMapSize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<REPoint> getObstacles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPlayerBodyWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public XYPoint getSnakeHeadStartLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSnakeStartLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSpeed(List<Integer> collectTime) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getStartAngle() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasReachedGoal(List<Integer> collectTime) {
		// TODO Auto-generated method stub
		return false;
	}

}
