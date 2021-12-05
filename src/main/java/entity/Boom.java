package entity;

import engine.DrawManager;

import java.awt.*;
import java.io.Serializable;

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
        super(positionX, positionY, 15 * 2, 13 * 2, Color.white);

        this.speedX = speedX;
        this.speedY = speedY;
        this.spriteType = DrawManager.SpriteType.Boom;
    }
    /**
     * Updates the boom's position.
     */
    public final void update() {
        this.positionX += this.speedX;
        this.positionY += this.speedY;
    }
    /**
     * Setter of the speeds of the boom.
     *
     * @param speedX
     * 				New speedX of the boom.
     * @param speedY
     * 				New speedY of the boom.
     */
    public final void setSpeed(final int speedX, final int speedY) {
        this.speedX = speedX;
        this.speedY = speedY;
    }


}
