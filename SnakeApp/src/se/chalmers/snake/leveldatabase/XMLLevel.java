package se.chalmers.snake.leveldatabase;

import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import se.chalmers.snake.interfaces.LevelIC;
import se.chalmers.snake.interfaces.util.REPoint;
import se.chalmers.snake.interfaces.util.XYPoint;
import se.chalmers.snake.leveldatabase.LevelDatabase.LevelDatabaseData;

/**
 * The XMLString file load level.
 */
class XMLLevel implements LevelIC {

    private static final double DEG_TO_RAD = Math.PI / 180.0;
    private final int mapID;
    private final int level;
    private final String name;
    private final String description;
    private final XYPoint mapSize;
    private final LevelCFunc speed;
    private final LevelCFunc growth;
    private final LevelCFunc goal;
    private final LevelCRFunc items;
    private final REPoint player;
    private final int playerLength;
    private final List<REPoint> obstacles;

    static class XMLString {

        private static final String ROOT = "snakeappmap";
        private static final String ROOT_ID = "id";
        private static final String NAME = "name";
        private static final String DESCRIPTION = "description";
        private static final String MAPSIZE = "map";
        private static final String GAMESPEED = "gamespeed";
        private static final String GROWTHSPEED = "growthspeed";
        private static final String GOAL = "levelgoal";
        static final String CFUNC = "type";
        private static final String RADIUS = "r";
        private static final String ITEMS = "item";
        private static final String LEVEL = "level";
        private static final String PLAYER = "player";
        private static final String X = "x";
        private static final String Y = "y";
        private static final String ANGLE = "a";
        private static final String PLAYER_LENGTH = "s";
        private static final String OBSTACLES = "obstacles";
    }

    private static class LevelCRFunc extends LevelCFunc {

        private final int radius;

        private LevelCRFunc(Node nodeMeta) throws Exception {
            super(nodeMeta);
            int r = XMLReader.attributeInt(nodeMeta, XMLString.RADIUS);
            if (r > 0) {
                this.radius = r;
            } else {
                throw new NullPointerException("The attribute r in node is not in rang.");
            }
        }

    }

    /**
     * Load A Level by a XMLString file,
     *
     * @param rowData
     * @throws IOException Will be throw if the file not can be read as a Level.
     */
    public XMLLevel(LevelDatabaseData rowData) throws IOException {
        InputStream ioStream = null;
        try {
            ioStream = rowData.getInputSteam();
            Document xmlDoc = DocumentBuilderFactory.newInstance().
                    newDocumentBuilder().parse(ioStream);
            Element rootDoc = xmlDoc.getDocumentElement();
            if (rootDoc.getNodeName().equals(XMLString.ROOT)) { // Root Node Exist.
                this.mapID = XMLReader.attributeInt(rootDoc, XMLString.ROOT_ID);
                if (rowData.level > 0) {
                    this.level = rowData.level;
                } else {
                    this.level = XMLReader.attributeInt(rootDoc, XMLString.LEVEL);
                }

                this.name = XMLReader.val(XMLReader.find(rootDoc, XMLString.NAME));


                this.description = XMLReader.val(XMLReader.find(rootDoc, XMLString.DESCRIPTION));
                Node map = XMLReader.find(rootDoc, XMLString.MAPSIZE);


                this.mapSize = new XYPoint(XMLReader.attributeInt(map, "x"), XMLReader.attributeInt(map, "y"));


                this.speed = new LevelCFunc(XMLReader.find(rootDoc, XMLString.GAMESPEED));

                this.growth = new LevelCFunc(XMLReader.find(rootDoc, XMLString.GROWTHSPEED));

                this.goal = new LevelCFunc(XMLReader.find(rootDoc, XMLString.GOAL));



                this.items = new LevelCRFunc(XMLReader.find(rootDoc, XMLString.ITEMS));
                //<player x="150" y="200" r="10" a="90" s="4" />
                Node playerNode = XMLReader.find(rootDoc, XMLString.PLAYER);
                this.player = new REPoint(REPoint.REType.HEADSEG,
                        XMLReader.attributeInt(playerNode, XMLString.X),
                        XMLReader.attributeInt(playerNode, XMLString.Y),
                        XMLReader.attributeInt(playerNode, XMLString.RADIUS),
                        XMLReader.attributeInt(playerNode, XMLString.ANGLE) * XMLLevel.DEG_TO_RAD);
                this.playerLength = XMLReader.attributeInt(playerNode, XMLString.PLAYER_LENGTH);


                //<obstacles>...</obstacles>
                NodeList obstaclesNode = XMLReader.find(rootDoc, XMLString.OBSTACLES).getChildNodes();
                List<REPoint> obstaclesPoints = new ArrayList<REPoint>(obstaclesNode.getLength());
                for (int i = 0; i < obstaclesNode.getLength(); i++) {
                    Node obstacle = obstaclesNode.item(i);
                    if (obstacle != null && obstacle.getNodeType() != Node.TEXT_NODE) {
                        REPoint wallPoint = new REPoint(REPoint.REType.WALL,
                                XMLReader.attributeInt(obstacle, XMLString.X),
                                XMLReader.attributeInt(obstacle, XMLString.Y),
                                XMLReader.attributeInt(obstacle, XMLString.RADIUS),
                                0);


                        obstaclesPoints.add(wallPoint);
                    }
                }
                this.obstacles = Collections.unmodifiableList(obstaclesPoints);
            } else {
                throw new Exception("Root Node is not '" + XMLString.ROOT + "'");
            }
        } catch (Exception ex) {
            throw new IOException("Can not read select level, " + ex.getMessage());
        } finally {
            if (ioStream != null) {
                ioStream.close();
            }
        }
    }
    //<editor-fold defaultstate="collapsed" desc="LevelIC Override">

    @Override
    public String getLevelName() {
        return this.name;
    }

    @Override
    public String getLevelDescription() {
        return this.description;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public XYPoint getMapSize() {
        return this.mapSize;
    }

    @Override
    public int getSnakeStartLength() {
        return this.playerLength;
    }

    @Override
    public XYPoint getSnakeHeadStartLocation() {
        return this.player;
    }

    @Override
    public double getStartAngle() {
        return this.player.angle;
    }

    @Override
    public int getPlayerBodyWidth() {
        return this.player.radius;
    }

    @Override
    public List<REPoint> getObstacles() {
        return this.obstacles;
    }

    @Override
    public int getItemsRadius() {
        return this.items.radius;
    }

    @Override
    public float getSpeed(List<Integer> collectTime) {
        return this.speed.calc(collectTime);
    }

    @Override
    public boolean hasReachedGoal(List<Integer> collectTime) {
        return this.goal.calc(collectTime) > 0;
    }

    @Override
    public int getAddItems(int totalCollected, int totalItemInGame) {
        return (int) this.items.calc(totalCollected, totalItemInGame);
    }

    @Override
    public int getBodyGrowth(int collectTime, int totalCollected) {
        return (int) this.growth.calc(totalCollected, totalCollected);
    }
    //</editor-fold>

    @Override
    public String toString() {
        return "XMLLevel{" + "mapID=" + mapID + ", level=" + level + ", name=" + name + ", description=" + description + ", mapSize=" + mapSize + ", speed=" + speed + ", growth=" + growth + ", goal=" + goal + ", items=" + items + ", player=" + player + ", playerLength=" + playerLength + '}';
    }
}
