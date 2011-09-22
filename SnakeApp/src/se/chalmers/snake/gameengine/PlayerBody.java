package se.chalmers.snake.gameengine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import se.chalmers.snake.interfaces.util.REPoint;
import se.chalmers.snake.interfaces.util.XYPoint;

/**
 * Make the player body, of this game, the player body is base on a LinkedList of REPoint.
 */
class PlayerBody implements Iterable<REPoint> {

	private class PBFPoint {

		private float x, y;
		private double angle;

		private PBFPoint(float x, float y, double angle) {
			this.x = x;
			this.y = y;
			this.angle = angle;
		}

		private PBFPoint(XYPoint p, double angle) {
			this.x = p.x;
			this.y = p.y;
			this.angle = angle;
		}

		private PBFPoint(PBFPoint fp) {
			this.x = fp.x;
			this.y = fp.y;
			this.angle = fp.angle;
		}
	}
	private final LinkedList<PBFPoint> seg;
	private final float bodySpaceSize;
	private final int bodySegmentRadius;
	private final int bodySegmentRadius2;
	private final XYPoint gameSize;
	private int bufferBodySegment;
	private int lengthSincLastAddSegment;
	private boolean isSelfCollision;

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
		this.isSelfCollision = false;
		this.seg = new LinkedList<PBFPoint>();
		this.gameSize = gameSize;
		this.bodySegmentRadius = bodySegmentRadius;
		this.bodySegmentRadius2 = 2 * this.bodySegmentRadius;
		this.bodySpaceSize = bodySegmentRadius;
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
		PBFPoint floatPoint = new PBFPoint(point, angle);
		this.seg.add(floatPoint);
		double mirrorAngle = angle > Math.PI ? angle - Math.PI : angle + Math.PI; // Get the Mirror angle.
		for (int i = 0; i < segCount; i++) {
			floatPoint = this.nextPoint(floatPoint, mirrorAngle, (int)this.bodySpaceSize);
			floatPoint.angle = angle;
			this.seg.add(floatPoint);

		}
	}

	/**
	 * Calc a new PBFPoint from the old, that the new points is move from the old with select lenght and angle.
	 * @param oldPoint
	 * @param angle
	 * @param length
	 * @return 
	 */
	private PBFPoint nextPoint(PBFPoint oldPoint, double angle, float length) {
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
		return new PBFPoint(newX, newY, angle);
	}

	/**
	 * Modify the PBFPoint to move select lenght in select angle
	 * @param point
	 * @param angle
	 * @param length 
	 */
	/*
	private void nextPointStep(PBFPoint point, double angle, int length) {
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
	 */
	/**
	 * Calc the angle from two XYpoints. 
	 * @param p1
	 * @param p2
	 * @return 
	 */
	/*
	private double getAngle(PBFPoint p1, PBFPoint p2) {
	if(Math.abs(p1.y - p2.y)>=bodySpaceSize*2 || Math.abs(p1.x - p2.x)>=bodySpaceSize*2) {
	return p2.angle;
	} else {
	p2.angle = Math.atan2(p1.y - p2.y, p1.x - p2.x);
	return p2.angle;
	}
	}
	 */
	/**
	 * Bug 1: The snake head is work, but while the head cross a wall,
	 * the body are turn a round to get close to the head.
	 * 
	 * 
	 */
	/**
	 * Move the player a step in given angle and length.
	 * ( If the length == bodySegmentRadius the OP will go much faster.
	 * @param angle
	 * @param length 
	 */
	public synchronized void step(double angle, int length) {
		this.lengthSincLastAddSegment += length;
		if (length == this.bodySpaceSize) {
			this.step(angle);
			return;
		}
		PBFPoint oldPoint = null;
		Iterator<PBFPoint> it = this.seg.iterator();
		float dy = 0;
		float dx = 0;
		float jumpStep = length;
		System.out.println("***************************");
		while (it.hasNext()) {
			PBFPoint point = it.next();

			if (oldPoint != null) {
				dy = oldPoint.y - point.y;
				dx = oldPoint.x - point.x;
				if ((dy < 0 ? -dy : dy) <= (bodySpaceSize+length)*2 && (dx < 0 ? -dx : dx) <= (bodySpaceSize+length)*2) {
					point.angle = Math.atan2(dy, dx);
					double space = Math.sqrt(Math.pow(dy, 2)+Math.pow(dx, 2));
					jumpStep = (float) (length-bodySpaceSize+space);
					
				} else {
					System.out.println("Jump");
					this.isSelfCollision=true;
					jumpStep = length;
				}
				System.out.println(jumpStep+" "+point.angle);
				
				oldPoint.x = point.x;
				oldPoint.y = point.y;
				angle = point.angle;
			} else {
				oldPoint = new PBFPoint(point);
			}
			//point.x = (float) (point.x + jumpStep * Math.cos(angle));
			//point.y = (float) (point.y + jumpStep * Math.sin(angle));
			point.x += jumpStep * Math.cos(angle);
			point.y += jumpStep * Math.sin(angle);
			if (point.x <= 0) {
				point.x = this.gameSize.x - point.x;
			} else if (point.x > this.gameSize.x) {
				point.x -= this.gameSize.x;
			}
			if (point.y <= 0) {
				point.y = this.gameSize.y - point.y;
			} else if (point.y > this.gameSize.y) {
				point.y -= this.gameSize.y;
			}

		}
		//Add new body segments.
		if (this.bufferBodySegment > 0 && this.lengthSincLastAddSegment >= this.bodySpaceSize) {
			double mirrorAngle = angle > Math.PI ? angle - Math.PI : angle + Math.PI; // Get the Mirror angle.
			PBFPoint newTail = this.nextPoint(this.seg.getLast(), mirrorAngle, this.bodySpaceSize);
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
		this.seg.addFirst(this.nextPoint(this.seg.getFirst(), angle,this.bodySpaceSize));
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
		PBFPoint floatPoint = this.seg.getFirst();
		return new REPoint(REPoint.REType.HEADSEG, (int) floatPoint.x, (int) floatPoint.y, this.bodySegmentRadius);
	}

	public REPoint getTail() {
		PBFPoint floatPoint = this.seg.getLast();
		return new REPoint(REPoint.REType.TAILSEG, (int) floatPoint.x, (int) floatPoint.y, this.bodySegmentRadius);
	}

	/**
	 * Get the Player Body Seg List.
	 * The list is a clone of the Private List of the maticmatic
	 * @return 
	 */
	public List<REPoint> get() {
		ArrayList<REPoint> rList = new ArrayList<REPoint>(this.seg.size());
		boolean isFirst = true;
		Iterator<PBFPoint> it = this.seg.iterator();
		while (it.hasNext()) {
			PBFPoint point = it.next();
			if (isFirst == true) {
				rList.add(new REPoint(REPoint.REType.HEADSEG, (int) point.x, (int) point.y, this.bodySegmentRadius));
				isFirst = false;
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
		Iterator<PBFPoint> it = this.seg.iterator();
		while (it.hasNext()) {
			PBFPoint rep = it.next();
			sb.append("[").append((int) rep.x).append(":").append((int) rep.y).append("]\n");
		}
		return "PlayerBody{Size=" + this.seg.size() + ", BufferSeg=" + this.bufferBodySegment + ", BodyRadius=" + this.bodySegmentRadius + ", \n" + sb.toString() + "}";
	}

	/**
	 * Test if some part of the body is collision with the head.
	 * The tre first segment will not be tests.
	 * @return 
	 */
	public synchronized boolean isSelfCollision() {
		return this.isSelfCollision;
		/*
		if (this.seg.size() > 3) {
			PBFPoint head = this.seg.getFirst();
			ListIterator<PBFPoint> it = this.seg.listIterator(3);
			while (it.hasNext()) {
				if (this.isCollision(it.next(), head)) {
					return false;
				}
			}

		}
		return false;
		 */
	}

	synchronized boolean isCollisionWith(REPoint point) {
		
		
		for (PBFPoint body : this.seg) {
			float xx = Math.abs(body.x - point.x);
			float yy = Math.abs(body.y - point.y);
			int rr = point.radius + this.bodySegmentRadius;
			if (xx < rr && yy < rr && Math.sqrt(xx * xx + yy * yy) < rr) {
				return true;
			}
		}
		return false;
	}

	private boolean isCollision(PBFPoint p1, PBFPoint p2) {
		float xx = Math.abs(p1.x - p2.x);
		float yy = Math.abs(p1.y - p2.y);
		return xx < this.bodySegmentRadius2 && yy < this.bodySegmentRadius2 && (Math.sqrt(xx * xx + yy * yy)) < (this.bodySegmentRadius2);
	}

	public void addSeg(int bodyGrowth) {
		if (bodyGrowth > 0) {
			this.bufferBodySegment += bodyGrowth;
		}
	}

	public int size() {
		return this.seg.size();
	}

	@Override
	public Iterator<REPoint> iterator() {
		return this.get().iterator();
	}
}
