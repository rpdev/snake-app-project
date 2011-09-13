package se.chalmers.snake.highscoreDatabase;

import java.util.Comparator;

public class HighscoreComparator implements Comparator<Highscore>{

	@Override
	public int compare(Highscore object1, Highscore object2) {

		return object1.getPoints() - object2.getPoints();
	}

}
