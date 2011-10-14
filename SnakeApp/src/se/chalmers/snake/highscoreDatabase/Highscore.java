package se.chalmers.snake.highscoreDatabase;

import java.io.Serializable;

final class Highscore implements Serializable, Comparable<Highscore> {

	private static final long serialVersionUID = 4611406553461997259L;
	private String playerName;
	private int points;
	private String level;

	public Highscore(String playerName, int points) {
		this.setPlayerName(playerName);
		this.setPoints(points);
	}

	public Highscore(String playerName, int points, String level) {
		this.setPlayerName(playerName);
		this.setPoints(points);
		this.setLevel(level);
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getPoints() {
		return points;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getLevel() {
		return level;
	}

	public int compareTo(Highscore t) {
		return t.points - this.points;
	}
}
