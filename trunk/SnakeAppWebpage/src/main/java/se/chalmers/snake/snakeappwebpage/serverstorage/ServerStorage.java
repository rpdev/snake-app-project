package se.chalmers.snake.snakeappwebpage.serverstorage;

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
	
	public static <T extends MyPersistence> T newInstance(Class<T> prototype) {
		try {
			T obj = prototype.newInstance();
			return obj;
		} catch (Exception ex) {
			return null;
		}
	}
	
	public static <T extends MyPersistence> T getByIndex(Class<T> prototype, Object index) {
		try {
			T obj = ServerStorage.ENTITY_MANAGER_FACTORY.createEntityManager().find(prototype, index);
			return obj;
		} catch(Exception ex) {
			return null;
		}
	}
	
	public static <T extends MyPersistence> long size(Class<T> tableClass) {
		EntityManager entityManager = ServerStorage.ENTITY_MANAGER_FACTORY.createEntityManager();
		
		CriteriaQuery<Long> query = entityManager.getCriteriaBuilder().createQuery(Long.class);
		Root<T> table = query.from(tableClass);
		query.select(entityManager.getCriteriaBuilder().count(table));
		return entityManager.createQuery(query).getSingleResult().longValue();
	}
	
	
	public static <T extends MyPersistence> List<T> getAll(Class<T> tableClass) {
		EntityManager entityManager = ServerStorage.ENTITY_MANAGER_FACTORY.createEntityManager();
		CriteriaQuery<T> query = entityManager.getCriteriaBuilder().createQuery(tableClass);
		Root<T> table = query.from(tableClass);
		query.select(table);
		return entityManager.createQuery(query).getResultList();
	}

	public static <T extends MyPersistence> List<T> getAll(Class<T> tableClass, int start, int limit) {
		EntityManager entityManager = ServerStorage.ENTITY_MANAGER_FACTORY.createEntityManager();
		CriteriaQuery<T> query = entityManager.getCriteriaBuilder().createQuery(tableClass);
		Root<T> table = query.from(tableClass);
		query.select(table);
		return entityManager.createQuery(query).setFirstResult(start).setMaxResults(limit).getResultList();
	}
	
}
