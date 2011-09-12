package se.chalmers.snake.leveldatabase;

import java.util.List;


import se.chalmers.snake.interfaces.LevelIC;
import se.chalmers.snake.interfaces.util.REPoint;
import se.chalmers.snake.interfaces.util.XYPoint;

public class Level1 implements LevelIC {

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

	@Override
	public XYPoint getMapSize() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public XYPoint getSnakeHeadStartLocation() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getAddItems(int totalCollected, int totalItemInGame) {
		throw new UnsupportedOperationException("Not supported yet.");
	}


}
