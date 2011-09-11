package se.chalmers.snake.gameengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import se.chalmers.snake.interfaces.LevelIC;
import se.chalmers.snake.interfaces.util.REPoint;
import se.chalmers.snake.interfaces.util.XYPoint;

/**
 *
 */
class LevelEngine {

	private int score;
	private final LevelIC level;
	final PlayerBody playerBody;
	private final List<REPoint> allPossibleItems;
	private final List<ItemPoint> items;
	private final List<Integer> itemsCollect;
	private final List<REPoint> staticElement;
	private double xScal, yScal, fixScal;
	private int itemsRadius;
	private int playerBodyWidth;
	private boolean isRunning;
	private int stepLength;

	LevelEngine(LevelIC level, XYPoint gameFiledSize) {
		this.level = level;
		this.calcScal(gameFiledSize);
		this.itemsRadius = (int) (this.fixScal * level.getItemsRadius());
		this.playerBodyWidth = (int) (this.fixScal * level.getPlayerBodyWidth());
		this.isRunning = true;
		this.items = new ArrayList<ItemPoint>();
		this.itemsCollect = new ArrayList<Integer>();
		this.stepLength = level.getSpeed(this.itemsCollect);
		XYPoint startPoint = new XYPoint((int) (this.xScal * level.getSnakeHeadStartLocation().x), (int) (this.yScal * level.getSnakeHeadStartLocation().y));
		this.playerBody = new PlayerBody(gameFiledSize, startPoint, this.level.getStartAngle(), this.playerBodyWidth, level.getSnakeStartLength(), 0);
		this.staticElement = Collections.unmodifiableList(this.listStaticElement());
		this.score = 0;
		this.allPossibleItems = new ArrayList<REPoint>();
		this.fullAllPossibleItemsList(itemsRadius, gameFiledSize);


		this.addItems(this.level.getAddItems(0, 0));


	}

	/**
	 * Get a list of all items on the game filed.
	 * @return 
	 */
	List<ItemPoint> getItem() {
		return this.items;
	}

	int getItemsRadius() {
		return this.itemsRadius;
	}

	/**
	 * Step the time 1 step, 
	 * Each step will inca the timer count of each Item on the filed.
	 * and after this move player body a step.
	 * @param stepAngle
	 * @param stepLength 
	 */
	synchronized boolean step(double stepAngle) {
		if (this.isRunning == false) {
			return false;
		}



		this.playerBody.step(stepAngle, (int) (this.fixScal * this.stepLength));
		REPoint playerHead = this.playerBody.getHead();
		if (this.isCollision()) {
			this.isRunning = false;
			return false;
		}
		// Test if the players head are collide with the items.
		Iterator<ItemPoint> itPoint = this.items.iterator();
		int addsItemCount = 0;
		while (itPoint.hasNext()) {
			ItemPoint item = itPoint.next();
			if (playerHead.isCollideWith(item)) {
				this.itemsCollect.add(item.time);
				this.stepLength = this.level.getSpeed(this.itemsCollect);
				this.playerBody.addSeg(this.level.getBodyGrowth(item.time, this.itemsCollect.size() + 1));
				itPoint.remove();
				addsItemCount += this.level.getAddItems(this.itemsCollect.size(), this.items.size() + addsItemCount);
				//this.addItems(this.level.getAddItems(this.itemsCollect.size(), this.items.size()));
			}
		}

		this.addItems(addsItemCount);
		return true;
	}

	boolean hasReachedGoal() {
		return this.level.hasReachedGoal(this.itemsCollect);
	}

	/**
	 * Get this game filed the walls and static Elements.
	 * @return 
	 */
	List<REPoint> getStaticElement() {
		return this.staticElement;
	}

	/**
	 * Get the player body, this is clone of the orginal.
	 * @return 
	 */
	List<REPoint> getPlayerBody() {
		return this.playerBody.get();
	}

	List<REPoint> getItemsList() {
		ArrayList al = null;
		synchronized (this.items) {
			al = new ArrayList(this.items.size());
			al.addAll(this.items);
		}
		return al;
	}

	int getScore() {
		return this.score;
	}

	LevelIC getLevelData() {
		return this.level;
	}

	private void calcScal(XYPoint gameFiledSize) {
		int outScal = (gameFiledSize.x + gameFiledSize.y) / 2;
		XYPoint inScalPoint = new XYPoint(this.level.getMapSize().x, this.level.getMapSize().y);
		int inScal = (inScalPoint.x + inScalPoint.y) / 2;
		this.xScal = (double) gameFiledSize.x / (double) inScalPoint.x;
		this.yScal = (double) gameFiledSize.y / (double) inScalPoint.y;
		this.fixScal = (double) outScal / (double) inScal;
	}

	private List<REPoint> listStaticElement() {
		ArrayList<REPoint> alRE = new ArrayList<REPoint>();
		for (REPoint rsp : this.level.getObstacles()) {
			if (rsp.type == REPoint.REType.WALL) {
				alRE.add(new REPoint(REPoint.REType.WALL, (int) (this.xScal * rsp.x), (int) (this.yScal * rsp.y), (int) this.fixScal * rsp.radius));
			}
		}
		return alRE;
	}

	private boolean isCollision() {
		if (this.playerBody.isSelfCollision()) {
			return true;
		}
		REPoint head = this.playerBody.getHead();
		for (REPoint ePoint : this.staticElement) {
			if (head.isCollideWith(ePoint)) {
				return true;
			}
		}
		return false;
	}

	private void addItems(int count) {
		if (count > 0) {
			this.items.add(new ItemPoint(REPoint.REType.ITEM, new XYPoint(20, 20), 10));
		}
	}

	private boolean isStaticElementCollision(REPoint point) {
		for (REPoint ePoint : this.staticElement) {
			if (point.isCollideWith(ePoint)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Make a 2d map of the game filed for place the Items on.
	 * @param itemSize
	 * @param gameSize 
	 */
	private void fullAllPossibleItemsList(int itemSize, XYPoint gameSize) {
		int itemSize2 = itemSize * 2;
		int itemSize3 = itemSize * 3;
		int itemSize4 = itemSize * 4;

		int startX = itemSize2 + (gameSize.x % itemSize3) / 2;
		int countX = gameSize.x / itemSize3;

		int startY = itemSize2 + (gameSize.y % itemSize3) / 2;
		int countY = gameSize.y / itemSize3;
		for (int x = 0; x < countX; x++) {
			for (int y = 0; y < countY; y++) {
				REPoint point = new REPoint(REPoint.REType.ITEM, startX + x * itemSize3, startY + y * itemSize3, itemSize2);
				if (!this.isStaticElementCollision(point)) {
					this.allPossibleItems.add(point);
				}
			}
		}
		for(REPoint r:this.allPossibleItems) {
			System.out.println(r);
		}
	}
}
