package se.chalmers.snake.highscoreDatabase;

import java.io.Serializable;
import java.util.TreeSet;
import se.chalmers.snake.interfaces.HighscoreDatabaseIC;

public final class HighscoreDatabase implements HighscoreDatabaseIC, Serializable {

	private static final long serialVersionUID = 995427075433374394L;
	private final TreeSet<Highscore> highscoreList;

	public HighscoreDatabase() {
		this.highscoreList = new TreeSet<Highscore>(new HighscoreComparator());
		for (int i = 0; i < 5; i++) {
			this.addPlayerToHighscore("[Empty]", i);
		}
	}

	public boolean checkIfEnoughPoints(int points) {
		if (highscoreList.isEmpty()) {
			return true;
		}
		return points > highscoreList.first().getPoints();
	}

	@Override
	public boolean addPlayerToHighscore(String playerName, int points) {
		if (checkIfEnoughPoints(points)) {
			highscoreList.add(new Highscore(playerName, points));
			if (highscoreList.size() >= 10) {
				highscoreList.remove(highscoreList.first());
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean addPlayerToHighscore(String playerName, int points,
			  String level) {
		this.addPlayerToHighscore(playerName, points);
		return false;
	}

	@Override
	public String toString() {

		String text = "Highscore:";

		for (Highscore highscore : highscoreList) {
			text += "\n" + highscore.getPlayerName() + "\t\t\t\t\t\t" + highscore.getPoints();
		}

		return text;
	}
}
