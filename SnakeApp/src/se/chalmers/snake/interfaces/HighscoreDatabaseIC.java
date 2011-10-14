package se.chalmers.snake.interfaces;

public interface HighscoreDatabaseIC {

	/**
	 * Adds the player's name and score to the highscore database
	 * @param playerName
	 * @param points
	 * @return True if it was a new highscore, false if the score isn't high enough
	 */
	public boolean addPlayerToHighscore(String playerName, int points);

	/**
	 * Adds the player's name, score and last level to the highscore database
	 * @param playerName
	 * @param points
	 * @param level
	 * @return True if it was a new highscore, false if the score isn't high enough
	 */
	public boolean addPlayerToHighscore(String playerName, int points, String level);

	/**
	 * Returns the highscoreList as a string
	 * @return
	 */
	@Override
	public String toString();

	public boolean checkIfEnoughPoints(int points);

	public boolean saveHighscore();
}
