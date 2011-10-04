/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.snake.snakeappwebpage.lib;

import java.util.Arrays;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author 
 */
public class IntegerScanTest {

	public IntegerScanTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Test
	public void testSimple() {
		System.out.println("*** START TEST SIMPLE ***");
		String myStr = "{10,12,14}";
		IntegerScan it = new IntegerScan(myStr);
		int rows=0;
			for (List<Integer> list : it) {
				System.out.println(Arrays.toString(list.toArray()));
				rows++;
			}
		 
			assertEquals("Read not 1 row.",1, rows);



	}
}
