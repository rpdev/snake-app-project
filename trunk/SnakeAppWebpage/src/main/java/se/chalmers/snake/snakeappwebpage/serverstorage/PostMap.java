package se.chalmers.snake.snakeappwebpage.serverstorage;

import java.io.Serializable;
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
import javax.persistence.Temporal;

/**
 *
 */
@Entity
public class PostMap extends SelfPersistence implements Serializable {

	@ManyToOne
	@Column(nullable=true)
	private UserAccount userAccount;

	public static enum Status {
		PUBLIC,
		DELETE,
		CREATE
	}
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date publicDate;
	@Column(nullable = false)
	private Status status;
	@OneToMany
	private List<MapComment> comments;

	
	
	

	//<editor-fold defaultstate="collapsed" desc="Set and Get">
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<MapComment> getComments() {
		return comments;
	}

	public void setComments(List<MapComment> comments) {
		this.comments = comments;
	}

	public Date getPublicDate() {
		return publicDate;
	}

	public void setPublicDate(Date publicDate) {
		this.publicDate = publicDate;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
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
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	boolean trackDestroy(EntityManager entityManager, SelfPersistence removeObj) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
