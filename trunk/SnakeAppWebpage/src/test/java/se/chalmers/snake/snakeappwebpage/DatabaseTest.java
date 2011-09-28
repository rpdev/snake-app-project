/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.snake.snakeappwebpage;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import se.chalmers.snake.snakeappwebpage.serverstorage.UserAcc;
import static org.junit.Assert.*;

/**
 *
 * @author linux
 */
public class DatabaseTest {

    private static EntityManagerFactory emf;
    private static EntityManager em;

    public DatabaseTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
	emf = Persistence.createEntityManagerFactory("snakeappweb_pu_test");
	em = emf.createEntityManager();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
	em.close();
	emf.close();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

    @Test
    public void userTest(){
	UserAcc u = new UserAcc("Test!", "pass", "s@s.se");
	em.getTransaction().begin();
	em.merge(u);
	em.getTransaction().commit();
    }
}
