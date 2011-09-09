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
	private final PlayerBody playerBody;
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

		for (ItemPoint object : this.items) {
			object.incTime();
		}
		
		this.playerBody.step(stepAngle, (int) (this.fixScal * this.stepLength));
		REPoint playerHead = this.playerBody.getHead();
		if (this.isCollision(playerHead)) {
			this.isRunning = false;
			return false;
		}
		Iterator<ItemPoint> itPoint = this.items.iterator();
		while(itPoint.hasNext()) {
			ItemPoint item = itPoint.next();
			if(playerHead.isCollideWith(item)) {
				this.itemsCollect.add(item.time);	
				this.stepLength = this.level.getSpeed(this.itemsCollect);
				this.playerBody.addSeg(this.level.getBodyGrowth(item.time,this.itemsCollect.size()+1));
				itPoint.remove();
				this.addItems(this.level.getAddItems(this.itemsCollect.size(), this.items.size()));
			}
		}
		return true;
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
		ArrayList al = null;
		synchronized (this.playerBody) {
			al = new ArrayList(this.playerBody.size());
			al.addAll(this.playerBody);
		}
		return al;
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

	private boolean isCollision(REPoint head) {
		if (this.playerBody.isSelfCollision()) {
			return true;
		}
		for (REPoint ePoint : this.staticElement) {
			if (head.isCollideWith(ePoint)) {
				return true;
			}
		}
		return false;
	}
	
	
	private void addItems(int count) {
		
		
	}
}
