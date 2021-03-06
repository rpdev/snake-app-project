package se.chalmers.snake.gameengine;

import android.util.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import se.chalmers.snake.interfaces.LevelIC;
import se.chalmers.snake.interfaces.util.REPoint;
import se.chalmers.snake.interfaces.util.XYPoint;

/**
 * This is the part of the GameEngine that hold a single level and this data.
 */
class LevelEngine {

    private int score;

    /**
     * Private class for hold count of all items and how long time each item has
     * be on the game map.
     */
    private class LEIPoint extends REPoint {

        private static final long serialVersionUID = 5037544644353660074L;
        private int time = 0;

        private LEIPoint(XYPoint xyPoint, int radius) {
            super(REPoint.REType.ITEM, xyPoint, radius, 0);

        }

        private LEIPoint(int x, int y, int radius) {
            super(REPoint.REType.ITEM, x, y, radius, 0);
        }
    }
    private final LevelIC level;
    final PlayerBody playerBody;
    // Holds data for place items in the game, and hold history of collect items.
    private final List<REPoint> allPossibleItems;
    private int allPossibleItemsIndex;
    private final List<LEIPoint> items;
    private final List<Integer> itemsCollect;
    // Holds data for statics variabels, and calcs
    private final List<REPoint> obstacles;
    private double xScal, yScal, fixScal;
    private int itemsRadius;
    private int playerBodyWidth;
    private boolean isRunning;
    private int stepLength;

    /**
     * Load a new level into this class.
     *
     * @param level The level
     * @param gameFiledSize the Game Map Size
     */
    LevelEngine(LevelIC level, XYPoint gameFiledSize) {
        this.level = level;
        this.score = 0;
        this.calcScal(gameFiledSize);

        this.itemsRadius = (int) (this.fixScal * level.getItemsRadius());
        this.playerBodyWidth = (int) (this.fixScal * level.getPlayerBodyWidth());
        this.isRunning = true;
        this.items = new ArrayList<LEIPoint>();
        this.itemsCollect = new ArrayList<Integer>();
        this.stepLength = (int) (level.getSpeed(this.itemsCollect) * this.fixScal);
        XYPoint startPoint = new XYPoint((int) (this.xScal * level.getSnakeHeadStartLocation().x), (int) (this.yScal * level.getSnakeHeadStartLocation().y));

        this.playerBody = new PlayerBody(gameFiledSize, startPoint, this.level.getStartAngle(), (int) (this.playerBodyWidth * this.fixScal), level.getSnakeStartLength(), 0);

        this.obstacles = Collections.unmodifiableList(this.listStaticElement());
        /**
         * create a source of possible locations of items to be place at.
         */
        this.allPossibleItems = new ArrayList<REPoint>();
        this.fullAllPossibleItemsList(itemsRadius, gameFiledSize);
        Collections.shuffle(this.allPossibleItems);
        this.allPossibleItemsIndex = 0;


        /**
         * Add the basic numbers of items.
         */
        this.addItems(this.level.getAddItems(0, 0));
    }

    /**
     * Return the size each item has in the game.
     *
     * @return
     */
    int getItemsRadius() {
        return this.itemsRadius;
    }

    /**
     * Step the time 1 step, Each step will inca the timer count of each Item on
     * the filed. and after this move player body a step.
     *
     * @param stepAngle
     * @param stepLength return Return false if the player has hit a item.
     */
    synchronized boolean step(double stepAngle) {
        if (this.isRunning == false) {
            return false;
        }
        this.playerBody.step(stepAngle, this.stepLength);
        REPoint playerHead = this.playerBody.getHead();
        if (this.isCollision()) {
            this.isRunning = false;
            return false;
        }
        // Test if the players head are collide with the items.
        Iterator<LEIPoint> itPoint = this.items.iterator();
        int addsItemCount = 0;
        while (itPoint.hasNext()) {

            LEIPoint item = itPoint.next();
            item.time++;
            if (playerHead.isCollideWith(item)) {
                this.itemsCollect.add(item.time);
                this.stepLength = (int) (this.level.getSpeed(this.itemsCollect) * this.fixScal);
                this.playerBody.addSeg(this.level.getBodyGrowth(item.time, this.itemsCollect.size() + 1));
                this.calcScore(item.time, this.itemsCollect.size());
                itPoint.remove();
                addsItemCount += this.level.getAddItems(this.itemsCollect.size(), this.items.size() + addsItemCount);
            }
        }

        this.addItems(addsItemCount);
        return true;
    }

    private void calcScore(int time, int itemCollect) {
        int cPoint = 0;
        if (time < 100) {
            cPoint = (int) ((100 - time) / 20.0 + itemCollect * 0.3);
        } else {
            cPoint = (int) (itemCollect * 0.3);
        }
        cPoint = (int) (cPoint * this.level.getLevel() * 0.75);
        this.score += cPoint + 1;
    }

