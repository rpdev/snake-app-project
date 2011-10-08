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
import se.chalmers.snake.snakeappwebpage.serverstorage.Comment;
import se.chalmers.snake.snakeappwebpage.serverstorage.Database;
import se.chalmers.snake.snakeappwebpage.serverstorage.SnakeMap;
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
    public void addRemoveUserTest() {
        UserAcc u = new UserAcc("Test!", "pass", "s@s.se");
        Database.getInstance().mergeObject(u);
        for (UserAcc c : Database.getInstance().getEntityList(UserAcc.class)) {
            Database.getInstance().removeEnity(UserAcc.class, c.getId());
        }
    }

    @Test
    public void addRemoveMapAndUser() {
        UserAcc u = new UserAcc("addRemoveMapAndUser", "pass", "s@s.se");
        Database.getInstance().mergeObject(u);
        SnakeMap m = new SnakeMap(u);
        u.addMap(m);
        Database.getInstance().mergeObject(u);
        Database.getInstance().mergeObject(m);
        Database.getInstance().removeEnity(SnakeMap.class, m.getId());
        Database.getInstance().removeEnity(UserAcc.class, u.getId());
    }

    @Test
    public void addRemoveCommentMapAndUser() {
        UserAcc u = new UserAcc("addRemoveMapAndUser", "pass", "s@s.se");
        Database.getInstance().mergeObject(u);
        SnakeMap m = new SnakeMap(u);
        u.addMap(m);
        Comment c = new Comment("A comment", m, u); // adding itself to map and user
        // user --> map --> comment
        Database.getInstance().mergeObject(u);
        Database.getInstance().mergeObject(m);
        Database.getInstance().mergeObject(c);
        // comment --> map --> user
        //Database.getInstance().removeEnity(c.getClass(), c.getId());
        Database.getInstance().removeEnity(m.getClass(), m.getId());
        Database.getInstance().removeEnity(u.getClass(), u.getId());        
    }
}
