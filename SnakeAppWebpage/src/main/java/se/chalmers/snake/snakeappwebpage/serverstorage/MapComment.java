package se.chalmers.snake.snakeappwebpage.serverstorage;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 */
@Entity
public class MapComment implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//<editor-fold defaultstate="collapsed" desc="Variable Declaration">
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	
	@Temporal(javax.persistence.TemporalType.DATE)
	@Column(nullable=false)
	private Date publicDate;
	
	@OneToOne
	@JoinColumn
	private UserAccount userAccount;
	
	@Column(nullable=false,length=512)
	private String message;
	//</editor-fold>
	
	//<editor-fold defaultstate="collapsed" desc="Get and Set">
	public long getId() {
		return id;
	}
	/*
	 * public void setId(long id) {
	 * this.id = id;
	 * }
	 */
	
	public Date getPublicDate() {
		return publicDate;
	}
	/*
	 * public void setCurrentDate(Date currentDate) {
	 * this.currentDate = currentDate;
	 * }
	 */
	
	public String getMessage() {
		return message;
	}
	/*
	 * public void setMessage(String message) {
	 * this.message = message;
	 * }
	 */
	
	public UserAccount getUserAccount() {
		return userAccount;
	}
	/*
	 * public void setUserAccount(UserAccount userAccount) {
	 * this.userAccount = userAccount;
	 * }
	 */
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
		if (!(object instanceof MapComment)) {
			return false;
		}
		MapComment other = (MapComment) object;
		if (this.id != other.id) {
			return false;
		}
		return true;
	}

	
}