    /**
     * Test if the player has reached the goal of this map.
     *
     * @return
     */
    boolean hasReachedGoal() {
        return this.level.hasReachedGoal(this.itemsCollect);
    }

    /**
     * Get this game filed the walls and static Elements.
     *
     * @return
     */
    List<REPoint> getObstacles() {
        return this.obstacles;
    }

    /**
     * Get the player body, this is clone of the orginal.
     *
     * @return
     */
    List<REPoint> getPlayerBody() {
        return this.playerBody.get();
    }

    /**
     * Get a list of all items on the game map.
     *
     * @return
     */
    List<REPoint> getItemsList() {
        ArrayList<REPoint> al = null;
        synchronized (this.items) {
            al = new ArrayList<REPoint>(this.items.size());
            al.addAll(this.items);
        }
        return al;
    }

    /**
     * Get the current score of this map.
     *
     * @return
     */
    int getScore() {
        return this.score;

    }

    /**
     * Get the level data, only some of the meta data should be used.
     *
     * @return
     */
    LevelIC getLevelData() {
        return this.level;
    }

    /**
     * Get the escaling factor of the map to the screen resolution
     *
     * @param gameFiledSize
     */
    private void calcScal(XYPoint gameFiledSize) {
        int outScal = (gameFiledSize.x + gameFiledSize.y) / 2;
        XYPoint inScalPoint = new XYPoint(this.level.getMapSize().x, this.level.getMapSize().y);
        int inScal = (inScalPoint.x + inScalPoint.y) / 2;
        this.xScal = (double) gameFiledSize.x / (double) inScalPoint.x;
        this.yScal = (double) gameFiledSize.y / (double) inScalPoint.y;
        this.fixScal = (double) outScal / (double) inScal;
    }

    /**
     * Recalc all REPoints for the Static Elements.
     *
     * @return
     */
    private List<REPoint> listStaticElement() {
        ArrayList<REPoint> alRE = new ArrayList<REPoint>();
        for (REPoint rsp : this.level.getObstacles()) {
            alRE.add(new REPoint(REPoint.REType.WALL, (int) (this.xScal * rsp.x), (int) (this.yScal * rsp.y), (int) (this.fixScal * rsp.radius), 0));
        }
        return alRE;
    }

    /**
     * Test if the player has collision with a wall or the player self. ( Only
     * the head will be test for collision for speed up the test.
     *
     * @return
     */
    private boolean isCollision() {
        if (this.playerBody.isSelfCollision()) {
            return true;
        }
        REPoint head = this.playerBody.getHead();
        
        for (REPoint ePoint : this.obstacles) {
            if (head.isCollideWith(ePoint)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Add a new item in the game
     *
     * @param count
     */
    private void addItems(int count) {
        if (count > 0) {
            for (int i = 0; i < 2; i++) {
                while (this.allPossibleItemsIndex < this.allPossibleItems.size() && count > 0) {
                    REPoint mainPoint = this.allPossibleItems.get(this.allPossibleItemsIndex++);
                    int x = (int) (mainPoint.x + (Math.random() * this.itemsRadius * 2 - this.itemsRadius));
                    int y = (int) (mainPoint.y + (Math.random() * this.itemsRadius * 2 - this.itemsRadius));
                    LEIPoint item = new LEIPoint(x, y, this.itemsRadius);
                    if (!this.playerBody.isCollisionWith(item)) {
                        this.items.add(item);
                        count--;
                    }
                }
                if (count == 0) {
                    return;
                }
                this.allPossibleItemsIndex = 0;
                Collections.shuffle(this.allPossibleItems);
            }
        }
    }

    /**
     * Test if static element are collision with the player.
     *
     * @param point
     * @return
     */
    private boolean isStaticElementCollision(REPoint point) {
        for (REPoint ePoint : this.obstacles) {
            if (point.isCollideWith(ePoint)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Make a 2d map of the game filed for place the Items on.
     *
     * @param itemSize
     * @param gameSize
     */
    private void fullAllPossibleItemsList(int itemSize, XYPoint gameSize) {
        int itemSize2 = itemSize * 2;
        int itemSize3 = itemSize * 3;
        int itemSize4 = itemSize * 4;
        int startX = itemSize2 + ((gameSize.x - itemSize4) % itemSize3) / 2;
        int countX = gameSize.x / itemSize3;
        int startY = itemSize2 + ((gameSize.y - itemSize4) % itemSize3) / 2;
        int countY = gameSize.y / itemSize3;
        for (int x = 0; x < countX; x++) {
            for (int y = 0; y < countY; y++) {
                REPoint point = new REPoint(REPoint.REType.ITEM, startX + x * itemSize3, startY + y * itemSize3, itemSize2, 0);
                if (!this.isStaticElementCollision(point)) {
                    this.allPossibleItems.add(point);
                }
            }
        }
    }

    /**
     * Get the first REPoint in the Player Body Call Head.
     *
     * @return
     */
    REPoint getPlayerHead() {
        return this.playerBody.getHead();
    }
}
