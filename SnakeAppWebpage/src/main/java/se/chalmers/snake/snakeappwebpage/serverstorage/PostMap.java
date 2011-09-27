package se.chalmers.snake.snakeappwebpage.serverstorage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 * This contains data about a public map and the snake map row data class.
 * The PostMap has a public date, status ( Public, Delete and create ) 
 * This have also a userAccout connect and a list of MapComment do by user.
 * 
 * In case the PostMap is remove the MapComment and SnakeMap that this Obj is link to will
 * be remove.
 * But normal the PostMap will not be remove but only be mark as DELETE
 * This is for speed on the database and computer security for hackers.
 */
@Entity
public class PostMap extends SelfPersistence implements Serializable {

	private static final long serialVersionUID = 6750941702696169546L;

	public static enum Status {

		PUBLIC,
		DELETE,
		CREATE
	}
	//<editor-fold defaultstate="collapsed" desc="Variable Declaration">
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@ManyToOne
	@Column(nullable = true)
	private UserAccount userAccount;
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date publicDate;
	@Column(nullable = false)
	private Status status;
	@OneToMany
	private List<MapComment> comments;
	@OneToOne
	@Column(nullable = false)
	private SnakeMap map;

	//</editor-fold>
	public PostMap() {
		this.id = 0;
		this.userAccount = null;
		this.publicDate = new Date();
		this.status = Status.CREATE;
		this.comments = new ArrayList<MapComment>();
		this.map = new SnakeMap();
	}

	public PostMap(UserAccount userAccount) {
		if (userAccount == null) {
			throw new NullPointerException("UserAccount is null");
		}
		this.id = 0;
		this.userAccount = userAccount;
		this.publicDate = new Date();
		this.status = Status.CREATE;
		this.comments = new ArrayList<MapComment>();
		this.map = new SnakeMap();
	}

	//<editor-fold defaultstate="collapsed" desc="Set and Get">
	public long getId() {
		return id;
	}

	public void setId(long id) {
		if (this.id == 0) {
			this.id = id;
		}
	}

	public List<MapComment> getComments() {
		return comments;
	}

	public void setComments(List<MapComment> comments) {
		if (comments != null && comments.size() > 0) {
			this.comments.addAll(comments);
		}

	}

	public Date getPublicDate() {
		return this.publicDate;
	}

	public void setPublicDate(Date publicDate) {
		if (publicDate != null) {
			this.publicDate = publicDate;
		}
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status status) {
		if (status != null) {
			this.status = status;
			if (status == Status.PUBLIC) {
				this.publicDate = new Date();
			}
		}
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	public SnakeMap getMap() {
		return map;
	}

	public void setMap(SnakeMap map) {
		if (map != null) {
			this.map.override(map);
		}
	}

	//</editor-fold>
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (int) id;
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof PostMap)) {
			return false;
		}
		PostMap other = (PostMap) object;
		if (this.id != other.id) {
			return false;
		}
		return true;
	}

	@Override
	SelfPersistence trackPersistence(EntityManager entityManager) {
		this.map.trackPersistence(entityManager);
		if(this.userAccount!=null) {
			this.userAccount.trackPersistence(entityManager);
		}
		for (MapComment mapComment : this.comments) {
			mapComment.trackPersistence(entityManager);
		}
		
		return entityManager.merge(this);
	}

	@Override
	boolean trackDestroy(EntityManager entityManager, SelfPersistence removeObj) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
