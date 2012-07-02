package mapeditor;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import mapeditor.Frame.DotData;
import mapeditor.Frame.K;
import mapeditor.Frame.SnakeData;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

class ExportXML {
	
	static void storeXML(EnumMap<K, String> values, ArrayList<DotData> obst, SnakeData snake){
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element root = doc.createElement("snakeappmap");
			doc.appendChild(root);
			Element name = doc.createElement(K.name.toString());
			name.appendChild(doc.createTextNode(values.get(K.name)));
			root.appendChild(name);
			Element description = doc.createElement(K.description.toString());
			description.appendChild(doc.createTextNode(values.get(K.description)));
			root.appendChild(description);
			Element mapSize = doc.createElement(K.map.toString());
			mapSize.setAttribute(K.x.toString(), values.get(K.x));
			mapSize.setAttribute(K.y.toString(), values.get(K.y));
			root.appendChild(mapSize);
			Element gameSpeed = doc.createElement(K.gamespeed.toString());
			gameSpeed.setAttribute("type", "simple");
			gameSpeed.appendChild(doc.createTextNode(values.get(K.gamespeed)));
			root.appendChild(gameSpeed);
			Element growthSpeed = doc.createElement(K.growthspeed.toString());
			growthSpeed.setAttribute("type", "simple");
			growthSpeed.appendChild(doc.createTextNode(values.get(K.growthspeed)));
			root.appendChild(growthSpeed);
			Element levelGoal = doc.createElement(K.levelgoal.toString());
			levelGoal.setAttribute("type", "greater");
			levelGoal.appendChild(doc.createTextNode(values.get(K.levelgoal)));
			root.appendChild(levelGoal);
			Element item = doc.createElement(K.item.toString());
			item.setAttribute("type", "simple");
			item.setAttribute("r", values.get(K.item_radius));
			item.appendChild(doc.createTextNode(values.get(K.item_count)));
			root.appendChild(item);
			Element player = doc.createElement(K.player.toString());
			player.setAttribute(K.x.toString(), Integer.toString(snake.x));
			player.setAttribute(K.y.toString(), Integer.toString(snake.y));
			player.setAttribute("r", Integer.toString(snake.getDiameter()/2));
			player.setAttribute("a", Integer.toString(snake.getRot()));
			player.setAttribute("s", Integer.toString(snake.getSnakeSeg()));
			root.appendChild(player);
			
			Element obstacles = doc.createElement("obstacles");
			for(DotData o : obst){
				Element i = doc.createElement("item");
				i.setAttribute("x", Integer.toString(o.x));
				i.setAttribute("y", Integer.toString(o.y));
				i.setAttribute("r", Integer.toString(o.getDiameter()/2));
				obstacles.appendChild(i);
			}
			root.appendChild(obstacles);
			
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(values.get(K.name)));
			TransformerFactory.newInstance().newTransformer().transform(source, result);
		} catch (ParserConfigurationException | TransformerException | TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		}
	}
}
