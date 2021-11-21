package entity;

import engine.DrawManager;

import java.awt.*;

public class BossBullet extends Entity{
    /**
     * Speed of the bullet, positive or negative depending on direction -
     * positive is down.
     */
    private int speed;
    public int x;
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
    public BossBullet(final int positionX, final int positionY, final int speed, int x) {
        super(positionX, positionY, 3 * 2, 5 * 2, Color.WHITE);
        this.x = x;
        this.speed = speed;
        setSprite();
    }

    /**
     * Sets correct sprite for the bullet, based on speed.
     */
    public final void setSprite() {
        if (speed < 0)
            this.spriteType = DrawManager.SpriteType.Bullet;
        else
            this.spriteType = DrawManager.SpriteType.EnemyBullet;
    }

    /**
     * Updates the bullet's position.
     */
    public final void update() {
        switch (x){
            case 1:
                this.positionX += -7;
                this.positionY += this.speed;
                break;
            case 2:
                this.positionX += -6;
                this.positionY += this.speed;
                break;
            case 3:
                this.positionX += -5;
                this.positionY += this.speed;
                break;
            case 4:
                this.positionX += -4;
                this.positionY += this.speed;
                break;
            case 5:
                this.positionX += -3;
                this.positionY += this.speed;
                break;
            case 6:
                this.positionX += -2;
                this.positionY += this.speed;
                break;
            case 7:
                this.positionX += -0.1;
                this.positionY += this.speed;
                break;
            case 8:
                this.positionY += this.speed;
            case 9:
                this.positionX += 1;
                this.positionY += this.speed;
                break;
            case 10:
                this.positionX += 2;
                this.positionY += this.speed;
                break;
            case 11:
                this.positionX += 3.5;
                this.positionY += this.speed;
                break;
            case 12:
                this.positionX += 4;
                this.positionY += this.speed;
                break;
            case 13:
                this.positionX += 5;
                this.positionY += this.speed;
                break;
            case 14:
                this.positionX += 6;
                this.positionY += this.speed;
                break;
            case 15:
                this.positionX += 7;
                this.positionY += this.speed;
                break;
            default:
                this.positionY += this.speed;
                break;
        }
        ;
    }

    /**
     * Setter of the speed of the bullet.
     *
     * @param speed
     *            New speed of the bullet.
     */
    public final void setSpeed(final int speed) {
        this.speed = speed;
    }

    /**
     * Getter for the speed of the bullet.
     *
     * @return Speed of the bullet.
     */
    public final int getSpeed() {
        return this.speed;
    }
}
