package se.chalmers.snake.leveldatabase;

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
 * The XML file load level.
 */
class XMLLevel implements LevelIC {

	private static final double DEG_TO_RAD = Math.PI / 180.0;
	private final int mapID;
	private final int level;
	private final String name;
	private final String description;
	private final XYPoint mapSize;
	private final CFunc speed;
	private final CFunc growth;
	private final CFunc goal;
	private final CRFunc items;
	private final REPoint player;
	private final int playerLength;
	private final List<REPoint> obstacles;

	private static class XML {

		private static final String ROOT = "snakeappmap";
		private static final String ROOT_ID = "id";
		private static final String NAME = "name";
		private static final String DESCRIPTION = "description";
		private static final String MAPSIZE = "map";
		private static final String GAMESPEED = "gamespeed";
		private static final String GROWTHSPEED = "growthspeed";
		private static final String GOAL = "levelgoal";
		private static final String CFUNC = "type";
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

	private static class XR {

		private static Node find(Node node, String tagName) throws Exception {
			NodeList nl = node.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				if (nl.item(i).getNodeName().equals(tagName)) {
					return nl.item(i);
				}
			}
			throw new Exception("Node has no childen with tag '" + tagName + "'");

		}

		private static String attribute(Node node, String name) {
			if (node instanceof Element) {
				final Element e = (Element) node;
				if (e.hasAttribute(name)) {
					return e.getAttribute(name);
				}
			}
			return null;
		}

		private static int attributeInt(Node node, String name) {
			if (node instanceof Element) {
				final Element e = (Element) node;
				if (e.hasAttribute(name)) {
					return Integer.parseInt(e.getAttribute(name));
				}
			}
			return 0;
		}

		private static String val(Node node) {
			if (node == null) {
				throw new NullPointerException("Node not Exist");
			}
			String val = node.getFirstChild().getNodeValue();
			return val != null ? val : "";
		}
	}

	private static class CFunc {

		String type;
		float i;

		private CFunc(Node nodeMeta) throws Exception {
			this.type = XR.attribute(nodeMeta, XML.CFUNC);
			this.i = Float.parseFloat(XR.val(nodeMeta));
		}

		public float calc(int x) {
			if ("greater".equals(this.type)) {
				return x >= this.i ? 1 : 0;
			}
			return i;
		}

		@Override
		public String toString() {
			return "CFunc{" + "type=" + type + ", i=" + i + '}';
		}
	}

	private static class CRFunc extends CFunc {

		private final int radius;

		private CRFunc(Node nodeMeta) throws Exception {
			super(nodeMeta);
			int r = XR.attributeInt(nodeMeta, XML.RADIUS);
			if (r > 0) {
				this.radius = r;
			} else {
				throw new NullPointerException("The attribute r in node is not in rang.");
			}
		}

		@Override
		public String toString() {
			return "CRFunc{" + "radius=" + radius + ", type=" + type + ", i=" + i + '}';
		}
	}

	/**
	 * Load A Level by a XML file, 
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
			if (rootDoc.getNodeName().equals(XML.ROOT)) { // Root Node Exist.
				this.mapID = XR.attributeInt(rootDoc, XML.ROOT_ID);
				if(rowData.level>0) {
					this.level = rowData.level;
				} else {
					this.level = XR.attributeInt(rootDoc, XML.LEVEL);
				}
				
				this.name = XR.val(XR.find(rootDoc, XML.NAME));
				this.description = XR.val(XR.find(rootDoc, XML.DESCRIPTION));
				Node map = XR.find(rootDoc, XML.MAPSIZE);
				this.mapSize = new XYPoint(XR.attributeInt(map, "x"), XR.attributeInt(map, "y"));

				this.speed = new CFunc(XR.find(rootDoc, XML.GAMESPEED));

				this.growth = new CFunc(XR.find(rootDoc, XML.GROWTHSPEED));

				this.goal = new CFunc(XR.find(rootDoc, XML.GOAL));

				this.items = new CRFunc(XR.find(rootDoc, XML.ITEMS));
				//<player x="150" y="200" r="10" a="90" s="4" />
				Node playerNode = XR.find(rootDoc, XML.PLAYER);
				this.player = new REPoint(REPoint.REType.HEADSEG,
						  XR.attributeInt(playerNode, XML.X),
						  XR.attributeInt(playerNode, XML.Y),
						  XR.attributeInt(playerNode, XML.RADIUS),
						  XR.attributeInt(playerNode, XML.ANGLE) * XMLLevel.DEG_TO_RAD);
				this.playerLength = XR.attributeInt(playerNode, XML.PLAYER_LENGTH);


				//<obstacles>...</obstacles>
				NodeList obstaclesNode = XR.find(rootDoc, XML.OBSTACLES).getChildNodes();
				List<REPoint> obstaclesPoints = new ArrayList<REPoint>(obstaclesNode.getLength());
				for (int i = 0; i < obstaclesNode.getLength(); i++) {
					Node obstacle = obstaclesNode.item(i);
					if (obstacle != null) {
						obstaclesPoints.add(new REPoint(REPoint.REType.WALL,
								  XR.attributeInt(obstacle, XML.X),
								  XR.attributeInt(obstacle, XML.Y),
								  XR.attributeInt(obstacle, XML.RADIUS),
								  0));
					}
				}
				this.obstacles = Collections.unmodifiableList(obstaclesPoints);
			} else {
				throw new Exception("Root Node is not '" + XML.ROOT + "'");
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
		return this.speed.calc(collectTime.size());
	}

	@Override
	public boolean hasReachedGoal(List<Integer> collectTime) {
		return this.goal.calc(collectTime.size()) > 0;
	}

	@Override
	public int getAddItems(int totalCollected, int totalItemInGame) {
		return (int) this.items.calc(totalCollected);
	}

	@Override
	public int getBodyGrowth(int collectTime, int totalCollected) {
		return (int) this.growth.calc(totalCollected);
	}
	//</editor-fold>

	@Override
	public String toString() {
		return "XMLLevel{" + "mapID=" + mapID + ", level=" + level + ", name=" + name + ", description=" + description + ", mapSize=" + mapSize + ", speed=" + speed + ", growth=" + growth + ", goal=" + goal + ", items=" + items + ", player=" + player + ", playerLength=" + playerLength + '}';
	}
}
