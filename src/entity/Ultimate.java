package entity;

import engine.DrawManager;

import java.awt.*;
import java.io.Serializable;

public class Ultimate extends Entity implements Serializable {

    private int speed = -6;

    public Ultimate(int positionX, int positionY) {
        super(positionX, positionY, 100*2, 100*2, Color.white);

        this.spriteType = DrawManager.SpriteType.Ultimate;
    }

    public final void update() {
        this.positionY += this.speed;
    }
}