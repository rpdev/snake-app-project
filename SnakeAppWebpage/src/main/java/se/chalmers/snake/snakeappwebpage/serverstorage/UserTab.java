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
public class UserTab extends MyPersistence implements Serializable {

	public static final Class<UserTab> PORTOTYPE = UserTab.class;
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	protected UserTab() {
	}

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
		if (!(object instanceof UserTab)) {
			return false;
		}
		UserTab other = (UserTab) object;
		if (this.id != other.id) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "UserTab[ id=" + id + " ]";
	}

	
	@Override
	protected void trackPersistence(EntityManager entityManager) {
		entityManager.merge(this);
	}

	@Override
	protected boolean trackDestroy(EntityManager entityManager, MyPersistence removeObj) {
		entityManager.remove(this);
		return true;
	}
}
