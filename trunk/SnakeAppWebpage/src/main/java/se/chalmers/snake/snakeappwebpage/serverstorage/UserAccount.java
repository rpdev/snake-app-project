package se.chalmers.snake.snakeappwebpage.serverstorage;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * User Account for a user.
 */
@Entity
public class UserAccount extends SelfPersistence implements Serializable {

	public static final Class<UserAccount> PORTOTYPE = UserAccount.class;
	private static final long serialVersionUID = 1L;
	//<editor-fold defaultstate="collapsed" desc="Variable Declaration">
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(nullable = false, unique=true)
	private String userName;
	@Column(nullable = false)
	private String userPassword;
	@Column(nullable = false)
	private String userMail;
	@Column(nullable = false)
	private String userDescription;
	@OneToMany(mappedBy = "userAccount")
	private List<PostMap> maps;
	
	
	//</editor-fold>
	public UserAccount() {}
	
	//<editor-fold defaultstate="collapsed" desc="Get and Set">
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getUserDescription() {
		return userDescription;
	}
	
	public void setUserDescription(String userDescription) {
		this.userDescription = userDescription;
	}
	
	public String getUserMail() {
		return userMail;
	}
	
	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserPassword() {
		return userPassword;
	}
	
	public void setUserPassword(String userPassword) {
		this.userPassword = ServerStorage.SHAHashString(userPassword);
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
		if (!(object instanceof UserAccount)) {
			return false;
		}
		UserAccount other = (UserAccount) object;
		if (this.id != other.id) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return "UserAccount{" + "id=" + id + ", userName=" + userName + ", userPassword=" + userPassword + ", userMail=" + userMail + ", userDescription=" + userDescription + '}';
	}
	//</editor-fold>
	
	
	@Override
	boolean trackDestroy(EntityManager entityManager, SelfPersistence removeObj) {
		if (removeObj == this) {
			entityManager.remove(this);
		}
		return true;
	}

	@Override
	SelfPersistence trackPersistence(EntityManager entityManager) {
		return entityManager.merge(this);
		
	}
}
