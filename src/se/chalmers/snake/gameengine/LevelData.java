package se.chalmers.snake.gameengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import se.chalmers.snake.interfaces.LevelIC;
import se.chalmers.snake.interfaces.util.REPoint;
import se.chalmers.snake.interfaces.util.XYPoint;

/**
 *
 */
class LevelData {

	private int score;
	private final LevelIC level;
	private final PlayerBody playerBody;
	
	private final ArrayList<REPoint> items;
	private final List<REPoint> staticElement;
	private double xScal,yScal,fixScal;
	private int itemsRadius;
	private int playerBodyWidth;
	

	LevelData(LevelIC level, XYPoint gameFiledSize) {
		this.level = level;
		this.calcScal(gameFiledSize);
		this.itemsRadius = (int)(this.fixScal*level.getItemsRadius());
		this.playerBodyWidth = (int)(this.fixScal*level.getPlayerBodyWidth());
		
		this.items = new ArrayList<REPoint>(5);
		this.playerBody = new PlayerBody(null, null, 0.0, 5, 5, 0);
		this.staticElement = Collections.unmodifiableList(this.listStaticElement());
		this.score = 0;
	}

	private void calcScal(XYPoint gameFiledSize) {
				int outScal = (gameFiledSize.x+gameFiledSize.y)/2;
		XYPoint inScalPoint = new XYPoint(this.level.getMapSize().x,this.level.getMapSize().y);
		int inScal = (inScalPoint.x+inScalPoint.y)/2;
		this.xScal = (double)gameFiledSize.x/(double)inScalPoint.x;
		this.yScal = (double)gameFiledSize.y/(double)inScalPoint.y;
		this.fixScal = (double)outScal/(double)inScal;
	}
	
	
	private List<REPoint> listStaticElement() {
		ArrayList<REPoint> alRE = new ArrayList<REPoint>();
		for(REPoint rsp: this.level.getObstacles()) {
			if(rsp.type==REPoint.REType.WALL) {
				alRE.add(new REPoint(REPoint.REType.WALL, (int)(this.xScal*rsp.x), (int)(this.yScal*rsp.y), (int)this.fixScal*rsp.radius));
			}
		}
		return alRE;
	}

	public List<REPoint> getStaticElement() {
		return this.staticElement;
	}

	List<REPoint> clonePlayerBody() {
		ArrayList al = null;
		synchronized (this.playerBody) {
			al = new ArrayList(this.playerBody.size());
			al.addAll(this.playerBody);
		}
		return al;
	}

	List<REPoint> cloneItemList() {
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

	LevelIC getLevelMetaData() {
		return this.level;
	}
}
