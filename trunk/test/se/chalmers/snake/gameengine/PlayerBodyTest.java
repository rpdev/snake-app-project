package se.chalmers.snake.gameengine;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import se.chalmers.snake.interfaces.util.REPoint;
import se.chalmers.snake.interfaces.util.XYPoint;
import static org.junit.Assert.*;


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
		System.out.println(pb);
	}

	@Test
	public void testStep() {
		System.out.println("== testStep ==");
		PlayerBody pb = new PlayerBody(new XYPoint(100, 100), new XYPoint(50, 50), 0, 5, 5, 0);
		for (int i = 0; i < 4; i++) {

			System.out.println(" -- STEP --");
			pb.step(Math.PI / 2, 5);
			System.out.println(pb);
		}
	}

	@Test
	public void testAddStartSeg() {
		System.out.println("== testAddBodySeg ==");
		PlayerBody pb = new PlayerBody(new XYPoint(100, 100), new XYPoint(50, 50), 0, 5, 5, 4);
		for (int i = 0; i < 5; i++) {
			System.out.println(" -- STEP --");
			pb.step(0.0, 3);
			System.out.println(pb);
		}
	}
	
	@Test
	public void testMove() {
		System.out.println("== testMove ==");
		PlayerBody pb = new PlayerBody(new XYPoint(100, 100), new XYPoint(50, 50), 0, 5, 5,0);
		for (int i = 0; i < 5; i++) {
			System.out.println(" -- STEP testMove --");
			pb.step(1.0, 3);
			System.out.println(pb);
		}
	}
	
	
	@Test
	public void testAddSegInGame() {
		System.out.println("== testAddSegInGame ==");
		PlayerBody pb = new PlayerBody(new XYPoint(100, 100), new XYPoint(50, 50), 0, 5, 5,0);
		for (int i = 0; i < 3; i++) {
			System.out.println(" -- STEP testAddSegInGame --");
			pb.step(1.0, 3);
			System.out.println(pb);
		}
		pb.addSeg(1);
		for (int i = 0; i < 3; i++) {
			System.out.println(" -- STEP testAddSegInGame --");
			pb.step(1.0, 3);
			System.out.println(pb);
		}
	}
	
}
