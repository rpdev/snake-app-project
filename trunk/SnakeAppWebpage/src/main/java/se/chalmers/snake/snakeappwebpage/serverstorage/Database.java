package se.chalmers.snake.snakeappwebpage.serverstorage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Database is the class that manage operation to the
 * actual database. This class support adding, getting
 * and removing database entries.
 */
public class Database {

	private static final String DATABASE_NAME = "snakeappweb_pu";
	private static Database instance;
	private final EntityManagerFactory emf;
	private final EntityManager em;

	/**
	 * This class indicate the status of a map, there
	 * are two states CREATED and PUBLICHED.
	 */
	public enum STATUS {

		/**
		 * Map is created but not published, only visible
		 * for the user that created the map.
		 */
		CREATED,
		/**
		 * The map is created and pulished and is therefor
		 * visible for everyone.
		 */
		PUBLICHED
	}

	/**
	 * Database is a singleton and this private constructor initiate
	 * the EntityManager.
	 */
	private Database() {
		emf = Persistence.createEntityManagerFactory(DATABASE_NAME);
		em = emf.createEntityManager();
	}

	/**
	 * Hash a cleartext password by using the cryptation
	 * algorithm SHA-1 and returned the hashed version of the password.
	 * @param passwordClearText Password to be hashed.
	 * @return Hashed password
	 */
	String hashPassword(String passwordClearText) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(passwordClearText.getBytes());
			return new String(md.digest());
		} catch (NoSuchAlgorithmException ex) {
			Logger.getLogger(UserAcc.class.getName()).log(Level.SEVERE, null, ex);
		}
		throw new UnsupportedOperationException("Hashing password failed.");
	}

	/**
	 * Return the instance of the Database class.
	 * @return Instance of the Database.
	 */
	public static Database getInstance() {
		if (instance == null) {
			instance = new Database();
		}
		return instance;
	}

	/**
	 * Search the actual database for an entry.
	 * @param <T> Class of the entry.
	 * @param type Class of the entry.
	 * @param id The id of the desired object.
	 * @return The object if found otherwise null.
	 */
	public synchronized <T> T getEntity(Class<T> type, Long id) {
		return em.find(type, id);
	}

	/**
	 * Remove a disired number of objects of the same class. If
	 * a map is removed will all comments that this user has made be
	 * removed, in the case of maps will the relation between the map
	 * and the user removed. Similar if a map is removed then will this map
	 * be removed from the user that made it and all the comments for this map
	 * be removed.
	 * @param type Type of the object(s) to be removed valid types are 
	 * UserAcc, SnakeMap and Comment.
	 * @param id Id of those object that should be removed.
	 */
	public void removeEnity(Class<?> type, Long... id) {
		if (type == UserAcc.class) {
			removeUser(id);
		} else if (type == SnakeMap.class) {
			removeMap(id);
		} else if (type == Comment.class) {
			removeComment(id);
		} else {
			throw new UnsupportedOperationException(type.getSimpleName() + " is an invalid type");
		}
	}

	public List<UserAcc> getUsersByName(String name) {
		TypedQuery<UserAcc> query = em.createNamedQuery("UserAcc.findUserName", UserAcc.class);
		query.setParameter("name", name);
		return query.getResultList();
	}

	/**
	 * Private class for handeling the removal of comment(s).
	 * @param id Id for those comments that should be removed.
	 */
	private void removeComment(Long... id) {
		em.getTransaction().begin();
		for (Long i : id) {
			Comment c = em.find(Comment.class, i);
			c.cleanUp();
			mergeObject(c.getMap());
			mergeObject(c.getUserCommentViolate());
			em.remove(c);
		}
		em.getTransaction().commit();
	}

	/**
	 * Private class for handeling the removal of map(s), this is done
	 * by first removing all comments for this map.
	 * @param id Id of the maps that should be removed.
	 */
	private void removeMap(Long... id) {
		for (Long i : id) {
			SnakeMap sm = em.find(SnakeMap.class, i);
			List<Long> comment_ids = new ArrayList<Long>();
			for (Comment c : sm.getComments()) {
				comment_ids.add(c.getId());
			}
			sm.getUserName().removemap(sm);
			mergeObject(sm.getUserName());
			em.getTransaction().begin();
			em.remove(sm);
			em.getTransaction().commit();
		}
	}

	private void removeUser(Long... id) {
		for (Long i : id) {
			UserAcc ua = em.find(UserAcc.class, i);
			List<Long> comment_ids = new ArrayList<Long>();
			for (Comment c : ua.getUserComments()) {
				comment_ids.add(c.getId());
			}
			removeComment(comment_ids.toArray(new Long[comment_ids.size()]));
			em.getTransaction().begin();
			em.remove(ua);
			em.getTransaction().commit();
		}
	}

	public synchronized <T> List<T> getEntityList(Class<T> type) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = builder.createQuery(type);
		TypedQuery<T> query = em.createQuery(cq);
		return query.getResultList();
	}

	public <T> boolean mergeObject(T object) {
		em.getTransaction().begin();
		if (em.contains(object)) {
			em.refresh(object);
		} else {
			if(object.getClass() == UserAcc.class){
				UserAcc ua = (UserAcc) object;
				if(!getUsersByName(ua.getUserName()).isEmpty()){
					em.getTransaction().commit();
					return false;
				}
			}
			em.persist(object);
		}
		em.getTransaction().commit();
		return true;
	}

	public synchronized void closeDatabase() {
		em.close();
		emf.close();
		instance = null;
	}
}
