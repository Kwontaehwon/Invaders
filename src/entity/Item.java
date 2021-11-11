package entity;

import engine.Cooldown;
import engine.Core;
import engine.DrawManager;

import java.awt.*;

public class Item extends Entity{


    // 아이템떨어지는 속도.
    private int speed = 2;

    /** Cooldown between sprite changes. */
    // 이부분을활용하여 떨어질떄 애니메이션을 줄수있음. EnemyShip.java, update부분보면됨.
    //private Cooldown animationCooldown;

    /**
     * Constructor, establishes the entity's generic properties.
     *
     * @param positionX Initial position of the entity in the X axis.
     * @param positionY Initial position of the entity in the Y axis.
     *
     */
    public Item(int positionX, int positionY) {
        super(positionX, positionY, 13 * 2, 8 * 2, Color.BLUE);
        this.spriteType = DrawManager.SpriteType.Box;
    }

    public final void update() {

        this.positionY += this.speed;

    }

}
