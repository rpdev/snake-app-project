package se.chalmers.snake.snakeappwebpage.serverstorage;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 *
 */
public abstract class MyPersistence implements Serializable {

	public MyPersistence() {
	}

	protected abstract void trackPersistence(EntityManager entityManager);

	protected abstract boolean trackDestroy(EntityManager entityManager, MyPersistence removeObj);

	
	
	
	public boolean persistence(ServerStorage serverStorage) {
		EntityManager entityManager = ServerStorage.ENTITY_MANAGER_FACTORY.createEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();

		if (entityTransaction == null) {
			throw new NullPointerException("Invalid handling of MyPersistence");
		}
		try {
			entityTransaction.begin();
			this.trackPersistence(entityManager);
			entityTransaction.commit();
			return true;
		} catch (Exception ex) {
			entityTransaction.rollback();
			ex.printStackTrace();
		}
		return false;
	}

	public boolean destroy(MyPersistence removeObj) {
		EntityManager entityManager = ServerStorage.ENTITY_MANAGER_FACTORY.createEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();


		if (entityTransaction == null) {
			throw new NullPointerException("Invalid handling of MyPersistence");
		}
		try {
			entityTransaction.begin();
			this.trackDestroy(entityManager, removeObj);
			if (removeObj != this) {
				this.trackPersistence(entityManager);
			}
			entityTransaction.commit();
			return true;
		} catch (Exception ex) {
			entityTransaction.rollback();
			ex.printStackTrace();
		}
		return false;
	}
}
