package se.chalmers.snake.snakeappwebpage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.servlet.annotation.WebServlet;
import se.chalmers.snake.snakeappwebpage.lib.HttpServletBuilder;
import se.chalmers.snake.snakeappwebpage.serverstorage.Database;
import se.chalmers.snake.snakeappwebpage.serverstorage.MPoint;
import se.chalmers.snake.snakeappwebpage.serverstorage.REPoint;
import se.chalmers.snake.snakeappwebpage.serverstorage.SnakeMap;
import se.chalmers.snake.snakeappwebpage.serverstorage.UserAcc;

/**
 *
 */
@WebServlet(name = "EditMap", urlPatterns = {"/editmap"})
public class EditMap extends HttpServletBuilder {

	private static final long serialVersionUID = 2244928737289982716L;

	@Override
	protected void pageRequest(HttpMeta httpMeta, HttpOutput httpOutput) throws Exception {

		String action = httpMeta.REQUEST("action"); // Type of action
		// Allow types is "save","public","get","delete"

		if ("get".equals(action)) {
			this.getMap(httpMeta, httpOutput);
		} else if ("save".equals(action)) {
			this.storeMap(httpMeta, httpOutput, false);
		} else if ("public".equals(action)) {
			this.storeMap(httpMeta, httpOutput, true);
		}
	}

	private boolean isAccess(int mapID) {
		return true;
	}

	/**
	 * Here most somebody put code for get current userAccount for this user.
	 * Or null if user not are access to the system.
	 * @return 
	 */
	private UserAcc getUserAccount() {
		return new UserAcc("meme", "mypassword", "meme@se.se");
	}

	private void getMap(HttpMeta httpMeta, HttpOutput httpOutput) {
		try {
			httpOutput.getWriter().append("hello world");
		} catch (Exception ex) {
		}
	}

	private void storeMap(HttpMeta httpMeta, HttpOutput httpOutput, boolean publicMap) {
		UserAcc userAcc = this.getUserAccount();
		if (userAcc != null) {
			int mapID = this.getIntFromRequest(httpMeta, "id", -1);
			SnakeMap myMap = null;

			if (mapID > 0) {
				myMap = Database.getInstance().getEntity(SnakeMap.class, Long.valueOf(mapID));
			}
			if (myMap == null) {
				myMap = new SnakeMap(userAcc);
			}
			// Now we have the map Obj, test if this user are onwer of this map.
			if (myMap.getUserName().equals(userAcc)) {
				// You is allow to edit the map.
				myMap.setMapName(this.getStringFromRequest(httpMeta, "name",""));
				myMap.setMapDescription(this.getStringFromRequest(httpMeta, "description",""));
				myMap.setDifficuly(this.getIntFromRequest(httpMeta, "difficuly", 2,1,4));
				myMap.setGameSpeed(new MPoint("simple",this.getIntFromRequest(httpMeta, "gamespeed", 5,1,Integer.MAX_VALUE)));
				myMap.setGrowthspeed(new MPoint("simple", this.getIntFromRequest(httpMeta, "growth", 2,0,Integer.MAX_VALUE)));
				myMap.setItemPoint(new MPoint("simple", this.getIntFromRequest(httpMeta, "collectiblecount", 1,1,Integer.MAX_VALUE)));
				myMap.setItemSize(this.getIntFromRequest(httpMeta, "collectiblecount", 1,1,Integer.MAX_VALUE));
				
				myMap.setMapSize(this.getMapSize(httpMeta));
				this.setSnakeMeta(httpMeta, myMap);
				myMap.setObstacle(this.getObstacle(httpMeta));
			
				if(publicMap) {
					myMap.setStatus(Database.STATUS.PUBLICHED);
				}
				
				
			}
		}
	}

	private List<REPoint> getObstacle(HttpMeta httpMeta) {
		List<REPoint> points = new ArrayList<REPoint>();
		String str = this.getStringFromRequest(httpMeta, "obstacle","");
		
		
		
		return points;
	}
	
	private int getIntFromRequest(HttpMeta httpMeta, String key, int nullValue) {
		try {
			return Integer.parseInt(httpMeta.REQUEST(key));
		} catch (Exception ex) {
			return nullValue;
		}
	}
	private int getIntFromRequest(HttpMeta httpMeta, String key, int nullValue, int min, int max) {
		try {
			int value =  Integer.parseInt(httpMeta.REQUEST(key));
			return value>=min&&value<=max?value:nullValue;
		} catch (Exception ex) {
			return nullValue;
		}
	}

	private String getStringFromRequest(HttpMeta httpMeta, String key, String nullValue) {
		String value = httpMeta.REQUEST(key);
		return value != null ? value : nullValue;
	}

	private REPoint getMapSize(HttpMeta httpMeta) {
		
		return new REPoint(0,0,0);
	}

	private void setSnakeMeta(HttpMeta httpMeta, SnakeMap myMap) {
		myMap.setSnakeAngle(0);
		myMap.setSnakeSize(10);
		myMap.setSnakeMeta(new REPoint(0,0,0));
	}
}
