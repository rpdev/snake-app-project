package se.chalmers.snake.snakeappwebpage.serverstorage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQuery(name="UserAcc.findUserName",
		query="SELECT c FROM UserAcc c WHERE c.userName = :name")
public class UserAcc implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(nullable = false)
	private String userName;
	@Column(nullable = false)
	private String userPassword;
	@Column(nullable = false)
	private String email;
	@OneToMany
	private List<Comment> userComments = new ArrayList<Comment>();
	@OneToMany
	private List<SnakeMap> createMaps = new ArrayList<SnakeMap>();

	public UserAcc() {
	}

	public UserAcc(String userName, String userPassword, String email) {
		this.userName = userName;
		this.userPassword = Database.getInstance().hashPassword(userPassword);
		this.email = email;
	}

	public boolean login(String passwordClearText) {
		return getUserPassword().equals(Database.getInstance().hashPassword(passwordClearText));
	}

	public boolean addComment(Comment comment) {
		return getUserComments().add(comment);
	}

	public boolean removeComment(Comment comment) {
		return userComments.remove(comment);
	}

	public boolean addMap(SnakeMap map) {
		return getCreateMaps().add(map);
	}

	public boolean removemap(SnakeMap map) {
		return createMaps.remove(map);
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
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the userPassword
	 */
	public String getUserPassword() {
		return userPassword;
	}

	/**
	 * @param userPassword the userPassword to set
	 */
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the userComments
	 */
	public List<Comment> getUserComments() {
		return userComments;
	}

	/**
	 * @param userComments the userComments to set
	 */
	public void setUserComments(List<Comment> userComments) {
		this.userComments = userComments;
	}

	/**
	 * @return the createMaps
	 */
	public List<SnakeMap> getCreateMaps() {
		return createMaps;
	}

	/**
	 * @param createMaps the createMaps to set
	 */
	public void setCreateMaps(List<SnakeMap> createMaps) {
		this.createMaps = createMaps;
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
		if (!(object instanceof UserAcc)) {
			return false;
		}
		UserAcc other = (UserAcc) object;
		if (this.getUserName().equals(other.getUserName()) && this.getUserPassword().equals(other.getUserPassword())) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "UserAcc{" + "id=" + id + ", userName=" + userName + ", email=" + email + ", userComments=" + userComments.size() + ", createMaps=" + createMaps.size() + '}';
	}
}
