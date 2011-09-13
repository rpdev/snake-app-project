package se.chalmers.snake.highscoreDatabase;

public class Highscore {
	
	private String playerName;
	private int points;
	
	public Highscore(String playerName, int points) {
		this.setPlayerName(playerName);
		this.setPoints(points);
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

}
