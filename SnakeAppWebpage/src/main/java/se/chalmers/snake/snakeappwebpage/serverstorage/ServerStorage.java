package se.chalmers.snake.snakeappwebpage.serverstorage;

import java.security.MessageDigest;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


/**
 *
 */
public class ServerStorage {

	protected static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("snakeappweb_pu");

	public static <T extends SelfPersistence> T getByIndex(Class<T> prototype, Object index) {
		try {
			T obj = ServerStorage.ENTITY_MANAGER_FACTORY.createEntityManager().find(prototype, index);
			return obj;
		} catch(Exception ex) {
			return null;
		}
	}
	
	//<editor-fold defaultstate="collapsed" desc="getAll(...) and size(...)">
	public static <T extends SelfPersistence> long size(Class<T> tableClass) {
		EntityManager entityManager = ServerStorage.ENTITY_MANAGER_FACTORY.createEntityManager();
		
		CriteriaQuery<Long> query = entityManager.getCriteriaBuilder().createQuery(Long.class);
		Root<T> table = query.from(tableClass);
		query.select(entityManager.getCriteriaBuilder().count(table));
		return entityManager.createQuery(query).getSingleResult().longValue();
	}
	
	
	public static <T extends SelfPersistence> List<T> getAll(Class<T> tableClass) {
		EntityManager entityManager = ServerStorage.ENTITY_MANAGER_FACTORY.createEntityManager();
		CriteriaQuery<T> query = entityManager.getCriteriaBuilder().createQuery(tableClass);
		Root<T> table = query.from(tableClass);
		query.select(table);
		return entityManager.createQuery(query).getResultList();
	}
	
	public static <T extends SelfPersistence> List<T> getAll(Class<T> tableClass, int start, int limit) {
		EntityManager entityManager = ServerStorage.ENTITY_MANAGER_FACTORY.createEntityManager();
		CriteriaQuery<T> query = entityManager.getCriteriaBuilder().createQuery(tableClass);
		Root<T> table = query.from(tableClass);
		query.select(table);
		return entityManager.createQuery(query).setFirstResult(start).setMaxResults(limit).getResultList();
	}
	//</editor-fold>
	
	//<editor-fold defaultstate="collapsed" desc="Secure Hash Functions">
	public static String SHA1HashString(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA");
			if (str != null) {
				md.update(str.getBytes("UTF-8"));
			}
			return new sun.misc.BASE64Encoder().encode(md.digest());
		} catch (Exception ex) {
			throw new UnsupportedOperationException("SHA Function is not work, I say use PHP.");
		}
	}
	//</editor-fold>
}
