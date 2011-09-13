package se.chalmers.snake.highscoreDatabase;

import java.util.TreeSet;
import se.chalmers.snake.interfaces.HighscoreDatabaseIC;

public class HighscoreDatabase implements HighscoreDatabaseIC{
	private TreeSet<Highscore> highscoreList;
	
	public HighscoreDatabase(){
		this.highscoreList = new TreeSet<Highscore>(new HighscoreComparator());
	}

	@Override
	public boolean addPlayerToHighscore(String playerName, int points) {
		// TODO Auto-generated method stub
		return false;
	}
}
