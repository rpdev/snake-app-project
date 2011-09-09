package se.chalmers.snake.gameengine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import se.chalmers.snake.interfaces.util.REPoint;
import se.chalmers.snake.interfaces.util.XYPoint;

/**
 * Make the player body, of this game, the player body is basce on a LinkedList of REPoint.
 */
class PlayerBody implements Iterable<REPoint> {

	private class FloatPoint {

		private float x, y;

		private FloatPoint(float x, float y) {
			this.x = x;
			this.y = y;
		}

		private FloatPoint(XYPoint p) {
			this.x = p.x;
			this.y = p.y;
		}

		private FloatPoint(FloatPoint fp) {
			this.x = fp.x;
			this.y = fp.y;
		}
	}
	private final LinkedList<FloatPoint> seg;
	private final int bodySegmentRadius;
	private final int bodySegmentRadius2;
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

		this.seg = new LinkedList<FloatPoint>();
		this.gameSize = gameSize;
		this.bodySegmentRadius = bodySegmentRadius;
		this.bodySegmentRadius2 = 2*this.bodySegmentRadius;
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
		//super.add(new REPoint(REPoint.REType.HEADSEG, point, this.bodySegmentRadius));
		FloatPoint floatPoint = new FloatPoint(point);
		this.seg.add(floatPoint);

		double mirrorAngle = angle > Math.PI ? angle - Math.PI : angle + Math.PI; // Get the Mirror angle.
		for (int i = 0; i < segCount; i++) {
			floatPoint = this.nextPoint(floatPoint, mirrorAngle, this.bodySegmentRadius);
			this.seg.add(floatPoint);

		}
	}

	private FloatPoint nextPoint(FloatPoint oldPoint, double angle, int length) {
		float newX = (float) (oldPoint.x + length * Math.cos(angle));
		float newY = (float) (oldPoint.y + length * Math.sin(angle));
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
		return new FloatPoint(newX, newY);
	}

	private void nextPointStep(FloatPoint point, double angle, int length) {
		float newX = (float) (point.x + length * Math.cos(angle));
		float newY = (float) (point.y + length * Math.sin(angle));
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
		point.x = newX;
		point.y = newY;
	}

	/**
	 * Calc the angle from two XYpoints. 
	 * @param p1
	 * @param p2
	 * @return 
	 */
	private double getAngle(FloatPoint p1, FloatPoint p2) {
		return Math.atan2(p1.y - p2.y, p1.x - p2.x);
	}

	/**
	 * Move the player a step in given angle and length.
	 * ( If the length == bodySegmentRadius the OP will go much faster.
	 * @param angle
	 * @param length 
	 */
	synchronized void step(double angle, int length) {
		this.lengthSincLastAddSegment += length;
		if (length == this.bodySegmentRadius) {
			this.step(angle);
			return;
		}
		FloatPoint oldPoint = null;
		Iterator<FloatPoint> it = this.seg.iterator();
		while (it.hasNext()) {
			FloatPoint point = it.next();
			if (oldPoint != null) {
				angle = this.getAngle(oldPoint, point);
				oldPoint.x = point.x;
				oldPoint.y = point.y;
			} else {
				oldPoint = new FloatPoint(point);
			}
			this.nextPointStep(point, angle, length);
		}

		if (this.bufferBodySegment > 0 && this.lengthSincLastAddSegment >= this.bodySegmentRadius) {
			double mirrorAngle = angle > Math.PI ? angle - Math.PI : angle + Math.PI; // Get the Mirror angle.
			FloatPoint newTail = this.nextPoint(this.seg.getLast(), mirrorAngle, this.bodySegmentRadius);
			this.bufferBodySegment--;
			this.lengthSincLastAddSegment = 0;
			this.seg.addLast(newTail);
		}
	}

	/**
	 * Move the player 1 fix step, each step has a length of BodySegmentRadius
	 * @param angle 
	 */
	private void step(double angle) {
		this.lengthSincLastAddSegment += this.bodySegmentRadius;
		this.seg.addFirst(this.nextPoint(this.seg.getFirst(), angle, this.bodySegmentRadius));
		if (this.bufferBodySegment > 0) {
			this.bufferBodySegment--;
			this.lengthSincLastAddSegment = 0;
		} else {
			this.seg.removeLast();
		}
	}

	/**
	 * Get the head of the player Body.
	 * The head is the main part and the only point that is nead to be test for collide.
	 * @return 
	 */
	public REPoint getHead() {
		FloatPoint floatPoint = this.seg.getFirst();
		return new REPoint(REPoint.REType.ITEM, (int) floatPoint.x, (int) floatPoint.y, this.bodySegmentRadius);
	}

	public List<REPoint> get() {
		ArrayList<REPoint> rList = new ArrayList<REPoint>(this.seg.size());
		boolean isFirst = true;
		Iterator<FloatPoint> it = this.seg.iterator();
		while (it.hasNext()) {
			FloatPoint point = it.next();
			if (isFirst == true) {
				rList.add(new REPoint(REPoint.REType.HEADSEG, (int) point.x, (int) point.y, this.bodySegmentRadius));
			} else if (it.hasNext()) {
				rList.add(new REPoint(REPoint.REType.BODYSEG, (int) point.x, (int) point.y, this.bodySegmentRadius));
			} else {
				rList.add(new REPoint(REPoint.REType.TAILSEG, (int) point.x, (int) point.y, this.bodySegmentRadius));
			}
		}
		return rList;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		Iterator<FloatPoint> it = this.seg.iterator();
		while (it.hasNext()) {
			FloatPoint rep = it.next();
			sb.append("[").append((int) rep.x).append(":").append((int) rep.y).append("]\n");
		}
		return "PlayerBody{Size=" + this.seg.size() + ", BufferSeg=" + this.bufferBodySegment + ", BodyRadius=" + this.bodySegmentRadius + ", \n" + sb.toString() + "}";
	}

	/**
	 * Test if some part of the body is collision with the head.
	 * The tre first segment will not be tests.
	 * @return 
	 */
	synchronized boolean isSelfCollision() {
		if (this.seg.size() > 3) {
			FloatPoint head = this.seg.getFirst();
			ListIterator<FloatPoint> it = this.seg.listIterator(3);
			while (it.hasNext()) {
				if (this.isCollision(it.next(), head)) {
					return false;
				}
			}

		}
		return false;
	}

	private boolean isCollision(FloatPoint p1, FloatPoint p2) {
		float xx=Math.abs(p1.x-p2.x);
		float yy=Math.abs(p1.y-p2.y);
		return xx<this.bodySegmentRadius2 && yy<this.bodySegmentRadius2 && (Math.sqrt(xx * xx + yy * yy)) > (this.bodySegmentRadius2);
	}

	void addSeg(int bodyGrowth) {
		if (bodyGrowth > 0) {
			this.bufferBodySegment += bodyGrowth;
		}
	}

	int size() {
		return this.seg.size();
	}

	@Override
	public Iterator<REPoint> iterator() {
		return this.get().iterator();
	}
}
