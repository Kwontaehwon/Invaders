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
		if (!pool.isEmpty()) {
			bullet = pool.iterator().next(); // 객체로 Set의 모든 아이템을 순회(for loop 같은느낌)
			pool.remove(bullet); //있으면 삭제. recycle을 통해 업데이트된 총알삭제하는듯.
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
	 * Adds one or more bullets to the list of available ones.
	 *
	 * @param bullet
	 *            Bullets to recycle.
	 */
	public static void recycle(final Set<Bullet> bullet) {
		pool.addAll(bullet);
	}
}
