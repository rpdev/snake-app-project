package se.chalmers.snake.interfaces.util;

import java.io.Serializable;

/**
 * A simple XY Point in the 2D space of data
 * The XYPoint is not editable and support Clone and Serialise of data.
 */
public class XYPoint implements Cloneable, Serializable {

	private static final long serialVersionUID = -4040845416777153791L;
	public final int x, y;

	/**
	 * Make a new XYPoint that hold a x and y as int
	 * @param x
	 * @param y 
	 */
	public XYPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Make a new XYPoint that copy the value from a other XYPoint
	 * @param xyPoint 
	 */
	public XYPoint(XYPoint xyPoint) {
		if (xyPoint != null) {
			this.x = xyPoint.x;
			this.y = xyPoint.y;
		} else {
			throw new NullPointerException();
		}

	}

	/**
	 * Get the X value.
	 * @return 
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * Get the Y value.
	 * @return 
	 */
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
		return (129 + this.x) * 43 + this.y;
	}

	@Override
	public String toString() {
		return "XYPoint{" + "x=" + x + ", y=" + y + '}';
	}
}
