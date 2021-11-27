package entity;

import java.awt.Color;
import java.io.Serializable;
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
public class Ship extends Entity implements Serializable {
	/** Time between shots. */
	private static final int SHOOTING_INTERVAL = 750;
	/** Speed of the bullets shot by the ship. */
	private static int BULLET_SPEED = -6;
	/** Movement of the ship for each unit of time. */
	private static final int SPEED = 2;
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

		//슈팅 쿨타임을 설정.
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
	//Colldown이 슈팅쿨타운을 조절하는것을 알수있음.
	public final boolean shoot(final Set<Bullet> bullets) {
		if (this.shootingCooldown.checkFinished()) {
			this.shootingCooldown.reset();
			bullets.add(BulletPool.getBullet(positionX + this.width / 2,
					positionY, 0,BULLET_SPEED));
			return true;
		}
		return false;
	}
	//폭탄발사부분
	public final boolean boomShoot(final Set<Boom> booms ) {
		if (this.shootingCooldown.checkFinished()) {
			this.shootingCooldown.reset();
			booms.add(BoomPool.getBoom(positionX + this.width / 2,
					positionY, 0, BULLET_SPEED));
			return true;
		}
		return false;
	}
	
	//폭탄스킬발사
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
	//추가한부분: 총알 쿨타움조절함수
	public void setShootingCooldown(int interval){
		this.shootingCooldown = Core.getCooldown(interval);
	}
	//추가한부분: 총알 속도조절
	public void setBulletSpeed(int speed) {this.BULLET_SPEED = speed;}


	/**
	 * Setter for the ship's spriteType.
	 * @param type represents design
	 */
	public void setShipType(SpriteType type){ this.shipType = type; }
}
