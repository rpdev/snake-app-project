package se.chalmers.snake.gameengine;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import se.chalmers.snake.interfaces.util.REPoint;
import se.chalmers.snake.interfaces.util.XYPoint;
import static org.junit.Assert.*;

/**
 *
 * @author Figaro
 */
public class PlayerBodyTest {

	public PlayerBodyTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Test
	public void testConstructor() {
		System.out.println("== testConstructor ==");
		PlayerBody pb = new PlayerBody(new XYPoint(100, 100), new XYPoint(50, 50), 0, 5, 5, 0);
		for (REPoint rp : pb) {
			System.out.println(rp);
		}
	}

	@Test
	public void testStep() {
		System.out.println("== testStep ==");
		PlayerBody pb = new PlayerBody(new XYPoint(100, 100), new XYPoint(50, 50), 0, 5, 5, 0);
		for (int i = 0; i < 4; i++) {

			System.out.println(" -- STEP --");
			pb.step(Math.PI / 2, 5);
			for (REPoint rp : pb) {
				System.out.println(rp);
			}
		}
	}

	@Test
	public void testAddBodySegFixStep() {
		System.out.println("== testAddBodySegFixStep ==");
		PlayerBody pb = new PlayerBody(new XYPoint(100, 100), new XYPoint(50, 50), 0, 5, 5, 4);
		for (int i = 0; i < 5; i++) {
			System.out.println(" -- STEP --");
			pb.step(Math.PI / 4.0);
			for (REPoint rp : pb) {
				System.out.println(rp);
			}
		}
	}

	@Test
	public void testAddBodySeg() {
		System.out.println("== testAddBodySeg ==");
		PlayerBody pb = new PlayerBody(new XYPoint(100, 100), new XYPoint(50, 50), 0, 5, 5, 1);
		for (int i = 0; i < 5; i++) {
			System.out.println(" -- STEP --");
			pb.step(0.0, 3);
			System.out.println(pb);
		}
	}
}
