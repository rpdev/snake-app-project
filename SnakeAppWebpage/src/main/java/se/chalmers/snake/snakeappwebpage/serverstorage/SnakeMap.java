package se.chalmers.snake.snakeappwebpage.serverstorage;

import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
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
import se.chalmers.snake.snakeappwebpage.serverstorage.Database.STATUS;

@Entity
public class SnakeMap implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private UserAcc userName;
    @Column
    private Double mapRating;
    @Column
    private int mapRateCount;
    @Enumerated(EnumType.STRING)
    private STATUS status;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<Comment>();
    @Column(nullable = false, updatable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date creationDate;
    @Column
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date publicedDate;
    @Column
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date editedDate;
    /**
     * REPoint has value
     * int x,y,r
     */
    @OneToOne(cascade = CascadeType.ALL)
    private REPoint snakeMeta;
    @Column
    private Integer snakeAngle;
    @Column
    private Integer snakeSize;
    @OneToMany(cascade = CascadeType.ALL)
    private List<REPoint> obstacle;
    @OneToOne(cascade = CascadeType.ALL)
    private REPoint mapSize; // Only use x,y
    /**
     * MPoint
     * String type ( short string max 16 char )
     * int value
     */
    @OneToOne(cascade = CascadeType.ALL)
    private MPoint gameSpeed;
    @OneToOne(cascade = CascadeType.ALL)
    private MPoint growthspeed;
    @OneToOne(cascade = CascadeType.ALL)
    private MPoint levelgoal;
    @OneToOne(cascade = CascadeType.ALL)
    private MPoint itemPoint;
    @Column
    private Integer itemSize;
    @Column
    private String mapName;
    @Column
    private String mapDescription;
    @Column
    private Integer difficuly; // 1 to 4 ( 1:easy,2:normal,3:hard,4:vary Hard

    public SnakeMap() {
        this.obstacle = new ArrayList<REPoint>();
        this.mapRating = 0.0;
    }

    public SnakeMap(UserAcc user) {
        this.userName = user;
        creationDate = new Date();
        status = STATUS.CREATED;
        this.obstacle = new ArrayList<REPoint>();
        this.mapRating = 0.0;
    }

    public boolean addComment(Comment comment) {
        return comments.add(comment);
    }

    public boolean removeComment(Comment comment) {
        return comments.remove(comment);
    }

    /**
     * Return an string representing this map as xml, note that if the map
     * dosn't contain any data will the xml only contain one node. If any
     * of the following exception occur will this fuction return null,
     * {@link TransformerConfigurationException}, {@link TransformerException},
     * {@link ParserConfigurationException}.
     * 
     * @return XML representation of this level.
     */
    public String generateXML() {
        StringWriter sw = new StringWriter();
        if (this.generateXML(sw)) {
            return sw.toString();
        } else {
            return null;
        }
    }

    /**
     * Return an string representing this map as xml, note that if the map
     * dosn't contain any data will the xml only contain one node. If any
     * of the following exception occur will this fuction return null,
     * {@link TransformerConfigurationException}, {@link TransformerException},
     * {@link ParserConfigurationException}.
     * 
     * @return XML representation of this level.
     */
    public boolean generateXML(Writer ioWriter) {
        try {
            Document xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element rootNode = xmlDocument.createElement(XMLKeys.ROOT.key);
            rootNode.setAttribute(XMLKeys.ROOT_ID.key, id.toString());


            if (mapName != null) {
                Element nameNode = xmlDocument.createElement(XMLKeys.NAME.key);
                nameNode.appendChild(xmlDocument.createTextNode(mapName));
                rootNode.appendChild(nameNode);
            }
            if (difficuly != null) {
                Element descriptionNode = xmlDocument.createElement(XMLKeys.DIFFICULY.key);
                descriptionNode.appendChild(xmlDocument.createTextNode(this.difficuly.toString()));
                rootNode.appendChild(descriptionNode);
            }

            if (mapDescription != null) {
                Element descriptionNode = xmlDocument.createElement(XMLKeys.DESCRIPTION.key);
                descriptionNode.appendChild(xmlDocument.createTextNode(mapDescription));
                rootNode.appendChild(descriptionNode);
            }
            if (mapSize != null) {
                Element mapSizeNode = xmlDocument.createElement(XMLKeys.MAPSIZE.key);
                mapSizeNode.setAttribute(XMLKeys.X.key, mapSize.getX().toString());
                mapSizeNode.setAttribute(XMLKeys.Y.key, mapSize.getY().toString());
                rootNode.appendChild(mapSizeNode);
            }
            if (gameSpeed != null) {
                Element speedNode = xmlDocument.createElement(XMLKeys.GAMESPEED.key);
                speedNode.setAttribute(XMLKeys.CFUNC.key, gameSpeed.getType());
                speedNode.appendChild(xmlDocument.createTextNode(gameSpeed.getIntValue().toString()));
                rootNode.appendChild(speedNode);
            }
            if (growthspeed != null) {
                Element growthNode = xmlDocument.createElement(XMLKeys.GROWTHSPEED.key);
                growthNode.setAttribute(XMLKeys.CFUNC.key, growthspeed.getType());
                growthNode.appendChild(xmlDocument.createTextNode(growthspeed.getIntValue().toString()));
                rootNode.appendChild(growthNode);
            }
            if (levelgoal != null) {
                Element goalNode = xmlDocument.createElement(XMLKeys.GOAL.key);
                goalNode.setAttribute(XMLKeys.CFUNC.key, levelgoal.getType());
                goalNode.appendChild(xmlDocument.createTextNode(levelgoal.getIntValue().toString()));
                rootNode.appendChild(goalNode);
            }
            if (itemPoint != null) {
                Element itemNode = xmlDocument.createElement(XMLKeys.GOAL.key);
                itemNode.setAttribute(XMLKeys.CFUNC.key, itemPoint.getType());
                itemNode.setAttribute(XMLKeys.RADIUS.key, Integer.toString(itemSize));
                itemNode.appendChild(xmlDocument.createTextNode(itemPoint.getIntValue().toString()));
                rootNode.appendChild(itemNode);
            }
            if (snakeMeta != null && snakeAngle != null && snakeSize != null) {
                Element snakeNode = xmlDocument.createElement(XMLKeys.PLAYER.key);
                snakeNode.setAttribute(XMLKeys.X.key, snakeMeta.getX().toString());
                snakeNode.setAttribute(XMLKeys.Y.key, snakeMeta.getY().toString());
                snakeNode.setAttribute(XMLKeys.RADIUS.key, snakeMeta.getR().toString());
                snakeNode.setAttribute(XMLKeys.ANGLE.key, snakeAngle.toString());
                snakeNode.setAttribute(XMLKeys.PLAYER_LENGTH.key, snakeSize.toString());
                rootNode.appendChild(snakeNode);
            }
            if (obstacle != null) {
                Element obstaclesNode = xmlDocument.createElement(XMLKeys.OBSTACLES.key);
                for (REPoint r : obstacle) {
                    Element dataNode = xmlDocument.createElement(XMLKeys.OBSTACLES.key);
                    dataNode.setAttribute(XMLKeys.X.key, r.getX().toString());
                    dataNode.setAttribute(XMLKeys.Y.key, r.getY().toString());
                    dataNode.setAttribute(XMLKeys.RADIUS.key, r.getR().toString());
                    obstaclesNode.appendChild(dataNode);
                }
                rootNode.appendChild(obstaclesNode);
            }

            // http://www.petefreitag.com/item/445.cfm
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            StreamResult result = new StreamResult(ioWriter);
            DOMSource source = new DOMSource(rootNode);
            transformer.transform(source, result);
            return true;

        } catch (TransformerConfigurationException ex) {
        } catch (TransformerException ex) {
        } catch (ParserConfigurationException ex) {
        }
        return false;
    }

    /**
     * @return the mapRating
     */
    public Double getMapRating() {
        return this.mapRating;
    }
    
    /**
     * This method returns the mapRating rounded to nearest half
     */
    public Double getRoundedMapRating(){
        Double result = getMapRating();
        result *= 2;
        result = Math.floor(result + 0.5);
        result /= 2;
        return result;
    }

    /**
     * @param mapRating the mapRating to set
     */
    public void setMapRating(Double mapRating) {
        this.mapRating = mapRating;
    }

    public void rateMap(int mapRating) {
        rateMap(new Double(mapRating));
    }

    public void rateMap(Double mapRating) {
        if (getMapRating() == 0.0) {
            this.mapRating += mapRating;
            this.mapRateCount = 1;
        } else {
            this.mapRating = (this.mapRating * this.mapRateCount) + mapRating;
            this.mapRateCount++;
            this.mapRating /= this.mapRateCount;
        }
    }

    private enum XMLKeys {

        ROOT("snakeappmap"),
        ROOT_ID("id"),
        NAME("name"),
        DESCRIPTION("description"),
        MAPSIZE("map"),
        GAMESPEED("gamespeed"),
        GROWTHSPEED("growthspeed"),
        GOAL("levelgoal"),
        CFUNC("type"),
        RADIUS("r"),
        ITEMS("item"),
        LEVEL("level"),
        PLAYER("player"),
        X("x"),
        Y("y"),
        ANGLE("a"),
        PLAYER_LENGTH("s"),
        OBSTACLES("obstacles"),
        DIFFICULY("difficuly");
        private final String key;

        private XMLKeys(String key) {
            this.key = key;
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Getters & setters">
    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the userName
     */
    public UserAcc getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(UserAcc userName) {
        this.userName = userName;
    }

    /**
     * @return the status
     */
    public STATUS getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(STATUS status) {
        if (status == STATUS.CREATED) {
            this.creationDate = new Date();
        } else if (status == STATUS.PUBLICHED) {
            this.publicedDate = new Date();
        }
        this.status = status;
    }

    /**
     * @return the comments
     */
    public List<Comment> getComments() {
        return comments;
    }

    /**
     * @return the comments as strings
     */
    public List<String> getCommentsToString() {
        List<String> commentList = new ArrayList<String>();
        for (Comment comment : getComments()) {
            commentList.add(comment.toSimpleString());
        }
        if (commentList.isEmpty()) {
            commentList.add("No comments yet.");
        }
        return commentList;
    }

    /**
     * @param comments the comments to set
     */
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    /**
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * @return the publicedDate
     */
    public Date getPublicedDate() {
        return publicedDate;
    }

    /**
     * @param publicedDate the publicedDate to set
     */
    public void setPublicedDate(Date publicedDate) {
        this.publicedDate = publicedDate;
    }

    /**
     * @return the editedDate
     */
    public Date getEditedDate() {
        return editedDate;
    }

    /**
     * @param editedDate the editedDate to set
     */
    public void setEditedDate(Date editedDate) {
        this.editedDate = editedDate;
    }

    /**
     * @return the snakeMeta
     */
    public REPoint getSnakeMeta() {
        return snakeMeta;
    }

    /**
     * @param snakeMeta the snakeMeta to set
     */
    public void setSnakeMeta(REPoint snakeMeta) {
        this.snakeMeta = snakeMeta;
    }

    /**
     * @return the snakeAngle
     */
    public Integer getSnakeAngle() {
        return snakeAngle;
    }

    /**
     * @param snakeAngle the snakeAngle to set
     */
    public void setSnakeAngle(Integer snakeAngle) {
        this.snakeAngle = snakeAngle;
    }

    /**
     * @return the snakeSize
     */
    public Integer getSnakeSize() {
        return snakeSize;
    }

    /**
     * @param snakeSize the snakeSize to set
     */
    public void setSnakeSize(Integer snakeSize) {
        this.snakeSize = snakeSize;
    }

    /**
     * @return the obstacle
     */
    public List<REPoint> getObstacle() {
        return obstacle;
    }

    /**
     * @param obstacle the obstacle to set
     */
    public void setObstacle(List<REPoint> obstacle) {
        if (obstacle != null) {
            this.obstacle.addAll(obstacle);
        }
    }

    /**
     * @return the mapSize
     */
    public REPoint getMapSize() {
        return mapSize;
    }

    /**
     * @param mapSize the mapSize to set
     */
    public void setMapSize(REPoint mapSize) {
        this.mapSize = mapSize;
    }

    /**
     * @return the gameSpeed
     */
    public MPoint getGameSpeed() {
        return gameSpeed;
    }

    /**
     * @param gameSpeed the gameSpeed to set
     */
    public void setGameSpeed(MPoint gameSpeed) {
        this.gameSpeed = gameSpeed;
    }

    /**
     * @return the growthspeed
     */
    public MPoint getGrowthspeed() {
        return growthspeed;
    }

    /**
     * @param growthspeed the growthspeed to set
     */
    public void setGrowthspeed(MPoint growthspeed) {
        this.growthspeed = growthspeed;
    }

    /**
     * @return the levelgoal
     */
    public MPoint getLevelgoal() {
        return levelgoal;
    }

    /**
     * @param levelgoal the levelgoal to set
     */
    public void setLevelgoal(MPoint levelgoal) {
        this.levelgoal = levelgoal;
    }

    /**
     * @return the itemPoint
     */
    public MPoint getItemPoint() {
        return itemPoint;
    }

    /**
     * @param itemPoint the itemPoint to set
     */
    public void setItemPoint(MPoint itemPoint) {
        this.itemPoint = itemPoint;
    }

    /**
     * @return the itemCount
     */
    public Integer getItemSize() {
        return itemSize;
    }

    /**
     * @param itemCount the itemCount to set
     */
    public void setItemSize(Integer itemCount) {
        this.itemSize = itemCount;
    }

    /**
     * @return the mapName
     */
    public String getMapName() {
        return mapName;
    }

    /**
     * @param mapName the mapName to set
     */
    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    /**
     * @return the mapDescription
     */
    public String getMapDescription() {
        return mapDescription;
    }

    /**
     * @param mapDescription the mapDescription to set
     */
    public void setMapDescription(String mapDescription) {
        this.mapDescription = mapDescription;
    }

    /**
     * @return the difficuly
     */
    public int getDifficuly() {
        return difficuly;
    }

    /**
     * @param difficuly the difficuly to set
     */
    public void setDifficuly(int difficuly) {
        this.setDifficuly((Integer) difficuly);
    }

    /**
     * @param difficuly the difficuly to set
     */
    public void setDifficuly(Integer difficuly) {
        this.difficuly = difficuly;
    }
    //</editor-fold>

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SnakeMap)) {
            return false;
        }
        SnakeMap other = (SnakeMap) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SnakeMap{" + "id=" + id + ", userName=" + userName + ", mapRating=" + mapRating + ", status=" + status + ", comments=" + comments + ", creationDate=" + creationDate + ", publicedDate=" + publicedDate + ", editedDate=" + editedDate + ", snakeMeta=" + snakeMeta + ", snakeAngle=" + snakeAngle + ", snakeSize=" + snakeSize + ", obstacle=" + obstacle + ", mapSize=" + mapSize + ", gameSpeed=" + gameSpeed + ", growthspeed=" + growthspeed + ", levelgoal=" + levelgoal + ", itemPoint=" + itemPoint + ", itemSize=" + itemSize + ", mapName=" + mapName + ", mapDescription=" + mapDescription + ", difficuly=" + difficuly + '}';
    }
}
