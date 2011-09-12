/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.snake.gameengine;

import java.util.Date;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


public class OscillatorTest {
	private long diff = 0;
	public OscillatorTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Test
	public void testInteval() {
		this.diff = System.nanoTime();
		final long time = 1500;
		final long timeinNano = time*1000000;
		
		System.out.println("== test"+time+"Inteval ==");
		Oscillator oc = new Oscillator((int)time, new Runnable() {
						  @Override
						  public void run() {
							  long cc = System.nanoTime();
							  
							  System.out.println(" -- test"+time+"Inteval "+(cc-OscillatorTest.this.diff-timeinNano));
							  OscillatorTest.this.diff = cc;
						  }
					  });
		oc.start();
		try {
			Thread.sleep(10000);
		} catch(Exception ex) {}
		oc.stop();	
	}
	
	@Test
	public void testStopAndRun() {
		this.diff = System.nanoTime();
		final long time = 500;
		final long timeinNano = time*1000000;
		
		System.out.println("== testStopAndRun ==");
		Oscillator oc = new Oscillator((int)time, new Runnable() {
						  @Override
						  public void run() {
							  long cc = System.nanoTime();
							  
							  System.out.println(" -- testStopAndRun "+(cc-OscillatorTest.this.diff-timeinNano));
							  OscillatorTest.this.diff = cc;
						  }
					  });

		oc.start();
		try {
			Thread.sleep(2000);
		} catch(Exception ex) {}
		oc.stop();	
		try {
			Thread.sleep(2000);
		} catch(Exception ex) {}
		oc.start();
		try {
			Thread.sleep(2000);
		} catch(Exception ex) {}
		oc.stop();
	}
}
