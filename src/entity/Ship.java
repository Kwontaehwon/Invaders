package entity;

import java.awt.Color;
import java.util.Set;

import engine.Cooldown;
import engine.Core;
import engine.DrawManager.SpriteType;

/**
 * Implements a ship, to be controlled by the player.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class Ship extends Entity {
	/** Time between shots. */
	private static int SHOOTING_INTERVAL = 750;
	/** Speed of the bullets shot by the ship. */
	private static int BULLET_SPEED = -6;
	/** Movement of the ship for each unit of time. */
	private static final int SPEED = 2;
	/** Default SpriteType */
	private SpriteType shipType;

	/** Minimum time between shots. */
	private Cooldown shootingCooldown;
	/** Time spent inactive between hits. */
	private Cooldown destructionCooldown;

	/**
	 * Constructor, establishes the ship's properties.
	 * 
	 * @param positionX
	 *            Initial position of the ship in the X axis.
	 * @param positionY
	 *            Initial position of the ship in the Y axis.
	 */
	public Ship(final int positionX, final int positionY, SpriteType spriteType) {
		super(positionX, positionY, 13 * 2, 8 * 2, Color.GREEN);

		this.spriteType = spriteType;
		this.shipType = spriteType;
		this.shootingCooldown = Core.getCooldown(SHOOTING_INTERVAL);
		this.destructionCooldown = Core.getCooldown(1000);
	}

	/**
	 * Moves the ship speed uni ts right, or until the right screen border is
	 * reached.
	 */
	public final void moveRight() {
		this.positionX += SPEED;
	}

	/**
	 * Moves the ship speed units left, or until the left screen border is
	 * reached.
	 */
	public final void moveLeft() {
		this.positionX -= SPEED;
	}

	/**
	 * Shoots a bullet upwards.
	 * 
	 * @param bullets
	 *            List of bullets on screen, to add the new bullet.
	 * @return Checks if the bullet was shot correctly.
	 */
	public final boolean shoot(final Set<Bullet> bullets) {
		if (this.shootingCooldown.checkFinished()) {
			this.shootingCooldown.reset();
			bullets.add(BulletPool.getBullet(positionX + this.width / 2,
					positionY, 0, BULLET_SPEED));
			return true;
		}
		return false;
	}

	/**
	 * Shoots a bomb upwards.
	 *
	 * @param booms
	 *            List of bullets on screen, to add the new bomb.
	 * @return Checks if the bullet was shot correctly.
	 */
	public final boolean boomShoot(final Set<Boom> booms ) {
		if (this.shootingCooldown.checkFinished()) {
			this.shootingCooldown.reset();
			booms.add(BoomPool.getBoom(positionX + this.width / 2,
					positionY, 0, BULLET_SPEED));
			return true;
		}
		return false;
	}

	/**
	 * Shoots a bombSkill upwards.
	 *
	 * @param booms
	 *            List of bullets on screen, to add the new bombSkill.
	 * @return Checks if the bullet was shot correctly.
	 */
	public final void boomSkillShoot(final Set<Boom> booms, int speedX ){
		booms.add(BoomPool.getBoom(positionX + this.width / 2  ,
				positionY, speedX,BULLET_SPEED));
	}

	/**
	 * Updates status of the ship.
	 */
	public final void update() {
		if (!this.destructionCooldown.checkFinished())
			this.spriteType = SpriteType.ShipDestroyed;
		else
			this.spriteType = shipType;
	}

	/**
	 * Switches the ship to its destroyed state.
	 */
	public final void destroy() {
		this.destructionCooldown.reset();
	}

	/**
	 * Checks if the ship is destroyed.
	 * 
	 * @return True if the ship is currently destroyed.
	 */
	public final boolean isDestroyed() {
		return !this.destructionCooldown.checkFinished();
	}

	/**
	 * Getter for the ship's speed.
	 * 
	 * @return Speed of the ship.
	 */
	public final int getSpeed() {
		return SPEED;
	}

	/**
	 * Getter for the bullet's speed.
	 * @return Speed of the bullet.
	 */
	public int getBulletSpeed(){
		return this.BULLET_SPEED;
	}

	/**
	 * Setter for the bullet's speed.
	 * @param speed new speed of the bullet.
	 */
	public void setBulletSpeed(int speed) {this.BULLET_SPEED = speed;}

	/**
	 * Setter for the ship's spriteType.
	 * @param type represents design
	 */
	public void setShipType(SpriteType type){ this.shipType = type; }

	/**
	 * Getter for the shooting cooldown.
	 * @return milliseconds of the cool time.
	 */
	public int getShootingCoolDown(){
		return this.shootingCooldown.getMilliseconds();
	}

	/**
	 * Setter for the shooting cooldown.
	 * @param interval milliseconds of the cool time.
	 */
	public void setShootingCoolDown(int interval) {
		this.shootingCooldown = Core.getCooldown(interval);
		SHOOTING_INTERVAL = interval;
	}
}
