package entity;

import engine.DrawManager.SpriteType;

import java.awt.*;

/**
 * Implements a bullet that moves vertically up or down.
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
		super(positionX, positionY, 3 * 2, 5 * 2, Color.WHITE);
		this.speedX = speedX;
		this.speedY = speedY;
		setSprite();
	}

	/**
	 * Sets correct sprite for the bullet, based on speedY.
	 */
	public final void setSprite() {
		if (speedY < 0)
			this.spriteType = SpriteType.Bullet;
		else
			this.spriteType = SpriteType.EnemyBullet;
	}

	/**
	 * Updates the bullet's position.
	 */
	public final void update(boolean skill3) {
		if(skill3 && this.speedY > 0){
			this.positionY += 1;
		}
		else {
			this.positionX += this.speedX;
			this.positionY += this.speedY;
		}

	}

	/**
	 * Setter of the speedY of the bullet.
	 *
	 * @param speed
	 *            New speed of the bullet.
	 */

	public final void setXSpeed(final int speed) {
		this.speedX = speed;
	}

	public final void setYSpeed(final int speed) {
		this.speedY = speed;
	}

	/**
	 * Getter for the speedY of the bullet.
	 *
	 * @return Speed of the bullet.
	 */
	public final int getSpeedX() {
		return this.speedX;
	}

	public final int getSpeedY() {
		return this.speedY;
	}
}