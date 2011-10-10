package se.chalmers.snake.snakeappwebpage.bean;

import java.beans.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import se.chalmers.snake.snakeappwebpage.serverstorage.Database;
import se.chalmers.snake.snakeappwebpage.serverstorage.SnakeMap;
import se.chalmers.snake.snakeappwebpage.serverstorage.UserAcc;

/**
 *
 * @author rafaelm
 */
@ManagedBean(name = "DatabaseBean")
@SessionScoped
public class DatabaseBean implements Serializable {
    
    private List<SnakeMap> snakeMapList = new ArrayList<SnakeMap>();

    /**
     * @return the snakeMapList
     */
    public List<SnakeMap> getSnakeMapList() {
        return Database.getInstance().getEntityList(SnakeMap.class);
    }

    /**
     * @param snakeMapList the snakeMapList to set
     */
    public void setSnakeMapList(List<SnakeMap> snakeMapList) {
        this.snakeMapList = snakeMapList;
    }
    
}
