/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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

    public void removeEnity(Class<?> type, Long... id) {
        if (type == UserAcc.class) {
            removeUser(id);
        } else if (type == SnakeMap.class) {
            removeMap(id);
        } else if (type == Comment.class) {
            removeComment(id);
        } else 
            throw new UnsupportedOperationException(type.getSimpleName() + " is an invalid type");
    }
    
    private void removeComment(Long... id){
        em.getTransaction().begin();
        for(Long i : id){
            Comment c = em.find(Comment.class, i);
            c.cleanUp();
            mergeObject(c.getMap());
            mergeObject(c.getUserCommentViolate());
            em.remove(c);
        }
        em.getTransaction().commit();
    }
    
    private void removeMap(Long... id){
        for(Long i : id){
            SnakeMap sm = em.find(SnakeMap.class, i);
            List<Long> comment_ids = new ArrayList<Long>();
            for(Comment c : sm.getComments())
                comment_ids.add(c.getId());
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
            for (Comment c : ua.getUserComments())
                comment_ids.add(c.getId());
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
