package entity;

import java.util.HashSet;
import java.util.Set;
/**
 * Implements a pool of recyclable bullets.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
// 충돌된에 사용된 총알들을 삭제하고, 다시 recycle하는데 사용되는듯함.
// 그와중에 총알을 만들어주는 함수를 추가해준듯.
public final class BulletPool {

	/** Set of already created bullets. */
	private static Set<Bullet> pool = new HashSet<Bullet>();
	/** Set of already created bossbullets. */
	private static Set<BossBullet> bossPool = new HashSet<BossBullet>();

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
	 * @param speed
	 *            Requested speed of the bullet, positive or negative depending
	 *            on direction - positive is down.
	 * @return Requested bullet.
	 */
	public static Bullet getBullet(final int positionX,
			final int positionY, final int speed) {
		Bullet bullet;
		if (!pool.isEmpty()) {
			bullet = pool.iterator().next(); // 객체로 Set의 모든 아이템을 순회(for loop 같은느낌)
			pool.remove(bullet); //있으면 삭제. recycle을 통해 업데이트된 총알삭제하는듯.
			bullet.setPositionX(positionX - bullet.getWidth() / 2);
			bullet.setPositionY(positionY);
			bullet.setSpeed(speed);
			bullet.setSprite();

		} else {
			bullet = new Bullet(positionX, positionY, speed);
			bullet.setPositionX(positionX - bullet.getWidth() / 2);
		}
		return bullet;
	}

	public static BossBullet getBossBullet(final int positionX,
								   final int positionY, final int speed, int x) {
		BossBullet bossBullet;
		if (!bossPool.isEmpty()) {
			bossBullet = bossPool.iterator().next();
			bossPool.remove(bossBullet);
			bossBullet.setPositionX(positionX - bossBullet.getWidth() / 2);
			bossBullet.setPositionY(positionY);
			bossBullet.setSpeed(speed);
			bossBullet.setSprite();
		} else {
			bossBullet = new BossBullet(positionX, positionY, speed, x);
			bossBullet.setPositionX(positionX - bossBullet.getWidth() / 2);
		}
		return bossBullet;
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
	public static void bossRecycle(final Set<BossBullet> bossBullet) {
		bossPool.addAll(bossBullet);
	}

}
