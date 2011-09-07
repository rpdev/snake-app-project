package se.chalmers.snake.gameengine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import se.chalmers.snake.interfaces.util.REPoint;
import se.chalmers.snake.interfaces.util.XYPoint;

/**
 *
 */
class PlayerBody implements List<REPoint> {

	private final int bodySegmentRadius;
	private LinkedList<REPoint> bodySeg = new LinkedList<REPoint>();
	private final XYPoint gameSize;
	private int bufferBodySegment;

	public PlayerBody(XYPoint gameSize, XYPoint startPosition, double startAngle, int bodySegmentRadius, int startSegNumber, int startBufferSegNumber) {
		if (!(gameSize != null && gameSize.getX() > 0 && gameSize.getY() > 0)) {
			throw new IllegalArgumentException("The condition 'gameSize != null && gameSize.getX() > 0 && gameSize.getY() > 0' is not true.");
		}
		if (!(startPosition.getX() > 0 && startPosition.getX() < gameSize.getX())) {
			throw new IllegalArgumentException("The condition 'startPosition.getX()>0 && startPosition.getX()<gameSize.getX()' is not true.");
		}
		if (!(startPosition.getY() > 0 && startPosition.getY() < gameSize.getY())) {
			throw new IllegalArgumentException("The condition 'startPosition.getY() > 0 && startPosition.getY() < gameSize.getY()' is not true.");
		}
		if (!(bodySegmentRadius > 0 && startSegNumber > 1 && startAngle >= 0 && startAngle <= Math.PI * 2)) {
			throw new IllegalArgumentException("The condition 'bodySegmentRadius > 0 && startSegNumber > 1 && startAngle >= 0 && startAngle <= Math.PI * 2' is not true.");
		}
		this.gameSize = gameSize;
		this.bodySegmentRadius = bodySegmentRadius;
		this.bufferBodySegment = startBufferSegNumber;
		this.initBody(startPosition, startAngle, startSegNumber - 1);
	}

