package entity;

import engine.DrawManager;

import java.awt.*;

public class Boom extends Entity {

    private int speedX ;
    private int speedY ;
    /**
     * Constructor, establishes the entity's generic properties.
     *
     * @param positionX Initial position of the entity in the X axis.
     * @param positionY Initial position of the entity in the Y axis.

     */
    public Boom(int positionX, int positionY,int speedX, int speedY){
        super(positionX, positionY, 16 * 2, 16 * 2, Color.white);

        this.speedX = speedX;
        this.speedY = speedY;
        this.spriteType = DrawManager.SpriteType.Boom;
    }

    public final void update() {
        this.positionX += this.speedX;
        this.positionY += this.speedY;
    }

    public final void setSpeed(final int speedX, final int speedY) {
        this.speedX = speedX;
        this.speedY = speedY;
    }


}
