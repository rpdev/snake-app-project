package se.chalmers.snake.snakeappwebpage.serverstorage;

import java.io.Serializable;
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
public class MPoint implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(nullable = false)
	private Integer intValue;
	@Column(length = 16, nullable = false)
	private String type;

	public MPoint() {
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
	 * @return the intValue
	 */
	public Integer getIntValue() {
		return intValue;
	}

	/**
	 * @param intValue the intValue to set
	 */
	public void setIntValue(Integer intValue) {
		this.intValue = intValue;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	//</editor-fold>

	public MPoint(String type, int value) {
		this.intValue = value;
		if (type != null) {
			this.type = type;
		} else {
			this.type = "";
		}
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (getId() != null ? getId().hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof MPoint)) {
			return false;
		}
		MPoint other = (MPoint) object;
		if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "MPoint{" + "id=" + id + ", intValue=" + intValue + ", type=" + type + '}';
	}
}
