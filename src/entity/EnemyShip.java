package entity;

import java.awt.Color;
import java.io.Serializable;

import engine.Cooldown;
import engine.Core;
import engine.DrawManager.SpriteType;

/**
 * Implements a enemy ship, to be destroyed by the player.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class EnemyShip extends Entity {
	
	/** Point value of a type A enemy. */
	private static final int A_TYPE_POINTS = 10;
	/** Point value of a type B enemy. */
	private static final int B_TYPE_POINTS = 20;
	/** Point value of a type C enemy. */
	private static final int C_TYPE_POINTS = 30;
	/** Point value of a bonus enemy. */
	private static final int BONUS_TYPE_POINTS = 100;
	private static final int D_TYPE_POINTS = 40; // 두번 피격해야 죽는 적 D의 포인트.

	/** Cooldown between sprite changes. */
	private Cooldown animationCooldown;
	/** Checks if the ship has been hit by a bullet. */
	private boolean isDestroyed;
	/** Values of the ship, in points, when destroyed. */
	private int pointValue;

	private int live = 1; // 적의 live 추가 (기본값 1 : 한번 피격하면 죽음)

	/**
	 * Constructor, establishes the ship's properties.
	 * 
	 * @param positionX
	 *            Initial position of the ship in the X axis.
	 * @param positionY
	 *            Initial position of the ship in the Y axis.
	 * @param spriteType
	 *            Sprite type, image corresponding to the ship.
	 */
	public EnemyShip(final int positionX, final int positionY,
			final SpriteType spriteType) {
		super(positionX, positionY, 16 * 2, 16 * 2, Color.WHITE);

		this.spriteType = spriteType;
		this.animationCooldown = Core.getCooldown(500);
		this.isDestroyed = false;

		switch (this.spriteType) {
		case EnemyShipA1:
		case EnemyShipA2:
			this.pointValue = A_TYPE_POINTS;
			break;
		case EnemyShipB1:
		case EnemyShipB2:
			this.pointValue = B_TYPE_POINTS;
			break;
		case EnemyShipC1:
		case EnemyShipC2:
			this.pointValue = C_TYPE_POINTS;
			break;
		case EnemyShipD1:		// 적D 포인트 설정.
		case EnemyShipD2:
			this.pointValue = D_TYPE_POINTS;
			this.live = 2;
			break;
		default:
			this.pointValue = 0;
			break;
		}
	}

	/**
	 * Constructor, establishes the ship's properties for a special ship, with
	 * known starting properties.
	 */
	public EnemyShip() {
		super(-32, 120, 32 * 2, 32 * 2, Color.RED);
		this.spriteType = SpriteType.EnemyShipSpecial1;
		this.animationCooldown = Core.getCooldown(100);;
		this.isDestroyed = false;
		this.pointValue = BONUS_TYPE_POINTS;
	}

	/**
	 * Getter for the score bonus if this ship is destroyed.
	 * 
	 * @return Value of the ship.
	 */
	public final int getPointValue() {
		return this.pointValue;
	}

	/**
	 * Moves the ship the specified distance.
	 * 
	 * @param distanceX
	 *            Distance to move in the X axis.
	 * @param distanceY
	 *            Distance to move in the Y axis.
	 */
	public final void move(final int distanceX, final int distanceY) {
		this.positionX += distanceX;
		this.positionY += distanceY;
	}

	/**
	 * Updates attributes, mainly used for animation purposes.
	 */
	public final void update() {
		if (this.animationCooldown.checkFinished()) {
			this.animationCooldown.reset();

			switch (this.spriteType) {
				case EnemyShipA1:
					this.spriteType = SpriteType.EnemyShipA2;
					break;
				case EnemyShipA2:
					this.spriteType = SpriteType.EnemyShipA1;
					break;
				case EnemyShipB1:
					this.spriteType = SpriteType.EnemyShipB2;
					break;
				case EnemyShipB2:
					this.spriteType = SpriteType.EnemyShipB1;
					break;
				case EnemyShipC1:
					this.spriteType = SpriteType.EnemyShipC2;
					break;
				case EnemyShipC2:
					this.spriteType = SpriteType.EnemyShipC1;
					break;
				// D적 업데이트 스프라이트 추가.
				case EnemyShipD1:
					this.spriteType = SpriteType.EnemyShipD2;
					break;
				case EnemyShipD2:
					this.spriteType = SpriteType.EnemyShipD1;
					break;
				case EnemyShipD3:
					this.spriteType = SpriteType.EnemyShipD4;
					break;
				case EnemyShipD4:
					this.spriteType = SpriteType.EnemyShipD3;
					break;
				case EnemyShipSpecial1:
					this.spriteType = SpriteType.EnemyShipSpecial2;
					break;
				case EnemyShipSpecial2:
					this.spriteType = SpriteType.EnemyShipSpecial3;
					break;
				case EnemyShipSpecial3:
					this.spriteType = SpriteType.EnemyShipSpecial4;
					break;
				case EnemyShipSpecial4:
					this.spriteType = SpriteType.EnemyShipSpecial1;
					break;
				default:
					break;
			}
		}
	}
	/**
	 * Destroys the ship, causing an explosion.
	 */
	public final void destroy() {
		this.live--;
		// 피격시 spriteType 을 바꿔줌
		if(this.spriteType == SpriteType.EnemyShipD1 && this.live==1){
			this.spriteType = SpriteType.EnemyShipD4;
		}
		else if(this.spriteType == SpriteType.EnemyShipD2 && this.live==1){
			this.spriteType = SpriteType.EnemyShipD3;
		}

		if(this.live<=0){		// live가 0 이하면 isDestroyed 를 바꿈.
			this.isDestroyed = true;
			this.spriteType = SpriteType.Explosion3;
		}
	}

	/**
	 * Checks if the ship has been destroyed.
	 * 
	 * @return True if the ship has been destroyed.
	 */
	public final boolean isDestroyed() {
		return this.isDestroyed;
	}

	public int getLive() { return this.live; }
}
