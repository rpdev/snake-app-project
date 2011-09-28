/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.snake.snakeappwebpage.serverstorage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

class Database {

    private static final String DATABASE_NAME = "";
    private static Database instance;
    private final EntityManagerFactory emf;
    private final EntityManager em;

    public enum STATUS{CREATED, PUBLICHED}

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

    static Database getInstance() {
	if (instance == null) {
	    instance = new Database();
	}
	return instance;
    }

     <T> T getEntity(Class<T> type, Long id) {
	return em.find(type, id);
    }

    EntityTransaction getTransaction() {
	return em.getTransaction();
    }

    synchronized void closeDatabase() {
	em.close();
	emf.close();
	instance = null;
    }
}
