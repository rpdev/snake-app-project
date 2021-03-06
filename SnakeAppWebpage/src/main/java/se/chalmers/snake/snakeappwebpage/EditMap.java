package se.chalmers.snake.snakeappwebpage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import se.chalmers.snake.snakeappwebpage.lib.HttpServletBuilder;
import se.chalmers.snake.snakeappwebpage.lib.IntegerScan;
import se.chalmers.snake.snakeappwebpage.login.LoginServlet;
import se.chalmers.snake.snakeappwebpage.serverstorage.Comment;
import se.chalmers.snake.snakeappwebpage.serverstorage.Database;
import se.chalmers.snake.snakeappwebpage.serverstorage.MPoint;
import se.chalmers.snake.snakeappwebpage.serverstorage.REPoint;
import se.chalmers.snake.snakeappwebpage.serverstorage.SnakeMap;
import se.chalmers.snake.snakeappwebpage.serverstorage.UserAcc;

/**
 * The Servlet for save and read a SnakeMap Obj.
 * The save obj is come as a post call.
 * and the read obj as a get call and return a xml file for the select item if this exist.
 * 
 */
@WebServlet(name = "EditMap", urlPatterns = {"/editmap"})
public class EditMap extends HttpServletBuilder {

	private static final long serialVersionUID = 2244928737289982716L;

	@XmlRootElement
	static class SnakeMapXml {

		@XmlElement
		private String mapName;
		@XmlElement
		private String difficulty;
		@XmlElement
		private int speed;
		@XmlElement
		private int growth;
		@XmlElement
		private String description;
		@XmlElement
		private List<String> comment;

		public SnakeMapXml() {
		}

		public SnakeMapXml(SnakeMap snakeMap) {
			if (snakeMap != null) {
				if (snakeMap.getMapName() != null) {
					this.mapName = snakeMap.getMapName();
				} else {
					this.mapName = "";
				}
				switch (snakeMap.getDifficuly()) {
					case 1:
						this.difficulty = "Easy";
						break;
					case 2:
						this.difficulty = "Normal";
						break;
					case 3:
						this.difficulty = "Hard";
						break;
					case 4:
						this.difficulty = "Very Hard";
						break;
				}

				this.speed = snakeMap.getGameSpeed().getIntValue();
				this.growth = snakeMap.getGrowthspeed().getIntValue();
				this.description = snakeMap.getMapDescription();
				this.comment = snakeMap.getCommentsToString();
			}
		}
	}

	/**
	 * The main function of the Servlets and handle the first call of this Servlet.
	 * @param httpMeta 
	 * @param httpOutput
	 * @throws Exception 
	 */
	@Override
	protected void pageRequest(HttpMeta httpMeta, HttpOutput httpOutput) throws Exception {

		String action = httpMeta.REQUEST("action"); // Type of action
		// Allow types is "save","public","preview"

		if ("get".equals(action)) {
			httpMeta.setContentType("text/xml;charset=UTF-8");
			this.getMap(httpMeta, httpOutput);
		} else if ("save".equals(action) || "public".equals(action)) {
			httpMeta.setContentType("text/plain;charset=UTF-8");
			try {
				SnakeMap snakeMap = this.storeMap(httpMeta, "public".equals(action));
				if (snakeMap != null) {
					httpOutput.getWriter().print("public".equals(action) ? "Map Save & Published" : "Map Save");
					return;
				} else {
					httpOutput.getWriter().print("Error\nYou are not login on the page");;
					return;
				}
			} catch (Exception ex) {
				httpOutput.getWriter().print("Error\nSystem Error\n\n");
				ex.printStackTrace(httpOutput.getWriter());

				return;
			}
		} else if ("preview".equals(action)) {
			httpMeta.setContentType("text/xml;charset=UTF-8");
			this.preView(httpMeta, httpOutput);
		} else if ("comment".equals(action)) {
			this.addComment(httpMeta);
		} else if ("rate".equals(action)) {
			if(this.rateMap(httpMeta)) {
				httpOutput.getWriter().print("<ROOT>TRUE</ROOT>");
			} else {
				httpOutput.getWriter().print("<ROOT>FALSE</ROOT>");
			}
		} else if (("getRating").equals(action)) {
			this.getMapRating(httpMeta, httpOutput);
		}
	}

