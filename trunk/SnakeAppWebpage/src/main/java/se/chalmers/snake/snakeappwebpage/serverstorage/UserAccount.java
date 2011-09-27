package se.chalmers.snake.snakeappwebpage.serverstorage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * User Account for store info about a user.
 * The user info is the username, password, email, and a description.
 * The username can not be edit after the class has be create.
 */
@Entity
public class UserAccount extends SelfPersistence implements Serializable {
	private static final long serialVersionUID = 1525476798595928312L;


	
	//<editor-fold defaultstate="collapsed" desc="Variable Declaration">
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(nullable = false, unique = true)
	private String userName = null;
	@Column(nullable = false)
	private String userPassword = null;
	@Column(nullable = false)
	private String userMail = null;
	@Column(nullable = false)
	private String userDescription = null;
	@OneToMany(mappedBy = "userAccount")
	private List<PostMap> maps;

	//</editor-fold>

	public UserAccount() {
		this.id = 0;
		this.userName = "";
		this.userPassword = "";
		this.userMail = "";
		this.userDescription = "";
		this.maps = new ArrayList<PostMap>();
	}

	public UserAccount(String name, String email, String password) {
		if (name != null && email != null && password != null) {
			this.id = 0;
			this.userName = name;
			this.userMail = email;
			this.userPassword = ServerStorage.SHA1HashString(password);
			this.maps = new ArrayList<PostMap>();
		} else {
			throw new NullPointerException("name, email, or password is Null");
		}
	}

	//<editor-fold defaultstate="collapsed" desc="Get and Set">
	public long getId() {
		return id;
	}

	public void setId(long newID) {
		if(this.id==0) {
		this.id = newID;
		}
	}

	public String getUserDescription() {
		return userDescription;
	}

	public void setUserDescription(String userDescription) {
		if (userDescription != null) {
			this.userDescription = userDescription;
		} else {
			this.userDescription = "";
		}
	}

	public String getUserMail() {
		return userMail;
	}

	public void setUserMail(String userMail) {
		if (userMail != null) {
			this.userMail = userMail;
		} else {
			this.userMail = "";
		}
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		// Allow only name set if no name are set before.
		if (this.userName.length()==0 && userName!=null) {
			this.userName = userName;
		}
	}

	public String getUserPassword() {
		return this.userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = ServerStorage.SHA1HashString(userPassword);
	}

	public List<PostMap> getMaps() {
		return maps;
	}

	public void setMaps(List<PostMap> maps) {
		if (maps != null && maps.size() > 0) {
			this.maps.addAll(maps);
		}
	}
	//</editor-fold>
	
	public boolean passwordEquals(String password) {
		return this.userPassword.equals(ServerStorage.SHA1HashString(password));
	}
	
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
		return "UserAccount{" + "id=" + id + ", userName=" + userName + ", userMail=" + userMail + ", userDescription=" + userDescription + '}';
	}
	//</editor-fold>
	//<editor-fold defaultstate="collapsed" desc="Track Method">
	/**
	 * {@inheritDoc}
	 * @param entityManager
	 * @param removeObj
	 * @return 
	 */
	@Override
	boolean trackDestroy(EntityManager entityManager, SelfPersistence removeObj) {
		if (removeObj == this) {
			for (PostMap map : this.maps) {
				map.setUserAccount(null);
				map.trackPersistence(entityManager);
			}
			entityManager.remove(this);
			return true;
		} else {
			for (Iterator<PostMap> it = this.maps.iterator(); it.hasNext();) {
				PostMap map = it.next();
				if (map.trackDestroy(entityManager, removeObj)) {
					it.remove();
				}
			}
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 * @param entityManager
	 * @return 
	 */
	@Override
	SelfPersistence trackPersistence(EntityManager entityManager) {
		for (PostMap map : this.maps) {
			map.trackPersistence(entityManager);
		}
		return entityManager.merge(this);

	}
	//</editor-fold>
}
