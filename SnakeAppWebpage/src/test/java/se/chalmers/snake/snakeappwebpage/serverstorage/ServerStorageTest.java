/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.snake.snakeappwebpage.serverstorage;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author 
 */
public class ServerStorageTest {

	private class KeyPar {

		public String k, v;

		private KeyPar(String k, String v) {
			this.k = k;
			this.v = v;
		}
	}

	public ServerStorageTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Test
	public void SHAHashStringTest() throws Exception {

		List<KeyPar> map = new ArrayList<KeyPar>();
		map.add(new KeyPar("", "2jmj7l5rSw0yVb/vlWAYkK/YBwk="));
		map.add(new KeyPar("hello world", "Kq5sNclPz7QV2+lfQIuc6R7oRu0="));
		map.add(new KeyPar("...", "bq46WwYsbQ158HDCbm1iSGtAy0Y="));
		for (KeyPar keyPar : map) {
			if(!keyPar.v.equals(ServerStorage.SHAHashString(keyPar.k))) {
				throw new Exception("Test fall, "+keyPar.v);
			}
		}
	}
}
