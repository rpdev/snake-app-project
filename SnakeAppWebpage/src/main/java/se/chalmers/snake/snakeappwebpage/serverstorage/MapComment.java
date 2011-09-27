package se.chalmers.snake.snakeappwebpage.serverstorage;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 * A Comment by a user for a public map on the page.
 * The Comment will be del for while the PostMap that own the Comment is remove.
 * ( The PostMap will normal never be remove from the database and only mark as DELETE )
 * 
 */
@Entity
public class MapComment extends SelfPersistence implements Serializable {

	private static final long serialVersionUID = -8555087314540355004L;
	//<editor-fold defaultstate="collapsed" desc="Variable Declaration">
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Temporal(javax.persistence.TemporalType.DATE)
	@Column(nullable = false)
	private Date publicDate;
	@OneToOne
	@Column(nullable = true)
	private UserAccount userAccount;
	@Column(nullable = false, length = 512)
	private String message;
	//</editor-fold>

	public MapComment() {
		this.id=0;
		this.publicDate = new Date();
		this.userAccount = null;
		this.message = "";
	}

	public MapComment(UserAccount user, String message) {
		this.userAccount = user;
		this.id=0;
		if (message != null) {
			this.message = message;
		} else {
			this.message = "";
		}
		this.publicDate = new Date();
	}

	//<editor-fold defaultstate="collapsed" desc="Get and Set">
	public long getId() {
		return id;
	}

	public void setId(long id) {
		if(this.id==0) {
		this.id = id;
		}
	}

	public Date getPublicDate() {
		return publicDate;
	}

	public void setPublicDate(Date publicDate) {
		if (publicDate != null) {
			this.publicDate = publicDate;
		}
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		if (message != null) {
			this.message = message;
		} else {
			this.message = "";
		}
	}

	public UserAccount getUserAccount() {
		return this.userAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		
		this.userAccount = userAccount;
	}

	//</editor-fold>
	//<editor-fold defaultstate="collapsed" desc="Object Override">
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (int) id;
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof MapComment)) {
			return false;
		}
		MapComment other = (MapComment) object;
		if (this.id != other.id) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "MapComment{" + "id=" + id + ", publicDate=" + publicDate + ", userAccount=" + userAccount + ", message=" + message + '}';
	}
	//</editor-fold>
	//<editor-fold defaultstate="collapsed" desc="Track Method">
	@Override
	SelfPersistence trackPersistence(EntityManager entityManager) {
		if (this.userAccount != null) {
			this.userAccount.trackPersistence(entityManager);
		}
		return entityManager.merge(this);
	}

	@Override
	boolean trackDestroy(EntityManager entityManager, SelfPersistence removeObj) {
		if(removeObj == this) {
			this.userAccount = null;
			entityManager.remove(this);
			return true;
		}
		return false;
	}
	//</editor-fold>
}
