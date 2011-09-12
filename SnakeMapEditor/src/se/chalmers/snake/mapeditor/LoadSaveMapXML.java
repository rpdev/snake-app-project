package se.chalmers.snake.mapeditor;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import se.chalmers.snake.mapeditor.SnakeMapEditor.Settings;

public class LoadSaveMapXML {
	private static final String ROOT="SnakeAppMap", ID="id", X="x", Y="y";
	private static LoadSaveMapXML instance;
	
	private LoadSaveMapXML(){
		instance = this;
	}

	static LoadSaveMapXML getInstance() {
		if(instance == null)
			new LoadSaveMapXML();
		return instance;
	}

	void saveMap(EnumMap<Settings,String> data, Square[] squares) {
		try {
			Document doc = createDocument(data, squares);
			File f = new File("test.xml");
			if(f.exists())
				f.delete();
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(f);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);
			System.out.println("Saved");
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	Data loadMap(File file){
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = db.parse(file);
			return readDoc(doc);
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
		return null;
	}
	
	Data readDoc(Document doc){
		EnumMap<Settings, String> data = new EnumMap<Settings, String>(Settings.class);
		ArrayList<SquareData> squares = new ArrayList<SquareData>();
		NodeList nl = doc.getDocumentElement().getChildNodes();
		Element squareNode = null;
		for(int i=0;i<nl.getLength();i++){
			Node n = nl.item(i);
			if(n.getNodeType() == Node.ELEMENT_NODE){
				if(n.getNodeName().equals(Square.class.getSimpleName()))
					squareNode = (Element) n;
				else{
					Element e = (Element) n;
					Settings key = Settings.valueOf(e.getNodeName().toUpperCase());
					String value = e.getTextContent();
					data.put(key, value);
				}
			}
		}
		NodeList squareNodeList = squareNode.getChildNodes();
		for(int i=0;i<squareNodeList.getLength();i++){
			Node n = squareNodeList.item(i);
			if(n.getNodeType() == Node.ELEMENT_NODE){
			Element e = (Element) n;
				squares.add(new SquareData(
						Integer.parseInt(e.getAttribute(ID)),
						Integer.parseInt(e.getAttribute(X)),
						Integer.parseInt(e.getAttribute(Y))
						)
				);
			}
		}
		return new Data(data, squares.toArray(new SquareData[squares.size()]));
	}
	
	class SquareData{
		final int id, x, y;
		
		private SquareData(int id, int x, int y){
			this.id = id;
			this.x = x;
			this.y = y;
		}
	}
	
	class Data{
		final EnumMap<Settings, String> data;
		final SquareData[] squares;
		
		private Data(EnumMap<Settings, String> data, SquareData[] squares){
			this.data = data;
			this.squares = squares;
		}
	}
	
	private Document createDocument(EnumMap<Settings, String> data, Square[] squares) throws ParserConfigurationException {
		// http://www.mkyong.com/java/how-to-create-xml-file-in-java-dom/
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		
		// Root node
		Element root = doc.createElement(ROOT);
		doc.appendChild(root);
		
		// Save Settings
		for(Entry<Settings, String> e : data.entrySet()){
			Element node = doc.createElement(e.getKey().toString());
			root.appendChild(node);
			
			Text text = doc.createTextNode(e.getValue());
			node.appendChild(text);
		}
		
		// Save filled squares
		Element filledSquares = doc.createElement(Square.class.getSimpleName());
		root.appendChild(filledSquares);
		for(int i=0;i<squares.length;i++){
			if (squares[i].isFilled()) {
				Element n = doc.createElement(Square.class.getSimpleName());
				filledSquares.appendChild(n);
				Point p = squares[i].getLoc();
				n.setAttribute(ID, Integer.toString(i));
				n.setAttribute(X, Integer.toString(p.x));
				n.setAttribute(Y, Integer.toString(p.y));
			}
		}		
		return doc;
	}
}