	private void getMapRating(HttpMeta httpMeta, HttpOutput httpOutput) throws ParserConfigurationException, TransformerConfigurationException, IOException, TransformerException {
		int mapID = this.getIntFromRequest(httpMeta, "id", -1);
		if (mapID > 0) {
			SnakeMap snakeMap = Database.getInstance().getEntity(SnakeMap.class, Long.valueOf(mapID));
			if (snakeMap != null) {
				Document xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
				Element rootNode = xmlDocument.createElement("snakeappmap");

				Element mapRatingNode = xmlDocument.createElement("mapRating");
				mapRatingNode.appendChild(xmlDocument.createTextNode("" + snakeMap.getRoundedMapRating()));
				rootNode.appendChild(mapRatingNode);

				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");

				StreamResult result = new StreamResult(httpOutput.getWriter());
				DOMSource source = new DOMSource(rootNode);
				transformer.transform(source, result);
			}
		}
	}

	private boolean rateMap(HttpMeta httpMeta) {
		int mapID = this.getIntFromRequest(httpMeta, "id", -1);
		int mapRating = this.getIntFromRequest(httpMeta, "mapRating", -1);
		if (mapID > 0 && mapRating > 0) {
			SnakeMap snakeMap = Database.getInstance().getEntity(SnakeMap.class, Long.valueOf(mapID));
			if (snakeMap != null) {
				snakeMap.rateMap(mapRating);
				if(snakeMap.getUserName()!=null) {
					Database.getInstance().mergeObject(snakeMap.getUserName());
				}
				Database.getInstance().mergeObject(snakeMap);
				return true;
			}
		}
		return false;
	}

