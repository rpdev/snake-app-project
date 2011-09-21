package se.chalmers.snake.snakeappwebpage.serverstorage;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 *
 */
public abstract class SelfPersistence implements Serializable {

	public SelfPersistence() {
	}

	abstract SelfPersistence trackPersistence(EntityManager entityManager);

	abstract boolean trackDestroy(EntityManager entityManager, SelfPersistence removeObj);

	
	
	/**
	 * Presistence this obj to the database, this will include sub objects.
	 * @param serverStorage
	 * @return 
	 */
	public boolean persistence() {
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

	public boolean destroy(SelfPersistence removeObj) {
		EntityManager entityManager = ServerStorage.ENTITY_MANAGER_FACTORY.createEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();


		if (entityTransaction == null) {
			throw new NullPointerException("Invalid handling of MyPersistence");
		}
		try {
			entityTransaction.begin();
			this.trackDestroy(entityManager, removeObj);
			if (removeObj != this) {
				SelfPersistence myPersistence= this.trackPersistence(entityManager);
				myPersistence.destroy(removeObj.trackPersistence(entityManager));
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
