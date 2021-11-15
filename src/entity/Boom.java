package entity;

import engine.DrawManager;

import java.awt.*;

public class Boom extends Entity {

    private int speed ;
    /**
     * Constructor, establishes the entity's generic properties.
     *
     * @param positionX Initial position of the entity in the X axis.
     * @param positionY Initial position of the entity in the Y axis.

     */
    public Boom(int positionX, int positionY,int speed){
        super(positionX, positionY, 8 * 2, 8 * 2, Color.white);

        this.speed = speed;
        this.spriteType = DrawManager.SpriteType.Boom;
    }

    public final void update() {
        this.positionY += this.speed;
    }

    public final void setSpeed(final int speed) {
        this.speed = speed;
    }

    public final int getSpeed() {
        return this.speed;
    }

}
