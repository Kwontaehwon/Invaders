package engine;

import java.io.Serializable;

/**
 * Implements an object that stores the state of the game between levels.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class GameState implements Serializable {

	/** Current game level. */
	private int level;
	/** Current score. */
	private int score;
	/** Lives currently remaining. */
	private int livesRemaining;
	/** Bullets shot until now. */
	private int bulletsShot;
	/** Ships destroyed until now. */
	private int shipsDestroyed;
	/** Save bomb time */
	private int boomTimes;
	/** Save skill cool time */
	private int[] skillCool ;
	/** Save ultimate skill time */
	private int ultimateTimes ;
	/**
	 * Constructor.
	 * 
	 * @param level
	 *            Current game level.
	 * @param score
	 *            Current score.
	 * @param livesRemaining
	 *            Lives currently remaining.
	 * @param bulletsShot
	 *            Bullets shot until now.
	 * @param shipsDestroyed
	 *            Ships destroyed until now.
	 * @param boomTimes
	 * 			  Ships had until now.
	 * @param skillCool
	 * 			  Ships had until now.
	 * @param ultimateTimes
	 *            Ships had until now
	 */
	public GameState(final int level, final int score,
			final int livesRemaining, final int bulletsShot,
			final int shipsDestroyed,final int boomTimes,final int[] skillCool,final int ultimateTimes) {
		this.level = level;
		this.score = score;
		this.livesRemaining = livesRemaining;
		this.bulletsShot = bulletsShot;
		this.shipsDestroyed = shipsDestroyed;
		this.boomTimes = boomTimes;
		this.skillCool = skillCool;
		this.ultimateTimes = ultimateTimes;
	}

	/**
	 * @return the level
	 */
	public final int getLevel() {
		return level;
	}

	/**
	 * @return the score
	 */
	public final int getScore() {
		return score;
	}

	/**
	 * @return the livesRemaining
	 */
	public final int getLivesRemaining() {
		return livesRemaining;
	}

	/**
	 * @return the bulletsShot
	 */
	public final int getBulletsShot() {
		return bulletsShot;
	}

	/**
	 * @return the shipsDestroyed
	 */
	public final int getShipsDestroyed() {
		return shipsDestroyed;
	}
	/**
	 * @return the boomTimes
	 */
	public final int getBoomTimes() { return boomTimes;}
	/**
	 * @return the skillCool
	 */
	public final int[] getSkillCool() { return skillCool;}

	/**
	 * @return UltimateTimes
	 */
	public final int getUltimateTimes() { return ultimateTimes;}
	/**
	 * Setter the score, livesRemaining, bulletsShot, shipsDestroyed
	 * @param score
	 * @param livesRemaining
	 * @param bulletsShot
	 * @param shipsDestroyed
	 */
	public void setState(int score, int livesRemaining, int bulletsShot, int shipsDestroyed){
		this.score = score;
		this.livesRemaining = livesRemaining;
		this.bulletsShot = bulletsShot;
		this.shipsDestroyed = shipsDestroyed;
	}

}
