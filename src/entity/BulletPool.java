package entity;

import java.util.HashSet;
import java.util.Set;

/**
 * Implements a pool of recyclable bullets.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public final class BulletPool {

	/** Set of already created bullets. */
	private static Set<Bullet> pool = new HashSet<Bullet>();

	/**
	 * Constructor, not called.
	 */
	private BulletPool() {

	}

	/**
	 * Returns a bullet from the pool if one is available, a new one if there
	 * isn't.
	 *
	 * @param positionX
	 *            Requested position of the bullet in the X axis.
	 * @param positionY
	 *            Requested position of the bullet in the Y axis.
	 * @param speedX
	 *
	 * @param speedY
	 *
	 * @return Requested bullet.
	 */
	public static Bullet getBullet(final int positionX,
								   final int positionY, final int speedX, final int speedY) {
		Bullet bullet;
		if (!pool.isEmpty()) {					// pool에 총알이 있으면
			bullet = pool.iterator().next(); 	// 총알 하나를 꺼내고
			pool.remove(bullet); 				// pool에서 삭제.
			bullet.setPositionX(positionX - bullet.getWidth() / 2);
			bullet.setPositionY(positionY);
			bullet.setXSpeed(speedX);
			bullet.setYSpeed(speedY);
			bullet.setSprite();
		} else {
			bullet = new Bullet(positionX, positionY, speedX, speedY);
			bullet.setPositionX(positionX - bullet.getWidth() / 2);
		}
		return bullet;
	}

	/**
	 * Returns a bullet from the pool if one is available, a new one if there
	 * isn't.
	 *
	 * @param positionX
	 *            Requested position of the bullet in the X axis.
	 * @param positionY
	 *            Requested position of the bullet in the Y axis.
	 * @param xSpeed
	 *            Requested speed of the bullet, positive or negative depending
	 *            on direction - positive is down.
	 * @param ySpeed
	 * 			  Requested speed of the bullet, positive or negative depending
	 * 	 *            on direction - positive is down.
	 * @return Requested bullet.
	 */
	public static Bullet getBullet(final int positionX, final int positionY,
								   final int xSpeed, final int ySpeed) {
		Bullet bullet;
		if (!pool.isEmpty()) {					// pool에 총알이 있으면
			bullet = pool.iterator().next(); 	// 총알 하나를 꺼내고
			pool.remove(bullet); 				// pool에서 삭제.
			bullet.setPositionX(positionX - bullet.getWidth() / 2);
			bullet.setPositionY(positionY);
			bullet.setSpeed(xSpeed, ySpeed);
			bullet.setSprite();

		} else {
			bullet = new Bullet(positionX, positionY, xSpeed, ySpeed);
			bullet.setPositionX(positionX - bullet.getWidth() / 2);
		}
		return bullet;
	}

	/**
	 * Adds one or more bullets to the list of available ones.
	 *
	 * @param bullet
	 *            Bullets to recycle.
	 */
	public static void recycle(final Set<Bullet> bullet) {
		pool.addAll(bullet);
	}
}