	private void initBody(XYPoint point, double angle, int segCount) {
		this.bodySeg.add(new REPoint(REPoint.REType.HEADSEG, point, this.bodySegmentRadius));
		double mirrorAngle = angle > Math.PI ? angle - Math.PI : angle + Math.PI; // Get the Mirror angle.
		for (int i = 0; i < segCount; i++) {
			point = this.nextPoint(point, mirrorAngle, this.bodySegmentRadius);
			this.bodySeg.add(new REPoint(
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
		int newX = oldPoint.getX() + (int) (length * Math.cos(angle));
		int newY = oldPoint.getY() + (int) (length * Math.sin(angle));
		if (newX <= 0) {
			newX = this.gameSize.getX() - newX;
		} else if (newX > this.gameSize.getX()) {
			newX = newX - this.gameSize.getX();
		}
		if (newY <= 0) {
			newY = this.gameSize.getY() - newY;
		} else if (newY > this.gameSize.getY()) {
			newY = newY - this.gameSize.getY();
		}
		return new XYPoint(newX, newY);
	}

	private REPoint nextPoint(REPoint oldPoint, double angle, int length) {
		int newX = oldPoint.getX() + (int) (length * Math.cos(angle));
		int newY = oldPoint.getY() + (int) (length * Math.sin(angle));
		if (newX <= 0) {
			newX = this.gameSize.getX() - newX;
		} else if (newX > this.gameSize.getX()) {
			newX = newX - this.gameSize.getX();
		}
		if (newY <= 0) {
			newY = this.gameSize.getY() - newY;
		} else if (newY > this.gameSize.getY()) {
			newY = newY - this.gameSize.getY();
		}
		return new REPoint(oldPoint.getType(), newX, newY, oldPoint.getRadius());
	}

	private double getAngle(REPoint p1, REPoint p2) {
		int xVal = p1.getX() - p2.getX();
		int yVal = p1.getY() - p2.getY();
		return Math.atan2(yVal, xVal) + ((xVal < 0 ^ yVal < 0) ? Math.PI : 0.0);

	}

	public synchronized void step(double angle, int length) {
		REPoint oldPoint = null;
		for (int i = 0; i < this.bodySeg.size(); i++) {
			REPoint currentPoint = this.bodySeg.get(i);
			if (currentPoint.getType()==REPoint.REType.TAILSEG && this.bufferBodySegment > 0) {
				
				
				
				
				
				
				
				this.bufferBodySegment--;
				break;
			}
			if (oldPoint != null) {
				angle = this.getAngle(oldPoint, currentPoint);
			}
			oldPoint = currentPoint;
			REPoint newPoint = this.nextPoint(oldPoint, angle, length);
			this.bodySeg.set(i, newPoint);
		}
	}

	public synchronized boolean isSelfCollision() {
		if (this.bodySeg.size() > 3) {
			Iterator<REPoint> it = this.bodySeg.iterator();
			REPoint head = this.bodySeg.get(0);
			try {
				it.next();
				it.next();
				it.next();
				while (it.hasNext()) {
					REPoint re = it.next();
					if (re.isCollideWith(head)) {
						return true;
					}
				}
			} catch (Exception ex) {
			}
		}
		return false;
	}

	//<editor-fold defaultstate="collapsed" desc="Support List Methods">
	@Override
	public int size() {
		return this.bodySeg.size();
	}

	@Override
	public boolean isEmpty() {
		return this.bodySeg.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return this.bodySeg.contains(o);
	}

	@Override
	public Iterator<REPoint> iterator() {
		return this.bodySeg.iterator();
	}

	@Override
	public Object[] toArray() {
		return this.bodySeg.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return this.bodySeg.toArray(a);
	}

	@Override
	public REPoint get(int index) {
		return this.bodySeg.get(index);
	}

	@Override
	public ListIterator<REPoint> listIterator() {
		return this.bodySeg.listIterator();
	}
	//</editor-fold>
	//<editor-fold defaultstate="collapsed" desc="Not supported in the PlayerBodyList">

	@Override
	public ListIterator<REPoint> listIterator(int index) {
		throw new UnsupportedOperationException("Not supported in the PlayerBodyList");
	}

	@Override
	public boolean add(REPoint e) {
		throw new UnsupportedOperationException("Not supported in the PlayerBodyList");
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException("Not supported in the PlayerBodyList");
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		throw new UnsupportedOperationException("Not supported in the PlayerBodyList");
	}

	@Override
	public boolean addAll(Collection<? extends REPoint> c) {
		throw new UnsupportedOperationException("Not supported in the PlayerBodyList");
	}

	@Override
	public boolean addAll(int index, Collection<? extends REPoint> c) {
		throw new UnsupportedOperationException("Not supported in the PlayerBodyList");
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException("Not supported in the PlayerBodyList");
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("Not supported in the PlayerBodyList");
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("Not supported in the PlayerBodyList");
	}

	@Override
	public REPoint set(int index, REPoint element) {
		throw new UnsupportedOperationException("Not supported in the PlayerBodyList");
	}

	@Override
	public void add(int index, REPoint element) {
		throw new UnsupportedOperationException("Not supported in the PlayerBodyList");
	}

	@Override
	public REPoint remove(int index) {
		throw new UnsupportedOperationException("Not supported in the PlayerBodyList");
	}

	@Override
	public int indexOf(Object o) {
		throw new UnsupportedOperationException("Not supported in the PlayerBodyList");
	}

	@Override
	public int lastIndexOf(Object o) {
		throw new UnsupportedOperationException("Not supported in the PlayerBodyList");
	}

	@Override
	public List<REPoint> subList(int fromIndex,
			  int toIndex) {
		throw new UnsupportedOperationException("Not supported in the PlayerBodyList");
	}
	//</editor-fold>
}
