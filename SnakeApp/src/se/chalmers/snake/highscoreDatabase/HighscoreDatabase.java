package se.chalmers.snake.highscoreDatabase;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;

import se.chalmers.snake.interfaces.HighscoreDatabaseIC;
import se.chalmers.snake.mastercontroller.ControlResources;
import se.chalmers.snake.util.Storage;

public final class HighscoreDatabase implements HighscoreDatabaseIC, Serializable {

	private static final int SHOW_COUNT = 10;
	private static final long serialVersionUID = 995427075433374393L;
	private final LinkedList<Highscore> highscoreList;

	public HighscoreDatabase() {

		// Sort by higest point first.
		this.highscoreList = new LinkedList<Highscore>();
		for (int i = 0; i < SHOW_COUNT; i++) {
			this.addPlayerToHighscore("[Empty]", 0);
		}
	}

	@Override
	public boolean checkIfEnoughPoints(int points) {
		if (points <= 0) {
			return false;
		}
		if (highscoreList.isEmpty()) {
			return true;
		}

		return points >= this.highscoreList.getLast().getPoints();
	}

	@Override
	public boolean addPlayerToHighscore(String playerName, int points) {
		this.controlListIntegrity();
		if (checkIfEnoughPoints(points)) {
			this.highscoreList.add(new Highscore(playerName, points));
			Collections.sort(this.highscoreList);
			if (this.highscoreList.size() > SHOW_COUNT) {
				this.highscoreList.remove(this.highscoreList.getLast());
			}

			return true;
		}
		return false;
	}

	/**
	 * This method will ensure the list has "SHOW_COUNT" elements in it and fix as necessary.
	 * Fixes a bug where the list only accepted one element.
	 */
	private void controlListIntegrity() {
		if (this.highscoreList.size() == SHOW_COUNT) {
			return;
		}
		int itemsLeft = SHOW_COUNT - this.highscoreList.size();
		for (int i = 0; i < itemsLeft; i++) {
			this.highscoreList.add(new Highscore("[Empty]", 0));
		}
	}

	@Override
	public boolean addPlayerToHighscore(String playerName, int points,
			  String level) {
		this.addPlayerToHighscore(playerName, points);
		return false;
	}

	@Override
	public String toString() {
		this.controlListIntegrity();

		StringBuilder outText = new StringBuilder("Highscore:");
		for (Highscore highscore : this.highscoreList) {
			outText.append("\n").append(highscore.getPlayerName()).append(" - ").append(highscore.getPoints());
		}
		return outText.toString();
	}

	public boolean saveHighscore() {
		this.controlListIntegrity(); //Might not be needed here but just in case.
		Storage storage = ControlResources.get().getStorage();
		storage.storeObject("highscore", this);
		if (storage.getObject("highscore") == null) {
			return false;
		}
		return true;
	}
}
