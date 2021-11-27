package entity;

import java.awt.Color;
import java.io.Serializable;

import engine.DrawManager.SpriteType;

/**
 * Implements a bullet that moves vertically up or down.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class Bullet extends Entity implements Serializable {

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
	 * @param speed
	 *            Speed of the bullet, positive or negative depending on
	 *            direction - positive is down.
	 */
	public Bullet(final int positionX, final int positionY, final int speed) {
		super(positionX, positionY, 3 * 2, 5 * 2, Color.WHITE);

		this.speedX = 0;
		this.speedY = speed;
		setSprite();
	}

	public Bullet(final int positionX, final int positionY, final int speedX, final int speedY) {
		super(positionX, positionY, 3*2, 5*2, Color.WHITE);

		this.speedX = speedX;
		this.speedY = speedY;
		setSprite();
	}

	/**
	 * Sets correct sprite for the bullet, based on speed.
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
			this.positionX += speedX/speedY;
			this.positionY += 1;
		}
		else {
			this.positionY += this.speedY;
			this.positionX += this.speedX;
		}

	}

	/**
	 * Setter of the speed of the bullet.
	 * 
	 * @param speedY
	 *            New speed of the bullet.
	 */
	public final void setSpeedY(final int speedY) {
		this.speedY = speedY;
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
	 * Getter for the speed of the bullet.
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
