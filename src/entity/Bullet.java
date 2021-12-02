package entity;

import engine.DrawManager.SpriteType;

import java.awt.*;

/**
 * Implements a bullet that moves with fixed speed.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public class Bullet extends Entity {

	/**
	 * Speed of the bullet, positive or negative depending on direction -
	 * positive is down.
	 */
	private int speedX;
	private int speedY;

	/**
	 * Constructor, establishes the bullet's properties.
	 *
	 * @param positionX
	 *            Initial position of the bullet in the X axis.
	 * @param positionY
	 *            Initial position of the bullet in the Y axis.
	 * @param speedY
	 *            Speed of the bullet, positive or negative depending on
	 *            direction - positive is down.
	 */

	public Bullet(final int positionX, final int positionY, final int speedX, final int speedY) {
		super(positionX, positionY, 3*2, 5*2, Color.WHITE);
		this.speedX = speedX;
		this.speedY = speedY;
		setSprite();
	}

	/**
	 * Sets correct sprite for the bullet, based on speedY.
	 */
	public final void setSprite() {
		int bullet1Speed1 = -6;
		int bullet1Speed2 = -7;
		int bullet2Speed = -8;
		if (speedY > 0)	this.spriteType = SpriteType.EnemyBullet;
		else if(speedY == bullet1Speed1 || speedY == bullet1Speed2) this.spriteType = SpriteType.Bullet1;
		else if(speedY == bullet2Speed) this.spriteType = SpriteType.Bullet2;
		else this.spriteType = SpriteType.Bullet3;
	}

	/**
	 * Updates the bullet's position.
	 */
	public final void update(boolean skill3) {
		if(skill3 && this.speedY > 0){
			this.positionX += speedX/speedY;
			this.positionY += 1;
		}
		else {
			this.positionY += this.speedY;
			this.positionX += this.speedX;
		}
	}

	/**
	 * Setter of the speedY of the bullet.
	 *
	 * @param speed
	 *            New speed of the bullet.
	 */

	public final void setSpeed(final int speed) {
		this.speedY = speed;
	}

	/**
	 * Setter of the speeds of the bullet.
	 *
	 * @param speedX
	 * 				New speedX of the bullet.
	 * @param speedY
	 * 				New speedY of the bullet.
	 */
	public final void setSpeed(final int speedX, final int speedY) {
		this.speedX = speedX;
		this.speedY = speedY;
	}

	/**
	 * Getter for the speedY of the bullet.
	 * 
	 * @return SpeedY of the bullet.
	 */
	public final int getSpeedY() {
		return this.speedY;
	}

	/**
	 * Getter for the speedX of the bullet.
	 *
	 * @return SpeedX of the Bullet
	 */
	public final int getSpeedX() { return this.speedX; }
}
