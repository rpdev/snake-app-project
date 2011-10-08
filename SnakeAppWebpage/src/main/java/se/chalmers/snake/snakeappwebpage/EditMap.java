package se.chalmers.snake.snakeappwebpage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.servlet.annotation.WebServlet;
import se.chalmers.snake.snakeappwebpage.lib.HttpServletBuilder;
import se.chalmers.snake.snakeappwebpage.lib.IntegerScan;
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
            int mapID = this.getIntFromRequest(httpMeta, "id", -1);
            if (mapID > 0) {
                SnakeMap myMap = Database.getInstance().getEntity(SnakeMap.class, Long.valueOf(mapID));
                if (myMap != null) {
                    myMap.generateXML(httpOutput.getWriter());
                }
            }
        } catch (Exception ex) {
        }
    }

    private SnakeMap storeMap(HttpMeta httpMeta, boolean publicMap) throws Exception {
        UserAcc userAcc = this.getUserAccount();
        if (userAcc != null) {
            //int mapID = this.getIntFromRequest(httpMeta, "id", -1);
            SnakeMap myMap = null;

            /*if (mapID > 0) {
                myMap = Database.getInstance().getEntity(SnakeMap.class, Long.valueOf(mapID));
            }*/
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
                myMap.setItemSize(this.getIntFromRequest(httpMeta, "collectiblecount", 1, 1, Integer.MAX_VALUE));

                myMap.setMapSize(this.getMapSize(httpMeta));
                //this.setSnakeMeta(httpMeta, myMap);
                myMap.setObstacle(this.getObstacle(httpMeta));

                if (publicMap) {
                    myMap.setStatus(Database.STATUS.PUBLICHED);
                }
                Database.getInstance().mergeObject(userAcc);
                Database.getInstance().mergeObject(myMap);
                return myMap;
            }
        }
        throw new Exception("No user are access or allow to edit this map");
    }

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

    private int getIntFromRequest(HttpMeta httpMeta, String key, int nullValue) {
        try {
            return Integer.parseInt(httpMeta.REQUEST(key));
        } catch (Exception ex) {
            return nullValue;
        }
    }

    private int getIntFromRequest(HttpMeta httpMeta, String key, int nullValue, int min, int max) {
        try {
            int value = Integer.parseInt(httpMeta.REQUEST(key));
            return value >= min && value <= max ? value : nullValue;
        } catch (Exception ex) {
            return nullValue;
        }
    }

    private String getStringFromRequest(HttpMeta httpMeta, String key, String nullValue) {
        String value = httpMeta.REQUEST(key);
        return value != null ? value : nullValue;
    }

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

    private void setSnakeMeta(HttpMeta httpMeta, SnakeMap myMap) {
        String rowString = this.getStringFromRequest(httpMeta, "player", "");
        IntegerScan is = new IntegerScan(rowString);
        if (is.hasNext()) {
            List<Integer> li = is.next();
            myMap.setSnakeMeta(new REPoint(li));
            if (li.size() >= 5) {
                myMap.setSnakeAngle(li.get(3));
                myMap.setSnakeSize(li.get(4));
            }

        }
        throw new NullPointerException("Can not read the SnakeMeta");
    }
}
