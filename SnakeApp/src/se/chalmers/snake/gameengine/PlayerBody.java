package se.chalmers.snake.gameengine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import se.chalmers.snake.interfaces.util.REPoint;
import se.chalmers.snake.interfaces.util.XYPoint;

/**
 * Make the player body, of this game, the player body is base on a LinkedList of REPoint.
 * 
 */
class PlayerBody implements Iterable<REPoint> {

	/**
	 * Private REPoint for the body.
	 */
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

	/**
	 * Make a new Player Body 
	 * @param gameSize The Game map size, use for warp around the body on the egel.
	 * @param startPosition The start pos of the body on the game map
	 * @param startAngle The start angle the body will be place on, the angle give the dir outplace body part
	 * @param bodySegmentRadius The radius for each body segment cirel.
	 * @param startSegNumber How long the body will be on start
	 * @param startBufferSegNumber How much will the body buffer, the buffer is body seg that will be put on the player 
	 * while the player are has.
	 */
	public PlayerBody(XYPoint gameSize, XYPoint startPosition, double startAngle, int bodySegmentRadius, int startSegNumber, int startBufferSegNumber) {
		if (!(gameSize != null && gameSize.x > 0 && gameSize.y > 0)) {
			throw new IllegalArgumentException("The condition 'gameSize != null && gameSize.x > 0 && gameSize.y > 0' is not true, the value is " + gameSize + ".");
		}
		if (!(startPosition.x > 0 && startPosition.x < gameSize.x)) {
			throw new IllegalArgumentException("The condition 'startPosition.x>0 && startPosition.x<gameSize.x' is not true, the value is " + startPosition + ".");
		}
		if (!(startPosition.y > 0 && startPosition.y < gameSize.y)) {
			throw new IllegalArgumentException("The condition 'startPosition.y > 0 && startPosition.y < gameSize.y' is not true, the value is " + startPosition + ".");
		}
		if (!(bodySegmentRadius > 0 && startSegNumber > 1 && startAngle >= 0 && startAngle <= Math.PI * 2)) {
			throw new IllegalArgumentException("The condition 'bodySegmentRadius > 0 && startSegNumber > 1 && startAngle >= 0 && startAngle <= Math.PI * 2' is not true, the value is " + startAngle + ".");
		}

		this.seg = new LinkedList<PBFPoint>();
		this.gameSize = gameSize;
		// Set how long space betwin each body seg will be
		this.bodySpaceSize = (float) (bodySegmentRadius*1.15);
		this.bodySegmentRadius = bodySegmentRadius;
		this.bodySegmentRadius2 = 2 * this.bodySegmentRadius;
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
		//double mirrorAngle = angle > Math.PI ? angle - Math.PI : angle + Math.PI; // Get the Mirror angle.
		//double mirrorAngle = angle > Math.PI ? angle - Math.PI : angle + Math.PI; // Get the Mirror angle.
		for (int i = 0; i < segCount; i++) {
			floatPoint = this.nextPoint(floatPoint, angle, (int) this.bodySpaceSize);
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
		return new PBFPoint(
				  (float) ((this.gameSize.x + (oldPoint.x + length * Math.cos(angle))) % this.gameSize.x),
				  (float) ((this.gameSize.y + (oldPoint.y + length * Math.sin(angle))) % this.gameSize.y),
				  angle);
	}

	/**
	 * Call this for move the body 1 step in given angle and length.
	 * @param angle is on rad and has normal value 0 to 2*Math.PI
	 * @param length 
	 */
	public synchronized void step(double angle, int length) {
		this.lengthSincLastAddSegment += length;
		while (length >= this.bodySpaceSize) {
			this.fixStep(angle);
			length -= this.bodySpaceSize;
		}
		if (length == 0) {
			return;
		}
		PBFPoint oldPoint = null;
		Iterator<PBFPoint> it = this.seg.iterator();
		float dy = 0;
		float dx = 0;


		float jumpStep = length;
		while (it.hasNext()) {
			PBFPoint point = it.next();

			if (oldPoint != null) {
				float XP = oldPoint.x - point.x;
				float YP = oldPoint.y - point.y;
				float XM = ((point.x < oldPoint.x) ? -this.gameSize.x : this.gameSize.x) + XP;
				float YM = ((point.y < oldPoint.y) ? -this.gameSize.y : this.gameSize.y) + YP;
				dx = Math.abs(XP) < Math.abs(XM) ? XP : XM;
				dy = Math.abs(YP) < Math.abs(YM) ? YP : YM;

				point.angle = Math.atan2(dy, dx);
				double space = Math.sqrt(dy * dy + dx * dx);
				jumpStep = (float) (length - this.bodySpaceSize + space);
				oldPoint.x = point.x;
				oldPoint.y = point.y;
				angle = point.angle;
			} else {
				oldPoint = new PBFPoint(point);
				point.angle = angle;
			}

			point.x = (float) ((this.gameSize.x + point.x + jumpStep * Math.cos(angle)) % this.gameSize.x);
			point.y = (float) ((this.gameSize.y + point.y + jumpStep * Math.sin(angle)) % this.gameSize.y);
		} // End Loop
		//Add new body segments.
		if (this.bufferBodySegment > 0 && this.lengthSincLastAddSegment > this.bodySpaceSize) {
			double mirrorAngle = angle > Math.PI ? angle - Math.PI : angle + Math.PI; // Get the Mirror angle.
			PBFPoint newTail = this.nextPoint(this.seg.getLast(), mirrorAngle, this.bodySpaceSize);
			newTail.angle = angle;
			this.bufferBodySegment--;
			this.lengthSincLastAddSegment = 0;
			this.seg.addLast(newTail);
		}
	}

	/**
	 * Move the player 1 fix step, each step has a length of bodySpaceSize
	 * @param angle 
	 */
	private void fixStep(double angle) {
		this.seg.addFirst(this.nextPoint(this.seg.getFirst(), angle, this.bodySpaceSize));
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
		return new REPoint(REPoint.REType.HEADSEG, (int) floatPoint.x, (int) floatPoint.y, this.bodySegmentRadius, floatPoint.angle);
	}

	/**
	 * Get the tail of the player body,
	 * 
	 * @return 
	 */
	public REPoint getTail() {
		PBFPoint floatPoint = this.seg.getLast();
		return new REPoint(REPoint.REType.TAILSEG, (int) floatPoint.x, (int) floatPoint.y, this.bodySegmentRadius, floatPoint.angle);
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
				rList.add(new REPoint(REPoint.REType.HEADSEG, (int) point.x, (int) point.y, this.bodySegmentRadius, point.angle));
				isFirst = false;
			} else if (it.hasNext()) {
				rList.add(new REPoint(REPoint.REType.BODYSEG, (int) point.x, (int) point.y, this.bodySegmentRadius, point.angle));
			} else {
				rList.add(new REPoint(REPoint.REType.TAILSEG, (int) point.x, (int) point.y, this.bodySegmentRadius, point.angle));
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
		int startCollied = 0;
		if (this.seg.size() > 1) {
			PBFPoint head = this.seg.getFirst();
			for (Iterator<PBFPoint> it = this.seg.iterator(); it.hasNext();) {
				if (startCollied < 3) {
					startCollied += this.isCollision(head, it.next()) ? 0 : 1;
				} else if (this.isCollision(head, it.next())) {
					return true;
				}
			}

		}
		return false;

	}

	/**
	 * Test if a select point are collision with none of the bodyparts.
	 * @param point
	 * @return 
	 */
	public synchronized boolean isCollisionWith(REPoint point) {
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

	/**
	 * Add a new number of seg to the player, the seg will be add while the player are move.
	 * @param bodyGrowth 
	 */
	public void addSeg(int bodyGrowth) {
		if (bodyGrowth > 0) {
			this.bufferBodySegment += bodyGrowth;
		}
	}

	/**
	 * Return how many seg the player body is now.
	 * @return 
	 */
	public int size() {
		return this.seg.size();
	}

	/**
	 * Return a Iterator<REPoint> for all bodysegment in the player body.
	 * @return u
	 */
	@Override
	public Iterator<REPoint> iterator() {
		return this.get().iterator();
	}
}
