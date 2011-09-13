package se.chalmers.snake.leveldatabase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import se.chalmers.snake.interfaces.LevelIC;
import se.chalmers.snake.interfaces.util.REPoint;
import se.chalmers.snake.interfaces.util.XYPoint;
import se.chalmers.snake.interfaces.util.REPoint.REType;
import se.chalmers.snake.leveldatabase.LevelDatabase.Data;

/**
 * The level class represent a level in the game, each level is created by reading the file
 * stored in the file that is sent to the constructor of this class.
 */
class Level implements LevelIC {
	private static final String X="x", Y="y",SNAKE = "Snake", SQUARE = "Square";
	private final EnumMap<Settings, String> values = new EnumMap<Settings, String>(Settings.class);
	private final ArrayList<REPoint> walls = new ArrayList<REPoint>(), snake = new ArrayList<REPoint>();
	private final XYPoint mapSize;
	private int items;
	
	/**
	 * Create a new level instance from the file found in the data package.
	 * @param data Data package.
	 */
	Level(Data data) {
		loadMap(data.file);
		mapSize = new XYPoint(
				Integer.parseInt(values.get(Settings.MAPSIZEX)),
				Integer.parseInt(values.get(Settings.MAPSIZEY)
		));
		items = Integer.parseInt(values.get(Settings.COLLECTIBLE));
	}

	@Override
	public int getAddItems(int totalCollected, int totalItemInGame) {
		if(totalCollected >= items || totalItemInGame > 0)
			return 0;
		return 1;
	}

	@Override
	public int getBodyGrowth(int collectTime, int totalCollected) {
		return 1;
	}

	@Override
	public int getItemsRadius() {
		return Integer.parseInt(values.get(Settings.CIRCLERADIE));
	}

	@Override
	public int getLevel() {
		return Integer.parseInt(values.get(Settings.LEVELDIFFICULY));
	}

	@Override
	public String getLevelDescription() {
		return values.get(Settings.LEVELDESCRIPTION);
	}

	@Override
	public String getLevelName() {
		return values.get(Settings.LEVELNAME);
	}

	@Override
	public XYPoint getMapSize() {
		return mapSize;
	}

	@Override
	public List<REPoint> getObstacles() {
		return walls;
	}

	@Override
	public int getPlayerBodyWidth() {
		return 2*Integer.parseInt(values.get(Settings.CIRCLERADIE));
	}

	@Override
	public XYPoint getSnakeHeadStartLocation() {
		return snake.get(0);
	}

	@Override
	public int getSnakeStartLength() {
		return snake.size();
	}

	@Override
	public int getSpeed(List<Integer> collectTime) {
		return 1;
	}

	@Override
	public double getStartAngle() {
		return 90d;
	}

	@Override
	public boolean hasReachedGoal(List<Integer> collectTime) {
		return collectTime.size() >= items;
	}

	/**
	 * Initiate the reading mechanism
	 * @param file File to be read.
	 */
	private void loadMap(File file){
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = db.parse(file);
			readDoc(doc);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Read the document and store values found in the document
	 * to variables walls, snake, values.
	 * @param doc Document to be read.
	 */
	private void readDoc(Document doc){
		NodeList nl = doc.getDocumentElement().getChildNodes();
		Element squareNode = null, snakeNode = null;
		for(int i=0;i<nl.getLength();i++){
			Node n = nl.item(i);
			if(n.getNodeType() == Node.ELEMENT_NODE){
				if(n.getNodeName().equals(SQUARE))
					squareNode = (Element) n;
				else if(n.getNodeName().equals(SNAKE))
					snakeNode = (Element) n;
				else{
					Element e = (Element) n;
					Settings key = Settings.valueOf(e.getNodeName().toUpperCase());
					String value = e.getNodeValue();
					values.put(key, value);
				}
			}
		}
		
		// Walls
		int radius = Integer.parseInt(values.get(Settings.CIRCLERADIE));
		NodeList squareNodeList = squareNode.getChildNodes();
		for(int i=0;i<squareNodeList.getLength();i++){
			Node n = squareNodeList.item(i);
			if(n.getNodeType() == Node.ELEMENT_NODE){
			Element e = (Element) n;
				walls.add(new REPoint(REType.WALL,
						//Integer.parseInt(e.getAttribute(ID)),
						Integer.parseInt(e.getAttribute(X)),
						Integer.parseInt(e.getAttribute(Y)),
						radius
						)
				);
			}
		}
		walls.trimToSize();
		
		// Snake
		LinkedList<REPoint> tmpSnake = new LinkedList<REPoint>();
		NodeList snakeNodeList = snakeNode.getChildNodes();
		for(int i=0;i<snakeNodeList.getLength();i++){
			Node n = snakeNodeList.item(i);
			if(n.getNodeType() == Node.ELEMENT_NODE){
			Element e = (Element) n;
				tmpSnake.add(new REPoint(REType.BODYSEG,
						//Integer.parseInt(e.getAttribute(ID)),
						Integer.parseInt(e.getAttribute(X)),
						Integer.parseInt(e.getAttribute(Y)),
						radius
						)
				);
			}
		}
		REPoint snakeHead = tmpSnake.removeFirst(), snakeTail = tmpSnake.removeLast();
		snake.add(new REPoint(REType.HEADSEG, snakeHead.x, snakeHead.y, radius));
		for(REPoint r : tmpSnake)
			snake.add(r);
		snake.add(new REPoint(REType.TAILSEG, snakeTail.x, snakeTail.y, radius));
	}
	
	/**
	 * Data fields that is stored in the file representing the level.
	 */
	private enum Settings{
		LEVELNAME,
		LEVELDESCRIPTION,
		LEVELDIFFICULY,
		MAPSIZEX,
		MAPSIZEY,
		COLLECTIBLE,
		CIRCLERADIE;
	}
}
