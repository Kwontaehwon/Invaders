package entity;

import engine.DrawManager;

import java.awt.*;

public class Item extends Entity{


    /** Movement of the item for each unit of time. */
    private int speed;
    /** Point of bonus item */
    private int pointValue;

    /**
     * Constructor, establishes the entity's generic properties.
     *
     * @param positionX Initial position of the entity in the X axis.
     * @param positionY Initial position of the entity in the Y axis.
     * @param spriteType Sprite of the entity.
     */

    public Item(int positionX, int positionY, DrawManager.SpriteType spriteType) {
        super(positionX, positionY, 13 * 2, 8 * 2, Color.BLUE);
        this.spriteType = spriteType;
        this.speed = 2;
        switch (this.spriteType) {
            case BonusScoreItem1:
                this.pointValue = 50;
                this.speed = 2;
                break;
            case BonusScoreItem2:
                this.pointValue = 100;
                this.speed = 4;
                break;
            case BonusScoreItem3:
                this.pointValue = 300;
                this.speed = 6;
                break;
            default:
                this.pointValue = 0;
                break;
        }
    }

    /**
     * Update the item's position.
     */
    public final void update() {
        this.positionY += this.speed;
    }

    /**
     * Getter of the item's speed.
     * @return speed of the item.
     */
    public int getSpeed(){
        return this.speed;
    }

    /**
     * Getter of the item's point.
     * @return point
     */
    public int getPointValue(){
        return this.pointValue;
    }
}
