package se.chalmers.snake.test;

import se.chalmers.snake.R;
import se.chalmers.snake.StartActivity;
import se.chalmers.snake.highscoreDatabase.HighscoreDatabase;
import se.chalmers.snake.mastercontroller.ControlResources;
import android.test.ActivityInstrumentationTestCase2;

public class HighscoreDatabaseTest extends ActivityInstrumentationTestCase2<StartActivity> {
	private static final String u1 = "UserA", u2 = "User2", l2 = "10";
	private static final int i1 = 100, i2 = 200;
	
	public HighscoreDatabaseTest(String pkg, Class<StartActivity> activityClass) {
		super(pkg, activityClass);
	}

	public HighscoreDatabaseTest() { // fixed 'Expected 1 tests, received 0', android's documentation well is ...
		super("se.chalmers.snake", StartActivity.class);
	}

	public void testAdd() {
		HighscoreDatabase db = new HighscoreDatabase();
		db.addPlayerToHighscore(u1, i1);
		db.addPlayerToHighscore(u2, i2, l2);
		assertTrue(db.toString().contains(u1));
	}

	public void testFailAdd() {
		HighscoreDatabase db = new HighscoreDatabase();
		for (int i = 0; i < 100; i++) {
			db.addPlayerToHighscore(Integer.toHexString(i),	(int) (Math.random() + 0.1d * i));
		}
		assertFalse(db.checkIfEnoughPoints(0));
	}

	public void testFetch() {
		HighscoreDatabase db = new HighscoreDatabase();
		StringBuilder sb = new StringBuilder("Highscore:");
		for (int i = 0; i < 10; i++) {
			db.addPlayerToHighscore("user" + Integer.toString(i), 10 - i);
			sb.append("\nuser" + Integer.toString(i) + " - " + (10 - i));
		}
		assertTrue(db.toString().equalsIgnoreCase(sb.toString()));
	}

	public void testStore() {
		ControlResources.make(getActivity(), R.id.spelplan);
		HighscoreDatabase db = new HighscoreDatabase();
		for (int i = 0; i < 11; i++) {
			db.addPlayerToHighscore("user" + Integer.toString(i), 11 - i);
		}
		db.saveHighscore();
		HighscoreDatabase hdb = ControlResources.get().getStorage().getObject("highscore");
		assertNotNull(hdb);
		//assertEquals(db, hdb); // won't work
		assertTrue(db.toString().equalsIgnoreCase(hdb.toString()));
	}
}