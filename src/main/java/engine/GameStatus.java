package engine;

import java.io.Serializable;

/**
 * Implements a game status record.
 */
public class GameStatus implements Serializable {

	/** Game States. */
	private GameState gameStates;
	/** Game Settings. */
	private GameSettings gameSettings;
	/** Game bonus Life */
	private boolean bonus;

	/**
	 * Constructor.
	 *
	 * @param gameStates
	 * @param gameSettings
	 * @param bonus
	 */
	public GameStatus(final GameState gameStates, final GameSettings gameSettings, final Boolean bonus) {
		this.gameStates = gameStates;
		this.gameSettings = gameSettings;
		this.bonus = bonus;
	}

	/**
	 * Getter for the Game States.
	 * 
	 * @return game states
	 */
	public final GameState getStates() {
		return this.gameStates;
	}

	/**
	 * Getter for the Game Settings.
	 * 
	 * @return game setting
	 */
	public final GameSettings getSettings() {
		return this.gameSettings;
	}

	/**
	 * Getter for the Game bonus life.
	 *
	 * @return game bonus life
	 */
	public final boolean getBonus(){return this.bonus;}

}
