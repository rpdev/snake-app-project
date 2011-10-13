package se.chalmers.snake.snakeappwebpage;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import se.chalmers.snake.snakeappwebpage.lib.HttpServletBuilder;
import se.chalmers.snake.snakeappwebpage.lib.IntegerScan;
import se.chalmers.snake.snakeappwebpage.login.LoginServlet;
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

	/**
	 * The main function of the Servlets and handle the first call of this Servlet.
	 * @param httpMeta 
	 * @param httpOutput
	 * @throws Exception 
	 */
	@Override
	protected void pageRequest(HttpMeta httpMeta, HttpOutput httpOutput) throws Exception {
		httpMeta.setContentType("text/xml;charset=UTF-8");
		String action = httpMeta.REQUEST("action"); // Type of action
		// Allow types is "save","public","get","delete"

        if ("get".equals(action)) {
            this.getMap(httpMeta, httpOutput);
        } else if ("save".equals(action)) {
            try {
                System.out.println("Trying to save map");
                this.storeMap(httpMeta, false).generateXML(httpOutput.getWriter());
            } catch (Exception ex) {
            }
        } else if ("public".equals(action)) {
            try {
                this.storeMap(httpMeta, true).generateXML(httpOutput.getWriter());
            } catch (Exception ex) {
            }
        }
    }

	
	/**
	 * Get the current user account that has access the page
	 * @param httpMeta
	 * @return The UserAcc if a user are access to the page, else null.
	 */
	private UserAcc getUserAccount(HttpMeta httpMeta) {
		return LoginServlet.getUserAccount(httpMeta);
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
                } catch (Exception ex) {}
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
                System.out.println(myMap);
                return myMap;
            }
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
        throw new NullPointerException("Can not read the SnakeMeta");
    }
}