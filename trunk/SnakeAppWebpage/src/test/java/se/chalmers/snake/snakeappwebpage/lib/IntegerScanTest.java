/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.snake.snakeappwebpage.lib;

import java.util.ArrayList;
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
		ArrayList<Integer> myList = new ArrayList<Integer>();
		myList.add(10);
		myList.add(12);
		myList.add(14);
		IntegerScan it = new IntegerScan(myStr);
		int rows = 0;
		for (List<Integer> list : it) {
			assertEquals("List not equals.", myList, list);
			rows++;
		}
		assertEquals("Read not 1 row.", 1, rows);
	}

	@Test
	public void testComplex() {
		System.out.println("*** START TEST COMPLEX ***");
		String myStr = "{10,12,14}{999,921,8123012,0,12312}";
		ArrayList<List<Integer>> myList = new ArrayList<List<Integer>>();
		myList.add(Arrays.asList(10, 12, 14));
		myList.add(Arrays.asList(999, 921, 8123012, 0, 12312));

		IntegerScan it = new IntegerScan(myStr);
		ArrayList<List<Integer>> outList = new ArrayList<List<Integer>>();
		for (List<Integer> list : it) {
			outList.add(list);
		}
		assertEquals("Lists not equals.", myList, outList);

	}

	@Test
	public void testNull() {
		System.out.println("*** START TEST NULL ***");
		String myStr = "";
		ArrayList<List<Integer>> myList = new ArrayList<List<Integer>>();
		IntegerScan it = new IntegerScan(myStr);
		ArrayList<List<Integer>> outList = new ArrayList<List<Integer>>();
		for (List<Integer> list : it) {
			outList.add(list);
		}
		assertEquals("Lists not equals.", myList, outList);

	}

	@Test
	public void testErrorData() {
		System.out.println("*** START TEST ERROR DATA ***");
		String myStr = "Hello My Name Is Error";
		ArrayList<List<Integer>> myList = new ArrayList<List<Integer>>();
		IntegerScan it = new IntegerScan(myStr);
		ArrayList<List<Integer>> outList = new ArrayList<List<Integer>>();
		for (List<Integer> list : it) {
			outList.add(list);
		}
		assertEquals("Lists not equals.", myList, outList);
	}

	@Test
	public void testErrorData2() {
		System.out.println("*** START TEST ERROR DATA 2 ***");
		String myStr = "{10,12,14}{Error Data}{999,921,8123012,0,12312}";
		ArrayList<List<Integer>> myList = new ArrayList<List<Integer>>();
		myList.add(Arrays.asList(10, 12, 14));
		
		IntegerScan it = new IntegerScan(myStr);
		ArrayList<List<Integer>> outList = new ArrayList<List<Integer>>();
		for (List<Integer> list : it) {
			outList.add(list);
		}
		assertEquals("Lists not equals.", myList, outList);
	}
}
