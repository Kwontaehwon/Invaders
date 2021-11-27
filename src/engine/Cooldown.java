package engine;

/**
 * Imposes a cooldown period between two actions.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class Cooldown {

	/** Cooldown duration. */
	private int milliseconds;
	/** Maximum difference between durations. */
	private int variance;
	/** Duration of this run, varies between runs if variance > 0. */
	private int duration;
	/** Beginning time. */
	private long time;
	/**
	 * Constructor, established the time until the action can be performed
	 * again.
	 *
	 * @param milliseconds
	 *            Time until cooldown period is finished.
	 */
	protected Cooldown(final int milliseconds) {
		this.milliseconds = milliseconds;
		this.variance = 0;
		this.duration = milliseconds;
		this.time = 0;
	}

	/**
	 * Constructor, established the time until the action can be performed
	 * again, with a variation of +/- variance.
	 *
	 * @param milliseconds
	 *            Time until cooldown period is finished.
	 * @param variance
	 *            Variance in the cooldown period.
	 */
	protected Cooldown(final int milliseconds, final int variance) {
		this.milliseconds = milliseconds;
		this.variance = variance;
		this.time = 0;
	}

	/**
	 * Checks if the cooldown is finished.
	 *
	 * @return Cooldown state.
	 */
	public final boolean checkFinished() {
		if ((this.time == 0)
				|| this.time + this.duration < System.currentTimeMillis())
			return true;
		return false;
	}

	public int passedCooldown(){
		return (int)(System.currentTimeMillis() - this.time)/1000;
	}

	//pause한 시간만큼 시간을 시작시간에 더해줌.
	public void pause(long pauseTime){
		this.time += pauseTime;
	}
	/**
	 * Restarts the cooldown.
	 */
	public final void reset() {
		this.time = System.currentTimeMillis();
		if (this.variance != 0)
			this.duration = (this.milliseconds - this.variance)
					+ (int) (Math.random()
					* (this.milliseconds + this.variance));
	}

	public int getDuration(){
		return this.duration/1000;
	}

	public int getMilliseconds() { return milliseconds; }
}

