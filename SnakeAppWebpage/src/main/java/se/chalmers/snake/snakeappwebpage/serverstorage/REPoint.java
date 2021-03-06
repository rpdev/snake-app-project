package se.chalmers.snake.snakeappwebpage.serverstorage;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author rickard
 */
@Entity
public class REPoint implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(nullable = false)
	private Integer x;
	@Column(nullable = false)
	private Integer y;
	@Column
	private Integer r;

	public REPoint() {
	}

	/**
	 * Try to read the 3 first post as X,Y,R
	 * This will throws NullPointerException if not the list have last 3 post
	 * @param iList 
	 * 
	 */
	public REPoint(List<Integer> iList) throws NullPointerException {
		if (iList != null && iList.size() >= 3) {
			this.x = iList.get(0);
			this.y = iList.get(1);
			this.r = iList.get(2);
		} else {
			throw new NullPointerException("Can not make REPoint from the List<Integer>");
		}


	}

	public REPoint(int x, int y, int r) {
		this.x = x;
		this.y = y;
		this.r = r;
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
	 * @return the x
	 */
	public Integer getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(Integer x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public Integer getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(Integer y) {
		this.y = y;
	}

	/**
	 * @return the r
	 */
	public Integer getR() {
		return r;
	}

	/**
	 * @param r the r to set
	 */
	public void setR(Integer r) {
		this.r = r;
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
		if (!(object instanceof REPoint)) {
			return false;
		}
		REPoint other = (REPoint) object;
		if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "REPoint{" + "id=" + id + ", x=" + x + ", y=" + y + ", r=" + r + '}';
	}
}
