package se.chalmers.snake.interfaces.util;

/**
 * A simple XY Point in the 2D space of data
 */
public class XYPoint {

	public final int x, y;

	public XYPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public XYPoint(XYPoint xyPoint) {
		if(xyPoint!=null) {
			this.x=xyPoint.x;
			this.y=xyPoint.y;
		} else {
			throw new NullPointerException();
		}
		
	}
	
	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		final XYPoint other = (XYPoint) obj;
		return this.x == other.x && this.y == other.y;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 43 * hash + this.x;
		hash = 43 * hash + this.y;
		return hash;
	}

	@Override
	public String toString() {
		return "XYPoint{" + "x=" + x + ", y=" + y + '}';
	}
	
	
}
