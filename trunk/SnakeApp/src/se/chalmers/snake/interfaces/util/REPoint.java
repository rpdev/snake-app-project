package se.chalmers.snake.interfaces.util;

import java.io.Serializable;

/**
 * REPoint ( Radius Enum Point ) is use for store a data point for each Game REType, as Snake, Walls, Apple.
 */
public class REPoint extends XYPoint implements Serializable, Cloneable {

	private static final long serialVersionUID = -1408202542090251833L;

	public static enum REType {

		/**
		 * This is the normal item and that the player will try to collect.
		 */
		ITEM,
		/**
		 * This is one way, and if users are running into it, the player loses the game.
		 */
		WALL,
		/**
		 * This say the REPoint is a head segments of the player.
		 */
		HEADSEG,
		/**
		 * This say the REPoint is a body segments of the player. 
		 */
		BODYSEG,
		/**
		 * This say the REPoint is a tail segments of the player.
		 */
		TAILSEG
	};
	public final REType type;
	public final int radius;
	public final double angle;

	public REPoint(REType type, int xPoint, int yPoint, int radius, double angle) {
		super(xPoint, yPoint);
		if (type != null && radius >= 0) {
			this.type = type;
			this.radius = radius;
			this.angle = angle;
		} else {
			throw new NullPointerException("Null Error, or value out of rang.");
		}
	}

	public REPoint(REType type, XYPoint xyPoint, int radius, double angle) {
		super(xyPoint);
		if (type != null && radius >= 0) {
			this.type = type;
			this.radius = radius;
			this.angle = angle;
		} else {
			throw new NullPointerException("Null Error, or value out of rang.");
		}
	}

	/**
	 * Get the REPoint type.
	 * @return One of {@link REPoint.REType}
	 */
	public REType getType() {
		return this.type;
	}

	/**
	 * Return the Points radius.
	 * @return 0 to {@link Integer#MAX_VALUE}
	 */
	public int getRadius() {
		return this.radius;
	}

	/**
	 * Return the angle this REPoint are face on.
	 * @return 
	 */
	public double getAngle() {
		return this.angle;
	}

	/**
	 * Calculating the distance between two points, the calculating refer to the midpoint of each Point
	 * @param gp A point
	 * @return Return 0 to {@link Integer#MAX_VALUE}
	 * 
	 */
	public int calcDistance(REPoint gp) {
		if (gp == null) {
			throw new NullPointerException("GamePoint gp is null.");
		}
		int xx = Math.abs(this.x < gp.x ? (this.x - gp.x) : (gp.x - this.x));
		int yy = Math.abs(this.y - gp.y);
		return (int) Math.sqrt(xx * xx + yy * yy);
	}

	/**
	 * Test if this REPoint are collide with a other REPoint.
	 * @param gp
	 * @return 
	 */
	public boolean isCollideWith(REPoint gp) {
		if (gp == null) {
			throw new NullPointerException("GamePoint gp is null.");
		}
		int xx = Math.abs(this.x - gp.x);
		int yy = Math.abs(this.y - gp.y);
		int limit = (this.radius + gp.radius);
		return xx < limit && yy < limit && ((int) Math.sqrt(xx * xx + yy * yy)) < (limit);
	}

	@Override
	public String toString() {
		return "REPoint{xy=[" + this.x + ":" + this.y + "], type=" + type + ", radius=" + radius + '}';
	}
}
