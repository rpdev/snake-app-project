package se.chalmers.snake.snakeappwebpage.serverstorage;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 */
@Entity
public class SnakeMap  extends SelfPersistence implements Serializable{
	private static final long serialVersionUID = -1792642630083319368L;
	
	
	
	
	@Id
   @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (int) id;
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof SnakeMap)) {
			return false;
		}
		SnakeMap other = (SnakeMap) object;
		if (this.id != other.id) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "se.chalmers.snake.snakeappwebpage.serverstorage.SnakeMap[ id=" + id + " ]";
	}

	@Override
	SelfPersistence trackPersistence(EntityManager entityManager) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	boolean trackDestroy(EntityManager entityManager, SelfPersistence removeObj) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	void override(SnakeMap map) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
}
