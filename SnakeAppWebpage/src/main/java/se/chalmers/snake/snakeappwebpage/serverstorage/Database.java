/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.snake.snakeappwebpage.serverstorage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

public class Database {

	private static final String DATABASE_NAME = "snakeappweb_pu";
	private static Database instance;
	private final EntityManagerFactory emf;
	private final EntityManager em;

	public enum STATUS {

		CREATED, PUBLICHED
	}

	private Database() {
		emf = Persistence.createEntityManagerFactory(DATABASE_NAME);
		em = emf.createEntityManager();
	}

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

	public static Database getInstance() {
		if (instance == null) {
			instance = new Database();
		}
		return instance;
	}

	public synchronized <T> T getEntity(Class<T> type, Long id) {
		return em.find(type, id);
	}

	public synchronized <T> void removeEnity(Class<T> type, Long... id) {
		em.getTransaction().begin();
		for (Long i : id) {

			em.remove(em.find(type, i));
		}
		em.getTransaction().commit();
	}

	public synchronized <T> List<T> getEntityList(Class<T> type) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = builder.createQuery(type);
		TypedQuery<T> query = em.createQuery(cq);
		return query.getResultList();
	}

	public <T> void mergeObject(T object) {
		em.getTransaction().begin();
		if (em.contains(object)) {
			em.refresh(object);
		} else {
			em.persist(object);
		}
		em.getTransaction().commit();
	}

	synchronized void closeDatabase() {
		em.close();
		emf.close();
		instance = null;
	}
}
