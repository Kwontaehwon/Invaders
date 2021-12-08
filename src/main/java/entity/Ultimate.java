package entity;

import engine.DrawManager;

import java.awt.*;
import java.io.Serializable;

public class Ultimate extends Entity {

    /** Movement of the Ultimate entity */
    private int speed = -6;

    /**
     * Constructor, establishes the ultimate's properties.
     * @param positionX Initial position of the ultimate in x axis.
     * @param positionY Initial position of the ultimate in y axis.
     */
    public Ultimate(int positionX, int positionY) {
        super(positionX, positionY, 100*2, 100*2, Color.white);

        this.spriteType = DrawManager.SpriteType.Ultimate;
    }

    /**
     * Update position of the ultimate entity.
     */
    public final void update() {
        this.positionY += this.speed;
    }
}