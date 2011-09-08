package se.chalmers.snake.interfaces;

import java.util.List;
import se.chalmers.snake.interfaces.util.REPoint;
import se.chalmers.snake.interfaces.util.XYPoint;

/**
 *
 */
public interface LevelIC {
	public int getSpeed(int collectNumber);
	public int getSpeed(List<Integer> collectTime);
	
	public boolean  hasRachGoal(int collectNumber);
	public boolean  hasRachGoal(List<Integer> collectTime);
	
	public int getItemsCount(int collectNumber);
	public int getItemsCount(List<Integer> collectTime);
	
	
	public int getAddBodySegments(int collectNumber);
	public int getAddBodySegments(List<Integer> collectTime);
	
	
	public List<REPoint> getStaticElements();
	
	public XYPoint getGameFiledSize(); 
	
}
