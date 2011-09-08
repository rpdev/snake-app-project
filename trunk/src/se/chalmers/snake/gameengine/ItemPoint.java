package se.chalmers.snake.gameengine;

import se.chalmers.snake.interfaces.util.REPoint;
import se.chalmers.snake.interfaces.util.XYPoint;

/**
 *
 */
class ItemPoint extends REPoint{
	int time=0;
	public ItemPoint(REType type, XYPoint xyPoint, int radius) {
		super(type, xyPoint, radius);
	}
	
	public ItemPoint(REType type, int x, int y, int radius) {
		super(type, x,y, radius);
	}
	
	void incTime() {
		this.time++;
	}
	
	int getTime() {
		return this.time;
	}
	
}
