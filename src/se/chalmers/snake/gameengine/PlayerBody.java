package se.chalmers.snake.gameengine;

import java.util.Iterator;
import java.util.LinkedList;
import se.chalmers.snake.interfaces.util.REPoint;
import se.chalmers.snake.interfaces.util.XYPoint;

/**
 * Make the player body, of this game, the player body is basce on a LinkedList of REPoint.
 */
class PlayerBody extends LinkedList<REPoint> {

	private final int bodySegmentRadius;
	private final XYPoint gameSize;
	private int bufferBodySegment;
	private int lengthSincLastAddSegment;

	public PlayerBody(XYPoint gameSize, XYPoint startPosition, double startAngle, int bodySegmentRadius, int startSegNumber, int startBufferSegNumber) {
		if (!(gameSize != null && gameSize.x > 0 && gameSize.y > 0)) {
			throw new IllegalArgumentException("The condition 'gameSize != null && gameSize.x > 0 && gameSize.y > 0' is not true.");
		}
		if (!(startPosition.x > 0 && startPosition.x < gameSize.x)) {
			throw new IllegalArgumentException("The condition 'startPosition.x>0 && startPosition.x<gameSize.x' is not true.");
		}
		if (!(startPosition.y > 0 && startPosition.y < gameSize.y)) {
			throw new IllegalArgumentException("The condition 'startPosition.y > 0 && startPosition.y < gameSize.y' is not true.");
		}
		if (!(bodySegmentRadius > 0 && startSegNumber > 1 && startAngle >= 0 && startAngle <= Math.PI * 2)) {
			throw new IllegalArgumentException("The condition 'bodySegmentRadius > 0 && startSegNumber > 1 && startAngle >= 0 && startAngle <= Math.PI * 2' is not true.");
		}


		this.gameSize = gameSize;
		this.bodySegmentRadius = bodySegmentRadius;
		this.bufferBodySegment = startBufferSegNumber;
		this.initBody(startPosition, startAngle, startSegNumber - 1);
	}

	/**
	 * Set up the player body while the map is make.
	 * @param point
	 * @param angle
	 * @param segCount 
	 */
	private void initBody(XYPoint point, double angle, int segCount) {
		super.add(new REPoint(REPoint.REType.HEADSEG, point, this.bodySegmentRadius));
		double mirrorAngle = angle > Math.PI ? angle - Math.PI : angle + Math.PI; // Get the Mirror angle.
		for (int i = 0; i < segCount; i++) {
			point = this.nextPoint(point, mirrorAngle, this.bodySegmentRadius);
			super.add(new REPoint(
					  (i < segCount - 1) ? REPoint.REType.BODYSEG : REPoint.REType.TAILSEG,
					  point,
					  this.bodySegmentRadius));

		}
	}

	/**
	 * Calc a new Point basc on the old point and a angle and length.
	 * The value will also be warp around if nead.
	 * @param oldPoint
	 * @param angle
	 * @param length
	 * @return 
	 */
	private XYPoint nextPoint(XYPoint oldPoint, double angle, int length) {
		int newX = oldPoint.x + (int) (length * Math.cos(angle));
		int newY = oldPoint.y + (int) (length * Math.sin(angle));
		if (newX <= 0) {
			newX = this.gameSize.x - newX;
		} else if (newX > this.gameSize.x) {
			newX = newX - this.gameSize.x;
		}
		if (newY <= 0) {
			newY = this.gameSize.y - newY;
		} else if (newY > this.gameSize.y) {
			newY = newY - this.gameSize.y;
		}
		return new XYPoint(newX, newY);
	}

	/**
	 * Calc a new Point basc on the old point and a angle and length.
	 * The value will also be warp around if nead.
	 * @param oldPoint
	 * @param angle
	 * @param length
	 * @return 
	 */
	private REPoint nextPoint(REPoint oldPoint, double angle, int length) {
		int newX = oldPoint.x + (int) (length * Math.cos(angle));
		int newY = oldPoint.y + (int) (length * Math.sin(angle));
		if (newX <= 0) {
			newX = this.gameSize.x - newX;
		} else if (newX > this.gameSize.x) {
			newX = newX - this.gameSize.x;
		}
		if (newY <= 0) {
			newY = this.gameSize.y - newY;
		} else if (newY > this.gameSize.y) {
			newY = newY - this.gameSize.y;
		}
		return new REPoint(oldPoint.type, newX, newY, oldPoint.radius);
	}

	/**
	 * Calc the angle from two XYpoints. 
	 * @param p1
	 * @param p2
	 * @return 
	 */
	private double getAngle(XYPoint p1, XYPoint p2) {
		return Math.atan2(p1.y - p2.y, p1.x - p2.x);
	}

	/**
	 * Move the player a step in given angle and length.
	 * @param angle
	 * @param length 
	 */
	public synchronized void step(double angle, int length) {
		this.lengthSincLastAddSegment += length;
		if (length == this.bodySegmentRadius) {
			this.step(angle);
			return;
		}
		REPoint append = null;
		REPoint oldPoint = null;
		for (int i = 0; i < super.size(); i++) {
			REPoint currentPoint = super.get(i);
			if (oldPoint != null) {
				angle = this.getAngle(oldPoint, currentPoint);
			}
			REPoint currentNewPoint = this.nextPoint(currentPoint, angle, length);
			if (currentPoint.getType() == REPoint.REType.TAILSEG) {
				if (this.bufferBodySegment > 0 && this.lengthSincLastAddSegment >= this.bodySegmentRadius) {
					double tailAngle = this.getAngle(currentPoint, currentNewPoint);
					append = this.nextPoint(currentNewPoint, tailAngle, this.bodySegmentRadius);
					this.lengthSincLastAddSegment = 0;
					this.bufferBodySegment--;
					currentNewPoint = new REPoint(REPoint.REType.BODYSEG, currentNewPoint, this.bodySegmentRadius);
				}
			}
			oldPoint = currentPoint;
			super.set(i, currentNewPoint);
		}
		if (append != null) {
			super.addLast(append);
		}
	}

	/**
	 * Move the player 1 fix step, each step has a length of BodySegmentRadius
	 * @param angle 
	 */
	public synchronized void step(double angle) {
		this.lengthSincLastAddSegment += this.bodySegmentRadius;
		REPoint topSeg = super.removeFirst();
		REPoint newSeg = new REPoint(REPoint.REType.BODYSEG, topSeg, this.bodySegmentRadius);
		super.addFirst(newSeg);
		super.addFirst(this.nextPoint(topSeg, angle, this.bodySegmentRadius));
		if (this.bufferBodySegment > 0) {
			this.bufferBodySegment--;
		} else {
			super.addLast(new REPoint(REPoint.REType.TAILSEG, super.removeLast(), this.bodySegmentRadius));
		}
	}

	/**
	 * Get the head of the player Body.
	 * The head is the main part and the only point that is nead to be test for collide.
	 * @return 
	 */
	public REPoint getHead() {
		return super.getFirst();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Iterator<REPoint> it = this.iterator();
		while (it.hasNext()) {
			REPoint rep = it.next();
			sb.append("[").append(rep.x).append(":").append(rep.y).append("]\n");
		}
		return "PlayerBody{BufferSeg=" + bufferBodySegment + ", BodyRadius=" + bodySegmentRadius + ", \n" + sb.toString() + "}";
	}
}