	private void preView(HttpMeta httpMeta, HttpOutput httpOutput) {
		int mapID = this.getIntFromRequest(httpMeta, "id", -1);
		if (mapID > 0) {
			SnakeMap snakeMap = Database.getInstance().getEntity(SnakeMap.class, Long.valueOf(mapID));
			SnakeMapXml wrapper = new SnakeMapXml(snakeMap);
			JAXBContext jc;
			try {
				jc = JAXBContext.newInstance(SnakeMapXml.class);
				Marshaller m = jc.createMarshaller();
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
						  Boolean.TRUE);
				// Dump XML data
				m.marshal(wrapper, httpOutput.getWriter());
			} catch (Exception ex) {
			}
		}
	}

	private void addComment(HttpMeta httpMeta) {
		int mapID = this.getIntFromRequest(httpMeta, "id", -1);
		if (mapID > 0) {
			String commentString = this.getStringFromRequest(httpMeta, "commentString", "empty");
			SnakeMap snakeMap = Database.getInstance().getEntity(SnakeMap.class, Long.valueOf(mapID));
			UserAcc user = this.getUserAccount(httpMeta);
			Comment comment = new Comment(commentString, snakeMap, user);
			Database.getInstance().mergeObject(comment);
			Database.getInstance().mergeObject(user);
			Database.getInstance().mergeObject(snakeMap);
		}
	}

	/**
	 * Get the current user account that has access the page
	 * @param httpMeta
	 * @return The UserAcc if a user are access to the page, else null.
	 */
	private UserAcc getUserAccount(HttpMeta httpMeta) {
		try {
			return LoginServlet.getUserAccount(httpMeta);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Get the map if the map exist, else not return a template file
	 * @param httpMeta
	 * @param httpOutput 
	 */
	private void getMap(HttpMeta httpMeta, HttpOutput httpOutput) {
		try {
			int mapID = this.getIntFromRequest(httpMeta, "id", -1);
			if (mapID > 0) {
				SnakeMap myMap = Database.getInstance().getEntity(SnakeMap.class, Long.valueOf(mapID));
				if (myMap != null) {
					myMap.generateXML(httpOutput.getWriter());
					return;
				}
			}
		} catch (Exception ex) {
		}
		httpOutput.forward("mapeditor/template.xml");
	}

	/**
	 * Save a map to the database. A map can be save as a new post or edit of a old map.
	 * And the map can be mark with public state while save.
	 * @param httpMeta The HTTP Meta
	 * @param publicMap Will this map be public or not.
	 * @return Return the Obj if this can be save, else this will throws a error.
	 * @throws Exception 
	 */
	private SnakeMap storeMap(HttpMeta httpMeta, boolean publicMap) throws Exception {
		UserAcc userAcc = this.getUserAccount(httpMeta);
		if (userAcc != null) {
			int mapID = this.getIntFromRequest(httpMeta, "id", -1);
			SnakeMap myMap = null;

			if (mapID > 0) {
				try {
					myMap = Database.getInstance().getEntity(SnakeMap.class, Long.valueOf(mapID));
				} catch (Exception ex) {
				}
			}

			if (myMap == null) {
				myMap = new SnakeMap(userAcc);
			}
			// Now we have the map Obj, test if this user are onwer of this map.
			if (myMap.getUserName().equals(userAcc)) {
				// You is allow to edit the map.

				myMap.setMapName(this.getStringFromRequest(httpMeta, "name", ""));
				myMap.setMapDescription(this.getStringFromRequest(httpMeta, "description", ""));
				myMap.setDifficuly(this.getIntFromRequest(httpMeta, "difficuly", 2, 1, 4));
				myMap.setGameSpeed(new MPoint("simple", this.getIntFromRequest(httpMeta, "gamespeed", 5, 1, Integer.MAX_VALUE)));
				myMap.setGrowthspeed(new MPoint("simple", this.getIntFromRequest(httpMeta, "growth", 2, 0, Integer.MAX_VALUE)));
				myMap.setItemPoint(new MPoint("simple", this.getIntFromRequest(httpMeta, "collectiblecount", 1, 1, Integer.MAX_VALUE)));
				myMap.setItemSize(this.getIntFromRequest(httpMeta, "collectiblesize", 10, 1, Integer.MAX_VALUE));
				myMap.setLevelgoal(new MPoint("simple", 0));
				myMap.setMapSize(this.getMapSize(httpMeta));
				this.setSnakeMeta(httpMeta, myMap);
				myMap.setObstacle(this.getObstacle(httpMeta));

				if (publicMap) {
					myMap.setStatus(Database.STATUS.PUBLICHED);
				}

				if (myMap != null) {
					Database.getInstance().mergeObject(userAcc);
					userAcc.addMap(myMap);
					Database.getInstance().mergeObject(myMap);
				}
				return myMap;
			}
		} else {
			return null;
		}
		throw new Exception("No user are access or allow to edit this map");
	}

	/**
	 * Get a list of all Obstract that is store in the POST call while save data.
	 * @param httpMeta
	 * @return 
	 */
	private List<REPoint> getObstacle(HttpMeta httpMeta) {
		List<REPoint> points = new ArrayList<REPoint>();
		String str = this.getStringFromRequest(httpMeta, "obstacle", "");
		if (str.length() > 0) {
			for (List<Integer> iList : new IntegerScan(str)) {
				points.add(new REPoint(iList));
			}
		}
		return points;
	}

	/**
	 * Return a int from the Request, or return a default value if not value are allow. 
	 * @param httpMeta
	 * @param key The key of request to be read
	 * @param nullValue the default if not the value can be read as a int.
	 * @return 
	 */
	private int getIntFromRequest(HttpMeta httpMeta, String key, int nullValue) {
		try {
			return Integer.parseInt(httpMeta.REQUEST(key));
		} catch (Exception ex) {
			return nullValue;
		}
	}

	/**
	 * Return a int from the Request, or return a default value if not value are allow
	 * or in given limit.
	 * @param httpMeta
	 * @param key The key of request to be read
	 * @param nullValue the default if not the value can be read as a int.
	 * @param min the lower limit, include this number.
	 * @param max the higer limit, include this number.
	 * @return 
	 */
	private int getIntFromRequest(HttpMeta httpMeta, String key, int nullValue, int min, int max) {
		try {
			int value = Integer.parseInt(httpMeta.REQUEST(key));
			return value >= min && value <= max ? value : nullValue;
		} catch (Exception ex) {
			return nullValue;
		}
	}

	/**
	 * Return a String from the request by a key, if the value exist and can be use.
	 * @param httpMeta
	 * @param key The key of request to be read
	 * @param nullValue the default if not the value can be read as a int.
	 * @return 
	 */
	private String getStringFromRequest(HttpMeta httpMeta, String key, String nullValue) {
		String value = httpMeta.REQUEST(key);
		return value != null ? value : nullValue;
	}

	/**
	 * Read the map size from the post data on save or public call.
	 * @param httpMeta
	 * @return 
	 */
	private REPoint getMapSize(HttpMeta httpMeta) {
		String rowString = this.getStringFromRequest(httpMeta, "gamesize", "");
		IntegerScan is = new IntegerScan(rowString);
		if (is.hasNext()) {
			List<Integer> li = is.next();
			if (li.size() >= 2) {
				return new REPoint(li.get(0), li.get(1), 0);
			}
		}
		throw new NullPointerException("Can not read the MapSize");
	}

	/**
	 * Read the SnakeMeta from the post data on save or public call.
	 * @param httpMeta
	 * @param myMap 
	 */
	private void setSnakeMeta(HttpMeta httpMeta, SnakeMap myMap) {
		String rowString = this.getStringFromRequest(httpMeta, "player", "");
		IntegerScan is = new IntegerScan(rowString);
		if (is.hasNext()) {
			List<Integer> li = is.next();
			myMap.setSnakeMeta(new REPoint(li));
			if (li.size() >= 5) {
				myMap.setSnakeAngle(li.get(3));
				myMap.setSnakeSize(li.get(4));
				return;
			}

		}
		throw new NullPointerException("Can not read the SnakeMeta, " + rowString);
	}
}
