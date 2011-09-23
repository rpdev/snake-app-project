package se.chalmers.snake.highscoreDatabase;

import java.util.TreeSet;
import se.chalmers.snake.interfaces.HighscoreDatabaseIC;

public class HighscoreDatabase implements HighscoreDatabaseIC{
	private TreeSet<Highscore> highscoreList;
	
	public HighscoreDatabase(){
		this.highscoreList = new TreeSet<Highscore>(new HighscoreComparator());
		for(int i = 0; i < 5; i++){
			addPlayerToHighscore("" + i, i);
		}
	}

	@Override
	public boolean addPlayerToHighscore(String playerName, int points) {
		return highscoreList.add(new Highscore(playerName, points));
	}

	@Override
	public boolean addPlayerToHighscore(String playerName, int points,
			String level) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String toString(){
		
		String text = "Highscore";
		
		for(Highscore highscore : highscoreList){
			text += "\n" + highscore.getPlayerName() + "\t\t\t\t\t\t" + highscore.getPoints();
		}
		
		return text;
	}
}
