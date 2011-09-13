package se.chalmers.snake.interfaces;

public interface HighscoreDatabaseIC {
	
	/**
	 * Adds the player's name and score to the highscore database
	 * @param playerName
	 * @param points
	 * @return True if it was a new highscore, false if the score isn't high enough
	 */
	public boolean addPlayerToHighscore(String playerName, int points);

}
